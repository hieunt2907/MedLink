package com.hieunt.medlink.app.repositories;

import com.hieunt.medlink.app.entities.UserEntity;
import com.hieunt.medlink.app.responses.user.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface UserRepository extends BaseRepository<UserEntity, Long> {
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    @Query(value = """
                        select u.id         as id,
                        u.username          as username,
                        u.email             as email,
                        u.full_name         as fullName,
                        u.phone             as phone,
                        u.gender            as gender,
                        u.date_of_birth     as dateOfBirth,
                        u.address           as address,
                        u.avatar_url        as avatarUrl,
                        u.is_verified       as isVerified,
                        u.two_fa_enabled    as twoFaEnabled,
                        ur.hospital_id      as hospitalId,
                        ur.role             as role,
                        u.status            as status,
                        u.created_at        as createdAt,
                        u.updated_at        as updatedAt
                        from users u
                        inner join user_roles ur on u.id = ur.user_id
                        where u.username ilike concat('%', :keyword, '%')
                            or u.email ilike concat('%', :keyword, '%')
                            and ur.role <> 'super_admin'
                            and ur.role <> 'admin'
                            and u.status = 'active'
            """, nativeQuery = true)
    Page<UserResponse> filterUsers(@Param("keyword") String keyword, Pageable pageable);


    @Query(value = """
            select
                u.id                as id,
                u.username          as username,
                u.email             as email,
                u.full_name         as fullName,
                u.phone             as phone,
                u.gender            as gender,
                u.date_of_birth     as dateOfBirth,
                u.address           as address,
                u.avatar_url        as avatarUrl,
                u.is_verified       as isVerified,
                u.two_fa_enabled    as twoFaEnabled,
                ur.hospital_id      as hospitalId,
                ur.role             as role,
                u.status            as status,
                u.created_at        as createdAt,
                u.updated_at        as updatedAt
            from users u
            join user_roles ur on ur.user_id = u.id
            where u.id = :id
            """, nativeQuery = true)
    UserResponse getUser(@Param("id") Long id);

    UserEntity findByUsername(String username);

    UserEntity findByEmail(String email);
}
