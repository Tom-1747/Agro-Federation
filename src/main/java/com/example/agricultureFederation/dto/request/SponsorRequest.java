package com.example.agricultureFederation.dto.request;

public class SponsorRequest {

    private int memberId;
    private String relationship;

    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }

    public String getRelationship() { return relationship; }
    public void setRelationship(String relationship) { this.relationship = relationship; }
}
