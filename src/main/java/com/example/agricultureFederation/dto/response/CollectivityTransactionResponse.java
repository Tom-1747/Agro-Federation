package com.example.agricultureFederation.dto.response;

import com.example.agricultureFederation.entity.enums.PaymentMethodType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class CollectivityTransactionResponse {
    private String id;
    private LocalDate creationDate;
    private BigDecimal amount;
    private PaymentMethodType paymentMode;
    private AccountResponse accountCredited;
    private MemberResponse memberDebited;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public LocalDate getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDate creationDate) { this.creationDate = creationDate; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public PaymentMethodType getPaymentMode() { return paymentMode; }
    public void setPaymentMode(PaymentMethodType paymentMode) { this.paymentMode = paymentMode; }

    public AccountResponse getAccountCredited() { return accountCredited; }
    public void setAccountCredited(AccountResponse accountCredited) { this.accountCredited = accountCredited; }

    public MemberResponse getMemberDebited() { return memberDebited; }
    public void setMemberDebited(MemberResponse memberDebited) { this.memberDebited = memberDebited; }
}
