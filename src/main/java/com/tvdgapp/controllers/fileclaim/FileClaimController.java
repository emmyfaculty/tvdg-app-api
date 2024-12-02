package com.tvdgapp.controllers.fileclaim;

import com.tvdgapp.apiresponse.ApiDataResponse;
import com.tvdgapp.dtos.common.FileUrlDto;
import com.tvdgapp.dtos.fileclaim.FileClaimRequestDto;
import com.tvdgapp.dtos.fileclaim.FileClaimResponseDto;
import com.tvdgapp.exceptions.InvalidRequestException;
import com.tvdgapp.services.fileclaim.FileClaimService;
import com.tvdgapp.utils.ApiResponseUtils;
import com.tvdgapp.utils.FileUploadValidatorUtils;
import com.tvdgapp.utils.UserInfoUtil;
import com.tvdgapp.validators.ValidationErrors;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.basepath-customer}/file_claim")
public class FileClaimController {

    public static final int MAX_RECEIPT_UPLOAD_SIZE = 5242880;//1024*1024*5(5mb)

    private final FileClaimService fileClaimService;
    private final UserInfoUtil userInfoUtil;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('FileClaim:create')")
    public ResponseEntity<ApiDataResponse<FileClaimResponseDto>> createFileClaims(@Valid @RequestBody FileClaimRequestDto fileClaimRequestDto) {
//        validateFileUploads(receiptFileUpload);
        FileClaimResponseDto fileClaims1 = this.fileClaimService.createFileClaim(fileClaimRequestDto);
        return ApiResponseUtils.response(HttpStatus.CREATED, fileClaims1, "Resource created successfully");
    }

    @PutMapping("/{fileClaimsId}/receipt")
    @PreAuthorize("hasAuthority('FileClaim:create')")
    public ResponseEntity<ApiDataResponse<FileUrlDto>> uploadShipmentReceipt(@PathVariable Long fileClaimsId, @RequestPart(value = "receipt_file_upload") MultipartFile receiptFileUpload) {
        validateFileUploads(receiptFileUpload);
        String fileUrl = this.fileClaimService.uploadShipmentReceipt(fileClaimsId, receiptFileUpload).orElseThrow(() ->
                new InvalidRequestException("unable to update file"));
        return ApiResponseUtils.response(HttpStatus.OK, new FileUrlDto(fileUrl), "Resource updated successfully");
    }

    private void validateFileUploads(MultipartFile receiptFileUpload) {

        ValidationErrors validationErrors = new ValidationErrors();

        if (receiptFileUpload!=null) {
            List<String> mimeTypes = List.of("png", "jpeg", "jpg", "gif", "pdf");
            FileUploadValidatorUtils.validateRequired(receiptFileUpload, validationErrors);
            FileUploadValidatorUtils.validateFileType(receiptFileUpload, mimeTypes, validationErrors);
            FileUploadValidatorUtils.validateSize(receiptFileUpload, MAX_RECEIPT_UPLOAD_SIZE, validationErrors);
        }
        if (validationErrors.hasErrors()) {
            throw new InvalidRequestException(validationErrors);
        }
    }
}
