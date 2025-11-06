package com.eventix.user_service.service;

import com.eventix.user_service.model.Role;
import java.util.List;

public interface RoleService {
    List<Role> getAllRoles();
    Role assignRole(Long userId, String roleName);
}
