package com.changarro.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "reviews")
public class Review {

    @Id
    private String id;

    private String businessId;
    private String userId;
    private String userName;
    private String userLevel;
    private String userEmoji;

    private int rating;         // 1-5
    private String text;
    private List<String> photos = new ArrayList<>();

    private int likes = 0;
    private Instant createdAt = Instant.now();

    public Review() {}

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getBusinessId() { return businessId; }
    public void setBusinessId(String businessId) { this.businessId = businessId; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserLevel() { return userLevel; }
    public void setUserLevel(String userLevel) { this.userLevel = userLevel; }

    public String getUserEmoji() { return userEmoji; }
    public void setUserEmoji(String userEmoji) { this.userEmoji = userEmoji; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public List<String> getPhotos() { return photos; }
    public void setPhotos(List<String> photos) { this.photos = photos; }

    public int getLikes() { return likes; }
    public void setLikes(int likes) { this.likes = likes; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
