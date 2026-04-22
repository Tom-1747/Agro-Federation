package com.example.agricultureFederation.dto.response;

import com.example.agricultureFederation.entity.enums.AccountTypeType;
import com.example.agricultureFederation.entity.enums.BankNameType;
import com.example.agricultureFederation.entity.enums.MobileMoneyType;

import java.math.BigDecimal;

public class AccountResponse {
    private String id;
    private AccountTypeType type;
    private BigDecimal amount;

    private String holderName;
    private MobileMoneyType mobileBankingService;
    private Long mobileNumber;

    private BankNameType bankName;
    private Integer bankCode;
    private Integer bankBranchCode;
    private Integer bankAccountNumber;
    private Integer bankAccountKey;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public AccountTypeType getType() { return type; }
    public void setType(AccountTypeType type) { this.type = type; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getHolderName() { return holderName; }
    public void setHolderName(String holderName) { this.holderName = holderName; }

    public MobileMoneyType getMobileBankingService() { return mobileBankingService; }
    public void setMobileBankingService(MobileMoneyType s) { this.mobileBankingService = s; }

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
