
package com.example.agricultureFederation.dto.response;

import java.time.LocalDate;
import java.util.List;

public class CollectivityStatisticsResponse {

    private String collectivityId;
    private LocalDate from;
    private LocalDate to;
    private List<MemberStatDto> memberStats;

    public CollectivityStatisticsResponse() {}

    public String getCollectivityId() { return collectivityId; }
    public void setCollectivityId(String collectivityId) { this.collectivityId = collectivityId; }

    public LocalDate getFrom() { return from; }
    public void setFrom(LocalDate from) { this.from = from; }

    public LocalDate getTo() { return to; }
    public void setTo(LocalDate to) { this.to = to; }

    public List<MemberStatDto> getMemberStats() { return memberStats; }
    public void setMemberStats(List<MemberStatDto> memberStats) { this.memberStats = memberStats; }
}