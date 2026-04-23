package com.example.agricultureFederation.entity;

import com.example.agricultureFederation.entity.enums.ActivityTypeType;
import com.example.agricultureFederation.entity.enums.TargetMembersType;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Activity {

    private Integer idActivity;           // maps to id_activity (PK)
    private Integer collectiveId;         // maps to id_collective (FK)
    private Integer federationId;         // maps to id_federation (FK)
    private String title;                 // maps to title
    private ActivityTypeType activityType; // maps to activity_type (enum)
    private LocalDate activityDate;       // maps to activity_date
    private Boolean attendanceRequired;   // maps to attendance_required
    private TargetMembersType targetMembers; // maps to target_members (enum)
    private Boolean isFederation;         // maps to is_federation
    private LocalDateTime createdAt;      // Optional: for tracking
    private LocalDateTime updatedAt;      // Optional: for tracking

    public Activity() {
        this.attendanceRequired = true;
        this.targetMembers = TargetMembersType.All;
        this.isFederation = false;
    }

    public Activity(Integer collectiveId, Integer federationId, String title,
                    ActivityTypeType activityType, LocalDate activityDate,
                    Boolean attendanceRequired, TargetMembersType targetMembers,
                    Boolean isFederation) {
        this.collectiveId = collectiveId;
        this.federationId = federationId;
        this.title = title;
        this.activityType = activityType;
        this.activityDate = activityDate;
        this.attendanceRequired = attendanceRequired != null ? attendanceRequired : true;
        this.targetMembers = targetMembers != null ? targetMembers : TargetMembersType.All;
        this.isFederation = isFederation != null ? isFederation : false;
    }

    /**
     * Factory method for collective-level activity
     */
    public static Activity collectiveActivity(Integer collectiveId, String title,
                                              ActivityTypeType activityType,
                                              LocalDate activityDate) {
        Activity activity = new Activity();
        activity.setCollectiveId(collectiveId);
        activity.setTitle(title);
        activity.setActivityType(activityType);
        activity.setActivityDate(activityDate);
        activity.setIsFederation(false);
        return activity;
    }

    /**
     * Factory method for federation-level activity
     */
    public static Activity federationActivity(Integer federationId, String title,
                                              ActivityTypeType activityType,
                                              LocalDate activityDate) {
        Activity activity = new Activity();
        activity.setFederationId(federationId);
        activity.setTitle(title);
        activity.setActivityType(activityType);
        activity.setActivityDate(activityDate);
        activity.setIsFederation(true);
        return activity;
    }

    // Getters and Setters

    public Integer getIdActivity() {
        return idActivity;
    }

    public void setIdActivity(Integer idActivity) {
        this.idActivity = idActivity;
    }

    public Integer getCollectiveId() {
        return collectiveId;
    }

    public void setCollectiveId(Integer collectiveId) {
        this.collectiveId = collectiveId;
    }

    public Integer getFederationId() {
        return federationId;
    }

    public void setFederationId(Integer federationId) {
        this.federationId = federationId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ActivityTypeType getActivityType() {
        return activityType;
    }

    public void setActivityType(ActivityTypeType activityType) {
        this.activityType = activityType;
    }

    public LocalDate getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(LocalDate activityDate) {
        this.activityDate = activityDate;
    }

    public Boolean getAttendanceRequired() {
        return attendanceRequired;
    }

    public void setAttendanceRequired(Boolean attendanceRequired) {
        this.attendanceRequired = attendanceRequired;
    }

    public TargetMembersType getTargetMembers() {
        return targetMembers;
    }

    public void setTargetMembers(TargetMembersType targetMembers) {
        this.targetMembers = targetMembers;
    }

    public Boolean getIsFederation() {
        return isFederation;
    }

    public void setIsFederation(Boolean isFederation) {
        this.isFederation = isFederation;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Check if activity is from federation level
     */
    public boolean isFederationLevel() {
        return isFederation != null && isFederation;
    }

    /**
     * Check if activity is from collective level
     */
    public boolean isCollectiveLevel() {
        return isFederation != null && !isFederation;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "idActivity=" + idActivity +
                ", title='" + title + '\'' +
                ", activityType=" + activityType +
                ", activityDate=" + activityDate +
                ", isFederation=" + isFederation +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity activity = (Activity) o;
        return idActivity != null && idActivity.equals(activity.idActivity);
    }

    @Override
    public int hashCode() {
        return idActivity != null ? idActivity.hashCode() : 0;
    }
}