package com.example.agricultureFederation.service;

import com.example.agricultureFederation.dto.response.*;
import com.example.agricultureFederation.repository.CollectiveRepository;
import com.example.agricultureFederation.repository.StatisticsRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

public class StatisticsService {

    private final StatisticsRepository statisticsRepository;
    private final CollectiveRepository collectiveRepository;

    public StatisticsService(StatisticsRepository statisticsRepository,
                             CollectiveRepository collectiveRepository) {
        this.statisticsRepository = statisticsRepository;
        this.collectiveRepository = collectiveRepository;
    }

    public CollectivityStatisticsResponse getCollectivityStatistics(
            String collectiveId, LocalDate from, LocalDate to) throws SQLException {

        int id = Integer.parseInt(collectiveId);

        if (collectiveRepository.findById(id) == null) {
            throw new IllegalArgumentException("Collectivity not found: " + collectiveId);
        }
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("'from' date must be before or equal to 'to' date.");
        }

        Map<Integer, BigDecimal> paidByMember =
                statisticsRepository.getTotalPaidByMemberInPeriod(id, from, to);

        Map<Integer, BigDecimal> unpaidByMember =
                statisticsRepository.getExpectedActiveContributionsByMember(id, from, to);

        List<int[]> memberIds = statisticsRepository.getActiveMemberIds(id);

        List<MemberStatDto> memberStats = new ArrayList<>();
        for (int[] memberIdArr : memberIds) {
            int memberId = memberIdArr[0];
            String[] name = statisticsRepository.getMemberName(memberId);

            BigDecimal collected = paidByMember.getOrDefault(memberId, BigDecimal.ZERO);
            BigDecimal unpaid = unpaidByMember.getOrDefault(memberId, BigDecimal.ZERO);

            MemberStatDto dto = new MemberStatDto();
            dto.setMemberId(String.valueOf(memberId));
            dto.setFirstName(name[0]);
            dto.setLastName(name[1]);
            dto.setTotalCollected(collected);
            dto.setPotentialUnpaid(unpaid);
            memberStats.add(dto);
        }

        CollectivityStatisticsResponse response = new CollectivityStatisticsResponse();
        response.setCollectivityId(collectiveId);
        response.setFrom(from);
        response.setTo(to);
        response.setMemberStats(memberStats);
        return response;
    }

    public FederationStatisticsResponse getFederationStatistics(
            LocalDate from, LocalDate to) throws SQLException {

        if (from.isAfter(to)) {
            throw new IllegalArgumentException("'from' date must be before or equal to 'to' date.");
        }

        List<Integer> collectiveIds = statisticsRepository.getAllCollectiveIds();

        Map<Integer, Integer> activeMembersMap =
                statisticsRepository.countActiveMembersPerCollective();
        Map<Integer, Integer> upToDateMap =
                statisticsRepository.countMembersUpToDatePerCollective(from, to);
        Map<Integer, Integer> newMembersMap =
                statisticsRepository.countNewMembersPerCollective(from, to);

        List<CollectivitySummaryStatDto> summaries = new ArrayList<>();

        for (int collectiveId : collectiveIds) {
            String[] nameAndLocation = statisticsRepository.getCollectiveNameAndNumber(collectiveId);

            int activeCount = activeMembersMap.getOrDefault(collectiveId, 0);
            int upToDateCount = upToDateMap.getOrDefault(collectiveId, 0);
            int newCount = newMembersMap.getOrDefault(collectiveId, 0);

            BigDecimal percentage = BigDecimal.ZERO;
            if (activeCount > 0) {
                percentage = BigDecimal.valueOf(upToDateCount)
                        .multiply(BigDecimal.valueOf(100))
                        .divide(BigDecimal.valueOf(activeCount), 2, RoundingMode.HALF_UP);
            }

            CollectivitySummaryStatDto dto = new CollectivitySummaryStatDto();
            dto.setCollectivityId(String.valueOf(collectiveId));
            dto.setName(nameAndLocation[0]);
            dto.setActiveMembersCount(activeCount);
            dto.setUpToDateMembersCount(upToDateCount);
            dto.setUpToDatePercentage(percentage);
            dto.setNewMembersCount(newCount);
            summaries.add(dto);
        }

        FederationStatisticsResponse response = new FederationStatisticsResponse();
        response.setFrom(from);
        response.setTo(to);
        response.setCollectivities(summaries);
        return response;
    }
}

