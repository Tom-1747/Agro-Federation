package com.example.agricultureFederation.entity;

import com.example.agricultureFederation.entity.enums.PaymentMethodType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CollectivityTransaction {

    private int transactionId;
    private int collectiveId;
    private int memberId;
    private int accountId;
    private BigDecimal amount;
    private PaymentMethodType paymentMethod;
    private LocalDate creationDate;

    public CollectivityTransaction() {}

    public CollectivityTransaction(int collectiveId, int memberId, int accountId,
                                   BigDecimal amount, PaymentMethodType paymentMethod,
                                   LocalDate creationDate) {
        this.collectiveId = collectiveId;
        this.memberId = memberId;
        this.accountId = accountId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.creationDate = creationDate;
    }

    public int getTransactionId() { return transactionId; }
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }

    public int getCollectiveId() { return collectiveId; }
    public void setCollectiveId(int collectiveId) { this.collectiveId = collectiveId; }

    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }

    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public PaymentMethodType getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethodType paymentMethod) { this.paymentMethod = paymentMethod; }

    /** Raw String setter for JDBC ResultSet mapping. */
    public void setPaymentMethodFromString(String value) {
        try { this.paymentMethod = PaymentMethodType.valueOf(value); } catch (Exception ignored) {}
    }

    public LocalDate getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDate creationDate) { this.creationDate = creationDate; }

    @Override
    public String toString() {
        return "CollectivityTransaction{" +
                "transactionId=" + transactionId +
                ", collectiveId=" + collectiveId +
                ", memberId=" + memberId +
                ", amount=" + amount +
                ", creationDate=" + creationDate +
                '}';
    }

    public void setContributionId(int idContribution) {    }
}