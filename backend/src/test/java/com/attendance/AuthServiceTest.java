package com.attendance;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.attendance.dto.AuthResponse;
import com.attendance.dto.LoginRequest;
import com.attendance.dto.RegisterRequest;
import com.attendance.entity.User;
import com.attendance.entity.UserRole;
import com.attendance.repository.UserRepository;
import com.attendance.security.JwtService;
import com.attendance.service.AuthService;

class AuthServiceTest {

    @Test
    void loginReturnsTokenForValidUser() {
        UserRepository userRepository = mock(UserRepository.class);
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        JwtService jwtService = new JwtService();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        User user = new User("admin", "admin@example.com", encoder.encode("secret123"), UserRole.ADMIN);
        when(userRepository.findByUsernameOrEmail("admin", "admin")).thenReturn(Optional.of(user));

        Authentication authentication = new UsernamePasswordAuthenticationToken("admin", "secret123", java.util.List.of());
        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        AuthService authService = new AuthService(userRepository, encoder, authenticationManager, jwtService);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("admin");
        loginRequest.setPassword("secret123");

        AuthResponse response = authService.login(loginRequest);
        assertNotNull(response.getToken());
        assertEquals("admin", response.getUsername());
    }

    @Test
    void registerCreatesUser() {
        UserRepository userRepository = mock(UserRepository.class);
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        JwtService jwtService = new JwtService();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        when(userRepository.findByUsernameOrEmail("newuser", "new@example.com")).thenReturn(Optional.empty());
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AuthService authService = new AuthService(userRepository, encoder, authenticationManager, jwtService);
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newuser");
        request.setEmail("new@example.com");
        request.setPassword("secret123");
        request.setFullName("New User");
        request.setRole("ADMIN");

        AuthResponse response = authService.register(request);
        assertNotNull(response.getToken());
        assertEquals("newuser", response.getUsername());
    }
}
