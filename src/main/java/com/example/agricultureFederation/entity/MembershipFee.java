package com.example.agricultureFederation.entity;

import com.example.agricultureFederation.entity.enums.ContributionTypeType;
import com.example.agricultureFederation.entity.enums.FrequencyType;

import java.math.BigDecimal;
import java.time.LocalDate;


public class MembershipFee {

    private int membershipFeeId;
    private int collectiveId;
    private ContributionTypeType contributionType;
    private FrequencyType frequency;
    private BigDecimal amount;
    private LocalDate dueDate;
    private Boolean isPaid;
    private String label;   // logical label derived from frequency+amount for display
    private String status;  // "ACTIVE" / "INACTIVE" — application-level status

    public MembershipFee() {
        this.isPaid = false;
        this.status = "ACTIVE";
    }

    public int getMembershipFeeId() { return membershipFeeId; }
    public void setMembershipFeeId(int membershipFeeId) { this.membershipFeeId = membershipFeeId; }

    public int getCollectiveId() { return collectiveId; }
    public void setCollectiveId(int collectiveId) { this.collectiveId = collectiveId; }

    public ContributionTypeType getContributionType() { return contributionType; }
    public void setContributionType(ContributionTypeType contributionType) { this.contributionType = contributionType; }

    public FrequencyType getFrequency() { return frequency; }
    public void setFrequency(FrequencyType frequency) { this.frequency = frequency; }

    /** Stored as String in DB for raw JDBC compat; use getFrequency() where enum is needed. */
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

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return "MembershipFee{" +
                "membershipFeeId=" + membershipFeeId +
                ", collectiveId=" + collectiveId +
                ", frequency=" + frequency +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                '}';
    }
}