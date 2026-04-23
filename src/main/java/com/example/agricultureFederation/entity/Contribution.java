package com.example.agricultureFederation.entity;

import com.example.agricultureFederation.entity.enums.ContributionTypeType;
import com.example.agricultureFederation.entity.enums.FrequencyType;

import java.math.BigDecimal;
import java.time.LocalDate;


public class Contribution {

    private Integer idContribution;
    private Integer memberId;
    private Integer collectiveId;
    private ContributionTypeType contributionType;
    private FrequencyType frequency;
    private BigDecimal amount;
    private LocalDate dueDate;
    private Boolean isPaid;

    public Contribution() {
        this.isPaid = false;
    }

    public Contribution(Integer memberId, Integer collectiveId,
                        ContributionTypeType contributionType, FrequencyType frequency,
                        BigDecimal amount, LocalDate dueDate) {
        this.memberId = memberId;
        this.collectiveId = collectiveId;
        this.contributionType = contributionType;
        this.frequency = frequency;
        this.amount = amount;
        this.dueDate = dueDate;
        this.isPaid = false;
    }

    public Integer getIdContribution() { return idContribution; }
    public void setIdContribution(Integer idContribution) { this.idContribution = idContribution; }

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

    public Boolean getIsPaid() { return isPaid; }
    public void setIsPaid(Boolean isPaid) { this.isPaid = isPaid; }

    @Override
    public String toString() {
        return "Contribution{" +
                "idContribution=" + idContribution +
                ", memberId=" + memberId +
                ", contributionType=" + contributionType +
                ", amount=" + amount +
                ", dueDate=" + dueDate +
                ", isPaid=" + isPaid +
                '}';
    }
}
