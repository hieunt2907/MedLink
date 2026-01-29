package com.hieunt.medlink.app.requests.role;

import com.hieunt.medlink.app.entities.UserRoleEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRoleCreateRequest {
    private Long userId;
    private UserRoleEntity.Role role;

}
