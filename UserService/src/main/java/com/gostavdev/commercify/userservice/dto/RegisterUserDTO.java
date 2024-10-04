package com.gostavdev.commercify.userservice.dto;

public record RegisterUserDTO(
        String email,
        String password,
        String firstName,
        String lastName) {
}
