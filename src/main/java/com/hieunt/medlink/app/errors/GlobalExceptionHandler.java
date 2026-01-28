package com.hieunt.medlink.app.errors;

import com.hieunt.medlink.app.responses.BaseResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Xử lý ResourceNotFoundException - 404 NOT FOUND
     * Dùng khi không tìm thấy resource (Hospital, Patient, etc.)
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<BaseResponse<Object>> handleResourceNotFoundException(
            ResourceNotFoundException ex, WebRequest request) {
        logException("ResourceNotFoundException", ex, request);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new BaseResponse<>(ex.getMessage(), null));
    }

    /**
     * Xử lý DuplicateResourceException - 409 CONFLICT
     * Dùng khi resource đã tồn tại (duplicate name, email, etc.)
     */
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<BaseResponse<Object>> handleDuplicateResourceException(
            DuplicateResourceException ex, WebRequest request) {
        logException("DuplicateResourceException", ex, request);
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new BaseResponse<>(ex.getMessage(), null));
    }

    /**
     * Xử lý BadRequestException - 400 BAD REQUEST
     * Dùng khi request không hợp lệ
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BaseResponse<Object>> handleBadRequestException(
            BadRequestException ex, WebRequest request) {
        logException("BadRequestException", ex, request);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new BaseResponse<>(ex.getMessage(), null));
    }

    /**
     * Xử lý validation errors từ @Valid annotation - 400 BAD REQUEST
     * Tự động bắt lỗi validation từ Spring
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<BaseResponse<Object>> handleValidationException(
            MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        logException("ValidationException", ex, request);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new BaseResponse<>("Validation failed: " + errors.toString(), null));
    }

    /**
     * Xử lý IllegalArgumentException - 400 BAD REQUEST
     * Dùng khi argument không hợp lệ
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseResponse<Object>> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {
        logException("IllegalArgumentException", ex, request);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new BaseResponse<>(ex.getMessage(), null));
    }

    /**
     * Xử lý NullPointerException - 500 INTERNAL SERVER ERROR
     * Đây là lỗi nghiêm trọng, cần fix code
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<BaseResponse<Object>> handleNullPointerException(
            NullPointerException ex, WebRequest request) {
        logException("NullPointerException", ex, request);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new BaseResponse<>("Internal server error: Null pointer exception occurred", null));
    }

    /**
     * Xử lý tất cả các exception chưa được handle - 500 INTERNAL SERVER ERROR
     * Đây là fallback handler cuối cùng
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Object>> handleGlobalException(
            Exception ex, WebRequest request) {
        logException("UnhandledException", ex, request);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new BaseResponse<>("Internal server error: " + ex.getMessage(), null));
    }

    /**
     * Helper method để log exception
     * Có thể thay thế bằng logger framework như SLF4J/Logback
     */
    private void logException(String exceptionType, Exception ex, WebRequest request) {
        System.err.println("=== " + exceptionType + " ===");
        System.err.println("Time: " + LocalDateTime.now());
        System.err.println("Path: " + request.getDescription(false));
        System.err.println("Message: " + ex.getMessage());
        ex.printStackTrace();
        System.err.println("======================");
    }
}