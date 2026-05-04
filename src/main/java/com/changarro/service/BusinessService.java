package com.changarro.service;

import com.changarro.model.Business;
import com.changarro.repository.BusinessRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusinessService {

    private final BusinessRepository businessRepository;

    public BusinessService(BusinessRepository businessRepository) {
        this.businessRepository = businessRepository;
    }

    public List<Business> findAll() {
        return businessRepository.findAll();
    }

    public Business findById(String id) {
        return businessRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Negocio no encontrado: " + id));
    }

    public List<Business> findByCategory(String categoryId) {
        return businessRepository.findByCategoryId(categoryId);
    }

    public List<Business> findTrending() {
        return businessRepository.findByTrendingTrue();
    }

    public List<Business> findNew() {
        return businessRepository.findByNuevoTrue();
    }

    public List<Business> findWithPromo() {
        return businessRepository.findByActivePromoIsNotNull();
    }

    public List<Business> search(String query) {
        return businessRepository.findByNameContainingIgnoreCase(query);
    }

    public Business create(Business business) {
        return businessRepository.save(business);
    }

    public Business update(String id, Business updates) {
        Business existing = findById(id);
        if (updates.getName() != null) existing.setName(updates.getName());
        if (updates.getDescription() != null) existing.setDescription(updates.getDescription());
        if (updates.getAddress() != null) existing.setAddress(updates.getAddress());
        if (updates.getPhone() != null) existing.setPhone(updates.getPhone());
        if (updates.getSchedule() != null) existing.setSchedule(updates.getSchedule());
        if (updates.getActivePromo() != null) existing.setActivePromo(updates.getActivePromo());
        if (updates.getMenuItems() != null && !updates.getMenuItems().isEmpty()) {
            existing.setMenuItems(updates.getMenuItems());
        }
        return businessRepository.save(existing);
    }

    public void updateRating(String businessId, double newRating, int newCount) {
        Business biz = findById(businessId);
        biz.setRating(newRating);
        biz.setReviewCount(newCount);
        businessRepository.save(biz);
    }
}
