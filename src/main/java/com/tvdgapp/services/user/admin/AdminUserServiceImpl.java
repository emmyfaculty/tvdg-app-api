package com.tvdgapp.services.user.admin;

import com.tvdgapp.constants.Constants;
import com.tvdgapp.constants.SchemaConstant;
import com.tvdgapp.dtos.auth.ResetPasswordDto;
import com.tvdgapp.dtos.user.ChangePasswordDto;
import com.tvdgapp.dtos.user.UpdateUserProfileDto;
import com.tvdgapp.dtos.user.UserProfileDto;
import com.tvdgapp.dtos.user.admin.*;
import com.tvdgapp.dtos.user.role.ListRoleDto;
import com.tvdgapp.exceptions.*;
import com.tvdgapp.mapper.AccessCodeMapper;
import com.tvdgapp.models.user.Role;
import com.tvdgapp.models.user.RoleType;
import com.tvdgapp.models.user.User;
import com.tvdgapp.models.user.UserStatus;
import com.tvdgapp.models.user.admin.AccessCode;
import com.tvdgapp.models.user.admin.AdminUser;
import com.tvdgapp.models.user.admin.GenerateCode;
import com.tvdgapp.populator.user.AdminUserPopulator;
import com.tvdgapp.repositories.User.UserRepository;
import com.tvdgapp.repositories.User.admin.AccessCodeRepository;
import com.tvdgapp.repositories.User.admin.AdminUserRepository;
import com.tvdgapp.services.generic.TvdgEntityServiceImpl;
import com.tvdgapp.services.storage.FileStorageService;
import com.tvdgapp.services.system.SystemConfigurationService;
import com.tvdgapp.services.user.RoleService;
import com.tvdgapp.utils.*;
import jakarta.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static com.tvdgapp.constants.RespMsgs.RESET_TOKEN_EXPIRED;
import static com.tvdgapp.constants.RespMsgs.RESET_TOKEN_NOT_EXIST;
import static com.tvdgapp.exceptions.EntityType.ADMIN_USER;


@Service("adminUserServiceImpl")
public class AdminUserServiceImpl extends TvdgEntityServiceImpl<Long, AdminUser> implements AdminUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminUserServiceImpl.class);

    private final AdminUserRepository repository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final EmailTemplateUtils emailTemplateUtils;
    private final SystemConfigurationService systemConfigurationService;
    private final FileStorageService fileStorageService;
    private final AccessCodeService accessCodeService;
    private final AccessCodeMapper accessCodeMapper;
    private final AccessCodeRepository accessCodeRepository;
