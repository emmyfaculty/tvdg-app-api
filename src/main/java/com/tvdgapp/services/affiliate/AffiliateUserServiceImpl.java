package com.tvdgapp.services.affiliate;

import com.tvdgapp.constants.Constants;
import com.tvdgapp.constants.SchemaConstant;
import com.tvdgapp.dtos.affiliate.*;
import com.tvdgapp.dtos.auth.ResetPasswordDto;
import com.tvdgapp.dtos.shipment.ShipmentDto;
import com.tvdgapp.dtos.user.ChangePasswordDto;
import com.tvdgapp.exceptions.*;
import com.tvdgapp.mapper.AffiliateUserMapper;
import com.tvdgapp.mapper.ShipmentMapper;
import com.tvdgapp.models.commissionrate.CommissionRate;
import com.tvdgapp.models.shipment.SenderDetails;
import com.tvdgapp.models.shipment.Shipment;
import com.tvdgapp.models.shipment.ShipmentStatus;
import com.tvdgapp.models.user.Role;
import com.tvdgapp.models.user.User;
import com.tvdgapp.models.user.UserStatus;
import com.tvdgapp.models.user.affiliateuser.AffiliateBenefitRecord;
import com.tvdgapp.models.user.affiliateuser.AffiliateUser;
import com.tvdgapp.models.wallet.Wallet;
import com.tvdgapp.models.wallet.transaction.TransactionCategory;
import com.tvdgapp.models.wallet.transaction.TransactionStatus;
import com.tvdgapp.models.wallet.transaction.TransactionType;
import com.tvdgapp.models.wallet.transaction.WalletTransaction;
import com.tvdgapp.populator.AffiliateUserPopulator;
import com.tvdgapp.populator.UpdateAffiliateUserPopulator;
import com.tvdgapp.repositories.affiliate.AffiliateBenefitRecordRepository;
import com.tvdgapp.repositories.affiliate.AffiliateRepository;
import com.tvdgapp.repositories.commissionrate.CommissionRateRepository;
import com.tvdgapp.repositories.shipment.SenderDetailsRepository;
import com.tvdgapp.repositories.shipment.ShipmentRepository;
import com.tvdgapp.repositories.wallet.WalletRepository;
import com.tvdgapp.repositories.wallet.WalletTransactionRepository;
import com.tvdgapp.services.generic.TvdgEntityServiceImpl;
import com.tvdgapp.services.storage.FileStorageService;
import com.tvdgapp.services.system.SystemConfigurationService;
import com.tvdgapp.services.user.RoleService;
import com.tvdgapp.services.wallet.WalletService;
import com.tvdgapp.utils.*;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.tvdgapp.constants.RespMsgs.RESET_TOKEN_EXPIRED;
import static com.tvdgapp.constants.RespMsgs.RESET_TOKEN_NOT_EXIST;
import static com.tvdgapp.exceptions.EntityType.AFFILIATE_USER;
import static com.tvdgapp.exceptions.EntityType.CUSTOMER_USER;

