package com.hieunt.medlink.app.requests.user;

import com.hieunt.medlink.app.entities.UserEntity;
import com.hieunt.medlink.app.entities.UserRoleEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequest {
    @NotBlank(message = "Username không được để trống")
    @Size(min = 3, max = 20, message = "Username phải từ 3 đến 20 ký tự")
    @Pattern(
            regexp = "^[a-zA-Z0-9._-]{3,20}$",
            message = "Username chỉ được chứa chữ cái, chữ số, dấu chấm, dấu gạch dưới và dấu gạch ngang"
    )
    private String username;
    @Email
    private String email;
    @NotBlank(message = "Password không được để trống")
    @Size(min = 8, max = 20, message = "Password phải từ 8 đến 20 ký tự")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,20}$",
            message = "Password phải có ít nhất 1 chữ thường, 1 chữ hoa, 1 chữ số và 1 ký tự đặc biệt"
    )
    private String password;
    private String fullName;
    private String phone;
    private LocalDate dateOfBirth;
    private UserEntity.Gender gender;
    private String address;
    private UserRoleEntity.Role role = UserRoleEntity.Role.patient;
    private Long hospitalId;

}
