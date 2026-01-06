package com.hieunt.medlink.app.services.role;

import com.hieunt.medlink.app.entities.UserEntity;
import com.hieunt.medlink.app.entities.UserRoleEntity;
import com.hieunt.medlink.app.mappers.UserRoleMapper;
import com.hieunt.medlink.app.repositories.UserRoleRepository;
import com.hieunt.medlink.app.requests.user.AdminUserUpdateRequest;
import com.hieunt.medlink.app.requests.user.UserCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserRoleServiceImpl implements UserRoleService {
    private final UserRoleRepository userRoleRepository;
    private final UserRoleMapper userRoleMapper;

    @Override
    public void createUserRole(UserCreateRequest user, UserEntity userEntity) {
        try {
            UserRoleEntity userRoleEntity = userRoleMapper.toEntity(user);
            userRoleEntity.setUserId(userEntity.getId());
            userRoleRepository.save(userRoleEntity);
        } catch (Exception e) {
            throw new RuntimeException("error creating user role: " + e.getMessage(), e);
        }
    }

    @Override
    public void updateUserRole(Long userId, AdminUserUpdateRequest user) {
        try {
            UserRoleEntity userRoleEntity = userRoleRepository.findByUserId(userId);
            userRoleEntity = userRoleMapper.toEntity(user, userRoleEntity);
            if (userRoleEntity != null)
            userRoleRepository.save(userRoleEntity);
        } catch (Exception e) {
            throw new RuntimeException("error updating user role: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteUserRoleByUserId(UserEntity userEntity) {
        try {
            userRoleRepository.deleteByUserId(userEntity.getId());
        } catch (Exception e) {
            throw new RuntimeException("error deleting user role: " + e.getMessage(), e);
        }
    }
}
