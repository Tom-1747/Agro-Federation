package com.example.agricultureFederation.dto.response;

import java.time.LocalDate;
import java.util.List;

public class FederationStatisticsResponse {

    private LocalDate from;
    private LocalDate to;
    private List<CollectivitySummaryStatDto> collectivities;

    public FederationStatisticsResponse() {}

    public LocalDate getFrom() { return from; }
    public void setFrom(LocalDate from) { this.from = from; }

    public LocalDate getTo() { return to; }
    public void setTo(LocalDate to) { this.to = to; }

    public List<CollectivitySummaryStatDto> getCollectivities() { return collectivities; }
    public void setCollectivities(List<CollectivitySummaryStatDto> collectivities) { this.collectivities = collectivities; }
}
