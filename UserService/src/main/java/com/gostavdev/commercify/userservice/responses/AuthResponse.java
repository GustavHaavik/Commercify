package com.gostavdev.commercify.userservice.responses;

public record AuthResponse(String token, long expiresIn) {
}
