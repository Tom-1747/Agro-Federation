package com.example.agricultureFederation.entity;

import com.example.agricultureFederation.entity.enums.AccountTypeType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Account {

    private Integer accountId;           // maps to id_account (PK)
    private Integer collectiveId;        // maps to id_collective (FK)
    private AccountTypeType accountType; // maps to account_type (enum: 'Bank', 'Mobile Money', 'Cash')
    private String accountHolder;        // maps to account_holder
    private BigDecimal balance;          // maps to balance
    private LocalDateTime createdAt;     // Optional: for tracking when account was created
    private LocalDateTime updatedAt;     // Optional: for tracking last update

    // Additional fields for Bank accounts (if you add these columns to your schema)
    private String bankName;             // Would need to add to schema
    private Integer bankCode;            // Would need to add to schema
    private Integer bankBranchCode;      // Would need to add to schema
    private String bankAccountNumber;    // Would need to add to schema (should be String, not Integer)
    private Integer bankAccountKey;      // Would need to add to schema

    // Additional fields for Mobile Money accounts (if you add these columns to your schema)
    private String mobileMoneyService;   // Would need to add to schema
    private String mobileNumber;         // Would need to add to schema (should be String, not Long)

    public Account() {}

    /**
     * Factory method for Cash account
     */
    public static Account cashAccount(Integer collectiveId, String accountHolder, BigDecimal balance) {
        Account account = new Account();
        account.setCollectiveId(collectiveId);
        account.setAccountType(AccountTypeType.Cash);
        account.setAccountHolder(accountHolder);
        account.setBalance(balance);
        return account;
    }

    /**
     * Factory method for Mobile Money account
     * Note: Requires additional columns in your schema
     */
    public static Account mobileMoneyAccount(Integer collectiveId, String accountHolder,
                                             String mobileMoneyService, String mobileNumber,
                                             BigDecimal balance) {
        Account account = new Account();
        account.setCollectiveId(collectiveId);
        account.setAccountType(AccountTypeType.Mobile_Money);
        account.setAccountHolder(accountHolder);
        account.setMobileMoneyService(mobileMoneyService);
        account.setMobileNumber(mobileNumber);
        account.setBalance(balance);
        return account;
    }

    /**
     * Factory method for Bank account
     * Note: Requires additional columns in your schema
     */
    public static Account bankAccount(Integer collectiveId, String accountHolder, String bankName,
                                      Integer bankCode, Integer bankBranchCode,
                                      String bankAccountNumber, Integer bankAccountKey,
                                      BigDecimal balance) {
        Account account = new Account();
        account.setCollectiveId(collectiveId);
        account.setAccountType(AccountTypeType.Bank);
        account.setAccountHolder(accountHolder);
        account.setBankName(bankName);
        account.setBankCode(bankCode);
        account.setBankBranchCode(bankBranchCode);
        account.setBankAccountNumber(bankAccountNumber);
        account.setBankAccountKey(bankAccountKey);
        account.setBalance(balance);
        return account;
    }

    // Getters and Setters

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getCollectiveId() {
        return collectiveId;
    }

    public void setCollectiveId(Integer collectiveId) {
        this.collectiveId = collectiveId;
    }

    public AccountTypeType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountTypeType accountType) {
        this.accountType = accountType;
    }

    public String getAccountHolder() {
        return accountHolder;
    }

    public void setAccountHolder(String accountHolder) {
        this.accountHolder = accountHolder;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Bank-specific fields
    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Integer getBankCode() {
        return bankCode;
    }

    public void setBankCode(Integer bankCode) {
        this.bankCode = bankCode;
    }

    public Integer getBankBranchCode() {
        return bankBranchCode;
    }

    public void setBankBranchCode(Integer bankBranchCode) {
        this.bankBranchCode = bankBranchCode;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public Integer getBankAccountKey() {
        return bankAccountKey;
    }

    public void setBankAccountKey(Integer bankAccountKey) {
        this.bankAccountKey = bankAccountKey;
    }

    // Mobile Money-specific fields
    public String getMobileMoneyService() {
        return mobileMoneyService;
    }

    public void setMobileMoneyService(String mobileMoneyService) {
        this.mobileMoneyService = mobileMoneyService;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", collectiveId=" + collectiveId +
                ", accountType=" + accountType +
                ", accountHolder='" + accountHolder + '\'' +
                ", balance=" + balance +
                '}';
    }
}