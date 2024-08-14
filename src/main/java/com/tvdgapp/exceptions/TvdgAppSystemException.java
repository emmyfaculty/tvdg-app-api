package com.tvdgapp.exceptions;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class TvdgAppSystemException extends RuntimeException{

    protected String message;

    public TvdgAppSystemException(String message) {
        this.message=message;
    }

    public TvdgAppSystemException(Throwable cause) {
        super(cause);
    }

    public TvdgAppSystemException(String message, Throwable cause) {
        super(message, cause);
        this.message=message;
    }
}
