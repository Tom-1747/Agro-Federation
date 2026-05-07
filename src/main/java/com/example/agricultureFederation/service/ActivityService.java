package com.example.agricultureFederation.service;

import com.example.agricultureFederation.dto.request.CreateActivityRequest;
import com.example.agricultureFederation.dto.response.ActivityResponse;
import com.example.agricultureFederation.entity.Activity;
import com.example.agricultureFederation.entity.enums.TargetMembersType;
import com.example.agricultureFederation.repository.ActivityRepository;
import com.example.agricultureFederation.repository.CollectiveRepository;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ActivityService {

    private final ActivityRepository activityRepository;
    private final CollectiveRepository collectiveRepository;

    public ActivityService(ActivityRepository activityRepository,
                           CollectiveRepository collectiveRepository) {
        this.activityRepository = activityRepository;
        this.collectiveRepository = collectiveRepository;
    }

    public List<ActivityResponse> createActivities(String collectiveId,
                                                   List<CreateActivityRequest> requests) throws SQLException {
        int id = Integer.parseInt(collectiveId);
        if (collectiveRepository.findById(id) == null) {
            throw new IllegalArgumentException("Collectivity not found: " + collectiveId);
        }

        List<ActivityResponse> responses = new ArrayList<>();
        for (CreateActivityRequest request : requests) {
            if (request.getTitle() == null || request.getTitle().isBlank()) {
                throw new IllegalArgumentException("Activity title is required.");
            }
            if (request.getActivityType() == null) {
                throw new IllegalArgumentException("Activity type is required.");
            }
            if (request.getActivityDate() == null) {
                throw new IllegalArgumentException("Activity date is required.");
            }

            Activity activity = new Activity();
            activity.setCollectiveId(id);
            activity.setTitle(request.getTitle());
            activity.setActivityType(request.getActivityType());
            activity.setActivityDate(request.getActivityDate());
            activity.setAttendanceRequired(
                    request.getAttendanceRequired() != null ? request.getAttendanceRequired() : true
            );
            activity.setTargetMembers(
                    request.getTargetMembers() != null ? request.getTargetMembers() : TargetMembersType.All
            );
            activity.setIsFederation(false);

            Activity saved = activityRepository.save(activity);
            responses.add(toResponse(saved));
        }
        return responses;
    }

    public List<ActivityResponse> getActivities(String collectiveId) throws SQLException {
        int id = Integer.parseInt(collectiveId);
        if (collectiveRepository.findById(id) == null) {
            throw new IllegalArgumentException("Collectivity not found: " + collectiveId);
        }
        List<Activity> activities = activityRepository.findByCollectiveId(id);
        List<ActivityResponse> responses = new ArrayList<>();
        for (Activity a : activities) responses.add(toResponse(a));
        return responses;
    }

    private ActivityResponse toResponse(Activity a) {
        ActivityResponse r = new ActivityResponse();
        r.setId(a.getIdActivity());
        r.setCollectiveId(a.getCollectiveId());
        r.setFederationId(a.getFederationId());
        r.setTitle(a.getTitle());
        r.setActivityType(a.getActivityType());
        r.setActivityDate(a.getActivityDate());
        r.setAttendanceRequired(a.getAttendanceRequired());
        r.setTargetMembers(a.getTargetMembers());
        r.setIsFederation(a.getIsFederation());
        return r;
    }
}