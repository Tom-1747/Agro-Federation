package com.example.agricultureFederation.entity;

import com.example.agricultureFederation.entity.enums.AccountTypeType;
import com.example.agricultureFederation.entity.enums.BankNameType;
import com.example.agricultureFederation.entity.enums.MobileMoneyType;

import java.math.BigDecimal;


public class FinancialAccount {

    private int accountId;
    private Integer collectiveId;
    private Integer federationId;
    private AccountTypeType accountType;
    private String accountHolder;
    private BankNameType bankName;
    private MobileMoneyType mobileMoneySevice;
    private String bankAccountNumber;
    private String phoneNumber;
    private BigDecimal balance;

    public FinancialAccount() {
        this.balance = BigDecimal.ZERO;
    }

    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }

    public Integer getCollectiveId() { return collectiveId; }
    public void setCollectiveId(Integer collectiveId) { this.collectiveId = collectiveId; }

    public Integer getFederationId() { return federationId; }
    public void setFederationId(Integer federationId) { this.federationId = federationId; }

    public AccountTypeType getAccountType() { return accountType; }
    public void setAccountType(AccountTypeType accountType) { this.accountType = accountType; }

    public String getAccountHolder() { return accountHolder; }
    public void setAccountHolder(String accountHolder) { this.accountHolder = accountHolder; }

    public BankNameType getBankName() { return bankName; }
    public void setBankName(BankNameType bankName) { this.bankName = bankName; }

    public MobileMoneyType getMobileMoneyService() { return mobileMoneySevice; }
    public void setMobileMoneyService(MobileMoneyType mobileMoneySevice) { this.mobileMoneySevice = mobileMoneySevice; }

    public String getBankAccountNumber() { return bankAccountNumber; }
    public void setBankAccountNumber(String bankAccountNumber) { this.bankAccountNumber = bankAccountNumber; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    @Override
    public String toString() {
        return "FinancialAccount{" +
                "accountId=" + accountId +
                ", accountType=" + accountType +
                ", accountHolder='" + accountHolder + '\'' +
                ", balance=" + balance +
                '}';
    }
}