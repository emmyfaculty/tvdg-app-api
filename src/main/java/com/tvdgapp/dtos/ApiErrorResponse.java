package com.tvdgapp.dtos;

import org.springframework.http.HttpStatus;

public class ApiErrorResponse {
    private HttpStatus status;
    private String message;

    public ApiErrorResponse(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    // Getters and setters
    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
