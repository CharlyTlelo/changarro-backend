package com.changarro.service;

import com.changarro.config.JwtService;
import com.changarro.dto.AuthResponse;
import com.changarro.dto.LoginRequest;
import com.changarro.dto.RegisterBusinessRequest;
import com.changarro.dto.RegisterRequest;
import com.changarro.model.Business;
import com.changarro.model.User;
import com.changarro.repository.BusinessRepository;
import com.changarro.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BusinessRepository businessRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, BusinessRepository businessRepository,
                       PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.businessRepository = businessRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("El correo ya esta registrado");
        }

        User user = new User(request.name(), request.email(), passwordEncoder.encode(request.password()));
        user.setCoins(100);
        user = userRepository.save(user);

        String token = jwtService.generateToken(user.getId(), user.getEmail());

        return new AuthResponse(token, user.getId(), user.getName(), user.getEmail(),
                user.getRole(), null,
                user.getCoins(), user.getLevel(), user.getLevelName());
    }

    public AuthResponse registerBusiness(RegisterBusinessRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new IllegalArgumentException("El correo ya esta registrado");
        }

        User user = new User(request.name(), request.email(), passwordEncoder.encode(request.password()));
        user.setRole("BUSINESS");
        user.setPhone(request.phone());
        user.setCoins(0);
        user = userRepository.save(user);

        Business business = new Business();
        business.setName(request.businessName());
        business.setPhone(request.phone());
        business.setAddress(request.address());
        business.setDescription(request.description());
        business.setOwnerId(user.getId());
        business.setNuevo(true);
        business = businessRepository.save(business);

        String token = jwtService.generateToken(user.getId(), user.getEmail());

        return new AuthResponse(token, user.getId(), user.getName(), user.getEmail(),
                user.getRole(), business.getId(),
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

        String businessId = null;
        if ("BUSINESS".equals(user.getRole())) {
            List<Business> owned = businessRepository.findByOwnerId(user.getId());
            if (!owned.isEmpty()) {
                businessId = owned.getFirst().getId();
            }
        }

        String token = jwtService.generateToken(user.getId(), user.getEmail());

        return new AuthResponse(token, user.getId(), user.getName(), user.getEmail(),
                user.getRole(), businessId,
                user.getCoins(), user.getLevel(), user.getLevelName());
    }

    public AuthResponse adminLogin(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Correo o contrasena incorrectos"));

        if (!"ADMIN".equals(user.getRole())) {
            throw new IllegalArgumentException("Acceso denegado");
        }

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("Correo o contrasena incorrectos");
        }

        user.setLastLogin(java.time.Instant.now());
        userRepository.save(user);

        String token = jwtService.generateToken(user.getId(), user.getEmail());

        return new AuthResponse(token, user.getId(), user.getName(), user.getEmail(),
                user.getRole(), null,
                user.getCoins(), user.getLevel(), user.getLevelName());
    }
}
