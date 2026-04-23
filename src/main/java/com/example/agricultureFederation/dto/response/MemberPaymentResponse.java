package com.example.agricultureFederation.dto.response;

import com.example.agricultureFederation.entity.enums.PaymentMethodType;

import java.math.BigDecimal;
import java.time.LocalDate;


public class MemberPaymentResponse {

    private String id;
    private BigDecimal amount;                  // was Integer — corrected
    private PaymentMethodType paymentMethod;    // was paymentMode String — corrected
    private LocalDate paymentDate;              // was creationDate — aligned with entity
    private BigDecimal federationShare;
    private FinancialAccountResponse accountCredited;
    private String membershipFeeId;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public PaymentMethodType getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethodType paymentMethod) { this.paymentMethod = paymentMethod; }

    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }

    public BigDecimal getFederationShare() { return federationShare; }
    public void setFederationShare(BigDecimal federationShare) { this.federationShare = federationShare; }

    public FinancialAccountResponse getAccountCredited() { return accountCredited; }
    public void setAccountCredited(FinancialAccountResponse accountCredited) { this.accountCredited = accountCredited; }

    public String getMembershipFeeId() { return membershipFeeId; }
    public void setMembershipFeeId(String membershipFeeId) { this.membershipFeeId = membershipFeeId; }
}