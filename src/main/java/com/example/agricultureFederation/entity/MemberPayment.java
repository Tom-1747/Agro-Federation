package com.example.agricultureFederation.entity;

import java.time.LocalDate;

public class MemberPayment {

    private int paymentId;
    private int memberId;
    private int membershipFeeId;
    private int accountId;
    private int amount;
    private String paymentMode;
    private LocalDate creationDate;

    public MemberPayment() {}

    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }

    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }

    public int getMembershipFeeId() { return membershipFeeId; }
    public void setMembershipFeeId(int membershipFeeId) { this.membershipFeeId = membershipFeeId; }

    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }

    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }

    public String getPaymentMode() { return paymentMode; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }

    public LocalDate getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDate creationDate) { this.creationDate = creationDate; }
}