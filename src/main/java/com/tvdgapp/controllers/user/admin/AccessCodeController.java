package com.tvdgapp.controllers.user.admin;

import com.tvdgapp.apiresponse.ApiDataResponse;
import com.tvdgapp.dtos.user.admin.AccessCodeDTO;
import com.tvdgapp.dtos.user.admin.CreateAccessCodeRequest;
import com.tvdgapp.services.user.admin.AccessCodeService;
import com.tvdgapp.utils.ApiResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/access-codes")
@Validated
@RequiredArgsConstructor
public class AccessCodeController {

    private final AccessCodeService accessCodeService;

//    @PostMapping
//    public ResponseEntity<ApiDataResponse<AccessCodeDTO>> createAccessCode(@Valid @RequestBody CreateAccessCodeRequest request) {
//        AccessCodeDTO accessCodeDTO = accessCodeService.createAccessCode(request);
//        return ApiResponseUtils.response(HttpStatus.CREATED, accessCodeDTO, "Resource created successfully");
//
//    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiDataResponse<AccessCodeDTO>> updateAccessCode(@PathVariable Long id, @Valid @RequestBody CreateAccessCodeRequest request) {
        AccessCodeDTO accessCodeDTO = accessCodeService.updateAccessCode(id);
        return ApiResponseUtils.response(HttpStatus.CREATED, accessCodeDTO, "Resource updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiDataResponse<Object>> deleteAccessCode(@PathVariable Long id) {
        accessCodeService.deleteAccessCode(id);
        return ApiResponseUtils.response(HttpStatus.NO_CONTENT, "Resource deleted successfully");
    }
}
