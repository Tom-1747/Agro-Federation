package com.example.agricultureFederation.entity;

import java.time.LocalDateTime;
import java.util.Objects;

public class AgriculturalTrade {
    private Integer idTrade;     // maps to id_trade
    private String label;        // maps to label
    private String description;  // Optional: additional field if needed
    private String category;     // Optional: to group trades (e.g., "Crop", "Livestock", "Processing")
    private LocalDateTime createdAt;  // Optional: for tracking
    private LocalDateTime updatedAt;  // Optional: for tracking

    public AgriculturalTrade() {}

    public AgriculturalTrade(Integer idTrade, String label) {
        this.idTrade = idTrade;
        this.label = label;
    }

    /**
     * Factory method for creating a new trade
     */
    public static AgriculturalTrade create(String label) {
        AgriculturalTrade trade = new AgriculturalTrade();
        trade.setLabel(label);
        return trade;
    }

    /**
     * Factory method with category
     */
    public static AgriculturalTrade create(String label, String category) {
        AgriculturalTrade trade = new AgriculturalTrade();
        trade.setLabel(label);
        trade.setCategory(category);
        return trade;
    }

    // Getters et Setters
    public Integer getIdTrade() {
        return idTrade;
    }

    public void setIdTrade(Integer idTrade) {
        this.idTrade = idTrade;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgriculturalTrade that = (AgriculturalTrade) o;
        return Objects.equals(idTrade, that.idTrade);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idTrade);
    }

    @Override
    public String toString() {
        return "AgriculturalTrade{" +
                "idTrade=" + idTrade +
                ", label='" + label + '\'' +
                ", category='" + category + '\'' +
                '}';
    }
}