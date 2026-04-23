package com.example.agricultureFederation.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

public class Attendance {

    private Integer idAttendance;        // maps to id_attendance (PK)
    private Integer activityId;          // maps to id_activity (FK)
    private Integer memberId;            // maps to id_member (FK)
    private Boolean isPresent;           // maps to is_present
    private Boolean isExcused;           // maps to is_excused
    private String absenceReason;        // maps to absence_reason
    private LocalDateTime recordedAt;    // maps to recorded_at (when attendance was recorded)
    private LocalDateTime updatedAt;     // Optional: for tracking updates

    // Derived fields (not stored in database, calculated from queries)
    private LocalDate monthConcerned;        // Derived from activity date
    private BigDecimal overallAttendanceRate; // Calculated from attendance data
    private Integer activeMembersCount;       // Calculated from member table
    private LocalDate reportDate;             // For reporting purposes
    private String memberName;                // For display purposes (joined from member table)
    private String activityTitle;             // For display purposes (joined from activity table)

    public Attendance() {
        this.isPresent = false;
        this.isExcused = false;
    }

    /**
     * Constructor for creating a new attendance record
     */
    public Attendance(Integer activityId, Integer memberId, Boolean isPresent, Boolean isExcused, String absenceReason) {
        this.activityId = activityId;
        this.memberId = memberId;
        this.isPresent = isPresent != null ? isPresent : false;
        this.isExcused = isExcused != null ? isExcused : false;
        this.absenceReason = absenceReason;
        this.recordedAt = LocalDateTime.now();
    }

    /**
     * Factory method for present member
     */
    public static Attendance present(Integer activityId, Integer memberId) {
        return new Attendance(activityId, memberId, true, false, null);
    }

    /**
     * Factory method for absent member
     */
    public static Attendance absent(Integer activityId, Integer memberId, String reason) {
        return new Attendance(activityId, memberId, false, false, reason);
    }

    /**
     * Factory method for excused member
     */
    public static Attendance excused(Integer activityId, Integer memberId, String reason) {
        Attendance attendance = new Attendance(activityId, memberId, false, true, reason);
        return attendance;
    }

    // Getters and Setters

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

    // Derived fields (not stored in DB)

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

    // Helper methods

    /**
     * Check if member was present
     */
    public boolean isPresent() {
        return Boolean.TRUE.equals(isPresent);
    }

    /**
     * Check if member was excused
     */
    public boolean isExcused() {
        return Boolean.TRUE.equals(isExcused);
    }

    /**
     * Check if member was absent without excuse
     */
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