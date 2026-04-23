package com.example.agricultureFederation.entity;

import com.example.agricultureFederation.entity.enums.PaymentMethodType;

import java.math.BigDecimal;
import java.time.LocalDate;


public class MemberPayment {

    private int paymentId;
    private int membershipFeeId;
    private int accountId;
    private BigDecimal amount;
    private PaymentMethodType paymentMethod;
    private LocalDate paymentDate;
    private BigDecimal federationShare;

    public MemberPayment() {
        this.federationShare = BigDecimal.ZERO;
    }

    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }

    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }

    public int getMembershipFeeId() { return membershipFeeId; }
    public void setMembershipFeeId(int membershipFeeId) { this.membershipFeeId = membershipFeeId; }

    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    /** Convenience setter used by services that receive an Integer from the request DTO. */
    public void setAmount(int amount) { this.amount = BigDecimal.valueOf(amount); }

    public PaymentMethodType getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethodType paymentMethod) { this.paymentMethod = paymentMethod; }

    /** Raw String setter for JDBC ResultSet mapping. */
    public void setPaymentMethodFromString(String value) {
        try { this.paymentMethod = PaymentMethodType.valueOf(value); } catch (Exception ignored) {}
    }

    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }

    public BigDecimal getFederationShare() { return federationShare; }
    public void setFederationShare(BigDecimal federationShare) { this.federationShare = federationShare; }

    @Override
    public String toString() {
        return "MemberPayment{" +
                "paymentId=" + paymentId +
                ", memberId=" + memberId +
                ", membershipFeeId=" + membershipFeeId +
                ", amount=" + amount +
                ", paymentDate=" + paymentDate +
                '}';
    }
}