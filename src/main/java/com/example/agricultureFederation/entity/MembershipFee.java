package com.example.agricultureFederation.entity;

import java.time.LocalDate;


public class MembershipFee {

    private int membershipFeeId;
    private int collectiveId;
    private LocalDate eligibleFrom;
    private String frequency;
    private double amount;
    private String label;
    private String status;

    public MembershipFee() {}

    public int getMembershipFeeId() { return membershipFeeId; }
    public void setMembershipFeeId(int membershipFeeId) { this.membershipFeeId = membershipFeeId; }

    public int getCollectiveId() { return collectiveId; }
    public void setCollectiveId(int collectiveId) { this.collectiveId = collectiveId; }

    public LocalDate getEligibleFrom() { return eligibleFrom; }
    public void setEligibleFrom(LocalDate eligibleFrom) { this.eligibleFrom = eligibleFrom; }

    public String getFrequency() { return frequency; }
    public void setFrequency(String frequency) { this.frequency = frequency; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}