package com.attendance.service;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.attendance.dto.AuthResponse;
import com.attendance.dto.LoginRequest;
import com.attendance.dto.RegisterRequest;
import com.attendance.dto.UserSummaryDto;
import com.attendance.entity.User;
import com.attendance.entity.UserRole;
import com.attendance.exception.ResourceNotFoundException;
import com.attendance.repository.UserRepository;
import com.attendance.security.JwtService;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        if (!authentication.isAuthenticated()) {
            throw new ResourceNotFoundException("Invalid credentials");
        }

        User user = userRepository.findByUsernameOrEmail(request.getUsername(), request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        String token = jwtService.generateToken(user);
        return new AuthResponse(token, user.getUsername(), user.getRole().name());
    }

    public AuthResponse register(RegisterRequest request) {
        Optional<User> existingUser = userRepository.findByUsernameOrEmail(request.getUsername(), request.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Username or email already exists");
        }

        UserRole role = UserRole.valueOf(request.getRole().toUpperCase());
        User user = new User(request.getUsername(), request.getEmail(), passwordEncoder.encode(request.getPassword()), role);
        userRepository.save(user);

        String token = jwtService.generateToken(user);
        return new AuthResponse(token, user.getUsername(), user.getRole().name());
    }

    public UserSummaryDto getCurrentUser(String username) {
        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return new UserSummaryDto(user.getId(), user.getUsername(), user.getEmail(), user.getRole().name());
    }
}
