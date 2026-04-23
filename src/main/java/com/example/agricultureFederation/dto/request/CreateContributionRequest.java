package com.example.agricultureFederation.dto.request;

import com.example.agricultureFederation.entity.enums.ContributionTypeType;
import com.example.agricultureFederation.entity.enums.FrequencyType;

import java.math.BigDecimal;
import java.time.LocalDate;


public class CreateContributionRequest {

    private Integer memberId;
    private Integer collectiveId;
    private ContributionTypeType contributionType;
    private FrequencyType frequency;
    private BigDecimal amount;
    private LocalDate dueDate;

    public Integer getMemberId() { return memberId; }
    public void setMemberId(Integer memberId) { this.memberId = memberId; }

    public Integer getCollectiveId() { return collectiveId; }
    public void setCollectiveId(Integer collectiveId) { this.collectiveId = collectiveId; }

    public ContributionTypeType getContributionType() { return contributionType; }
    public void setContributionType(ContributionTypeType contributionType) { this.contributionType = contributionType; }

    public FrequencyType getFrequency() { return frequency; }
    public void setFrequency(FrequencyType frequency) { this.frequency = frequency; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
}
