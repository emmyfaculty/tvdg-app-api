package com.tvdgapp.services.user;

import com.tvdgapp.constants.Constants;
import com.tvdgapp.constants.DateConstants;
import com.tvdgapp.constants.SchemaConstant;
import com.tvdgapp.dtos.auth.ResetPasswordDto;
import com.tvdgapp.dtos.auth.UserDto;
import com.tvdgapp.dtos.user.*;
import com.tvdgapp.exceptions.*;
import com.tvdgapp.models.user.Role;
import com.tvdgapp.models.user.RoleType;
import com.tvdgapp.models.user.User;
import com.tvdgapp.models.user.customer.CustomerUser;
import com.tvdgapp.populator.CustomerUserPopulator;
import com.tvdgapp.repositories.User.CustomerUserRepository;
import com.tvdgapp.services.generic.TvdgEntityServiceImpl;
import com.tvdgapp.services.image_resizer.ImageResizeService;
import com.tvdgapp.services.storage.FileStorageService;
import com.tvdgapp.services.system.SystemConfigurationService;
import com.tvdgapp.services.wallet.WalletService;
import com.tvdgapp.utils.*;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.tvdgapp.constants.RespMsgs.RESET_TOKEN_EXPIRED;
import static com.tvdgapp.constants.RespMsgs.RESET_TOKEN_NOT_EXIST;
import static com.tvdgapp.exceptions.EntityType.CUSTOMER_USER;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomerUserServiceImpl extends TvdgEntityServiceImpl<Long, CustomerUser> implements CustomerUserService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerUserService.class);


    private final CustomerUserRepository repository;
    private final FileStorageService fileStorageService;
    private final ImageResizeService imageResizerService;
    private final PasswordEncoder passwordEncoder;
    private final SystemConfigurationService systemConfigurationService;
    private final EmailTemplateUtils emailTemplateUtils;
    private final RoleService roleService;
    private final WalletService walletService;
    //    private final WalletRepository walletRepository;



    @Value("${api.url-domain}")
    private @Nullable String urlDomain;

