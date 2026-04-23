package com.example.agricultureFederation.entity;

import com.example.agricultureFederation.entity.enums.PaymentMethodType;

import java.math.BigDecimal;
import java.time.LocalDate;


public class MemberShipFees {

    private Integer idCollection;
    private Integer contributionId;
    private Integer accountId;
    private BigDecimal amount;
    private LocalDate membershipFeesDate;       // renamed column: membershipFees_date
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

    public Integer getIdCollection() { return idCollection; }
    public void setIdCollection(Integer idCollection) { this.idCollection = idCollection; }

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

    public BigDecimal getFederationShare() { return federationShare; }
    public void setFederationShare(BigDecimal federationShare) { this.federationShare = federationShare; }

    @Override
    public String toString() {
        return "MemberShipFees{" +
                "idCollection=" + idCollection +
                ", contributionId=" + contributionId +
                ", accountId=" + accountId +
                ", amount=" + amount +
                ", membershipFeesDate=" + membershipFeesDate +
                ", paymentMethod=" + paymentMethod +
                '}';
    }
}