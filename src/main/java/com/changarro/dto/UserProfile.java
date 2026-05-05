package com.changarro.dto;

import java.util.List;

public record UserProfile(
    String id,
    String name,
    String email,
    String phone,
    String avatarEmoji,
    int coins,
    int level,
    String levelName,
    int xp,
    int visitedCount,
    int favoriteCount,
    int reviewCount,
    int stampCount,
    List<String> stampIds,
    List<String> favoriteIds,
    List<String> visitedIds
) {}
