package com.hieunt.medlink.app.repositories;

import com.hieunt.medlink.app.entities.UserRoleEntity;

public interface UserRoleRepository extends BaseRepository<UserRoleEntity, Long> {
    void deleteByUserId(Long userId);

    UserRoleEntity findByUserId(Long userId);
}
