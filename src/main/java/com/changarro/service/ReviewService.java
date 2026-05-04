package com.changarro.service;

import com.changarro.dto.ReviewRequest;
import com.changarro.model.Review;
import com.changarro.model.User;
import com.changarro.repository.ReviewRepository;
import com.changarro.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final BusinessService businessService;

    private static final int REVIEW_COINS = 50;

    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository,
                         BusinessService businessService) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.businessService = businessService;
    }

    public List<Review> findByBusiness(String businessId) {
        return reviewRepository.findByBusinessIdOrderByCreatedAtDesc(businessId);
    }

    public Review create(String userId, ReviewRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Review review = new Review();
        review.setBusinessId(request.businessId());
        review.setUserId(userId);
        review.setUserName(user.getName());
        review.setUserLevel(user.getLevelName());
        review.setUserEmoji(user.getAvatarEmoji());
        review.setRating(request.rating());
        review.setText(request.text());
        if (request.photos() != null) review.setPhotos(request.photos());

        review = reviewRepository.save(review);

        // Award coins for reviewing
        user.setCoins(user.getCoins() + REVIEW_COINS);
        user.setXp(user.getXp() + 25);
        updateLevel(user);
        userRepository.save(user);

        // Update business rating
        recalculateRating(request.businessId());

        return review;
    }

    public Review likeReview(String reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Resena no encontrada"));
        review.setLikes(review.getLikes() + 1);
        return reviewRepository.save(review);
    }

    private void recalculateRating(String businessId) {
        List<Review> reviews = reviewRepository.findByBusinessIdOrderByCreatedAtDesc(businessId);
        if (reviews.isEmpty()) return;
        double avg = reviews.stream().mapToInt(Review::getRating).average().orElse(0);
        businessService.updateRating(businessId, Math.round(avg * 10.0) / 10.0, reviews.size());
    }

    private void updateLevel(User user) {
        int xp = user.getXp();
        if (xp >= 1000) { user.setLevel(5); user.setLevelName("Leyenda"); }
        else if (xp >= 500) { user.setLevel(4); user.setLevelName("Embajador"); }
        else if (xp >= 200) { user.setLevel(3); user.setLevelName("Vecino"); }
        else if (xp >= 50) { user.setLevel(2); user.setLevelName("Explorador"); }
        else { user.setLevel(1); user.setLevelName("Nuevo"); }
    }
}
