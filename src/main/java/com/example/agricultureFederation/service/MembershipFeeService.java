package com.example.agricultureFederation.service;

import com.example.agricultureFederation.dto.request.CreateMembershipFeeRequest;
import com.example.agricultureFederation.dto.response.MembershipFeeResponse;
import com.example.agricultureFederation.entity.MembershipFee;
import com.example.agricultureFederation.entity.enums.FrequencyType;
import com.example.agricultureFederation.repository.CollectiveRepository;
import com.example.agricultureFederation.repository.MembershipFeeRepository;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MembershipFeeService {

    private final MembershipFeeRepository membershipFeeRepository;
    private final CollectiveRepository collectiveRepository;

    public MembershipFeeService(MembershipFeeRepository membershipFeeRepository,
                                CollectiveRepository collectiveRepository) {
        this.membershipFeeRepository = membershipFeeRepository;
        this.collectiveRepository = collectiveRepository;
    }

    public List<MembershipFeeResponse> getByCollectiveId(String collectiveId) throws SQLException {
        int id = Integer.parseInt(collectiveId);
        if (collectiveRepository.findById(id) == null) {
            throw new IllegalArgumentException("Collectivity not found: " + collectiveId);
        }
        List<MembershipFee> fees = membershipFeeRepository.findByCollectiveId(id);
        List<MembershipFeeResponse> responses = new ArrayList<>();
        for (MembershipFee fee : fees) responses.add(toResponse(fee));
        return responses;
    }

    public List<MembershipFeeResponse> createFees(String collectiveId,
                                                  List<CreateMembershipFeeRequest> requests) throws SQLException {
        int id = Integer.parseInt(collectiveId);
        if (collectiveRepository.findById(id) == null) {
            throw new IllegalArgumentException("Collectivity not found: " + collectiveId);
        }

        List<MembershipFeeResponse> responses = new ArrayList<>();
        for (CreateMembershipFeeRequest request : requests) {

            if (request.getFrequency() == null) {
                throw new IllegalArgumentException("Frequency is required.");
            }

            if (request.getAmount() == null || request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Amount must be greater than 0.");
            }

            MembershipFee fee = new MembershipFee();
            fee.setCollectiveId(id);
            fee.setDueDate(request.getEligibleFrom());
            fee.setFrequency(request.getFrequency());
            fee.setAmount(request.getAmount());
            fee.setLabel(request.getLabel());
            fee.setStatus("ACTIVE");

            MembershipFee saved = membershipFeeRepository.save(fee);
            responses.add(toResponse(saved));
        }
        return responses;
    }

    private MembershipFeeResponse toResponse(MembershipFee fee) {
        MembershipFeeResponse r = new MembershipFeeResponse();
        r.setId(String.valueOf(fee.getMembershipFeeId()));
        r.setEligibleFrom(fee.getDueDate());
        r.setFrequency(fee.getFrequency());
        r.setAmount(fee.getAmount() != null && fee.getAmount().compareTo(java.math.BigDecimal.ZERO) != 0 ? fee.getAmount() : null);
        r.setLabel(fee.getLabel());
        r.setActive("ACTIVE".equals(fee.getStatus()));
        return r;
    }
}