package com.changarro.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "users")
public class User {

    @Id
    private String id;

    private String name;

    @Indexed(unique = true)
    private String email;

    private String password;

    private String phone;

    private String avatarEmoji = "😊";

    private String role = "USER"; // USER or BUSINESS

    private int coins = 100;       // start with 100 welcome coins
    private int level = 1;         // Explorador
    private String levelName = "Explorador";
    private int xp = 0;

    private List<String> stampIds = new ArrayList<>();       // earned stamp IDs
    private List<String> favoriteIds = new ArrayList<>();    // favorite business IDs
    private List<String> visitedIds = new ArrayList<>();     // visited business IDs
    private List<String> redeemedRewardIds = new ArrayList<>();

    private Instant createdAt = Instant.now();
    private Instant lastLogin;

    public User() {}

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdAt = Instant.now();
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getAvatarEmoji() { return avatarEmoji; }
    public void setAvatarEmoji(String avatarEmoji) { this.avatarEmoji = avatarEmoji; }

    public int getCoins() { return coins; }
    public void setCoins(int coins) { this.coins = coins; }

    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = level; }

    public String getLevelName() { return levelName; }
    public void setLevelName(String levelName) { this.levelName = levelName; }

    public int getXp() { return xp; }
    public void setXp(int xp) { this.xp = xp; }

    public List<String> getStampIds() { return stampIds; }
    public void setStampIds(List<String> stampIds) { this.stampIds = stampIds; }

    public List<String> getFavoriteIds() { return favoriteIds; }
    public void setFavoriteIds(List<String> favoriteIds) { this.favoriteIds = favoriteIds; }

    public List<String> getVisitedIds() { return visitedIds; }
    public void setVisitedIds(List<String> visitedIds) { this.visitedIds = visitedIds; }

    public List<String> getRedeemedRewardIds() { return redeemedRewardIds; }
    public void setRedeemedRewardIds(List<String> redeemedRewardIds) { this.redeemedRewardIds = redeemedRewardIds; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public Instant getLastLogin() { return lastLogin; }
    public void setLastLogin(Instant lastLogin) { this.lastLogin = lastLogin; }
}
