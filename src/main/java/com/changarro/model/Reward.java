package com.changarro.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "rewards")
public class Reward {

    @Id
    private String id;

    private String name;
    private int cost;           // coins required
    private String emoji;
    private String color;
    private int redeemedCount;
    private String businessId;  // which business offers this

    public Reward() {}

    public Reward(String name, int cost, String emoji, String color, String businessId) {
        this.name = name;
        this.cost = cost;
        this.emoji = emoji;
        this.color = color;
        this.businessId = businessId;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getCost() { return cost; }
    public void setCost(int cost) { this.cost = cost; }

    public String getEmoji() { return emoji; }
    public void setEmoji(String emoji) { this.emoji = emoji; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public int getRedeemedCount() { return redeemedCount; }
    public void setRedeemedCount(int redeemedCount) { this.redeemedCount = redeemedCount; }

    public String getBusinessId() { return businessId; }
    public void setBusinessId(String businessId) { this.businessId = businessId; }
}
