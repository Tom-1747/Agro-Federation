package com.example.agricultureFederation.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class AgriculturalSpecialty {
    private Integer idSpecialty;    // maps to id_specialty
    private String name;            // maps to name
    private String sector;          // maps to sector
    private String description;     // Optional: additional field if needed
    private LocalDateTime createdAt; // Optional: for tracking
    private LocalDateTime updatedAt; // Optional: for tracking

    public AgriculturalSpecialty() {}

    public AgriculturalSpecialty(Integer idSpecialty, String name, String sector) {
        this.idSpecialty = idSpecialty;
        this.name = name;
        this.sector = sector;
    }


    public static AgriculturalSpecialty create(String name, String sector) {
        AgriculturalSpecialty specialty = new AgriculturalSpecialty();
        specialty.setName(name);
        specialty.setSector(sector);
        return specialty;
    }

    public Integer getIdSpecialty() {
        return idSpecialty;
    }

    public void setIdSpecialty(Integer idSpecialty) {
        this.idSpecialty = idSpecialty;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
        AgriculturalSpecialty that = (AgriculturalSpecialty) o;
        return Objects.equals(idSpecialty, that.idSpecialty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSpecialty);
    }

    @Override
    public String toString() {
        return "AgriculturalSpecialty{" +
                "idSpecialty=" + idSpecialty +
                ", name='" + name + '\'' +
                ", sector='" + sector + '\'' +
                '}';
    }
}