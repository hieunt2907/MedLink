package com.hieunt.medlink.app.requests.user;

import com.hieunt.medlink.app.entities.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {
    private String email;
    private String fullName;
    private String phone;
    private LocalDate dateOfBirth;
    private UserEntity.Gender gender;
    private String address;
    private String avatarUrl;
    private Boolean twoFaEnabled;
}
