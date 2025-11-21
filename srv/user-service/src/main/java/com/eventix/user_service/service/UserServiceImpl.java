package com.eventix.user_service.service;

import com.eventix.user_service.model.User;
import com.eventix.user_service.dto.UserRegistrationDTO;
import com.eventix.user_service.repository.UserRepository;
import com.eventix.user_service.exception.NotFoundException;
import com.eventix.user_service.exception.ConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private com.eventix.user_service.repository.RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(UserRegistrationDTO dto) {
        // Check if email already exists
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new ConflictException("User with email " + dto.getEmail() + " already exists");
        }
        
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword())); 
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setEmailVerified(false);
        user.setActive(true);
        user.setOrganizerId(null); // attendee registration
        // Assign default role 'attendee'
        var attendeeRole = roleRepository.findByName("attendee").orElseThrow(
            () -> new NotFoundException("Default role 'attendee' not found")
        );
        java.util.Set<com.eventix.user_service.model.Role> roles = new java.util.HashSet<>();
        roles.add(attendeeRole);
        user.setRoles(roles);
        
        return userRepository.save(user);
    }

    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User updateUser(Long id, UserRegistrationDTO dto) {
        User user = userRepository.findById(id).orElseThrow(
            () -> new NotFoundException("User not found with id: " + id)
        );
        
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setEmail(dto.getEmail());
        // Optionally update password (should be hashed)
        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }
        return userRepository.save(user);
    }

    @Override
    public void deactivateUser(Long id) {
        User user = userRepository.findById(id).orElseThrow(
            () -> new NotFoundException("User not found with id: " + id)
        );
        user.setActive(false);
        userRepository.save(user);
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

    @Override
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
}
