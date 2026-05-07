package com.changarro.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record RegisterBusinessRequest(
    String name,
    @Email String email,
    String whatsapp,
    @Size(min = 6) String password,
    String businessName,
    String categoryId,
    String subcategoryId,
    String phone,
    String address,
    String description
) {}
