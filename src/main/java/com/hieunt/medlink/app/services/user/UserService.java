package com.hieunt.medlink.app.services.user;

import com.hieunt.medlink.app.requests.user.AdminUserUpdateRequest;
import com.hieunt.medlink.app.requests.user.UserCreateRequest;
import com.hieunt.medlink.app.requests.user.UserUpdateRequest;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.responses.user.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    BaseResponse<UserResponse> createUser(UserCreateRequest user);
    BaseResponse<UserResponse> getUser(Long id);
    BaseResponse<UserResponse> updateUser(Long id, AdminUserUpdateRequest user);
    BaseResponse<UserResponse> updateMe(UserUpdateRequest user);
    BaseResponse<Boolean> deleteUser(Long id);
    BaseResponse<Page<UserResponse>> filterUsers(String keyword, Pageable pageable);
    BaseResponse<UserResponse> getCurrentUser();
}
