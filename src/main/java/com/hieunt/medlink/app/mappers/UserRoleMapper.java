package com.hieunt.medlink.app.mappers;

import com.hieunt.medlink.app.entities.UserRoleEntity;
import com.hieunt.medlink.app.requests.user.AdminUserUpdateRequest;
import com.hieunt.medlink.app.requests.user.UserCreateRequest;
import org.springframework.stereotype.Component;

@Component
public class UserRoleMapper {
    public UserRoleEntity toEntity(UserCreateRequest user) {
        if (user == null) {
            return null;
        }
        UserRoleEntity userRoleEntity = new UserRoleEntity();
        userRoleEntity.setHospitalId(user.getHospitalId());
        userRoleEntity.setRole(user.getRole());
        return userRoleEntity;
    }

    public UserRoleEntity toEntity(AdminUserUpdateRequest user, UserRoleEntity userRoleEntity) {
        if (user == null) {
            return userRoleEntity;
        }

        if (user.getHospitalId() != null) {
            userRoleEntity.setHospitalId(user.getHospitalId());
        }

        if (user.getRole() != null) {
            userRoleEntity.setRole(user.getRole());
        }
        return userRoleEntity;
    }
}