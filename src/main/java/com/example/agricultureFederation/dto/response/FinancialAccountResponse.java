package com.example.agricultureFederation.dto.response;

import com.example.agricultureFederation.entity.enums.AccountTypeType;
import com.example.agricultureFederation.entity.enums.BankNameType;
import com.example.agricultureFederation.entity.enums.MobileMoneyType;

import java.math.BigDecimal;


public class FinancialAccountResponse {

    private String id;
    private AccountTypeType accountType;
    private BigDecimal balance;
    private String accountHolder;
    private MobileMoneyType mobileMoneyService;
    private String phoneNumber;
    private BankNameType bankName;
    private String bankAccountNumber;   // RIB composite — VARCHAR(23)
    private Integer collectiveId;
    private Integer federationId;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public AccountTypeType getAccountType() { return accountType; }
    public void setAccountType(AccountTypeType accountType) { this.accountType = accountType; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public String getAccountHolder() { return accountHolder; }
    public void setAccountHolder(String accountHolder) { this.accountHolder = accountHolder; }

    public MobileMoneyType getMobileMoneyService() { return mobileMoneyService; }
    public void setMobileMoneyService(MobileMoneyType mobileMoneyService) { this.mobileMoneyService = mobileMoneyService; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public BankNameType getBankName() { return bankName; }
    public void setBankName(BankNameType bankName) { this.bankName = bankName; }

    public String getBankAccountNumber() { return bankAccountNumber; }
    public void setBankAccountNumber(String bankAccountNumber) { this.bankAccountNumber = bankAccountNumber; }

    public Integer getCollectiveId() { return collectiveId; }
    public void setCollectiveId(Integer collectiveId) { this.collectiveId = collectiveId; }

    public Integer getFederationId() { return federationId; }
    public void setFederationId(Integer federationId) { this.federationId = federationId; }
}