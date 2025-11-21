package com.eventix.user_service.service;

import com.eventix.user_service.model.User;
import com.eventix.user_service.dto.UserRegistrationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface UserService {
    User registerUser(UserRegistrationDTO dto);
    Optional<User> getUserById(Long id);
    User updateUser(Long id, UserRegistrationDTO dto);
    void deactivateUser(Long id);
    Optional<User> getCurrentUser();

    java.util.List<User> getAllUsers();
    Page<User> getAllUsers(Pageable pageable);
}
