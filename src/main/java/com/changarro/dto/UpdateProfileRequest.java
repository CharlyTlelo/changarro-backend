package com.changarro.dto;

import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    String name,

    @Size(max = 20, message = "El teléfono no puede tener más de 20 caracteres")
    String phone
) {}
