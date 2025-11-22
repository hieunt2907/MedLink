package com.hieunt.medlink.pkg.error;

import com.hieunt.medlink.app.responses.BaseResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<Object>> handleGlobalException(Exception ex) {
        // Log lỗi ra console hoặc file log để debug (Optional)
        ex.printStackTrace();

        // Trả về format chuẩn BaseResponse kèm message lỗi
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST) // Hoặc INTERNAL_SERVER_ERROR tùy logic
                .body(new BaseResponse<>(ex.getMessage(), null));
    }

    // @ExceptionHandler(ResourceNotFoundException.class)
    // public ResponseEntity<BaseResponse<Object>> handleResourceNotFound(ResourceNotFoundException ex) {
    //     return ResponseEntity.status(HttpStatus.NOT_FOUND)
    //             .body(new BaseResponse<>(ex.getMessage(), null));
    // }
}