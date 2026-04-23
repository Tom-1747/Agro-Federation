package com.example.agricultureFederation.entity;

import com.example.agricultureFederation.entity.enums.PaymentMethodType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MemberShipFees {

    private String id;
    private LocalDate creationDate;
    private BigDecimal amount;
    private PaymentMethodType paymentMode;  // enum PaymentMethodType existant
    private String accountCreditedId;
    private String memberDebitedId;
    private String collectivityId;

    // Objets peuplés par le repository
    private Account accountCredited;
    private Member memberDebited;

    public MemberShipFees() {}

    public MemberShipFees(String id, LocalDate creationDate, BigDecimal amount,
                      PaymentMethodType paymentMode, String accountCreditedId,
                      String memberDebitedId, String collectivityId) {
        this.id = id;
        this.creationDate = creationDate;
        this.amount = amount;
        this.paymentMode = paymentMode;
        this.accountCreditedId = accountCreditedId;
        this.memberDebitedId = memberDebitedId;
        this.collectivityId = collectivityId;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public LocalDate getCreationDate() { return creationDate; }
    public void setCreationDate(LocalDate creationDate) { this.creationDate = creationDate; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public PaymentMethodType getPaymentMode() { return paymentMode; }
    public void setPaymentMode(PaymentMethodType paymentMode) { this.paymentMode = paymentMode; }

    public String getAccountCreditedId() { return accountCreditedId; }
    public void setAccountCreditedId(String accountCreditedId) { this.accountCreditedId = accountCreditedId; }

    public String getMemberDebitedId() { return memberDebitedId; }
    public void setMemberDebitedId(String memberDebitedId) { this.memberDebitedId = memberDebitedId; }

    public String getCollectivityId() { return collectivityId; }
    public void setCollectivityId(String collectivityId) { this.collectivityId = collectivityId; }

    public Account getAccountCredited() { return accountCredited; }
    public void setAccountCredited(Account accountCredited) { this.accountCredited = accountCredited; }

    public Member getMemberDebited() { return memberDebited; }
    public void setMemberDebited(Member memberDebited) { this.memberDebited = memberDebited; }
}
