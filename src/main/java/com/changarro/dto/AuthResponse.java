package com.changarro.dto;

public record AuthResponse(
    String token,
    String userId,
    String name,
    String email,
    String role,
    String businessId,
    int coins,
    int level,
    String levelName
) {}
