package com.hieunt.medlink.app.requests.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {
    @NotBlank(message = "Password cũ không được để trống")
    private String currentPassword;
    @NotBlank(message = "Password không được để trống")
    @Size(min = 8, max = 20, message = "Password phải từ 8 đến 20 ký tự")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,20}$",
            message = "Password phải có ít nhất 1 chữ thường, 1 chữ hoa, 1 chữ số và 1 ký tự đặc biệt"
    )
    private String newPassword;
}
