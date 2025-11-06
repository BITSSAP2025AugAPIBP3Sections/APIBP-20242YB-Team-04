package com.eventix.user_service.graphql;

import com.eventix.user_service.model.User;
import com.eventix.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class UserQuery {
    @Autowired
    private UserService userService;

    @QueryMapping
    public User user(@Argument Long id) {
        Optional<User> userOpt = userService.getUserById(id);
        return userOpt.orElse(null);
    }

    @QueryMapping
    public List<User> users() {
        return userService.getAllUsers();
    }
}
