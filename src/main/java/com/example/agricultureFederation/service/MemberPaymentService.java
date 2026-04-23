package com.example.agricultureFederation.service;

import com.example.agricultureFederation.dto.request.CreateMemberPaymentRequest;
import com.example.agricultureFederation.dto.response.AccountResponse;
import com.example.agricultureFederation.dto.response.MemberPaymentResponse;
import com.example.agricultureFederation.entity.CollectivityTransaction;
import com.example.agricultureFederation.entity.FinancialAccount;
import com.example.agricultureFederation.entity.MemberPayment;
import com.example.agricultureFederation.entity.MembershipFee;
import com.example.agricultureFederation.entity.enums.AccountTypeType;
import com.example.agricultureFederation.entity.enums.PaymentMethodType;
import com.example.agricultureFederation.repository.CollectivityTransactionRepository;
import com.example.agricultureFederation.repository.FinancialAccountRepository;
import com.example.agricultureFederation.repository.MemberPaymentRepository;
import com.example.agricultureFederation.repository.MemberRepository;
import com.example.agricultureFederation.repository.MembershipFeeRepository;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MemberPaymentService {

    private final MemberPaymentRepository memberPaymentRepository;
    private final MemberRepository memberRepository;
    private final MembershipFeeRepository membershipFeeRepository;
    private final FinancialAccountRepository financialAccountRepository;
    private final CollectivityTransactionRepository transactionRepository;

    public MemberPaymentService(MemberPaymentRepository memberPaymentRepository,
                                MemberRepository memberRepository,
                                MembershipFeeRepository membershipFeeRepository,
                                FinancialAccountRepository financialAccountRepository,
                                CollectivityTransactionRepository transactionRepository) {
        this.memberPaymentRepository = memberPaymentRepository;
        this.memberRepository = memberRepository;
        this.membershipFeeRepository = membershipFeeRepository;
        this.financialAccountRepository = financialAccountRepository;
        this.transactionRepository = transactionRepository;
    }

    public List<MemberPaymentResponse> createPayments(String memberId,
                                                      List<CreateMemberPaymentRequest> requests) throws SQLException {

        int mId = Integer.parseInt(memberId);
        if (memberRepository.findById(mId) == null) {
            throw new IllegalArgumentException("Member not found: " + memberId);
        }

        List<MemberPaymentResponse> responses = new ArrayList<>();

        for (CreateMemberPaymentRequest request : requests) {

            MembershipFee fee = membershipFeeRepository.findById(
                    Integer.parseInt(request.getMembershipFeeIdentifier())
            );
            if (fee == null) {
                throw new IllegalArgumentException(
                        "Membership fee not found: " + request.getMembershipFeeIdentifier()
                );
            }

            FinancialAccount account = financialAccountRepository.findById(
                    Integer.parseInt(request.getAccountCreditedIdentifier())
            );
            if (account == null) {
                throw new IllegalArgumentException(
                        "Account not found: " + request.getAccountCreditedIdentifier()
                );
            }

            MemberPayment payment = new MemberPayment();
            payment.setMemberId(mId);
            payment.setMembershipFeeId(fee.getMembershipFeeId());
            payment.setAccountId(account.getAccountId());
            payment.setAmount(request.getAmount());
            // PaymentMethodType enum → String pour la DB
            payment.setPaymentMode(request.getPaymentMode() != null ? request.getPaymentMode().name() : null);
            payment.setCreationDate(LocalDate.now());

            MemberPayment saved = memberPaymentRepository.save(payment);

            financialAccountRepository.updateBalance(
                    account.getAccountId(),
                    account.getAmount() + request.getAmount()
            );

            // Auto-create transaction
            CollectivityTransaction transaction = new CollectivityTransaction();
            transaction.setCollectiveId(fee.getCollectiveId());
            transaction.setMemberId(mId);
            transaction.setAccountId(account.getAccountId());
            transaction.setAmount(request.getAmount());
            // PaymentMethodType enum → String pour la DB
            transaction.setPaymentMode(request.getPaymentMode() != null ? request.getPaymentMode().name() : null);
            transaction.setCreationDate(LocalDate.now());
            transactionRepository.save(transaction);

            responses.add(toResponse(saved, account));
        }

        return responses;
    }

    private MemberPaymentResponse toResponse(MemberPayment payment, FinancialAccount account) {
        MemberPaymentResponse r = new MemberPaymentResponse();
        r.setId(String.valueOf(payment.getPaymentId()));
        r.setAmount(payment.getAmount());
        // String DB → PaymentMethodType enum pour la réponse
        try { r.setPaymentMode(PaymentMethodType.valueOf(payment.getPaymentMode())); } catch (Exception ignored) {}
        r.setCreationDate(payment.getCreationDate());
        r.setAccountCredited(toAccountResponse(account));
        return r;
    }

    private AccountResponse toAccountResponse(FinancialAccount account) {
        AccountResponse r = new AccountResponse();
        r.setId(String.valueOf(account.getAccountId()));
        try { r.setType(AccountTypeType.valueOf(account.getType())); } catch (Exception ignored) {}
        r.setHolderName(account.getHolderName());
        r.setAmount(BigDecimal.valueOf(account.getAmount()));
        return r;
    }
}
