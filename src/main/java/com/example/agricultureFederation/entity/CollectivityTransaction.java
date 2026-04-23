package com.example.agricultureFederation.entity;


import java.time.LocalDate;


public class CollectivityTransaction {

    private int transactionId;
    private int collectiveId;
    private int memberId;
    private int accountId;
    private double amount;
    private String paymentMode;
    private LocalDate creationDate;

    public CollectivityTransaction() {}

    public int getTransactionId() { return transactionId; }
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }

    public int getCollectiveId() { return collectiveId; }
    public void setCollectiveId(int collectiveId) { this.collectiveId = collectiveId; }

    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }

    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getPaymentMode() { return paymentMode; }
    public void setPaymentMode(String paymentMode) { this.paymentMode = paymentMode; }

    public LocalDate getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDate creationDate) { this.creationDate = creationDate; }
}
