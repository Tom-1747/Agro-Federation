package com.example.agricultureFederation.service;

import com.example.agricultureFederation.dto.response.FinancialAccountResponse;
import com.example.agricultureFederation.entity.FinancialAccount;
import com.example.agricultureFederation.repository.CollectiveRepository;
import com.example.agricultureFederation.repository.FinancialAccountRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FinancialAccountService {

    private final FinancialAccountRepository financialAccountRepository;
    private final CollectiveRepository collectiveRepository;

    public FinancialAccountService(FinancialAccountRepository financialAccountRepository,
                                   CollectiveRepository collectiveRepository) {
        this.financialAccountRepository = financialAccountRepository;
        this.collectiveRepository = collectiveRepository;
    }

    public List<FinancialAccountResponse> getAccountsAt(String collectiveId,
                                                        LocalDate at) throws SQLException {
        int id = Integer.parseInt(collectiveId);

        if (collectiveRepository.findById(id) == null) {
            throw new IllegalArgumentException("Collectivity not found: " + collectiveId);
        }

        List<FinancialAccount> accounts = financialAccountRepository.findByCollectiveId(id);
        List<FinancialAccountResponse> responses = new ArrayList<>();

        for (FinancialAccount account : accounts) {
            double balanceAt = financialAccountRepository.getBalanceAt(account.getAccountId(), at);
            account.setAmount(balanceAt);
            responses.add(toResponse(account));
        }

        return responses;
    }

    private FinancialAccountResponse toResponse(FinancialAccount account) {
        FinancialAccountResponse r = new FinancialAccountResponse();
        r.setId(String.valueOf(account.getAccountId()));
        r.setType(account.getType());
        r.setHolderName(account.getHolderName());
        r.setBankName(account.getBankName());
        r.setBankCode(account.getBankCode());
        r.setBankBranchCode(account.getBankBranchCode());
        r.setBankAccountNumber(account.getBankAccountNumber());
        r.setBankAccountKey(account.getBankAccountKey());
        r.setMobileBankingService(account.getMobileBankingService());
        r.setMobileNumber(account.getMobileNumber());
        r.setAmount(account.getAmount());
        return r;
    }
}