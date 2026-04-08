package com.example.nutriuniv.common.exception;

import com.example.nutriuniv.common.response.CommonResponse;
import com.example.nutriuniv.common.response.ErrorResponse;
import com.example.nutriuniv.common.exception.CustomException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // 일반 에러
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CommonResponse<ErrorResponse>> handleCustomException(CustomException e,
                                                                               HttpServletRequest request) {
        ErrorCode errorCode = e.getErrorCode();

        log.warn("[CustomException] url: {} | errorType: {} | message: {}", request.getRequestURI(),
                errorCode.name(), e.getMessage());

        ErrorResponse errorResponse = ErrorResponse.of(
                errorCode,
                e.getMessage(),
                request.getRequestURI());

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(CommonResponse.fail(errorResponse));
    }

    // 검증 에러
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse<ErrorResponse>> handleValidationException(
            MethodArgumentNotValidException e,
            HttpServletRequest request) {

        ErrorCode errorCode = ErrorCode.VALIDATION_FAILED;

        log.info("[ValidationException] url: {} | message: {}", request.getRequestURI(), e.getMessage());

        ErrorResponse errorResponse = ErrorResponse.of(
                errorCode,
                e.getBindingResult(), // 실패한 필드 정보들
                request.getRequestURI());

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(CommonResponse.fail(errorResponse));
    }

    // 405 에러
    @ExceptionHandler(org.springframework.web.HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<CommonResponse<ErrorResponse>> handleMethodNotSupportedException(
            org.springframework.web.HttpRequestMethodNotSupportedException e,
            HttpServletRequest request) {

        ErrorCode errorCode = ErrorCode.METHOD_NOT_ALLOWED;

        log.info("[MethodNotAllowed] url: {} | message: {}", request.getRequestURI(), e.getMessage());

        ErrorResponse errorResponse = ErrorResponse.of(errorCode, e.getMessage(), request.getRequestURI());

        return ResponseEntity.status(errorCode.getHttpStatus()).body(CommonResponse.fail(errorResponse));
    }

    // 나머지 모든 예외 처리
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse<ErrorResponse>> handleAllException(
            Exception e,
            HttpServletRequest request) {

        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;

        log.warn("[InternalServerError] url: {} | message: {}", request.getRequestURI(), e.getMessage(), e);

        ErrorResponse errorResponse = ErrorResponse.of(
                errorCode,
                e.getMessage(),
                request.getRequestURI());

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(CommonResponse.fail(errorResponse));
    }
}
