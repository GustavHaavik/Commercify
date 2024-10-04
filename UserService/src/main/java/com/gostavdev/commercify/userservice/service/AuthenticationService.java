package com.gostavdev.commercify.userservice.service;

import com.gostavdev.commercify.userservice.UserRepository;
import com.gostavdev.commercify.userservice.dto.LoginUserDTO;
import com.gostavdev.commercify.userservice.dto.RegisterUserDTO;
import com.gostavdev.commercify.userservice.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public User signup(RegisterUserDTO register) {
        User user = User.builder()
                .firstName(register.firstName())
                .lastName(register.lastName())
                .email(register.email())
                .password(passwordEncoder.encode(register.password()))
                .build();

        return userRepository.save(user);
    }

    public User authenticate(LoginUserDTO login) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        login.email(),
                        login.password()
                )
        );

        return userRepository.findByEmail(login.email())
                .orElseThrow();
    }
}
