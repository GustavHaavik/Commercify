package com.gostavdev.commercify.userservice.controllers;

import com.gostavdev.commercify.userservice.dto.UserDTO;
import com.gostavdev.commercify.userservice.entities.User;
import com.gostavdev.commercify.userservice.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    // Endpoint to fetch user details by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/me")
    public ResponseEntity<User> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(currentUser);
    }
}
