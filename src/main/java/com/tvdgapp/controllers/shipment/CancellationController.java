package com.tvdgapp.controllers.shipment;

import com.tvdgapp.apiresponse.ApiDataResponse;
import com.tvdgapp.dtos.shipment.CancellationRequestDto;
import com.tvdgapp.dtos.shipment.ReviewCancellationRequestDto;
import com.tvdgapp.securityconfig.SecuredUserInfo;
import com.tvdgapp.services.shipment.CancellationService;
import com.tvdgapp.utils.ApiResponseUtils;
import com.tvdgapp.utils.UserInfoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cancellations")
public class CancellationController {

    @Autowired
    private CancellationService cancellationService;
    @Autowired
    private UserInfoUtil userInfoUtil;

    // Endpoint for requesting cancellation (User)
    @PostMapping("/request")
    public ResponseEntity<ApiDataResponse<Object>> requestCancellation(@RequestBody CancellationRequestDto dto) {
        SecuredUserInfo securedUserInfo = (SecuredUserInfo) userInfoUtil.AuthenticatedUserDetails();
        cancellationService.requestCancellation(dto, securedUserInfo.getUserId());
        return ApiResponseUtils.response(HttpStatus.OK, "Cancellation request submitted successfully.");
    }

    // Endpoint for reviewing cancellation (Admin)
    @PatchMapping("/{id}/review")
    public ResponseEntity<ApiDataResponse<Object>> reviewCancellationRequest(@PathVariable Long id, @RequestBody ReviewCancellationRequestDto dto) {
        SecuredUserInfo securedUserInfo = (SecuredUserInfo) userInfoUtil.AuthenticatedUserDetails();
        cancellationService.reviewCancellationRequest(id, dto, securedUserInfo.getUserId());
        return ApiResponseUtils.response(HttpStatus.OK, "Cancellation request reviewed successfully.");
    }
}
