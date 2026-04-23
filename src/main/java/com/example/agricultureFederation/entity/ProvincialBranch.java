package com.example.agricultureFederation.entity;

import java.util.Objects;

public class ProvincialBranch {
    private Integer idBranch;
    private String province;
    private String capitalCity;
    private String address;
    private String phone;
    private String email;

    public ProvincialBranch() {}

    public ProvincialBranch(Integer idBranch, String province, String capitalCity, String address) {
        this.idBranch = idBranch;
        this.province = province;
        this.capitalCity = capitalCity;
        this.address = address;
    }

    public ProvincialBranch(String province, String capitalCity, String address) {
        this.province = province;
        this.capitalCity = capitalCity;
        this.address = address;
    }

    public static ProvincialBranch create(String province, String capitalCity, String address) {
        return new ProvincialBranch(province, capitalCity, address);
    }

    public Integer getIdBranch() { return idBranch; }
    public void setIdBranch(Integer idBranch) { this.idBranch = idBranch; }

    public String getProvince() { return province; }
    public void setProvince(String province) { this.province = province; }

    public String getCapitalCity() { return capitalCity; }
    public void setCapitalCity(String capitalCity) { this.capitalCity = capitalCity; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProvincialBranch that = (ProvincialBranch) o;
        return Objects.equals(idBranch, that.idBranch);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idBranch);
    }

    @Override
    public String toString() {
        return "ProvincialBranch{" +
                "idBranch=" + idBranch +
                ", province='" + province + '\'' +
                ", capitalCity='" + capitalCity + '\'' +
                '}';
    }
}