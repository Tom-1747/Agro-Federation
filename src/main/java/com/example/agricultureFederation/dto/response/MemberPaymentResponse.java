package com.example.agricultureFederation.dto.response;

import com.example.agricultureFederation.entity.enums.PaymentMethodType;

import java.time.LocalDate;

public class MemberPaymentResponse {
    private String id;
    private Integer amount;
    private PaymentMethodType paymentMode;
    private AccountResponse accountCredited;
    private LocalDate creationDate;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public Integer getAmount() { return amount; }
    public void setAmount(Integer amount) { this.amount = amount; }

    public PaymentMethodType getPaymentMode() { return paymentMode; }
    public void setPaymentMode(PaymentMethodType paymentMode) { this.paymentMode = paymentMode; }

    public AccountResponse getAccountCredited() { return accountCredited; }
    public void setAccountCredited(AccountResponse accountCredited) { this.accountCredited = accountCredited; }

    public LocalDate getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDate creationDate) { this.creationDate = creationDate; }
}
