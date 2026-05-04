package com.changarro.repository;

import com.changarro.model.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReviewRepository extends MongoRepository<Review, String> {
    List<Review> findByBusinessIdOrderByCreatedAtDesc(String businessId);
    List<Review> findByUserId(String userId);
    long countByBusinessId(String businessId);
}
