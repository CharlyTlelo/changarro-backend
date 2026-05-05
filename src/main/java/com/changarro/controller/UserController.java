package com.changarro.controller;

import com.changarro.dto.UpdateProfileRequest;
import com.changarro.dto.UserProfile;
import com.changarro.model.Stamp;
import com.changarro.repository.StampRepository;
import com.changarro.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final StampRepository stampRepository;

    public UserController(UserService userService, StampRepository stampRepository) {
        this.userService = userService;
        this.stampRepository = stampRepository;
    }

    @GetMapping("/me")
    public UserProfile getProfile(Authentication auth) {
        String userId = (String) auth.getPrincipal();
        return userService.getProfile(userId);
    }

    @PatchMapping("/me")
    public UserProfile updateProfile(@Valid @RequestBody UpdateProfileRequest request, Authentication auth) {
        String userId = (String) auth.getPrincipal();
        return userService.updateProfile(userId, request);
    }

    @DeleteMapping("/me")
    public ResponseEntity<Map<String, String>> deleteAccount(Authentication auth) {
        String userId = (String) auth.getPrincipal();
        userService.deleteAccount(userId);
        return ResponseEntity.ok(Map.of("message", "Cuenta eliminada exitosamente"));
    }

    @PostMapping("/me/favorites/{businessId}")
    public Map<String, Object> toggleFavorite(@PathVariable String businessId, Authentication auth) {
        String userId = (String) auth.getPrincipal();
        var user = userService.toggleFavorite(userId, businessId);
        boolean isFav = user.getFavoriteIds().contains(businessId);
        return Map.of("favorited", isFav, "totalFavorites", user.getFavoriteIds().size());
    }

    @PostMapping("/me/visits/{businessId}")
    public Map<String, Object> visitBusiness(@PathVariable String businessId, Authentication auth) {
        String userId = (String) auth.getPrincipal();
        var user = userService.visitBusiness(userId, businessId);
        return Map.of("coins", user.getCoins(), "totalVisits", user.getVisitedIds().size());
    }

    @GetMapping("/stamps")
    public List<Stamp> getAllStamps() {
        return stampRepository.findAll();
    }
}
