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
    private MobileMoneyType mobileMoneyService;
    private String bankAccountNumber;
    private String phoneNumber;
    private BigDecimal balance;

    public FinancialAccount() {
        this.balance = BigDecimal.ZERO;
    }

    public FinancialAccount(int collectiveId, AccountTypeType accountType, String accountHolder, BigDecimal balance) {
        this.collectiveId = collectiveId;
        this.accountType = accountType;
        this.accountHolder = accountHolder;
        this.balance = balance != null ? balance : BigDecimal.ZERO;
    }

    public static FinancialAccount cashAccount(Integer collectiveId, String accountHolder, BigDecimal balance) {
        FinancialAccount account = new FinancialAccount();
        account.setCollectiveId(collectiveId);
        account.setAccountType(AccountTypeType.Cash);
        account.setAccountHolder(accountHolder);
        account.setBalance(balance);
        return account;
    }

    public static FinancialAccount bankAccount(Integer collectiveId, String accountHolder,
                                               BankNameType bankName, String bankAccountNumber,
                                               BigDecimal balance) {
        FinancialAccount account = new FinancialAccount();
        account.setCollectiveId(collectiveId);
        account.setAccountType(AccountTypeType.Bank);
        account.setAccountHolder(accountHolder);
        account.setBankName(bankName);
        account.setBankAccountNumber(bankAccountNumber);
        account.setBalance(balance);
        return account;
    }

    public static FinancialAccount mobileMoneyAccount(Integer collectiveId, String accountHolder,
                                                      MobileMoneyType mobileMoneyService, String phoneNumber,
                                                      BigDecimal balance) {
        FinancialAccount account = new FinancialAccount();
        account.setCollectiveId(collectiveId);
        account.setAccountType(AccountTypeType.Mobile_Money);
        account.setAccountHolder(accountHolder);
        account.setMobileMoneyService(mobileMoneyService);
        account.setPhoneNumber(phoneNumber);
        account.setBalance(balance);
        return account;
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

    public MobileMoneyType getMobileMoneyService() { return mobileMoneyService; }
    public void setMobileMoneyService(MobileMoneyType mobileMoneyService) { this.mobileMoneyService = mobileMoneyService; }

    public String getBankAccountNumber() { return bankAccountNumber; }
    public void setBankAccountNumber(String bankAccountNumber) { this.bankAccountNumber = bankAccountNumber; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }

    public void addToBalance(BigDecimal amount) {
        if (amount != null && balance != null) {
            this.balance = this.balance.add(amount);
        }
    }

    public void subtractFromBalance(BigDecimal amount) {
        if (amount != null && balance != null) {
            this.balance = this.balance.subtract(amount);
        }
    }

    public boolean isCashAccount() {
        return accountType == AccountTypeType.Cash;
    }

    public boolean isBankAccount() {
        return accountType == AccountTypeType.Bank;
    }

    public boolean isMobileMoneyAccount() {
        return accountType == AccountTypeType.Mobile_Money;
    }

    @Override
    public String toString() {
        return "FinancialAccount{" +
                "accountId=" + accountId +
                ", accountType=" + accountType +
                ", accountHolder='" + accountHolder + '\'' +
                ", balance=" + balance +
                '}';
    }

    // Alias methods for backward compatibility with services/repositories
    public String getType() {
        return accountType != null ? accountType.name() : null;
    }
    public void setType(String type) {
        try { this.accountType = com.example.agricultureFederation.entity.enums.AccountTypeType.valueOf(type); } catch (Exception ignored) {}
    }

    public String getHolderName() { return accountHolder; }
    public void setHolderName(String holderName) { this.accountHolder = holderName; }

    public java.math.BigDecimal getAmount() { return balance; }
    public void setAmount(java.math.BigDecimal amount) { this.balance = amount; }
    public void setAmount(double amount) { this.balance = java.math.BigDecimal.valueOf(amount); }

    public String getMobileBankingService() {
        return mobileMoneyService != null ? mobileMoneyService.name() : null;
    }
    public String getMobileNumber() { return phoneNumber; }

    public Integer getBankCode() { return null; }
    public Integer getBankBranchCode() { return null; }
    public Integer getBankAccountKey() { return null; }

}