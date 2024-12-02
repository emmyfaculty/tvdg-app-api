package com.tvdgapp.services.rider;

import com.tvdgapp.constants.Constants;
import com.tvdgapp.dtos.rider.*;
import com.tvdgapp.dtos.user.ChangePasswordDto;
import com.tvdgapp.exceptions.DuplicateEntityException;
import com.tvdgapp.exceptions.InvalidRequestException;
import com.tvdgapp.exceptions.ResourceNotFoundException;
import com.tvdgapp.exceptions.TvdgAppSystemException;
import com.tvdgapp.models.user.Role;
import com.tvdgapp.models.user.RoleType;
import com.tvdgapp.models.user.User;
import com.tvdgapp.models.user.rider.RiderUser;
import com.tvdgapp.populator.UpdateRiderUserPopulator;
import com.tvdgapp.populator.user.RiderUserPopulator;
import com.tvdgapp.repositories.User.rider.RiderRepository;
import com.tvdgapp.services.generic.TvdgEntityServiceImpl;
import com.tvdgapp.services.storage.FileStorageService;
import com.tvdgapp.services.user.RoleService;
import com.tvdgapp.utils.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.tvdgapp.exceptions.EntityType.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class RiderUserServiceImpl extends TvdgEntityServiceImpl<Long, RiderUser> implements RiderUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RiderUserServiceImpl.class);

    private final RiderRepository repository;
    private final FileStorageService fileStorageService;
    private final PasswordEncoder passwordEncoder;
    private final EmailTemplateUtils emailTemplateUtils;
    private final RoleService roleService;


    @Override
    @Transactional
    public RiderUser createRiderUser(RiderUserDto requestDto)  {

//        if (this.userEmailTaken(requestDto.getEmail())) {
//            throw new DuplicateEntityException(RIDER_USER, requestDto.getEmail());
//        }

//        // Check if the phone number is already taken
//        if (this.userPhoneTaken(requestDto.getPhone())) {
//            throw new DuplicateEntityException("Phone number " + requestDto.getPhone() + " already exist");
//        }

        // Check if the email is already taken
        if (this.userEmailTaken(requestDto.getEmail())) {
            throw new DuplicateEntityException(AFFILIATE_USER.name(), "email", requestDto.getEmail());
        }

        // Check if the username is already taken
        if (this.userPhoneTaken(requestDto.getPhone())) {
            throw new DuplicateEntityException(AFFILIATE_USER.name(), "username", requestDto.getPhone());
        }

        RiderUser user = this.createRiderUserModelEntity(requestDto);
        String plainPassword = this.createUserPassword(user);

        this.addRiderUserRoles(user, requestDto.getRoles());


        user = this.saveRiderUser(user);

        this.sendCreateCustomerUserEmail(user, plainPassword, requestDto.getLoginUrl());

        return user;
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

    private void sendCreateCustomerUserEmail(RiderUser riderUser, String plainPassword, String loginUrl) {
        try {
            this.emailTemplateUtils.sendCreatedCustomerEmail(riderUser, plainPassword, loginUrl);
        } catch (Exception e) {
            LOGGER.error("Cannot send create admin user email", e);
        }
    }


    private void addRiderUserRoles(RiderUser riderUser, Collection<Integer> requestRoles) {
        boolean specificRiderRoleSelected = false;

        if (CollectionUtils.isEmpty(requestRoles)) {
            Optional<Role> role = this.roleService.fetchByRoleKey(Constants.ROLE_RIDER);
            if (role.isEmpty()) {
                throw new TvdgAppSystemException("Role customer not defined");
            }
            this.addUserRoles(riderUser, List.of(role.get()));
        } else {
            Collection<Role> roles = this.roleService.getRoleByIds(requestRoles);
            for (Role role: roles) {
                if (Constants.ROLE_RIDER.equals(role.getRoleKey())) {
                    specificRiderRoleSelected = true;
                    break;
                }
            }
            if (!specificRiderRoleSelected) {
                Optional<Role> role = this.roleService.fetchByRoleKey(Constants.ROLE_CUSTOMER);
                if (role.isEmpty()) {
                    throw new TvdgAppSystemException("Role customer not defined");
                }
                roles.add(role.get());
            }
            if (CollectionUtils.isNotEmpty(roles)) {
                this.addUserRoles(riderUser, roles);
            }
        }
    }

    private void addUserRoles(RiderUser riderUser, Collection<Role> roles) {
        roles.forEach(role -> {
            if (role.getRoleType()!=null&&role.getRoleType().equals(RoleType.RIDER)) {
                riderUser.addRole(role);
            }
        });
    }

    @Override
    public RiderUserDetailResponseDto fetchRiderUserDetail(Long userId) {
        //TODO:remove redundant repository call
        RiderUser riderUser = this.repository.findRiderUserDetail(userId).orElseThrow(() ->
                new ResourceNotFoundException(RIDER_USER, String.valueOf(userId)));
        return this.convertToDetailDto(riderUser);
    }
    private RiderUserDetailResponseDto convertToDetailDto(RiderUser riderUser) {
        RiderUserDetailResponseDto dto = new RiderUserDetailResponseDto();
        dto.setId(riderUser.getId());
        dto.setTitle(riderUser.getTitle());
        if (null != riderUser.getProfilePic()) {
            dto.setProfilePic(FilePathUtils.buildFileUrl(fileStorageService.getStorageLocation(), FilePathUtils.buildCustomerUserProfilePicUploadPath(), riderUser.getProfilePic()));
        }
        dto.setEmail(riderUser.getEmail());
        dto.setFirstName(riderUser.getFirstName());
        dto.setLastName(riderUser.getLastName());
        dto.setProfilePic(riderUser.getProfilePic());
        dto.setPhoneCode(riderUser.getPhoneCode());
        dto.setPhone(riderUser.getPhone());
        dto.setStatus(riderUser.getStatus().name());
        dto.setDateOfBirth(String.valueOf(riderUser.getDateOfBirth()));
        dto.setEmployeeId(riderUser.getEmployeeId());

        return dto;
    }


    @Override
    public void changePassword(String email, ChangePasswordDto changePasswordDto) {
        Optional<RiderUser> user = this.repository.findByEmail(email);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException(CUSTOMER_USER, email);
        }
        if (user.get().getPassword() == null || !this.checkIfOldPasswordIsValid(changePasswordDto.getPassword(), user.get().getPassword())) {
            throw new InvalidRequestException("Invalid old password");
        }
        this.saveNewPassword(user.get(), changePasswordDto.getNewPassword());
    }

    @Override
    @Transactional
    public Optional<String> updateProfilePic(long userId, MultipartFile profilePic) {
        Optional<RiderUser> optionalRiderUser = this.repository.findRiderUserById(userId);
        if (optionalRiderUser.isEmpty()) {
            throw new ResourceNotFoundException(RIDER_USER, String.valueOf(userId));
        }

        RiderUser riderUser = optionalRiderUser.get();
        return storeFile(riderUser,profilePic);
    }

    private Optional<String> storeFile(RiderUser riderUser, MultipartFile profilePic) {
        if (FileUploadValidatorUtils.isFileUploaded(profilePic)) {
            try {
                //build file path
                String filename = FilePathUtils.buildUniqueFileName(profilePic);
                String filePath = FilePathUtils.buildRiderUserProfilePicUploadPath();
                String fileNamePath = filePath + File.separator + filename;
                //if edit mode delete existing file
                if(riderUser.getProfilePic()!=null) {
                    this.fileStorageService.deleteFile(filePath+File.separator+riderUser.getProfilePic());
                }
                //store file
                this.fileStorageService.storeFile(profilePic, fileNamePath);
                //add or update record
                riderUser.setProfilePic(filename);
                return Optional.of(this.fileStorageService.getStorageLocation()+File.separator+fileNamePath);
            } catch (Exception e) {
                LOGGER.error("Unable to store uploaded profile pic", e);
            }
        }
        return Optional.empty();
    }


    private boolean checkIfOldPasswordIsValid(final String oldPassword, final String userPassword) {
        return passwordEncoder.matches(oldPassword, userPassword);
    }

    private void saveNewPassword(User user, String newPassword) {
        user.setPassword(this.passwordEncoder.encode(newPassword));
        this.repository.save((RiderUser) user);
    }

    @Override
    public Page<ListRiderUserDto> listUsers(Pageable pageable) {
        return this.repository.listRiderUsers(PageUtils.normalisePageRequest(pageable));
    }
    @Override
    @Transactional
    public Optional<RiderUser> deleteRiderUserById(Long riderUserId) {
        Optional<RiderUser> riderUserOptional = repository.findById(riderUserId);
        riderUserOptional.ifPresent(repository::delete);
        return riderUserOptional;
    }



    @Override
    @Transactional
    public RiderUser updateRiderUser(long userId, UpdateRiderUserDetailDto dto) {

        Optional<RiderUser> riderUserOptional = this.repository.findById(userId);
        if (riderUserOptional.isEmpty()) {
            throw new ResourceNotFoundException(RIDER_USER, String.valueOf(userId));
        }

        RiderUser riderUser = riderUserOptional.get();
        this.updateRiderUserModelEntity(riderUser, dto);

        this.repository.save(riderUser);

        return riderUser;
    }

    public List<RiderUserResponseDto> getAllRiderUsers() {
        return repository.findAll().stream()
                .map(this::convertToRiderUserDto)
                .collect(Collectors.toList());
    }

    public Optional<RiderUserResponseDto> getRiderUserById(Long id) {
        return repository.findById(id).map(this::convertToRiderUserDto);
    }

    private RiderUserResponseDto convertToRiderUserDto(RiderUser user) {
        RiderUserResponseDto dto = new RiderUserResponseDto();
        dto.setId(user.getId());
        dto.setTitle(user.getTitle());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setStatus(user.getStatus().name());
        dto.setDateOfBirth(String.valueOf(user.getDateOfBirth()));
        // Set other necessary fields
        return dto;
    }

    private boolean userEmailTaken(String email) {
        return this.repository.existsByEmail(email);
    }

    private boolean userPhoneTaken(String phone) {
        return repository.findByPhone(phone).isPresent();
    }

    private RiderUser createRiderUserModelEntity(RiderUserDto riderUserDto) {
        RiderUser riderUser = new RiderUser();
        this.populateRiderModelEntity(riderUserDto, riderUser);
        return riderUser;
    }
    private void updateRiderUserModelEntity(RiderUser riderUser,UpdateRiderUserDetailDto riderUserDetailDto) {
        this.populateUpdateRiderModelEntity(riderUserDetailDto, riderUser);
    }

    private void populateRiderModelEntity(RiderUserDto riderUserDto, RiderUser riderUser) {
        RiderUserPopulator riderUserPopulator = new RiderUserPopulator();
        riderUserPopulator.populate(riderUserDto, riderUser);
    }
    private void populateUpdateRiderModelEntity(UpdateRiderUserDetailDto riderUserDetailDto, RiderUser riderUser) {
        UpdateRiderUserPopulator riderUserPopulator = new UpdateRiderUserPopulator();
        riderUserPopulator.populate(riderUserDetailDto, riderUser);
    }

    private RiderUser saveRiderUser(RiderUser riderUser) {
        return this.repository.save(riderUser);
    }

}
