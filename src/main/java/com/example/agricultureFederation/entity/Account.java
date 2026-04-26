package com.example.agricultureFederation.entity;

import com.example.agricultureFederation.entity.enums.AccountTypeType;
import com.example.agricultureFederation.entity.enums.BankNameType;
import com.example.agricultureFederation.entity.enums.MobileMoneyType;

import java.math.BigDecimal;

public class Account {

    private String id;
    private AccountTypeType accountType;
    private BigDecimal amount;
    private String holderName;
    private MobileMoneyType mobileBankingService;
    private Long mobileNumber;
    private BankNameType bankName;
    private Integer bankCode;
    private Integer bankBranchCode;
    private Integer bankAccountNumber;
    private Integer bankAccountKey;

    public Account() {}

    public static Account cash(String id, BigDecimal amount) {
        Account a = new Account();
        a.id = id;
        a.accountType = AccountTypeType.Cash;
        a.amount = amount;
        return a;
    }

    public static Account mobileBanking(String id, String holderName,
                                        MobileMoneyType service, Long mobileNumber, BigDecimal amount) {
        Account a = new Account();
        a.id = id;
        a.accountType = AccountTypeType.Mobile_Money;
        a.holderName = holderName;
        a.mobileBankingService = service;
        a.mobileNumber = mobileNumber;
        a.amount = amount;
        return a;
    }


    public static Account bank(String id, String holderName, BankNameType bankName,
                               Integer bankCode, Integer bankBranchCode,
                               Integer bankAccountNumber, Integer bankAccountKey, BigDecimal amount) {
        Account a = new Account();
        a.id = id;
        a.accountType = AccountTypeType.Bank;
        a.holderName = holderName;
        a.bankName = bankName;
        a.bankCode = bankCode;
        a.bankBranchCode = bankBranchCode;
        a.bankAccountNumber = bankAccountNumber;
        a.bankAccountKey = bankAccountKey;
        a.amount = amount;
        return a;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public AccountTypeType getAccountType() { return accountType; }
    public void setAccountType(AccountTypeType accountType) { this.accountType = accountType; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getHolderName() { return holderName; }
    public void setHolderName(String holderName) { this.holderName = holderName; }

    public MobileMoneyType getMobileBankingService() { return mobileBankingService; }
    public void setMobileBankingService(MobileMoneyType mobileBankingService) { this.mobileBankingService = mobileBankingService; }

    public Long getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(Long mobileNumber) { this.mobileNumber = mobileNumber; }

    public BankNameType getBankName() { return bankName; }
    public void setBankName(BankNameType bankName) { this.bankName = bankName; }

    public Integer getBankCode() { return bankCode; }
    public void setBankCode(Integer bankCode) { this.bankCode = bankCode; }

    public Integer getBankBranchCode() { return bankBranchCode; }
    public void setBankBranchCode(Integer bankBranchCode) { this.bankBranchCode = bankBranchCode; }

    public Integer getBankAccountNumber() { return bankAccountNumber; }
    public void setBankAccountNumber(Integer bankAccountNumber) { this.bankAccountNumber = bankAccountNumber; }

    public Integer getBankAccountKey() { return bankAccountKey; }
    public void setBankAccountKey(Integer bankAccountKey) { this.bankAccountKey = bankAccountKey; }
}
