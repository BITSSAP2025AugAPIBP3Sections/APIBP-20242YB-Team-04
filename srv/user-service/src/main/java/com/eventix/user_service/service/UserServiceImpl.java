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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger access = LogManager.getLogger("access");
    private static final Logger error  = LogManager.getLogger("error");
    private static final Logger debug  = LogManager.getLogger("debug");

    @Autowired
    private com.eventix.user_service.repository.RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(UserRegistrationDTO dto) {
        access.info("registerUser called email={}", dto.getEmail());
        debug.debug("registerUser payload: firstName={} lastName={}", dto.getFirstName(), dto.getLastName());

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            error.error("registerUser conflict: email already exists={}", dto.getEmail());
            throw new ConflictException("User with email " + dto.getEmail() + " already exists");
        }

        try {
            var attendeeRole = roleRepository.findByName("attendee")
                    .orElseThrow(() -> {
                        error.error("Default role 'attendee' not found during registration email={}", dto.getEmail());
                        return new NotFoundException("Default role 'attendee' not found");
                    });

            User user = new User();
            user.setEmail(dto.getEmail());
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
            user.setFirstName(dto.getFirstName());
            user.setLastName(dto.getLastName());
            user.setPhoneNumber(dto.getPhoneNumber());
            user.setEmailVerified(false);
            user.setActive(true);
            user.setOrganizerId(null); // attendee registration

            java.util.Set<com.eventix.user_service.model.Role> roles = new java.util.HashSet<>();
            roles.add(attendeeRole);
            user.setRoles(roles);

            User saved = userRepository.save(user);
            access.info("registerUser successful userId={} email={}", saved.getId(), saved.getEmail());
            return saved;

        } catch (RuntimeException e) {
            error.error("registerUser failed email={} reason={}", dto.getEmail(), e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Optional<User> getUserById(Long id) {
        access.info("getUserById called userId={}", id);
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            access.info("getUserById found userId={}", id);
        } else {
            debug.debug("getUserById no result for userId={}", id);
        }
        return user;
    }

    @Override
    public User updateUser(Long id, UserRegistrationDTO dto) {
        access.info("updateUser called userId={}", id);
        debug.debug("updateUser payload: firstName={} lastName={}", dto.getFirstName(), dto.getLastName());

        User user = userRepository.findById(id).orElseThrow(() -> {
            error.error("updateUser not found userId={}", id);
            return new NotFoundException("User not found with id: " + id);
        });

        try {
            user.setFirstName(dto.getFirstName());
            user.setLastName(dto.getLastName());
            user.setPhoneNumber(dto.getPhoneNumber());
            user.setEmail(dto.getEmail());
            if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(dto.getPassword()));
                debug.debug("updateUser password updated for userId={}", id);
            }
            User saved = userRepository.save(user);
            access.info("updateUser successful userId={}", saved.getId());
            return saved;
        } catch (RuntimeException e) {
            error.error("updateUser failed userId={} reason={}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public void deactivateUser(Long id) {
        access.info("deactivateUser called userId={}", id);

        User user = userRepository.findById(id).orElseThrow(() -> {
            error.error("deactivateUser not found userId={}", id);
            return new NotFoundException("User not found with id: " + id);
        });

        try {
            user.setActive(false);
            userRepository.save(user);
            access.info("deactivateUser successful userId={}", id);
        } catch (RuntimeException e) {
            error.error("deactivateUser failed userId={} reason={}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public Optional<User> getCurrentUser() {
        access.info("getCurrentUser called");
        try {
            Object principal = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principal instanceof Long userId) {
                debug.debug("getCurrentUser resolved userId={}", userId);
                return userRepository.findById(userId);
            } else {
                debug.debug("getCurrentUser principal not Long: {}", principal);
                return Optional.empty();
            }
        } catch (Exception e) {
            error.error("getCurrentUser failed reason={}", e.getMessage(), e);
            return Optional.empty();
        }
    }

    @Override
    public java.util.List<User> getAllUsers() {
        access.info("getAllUsers called");
        java.util.List<User> list = userRepository.findAll();
        debug.debug("getAllUsers returned count={}", list.size());
        return list;
    }

    @Override
    public Page<User> getAllUsers(Pageable pageable) {
        access.info("getAllUsers(page) called page={} size={}", pageable.getPageNumber(), pageable.getPageSize());
        Page<User> page = userRepository.findAll(pageable);
        debug.debug("getAllUsers(page) returned totalElements={}", page.getTotalElements());
        return page;
    }
}
