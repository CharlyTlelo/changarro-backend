package com.changarro.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record ReviewRequest(
    @NotBlank String businessId,
    @Min(1) @Max(5) int rating,
    @NotBlank String text,
    List<String> photos
) {}
