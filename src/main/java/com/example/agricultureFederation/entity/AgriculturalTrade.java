package com.example.agricultureFederation.entity;


import java.util.Objects;

public class AgriculturalTrade {
    private Integer idTrade;
    private String label;

    public AgriculturalTrade() {}

    public AgriculturalTrade(Integer idTrade, String label) {
        this.idTrade = idTrade;
        this.label = label;
    }

    // Getters et Setters
    public Integer getIdTrade() { return idTrade; }
    public void setIdTrade(Integer idTrade) { this.idTrade = idTrade; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

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
                '}';
    }
}