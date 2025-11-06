package com.eventix.user_service.service;

import com.eventix.user_service.model.User;
import com.eventix.user_service.dto.UserRegistrationDTO;
import com.eventix.user_service.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private com.eventix.user_service.repository.RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public User registerUser(UserRegistrationDTO dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword()); // TODO: hash password in AuthService
        user.setName(dto.getName());
        user.setEmailVerified(false);
        user.setActive(true);
        user.setOrganizerId(null); // attendee registration
        // Assign default role 'attendee'
        var attendeeRole = roleRepository.findByName("attendee").orElse(null);
        if (attendeeRole != null) {
            java.util.Set<com.eventix.user_service.model.Role> roles = new java.util.HashSet<>();
            roles.add(attendeeRole);
            user.setRoles(roles);
        }
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User updateUser(Long id, UserRegistrationDTO dto) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setName(dto.getName());
            user.setEmail(dto.getEmail());
            // Optionally update password (should be hashed)
            if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
                user.setPassword(dto.getPassword());
            }
            return userRepository.save(user);
        }
        return null;
    }

    @Override
    public void deactivateUser(Long id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setActive(false);
            userRepository.save(user);
        }
    }

    @Override
    public Optional<User> getCurrentUser() {
        Object principal = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof Long userId) {
            return userRepository.findById(userId);
        }
        return Optional.empty();
    }

    @Override
    public java.util.List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
