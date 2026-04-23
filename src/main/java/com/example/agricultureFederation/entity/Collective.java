package com.example.agricultureFederation.entity;

import java.time.LocalDate;
import java.util.Objects;

public class Collective {
    private Integer collectiveId;
    private Integer federationId;
    private Integer specialtyId;
    private Integer branchId;
    private String name;
    private String location;
    private String phone;
    private LocalDate creationDate;
    private Integer presidentId;

    public Collective() {}

    public Collective(Integer federationId, Integer specialtyId, Integer branchId,
                      String name, String location, String phone, LocalDate creationDate) {
        this.federationId = federationId;
        this.specialtyId = specialtyId;
        this.branchId = branchId;
        this.name = name;
        this.location = location;
        this.phone = phone;
        this.creationDate = creationDate;
    }

    public Collective(Integer collectiveId, Integer federationId, Integer specialtyId,
                      Integer branchId, String name, String location, String phone, LocalDate creationDate) {
        this.collectiveId = collectiveId;
        this.federationId = federationId;
        this.specialtyId = specialtyId;
        this.branchId = branchId;
        this.name = name;
        this.location = location;
        this.phone = phone;
        this.creationDate = creationDate;
    }

    public static Collective create(Integer federationId, Integer specialtyId, Integer branchId,
                                    String name, String location, String phone) {
        return new Collective(federationId, specialtyId, branchId, name, location, phone, LocalDate.now());
    }

    public Integer getCollectiveId() {
        return collectiveId;
    }

    public void setCollectiveId(Integer collectiveId) {
        this.collectiveId = collectiveId;
    }

    public Integer getFederationId() {
        return federationId;
    }

    public void setFederationId(Integer federationId) {
        this.federationId = federationId;
    }

    public Integer getSpecialtyId() {
        return specialtyId;
    }

    public void setSpecialtyId(Integer specialtyId) {
        this.specialtyId = specialtyId;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getPresidentId() {
        return presidentId;
    }

    public void setPresidentId(Integer presidentId) {
        this.presidentId = presidentId;
    }

    public boolean hasPresident() {
        return presidentId != null && presidentId > 0;
    }

    public boolean isNew() {
        return collectiveId == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Collective that = (Collective) o;
        return Objects.equals(collectiveId, that.collectiveId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(collectiveId);
    }

    @Override
    public String toString() {
        return "Collective{" +
                "collectiveId=" + collectiveId +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }
}