//    private final EmailTemplateUtils emailTemplateUtils;


    @Override
    @Transactional
    public CustomerUser createCustomerUser(CustomerUserDto requestDto)  {

        if (this.userEmailTaken(requestDto.getEmail())) {
            throw new DuplicateEntityException(CUSTOMER_USER, requestDto.getEmail());
        }

        CustomerUser user = this.createCustomerUserModelEntity(requestDto);
        this.addCustomerUserRoles(user, requestDto.getRoles());
        String plainPassword = this.createUserPassword(user);

        user = this.saveCustomerUser(user);

        this.walletService.createWalletForUser(user.getId(), requestDto.getCurrency());

        this.sendCreateCustomerUserEmail(user, plainPassword, requestDto.getLoginUrl());


//        //todo:process asynchronously
//        this.storeFiles(user, profilePic);

//        this.sendCreateEngineerEmail(user);

        return user;
    }
    @Override
    @Transactional
    public CustomerUser updateCustomerUser(Long userId, UpdateCustomerUserDto updateCustomerUserDto) {
            logger.info("Updating CustomerUser with id: {}", userId);

            CustomerUser loggedInUser = repository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Resource not found"));

        logger.info("Updating CustomerUser with id: {}", loggedInUser.getId());

        loggedInUser.setTitle(updateCustomerUserDto.getTitle());
        loggedInUser.setFirstName(updateCustomerUserDto.getFirstName());
        loggedInUser.setLastName(updateCustomerUserDto.getLastName());
        loggedInUser.setEmail(updateCustomerUserDto.getEmail());
        loggedInUser.setTelephoneNumber(updateCustomerUserDto.getPhoneNumber());
        if (StringUtils.isNotEmpty(updateCustomerUserDto.getDateOfBirth())) {
            loggedInUser.setDateOfBirth(TvdgAppDateUtils.formatStringToDate(updateCustomerUserDto.getDateOfBirth(), DateConstants.DATE_INPUT_FORMAT));
        }

        CustomerUser updatedCustomerUser = repository.save(loggedInUser);

        logger.info("Updated CustomerUser with id: {}", loggedInUser.getId());
        return updatedCustomerUser;
    }

    @Transactional
    public CustomerUser updateCustomerUser(Long userId, UpdateCustomerAccountInfoDto updateDto) {

        logger.info("Updating CustomerUser with id: {}", userId);
        CustomerUser customerUser = repository.findById(userId)
                .orElseThrow(() -> new TvdgException.EntityNotFoundException("Customer user not found with id: " + userId));

        logger.info("Updating CustomerUser with id: {}", customerUser.getId());

        // Update the customer user entity with fields from the DTO
        customerUser.setCompanyName(updateDto.getCompanyName());
        customerUser.setCompanyContactName(updateDto.getContactName());
        customerUser.setCompanyRegNumber(updateDto.getCompanyRegistrationNumber());
        customerUser.setCompanyEmail(updateDto.getCompanyEmail());
        customerUser.setTelephoneNumber(updateDto.getPhoneNumber());
        customerUser.setNatureOfBusiness(updateDto.getNatureOfBusiness());
        customerUser.setIndustry(updateDto.getIndustry());
        customerUser.setPostalCode(updateDto.getPostalCode());
        customerUser.setCity(updateDto.getCity());
        customerUser.setState(updateDto.getState());
        customerUser.setAddress(updateDto.getStreetAddress());

        // Save the updated customer user entity
        CustomerUser updatedUser = repository.save(customerUser);

        // Return response DTO with the updated user ID
        logger.info("Updated CustomerUser with id: {}", customerUser.getId());

        return updatedUser;
    }

    private void addCustomerUserRoles(CustomerUser customerUser, Collection<Integer> requestRoles) {
        boolean specificCustomerRoleSelected = false;

        if (CollectionUtils.isEmpty(requestRoles)) {
            Optional<Role> role = this.roleService.fetchByRoleKey(Constants.ROLE_CUSTOMER);
            if (role.isEmpty()) {
                throw new TvdgAppSystemException("Role customer not defined");
            }
            this.addUserRoles(customerUser, List.of(role.get()));
        } else {
            Collection<Role> roles = this.roleService.getRoleByIds(requestRoles);
            for (Role role: roles) {
                if (Constants.ROLE_CUSTOMER.equals(role.getRoleKey())) {
                    specificCustomerRoleSelected = true;
                    break;
                }
            }
            if (!specificCustomerRoleSelected) {
                Optional<Role> role = this.roleService.fetchByRoleKey(Constants.ROLE_CUSTOMER);
                if (role.isEmpty()) {
                    throw new TvdgAppSystemException("Role customer not defined");
                }
                roles.add(role.get());
            }
            if (CollectionUtils.isNotEmpty(roles)) {
                this.addUserRoles(customerUser, roles);
            }
        }
    }

    private void addUserRoles(CustomerUser customerUser, Collection<Role> roles) {
        roles.forEach(role -> {
            if (role.getRoleType()!=null&&role.getRoleType().equals(RoleType.CUSTOMER)) {
                customerUser.addRole(role);
            }
        });
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

    private void sendCreateCustomerUserEmail(CustomerUser customerUser, String plainPassword, String loginUrl) {
        try {
            this.emailTemplateUtils.sendCreatedCustomerEmail(customerUser, plainPassword, loginUrl);
        } catch (Exception e) {
            logger.error("Cannot send create admin user email", e);
        }
    }



    @Override
    public CustomerUserDetailDto fetchCustomerUserDetail(Long customerUserId) {
        //TODO:remove redundant repository call
        CustomerUser customerUser = this.repository.findCustomerUserById(customerUserId).orElseThrow(() ->
                new ResourceNotFoundException(CUSTOMER_USER, String.valueOf(customerUserId)));
        return this.convertEntityToDetaiDto(customerUser);
    }

    public List<CustomerUserDtoResponse> getAllCustomerUsers() {
        return repository.findAll().stream()
                .map(this::convertToCustomerUserDto)
                .collect(Collectors.toList());
    }

//    public Optional<CustomerUserDtoResponse> getCustomerUserById(Long id) {
//        return repository.findById(id).map(this::convertToCustomerUserDto);
//    }
    public CustomerUserDtoResponse getCustomerUserById(Long id) {
        return repository.findById(id)
                .map(this::convertToCustomerUserDto)
                .orElseThrow(() -> new InvalidUserIdException("Invalid user ID: " + id));
    }

    private CustomerUserDtoResponse convertToCustomerUserDto(CustomerUser customerUser) {
        CustomerUserDtoResponse dto = new CustomerUserDtoResponse();
//        Wallet wallet = this.walletRepository.findByUserId(customerUser.getId());

        com.tvdgapp.dtos.auth.UserDto user = new UserDto(
                customerUser.getId(),
                customerUser.getFirstName(),
                customerUser.getLastName(),
                customerUser.getEmail(),
                customerUser.getTelephoneNumber(),
                customerUser.getProfilePic(),
                customerUser.getStatus().name(),
                customerUser.getLastLogin()
        );

        String dateOfBirth = customerUser.getDateOfBirth() != null ? customerUser.getDateOfBirth().toString() : "null";

        CustomerDtoResponse customerDtoResponse = new CustomerDtoResponse(
                customerUser.getTitle(),
                customerUser.getCompanyName(),
                customerUser.getCompanyContactName(),
                customerUser.getCompanyEmail(),
                customerUser.getCompanyPhoneNo(),
                customerUser.getCompanyRegNumber(),
                customerUser.getDesignation(),
                customerUser.getAddress(),
                customerUser.getCity(),
                customerUser.getState(),
                customerUser.getPostalCode(),
                customerUser.getIndustry(),
                dateOfBirth,
                customerUser.getNatureOfBusiness(),
                customerUser.getCustomerType(),
                customerUser.getPricingLevelId()
        );

//        WalletDto walletDto = new WalletDto(
//                wallet.getWalletId(),
//                wallet.getUserId(),
//                wallet.getBalance(),
//                wallet.getWalletNumber()
//        );

        dto.setUser(user);
        dto.setCustomer(customerDtoResponse);
//        dto.setWallet(walletDto);

        return dto;
    }

    @Override
    @Transactional
    public void publishForgotPasswordEmail(String email) {

        Optional<CustomerUser> user = repository.findByEmail(email);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException(EntityType.USER,email);
        }

        final String token = UUID.randomUUID().toString();
        this.createPasswordResetTokenForUser(user.get(),token);

        String passwordResetUrl = this.createPasswordResetUrl(token);

        this.publishPasswordResetEmail(user.get(),passwordResetUrl);

    }
    @Override
    @Transactional
    public void resetPassword(ResetPasswordDto resetPasswordDto) {

        this.validatePasswordResetToken(resetPasswordDto.getToken()).ifPresent((it)->{
            throw new InvalidRequestException(it);
        });

        Optional<CustomerUser> user = this.findUserByPasswordResetToken(resetPasswordDto.getToken());
        if (user.isEmpty()) {
            throw new InvalidRequestException(RESET_TOKEN_NOT_EXIST);
        }
        this.resetPassword(user.get(),resetPasswordDto.getNewPassword());
    }

    private Optional<String> validatePasswordResetToken(String token) {
        if (token == null) {
            return Optional.of("Invalid token");
        }
        Optional<CustomerUser> user = this.findUserByPasswordResetToken(token);
        if (user.isEmpty()) {
            return Optional.of(RESET_TOKEN_NOT_EXIST);
        }
        if (isExpiredToken(user.get())) {
            return Optional.of(RESET_TOKEN_EXPIRED);
        }
        return Optional.empty();
    }

    private Optional<CustomerUser> findUserByPasswordResetToken(String token) {
        return this.repository.findCustomerByPasswordResetToken(token);
    }

    private boolean isExpiredToken(User user) {
        if (user.getPasswordResetVldtyTerm()!=null&&user.getPasswordResetVldtyTerm().before(TvdgAppDateUtils.now())) {
            return true;
        }
        return false;
    }

    private void resetPassword(CustomerUser user, String password) {
        this.changePassword(user, password);
    }

    private void changePassword(CustomerUser user, String newPassword) {
        user.setPassword(this.passwordEncoder.encode(newPassword));
        this.repository.save(user);
    }

    private void createPasswordResetTokenForUser(CustomerUser user, String token) {
        user.setPasswordResetToken(token);
        String valdtyTrm = this.systemConfigurationService.findConfigValueByKey(SchemaConstant.PSSWORD_SETTING_TOKEN_VLDTY_TRM);
        user.calculateTokenExpiryDate(valdtyTrm);
        this.repository.save(user);
    }

    private String createPasswordResetUrl(String token) {
        return urlDomain + Constants.PASSWORD_RESET_URL + Constants.CUSTOMER_URI + "/" + token;
    }

    private void publishPasswordResetEmail(User user, String passwordResetUrl) {
        try {
            this.emailTemplateUtils.sendPasswordResetEmail(user, passwordResetUrl);
        } catch (Exception e) {
            log.error("Cannot send email to user ", e);
        }
    }


    @Override
    public void changePassword(String email, ChangePasswordDto changePasswordDto) {
        Optional<CustomerUser> user = this.repository.findByEmail(email);
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
        Optional<CustomerUser> optionalCustomerUser = this.repository.findCustomerUserById(userId);
        if (optionalCustomerUser.isEmpty()) {
            throw new ResourceNotFoundException(CUSTOMER_USER, String.valueOf(userId));
        }
        return storeFile(optionalCustomerUser.get(),profilePic);
    }

    private Optional<String> storeFile(CustomerUser customerUser, MultipartFile profilePic) {
        if (FileUploadValidatorUtils.isFileUploaded(profilePic)) {
            try {
                // Build file path
                String filename = FilePathUtils.buildUniqueFileName(profilePic);
                String filePath = FilePathUtils.buildAdminUserProfilePicUploadPath();
                String fileNamePath = filePath + File.separator + filename;

                // If edit mode, delete existing file
                if (customerUser.getProfilePic() != null) {
                    this.fileStorageService.deleteFile(filePath + File.separator + customerUser.getProfilePic());
                }

                // Store file
                this.fileStorageService.storeFile(profilePic, fileNamePath);

                // Add or update record
                customerUser.setProfilePic(filename);
                return Optional.of(this.fileStorageService.getStorageLocation() + File.separator + fileNamePath);
            } catch (Exception e) {
                logger.error("Unable to store uploaded profile pic", e);
            }
        }
        return Optional.empty();
    }


    private boolean checkIfOldPasswordIsValid(final String oldPassword, final String userPassword) {
        return passwordEncoder.matches(oldPassword, userPassword);
    }

    private void saveNewPassword(User user, String newPassword) {
        user.setPassword(this.passwordEncoder.encode(newPassword));
        this.repository.save((CustomerUser) user);
    }

    private CustomerUserDetailDto convertEntityToDetaiDto(CustomerUser customerUser) {
        CustomerUserDetailDto detailDto = new CustomerUserDetailDto();
        detailDto.setId(customerUser.getId());
        if (null != customerUser.getProfilePic()) {
            detailDto.setProfilePic(FilePathUtils.buildFileUrl(fileStorageService.getStorageLocation(), FilePathUtils.buildCustomerUserProfilePicUploadPath(), customerUser.getProfilePic()));
        }
        detailDto.setTittle(customerUser.getTitle());
        detailDto.setEmail(customerUser.getEmail());
        detailDto.setFirstName(customerUser.getFirstName());
        detailDto.setLastName(customerUser.getLastName());
        detailDto.setEmail(customerUser.getEmail());
        detailDto.setPhone(customerUser.getTelephoneNumber());
        detailDto.setDateOfBirth(String.valueOf(customerUser.getDateOfBirth()));
        detailDto.setCompanyName(customerUser.getCompanyName());
        detailDto.setCompanyContactName(customerUser.getCompanyContactName());
        detailDto.setCompanyEmail(customerUser.getCompanyEmail());
        detailDto.setCompanyPhoneNo(customerUser.getCompanyPhoneNo());
        detailDto.setNatureOfBusiness(customerUser.getNatureOfBusiness());
        detailDto.setIndustry(customerUser.getIndustry());
        detailDto.setPostalCode(customerUser.getPostalCode());
        detailDto.setCity(customerUser.getCity());
        detailDto.setState(customerUser.getState());
        detailDto.setAddress(customerUser.getAddress());
        detailDto.setCustomerType(customerUser.getCustomerType());
        detailDto.setPricingLevelId(customerUser.getPricingLevelId());
        detailDto.setShipmentCount(String.valueOf(customerUser.getShipmentCount()));

        return detailDto;
    }

    @Override
    @Transactional
    public CustomerUser updateCustomerUser(long customerUserIdUserId, CustomerUserDto dto, MultipartFile profilePic) {

        Optional<CustomerUser> customerUser = this.repository.findCustomerUserById(customerUserIdUserId);
        if (customerUser.isEmpty()) {
            throw new ResourceNotFoundException(CUSTOMER_USER, String.valueOf(customerUserIdUserId));
        }
        this.populateCustomerModelEntity(dto, customerUser.get());


        this.storeFiles(customerUser.get(), profilePic);

        this.repository.save(customerUser.get());

        return customerUser.get();
    }

    private boolean userEmailTaken(String email) {
        return this.repository.existsByEmail(email);
    }

    private CustomerUser createCustomerUserModelEntity(CustomerUserDto customerUserDto) {
        CustomerUser customerUserUser = new CustomerUser();
        this.populateCustomerModelEntity(customerUserDto, customerUserUser);
        return customerUserUser;
    }

    private void populateCustomerModelEntity(CustomerUserDto customerUserDto, CustomerUser customerUser) {
        CustomerUserPopulator customerUserPopulator = new CustomerUserPopulator();
        customerUserPopulator.populate(customerUserDto, customerUser);
    }

    private CustomerUser saveCustomerUser(CustomerUser customerUser) {
        return this.repository.save(customerUser);
    }

    private void storeFiles(CustomerUser customerUser, MultipartFile profilePic) {

        String filename, fileDir, fileNamePath;
        //store uploaded files
        //profilePic
        if (FileUploadValidatorUtils.isFileUploaded(profilePic)) {
            try {
                //build file path
                filename = FilePathUtils.buildUniqueFileName(profilePic);
                fileDir = FilePathUtils.buildCustomerUserProfilePicUploadPath();
                fileNamePath = fileDir + File.separator + filename;

                //if edit mode delete existing file
                if (customerUser.getProfilePic() != null) {
                    this.fileStorageService.deleteFile(fileDir + File.separator + customerUser.getProfilePic());
                }
                //resize for thumbnail todo:use lambda function for images stored in the cloud
                File thumbnail = this.resizeImage(profilePic);
                //store file
                this.fileStorageService.storeFile(profilePic, fileNamePath);
                //add or update record
                customerUser.setProfilePic(filename);
                //store thumbnail
                String thumbnailDir = FilePathUtils.buildEngineerThumbnailUploadPath();
                String thumbnailNamePath = thumbnailDir + File.separator + customerUser.getProfilePic();
                if (customerUser.getProfilePic() != null) {
                    this.fileStorageService.deleteFile(thumbnailDir + File.separator + customerUser.getProfilePic());
                }
                this.fileStorageService.storeFile(thumbnail, thumbnailNamePath);

            } catch (Exception e) {
                log.error("Unable to store uploaded profilePic", e);
            }
        }
    }

    private File resizeImage(MultipartFile profilePic) throws IOException {
        return this.imageResizerService.resizeImage(profilePic.getInputStream(), FilenameUtils.getExtension(profilePic.getOriginalFilename()), Constants.AVATAR_THUMBNAIL_WIDTH, Constants.AVATAR_THUMBNAIL_HEIGHT);
    }

}
