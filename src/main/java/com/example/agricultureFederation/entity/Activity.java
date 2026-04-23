package com.example.agricultureFederation.entity;

import com.example.agricultureFederation.entity.enums.ActivityTypeType;
import com.example.agricultureFederation.entity.enums.TargetMembersType;

import java.time.LocalDate;

public class Activity {

    private Integer idActivity;
    private Integer collectiveId;
    private Integer federationId;
    private String title;
    private ActivityTypeType activityType;
    private LocalDate activityDate;
    private Boolean attendanceRequired;
    private TargetMembersType targetMembers;
    private Boolean isFederation;

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

    public Integer getIdActivity() { return idActivity; }
    public void setIdActivity(Integer idActivity) { this.idActivity = idActivity; }

    public Integer getCollectiveId() { return collectiveId; }
    public void setCollectiveId(Integer collectiveId) { this.collectiveId = collectiveId; }

    public Integer getFederationId() { return federationId; }
    public void setFederationId(Integer federationId) { this.federationId = federationId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public ActivityTypeType getActivityType() { return activityType; }
    public void setActivityType(ActivityTypeType activityType) { this.activityType = activityType; }

    public LocalDate getActivityDate() { return activityDate; }
    public void setActivityDate(LocalDate activityDate) { this.activityDate = activityDate; }

    public Boolean getAttendanceRequired() { return attendanceRequired; }
    public void setAttendanceRequired(Boolean attendanceRequired) { this.attendanceRequired = attendanceRequired; }

    public TargetMembersType getTargetMembers() { return targetMembers; }
    public void setTargetMembers(TargetMembersType targetMembers) { this.targetMembers = targetMembers; }

    public Boolean getIsFederation() { return isFederation; }
    public void setIsFederation(Boolean isFederation) { this.isFederation = isFederation; }

    @Override
    public String toString() {
        return "Activity{" +
                "idActivity=" + idActivity +
                ", title='" + title + '\'' +
                ", activityType=" + activityType +
                ", activityDate=" + activityDate +
                '}';
    }
}
