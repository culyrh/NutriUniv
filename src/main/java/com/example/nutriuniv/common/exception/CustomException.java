package com.example.nutriuniv.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final com.example.nutriuniv.common.exception.ErrorCode errorCode;

    public CustomException(com.example.nutriuniv.common.exception.ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public CustomException(com.example.nutriuniv.common.exception.ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
