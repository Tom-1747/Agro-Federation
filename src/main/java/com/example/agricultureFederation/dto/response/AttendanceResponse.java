package com.example.agricultureFederation.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public class AttendanceResponse {

    private Integer id;
    private Integer activityId;
    private Integer memberId;
    private String memberFullName;
    private Integer memberCollectiveId;
    private Boolean isPresent;
    private Boolean isExcused;
    private String absenceReason;
    private LocalDate monthConcerned;
    private BigDecimal overallAttendanceRate;
    private Integer activeMembersCount;
    private LocalDate reportDate;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getActivityId() { return activityId; }
    public void setActivityId(Integer activityId) { this.activityId = activityId; }

    public Integer getMemberId() { return memberId; }
    public void setMemberId(Integer memberId) { this.memberId = memberId; }

    public String getMemberFullName() { return memberFullName; }
    public void setMemberFullName(String memberFullName) { this.memberFullName = memberFullName; }

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

    public BigDecimal getOverallAttendanceRate() { return overallAttendanceRate; }
    public void setOverallAttendanceRate(BigDecimal overallAttendanceRate) { this.overallAttendanceRate = overallAttendanceRate; }

    public Integer getActiveMembersCount() { return activeMembersCount; }
    public void setActiveMembersCount(Integer activeMembersCount) { this.activeMembersCount = activeMembersCount; }

    public LocalDate getReportDate() { return reportDate; }
    public void setReportDate(LocalDate reportDate) { this.reportDate = reportDate; }
}
