package com.example.agricultureFederation.dto.response;

import com.example.agricultureFederation.entity.enums.FrequencyType;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MembershipFeeResponse {
    private String id;
    private LocalDate eligibleFrom;
    private FrequencyType frequency;
    private BigDecimal amount;
    private String label;
    private Boolean active;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public LocalDate getEligibleFrom() { return eligibleFrom; }
    public void setEligibleFrom(LocalDate eligibleFrom) { this.eligibleFrom = eligibleFrom; }

    public FrequencyType getFrequency() { return frequency; }
    public void setFrequency(FrequencyType frequency) { this.frequency = frequency; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
