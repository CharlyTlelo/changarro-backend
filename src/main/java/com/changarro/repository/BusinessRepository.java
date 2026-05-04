package com.changarro.repository;

import com.changarro.model.Business;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BusinessRepository extends MongoRepository<Business, String> {
    List<Business> findByCategoryId(String categoryId);
    List<Business> findByTrendingTrue();
    List<Business> findByNuevoTrue();
    List<Business> findByActivePromoIsNotNull();
    List<Business> findByNameContainingIgnoreCase(String name);
    List<Business> findByOwnerId(String ownerId);
}
