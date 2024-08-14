package com.tvdgapp.controllers.user.affiliate;

import com.tvdgapp.apiresponse.ApiDataResponse;
import com.tvdgapp.controllers.user.IAffiliateUserController;
import com.tvdgapp.dtos.affiliate.*;
import com.tvdgapp.dtos.common.FileUrlDto;
import com.tvdgapp.dtos.common.IdResponseDto;
import com.tvdgapp.dtos.shipment.ShipmentDto;
import com.tvdgapp.dtos.user.ChangePasswordDto;
import com.tvdgapp.exceptions.InvalidRequestException;
import com.tvdgapp.models.user.affiliateuser.AffiliateUser;
import com.tvdgapp.repositories.affiliate.AffiliateRepository;
import com.tvdgapp.securityconfig.SecuredUserInfo;
import com.tvdgapp.services.affiliate.AffiliateUserService;
import com.tvdgapp.utils.ApiResponseUtils;
import com.tvdgapp.utils.FileUploadValidatorUtils;
import com.tvdgapp.utils.UserInfoUtil;
import com.tvdgapp.validators.ValidationErrors;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static com.tvdgapp.validators.ValidatePassword.validateSamePassword;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.basepath-affiliate}")
public class AffiliateUserController implements IAffiliateUserController {
    public static final int MAX_PROFILE_PIC_UPLOAD_SIZE = 1024 * 1024 * 2; //2mb
    private final AffiliateUserService service;
    private final UserInfoUtil userInfoUtil;
    private final AffiliateRepository repository;
    @Override
    @PostMapping("/signup")
    public ResponseEntity<ApiDataResponse<IdResponseDto>> createAffiliateUser(@Valid @RequestBody AffiliateUserDto affiliateUserDto) {
        AffiliateUser affiliateUser = this.service.createAffiliateUser(affiliateUserDto);
        return ApiResponseUtils.response(HttpStatus.CREATED, new IdResponseDto(affiliateUser.getId()), "Resource created successfully");
    }


    @GetMapping
    @PreAuthorize("hasAuthority('affiliateUserManagement')")
    public ResponseEntity<ApiDataResponse<Page<ListAffiliateUserDto>>> listUsers(Pageable pageable){
        Page<ListAffiliateUserDto> list = this.service.listUsers(pageable);
        return ApiResponseUtils.response(HttpStatus.OK, list, "Resources retrieved successfully");
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAuthority('affiliateUserManagement')")
    public ResponseEntity<ApiDataResponse<IdResponseDto>> approveAffiliateUser(@PathVariable Long id) {
        AffiliateUser approvedUser = service.approveAffiliateUser(id);
        return ApiResponseUtils.response(HttpStatus.OK, new IdResponseDto(approvedUser.getId()), "Resource status updated successfully");
    }
    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasAuthority('affiliateUserManagement')")
    public ResponseEntity<ApiDataResponse<IdResponseDto>> deactivateAffiliateUser(@PathVariable Long id) {
        AffiliateUser approvedUser = service.deactivateAffiliateUser(id);
        return ApiResponseUtils.response(HttpStatus.OK, new IdResponseDto(approvedUser.getId()), "affiliate user deactivated successfully");
    }

    @PostMapping("/changepassword")
    public ResponseEntity<ApiDataResponse<Object>> changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto) throws Exception {
        validateSamePassword(changePasswordDto);
        SecuredUserInfo userInfo = (SecuredUserInfo) this.userInfoUtil.authenticatedUserInfo();
        this.service.changePassword(userInfo.getUsername(), changePasswordDto);
        return ApiResponseUtils.response(HttpStatus.OK, "Password changed successfully");
    }

    @Override
    @GetMapping("/details")
    @PreAuthorize("hasAnyAuthority('AFFILIATE', 'manageAffiliate')")
//    @PreAuthorize("hasAnyAuthority('manageAdminUser', 'viewAdminUser')")
    public ResponseEntity<ApiDataResponse<AffiliateUserDetailResponseDto>> fetchAffiliateUserDetail()  {
        SecuredUserInfo securedUserInfo = (SecuredUserInfo) this.userInfoUtil.AuthenticatedUserDetails();
        AffiliateUserDetailResponseDto userDetailResponseDto = this.service.fetchAffiliateDetail(securedUserInfo.getUserId());
        return ApiResponseUtils.response(HttpStatus.OK,userDetailResponseDto, "Resource retrieved successfully");
    }

