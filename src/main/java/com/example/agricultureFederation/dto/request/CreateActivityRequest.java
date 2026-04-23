package com.example.agricultureFederation.dto.request;

import com.example.agricultureFederation.entity.enums.ActivityTypeType;
import com.example.agricultureFederation.entity.enums.TargetMembersType;

import java.time.LocalDate;

public class CreateActivityRequest {

    private Integer collectiveId;
    private Integer federationId;
    private String title;
    private ActivityTypeType activityType;
    private LocalDate activityDate;
    private Boolean attendanceRequired;
    private TargetMembersType targetMembers;
    private Boolean isFederation;

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
}
