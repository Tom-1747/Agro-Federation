package com.example.agricultureFederation.dto.request;


import com.example.agricultureFederation.entity.enums.PaymentMethodType;

public class CreateMemberPaymentRequest {
    private Integer amount;
    private String membershipFeeIdentifier;
    private String accountCreditedIdentifier;
    private PaymentMethodType paymentMode;

    public Integer getAmount() { return amount; }
    public void setAmount(Integer amount) { this.amount = amount; }

    public String getMembershipFeeIdentifier() { return membershipFeeIdentifier; }
    public void setMembershipFeeIdentifier(String id) { this.membershipFeeIdentifier = id; }

    public String getAccountCreditedIdentifier() { return accountCreditedIdentifier; }
    public void setAccountCreditedIdentifier(String id) { this.accountCreditedIdentifier = id; }

    public PaymentMethodType getPaymentMode() { return paymentMode; }
    public void setPaymentMode(PaymentMethodType paymentMode) { this.paymentMode = paymentMode; }
}

