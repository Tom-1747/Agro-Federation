package com.example.agricultureFederation.entity;

public class FinancialAccount {

    private int accountId;
    private int collectiveId;
    private String type;
    private String holderName;
    private String bankName;
    private String mobileBankingService;
    private String mobileNumber;
    private double amount;
    private Integer bankCode;
    private Integer bankBranchCode;
    private Integer bankAccountNumber;
    private Integer bankAccountKey;

    public FinancialAccount() {}

    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }

    public int getCollectiveId() { return collectiveId; }
    public void setCollectiveId(int collectiveId) { this.collectiveId = collectiveId; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getHolderName() { return holderName; }
    public void setHolderName(String holderName) { this.holderName = holderName; }

    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }

    public String getMobileBankingService() { return mobileBankingService; }
    public void setMobileBankingService(String mobileBankingService) {
        this.mobileBankingService = mobileBankingService;
    }

    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public Integer getBankCode() { return bankCode; }
    public void setBankCode(Integer bankCode) { this.bankCode = bankCode; }

    public Integer getBankBranchCode() { return bankBranchCode; }
    public void setBankBranchCode(Integer bankBranchCode) { this.bankBranchCode = bankBranchCode; }

    public Integer getBankAccountNumber() { return bankAccountNumber; }
    public void setBankAccountNumber(Integer bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public Integer getBankAccountKey() { return bankAccountKey; }
    public void setBankAccountKey(Integer bankAccountKey) { this.bankAccountKey = bankAccountKey; }
}
