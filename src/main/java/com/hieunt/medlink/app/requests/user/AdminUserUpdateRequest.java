package com.hieunt.medlink.app.requests.user;

import com.hieunt.medlink.app.entities.UserEntity;
import com.hieunt.medlink.app.entities.UserRoleEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserUpdateRequest {
    private String password;
    private UserRoleEntity.Role role = UserRoleEntity.Role.patient;
    private Long hospitalId;
    private UserEntity.UserStatus status;
}
