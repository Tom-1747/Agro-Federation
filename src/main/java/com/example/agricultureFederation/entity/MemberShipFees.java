package com.example.agricultureFederation.entity;

import com.example.agricultureFederation.entity.enums.PaymentMethodType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MemberShipFees {

    private Integer idMembershipFees;
    private Integer contributionId;
    private Integer accountId;
    private BigDecimal amount;
    private LocalDate membershipFeesDate;
    private PaymentMethodType paymentMethod;
    private BigDecimal federationShare;

    public MemberShipFees() {
        this.federationShare = BigDecimal.ZERO;
    }

    public MemberShipFees(Integer contributionId, Integer accountId, BigDecimal amount,
                          LocalDate membershipFeesDate, PaymentMethodType paymentMethod,
                          BigDecimal federationShare) {
        this.contributionId = contributionId;
        this.accountId = accountId;
        this.amount = amount;
        this.membershipFeesDate = membershipFeesDate;
        this.paymentMethod = paymentMethod;
        this.federationShare = federationShare != null ? federationShare : BigDecimal.ZERO;
    }

    public MemberShipFees(Integer contributionId, Integer accountId, BigDecimal amount,
                          LocalDate membershipFeesDate, PaymentMethodType paymentMethod) {
        this(contributionId, accountId, amount, membershipFeesDate, paymentMethod, BigDecimal.ZERO);
    }

    public Integer getIdMembershipFees() { return idMembershipFees; }
    public void setIdMembershipFees(Integer idMembershipFees) { this.idMembershipFees = idMembershipFees; }

    public Integer getContributionId() { return contributionId; }
    public void setContributionId(Integer contributionId) { this.contributionId = contributionId; }

    public Integer getAccountId() { return accountId; }
    public void setAccountId(Integer accountId) { this.accountId = accountId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public LocalDate getMembershipFeesDate() { return membershipFeesDate; }
    public void setMembershipFeesDate(LocalDate membershipFeesDate) { this.membershipFeesDate = membershipFeesDate; }

    public PaymentMethodType getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethodType paymentMethod) { this.paymentMethod = paymentMethod; }

    public void setPaymentMethodFromString(String value) {
        try { this.paymentMethod = PaymentMethodType.valueOf(value); } catch (Exception ignored) {}
    }

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
        return "MemberShipFees{" +
                "idMembershipFees=" + idMembershipFees +
                ", contributionId=" + contributionId +
                ", accountId=" + accountId +
                ", amount=" + amount +
                ", membershipFeesDate=" + membershipFeesDate +
                ", paymentMethod=" + paymentMethod +
                '}';
    }
}