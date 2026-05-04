package com.changarro.service;

import com.changarro.config.JwtService;
import com.changarro.dto.AuthResponse;
import com.changarro.dto.LoginRequest;
import com.changarro.dto.RegisterRequest;
import com.changarro.model.User;
import com.changarro.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("El correo ya esta registrado");
        }

        User user = new User(request.name(), request.email(), passwordEncoder.encode(request.password()));
        user.setCoins(100); // welcome bonus
        user = userRepository.save(user);

        String token = jwtService.generateToken(user.getId(), user.getEmail());

        return new AuthResponse(token, user.getId(), user.getName(), user.getEmail(),
                user.getCoins(), user.getLevel(), user.getLevelName());
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Correo o contrasena incorrectos"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("Correo o contrasena incorrectos");
        }

        user.setLastLogin(java.time.Instant.now());
        userRepository.save(user);

        String token = jwtService.generateToken(user.getId(), user.getEmail());

        return new AuthResponse(token, user.getId(), user.getName(), user.getEmail(),
                user.getCoins(), user.getLevel(), user.getLevelName());
    }
}
