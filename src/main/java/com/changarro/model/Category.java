package com.changarro.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "categories")
public class Category {

    @Id
    private String id;

    private String label;
    private String emoji;
    private String color;
    private String bg;
    private int businessCount;

    public Category() {}

    public Category(String id, String label, String emoji, String color, String bg) {
        this.id = id;
        this.label = label;
        this.emoji = emoji;
        this.color = color;
        this.bg = bg;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public String getEmoji() { return emoji; }
    public void setEmoji(String emoji) { this.emoji = emoji; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getBg() { return bg; }
    public void setBg(String bg) { this.bg = bg; }

    public int getBusinessCount() { return businessCount; }
    public void setBusinessCount(int businessCount) { this.businessCount = businessCount; }
}
