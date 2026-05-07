package com.example.agricultureFederation.service;

import com.example.agricultureFederation.dto.request.CreateMemberRequest;
import com.example.agricultureFederation.dto.request.SponsorRequest;
import com.example.agricultureFederation.dto.response.MemberResponse;
import com.example.agricultureFederation.entity.Member;
import com.example.agricultureFederation.repository.CollectiveRepository;
import com.example.agricultureFederation.repository.MemberRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MemberService {

    private final MemberRepository memberRepository;
    private final CollectiveRepository collectiveRepository;

    public MemberService(MemberRepository memberRepository,
                         CollectiveRepository collectiveRepository) {
        this.memberRepository = memberRepository;
        this.collectiveRepository = collectiveRepository;
    }

    public List<MemberResponse> createMembers(
            List<CreateMemberRequest> requests) throws SQLException {

        List<MemberResponse> responses = new ArrayList<>();

        for (CreateMemberRequest request : requests) {

            int collectiveId = request.getCollectiveId();
            if (collectiveRepository.findById(collectiveId) == null) {
                throw new IllegalArgumentException(
                        "Collectivity not found: " + collectiveId
                );
            }

            if (request.getMembershipFee() != 50000) {
                throw new IllegalArgumentException("Membership fee must be exactly 50,000 MGA.");
            }

            if (request.getAnnualContribution() <= 0) {
                throw new IllegalArgumentException("Annual contribution must be paid in full.");
            }

            List<SponsorRequest> sponsors = request.getSponsors();
            if (sponsors == null || sponsors.size() < 2) {
                throw new IllegalArgumentException("At least 2 confirmed sponsors are required.");
            }

            List<Integer> sponsorIds = sponsors.stream()
                    .map(SponsorRequest::getMemberId)
                    .collect(Collectors.toList());

            List<Member> sponsorMembers = memberRepository.findByIds(sponsorIds);

            int sponsorsFromTargetCollective = 0;
            int sponsorsFromOtherCollectives = 0;

            for (Member sponsor : sponsorMembers) {
                boolean isConfirmed = memberRepository.isConfirmedMember(sponsor.getMemberId());
                if (!isConfirmed) {
                    throw new IllegalArgumentException(
                            "Sponsor with id " + sponsor.getMemberId() + " is not a confirmed member."
                    );
                }
                if (sponsor.getCollectiveId() == collectiveId) {
                    sponsorsFromTargetCollective++;
                } else {
                    sponsorsFromOtherCollectives++;
                }
            }

            if (sponsorsFromTargetCollective < sponsorsFromOtherCollectives) {
                throw new IllegalArgumentException(
                        "Sponsors from target collective (" + sponsorsFromTargetCollective +
                                ") must be >= sponsors from other collectives (" + sponsorsFromOtherCollectives + ")."
                );
            }

            Member member = new Member(
                    collectiveId,
                    request.getJobId(),
                    request.getLastName(),
                    request.getFirstName(),
                    request.getBirthDate(),
                    request.getGender(),
                    request.getAddress(),
                    request.getPhone(),
                    request.getEmail(),
                    LocalDate.now()
            );
            Member saved = memberRepository.save(member);
            responses.add(toMemberResponse(saved));
        }

        return responses;
    }

    private MemberResponse toMemberResponse(Member member) {
        MemberResponse r = new MemberResponse();
        r.setMemberId(member.getMemberId());
        r.setCollectiveId(member.getCollectiveId());
        r.setFirstName(member.getFirstName());
        r.setLastName(member.getLastName());
        r.setBirthDate(member.getBirthDate());
        r.setGender(member.getGender());
        r.setAddress(member.getAddress());
        r.setPhone(member.getPhone());
        r.setEmail(member.getEmail());
        r.setMembershipDate(member.getMembershipDate());
        return r;
    }
}