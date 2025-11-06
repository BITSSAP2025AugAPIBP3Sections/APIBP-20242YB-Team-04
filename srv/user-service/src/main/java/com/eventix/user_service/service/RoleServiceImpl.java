package com.eventix.user_service.service;

import com.eventix.user_service.model.Role;
import com.eventix.user_service.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private com.eventix.user_service.repository.UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role assignRole(Long userId, String roleName) {
        var userOpt = userRepository.findById(userId);
        var roleOpt = roleRepository.findByName(roleName);
        if (userOpt.isPresent() && roleOpt.isPresent()) {
            var user = userOpt.get();
            var role = roleOpt.get();
            if (user.getRoles() == null) user.setRoles(new java.util.HashSet<>());
            user.getRoles().add(role);
            userRepository.save(user);
            return role;
        }
        return null;
    }
}
