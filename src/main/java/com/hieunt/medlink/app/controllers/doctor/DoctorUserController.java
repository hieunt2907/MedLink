package com.hieunt.medlink.app.controllers.doctor;

import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.responses.user.UserResponse;
import com.hieunt.medlink.app.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/doctor/users")
@RequiredArgsConstructor
public class DoctorUserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<BaseResponse<UserResponse>> getCurrentUser() {
        BaseResponse<UserResponse> response = userService.getCurrentUser();
        return ResponseEntity.ok(response);
    }
}
