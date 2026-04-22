package com.example.agricultureFederation.entity;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class AgriculturalSpecialty {
    private Integer idSpecialty;
    private String name;
    private String sector;

    public AgriculturalSpecialty() {}

    public AgriculturalSpecialty(Integer idSpecialty, String name, String sector) {
        this.idSpecialty = idSpecialty;
        this.name = name;
        this.sector = sector;
    }

    // Getters et Setters
    public Integer getIdSpecialty() { return idSpecialty; }
    public void setIdSpecialty(Integer idSpecialty) { this.idSpecialty = idSpecialty; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSector() { return sector; }
    public void setSector(String sector) { this.sector = sector; }

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