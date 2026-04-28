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

    // Alias methods for backward compatibility
    public void setType(String type) {
        try { this.accountType = com.example.agricultureFederation.entity.enums.AccountTypeType.valueOf(type); } catch (Exception ignored) {}
    }
    public String getType() { return accountType != null ? accountType.name() : null; }

    public void setHolderName(String holderName) { this.accountHolder = holderName; }
    public String getHolderName() { return accountHolder; }

    public void setAmount(java.math.BigDecimal amount) { this.balance = amount; }
    public java.math.BigDecimal getAmount() { return balance; }

    public void setBankCode(Integer bankCode) { /* stored in bankAccountNumber */ }
    public Integer getBankCode() { return null; }

    public void setBankBranchCode(Integer code) { }
    public Integer getBankBranchCode() { return null; }

    public void setBankAccountKey(Integer key) { }
    public Integer getBankAccountKey() { return null; }

    public void setMobileBankingService(String service) {
        try { this.mobileMoneyService = com.example.agricultureFederation.entity.enums.MobileMoneyType.valueOf(service); } catch (Exception ignored) {}
    }
    public String getMobileBankingService() { return mobileMoneyService != null ? mobileMoneyService.name() : null; }

    public void setMobileNumber(String number) { this.phoneNumber = number; }
    public String getMobileNumber() { return phoneNumber; }

}