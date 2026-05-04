package com.changarro.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "stamps")
public class Stamp {

    @Id
    private String id;

    private String label;
    private String emoji;
    private String color;
    private String requirement;  // how to earn it

    public Stamp() {}

    public Stamp(String id, String label, String emoji, String color, String requirement) {
        this.id = id;
        this.label = label;
        this.emoji = emoji;
        this.color = color;
        this.requirement = requirement;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public String getEmoji() { return emoji; }
    public void setEmoji(String emoji) { this.emoji = emoji; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public String getRequirement() { return requirement; }
    public void setRequirement(String requirement) { this.requirement = requirement; }
}
