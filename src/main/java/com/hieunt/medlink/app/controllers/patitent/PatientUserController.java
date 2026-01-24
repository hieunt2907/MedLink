package com.hieunt.medlink.app.controllers.patitent;

import com.hieunt.medlink.app.requests.user.UserUpdateRequest;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.responses.user.UserResponse;
import com.hieunt.medlink.app.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/patient/users")
@RequiredArgsConstructor
public class PatientUserController {
    private final UserService userService;

    @PutMapping("/me")
    public ResponseEntity<BaseResponse<UserResponse>> updateMe(@RequestBody UserUpdateRequest user ) {
        try {
            BaseResponse<UserResponse> response = userService.updateMe(user);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getMessage(), null));
        }
    }

    @GetMapping("/me")
    public ResponseEntity<BaseResponse<UserResponse>> getCurrentUser() {
        try {
            BaseResponse<UserResponse> response = userService.getCurrentUser();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BaseResponse<>(e.getMessage(), null));
        }
    }
}
