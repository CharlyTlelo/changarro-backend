package com.changarro.service;

import com.changarro.model.Business;
import com.changarro.model.Review;
import com.changarro.repository.BusinessRepository;
import com.changarro.repository.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class BusinessService {

    private final BusinessRepository businessRepository;
    private final ReviewRepository reviewRepository;

    public BusinessService(BusinessRepository businessRepository, ReviewRepository reviewRepository) {
        this.businessRepository = businessRepository;
        this.reviewRepository = reviewRepository;
    }

    public List<Business> findAll() {
        return businessRepository.findAll();
    }

    public Business findById(String id) {
        return businessRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Negocio no encontrado: " + id));
    }

    public Business findByOwner(String ownerId) {
        List<Business> owned = businessRepository.findByOwnerId(ownerId);
        if (owned.isEmpty()) {
            throw new IllegalArgumentException("No tienes un negocio registrado");
        }
        return owned.getFirst();
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
        if (updates.getInstagram() != null) existing.setInstagram(updates.getInstagram());
        if (updates.getPaymentMethod() != null) existing.setPaymentMethod(updates.getPaymentMethod());
        if (updates.getPriceRange() != null) existing.setPriceRange(updates.getPriceRange());
        if (updates.getNeighborhood() != null) existing.setNeighborhood(updates.getNeighborhood());
        if (updates.getCategoryId() != null) existing.setCategoryId(updates.getCategoryId());
        if (updates.getEmoji() != null) existing.setEmoji(updates.getEmoji());
        if (updates.getColor() != null) existing.setColor(updates.getColor());
        if (updates.getTag() != null) existing.setTag(updates.getTag());
        if (updates.getActivePromo() != null) existing.setActivePromo(updates.getActivePromo());
        if (updates.getMenuItems() != null && !updates.getMenuItems().isEmpty()) {
            existing.setMenuItems(updates.getMenuItems());
        }
        return businessRepository.save(existing);
    }

    public Business updateMenu(String ownerId, List<Business.MenuItem> items) {
        Business biz = findByOwner(ownerId);
        biz.setMenuItems(items);
        return businessRepository.save(biz);
    }

    public Business updatePromo(String ownerId, Business.Promo promo) {
        Business biz = findByOwner(ownerId);
        biz.setActivePromo(promo);
        return businessRepository.save(biz);
    }

    public Business removePromo(String ownerId) {
        Business biz = findByOwner(ownerId);
        biz.setActivePromo(null);
        return businessRepository.save(biz);
    }

    public void updateRating(String businessId, double newRating, int newCount) {
        Business biz = findById(businessId);
        biz.setRating(newRating);
        biz.setReviewCount(newCount);
        businessRepository.save(biz);
    }

    public Map<String, Object> getAnalytics(String ownerId) {
        Business biz = findByOwner(ownerId);
        List<Review> reviews = reviewRepository.findByBusinessIdOrderByCreatedAtDesc(biz.getId());

        Map<String, Object> analytics = new LinkedHashMap<>();
        analytics.put("businessId", biz.getId());
        analytics.put("businessName", biz.getName());
        analytics.put("visitCount", biz.getVisitCount());
        analytics.put("reviewCount", biz.getReviewCount());
        analytics.put("rating", biz.getRating());
        analytics.put("menuItemCount", biz.getMenuItems().size());
        analytics.put("hasActivePromo", biz.getActivePromo() != null);
        analytics.put("isTrending", biz.isTrending());

        int totalOrders = biz.getMenuItems().stream()
                .mapToInt(Business.MenuItem::getOrderCount)
                .sum();
        analytics.put("totalOrders", totalOrders);

        String topItem = biz.getMenuItems().stream()
                .max((a, b) -> Integer.compare(a.getOrderCount(), b.getOrderCount()))
                .map(Business.MenuItem::getName)
                .orElse(null);
        analytics.put("topMenuItem", topItem);

        List<Map<String, Object>> recentReviews = reviews.stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .limit(5)
                .map(r -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("id", r.getId());
                    m.put("rating", r.getRating());
                    m.put("text", r.getText());
                    m.put("createdAt", r.getCreatedAt().toString());
                    return m;
                })
                .toList();
        analytics.put("recentReviews", recentReviews);

        return analytics;
    }
}