    @GetMapping("/fetch")
    @PreAuthorize("hasAnyAuthority('affiliateUserManagement')")
    public ResponseEntity<ApiDataResponse<List<AffiliateUserDtoResponse>>> getAllAffiliateUsers() {
        List<AffiliateUserDtoResponse> list = this.service.getAllAffiliateUsers();
        return ApiResponseUtils.response(HttpStatus.OK, list,"Resource retrieved successfully");
    }

    @GetMapping("/affiliates/{id}")
    @PreAuthorize("hasAnyAuthority('affiliateUserManagement')")
    public ResponseEntity<ApiDataResponse<Optional<AffiliateUserDtoResponse>>> getAffiliateUserById(@PathVariable Long id) {
        return ApiResponseUtils.response(HttpStatus.OK, this.service.getAffiliateUserById(id), "Resource successfully retrieved");

    }

    @Override
    @PutMapping("/profile")
    @PreAuthorize("hasAnyAuthority('AFFILIATE', 'manageAffiliate')")
    public ResponseEntity<ApiDataResponse<IdResponseDto>> updateAffiliateUserProfile(@Valid @RequestBody UpdateAffiliateUserDetailDto affiliateUserDto) {
        var devUserInfo = (SecuredUserInfo) this.userInfoUtil.authenticatedUserInfo();
        this.service.updateAffiliateUser(devUserInfo.getUser().getId(), affiliateUserDto);
        return ApiResponseUtils.response(HttpStatus.OK, "Resource updated successfully");
    }

    @Override
    @PutMapping("/profilePic")
    @PreAuthorize("hasAnyAuthority('AFFILIATE', 'manageAffiliate')")
    public ResponseEntity<ApiDataResponse<FileUrlDto>> updateProfilePic(@RequestPart(value = "profile_pic_upload") MultipartFile profileFileUpload) {
        validateProfilePicUpload(profileFileUpload);
        SecuredUserInfo userInfo = (SecuredUserInfo) this.userInfoUtil.authenticatedUserInfo();
        String fileUrl = this.service.updateProfilePic(userInfo.getUser().getId(), profileFileUpload).orElseThrow(() ->
                new InvalidRequestException("unable to update file"));
        return ApiResponseUtils.response(HttpStatus.OK, new FileUrlDto(fileUrl), "Resource updated successfully");
    }
    @Override
    @DeleteMapping("/{userId}")
    @PreAuthorize("hasAuthority('affiliateUserManagement')")
//    @PreAuthorize("hasAuthority('manageAdminUser')")
    public ResponseEntity<ApiDataResponse<String>> deleteUser(@PathVariable Long userId) throws Exception {
        this.service.deleteAffiliateUser(userId);
        return ApiResponseUtils.response(HttpStatus.NO_CONTENT, "Resource deleted successfully");
    }

