package com.changarro.dto;

public record AuthResponse(
    String token,
    String userId,
    String name,
    String email,
    int coins,
    int level,
    String levelName
) {}