@Service
@Slf4j
@RequiredArgsConstructor
public class AffiliateUserServiceImpl extends TvdgEntityServiceImpl<Long, AffiliateUser> implements AffiliateUserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AffiliateUserServiceImpl.class);

    private final AffiliateRepository repository;
    private final FileStorageService fileStorageService;
    private final PasswordEncoder passwordEncoder;
    private final CommissionRateRepository commissionRateRepository;
    private final SystemConfigurationService systemConfigurationService;
    private final EmailTemplateUtils emailTemplateUtils;
    private final RoleService roleService;
    private final WalletService walletService;
    private final ShipmentRepository shipmentRepository;
    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private final SenderDetailsRepository senderDetailsRepository;
    private final AffiliateUserMapper affiliateUserMapper;
    private final ShipmentMapper shipmentMapper;
    private final AffiliateBenefitRecordRepository affiliateBenefitRecordRepository;
    private final Logger logger = LoggerFactory.getLogger(AffiliateUserServiceImpl.class);


    @Value("${api.url-domain}")
    private @Nullable String urlDomain;

    @Override
    @Transactional
    public AffiliateUser createAffiliateUser(AffiliateUserDto requestDto) {

            // Check if the email is already taken
            if (this.userEmailTaken(requestDto.getEmail())) {
                throw new DuplicateEntityException(AFFILIATE_USER, requestDto.getEmail());
            }
            // Check if the username is already taken
            if (this.usernameTaken(requestDto.getUsername())) {
                throw new DuplicateEntityException("The username already exist", requestDto.getUsername());
            }
            // Check if the identificationNumber is already taken
            if (this.identificationNumberTaken(requestDto.getIdentificationNumber())) {
                throw new DuplicateEntityException("Identification number already exist,", requestDto.getIdentificationType());
            }

            // Create the AffiliateUser entity
            AffiliateUser user = this.createAffiliateUserModelEntity(requestDto);

            // Generate and set the user's password
            String password = this.createUserPassword(user, requestDto);

            // Fetch the role to assign and add it to the user
            Role role = roleService.fetchByRoleKey(Constants.ROLE_AFFILIATE)
                    .orElseThrow(() -> new RuntimeException("Role not found"));
            user.addRole(role);

            // Save the AffiliateUser
            user = this.saveAffiliateUser(user);

            // Create a wallet for the user
            this.walletService.createWalletForUser(user.getId(), requestDto.getCurrency());

            // Send verification email and notify admin
            this.sendPendingAffiliateUserVerificationEmail(user, password, requestDto.getLoginUrl());
            this.notifyAdminOfNewAffiliateUser(user);

            return user;
    }

    private String createUserPassword(User user, AffiliateUserDto affiliateUserDto) {
        String password = affiliateUserDto.getPassword();
        user.setPassword(affiliateUserDto.getPassword());
        this.encodeUserPassword(user);
        return password;
    }

    private void encodeUserPassword(User user) {
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
    }

    private void sendPendingAffiliateUserVerificationEmail(AffiliateUser affiliateUser, String plainPassword, String loginUrl) {
        try {
            this.emailTemplateUtils.sendPendingAffiliateUserEmail(affiliateUser, plainPassword, loginUrl);
        } catch (Exception e) {
            LOGGER.error("Cannot send create affiliate user email", e);
        }
    }
    private void notifyAdminOfNewAffiliateUser(AffiliateUser affiliateUser) {
        try {
            this.emailTemplateUtils.sendNotifyAdminOfNewAffiliateUserEmail(SchemaConstant.DEFAULT_COMPANY_EMAIL,affiliateUser);
        } catch (Exception e) {
            LOGGER.error("Cannot send create affiliate user email", e);
        }
    }
    @Override
    @Transactional
    public AffiliateUser approveAffiliateUser(Long userId) {
        AffiliateUser user = repository.findById(userId)
                .orElseThrow(() -> new TvdgException.EntityNotFoundException("AffiliateUser Not found"));
        user.setStatus(UserStatus.ACTIVE);
        user = repository.save(user);

        // Send notification email to the user
        this.sendAffiliateUserApprovalEmail(user);

        return user;
    }
    @Override
    @Transactional
    public AffiliateUser deactivateAffiliateUser(Long userId) {
        AffiliateUser user = repository.findById(userId)
                .orElseThrow(() -> new TvdgException.EntityNotFoundException("AffiliateUser Not found"));
        user.setStatus(UserStatus.INACTIVE);
        user = repository.save(user);

        return user;
    }

    private void sendAffiliateUserApprovalEmail(AffiliateUser affiliateUser) {
        try {
            this.emailTemplateUtils.sendAffiliateUserApprovalEmail(affiliateUser);
        } catch (Exception e) {
            LOGGER.error("Cannot send affiliate user approval email", e);
        }
    }

    @Override
    public AffiliateUserDetailResponseDto fetchAffiliateDetail(Long userId) {
        AffiliateUser affiliateUser = this.repository.findAffiliateUserDetail(userId).orElseThrow(() ->
                new ResourceNotFoundException(AFFILIATE_USER, String.valueOf(userId)));
        return this.convertEntityToDetailDto(affiliateUser);
    }

    @Override
    @Transactional
    public void publishForgotPasswordEmail(String email) {

        Optional<AffiliateUser> user = repository.findByEmail(email);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException(AFFILIATE_USER,email);
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

        Optional<AffiliateUser> user = this.findUserByPasswordResetToken(resetPasswordDto.getToken());
        if (user.isEmpty()) {
            throw new InvalidRequestException(RESET_TOKEN_NOT_EXIST);
        }
        this.resetPassword(user.get(),resetPasswordDto.getNewPassword());
    }

    private Optional<String> validatePasswordResetToken(String token) {
        if (token == null) {
            return Optional.of("Invalid token");
        }
        Optional<AffiliateUser> user = this.findUserByPasswordResetToken(token);
        if (user.isEmpty()) {
            return Optional.of(RESET_TOKEN_NOT_EXIST);
        }
        if (isExpiredToken(user.get())) {
            return Optional.of(RESET_TOKEN_EXPIRED);
        }
        return Optional.empty();
    }

    private Optional<AffiliateUser> findUserByPasswordResetToken(String token) {
        return this.repository.findCustomerByPasswordResetToken(token);
    }

    private boolean isExpiredToken(User user) {
        return user.getPasswordResetVldtyTerm() != null && user.getPasswordResetVldtyTerm().before(TvdgAppDateUtils.now());
    }

    private void resetPassword(AffiliateUser user, String password) {
        this.changePassword(user, password);
    }

    private void changePassword(AffiliateUser user, String newPassword) {
        user.setPassword(this.passwordEncoder.encode(newPassword));
        this.repository.save(user);
    }

    private void createPasswordResetTokenForUser(AffiliateUser user, String token) {
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
        Optional<AffiliateUser> user = this.repository.findByEmail(email);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException(CUSTOMER_USER, email);
        }
        if (user.get().getPassword() == null || !this.checkIfOldPasswordIsValid(changePasswordDto.getPassword(), user.get().getPassword())) {
            throw new InvalidRequestException("Invalid old password");
        }
        this.saveNewPassword(user.get(), changePasswordDto.getNewPassword());
    }

    @Override
    public void processReferral(Long shipmentId) {
        Shipment shipment = shipmentRepository.findById(shipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Shipment not found"));

        shipment.setStatus("COMPLETED");
        shipmentRepository.save(shipment);

        SenderDetails senderDetails = senderDetailsRepository.findByShipmentId(shipmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Sender details not found"));

        if (shipment.getReferralCode() != null && isRetailCustomer(shipment)) {
            AffiliateUser affiliateUser = repository.findByReferralCode(shipment.getReferralCode())
                    .orElseThrow(() -> new ResourceNotFoundException("Affiliate user not found"));

            if (!hasAffiliateBenefited(affiliateUser.getId(), senderDetails)) {
                Wallet wallet = walletRepository.findByUserId(affiliateUser.getId())
                        .orElseThrow(() -> new ResourceNotFoundException("Wallet not found"));

                BigDecimal commission = calculateCommission(shipment.getTransportCharge());
                wallet.setBalance(wallet.getBalance().add(commission));
                walletRepository.save(wallet);

                WalletTransaction transaction = new WalletTransaction();
                transaction.setWalletId(wallet.getWalletId());
                transaction.setAmount(commission);
                transaction.setTransactionType(TransactionType.CREDIT);
                transaction.setTransactionStatus(TransactionStatus.COMPLETED /*SUCCESS*/);
                transaction.setTransactionCategory(TransactionCategory.FUNDING /*.AFFILIATE_COMMISSION*/);
                transaction.setTransactionDate(LocalDateTime.now());
                transaction.setDescription("Commission for completed shipment " + shipmentId);
                walletTransactionRepository.save(transaction);

                // Save a record indicating the affiliate has benefited from this retail customer
                AffiliateBenefitRecord benefitRecord = new AffiliateBenefitRecord();
                benefitRecord.setAffiliateUserId(affiliateUser.getId());
                benefitRecord.setPhoneNumber(senderDetails.getPhoneNo());
                benefitRecord.setEmail(senderDetails.getEmail());
                benefitRecord.setCompany(senderDetails.getCompanyName());
                affiliateBenefitRecordRepository.save(benefitRecord);
            }
        }
    }

    private boolean isRetailCustomer(Shipment shipment) {
        // A customer is considered retail if there is no associated customerUser (i.e., not logged in)
        return shipment.getPricingLevelId() == null;
    }

    private boolean hasAffiliateBenefited(Long affiliateUserId, SenderDetails senderDetails) {
        // Check if the affiliate has already benefited from the retail customer
        List<AffiliateBenefitRecord> records = affiliateBenefitRecordRepository
                .findByAffiliateUserIdAndPhoneNumberOrEmailOrCompany(affiliateUserId, senderDetails.getPhoneNo(), senderDetails.getEmail(), senderDetails.getCompanyName());
        return !records.isEmpty();
    }

    @Override
    @Transactional
    public Optional<String> updateProfilePic(long userId, MultipartFile profilePic) {
        Optional<AffiliateUser> optionalAffiliateUser = this.repository.findAffiliateUserById(userId);
        if (optionalAffiliateUser.isEmpty()) {
            throw new ResourceNotFoundException(CUSTOMER_USER, String.valueOf(userId));
        }
        return storeFile(optionalAffiliateUser.get(),profilePic);
    }

    private Optional<String> storeFile(AffiliateUser affiliateUser, MultipartFile profilePic) {
        if (FileUploadValidatorUtils.isFileUploaded(profilePic)) {
            try {
                //build file path
                String filename = FilePathUtils.buildUniqueFileName(profilePic);
                String filePath = FilePathUtils.buildAdminUserProfilePicUploadPath();
                String fileNamePath = filePath + File.separator + filename;
                //if edit mode delete existing file
                if(affiliateUser.getProfilePic()!=null) {
                    this.fileStorageService.deleteFile(filePath+File.separator+affiliateUser.getProfilePic());
                }
                //store file
                this.fileStorageService.storeFile(profilePic, fileNamePath);
                //add or update record
                affiliateUser.setProfilePic(filename);
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
        this.repository.save((AffiliateUser) user);
    }

    private AffiliateUserDetailResponseDto convertEntityToDetailDto(AffiliateUser affiliateUser) {
        AffiliateUserDetailResponseDto detailDto = new AffiliateUserDetailResponseDto();
        AffiliateUserDetailDto affiliateUserDetailDto = detailDto.getAffiliateUserDto();

        affiliateUserDetailDto.setId(affiliateUser.getId());
        if (null != affiliateUser.getProfilePic()) {
            affiliateUserDetailDto.setProfilePic(FilePathUtils.buildFileUrl(fileStorageService.getStorageLocation(), FilePathUtils.buildCustomerUserProfilePicUploadPath(), affiliateUser.getProfilePic()));
        }
        affiliateUserDetailDto.setTittle(affiliateUser.getTitle());
        affiliateUserDetailDto.setEmail(affiliateUser.getEmail());
        affiliateUserDetailDto.setFirstName(affiliateUser.getFirstName());
        affiliateUserDetailDto.setLastName(affiliateUser.getLastName());
        affiliateUserDetailDto.setPhone(affiliateUser.getTelephoneNumber());
        affiliateUserDetailDto.setDateOfBirth(String.valueOf(affiliateUser.getDateOfBirth()));
        affiliateUserDetailDto.setPostalCode(affiliateUser.getPostalCode());
        affiliateUserDetailDto.setCity(affiliateUser.getCity());
        affiliateUserDetailDto.setState(affiliateUser.getState());
        affiliateUserDetailDto.setStreetAddress(affiliateUser.getStreetAddress());
        affiliateUserDetailDto.setIdentificationNumber(affiliateUser.getIdentificationNumber());
        affiliateUserDetailDto.setReferralCode(affiliateUser.getReferralCode());
        affiliateUserDetailDto.setGender(affiliateUser.getGender());
        affiliateUserDetailDto.setCountry(affiliateUser.getCountry());
        affiliateUserDetailDto.setUsername(affiliateUser.getUsername());

        return detailDto;

    }

    @Override
    public Page<ListAffiliateUserDto> listUsers(Pageable pageable) {
        return this.repository.listAffiliateUsers(PageUtils.normalisePageRequest(pageable));
    }
    @Override
    public List<AffiliateUserDtoResponse> getAllAffiliateUsers() {
        return repository.findAll().stream()
                .map(this::convertToAffiliateUserDto)
                .collect(Collectors.toList());
    }
    @Override
    public Optional<AffiliateUserDtoResponse> getAffiliateUserById(Long id) {
        return repository.findById(id).map(this::convertToAffiliateUserDto);
    }

    private AffiliateUserDtoResponse convertToAffiliateUserDto(AffiliateUser user) {
        AffiliateUserDtoResponse dto = new AffiliateUserDtoResponse();
        dto.setId(user.getId());
        dto.setTittle(user.getTitle());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getTelephoneNumber());
        dto.setStatus(user.getStatus().name());
        dto.setUsername(user.getUsername());
        dto.setStreetAddress(user.getStreetAddress());
        dto.setCity(user.getCity());
        dto.setState(user.getState());
        dto.setPostalCode(user.getPostalCode());
        dto.setCountry(user.getCountry());
        dto.setIdentificationNumber(user.getIdentificationNumber());
        dto.setGender(user.getGender());
        dto.setReferralCode(user.getReferralCode());
        dto.setDateOfBirth(String.valueOf(user.getDateOfBirth()));
        // Set other necessary fields
        return dto;
    }

    @Override
    @Transactional
    public void deleteAffiliateUser(Long userId) {
        Optional<AffiliateUser> optional = this.repository.findAffiliateUserById(userId);
        if (optional.isPresent()){
            AffiliateUser affiliateUser = optional.get();
            this.repository.delete(affiliateUser);
        }
    }


    @Override
    @Transactional
    public AffiliateUser updateAffiliateUser(long customerUserId, UpdateAffiliateUserDetailDto dto) {

        Optional<AffiliateUser> affiliateUser = this.repository.findAffiliateUserById(customerUserId);
        if (affiliateUser.isEmpty()) {
            throw new ResourceNotFoundException(CUSTOMER_USER, String.valueOf(customerUserId));
        }
        this.updateAffiliateUserModelEntity(dto);

        this.repository.save(affiliateUser.get());

        return affiliateUser.get();
    }

    private boolean userEmailTaken(String email) {
        return this.repository.existsByEmail(email);
    }
    private boolean usernameTaken(String username) {
        return this.repository.existsByUsername(username);
    }
    private boolean identificationNumberTaken(String identificationNumber) {
        return this.repository.existsByIdentificationNumber(identificationNumber);
    }

    private AffiliateUser createAffiliateUserModelEntity(AffiliateUserDto affiliateUserDto) {
        AffiliateUser affiliateUser = new AffiliateUser();
        this.populateAffiliateModelEntity(affiliateUserDto, affiliateUser);
        return affiliateUser;
    }
    private void updateAffiliateUserModelEntity(UpdateAffiliateUserDetailDto affiliateUserDto) {
        AffiliateUser affiliateUser = new AffiliateUser();
        this.populateUpdateAffiliateModelEntity(affiliateUserDto, affiliateUser);
    }

    private void populateAffiliateModelEntity(AffiliateUserDto affiliateUserDto, AffiliateUser affiliateUser) {
        AffiliateUserPopulator affiliateUserPopulator = new AffiliateUserPopulator();
        affiliateUserPopulator.populate(affiliateUserDto, affiliateUser);
    }
    private void populateUpdateAffiliateModelEntity(UpdateAffiliateUserDetailDto affiliateUserDto, AffiliateUser affiliateUser) {
        UpdateAffiliateUserPopulator affiliateUserPopulator = new UpdateAffiliateUserPopulator();
        affiliateUserPopulator.populate(affiliateUserDto, affiliateUser);
    }

    private AffiliateUser saveAffiliateUser(AffiliateUser affiliateUser) {
        return this.repository.save(affiliateUser);
    }

    public List<AffiliateResponseDto> listAllAffiliatesByStatus(String status) {
        UserStatus userStatus;
        try {
            userStatus = UserStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status value: " + status);
        }

        return repository.findAllByStatus(userStatus)
                .stream()
                .map(affiliateUserMapper::toAffiliateUserDTO)
                .collect(Collectors.toList());
    }

    public long getTotalAffiliateCountByStatus(String status) {
        UserStatus userStatus;
        try {
            userStatus = UserStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status value: " + status);
        }
        return repository.findAllByStatus(userStatus).size();
    }

    public AffiliateResponseDto getAffiliateByReferralCode(String referralCode) {
        return repository.findByReferralCode(referralCode)
                .map(affiliateUserMapper::toAffiliateUserDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Affiliate not found with referral code: " + referralCode));
    }

    public AffiliateResponseDto updateAffiliate(Long id, AffiliateUserDto affiliateUserDTO) {
        AffiliateUser affiliateUser = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Affiliate not found with id: " + id));
        affiliateUserMapper.toAffiliateUser(affiliateUserDTO);
        return affiliateUserMapper.toAffiliateUserDTO(repository.save(affiliateUser));
    }

    public List<ShipmentDto> listAllShipmentsByReferralCode(String referralCode) {
        return shipmentRepository.findAllByReferralCode(referralCode)
                .stream()
                .map(shipmentMapper::toShipmentDTO)
                .collect(Collectors.toList());
    }

    public AffiliateUser approveAffiliateRequest(Long id) {
        AffiliateUser affiliateUser = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Affiliate not found with id: " + id));
        affiliateUser.setStatus(UserStatus.APPROVED);
        repository.save(affiliateUser);
        logger.info("Affiliate approved with id: {}", id);
        return affiliateUser;
    }

    public AffiliateUser rejectAffiliateRequest(Long id) {
        AffiliateUser affiliateUser = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Affiliate not found with id: " + id));
        affiliateUser.setStatus(UserStatus.REJECTED);
        repository.save(affiliateUser);
        logger.info("Affiliate rejected with id: {}", id);
        return affiliateUser;
    }

    public BigDecimal getTotalEarningsByReferralCode(String referralCode) {
        BigDecimal totalSalesAmount = shipmentRepository.findAllByReferralCode(referralCode)
                .stream()
                .map(Shipment::getTransportCharge)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal commissionRate = calculateCommission(totalSalesAmount);
        return totalSalesAmount.multiply(commissionRate.divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP));
    }

    public BigDecimal getTotalSalesAmountByReferralCode(String referralCode) {
        return shipmentRepository.findAllByReferralCode(referralCode)
                .stream()
                .map(Shipment::getTransportCharge)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateCommission(BigDecimal totalSalesAmount) {
        CommissionRate commissionRate = commissionRateRepository.findByMinSalesAmountLessThanEqualAndMaxSalesAmountGreaterThanEqual(totalSalesAmount, totalSalesAmount)
                .orElseThrow(() -> new RuntimeException("Commission rate not found"));
        BigDecimal commissionPercentage = commissionRate.getCommissionPercentage();
        return totalSalesAmount.multiply(commissionPercentage).divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
    }

}
