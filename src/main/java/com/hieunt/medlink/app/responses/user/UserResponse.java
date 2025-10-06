package com.hieunt.medlink.app.responses.user;

import com.hieunt.medlink.app.entities.UserEntity;
import com.hieunt.medlink.app.entities.UserRoleEntity;

import java.time.Instant;
import java.time.LocalDate;

public interface UserResponse {
    Long getId();
    String getUsername();
    String getEmail();
    String getFullName();
    String getPhone();
    UserEntity.Gender getGender();
    LocalDate getDateOfBirth();
    String getAddress();
    String getAvatarUrl();
    Boolean getIsVerified();
    Boolean getTwoFaEnabled();
    Long getHospitalId();
    UserRoleEntity.Role getRole();
    UserEntity.UserStatus getStatus();
    Instant getCreatedAt();
    Instant getUpdatedAt();
}
