package com.hieunt.medlink.app.services.auth;

import com.hieunt.medlink.app.entities.UserEntity;
import com.hieunt.medlink.app.entities.UserRoleEntity;
import com.hieunt.medlink.app.repositories.UserRepository;
import com.hieunt.medlink.app.repositories.UserRoleRepository;
import com.hieunt.medlink.app.requests.auth.LoginRequest;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.responses.auth.LoginResponse;
import com.hieunt.medlink.app.utils.JwtUtil;
import com.hieunt.medlink.app.utils.PasswordGenerator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordGenerator passwordGenerator;
    private final UserRoleRepository userRoleRepository;

    @Override
    public BaseResponse<LoginResponse> login(LoginRequest request) {
        try {
            UserEntity user = null;
            if (request.getUsername() != null) {
                user = userRepository.findByUsername(request.getUsername());
            }

            if (request.getEmail() != null) {
                user = userRepository.findByEmail(request.getEmail());
            }

            if (user == null) {
                throw new RuntimeException("user not found");
            }

            if (!user.getStatus().equals(UserEntity.UserStatus.active)) {
                throw new RuntimeException("user is not active");
            }

            if (!passwordGenerator.checkPassword(request.getPassword(), user.getPasswordHash())) {
                throw new RuntimeException("invalid password");
            }

            UserRoleEntity userRoleEntity = userRoleRepository.findByUserId(user.getId());

            String token = jwtUtil.generateToken(user.getUsername(), userRoleEntity.getRole());
            return new BaseResponse<>("logging in successfully", new LoginResponse(token));
        } catch (Exception e) {
            throw new RuntimeException("error logging in: " + e.getMessage(), e);
        }
    }
}
