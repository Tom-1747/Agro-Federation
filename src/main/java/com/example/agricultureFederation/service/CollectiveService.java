package com.example.agricultureFederation.service;

import com.example.agricultureFederation.dto.request.AssignCollectivityIdentityRequest;
import com.example.agricultureFederation.dto.request.CreateCollectivityRequest;
import com.example.agricultureFederation.dto.response.CollectivityResponse;
import com.example.agricultureFederation.dto.response.CollectivityStructureResponse;
import com.example.agricultureFederation.dto.response.MemberResponse;
import com.example.agricultureFederation.entity.Collective;
import com.example.agricultureFederation.entity.Member;
import com.example.agricultureFederation.repository.CollectiveRepository;
<<<<<<< HEAD
=======
import com.example.agricultureFederation.repository.MemberRepository;
>>>>>>> 21-april

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;

public class CollectiveService {

    private final CollectiveRepository collectiveRepository;
    private final MemberRepository memberRepository;

    public CollectiveService(CollectiveRepository collectiveRepository,
                             MemberRepository memberRepository) {
        this.collectiveRepository = collectiveRepository;
        this.memberRepository = memberRepository;
    }

    public List<CollectivityResponse> createCollectivities(
            List<CreateCollectivityRequest> requests) throws SQLException {

        List<CollectivityResponse> responses = new ArrayList<>();

        for (CreateCollectivityRequest request : requests) {

            if (!request.isFederationApproval()) {
                throw new IllegalStateException("Federation approval is required.");
            }

            if (request.getStructure() == null) {
                throw new IllegalStateException("Collectivity structure is required.");
            }

            if (request.getMembers() == null || request.getMembers().size() < 10) {
                throw new IllegalStateException("At least 10 members are required.");
            }

            int seniorCount = 0;
            List<Member> memberEntities = new ArrayList<>();
            for (String memberId : request.getMembers()) {
                Member member = memberRepository.findById(Integer.parseInt(memberId));
                if (member == null) {
                    throw new IllegalArgumentException("Member not found: " + memberId);
                }
                memberEntities.add(member);
                if (memberRepository.hasMinimumSeniority(member.getMemberId(), 180)) {
                    seniorCount++;
                }
            }
            if (seniorCount < 5) {
                throw new IllegalStateException(
                        "At least 5 members must have 6 months seniority. Current: " + seniorCount
                );
            }

            Collective collective = new Collective(
                    1,
                    null,
                    null,
                    null,
                    request.getLocation(),
                    null,
                    LocalDate.now()
            );
            Collective saved = collectiveRepository.save(collective);
            responses.add(toResponse(saved, memberEntities, request));
        }

<<<<<<< HEAD
        int seniorMembers = collectiveRepository.countMembersWithSixMonthsSeniority(request.getFederationId());
        if (seniorMembers < 5) {
            throw new IllegalStateException(
                    "At least 5 members must have 6 months seniority in the federation. Current: " + seniorMembers
            );
        }

        int currentYear = Year.now().getValue();
        boolean positionsFilled = collectiveRepository.hasAllSpecificPositionsFilled(request.getFederationId(), currentYear);
        if (!positionsFilled) {
            throw new IllegalStateException(
                    "All specific positions (President, Vice-President, Treasurer, Secretary) must be filled."
            );
        }

        if (request.getLocation() == null || request.getLocation().isBlank()) {
            throw new IllegalArgumentException("Location is required to open a new collective.");
        }

        Collective collective = new Collective();
        collective.setFederationId(request.getFederationId());
        collective.setSpecialityId(request.getSpecialityId());
        collective.setBranchId(request.getBranchId());
        collective.setName(request.getName());
        collective.setLocation(request.getLocation());
        collective.setPhone(request.getPhone());
        collective.setCreationDate(request.getCreationDate() != null ? request.getCreationDate() : LocalDate.now());

        Collective saved = collectiveRepository.save(collective);
        return toResponse(saved);
=======
        return responses;
>>>>>>> 21-april
    }

    public CollectivityResponse getById(String collectiveId) throws SQLException {
        int id = Integer.parseInt(collectiveId);
        Collective collective = collectiveRepository.findById(id);
        if (collective == null) {
            throw new IllegalArgumentException("Collectivity not found: " + collectiveId);
        }
        List<Member> members = memberRepository.findActiveByCollectiveId(id);
        return toResponse(collective, members, null);
    }

    public CollectivityResponse assignIdentity(String collectiveId,
                                               AssignCollectivityIdentityRequest request) throws SQLException {

        int id = Integer.parseInt(collectiveId);

        Collective collective = collectiveRepository.findById(id);
        if (collective == null) {
            throw new IllegalArgumentException("Collectivity not found: " + collectiveId);
        }

        if (collective.getNumber() != null) {
            throw new IllegalStateException("Number has already been assigned and cannot be changed.");
        }

        if (collective.getName() != null) {
            throw new IllegalStateException("Name has already been assigned and cannot be changed.");
        }

        if (collectiveRepository.existsByNumber(request.getNumber())) {
            throw new IllegalStateException("Number already taken: " + request.getNumber());
        }

        if (collectiveRepository.existsByName(request.getName())) {
            throw new IllegalStateException("Name already taken: " + request.getName());
        }

        Collective updated = collectiveRepository.assignIdentity(id, request.getNumber(), request.getName());
        List<Member> members = memberRepository.findActiveByCollectiveId(id);
        return toResponse(updated, members, null);
    }

    private CollectivityResponse toResponse(Collective collective,
                                            List<Member> members,
                                            CreateCollectivityRequest request) throws SQLException {
        CollectivityResponse response = new CollectivityResponse();
        response.setId(String.valueOf(collective.getCollectiveId()));
        response.setLocation(collective.getLocation());
        response.setName(collective.getName());
        response.setNumber(collective.getNumber());

        if (request != null && request.getStructure() != null) {
            CollectivityStructureResponse structure = new CollectivityStructureResponse();
            structure.setPresident(getMemberResponse(request.getStructure().getPresident()));
            structure.setVicePresident(getMemberResponse(request.getStructure().getVicePresident()));
            structure.setTreasurer(getMemberResponse(request.getStructure().getTreasurer()));
            structure.setSecretary(getMemberResponse(request.getStructure().getSecretary()));
            response.setStructure(structure);
        }

        List<MemberResponse> memberResponses = new ArrayList<>();
        for (Member m : members) {
            memberResponses.add(toMemberResponse(m));
        }
        response.setMembers(memberResponses);

        return response;
    }
<<<<<<< HEAD
}
=======

    private MemberResponse getMemberResponse(String memberId) throws SQLException {
        if (memberId == null) return null;
        Member member = memberRepository.findById(Integer.parseInt(memberId));
        if (member == null) throw new IllegalArgumentException("Member not found: " + memberId);
        return toMemberResponse(member);
    }

    private MemberResponse toMemberResponse(Member member) {
        MemberResponse r = new MemberResponse();
        r.setId(String.valueOf(member.getMemberId()));
        r.setFirstName(member.getFirstName());
        r.setLastName(member.getLastName());
        r.setBirthDate(member.getBirthDate());
        r.setGender(member.getGender());
        r.setAddress(member.getAddress());
        r.setPhoneNumber(member.getPhone());
        r.setEmail(member.getEmail());
        return r;
    }
}
>>>>>>> 21-april
