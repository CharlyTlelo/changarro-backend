package com.changarro.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
    String name,
    @Email String email,
    String whatsapp,
    String phone,
    @Size(min = 6) String password
) {}
