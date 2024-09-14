package com.gostavdev.commercify.userservice;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
    private final UserService userService;

    public CustomAuthenticationProvider(UserService userService) {
        this.userService = userService;
    }


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String name = authentication.getName();
        final String password = authentication.getCredentials().toString();
        if (!"admin".equals(name) || !"system".equals(password)) {
            return null;
        }
        return authenticateAgainstThirdPartyAndGetAuthentication(name, password);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private UsernamePasswordAuthenticationToken authenticateAgainstThirdPartyAndGetAuthentication(String username, String password) {
        User user = userService.loadUserByUsername(username);

        final UserDetails principal = new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority(user.getRole())));

        return new UsernamePasswordAuthenticationToken(principal, password, principal.getAuthorities());
    }
}
