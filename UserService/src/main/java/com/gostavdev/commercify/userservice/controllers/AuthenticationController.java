package com.gostavdev.commercify.userservice.controllers;

import com.gostavdev.commercify.userservice.dto.LoginUserDTO;
import com.gostavdev.commercify.userservice.dto.RegisterUserDTO;
import com.gostavdev.commercify.userservice.entities.User;
import com.gostavdev.commercify.userservice.responses.AuthResponse;
import com.gostavdev.commercify.userservice.service.AuthenticationService;
import com.gostavdev.commercify.userservice.service.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<User> register(@RequestBody RegisterUserDTO registerRequest) {
        User user = authenticationService.signup(registerRequest);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/signin")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginUserDTO loginRequest) {
        User authenticatedUser = authenticationService.authenticate(loginRequest);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        AuthResponse authResponse = new AuthResponse(jwtToken, jwtService.getExpirationTime());

        return ResponseEntity.ok(authResponse);
    }
}
