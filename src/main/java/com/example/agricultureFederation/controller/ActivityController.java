package com.example.agricultureFederation.controller;

import com.example.agricultureFederation.dto.request.CreateActivityRequest;
import com.example.agricultureFederation.dto.response.ActivityResponse;
import com.example.agricultureFederation.service.ActivityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collectivities")
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @PostMapping("/{id}/activities")
    public ResponseEntity<?> createActivities(
            @PathVariable String id,
            @RequestBody List<CreateActivityRequest> requests) {
        try {
            List<ActivityResponse> responses = activityService.createActivities(id, requests);
            return ResponseEntity.status(HttpStatus.CREATED).body(responses);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error.");
        }
    }

    @GetMapping("/{id}/activities")
    public ResponseEntity<?> getActivities(@PathVariable String id) {
        try {
            List<ActivityResponse> responses = activityService.getActivities(id);
            return ResponseEntity.ok(responses);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error.");
        }
    }
}