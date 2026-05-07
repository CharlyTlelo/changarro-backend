package com.changarro.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "businesses")
public class Business {

    @Id
    private String id;

    private String name;
    private String categoryId;       // references Category.id
    private String subcategoryId;    // references Category.subcategories.id
    private String description;
    private String address;
    private String neighborhood;     // e.g. "Roma Norte"
    private String phone;
    private String instagram;
    private String paymentMethod;    // e.g. "Solo efectivo"

    private double rating = 0.0;
    private int reviewCount = 0;
    private int visitCount = 0;

    private String emoji;
    private String color;            // card color hex
    private List<String> tags = new ArrayList<>();
    private String tag;              // short subtitle

    private String priceRange;       // e.g. "$60-120"
    private String schedule;         // e.g. "Lun-Dom 12:00-23:00"

    private Promo activePromo;
    private List<MenuItem> menuItems = new ArrayList<>();

    private boolean trending = false;
    private boolean nuevo = false;

    private String ownerId;          // user who registered the business

    // Embedded promo
    public static class Promo {
        private String title;        // e.g. "2x1 al pastor"
        private String label;        // e.g. "2x1"
        private String validUntil;   // e.g. "HOY HASTA 11PM"
        private String note;         // e.g. "Menciona Changarro al pedir"
        private int bonusCoins;      // coins awarded for redeeming

        public Promo() {}
        public Promo(String title, String label, String validUntil, String note, int bonusCoins) {
            this.title = title; this.label = label; this.validUntil = validUntil;
            this.note = note; this.bonusCoins = bonusCoins;
        }

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getLabel() { return label; }
        public void setLabel(String label) { this.label = label; }
        public String getValidUntil() { return validUntil; }
        public void setValidUntil(String validUntil) { this.validUntil = validUntil; }
        public String getNote() { return note; }
        public void setNote(String note) { this.note = note; }
        public int getBonusCoins() { return bonusCoins; }
        public void setBonusCoins(int bonusCoins) { this.bonusCoins = bonusCoins; }
    }

    // Embedded menu item
    public static class MenuItem {
        private String name;
        private String emoji;
        private String price;
        private int orderCount;

        public MenuItem() {}
        public MenuItem(String name, String emoji, String price, int orderCount) {
            this.name = name; this.emoji = emoji; this.price = price; this.orderCount = orderCount;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmoji() { return emoji; }
        public void setEmoji(String emoji) { this.emoji = emoji; }
        public String getPrice() { return price; }
        public void setPrice(String price) { this.price = price; }
        public int getOrderCount() { return orderCount; }
        public void setOrderCount(int orderCount) { this.orderCount = orderCount; }
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

    public String getSubcategoryId() { return subcategoryId; }
    public void setSubcategoryId(String subcategoryId) { this.subcategoryId = subcategoryId; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getNeighborhood() { return neighborhood; }
    public void setNeighborhood(String neighborhood) { this.neighborhood = neighborhood; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getInstagram() { return instagram; }
    public void setInstagram(String instagram) { this.instagram = instagram; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public int getReviewCount() { return reviewCount; }
    public void setReviewCount(int reviewCount) { this.reviewCount = reviewCount; }

    public int getVisitCount() { return visitCount; }
    public void setVisitCount(int visitCount) { this.visitCount = visitCount; }

    public String getEmoji() { return emoji; }
    public void setEmoji(String emoji) { this.emoji = emoji; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }

    public String getTag() { return tag; }
    public void setTag(String tag) { this.tag = tag; }

    public String getPriceRange() { return priceRange; }
    public void setPriceRange(String priceRange) { this.priceRange = priceRange; }

    public String getSchedule() { return schedule; }
    public void setSchedule(String schedule) { this.schedule = schedule; }

    public Promo getActivePromo() { return activePromo; }
    public void setActivePromo(Promo activePromo) { this.activePromo = activePromo; }

    public List<MenuItem> getMenuItems() { return menuItems; }
    public void setMenuItems(List<MenuItem> menuItems) { this.menuItems = menuItems; }

    public boolean isTrending() { return trending; }
    public void setTrending(boolean trending) { this.trending = trending; }

    public boolean isNuevo() { return nuevo; }
    public void setNuevo(boolean nuevo) { this.nuevo = nuevo; }

    public String getOwnerId() { return ownerId; }
    public void setOwnerId(String ownerId) { this.ownerId = ownerId; }
}
