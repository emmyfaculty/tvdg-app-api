package com.tvdgapp.controllers.user.affiliate;

import com.tvdgapp.apiresponse.ApiDataResponse;
import com.tvdgapp.dtos.affiliate.CommissionRateRequestDto;
import com.tvdgapp.models.commissionrate.CommissionRate;
import com.tvdgapp.services.affiliate.CommissionRateService;
import com.tvdgapp.utils.ApiResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/commission-rates")
public class CommissionRateController {

    @Autowired
    private CommissionRateService commissionRateService;

    @PostMapping
    @PreAuthorize("hasAuthority('affiliateUserManagement')")
    public ResponseEntity<ApiDataResponse<CommissionRate>> createCommissionRate(@RequestBody CommissionRateRequestDto request) {
        CommissionRate commissionRate = commissionRateService.createCommissionRate(request);
        return ApiResponseUtils.response(HttpStatus.CREATED, commissionRate, "Resource created successfully");
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('affiliateUserManagement')")
    public ResponseEntity<ApiDataResponse<CommissionRate>> updateCommissionRate(@PathVariable Long id, @RequestBody CommissionRateRequestDto request) {
        CommissionRate commissionRate = commissionRateService.updateCommissionRate(id, request);
        return ApiResponseUtils.response(HttpStatus.OK,  commissionRate, "Resources updated successfully");
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('affiliateUserManagement')")
    public ResponseEntity<ApiDataResponse<CommissionRate>> getCommissionRateById(@PathVariable Long id) {
        CommissionRate commissionRate = commissionRateService.getCommissionRateById(id);
        return ApiResponseUtils.response(HttpStatus.OK, commissionRate, "Resources retrieved successfully");

    }

    @GetMapping
    @PreAuthorize("hasAuthority('affiliateUserManagement')")
    public ResponseEntity<ApiDataResponse<List<CommissionRate>>> getAllCommissionRates() {
        List<CommissionRate> commissionRates = commissionRateService.getAllCommissionRates();
        return ApiResponseUtils.response(HttpStatus.OK,commissionRates,"Resources retrieved successfully");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('affiliateUserManagement')")
    public ResponseEntity<ApiDataResponse<Object>> deleteCommissionRate(@PathVariable Long id) {
        commissionRateService.deleteCommissionRate(id);
        return ApiResponseUtils.response(HttpStatus.OK, "Resource deleted successfully");
    }
}
