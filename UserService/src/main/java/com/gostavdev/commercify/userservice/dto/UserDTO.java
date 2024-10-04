package com.gostavdev.commercify.userservice.dto;

public record UserDTO(
        Long id,
        String email,
        String firstName,
        String lastName) {
}
