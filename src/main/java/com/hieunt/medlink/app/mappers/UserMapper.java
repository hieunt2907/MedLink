package com.hieunt.medlink.app.mappers;

import com.hieunt.medlink.app.entities.UserEntity;
import com.hieunt.medlink.app.requests.user.AdminUserUpdateRequest;
import com.hieunt.medlink.app.requests.user.UserCreateRequest;
import com.hieunt.medlink.app.requests.user.UserUpdateRequest;
import com.hieunt.medlink.pkg.utils.PasswordGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final PasswordGenerator passwordGenerator;

    public UserEntity toEntity(UserCreateRequest user) {
        if (user == null) {
            return null;
        }

        UserEntity userEntity = new UserEntity();

        userEntity.setUsername(user.getUsername().toLowerCase());
        userEntity.setEmail(user.getEmail().toLowerCase());
        userEntity.setPasswordHash(passwordGenerator.encodePassword(user.getPassword()));
        userEntity.setPhone(user.getPhone());
        userEntity.setFullName(user.getFullName().toLowerCase());
        userEntity.setDateOfBirth(user.getDateOfBirth());
        userEntity.setGender(user.getGender());
        userEntity.setAddress(user.getAddress().toLowerCase());

        return userEntity;
    }

    public UserEntity toEntity(AdminUserUpdateRequest user, UserEntity userEntity) {
        if (user == null) {
            return userEntity;
        }

        if (user.getPassword() != null) {
            userEntity.setPasswordHash(passwordGenerator.encodePassword(user.getPassword()));
        }

        if (user.getStatus() != null) {
            userEntity.setStatus(user.getStatus());
        }

        return userEntity;
    }

    public UserEntity toEntity(UserUpdateRequest user, UserEntity userEntity) {
        if (user == null) {

            return userEntity;
        }

        if (user.getEmail() != null) {
            userEntity.setEmail(user.getEmail().toLowerCase());
        }
        if (user.getPhone() != null) {
            userEntity.setPhone(user.getPhone());
        }
        if (user.getFullName() != null) {
            userEntity.setFullName(user.getFullName().toLowerCase());
        }
        if (user.getDateOfBirth() != null) {
            userEntity.setDateOfBirth(user.getDateOfBirth());
        }
        if (user.getGender() != null) {
            userEntity.setGender(user.getGender());
        }
        if (user.getAddress() != null) {
            userEntity.setAddress(user.getAddress());
        }
        if (user.getAvatarUrl() != null) {
            userEntity.setAvatarUrl(user.getAvatarUrl());
        }
        if (user.getTwoFaEnabled() != null) {
            userEntity.setTwoFaEnabled(user.getTwoFaEnabled());
        }

        return userEntity;
    }
}
