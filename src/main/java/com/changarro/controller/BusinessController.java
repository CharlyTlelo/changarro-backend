package com.changarro.controller;

import com.changarro.model.Business;
import com.changarro.service.BusinessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/businesses")
public class BusinessController {

    private final BusinessService businessService;

    public BusinessController(BusinessService businessService) {
        this.businessService = businessService;
    }

    @GetMapping
    public List<Business> getAll(@RequestParam(required = false) String category,
                                  @RequestParam(required = false) String filter,
                                  @RequestParam(required = false) String q) {
        if (q != null && !q.isBlank()) {
            return businessService.search(q);
        }
        if (category != null && !category.isBlank()) {
            return businessService.findByCategory(category);
        }
        if (filter != null) {
            return switch (filter) {
                case "trending" -> businessService.findTrending();
                case "new" -> businessService.findNew();
                case "promo" -> businessService.findWithPromo();
                default -> businessService.findAll();
            };
        }
        return businessService.findAll();
    }

    @GetMapping("/{id}")
    public Business getById(@PathVariable String id) {
        return businessService.findById(id);
    }

    @PostMapping
    public ResponseEntity<Business> create(@RequestBody Business business, Authentication auth) {
        business.setOwnerId((String) auth.getPrincipal());
        Business created = businessService.create(business);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping("/{id}")
    public Business update(@PathVariable String id, @RequestBody Business updates) {
        return businessService.update(id, updates);
    }

    @GetMapping("/mine")
    public Business getMyBusiness(Authentication auth) {
        String userId = (String) auth.getPrincipal();
        return businessService.findByOwner(userId);
    }

    @PatchMapping("/mine")
    public Business updateMyBusiness(@RequestBody Business updates, Authentication auth) {
        String userId = (String) auth.getPrincipal();
        Business mine = businessService.findByOwner(userId);
        return businessService.update(mine.getId(), updates);
    }

    @PutMapping("/mine/menu")
    public Business updateMenu(@RequestBody List<Business.MenuItem> items, Authentication auth) {
        String userId = (String) auth.getPrincipal();
        return businessService.updateMenu(userId, items);
    }

    @PutMapping("/mine/promo")
    public Business updatePromo(@RequestBody Business.Promo promo, Authentication auth) {
        String userId = (String) auth.getPrincipal();
        return businessService.updatePromo(userId, promo);
    }

    @DeleteMapping("/mine/promo")
    public Business deletePromo(Authentication auth) {
        String userId = (String) auth.getPrincipal();
        return businessService.removePromo(userId);
    }

    @GetMapping("/mine/analytics")
    public Map<String, Object> getAnalytics(Authentication auth) {
        String userId = (String) auth.getPrincipal();
        return businessService.getAnalytics(userId);
    }
}
