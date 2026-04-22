package com.example.agricultureFederation.service;

import com.example.agricultureFederation.dto.request.CreateMembershipFeeRequest;
import com.example.agricultureFederation.dto.response.MembershipFeeResponse;
import com.example.agricultureFederation.entity.MembershipFee;
import com.example.agricultureFederation.repository.CollectiveRepository;
import com.example.agricultureFederation.repository.MembershipFeeRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MembershipFeeService {

    private final MembershipFeeRepository membershipFeeRepository;
    private final CollectiveRepository collectiveRepository;

    private static final Set<String> VALID_FREQUENCIES = Set.of("WEEKLY", "MONTHLY", "ANNUALLY", "PUNCTUALLY");

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

            // Rule 1 : valid frequency
            if (!VALID_FREQUENCIES.contains(request.getFrequency())) {
                throw new IllegalArgumentException("Unrecognized frequency: " + request.getFrequency());
            }

            // Rule 2 : amount must be positive
            if (request.getAmount() <= 0) {
                throw new IllegalArgumentException("Amount must be greater than 0.");
            }

            MembershipFee fee = new MembershipFee();
            fee.setCollectiveId(id);
            fee.setEligibleFrom(request.getEligibleFrom());
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
        r.setEligibleFrom(fee.getEligibleFrom());
        r.setFrequency(fee.getFrequency());
        r.setAmount(fee.getAmount());
        r.setLabel(fee.getLabel());
        r.setStatus(fee.getStatus());
        return r;
    }
}