package com.tvdgapp.controllers;

import com.tvdgapp.apiresponse.ApiDataResponse;
import com.tvdgapp.dtos.ApiErrorResponse;
import com.tvdgapp.exceptions.*;
import com.tvdgapp.utils.ApiResponseUtils;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Manual mapping of constraint names to field names
    private static final Map<String, String> CONSTRAINT_FIELD_MAP = new HashMap<>();

    static {
        CONSTRAINT_FIELD_MAP.put("UK_c4r7i5t2cj2rsm98uiig9dy6m", "identification_number");
        // Add other mappings as necessary
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiDataResponse<ApiErrorResponse>>handleResourceNotFoundException(ResourceNotFoundException ex) {
        ApiErrorResponse response = new ApiErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
        return ApiResponseUtils.response(HttpStatus.NOT_FOUND, response, "The resource was not found");
    }
    @ExceptionHandler(PasswordMismatchException.class)
    public ResponseEntity<ApiDataResponse<ApiErrorResponse>>handlePasswordMismatchException(PasswordMismatchException ex) {
        ApiErrorResponse response = new ApiErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
        return ApiResponseUtils.response(HttpStatus.NOT_FOUND, response, "Password and confirmed password do not match");
    }
    @ExceptionHandler(TvdgException.DuplicateEntityException.class)
    public ResponseEntity<ApiDataResponse<ApiErrorResponse>>handleResourceNotFoundException(TvdgException.DuplicateEntityException ex) {
        ApiErrorResponse response = new ApiErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
        return ApiResponseUtils.response(HttpStatus.CONFLICT, response, "The resource already exist");
    }

    @ExceptionHandler(TvdgException.EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiDataResponse<ApiErrorResponse>> handleEntityNotFoundException(TvdgException.EntityNotFoundException ex) {
        ApiErrorResponse response = new ApiErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
        return ApiResponseUtils.response(HttpStatus.NOT_FOUND, response, "The resource was not found.");    }
    @ExceptionHandler(TvdgException.InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public  ResponseEntity<ApiDataResponse<ApiErrorResponse>> handleEntityNotFoundException(TvdgException.InvalidCredentialsException ex) {
        ApiErrorResponse response = new ApiErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
        return ApiResponseUtils.response(HttpStatus.UNAUTHORIZED, response, "The request is not a valid request");
    }
    @ExceptionHandler(TvdgException.AccountDisabledException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiDataResponse<ApiErrorResponse>> handleEntityNotFoundException(TvdgException.AccountDisabledException ex) {
        ApiErrorResponse response = new ApiErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
        return ApiResponseUtils.response(HttpStatus.NOT_FOUND, response, "Your Account has been disabled");    }

    @ExceptionHandler(TvdgException.UnAuthorizeException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ApiDataResponse<ApiErrorResponse>> handleUnAuthorizeException(TvdgException.UnAuthorizeException ex) {
        ApiErrorResponse response = new ApiErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
        return ApiResponseUtils.response(HttpStatus.UNAUTHORIZED, response, "You are not authorize to access the resources");
    }

    @ExceptionHandler(DuplicateEntityException.class)
    public ResponseEntity<ApiDataResponse<ApiErrorResponse>> handleDuplicateEntityException(DuplicateEntityException ex) {
        ApiErrorResponse response = new ApiErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
        return ApiResponseUtils.response(HttpStatus.CONFLICT, response, "the resource already exist");
    }
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiDataResponse<ApiErrorResponse>> handleAuthenticationException(AuthenticationException ex) {
        ApiErrorResponse response = new ApiErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
        return ApiResponseUtils.response(HttpStatus.UNAUTHORIZED, response, "Failed to authenticate user");
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ApiDataResponse<ApiErrorResponse>> handleInvalidRequestException(InvalidRequestException ex) {
        ApiErrorResponse response = new ApiErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
        return ApiResponseUtils.response(HttpStatus.UNAUTHORIZED, response, "The request is not a valid request");
    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ApiDataResponse<ApiErrorResponse>> handleValidationExceptions(MethodArgumentNotValidException ex) {
//        ApiErrorResponse response = new ApiErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
//        return ApiResponseUtils.response(HttpStatus.BAD_REQUEST, response, "The request is not a valid request");
//    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiDataResponse<ApiErrorResponse>> handleGeneralException(Exception ex) {
        ApiErrorResponse response = new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return ApiResponseUtils.response(HttpStatus.INTERNAL_SERVER_ERROR, response, "An unexpected error occurred.");
    }

    @ExceptionHandler(AccountDisabledException.class)
    public ResponseEntity<ApiDataResponse<ApiErrorResponse>> handleAccountDisabledException(AccountDisabledException ex) {
        ApiErrorResponse response = new ApiErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage());
        return ApiResponseUtils.response(HttpStatus.FORBIDDEN, response, "Your account has been disabled");
    }

    @ExceptionHandler(ConversionRuntimeException.class)
    public ResponseEntity<ApiDataResponse<ApiErrorResponse>> handleConversionRuntimeException(ConversionRuntimeException ex) {
        ApiErrorResponse response = new ApiErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ApiResponseUtils.response(HttpStatus.BAD_REQUEST, response, "Your account has been disabled");
    }

    @ExceptionHandler(CountryCodeServiceException.class)
    public ResponseEntity<ApiDataResponse<ApiErrorResponse>> handleCountryCodeServiceException(CountryCodeServiceException ex) {
        ApiErrorResponse response = new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return ApiResponseUtils.response(HttpStatus.INTERNAL_SERVER_ERROR, response);
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<ApiDataResponse<ApiErrorResponse>> handleFileStorageException(FileStorageException ex) {
        ApiErrorResponse response = new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return ApiResponseUtils.response(HttpStatus.INTERNAL_SERVER_ERROR, response);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ApiDataResponse<ApiErrorResponse>> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        ApiErrorResponse response = new ApiErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
        return ApiResponseUtils.response(HttpStatus.UNAUTHORIZED, response, "The request is not a valid request");
    }

    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<ApiDataResponse<ApiErrorResponse>> handleInvalidOperationException(InvalidOperationException ex) {
        ApiErrorResponse response = new ApiErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ApiResponseUtils.response(HttpStatus.BAD_REQUEST, response);
    }

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ApiDataResponse<ApiErrorResponse>> handleInvalidRefreshTokenException(InvalidRefreshTokenException ex) {
        ApiErrorResponse response = new ApiErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ApiResponseUtils.response(HttpStatus.BAD_REQUEST, response);
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ApiDataResponse<ApiErrorResponse>> handleServiceException(ServiceException ex) {
        ApiErrorResponse response = new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return ApiResponseUtils.response(HttpStatus.INTERNAL_SERVER_ERROR, response);
    }

    @ExceptionHandler(ShippingOptionNotFoundException.class)
    public ResponseEntity<ApiDataResponse<ApiErrorResponse>> handleServiceOptionNotFoundException(ShippingOptionNotFoundException ex) {
        ApiErrorResponse response = new ApiErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
        return ApiResponseUtils.response(HttpStatus.NOT_FOUND, response);
    }

    @ExceptionHandler(ShipmentNotFoundException.class)
    public ResponseEntity<ApiDataResponse<ApiErrorResponse>> handleShipmentNotFoundException(ShipmentNotFoundException ex) {
        ApiErrorResponse response = new ApiErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
        return ApiResponseUtils.response(HttpStatus.NOT_FOUND, response);    }

    @ExceptionHandler(TemplateEngineException.class)
    public ResponseEntity<ApiDataResponse<ApiErrorResponse>> handleTemplateEngineException(TemplateEngineException ex) {
        ApiErrorResponse response = new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return ApiResponseUtils.response(HttpStatus.INTERNAL_SERVER_ERROR, response);
    }

    @ExceptionHandler(TvdgAppSystemException.class)
    public ResponseEntity<ApiDataResponse<ApiErrorResponse>> handleTvdgAppSystemException(TvdgAppSystemException ex) {
        ApiErrorResponse response = new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return ApiResponseUtils.response(HttpStatus.INTERNAL_SERVER_ERROR, response);
    }

    @ExceptionHandler(UnAuthorizedException.class)
    public ResponseEntity<ApiDataResponse<ApiErrorResponse>>handleUnAuthorizedException(UnAuthorizedException ex) {
        ApiErrorResponse response = new ApiErrorResponse(HttpStatus.UNAUTHORIZED, ex.getMessage());
        return ApiResponseUtils.response(HttpStatus.UNAUTHORIZED, response, "You are not authorize to access the resources");
    }

    // Define a generic handler for any other RuntimeExceptions
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiDataResponse<ApiErrorResponse>> handleRuntimeException(RuntimeException ex) {
        ApiErrorResponse response = new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return ApiResponseUtils.response(HttpStatus.INTERNAL_SERVER_ERROR, response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiDataResponse<ApiErrorResponse>> handleIllegalArgumentException(IllegalArgumentException ex) {
        ApiErrorResponse response = new ApiErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ApiResponseUtils.response(HttpStatus.BAD_REQUEST, response);

    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(WalletNotFoundException.class)
    @ResponseBody
    public ResponseEntity<ApiDataResponse<ApiErrorResponse>> handleWalletNotFound(WalletNotFoundException ex) {
        ApiErrorResponse response = new ApiErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
        return ApiResponseUtils.response(HttpStatus.NOT_FOUND, response, "Wallet not found");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InsufficientFundsException.class)
    @ResponseBody
    public ResponseEntity<ApiDataResponse<ApiErrorResponse>> handleInsufficientFunds(InsufficientFundsException ex) {
        ApiErrorResponse response = new ApiErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ApiResponseUtils.response(HttpStatus.BAD_REQUEST, response, "Your wallet balance is to low for this transaction");
    }

    @ExceptionHandler(InvalidUserIdException.class)
    public ResponseEntity<ApiDataResponse<ApiErrorResponse>> handleInvalidUserIdException(InvalidUserIdException ex) {
        ApiErrorResponse response = new ApiErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ApiResponseUtils.response(HttpStatus.NOT_FOUND,response, "the user Id is not a valid Id" );
    }
    @ExceptionHandler(PaymentProcessingException.class)
    public ResponseEntity<ApiDataResponse<ApiErrorResponse>> handlePaymentProcessingException(PaymentProcessingException ex) {
        ApiErrorResponse response = new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return ApiResponseUtils.response(HttpStatus.INTERNAL_SERVER_ERROR, response);
    }
    @ExceptionHandler(BankDetailsNotFoundException.class)
    public ResponseEntity<ApiDataResponse<ApiErrorResponse>> handleBankDetailsNotFoundException(BankDetailsNotFoundException ex) {
        ApiErrorResponse response = new ApiErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage());
        return ApiResponseUtils.response(HttpStatus.NOT_FOUND,response, "the supplied details does not exist" );

    }

    @ExceptionHandler(InvalidWithdrawalException.class)
    public ResponseEntity<ApiDataResponse<ApiErrorResponse>> handleInvalidWithdrawalException(InvalidWithdrawalException ex) {
        ApiErrorResponse response = new ApiErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ApiResponseUtils.response(HttpStatus.BAD_REQUEST,response, "the user wallet not found" );
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ApiDataResponse<ApiErrorResponse>> handleInsufficientBalanceException(InsufficientBalanceException ex) {
        ApiErrorResponse response = new ApiErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ApiResponseUtils.response(HttpStatus.BAD_REQUEST,response, "Insufficient balance in wallet" );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();

        // Extract field errors and set custom messages
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        // Create a custom response
        Map<String, Object> response = new HashMap<>();
        response.put("status", false);
        response.put("httpStatus", HttpStatus.BAD_REQUEST);
        response.put("message", "Validation failed");
        response.put("errors", errors);
        response.put("timestamp", System.currentTimeMillis());

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UniqueFieldViolationException.class)
    public ResponseEntity<ApiDataResponse<Map<String, String>>> handleUniqueFieldViolationException(UniqueFieldViolationException ex) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("field", ex.getFieldName());
        errorResponse.put("message", ex.getMessage());

        return ApiResponseUtils.response(HttpStatus.CONFLICT, errorResponse, "The resource already exists. Please use a different value." );
    }
    
}
