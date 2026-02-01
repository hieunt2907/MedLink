package com.hieunt.medlink.app.controllers;

import com.hieunt.medlink.app.requests.auth.LoginRequest;
import com.hieunt.medlink.app.requests.user.UserCreateRequest;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.responses.auth.LoginResponse;
import com.hieunt.medlink.app.responses.user.UserResponse;
import com.hieunt.medlink.app.services.auth.AuthService;
import com.hieunt.medlink.app.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/public/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/")
    public ResponseEntity<BaseResponse<UserResponse>> createUser(@RequestBody UserCreateRequest user) {
        try {
            BaseResponse<UserResponse> response = userService.createUser(user);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getMessage(), null));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
        try {
            BaseResponse<LoginResponse> response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getMessage(), null));
        }
    }
}
