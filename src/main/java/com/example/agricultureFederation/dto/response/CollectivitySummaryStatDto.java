package com.example.agricultureFederation.dto.response;

import java.math.BigDecimal;

public class CollectivitySummaryStatDto {

    private String collectivityId;
    private String name;
    private int activeMembersCount;
    private int upToDateMembersCount;
    private BigDecimal upToDatePercentage;
    private int newMembersCount;

    public CollectivitySummaryStatDto() {}

    public String getCollectivityId() { return collectivityId; }
    public void setCollectivityId(String collectivityId) { this.collectivityId = collectivityId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getActiveMembersCount() { return activeMembersCount; }
    public void setActiveMembersCount(int activeMembersCount) { this.activeMembersCount = activeMembersCount; }

    public int getUpToDateMembersCount() { return upToDateMembersCount; }
    public void setUpToDateMembersCount(int upToDateMembersCount) { this.upToDateMembersCount = upToDateMembersCount; }

    public BigDecimal getUpToDatePercentage() { return upToDatePercentage; }
    public void setUpToDatePercentage(BigDecimal upToDatePercentage) { this.upToDatePercentage = upToDatePercentage; }

    public int getNewMembersCount() { return newMembersCount; }
    public void setNewMembersCount(int newMembersCount) { this.newMembersCount = newMembersCount; }
}