//    private final CacheManager cacheManager;

    @Value("${api.url-domain}")
    private @Nullable String urlDomain;


    @Autowired
    public AdminUserServiceImpl(AdminUserRepository repository, PasswordEncoder passwordEncoder, RoleService roleService
            , EmailTemplateUtils emailTemplateUtils,SystemConfigurationService systemConfigurationService
    , FileStorageService fileStorageService,UserRepository userRepository, AccessCodeService accessCodeService,
                                AccessCodeMapper accessCodeMapper, AccessCodeRepository accessCodeRepository) {

        super(repository);
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.emailTemplateUtils = emailTemplateUtils;
        this.systemConfigurationService = systemConfigurationService;
        this.fileStorageService=fileStorageService;
        this.userRepository = userRepository;
        this.accessCodeService = accessCodeService;
        this.accessCodeMapper = accessCodeMapper;
        this.accessCodeRepository = accessCodeRepository;
//        this.cacheManager=cacheManager;

    }

    @Transactional
    @Override
    public AdminUser createUser(AdminUserRequestDto adminUserRequestDto) {

        if (this.userEmailTaken(adminUserRequestDto.getEmail())) {
            throw new DuplicateEntityException(ADMIN_USER, adminUserRequestDto.getEmail());
        }

        AdminUser adminUser = this.createAdminUserModelEntity(adminUserRequestDto);

        this.addUserRoles(adminUser, adminUserRequestDto.getRoles());

        String plainPassword = this.createUserPassword(adminUser);

        this.sendCreateAdminUserEmail(adminUser, plainPassword);

        var savedAdminUser = this.saveAdminUser(adminUser);

        // Check if generateCode is "yes", if so, generate the access code
        if ("yes".equalsIgnoreCase(adminUserRequestDto.getGenerateCode())) {

            this.accessCodeService.createAccessCode(savedAdminUser.getId());
        }

        return savedAdminUser;
    }

    private void addUserRoles(AdminUser adminUser, Collection<Integer> requestRoles) {
        boolean roleAdminMemberDefined = true;

        // Fetch all roles associated with the provided role IDs
        Collection<Role> roles = CollectionUtils.isEmpty(requestRoles)
                ? Collections.emptyList()
                : this.roleService.getRoleByIds(requestRoles);

        // If no roles are provided, assign the default 'ROLE_ADMIN_MEMBER'
        if (roles.isEmpty()) {
            Optional<Role> defaultRole = this.roleService.fetchByRoleKey(Constants.ROLE_ADMIN_MEMBER);
            if (defaultRole.isPresent()) {
                roles.add(defaultRole.get());
            } else {
                roleAdminMemberDefined = false;
            }
        } else {
            // Check if any specific admin roles are included
            boolean specificAdminRoleSelected = roles.stream()
                    .anyMatch(role -> Constants.ROLE_ADMIN.equals(role.getRoleKey()) ||
                            Constants.ROLE_FINANCE.equals(role.getRoleKey()) ||
                            Constants.ROLE_CUSTOMER_SERVICE_EXECUTIVE.equals(role.getRoleKey()));

            // If no specific admin role is selected, include all roles provided
            if (!specificAdminRoleSelected) {
                // No additional role is enforced here; all provided roles are valid
            }
        }

        // Add all valid roles to the admin user
        if (CollectionUtils.isNotEmpty(roles)) {
            this.addAdminUserRoles(adminUser, roles);
        }

        // Throw an exception if the default 'ROLE_ADMIN_MEMBER' is not defined and no roles were set
        if (!roleAdminMemberDefined) {
            throw new TvdgAppSystemException("Role admin member not defined");
        }
    }

    private String createUserPassword(User user) {
        String password = CommonUtils.generatePassword();
        user.setPassword(password);
        this.encodeUserPassword(user);
        return password;
    }

    private void encodeUserPassword(User user) {
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
    }


    @Override
    public Page<ListAdminUserDto> listUsers(Pageable pageable) {
        return this.repository.listAdminUsers(PageUtils.normalisePageRequest(pageable));
    }

    @Override
    public UserProfileDto fetchAdminUserProfile(String username) {
        Optional<AdminUser> adminUser = this.repository.findByEmail(username);
        if (adminUser.isEmpty()) {
            throw new ResourceNotFoundException(ADMIN_USER, username);
        }
        return this.convertToProfileDto(adminUser.get());
    }

    @Override
    @Transactional
    public void changePassword(String email, ChangePasswordDto changePasswordDto) {
        Optional<AdminUser> adminUser = this.repository.findByEmail(email);
        if (adminUser.isEmpty()) {
            throw new ResourceNotFoundException(ADMIN_USER, email);
        }
        if (adminUser.get().getPassword()==null||!this.checkIfValidOldPassword(changePasswordDto.getPassword(), adminUser.get().getPassword())) {
            throw new InvalidRequestException("Invalid old password");
        }
        this.changePassword(adminUser.get(), changePasswordDto.getNewPassword());

    }

    private void changePassword(User user, String newPassword) {
        user.setPassword(this.passwordEncoder.encode(newPassword));
        this.repository.save((AdminUser) user);
    }

    private boolean checkIfValidOldPassword(final String oldPassword, final String userPassword) {
        return passwordEncoder.matches(oldPassword, userPassword);
    }


    @Override
    public AdminUserDetailResponseDto fetchAdminUserDetail(Long userId) {
        Optional<AdminUser> adminUserOpt = this.repository.findAdminUserDetail(userId);
        if (adminUserOpt.isEmpty()) {
            throw new ResourceNotFoundException(ADMIN_USER, String.valueOf(userId));
        }
        AdminUser adminUser = adminUserOpt.get();

        AdminUserDetailDto detailDto = this.convertToDetailDto(adminUser);

        Collection<ListRoleDto> roles = this.roleService.listRolesForAdmin();
        return createAdminUserDetailResponseDto(detailDto, roles);
    }

    @Override
    @Transactional
    //@CacheEvict(value="adminUserAuthInfo", key = "#userId")
    public AdminUser updateAdminUser(Long userId, AdminUserRequestDto dto) {

        Optional<AdminUser> optional = this.repository.findAdminUserDetail(userId);
        if (optional.isEmpty()) {
            throw new ResourceNotFoundException(ADMIN_USER, String.valueOf(userId));
        }

        AdminUser adminUser = optional.get();

        this.checkUniqueUserEmailIfEmailHasChanged(dto, adminUser);

        this.populateAdminUserModel(dto, adminUser);

        this.updateAdminUserRoles(adminUser, dto.getRoles());

        adminUser=updateUser(adminUser);
        return adminUser;
    }

    @Override
    @Transactional
    public void deactivateUser(Long userId) {
        this.performActivationRequest(userId, UserStatus.INACTIVE);
    }

    @Override
    @Transactional
    public void activateUser(Long userId) {
        this.performActivationRequest(userId, UserStatus.ACTIVE);
    }

    @Override
    @Transactional
    public void publishForgotPasswordEmail(String email) {

        Optional<User> user = userRepository.findAuthUserByEmail(email);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException(EntityType.USER,email);
        }

        final String token = UUID.randomUUID().toString();
        this.createPasswordResetTokenForUser(user.get(),token);

        String passwordResetUrl = this.createPasswordResetUrl(token);

        this.publishPasswordResetEmail(user.get(),passwordResetUrl);

    }

    private void publishPasswordResetEmail(User user, String passwordResetUrl) {
        try {
            this.emailTemplateUtils.sendPasswordResetEmail(user,passwordResetUrl);
        } catch (Exception e) {
            LOGGER.error("Cannot send email to user ", e);
        }
    }


    private String createPasswordResetUrl(String token) {
        return urlDomain + Constants.PASSWORD_RESET_URL +Constants.ADMIN_URI+ "/"+ token;

    }

    private void performActivationRequest(Long userId, UserStatus status) {
        Optional<AdminUser> adminUser = this.repository.findById(userId);
        if (adminUser.isEmpty()) {
            throw new ResourceNotFoundException(ADMIN_USER, String.valueOf(userId));
        }
        adminUser.get().setStatus(status);
    }


    private AdminUser updateUser(AdminUser adminUser) {
        return this.repository.save(adminUser);
    }


    private void checkUniqueUserEmailIfEmailHasChanged(AdminUserRequestDto dto, AdminUser adminUser) {
        if (!dto.getEmail().equals(adminUser.getEmail())) {
            if (this.userEmailTaken(dto.getEmail(), adminUser.getId())) {
                throw new DuplicateEntityException(ADMIN_USER, dto.getEmail());
            }
        }
    }

    private AdminUserDetailResponseDto createAdminUserDetailResponseDto(AdminUserDetailDto detailDto, Collection<ListRoleDto> roles) {
        AdminUserDetailResponseDto userDetailResponseDto = new AdminUserDetailResponseDto();
        userDetailResponseDto.setAdminUserDto(detailDto);
        Map<String, Object> extras = new HashMap<>();
        extras.put("roles", roles);
        extras.put("status", UserStatus.toItemList());
        userDetailResponseDto.setExtras(extras);

//        // Fetch Access Code
//        AccessCode accessCode = accessCodeRepository.findByUserId(detailDto.getUserId()).orElse(null);
//        AccessCodeDTO accessCodeDto = accessCode != null ? accessCodeMapper.toDto(accessCode) : null;
//        userDetailResponseDto.setAccessCode(accessCodeDto);

        return userDetailResponseDto;
    }

    private AdminUserDetailDto convertToDetailDto(AdminUser adminUser) {
        AdminUserDetailDto dto = new AdminUserDetailDto();
        dto.setUserId(adminUser.getId());
        dto.setEmail(adminUser.getEmail());
        dto.setFirstName(adminUser.getFirstName());
        dto.setLastName(adminUser.getLastName());
        dto.setPhone(adminUser.getTelephoneNumber());
        dto.setStatus(adminUser.getStatus().name());

        AccessCode accessCode = accessCodeRepository.findByUserId(adminUser.getId()).orElse(null);
        String accessCodeDto = accessCode.getAccessCode() != null ? accessCode.getAccessCode() : null;
        dto.setAccessCode(accessCodeDto);
        dto.setRoles(adminUser.getRoles().stream().map(Role::getId).collect(Collectors.toList()));
        return dto;
    }


    private UserProfileDto convertToProfileDto(AdminUser adminUser) {
        UserProfileDto dto = new UserProfileDto();
        dto.setUserId(adminUser.getId());
        dto.setEmail(adminUser.getEmail());
        dto.setFirstName(adminUser.getFirstName());
        dto.setLastName(adminUser.getLastName());
        dto.setPhone(adminUser.getTelephoneNumber());
        AccessCode accessCode = accessCodeRepository.findByUserId(adminUser.getId()).orElse(null);
        String accessCodeDto = accessCode.getAccessCode() != null ? accessCode.getAccessCode() : null;
        dto.setAccessCode(accessCodeDto);
        if(adminUser.getProfilePic()!=null) {
            dto.setProfilePic(FilePathUtils.buildFileUrl(fileStorageService.getStorageLocation(), FilePathUtils.buildAdminUserProfilePicUploadPath(), adminUser.getProfilePic()));
        }else{
            dto.setProfilePic("");
        }
        return dto;
    }

    private boolean userEmailTaken(String email) {
        return this.repository.existsByEmail(email);
    }

    private boolean userEmailTaken(String email, Long id) {
        return repository.existsByEmailAndIdNot(email, id);
    }

    private AdminUser saveAdminUser(AdminUser adminUser) {
        return this.repository.save(adminUser);
    }

    private AdminUser createAdminUserModelEntity(AdminUserRequestDto adminUserRequestDto) {
        AdminUser adminUser = new AdminUser();
        this.populateAdminUserModel(adminUserRequestDto, adminUser);
        return adminUser;
    }

    private void populateAdminUserModel(AdminUserRequestDto adminUserRequestDto, AdminUser adminUser) {
        AdminUserPopulator populator=new AdminUserPopulator();
        populator.populate(adminUserRequestDto,adminUser);
    }

    /**
     * add user roles
     * only admin type role can be assigned to admin users
     *
     * @param adminUser
     * @param roles
     */
    private void addAdminUserRoles(AdminUser adminUser, Collection<Role> roles) {
        roles.forEach(role -> {
            if (role.getRoleType()!=null&&role.getRoleType().equals(RoleType.ADMIN)) {
                adminUser.addRole(role);
                System.out.println("Added role: " + role.getName());
            }
        });
    }

    private void updateAdminUserRoles(AdminUser adminUser, Collection<Integer> reqRoles) {
        this.removeAllAdminUserRoles(adminUser);
        this.addUserRoles(adminUser, reqRoles);
    }

    private void removeAllAdminUserRoles(AdminUser adminUser) {
        Collection<Role> definedRoles = new HashSet<>(adminUser.getRoles());//create a copy to avoid concurrent modification exception
        if (!CollectionUtils.isEmpty(definedRoles)) {
            definedRoles.forEach(adminUser::removeRole);
        }
    }

    private void sendCreateAdminUserEmail(AdminUser adminUser, String plainPassword) {
        try {
            this.emailTemplateUtils.sendCreateAdminUserEmail(adminUser, plainPassword);
        } catch (Exception e) {
            LOGGER.error("Cannot send create admin user email", e);
        }
    }

    public Optional findUserByEmail(String email) {
        return this.repository.findByEmail(email);
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordDto resetPasswordDto) {

        this.validatePasswordResetToken(resetPasswordDto.getToken()).ifPresent((it)->{
            throw new InvalidRequestException(it);
        });

        Optional<AdminUser> user = this.findUserByPasswordResetToken(resetPasswordDto.getToken());
        if (user.isEmpty()) {
            throw new InvalidRequestException(RESET_TOKEN_NOT_EXIST);
        }
        this.resetPassword(user.get(),resetPasswordDto.getNewPassword());
    }

    @Override
    public AdminUserDetailDto updateProfile(long userId, UpdateUserProfileDto dto) {
        Optional<AdminUser> optionalAdminUser = this.repository.findAdminUserDetail(userId);
        if (optionalAdminUser.isEmpty()) {
            throw new ResourceNotFoundException(ADMIN_USER, String.valueOf(userId));
        }
        AdminUser adminUser=optionalAdminUser.get();
        adminUser.setFirstName(dto.getFirstName());
        adminUser.setLastName(dto.getLastName());
        adminUser.setTelephoneNumber(dto.getPhone());

        this.updateUser(adminUser);

        return this.convertToDetailDto(adminUser);
    }

    @Override
    @Transactional
    public Optional<String> updateProfilePic(long userId, MultipartFile profilePic) {
        Optional<AdminUser> optionalAdminUser = this.repository.findAdminUserDetail(userId);
        if (optionalAdminUser.isEmpty()) {
            throw new ResourceNotFoundException(ADMIN_USER, String.valueOf(userId));
        }
      return storeFile(optionalAdminUser.get(),profilePic);
    }

