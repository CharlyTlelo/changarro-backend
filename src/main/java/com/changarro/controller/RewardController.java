package com.changarro.controller;

import com.changarro.model.Reward;
import com.changarro.repository.RewardRepository;
import com.changarro.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rewards")
public class RewardController {

    private final RewardRepository rewardRepository;
    private final UserService userService;

    public RewardController(RewardRepository rewardRepository, UserService userService) {
        this.rewardRepository = rewardRepository;
        this.userService = userService;
    }

    @GetMapping
    public List<Reward> getAll() {
        return rewardRepository.findAll();
    }

    @PostMapping("/{id}/redeem")
    public Reward redeem(@PathVariable String id, Authentication auth) {
        String userId = (String) auth.getPrincipal();
        Reward reward = rewardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Recompensa no encontrada"));

        userService.redeemReward(userId, id, reward.getCost());

        reward.setRedeemedCount(reward.getRedeemedCount() + 1);
        return rewardRepository.save(reward);
    }
}
