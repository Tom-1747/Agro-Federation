package com.example.agricultureFederation.service;

import com.example.agricultureFederation.dto.request.CreateMemberRequest;
import com.example.agricultureFederation.dto.request.SponsorRequest;
import com.example.agricultureFederation.dto.response.MemberResponse;
import com.example.agricultureFederation.entity.Member;
import com.example.agricultureFederation.repository.MemberRepository;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public MemberResponse registerMember(CreateMemberRequest request) throws SQLException {

        List<SponsorRequest> sponsors = request.getSponsors();

        if (sponsors == null || sponsors.size() < 2) {
            throw new IllegalArgumentException(
                    "The candidate must have at least 2 confirmed sponsors."
            );
        }

        List<Integer> sponsorIds = sponsors.stream()
                .map(SponsorRequest::getMemberId)
                .collect(Collectors.toList());

        List<Member> sponsorMembers = memberRepository.findSponsorsByIds(sponsorIds);

        for (Member sponsor : sponsorMembers) {
            boolean isConfirmed = memberRepository.isConfirmedMember(sponsor.getMemberId());
            if (!isConfirmed) {
                throw new IllegalArgumentException(
                        "Sponsor with id " + sponsor.getMemberId() + " is not a confirmed member."
                );
            }
        }

        long sponsorsFromTargetCollective = sponsorMembers.stream()
                .filter(s -> s.getCollectiveId() == request.getCollectiveId())
                .count();

        long sponsorsFromOtherCollectives = sponsorMembers.stream()
                .filter(s -> s.getCollectiveId() != request.getCollectiveId())
                .count();

        if (sponsorsFromTargetCollective < sponsorsFromOtherCollectives) {
            throw new IllegalArgumentException(
                    "The number of sponsors from the target collective (" + sponsorsFromTargetCollective +
                            ") must be greater than or equal to sponsors from other collectives (" + sponsorsFromOtherCollectives + ")."
            );
        }

        if (request.getMembershipFee() != 50000) {
            throw new IllegalArgumentException(
                    "Membership fee must be exactly 50,000 MGA."
            );
        }

        if (request.getAnnualContribution() <= 0) {
            throw new IllegalArgumentException(
                    "Annual contribution must be paid in full upon registration."
            );
        }

        Member member = new Member(
                request.getCollectiveId(),
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
        return toResponse(saved);
    }

    private MemberResponse toResponse(Member member) {
        MemberResponse response = new MemberResponse();
        response.setMemberId(member.getMemberId());
        response.setCollectiveId(member.getCollectiveId());
        response.setLastName(member.getLastName());
        response.setFirstName(member.getFirstName());
        response.setBirthDate(member.getBirthDate());
        response.setGender(member.getGender());
        response.setAddress(member.getAddress());
        response.setPhone(member.getPhone());
        response.setEmail(member.getEmail());
        response.setMembershipDate(member.getMembershipDate());
        return response;
    }
}