//    @Override
//    public UserTrackingDto getAdminUserTrackingData(Collection<SearchCriteria> searchCriteria) {
//
//        GenericSpecification spec = new GenericSpecification<>();
//        spec.setSearchCriterias(searchCriteria);
//
//        return repository.getAdminUsersCount(spec);
//
//    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        Optional<AdminUser> optional = this.repository.findAdminUserDetail(userId);
        if (optional.isPresent()){
            AdminUser adminUser = optional.get();
            Set<Role> userRoles = adminUser.getRoles();
            if (this.isSuperAdministrator(userRoles)){
                throw  new InvalidRequestException("can not delete a super admin");
            }
            for (Role role : userRoles) {
                adminUser.removeRole(role);
            }
            this.repository.delete(adminUser);
        }
    }

    @Override
    public Optional<AdminUser> findById(long adminUserId) {
        return this.repository.findById(adminUserId);
    }

    @Override
    public Collection<AdminUser> findUsersByIds(Collection<Long> userIds) {
        return this.repository.findAllById(userIds);
    }

    @Override
    @Transactional
    public AdminUser createNewUser(AdminUser user) {
        Optional<Role> role = this.roleService.fetchByRoleKey(Constants.ROLE_ADMIN_MEMBER);
        role.ifPresent(user::addRole);
        return this.saveAdminUser(user);
    }

    private boolean isSuperAdministrator(Set<Role> userRoles) {
            return userRoles.stream().anyMatch(role ->role.getRoleKey() != null && role.getRoleKey().equals(Constants.ROLE_SUPER_ADMIN));
    }

    private Optional<String> storeFile(AdminUser adminUser, MultipartFile profilePic) {
        if (FileUploadValidatorUtils.isFileUploaded(profilePic)) {
            try {
                //build file path
                String filename = FilePathUtils.buildUniqueFileName(profilePic);
                String filePath = FilePathUtils.buildAdminUserProfilePicUploadPath();
                String fileNamePath = filePath + File.separator + filename;
                //if edit mode delete existing file
                if(adminUser.getProfilePic()!=null) {
                    this.fileStorageService.deleteFile(filePath+File.separator+adminUser.getProfilePic());
                }
                //store file
                this.fileStorageService.storeFile(profilePic, fileNamePath);
                //add or update record
                adminUser.setProfilePic(filename);
                return Optional.of(this.fileStorageService.getStorageLocation()+File.separator+fileNamePath);
            } catch (Exception e) {
                LOGGER.error("Unable to store uploaded profile pic", e);
            }
        }
        return Optional.empty();
    }

    private void createPasswordResetTokenForUser(User user, String token) {
        user.setPasswordResetToken(token);
        String valdtyTrm = this.systemConfigurationService.findConfigValueByKey(SchemaConstant.PSSWORD_SETTING_TOKEN_VLDTY_TRM);
        user.calculateTokenExpiryDate(valdtyTrm);
        this.userRepository.save(user);
    }

    private Optional<String> validatePasswordResetToken(String token) {
        if (token == null) {
            return Optional.of("Invalid token");
        }
        Optional<AdminUser> user = this.findUserByPasswordResetToken(token);
        if (user.isEmpty()) {
            return Optional.of(RESET_TOKEN_NOT_EXIST);
        }
        if (isExpiredToken(user.get())) {
            return Optional.of(RESET_TOKEN_EXPIRED);
        }
        return Optional.empty();
    }


    private boolean isExpiredToken(AdminUser user) {
        return user.getPasswordResetVldtyTerm() != null && user.getPasswordResetVldtyTerm().before(TvdgAppDateUtils.now());
    }

    private Optional<AdminUser> findUserByPasswordResetToken(String token) {
        return this.repository.findUserByPasswordResetToken(token);
    }

    private void resetPassword(User user, String password) {
        this.changePassword(user, password);
    }


}
