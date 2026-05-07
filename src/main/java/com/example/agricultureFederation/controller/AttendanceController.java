package com.example.agricultureFederation.controller;

import com.example.agricultureFederation.dto.request.CreateAttendanceRequest;
import com.example.agricultureFederation.dto.response.AttendanceResponse;
import com.example.agricultureFederation.service.AttendanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collectivities")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("/{id}/activities/{activityId}/attendance")
    public ResponseEntity<?> createAttendances(
            @PathVariable String id,
            @PathVariable String activityId,
            @RequestBody List<CreateAttendanceRequest> requests) {
        try {
            List<AttendanceResponse> responses =
                    attendanceService.createAttendances(id, activityId, requests);
            return ResponseEntity.status(HttpStatus.CREATED).body(responses);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error.");
        }
    }

    @GetMapping("/{id}/activities/{activityId}/attendance")
    public ResponseEntity<?> getAttendances(
            @PathVariable String id,
            @PathVariable String activityId) {
        try {
            List<AttendanceResponse> responses =
                    attendanceService.getAttendances(id, activityId);
            return ResponseEntity.ok(responses);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error.");
        }
    }
}