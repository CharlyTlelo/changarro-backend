package com.changarro.service;

import com.changarro.dto.UpdateProfileRequest;
import com.changarro.dto.UserProfile;
import com.changarro.model.User;
import com.changarro.repository.ReviewRepository;
import com.changarro.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    public UserService(UserRepository userRepository, ReviewRepository reviewRepository) {
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
    }

    public UserProfile getProfile(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        long reviewCount = reviewRepository.findByUserId(userId).size();

        return new UserProfile(
                user.getId(), user.getName(), user.getEmail(), user.getPhone(),
                user.getAvatarEmoji(),
                user.getCoins(), user.getLevel(), user.getLevelName(), user.getXp(),
                user.getVisitedIds().size(), user.getFavoriteIds().size(),
                (int) reviewCount, user.getStampIds().size(),
                user.getStampIds(), user.getFavoriteIds(), user.getVisitedIds()
        );
    }

    public UserProfile updateProfile(String userId, UpdateProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (request.name() != null && !request.name().isBlank()) {
            user.setName(request.name().trim());
        }
        if (request.phone() != null) {
            user.setPhone(request.phone().isBlank() ? null : request.phone().trim());
        }

        userRepository.save(user);
        return getProfile(userId);
    }

    public void deleteAccount(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        // Delete user's reviews
        reviewRepository.findByUserId(userId).forEach(r -> reviewRepository.deleteById(r.getId()));
        // Delete user
        userRepository.delete(user);
    }

    public User toggleFavorite(String userId, String businessId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (user.getFavoriteIds().contains(businessId)) {
            user.getFavoriteIds().remove(businessId);
        } else {
            user.getFavoriteIds().add(businessId);
        }
        return userRepository.save(user);
    }

    public User visitBusiness(String userId, String businessId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (!user.getVisitedIds().contains(businessId)) {
            user.getVisitedIds().add(businessId);
            user.setCoins(user.getCoins() + 20); // coins for visiting
            user.setXp(user.getXp() + 10);
        }
        return userRepository.save(user);
    }

    public User redeemReward(String userId, String rewardId, int cost) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (user.getCoins() < cost) {
            throw new IllegalArgumentException("No tienes suficientes monedas");
        }

        user.setCoins(user.getCoins() - cost);
        user.getRedeemedRewardIds().add(rewardId);
        return userRepository.save(user);
    }
}
