package com.example.agricultureFederation.entity;

import java.time.LocalDate;

public class Collective {
    private int collectiveId;
    private int federationId;
    private Integer specialityId;
    private Integer branchId;
    private String name;
    private String location;
    private String phone;
    private LocalDate creationDate;
    private Integer presidentId;

    public Collective() {}

    public Collective(int collectiveId, int federationId, Integer specialityId, String name, String location, String phone, LocalDate creationDate) {
        this.collectiveId = collectiveId;
        this.federationId = federationId;
        this.specialityId = specialityId;
        this.branchId = branchId;
        this.name = name;
        this.location = location;
        this.phone = phone;
        this.creationDate = creationDate;
        this.presidentId = presidentId;
    }

    public int getCollectiveId() {
        return collectiveId;
    }

    public void setCollectiveId(int collectiveId) {
        this.collectiveId = collectiveId;
    }

    public int getFederationId() {
        return federationId;
    }

    public void setFederationId(int federationId) {
        this.federationId = federationId;
    }

    public Integer getSpecialityId() {
        return specialityId;
    }

    public void setSpecialityId(Integer specialityId) {
        this.specialityId = specialityId;
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
}