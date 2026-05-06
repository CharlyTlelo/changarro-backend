package com.changarro.config;

import com.changarro.model.User;
import com.changarro.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminBootstrap {

    @Bean
    CommandLineRunner ensureAdminUser(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${changarro.admin.email:}") String email,
            @Value("${changarro.admin.password:}") String password
    ) {
        return args -> {
            if (email == null || email.isBlank() || password == null || password.isBlank()) {
                return;
            }

            String normalizedEmail = email.trim();

            User admin = userRepository.findByEmail(normalizedEmail).orElseGet(() ->
                    new User("Administrador", normalizedEmail, passwordEncoder.encode(password))
            );

            admin.setRole("ADMIN");
            admin.setCoins(0);
            admin.setPassword(passwordEncoder.encode(password));

            userRepository.save(admin);
        };
    }
}

