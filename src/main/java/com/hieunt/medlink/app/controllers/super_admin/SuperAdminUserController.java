package com.hieunt.medlink.app.controllers.super_admin;

import com.hieunt.medlink.app.requests.user.AdminUserUpdateRequest;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.responses.user.UserResponse;
import com.hieunt.medlink.app.services.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/super-admin/users")
@RequiredArgsConstructor
public class SuperAdminUserController {
    private final UserService userService;

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<UserResponse>> updateUser(@PathVariable Long id,
            @RequestBody AdminUserUpdateRequest user) {
        BaseResponse<UserResponse> response = userService.updateUser(id, user);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/")
    public ResponseEntity<BaseResponse<Page<UserResponse>>> filterUsers(@RequestParam(required = false) String keyword,
            @PageableDefault(size = 20) Pageable pageable) {
        BaseResponse<Page<UserResponse>> response = userService.filterUsers(keyword, pageable);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Boolean>> deleteUser(@PathVariable Long id) {
        BaseResponse<Boolean> response = userService.deleteUser(id);
        return ResponseEntity.ok(response);
    }
}
