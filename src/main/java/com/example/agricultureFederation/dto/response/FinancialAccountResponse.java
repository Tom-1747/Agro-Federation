package com.example.agricultureFederation.dto.response;


public class FinancialAccountResponse {

    private String id;
    private String type;
    private Double amount;
    private String holderName;
    private String mobileBankingService;
    private String mobileNumber;
    private String bankName;
    private Integer bankCode;
    private Integer bankBranchCode;
    private Integer bankAccountNumber;
    private Integer bankAccountKey;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getHolderName() { return holderName; }
    public void setHolderName(String holderName) { this.holderName = holderName; }

    public String getMobileBankingService() { return mobileBankingService; }
    public void setMobileBankingService(String mobileBankingService) {
        this.mobileBankingService = mobileBankingService;
    }

    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }

    public String getBankName() { return bankName; }
    public void setBankName(String bankName) { this.bankName = bankName; }

    public Integer getBankCode() { return bankCode; }
    public void setBankCode(Integer bankCode) { this.bankCode = bankCode; }

    public Integer getBankBranchCode() { return bankBranchCode; }
    public void setBankBranchCode(Integer bankBranchCode) { this.bankBranchCode = bankBranchCode; }

    public Integer getBankAccountNumber() { return bankAccountNumber; }
    public void setBankAccountNumber(Integer bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public Integer getBankAccountKey() {
        return bankAccountKey;
    }
    public void setBankAccountKey(Integer bankAccountKey) {
        this.bankAccountKey = bankAccountKey;
    }
}