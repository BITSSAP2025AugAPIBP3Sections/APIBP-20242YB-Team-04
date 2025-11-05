package com.eventix.user_service.controller;

import com.eventix.user_service.model.Role;
import com.eventix.user_service.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/roles")
public class RoleController {
    @Autowired
    private RoleService roleService;

    @GetMapping
    public List<Role> getRoles() {
        return roleService.getAllRoles();
    }

    @PostMapping("/assign")
    public Role assignRole(@RequestParam Long userId, @RequestParam String roleName) {
        return roleService.assignRole(userId, roleName);
    }
}
