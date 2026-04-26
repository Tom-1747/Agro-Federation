package com.example.agricultureFederation.service;

import com.example.agricultureFederation.dto.response.AccountResponse;
import com.example.agricultureFederation.dto.response.CollectivityTransactionResponse;
import com.example.agricultureFederation.dto.response.MemberResponse;
import com.example.agricultureFederation.entity.FinancialAccount;
import com.example.agricultureFederation.entity.Member;
import com.example.agricultureFederation.entity.enums.AccountTypeType;
import com.example.agricultureFederation.entity.enums.PaymentMethodType;
import com.example.agricultureFederation.repository.CollectiveRepository;
import com.example.agricultureFederation.repository.CollectivityTransactionRepository;
import com.example.agricultureFederation.repository.MemberRepository;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CollectivityTransactionService {

    private final CollectivityTransactionRepository transactionRepository;
    private final CollectiveRepository collectiveRepository;
    private final MemberRepository memberRepository;
    private final FinancialAccountRepository financialAccountRepository;

    public CollectivityTransactionService(CollectivityTransactionRepository transactionRepository,
                                          CollectiveRepository collectiveRepository,
                                          MemberRepository memberRepository,
                                          FinancialAccountRepository financialAccountRepository) {
        this.transactionRepository = transactionRepository;
        this.collectiveRepository = collectiveRepository;
        this.memberRepository = memberRepository;
        this.financialAccountRepository = financialAccountRepository;
    }

    public List<CollectivityTransactionResponse> getTransactions(
            String collectiveId, LocalDate from, LocalDate to) throws SQLException {

        int id = Integer.parseInt(collectiveId);
        if (collectiveRepository.findById(id) == null) {
            throw new IllegalArgumentException("Collectivity not found: " + collectiveId);
        }
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("'from' date must be before 'to' date.");
        }

        List<CollectivityTransaction> transactions =
                transactionRepository.findByCollectiveIdAndPeriod(id, from, to);

        List<CollectivityTransactionResponse> responses = new ArrayList<>();
        for (CollectivityTransaction t : transactions) {
            Member member = memberRepository.findById(t.getMemberId());
            FinancialAccount account = financialAccountRepository.findById(t.getAccountId());
            responses.add(toResponse(t, member, account));
        }
        return responses;
    }

    private CollectivityTransactionResponse toResponse(CollectivityTransaction t,
                                                       Member member,
                                                       FinancialAccount account) {
        CollectivityTransactionResponse r = new CollectivityTransactionResponse();
        r.setId(String.valueOf(t.getTransactionId()));
        r.setCreationDate(t.getCreationDate());
        r.setAmount(BigDecimal.valueOf(t.getAmount()));
        try { r.setPaymentMode(PaymentMethodType.valueOf(t.getPaymentMode())); } catch (Exception ignored) {}

        if (account != null) {
            AccountResponse accountResponse = new AccountResponse();
            accountResponse.setId(String.valueOf(account.getAccountId()));
            try { accountResponse.setType(AccountTypeType.valueOf(account.getType())); } catch (Exception ignored) {}
            accountResponse.setAmount(BigDecimal.valueOf(account.getAmount()));
            r.setAccountCredited(accountResponse);
        }

        if (member != null) {
            MemberResponse memberResponse = new MemberResponse();
            memberResponse.setMemberId(member.getMemberId());
            memberResponse.setFirstName(member.getFirstName());
            memberResponse.setLastName(member.getLastName());
            r.setMemberDebited(memberResponse);
        }

        return r;
    }
}
