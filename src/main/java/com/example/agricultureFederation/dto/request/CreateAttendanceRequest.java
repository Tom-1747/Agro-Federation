package com.example.agricultureFederation.dto.request;

import java.time.LocalDate;

public class CreateAttendanceRequest {

    private Integer activityId;
    private Integer memberId;
    private Integer memberCollectiveId;
    private Boolean isPresent;
    private Boolean isExcused;
    private String absenceReason;
    private LocalDate monthConcerned;

    public Integer getActivityId() { return activityId; }
    public void setActivityId(Integer activityId) { this.activityId = activityId; }

    public Integer getMemberId() { return memberId; }
    public void setMemberId(Integer memberId) { this.memberId = memberId; }

    public Integer getMemberCollectiveId() { return memberCollectiveId; }
    public void setMemberCollectiveId(Integer memberCollectiveId) { this.memberCollectiveId = memberCollectiveId; }

    public Boolean getIsPresent() { return isPresent; }
    public void setIsPresent(Boolean isPresent) { this.isPresent = isPresent; }

    public Boolean getIsExcused() { return isExcused; }
    public void setIsExcused(Boolean isExcused) { this.isExcused = isExcused; }

    public String getAbsenceReason() { return absenceReason; }
    public void setAbsenceReason(String absenceReason) { this.absenceReason = absenceReason; }

    public LocalDate getMonthConcerned() { return monthConcerned; }
    public void setMonthConcerned(LocalDate monthConcerned) { this.monthConcerned = monthConcerned; }
}
