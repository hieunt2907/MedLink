package com.hieunt.medlink.app.services.role;

import com.hieunt.medlink.app.entities.UserEntity;
import com.hieunt.medlink.app.requests.user.AdminUserUpdateRequest;
import com.hieunt.medlink.app.requests.user.UserCreateRequest;


public interface UserRoleService {
    void createUserRole(UserCreateRequest user, UserEntity userEntity);
    void updateUserRole(Long userId, AdminUserUpdateRequest user);
    void deleteUserRoleByUserId(UserEntity userEntity);
}
