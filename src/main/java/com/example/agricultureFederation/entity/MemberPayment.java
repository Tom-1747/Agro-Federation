package com.example.agricultureFederation.entity;

import com.example.agricultureFederation.entity.enums.PaymentMethodType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MemberPayment {

    private int paymentId;
    private int contributionId;
    private int accountId;
    private int memberId;
    private int collectiveId;
    private BigDecimal amount;
    private PaymentMethodType paymentMethod;
    private LocalDate paymentDate;
    private BigDecimal federationShare;

    public MemberPayment() {
        this.federationShare = BigDecimal.ZERO;
    }

    public MemberPayment(int contributionId, int accountId, BigDecimal amount,
                         PaymentMethodType paymentMethod, LocalDate paymentDate) {
        this.contributionId = contributionId;
        this.accountId = accountId;
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
        this.federationShare = BigDecimal.ZERO;
    }

    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }

    public int getContributionId() { return contributionId; }
    public void setContributionId(int contributionId) { this.contributionId = contributionId; }

    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }

    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }

    public int getCollectiveId() { return collectiveId; }
    public void setCollectiveId(int collectiveId) { this.collectiveId = collectiveId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public void setAmount(int amount) { this.amount = BigDecimal.valueOf(amount); }

    public PaymentMethodType getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethodType paymentMethod) { this.paymentMethod = paymentMethod; }

    public void setPaymentMethodFromString(String value) {
        try { this.paymentMethod = PaymentMethodType.valueOf(value); } catch (Exception ignored) {}
    }

    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }

    public BigDecimal getFederationShare() { return federationShare; }
    public void setFederationShare(BigDecimal federationShare) { this.federationShare = federationShare; }

    public BigDecimal getCollectiveShare() {
        if (amount != null && federationShare != null) {
            return amount.subtract(federationShare);
        }
        return amount;
    }

    @Override
    public String toString() {
        return "MemberPayment{" +
                "paymentId=" + paymentId +
                ", contributionId=" + contributionId +
                ", memberId=" + memberId +
                ", amount=" + amount +
                ", paymentDate=" + paymentDate +
                '}';
    }
}