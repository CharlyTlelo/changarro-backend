package com.changarro.controller;

import com.changarro.dto.ReviewRequest;
import com.changarro.model.Review;
import com.changarro.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    public List<Review> getByBusiness(@RequestParam String businessId) {
        return reviewService.findByBusiness(businessId);
    }

    @PostMapping
    public ResponseEntity<Review> create(@Valid @RequestBody ReviewRequest request, Authentication auth) {
        String userId = (String) auth.getPrincipal();
        Review review = reviewService.create(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }

    @PostMapping("/{id}/like")
    public Review like(@PathVariable String id) {
        return reviewService.likeReview(id);
    }
}
