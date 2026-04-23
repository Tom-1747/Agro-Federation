package com.example.agricultureFederation.entity;

import com.example.agricultureFederation.entity.enums.ContributionTypeType;
import com.example.agricultureFederation.entity.enums.FrequencyType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MembershipFee {

    private int membershipFeeId;
    private int memberId;
    private int collectiveId;
    private ContributionTypeType contributionType;
    private FrequencyType frequency;
    private BigDecimal amount;
    private LocalDate dueDate;
    private Boolean isPaid;
    private String label;
    private String status;

    public MembershipFee() {
        this.isPaid = false;
        this.status = "ACTIVE";
    }

    public MembershipFee(int collectiveId, ContributionTypeType contributionType,
                         FrequencyType frequency, BigDecimal amount, LocalDate dueDate) {
        this.collectiveId = collectiveId;
        this.memberId = collectiveId;
        this.contributionType = contributionType;
        this.frequency = frequency;
        this.amount = amount;
        this.dueDate = dueDate;
        this.isPaid = false;
        this.status = "ACTIVE";
    }

    public MembershipFee(int memberId, int collectiveId, ContributionTypeType contributionType,
                         FrequencyType frequency, BigDecimal amount, LocalDate dueDate) {
        this.memberId = memberId;
        this.collectiveId = collectiveId;
        this.contributionType = contributionType;
        this.frequency = frequency;
        this.amount = amount;
        this.dueDate = dueDate;
        this.isPaid = false;
        this.status = "ACTIVE";
    }

    public int getMembershipFeeId() { return membershipFeeId; }
    public void setMembershipFeeId(int membershipFeeId) { this.membershipFeeId = membershipFeeId; }

    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }

    public int getCollectiveId() { return collectiveId; }
    public void setCollectiveId(int collectiveId) { this.collectiveId = collectiveId; }

    public ContributionTypeType getContributionType() { return contributionType; }
    public void setContributionType(ContributionTypeType contributionType) { this.contributionType = contributionType; }

    public FrequencyType getFrequency() { return frequency; }
    public void setFrequency(FrequencyType frequency) { this.frequency = frequency; }

    public String getFrequencyAsString() { return frequency != null ? frequency.name() : null; }
    public void setFrequencyFromString(String value) {
        try { this.frequency = FrequencyType.valueOf(value); } catch (Exception ignored) {}
    }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public Boolean getIsPaid() { return isPaid; }
    public void setIsPaid(Boolean isPaid) { this.isPaid = isPaid; }

    public String getLabel() {
        if (label == null && frequency != null && amount != null) {
            return frequency.name() + " - " + amount;
        }
        return label;
    }
    public void setLabel(String label) { this.label = label; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public boolean isOverdue() {
        if (isPaid) return false;
        if (dueDate == null) return false;
        return LocalDate.now().isAfter(dueDate);
    }

    public boolean isActive() {
        return "ACTIVE".equalsIgnoreCase(status);
    }

    @Override
    public String toString() {
        return "MembershipFee{" +
                "membershipFeeId=" + membershipFeeId +
                ", memberId=" + memberId +
                ", collectiveId=" + collectiveId +
                ", frequency=" + frequency +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                '}';
    }
}