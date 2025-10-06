package com.hieunt.medlink.app.controllers;

import com.hieunt.medlink.app.requests.user.UserCreateRequest;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.responses.user.UserResponse;
import com.hieunt.medlink.app.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/public/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<BaseResponse<UserResponse>> createUser(@RequestBody UserCreateRequest user) {
        try {
            BaseResponse<UserResponse> response = userService.createUser(user);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getMessage(), null));
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<BaseResponse<Page<UserResponse>>> filterUsers(@RequestParam(required = false) String keyword, Pageable pageable) {
        try {
            BaseResponse<Page<UserResponse>> response = userService.filterUsers(keyword, pageable);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getMessage(), null));
        }
    }
}
