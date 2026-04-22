package com.example.agricultureFederation.entity;


import java.time.LocalDate;
import java.util.Objects;

public class Federation {
    private Integer idFederation;
    private String name;
    private String headquarters;
    private String email;
    private String phone;
    private Integer mandateStartYear;
    private Integer mandateEndYear;
    private Member president;  // Référence à l'objet Member complet
    private Integer presidentId; // Alternative : seulement l'ID

    public Federation() {}

    public Federation(Integer idFederation, String name, String headquarters,
                      String email, String phone, Integer mandateStartYear,
                      Integer mandateEndYear, Member president) {
        this.idFederation = idFederation;
        this.name = name;
        this.headquarters = headquarters;
        this.email = email;
        this.phone = phone;
        this.mandateStartYear = mandateStartYear;
        this.mandateEndYear = mandateEndYear;
        this.president = president;
        if (president != null) {
            this.presidentId = president.getIdMember();
        }
    }

    // Getters et Setters
    public Integer getIdFederation() { return idFederation; }
    public void setIdFederation(Integer idFederation) { this.idFederation = idFederation; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getHeadquarters() { return headquarters; }
    public void setHeadquarters(String headquarters) { this.headquarters = headquarters; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public Integer getMandateStartYear() { return mandateStartYear; }
    public void setMandateStartYear(Integer mandateStartYear) { this.mandateStartYear = mandateStartYear; }

    public Integer getMandateEndYear() { return mandateEndYear; }
    public void setMandateEndYear(Integer mandateEndYear) { this.mandateEndYear = mandateEndYear; }

    public Member getPresident() { return president; }
    public void setPresident(Member president) {
        this.president = president;
        if (president != null) {
            this.presidentId = president.getIdMember();
        }
    }

    public Integer getPresidentId() { return presidentId; }
    public void setPresidentId(Integer presidentId) { this.presidentId = presidentId; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Federation that = (Federation) o;
        return Objects.equals(idFederation, that.idFederation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idFederation);
    }

    @Override
    public String toString() {
        return "Federation{" +
                "idFederation=" + idFederation +
                ", name='" + name + '\'' +
                ", headquarters='" + headquarters + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
