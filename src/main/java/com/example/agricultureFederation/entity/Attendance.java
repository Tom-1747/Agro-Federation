package com.example.agricultureFederation.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Attendance {

    private Integer idAttendance;
    private Integer activityId;
    private Integer memberId;
    private Boolean isPresent;
    private Boolean isExcused;
    private String absenceReason;
    private LocalDateTime recordedAt;
    private LocalDateTime updatedAt;

    private LocalDate monthConcerned;
    private BigDecimal overallAttendanceRate;
    private Integer activeMembersCount;
    private LocalDate reportDate;
    private String memberName;
    private String activityTitle;

    public Attendance() {
        this.isPresent = false;
        this.isExcused = false;
    }

    public Attendance(Integer activityId, Integer memberId, Boolean isPresent, Boolean isExcused, String absenceReason) {
        this.activityId = activityId;
        this.memberId = memberId;
        this.isPresent = isPresent != null ? isPresent : false;
        this.isExcused = isExcused != null ? isExcused : false;
        this.absenceReason = absenceReason;
        this.recordedAt = LocalDateTime.now();
    }


    public static Attendance present(Integer activityId, Integer memberId) {
        return new Attendance(activityId, memberId, true, false, null);
    }


    public static Attendance absent(Integer activityId, Integer memberId, String reason) {
        return new Attendance(activityId, memberId, false, false, reason);
    }


    public static Attendance excused(Integer activityId, Integer memberId, String reason) {
        Attendance attendance = new Attendance(activityId, memberId, false, true, reason);
        return attendance;
    }


    public Integer getIdAttendance() {
        return idAttendance;
    }

    public void setIdAttendance(Integer idAttendance) {
        this.idAttendance = idAttendance;
    }

    public Integer getActivityId() {
        return activityId;
    }

    public void setActivityId(Integer activityId) {
        this.activityId = activityId;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public Boolean getIsPresent() {
        return isPresent;
    }

    public void setIsPresent(Boolean isPresent) {
        this.isPresent = isPresent;
    }

    public Boolean getIsExcused() {
        return isExcused;
    }

    public void setIsExcused(Boolean isExcused) {
        this.isExcused = isExcused;
    }

    public String getAbsenceReason() {
        return absenceReason;
    }

    public void setAbsenceReason(String absenceReason) {
        this.absenceReason = absenceReason;
    }

    public LocalDateTime getRecordedAt() {
        return recordedAt;
    }

    public void setRecordedAt(LocalDateTime recordedAt) {
        this.recordedAt = recordedAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }


    public LocalDate getMonthConcerned() {
        return monthConcerned;
    }

    public void setMonthConcerned(LocalDate monthConcerned) {
        this.monthConcerned = monthConcerned;
    }

    public BigDecimal getOverallAttendanceRate() {
        return overallAttendanceRate;
    }

    public void setOverallAttendanceRate(BigDecimal overallAttendanceRate) {
        this.overallAttendanceRate = overallAttendanceRate;
    }

    public Integer getActiveMembersCount() {
        return activeMembersCount;
    }

    public void setActiveMembersCount(Integer activeMembersCount) {
        this.activeMembersCount = activeMembersCount;
    }

    public LocalDate getReportDate() {
        return reportDate;
    }

    public void setReportDate(LocalDate reportDate) {
        this.reportDate = reportDate;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getActivityTitle() {
        return activityTitle;
    }

    public void setActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }


    public boolean isPresent() {
        return Boolean.TRUE.equals(isPresent);
    }

    public boolean isExcused() {
        return Boolean.TRUE.equals(isExcused);
    }

    git
    public boolean isUnexcusedAbsence() {
        return !isPresent() && !isExcused();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attendance that = (Attendance) o;
        return Objects.equals(idAttendance, that.idAttendance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAttendance);
    }

    @Override
    public String toString() {
        return "Attendance{" +
                "idAttendance=" + idAttendance +
                ", activityId=" + activityId +
                ", memberId=" + memberId +
                ", isPresent=" + isPresent +
                ", isExcused=" + isExcused +
                '}';
    }
}