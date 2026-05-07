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
        String whatsapp = firstNotBlank(request.whatsapp(), request.phone());
        if (isBlank(request.name()) || isBlank(whatsapp) || isBlank(request.password())) {
            throw new IllegalArgumentException("Nombre, WhatsApp y contrasena son requeridos");
        }

        String normalizedEmail = normalizeEmail(request.email());
        String normalizedWhatsapp = normalizePhone(whatsapp);
        String storageEmail = normalizedEmail != null ? normalizedEmail : normalizedWhatsapp + "@whatsapp.local";

        if (userRepository.existsByEmail(storageEmail) || userRepository.existsByPhone(normalizedWhatsapp)) {
            throw new IllegalArgumentException("Este WhatsApp ya esta registrado");
        }

        User user = new User(request.name().trim(), storageEmail, passwordEncoder.encode(request.password()));
        user.setPhone(normalizedWhatsapp);
        user.setCoins(100);
        user = userRepository.save(user);

        String token = jwtService.generateToken(user.getId(), user.getEmail());

        return new AuthResponse(token, user.getId(), user.getName(), user.getEmail(),
                user.getRole(), null,
                user.getCoins(), user.getLevel(), user.getLevelName());
    }

    public AuthResponse registerBusiness(RegisterBusinessRequest request) {
        String whatsapp = firstNotBlank(request.whatsapp(), request.phone());
        if (isBlank(request.name()) || isBlank(request.businessName()) || isBlank(request.password()) || isBlank(whatsapp)) {
            throw new IllegalArgumentException("Nombre, WhatsApp, contrasena y negocio son requeridos");
        }

        String normalizedEmail = normalizeEmail(request.email());
        String normalizedWhatsapp = normalizePhone(whatsapp);
        String storageEmail = normalizedEmail != null ? normalizedEmail : normalizedWhatsapp + "@whatsapp.local";

        if (userRepository.existsByEmail(storageEmail) || userRepository.existsByPhone(normalizedWhatsapp)) {
            throw new IllegalArgumentException("Este WhatsApp ya esta registrado");
        }

        User user = new User(request.name().trim(), storageEmail, passwordEncoder.encode(request.password()));
        user.setRole("BUSINESS");
        user.setPhone(normalizedWhatsapp);
        user.setCoins(0);
        user = userRepository.save(user);

        Business business = new Business();
        business.setName(request.businessName().trim());
        business.setPhone(normalizedWhatsapp);
        business.setAddress(request.address());
        business.setDescription(request.description());
        business.setCategoryId(request.categoryId());
        business.setSubcategoryId(request.subcategoryId());
        business.setOwnerId(user.getId());
        business.setNuevo(true);
        business = businessRepository.save(business);

        String token = jwtService.generateToken(user.getId(), user.getEmail());

        return new AuthResponse(token, user.getId(), user.getName(), user.getEmail(),
                user.getRole(), business.getId(),
                user.getCoins(), user.getLevel(), user.getLevelName());
    }

    public AuthResponse login(LoginRequest request) {
        String identifier = request.email().trim();
        User user = findByIdentifier(identifier)
                .orElseThrow(() -> new IllegalArgumentException("WhatsApp o contrasena incorrectos"));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new IllegalArgumentException("WhatsApp o contrasena incorrectos");
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
        User user = userRepository.findByEmail(normalizeEmail(request.email()))
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

    private java.util.Optional<User> findByIdentifier(String rawIdentifier) {
        if (rawIdentifier.contains("@")) {
            return userRepository.findByEmail(normalizeEmail(rawIdentifier));
        }

        String normalizedPhone = normalizePhone(rawIdentifier);
        return userRepository.findByPhone(normalizedPhone)
                .or(() -> userRepository.findByPhone(rawIdentifier.trim()))
                .or(() -> userRepository.findByEmail(normalizedPhone + "@whatsapp.local"))
                .or(() -> userRepository.findAll().stream()
                        .filter(user -> normalizePhone(user.getPhone()).equals(normalizedPhone))
                        .findFirst());
    }

    private String normalizeEmail(String value) {
        if (isBlank(value)) return null;
        return value.trim().toLowerCase();
    }

    private String normalizePhone(String value) {
        if (value == null) return "";
        return value.replaceAll("\\D", "");
    }

    private String firstNotBlank(String first, String second) {
        if (!isBlank(first)) return first.trim();
        if (!isBlank(second)) return second.trim();
        return "";
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