    private void validateProfilePicUpload(MultipartFile profileFileUpload) {
        ValidationErrors validationErrors = new ValidationErrors();
        List<String> mimeTypes = List.of("jpg", "jpeg", "gif", "png");
        FileUploadValidatorUtils.validateRequired(profileFileUpload, validationErrors);
        FileUploadValidatorUtils.validateFileType(profileFileUpload, mimeTypes, validationErrors);
        FileUploadValidatorUtils.validateSize(profileFileUpload, MAX_PROFILE_PIC_UPLOAD_SIZE, validationErrors);
        if (validationErrors.hasErrors()) {
            throw new InvalidRequestException(validationErrors);
        }

    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAuthority('affiliateUserManagement')")
    public ResponseEntity<ApiDataResponse<List<AffiliateResponseDto>>> listAllAffiliatesByStatus(@PathVariable String status) {
        List<AffiliateResponseDto> list = this.service.listAllAffiliatesByStatus(status);
        return ApiResponseUtils.response(HttpStatus.OK, list,"Resource retrieved successfully");
    }

    @GetMapping("/count/status/{status}")
    @PreAuthorize("hasAuthority('affiliateUserManagement')")
    public ResponseEntity<ApiDataResponse<Long>> getTotalAffiliateCountByStatus(@PathVariable String status) {
        Long count = service.getTotalAffiliateCountByStatus(status);
        return ApiResponseUtils.response(HttpStatus.OK, count,"Resource counted successfully");
    }

    @GetMapping("/referral/{referralCode}")
    @PreAuthorize("hasAnyAuthority('affiliateUserManagement')")
    public ResponseEntity<ApiDataResponse<AffiliateResponseDto>> getAffiliateByReferralCode(@PathVariable String referralCode) {
        AffiliateResponseDto affiliateUserDto = service.getAffiliateByReferralCode(referralCode);
        return ApiResponseUtils.response(HttpStatus.OK, affiliateUserDto,"Resource retrieved successfully");

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('affiliateUserManagement')")
    public ResponseEntity<AffiliateResponseDto> updateAffiliate(@PathVariable Long id, @RequestBody AffiliateUserDto affiliateUserDTO) {
        return ResponseEntity.ok(service.updateAffiliate(id, affiliateUserDTO));
    }

    @GetMapping("/shipments/{referralCode}")
    @PreAuthorize("hasAnyAuthority('affiliateUserManagement', 'manageAffiliate')")
    public ResponseEntity<ApiDataResponse<List<ShipmentDto>>> listAllShipmentsByReferralCode(@PathVariable String referralCode) {
        List<ShipmentDto> shipmentDto = service.listAllShipmentsByReferralCode(referralCode);
        return ApiResponseUtils.response(HttpStatus.OK, shipmentDto,"Resource retrieved successfully");
    }

    @PostMapping("/approve/{id}")
    @PreAuthorize("hasAuthority('affiliateUserManagement')")
    public ResponseEntity<ApiDataResponse<IdResponseDto>> approveAffiliateRequest(@PathVariable Long id) {
        AffiliateUser approvedUser = service.approveAffiliateRequest(id);
        return ApiResponseUtils.response(HttpStatus.OK, new IdResponseDto(approvedUser.getId()), "Resource status updated successfully");

    }

    @PostMapping("/reject/{id}")
    @PreAuthorize("hasAnyAuthority('affiliateUserManagement')")
    public ResponseEntity<ApiDataResponse<IdResponseDto>> rejectAffiliateRequest(@PathVariable Long id) {
        AffiliateUser approvedUser = service.rejectAffiliateRequest(id);
        return ApiResponseUtils.response(HttpStatus.OK, new IdResponseDto(approvedUser.getId()), "Resource status updated successfully");
    }

    @GetMapping("/earnings/{referralCode}")
    @PreAuthorize("hasAnyAuthority('affiliateUserManagement', 'manageAffiliate')")
    public ResponseEntity<ApiDataResponse<BigDecimal>> getTotalEarningsByReferralCode(@PathVariable String referralCode) {
        BigDecimal totalEarnings = service.getTotalEarningsByReferralCode(referralCode);
        return ApiResponseUtils.response(HttpStatus.OK, totalEarnings,"Your total earnings: " + totalEarnings);

    }
    @GetMapping("/earnings")
    @PreAuthorize("hasAnyAuthority('AFFILIATE', 'manageAffiliate')")
    public ResponseEntity<ApiDataResponse<BigDecimal>> getTotalEarningsByReferralCode() {
        SecuredUserInfo userInfo = (SecuredUserInfo) this.userInfoUtil.authenticatedUserInfo();
        Long userId = userInfo.getUserId();
        Optional<AffiliateUser> optionalAffiliateUser = this.repository.findById(userId);
        AffiliateUser affiliateUser = optionalAffiliateUser.get();
        BigDecimal totalEarnings = service.getTotalEarningsByReferralCode(affiliateUser.getReferralCode());
        return ApiResponseUtils.response(HttpStatus.OK, totalEarnings,"Your total earnings: " + totalEarnings);

    }

    @GetMapping("/sales/{referralCode}")
    @PreAuthorize("hasAnyAuthority('affiliateUserManagement')")
    public ResponseEntity<ApiDataResponse<BigDecimal>> getTotalSalesAmountByReferralCode(@PathVariable String referralCode) {
        BigDecimal totalSalesAmount = service.getTotalSalesAmountByReferralCode(referralCode);
        return ApiResponseUtils.response(HttpStatus.OK, totalSalesAmount,"Your total earnings: " + totalSalesAmount);
    }
    @GetMapping("/sales")
    @PreAuthorize("hasAnyAuthority('AFFILIATE', 'manageAffiliate')")
    public ResponseEntity<ApiDataResponse<BigDecimal>> getTotalSalesAmountByReferralCode() {
        SecuredUserInfo userInfo = (SecuredUserInfo) this.userInfoUtil.authenticatedUserInfo();
        Long userId = userInfo.getUserId();
        Optional<AffiliateUser> optionalAffiliateUser = this.repository.findById(userId);
        AffiliateUser affiliateUser = optionalAffiliateUser.get();
        BigDecimal totalSalesAmount = service.getTotalSalesAmountByReferralCode(affiliateUser.getReferralCode());
        return ApiResponseUtils.response(HttpStatus.OK, totalSalesAmount,"Your total earnings: " + totalSalesAmount);
    }


}