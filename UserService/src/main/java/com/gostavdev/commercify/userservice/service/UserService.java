package com.gostavdev.commercify.userservice.service;

import com.gostavdev.commercify.userservice.UserRepository;
import com.gostavdev.commercify.userservice.dto.UserDTO;
import com.gostavdev.commercify.userservice.dto.mappers.UserDTOMapper;
import com.gostavdev.commercify.userservice.entities.User;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserDTOMapper mapper;

    public UserDTO getUserById(Long id) {
        return userRepository.findById(id).map(mapper).orElseThrow(() -> new BadCredentialsException("No user found with id: " + id));
    }
}
