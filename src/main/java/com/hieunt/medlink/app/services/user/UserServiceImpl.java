package com.hieunt.medlink.app.services.user;

import com.hieunt.medlink.app.entities.UserEntity;
import com.hieunt.medlink.app.mappers.UserMapper;
import com.hieunt.medlink.app.repositories.UserRepository;
import com.hieunt.medlink.app.requests.user.AdminUserUpdateRequest;
import com.hieunt.medlink.app.requests.user.UserCreateRequest;
import com.hieunt.medlink.app.requests.user.UserUpdateRequest;
import com.hieunt.medlink.app.responses.BaseResponse;
import com.hieunt.medlink.app.responses.user.UserResponse;
import com.hieunt.medlink.app.services.role.UserRoleService;
import com.hieunt.medlink.pkg.utils.GetCurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final GetCurrentUser getCurrentUser;
    private final UserMapper userMapper;
    private final UserRoleService userRoleService;

    @Override
    public BaseResponse<UserResponse> createUser(UserCreateRequest user) {
        try {
            validateUser(user);
            UserEntity userEntity = userMapper.toEntity(user);
            userRepository.save(userEntity);
            userRoleService.createUserRole(user, userEntity);
            UserResponse userResponse = userRepository.getUser(userEntity.getId());
            return new BaseResponse<>("creating user successfully", userResponse);
        } catch (Exception e) {
            throw new RuntimeException("error creating user: " + e.getMessage(), e);
        }
    }

    @Override
    public BaseResponse<UserResponse> getUser(Long id) {
        try {
            UserResponse user = userRepository.getUser(id);
            if (user == null) {
                throw new RuntimeException("user not found");
            }
            return new BaseResponse<>("getting user successfully", user);
        } catch (Exception e) {
            throw new RuntimeException("error getting user: " + e.getMessage(), e);
        }
    }

    @Override
    public BaseResponse<UserResponse> updateUser(Long id, AdminUserUpdateRequest user) {
        try {
            UserEntity existingUser = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("user not found"));
            UserEntity updatedUser = userMapper.toEntity(user, existingUser);
            userRepository.save(updatedUser);
            userRoleService.updateUserRole(updatedUser.getId(), user);
            UserResponse userResponse = userRepository.getUser(updatedUser.getId());
            return new BaseResponse<>("updating user successfully", userResponse);
        } catch (Exception e) {
            throw new RuntimeException("error updating user: " + e.getMessage(), e);
        }
    }

    @Override
    public BaseResponse<UserResponse> updateMe(UserUpdateRequest user) {
        try {
            UserEntity existingUser = getCurrentUser.getCurrentUser();
            if (userRepository.existsByEmail(user.getEmail())) {
                throw new RuntimeException("email already exists");
            }
            UserEntity updatedUser = userMapper.toEntity(user, existingUser);
            userRepository.save(updatedUser);
            UserResponse userResponse = userRepository.getUser(updatedUser.getId());
            return new BaseResponse<>("updating me successfully", userResponse);
        } catch (Exception e) {
            throw new RuntimeException("error updating me: " + e.getMessage(), e);
        }
    }

    @Override
    public BaseResponse<Boolean> deleteUser(Long id) {
        try {
            UserEntity existingUser = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("user not found"));
            userRoleService.deleteUserRoleByUserId(existingUser);
            userRepository.delete(existingUser);
            return new BaseResponse<>("deleting user successfully", true);
        } catch (Exception e) {
            throw new RuntimeException("error deleting user: " + e.getMessage(), e);
        }
    }

    @Override
    public BaseResponse<Page<UserResponse>> filterUsers(String keyword, Pageable pageable) {
        try {
            Page<UserResponse> users = userRepository.filterUsers(keyword, pageable);
            return new BaseResponse<>("filtering users successfully", users);
        } catch (Exception e) {
            throw new RuntimeException("error filtering users: " + e.getMessage(), e);
        }
    }

    @Override
    public BaseResponse<UserResponse> getCurrentUser() {
        try {
            UserEntity currentUser = getCurrentUser.getCurrentUser();
            if (currentUser == null) {
                throw new RuntimeException("current user not found");
            }
            UserResponse userResponse = userRepository.getUser(currentUser.getId());
            return new BaseResponse<>("getting current user successfully", userResponse);
        } catch (Exception e) {
            throw new RuntimeException("error getting current user: " + e.getMessage(), e);
        }
    }

    private void validateUser(UserCreateRequest user) {
        if (userRepository.existsByUsername(user.getUsername()) && userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("user already exists");
        }
    }
}
