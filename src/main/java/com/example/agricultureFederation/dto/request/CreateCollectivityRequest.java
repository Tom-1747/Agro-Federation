package com.example.agricultureFederation.dto.request;

import java.time.LocalDate;

public class CreateCollectivityRequest {

    private int federationId;
    private Integer specialityId;
    private Integer branchId;
    private String name;
    private String location;
    private String phone;
    private LocalDate creationDate;

    public int getFederationId() { return federationId; }
    public void setFederationId(int federationId) { this.federationId = federationId; }

    public Integer getSpecialityId() { return specialityId; }
    public void setSpecialityId(Integer specialityId) { this.specialityId = specialityId; }

    public Integer getBranchId() { return branchId; }
    public void setBranchId(Integer branchId) { this.branchId = branchId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public LocalDate getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDate creationDate) { this.creationDate = creationDate; }
}
