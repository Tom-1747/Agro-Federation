package com.example.agricultureFederation.controller;

import com.example.agricultureFederation.dto.request.AssignCollectivityIdentityRequest;
import com.example.agricultureFederation.dto.request.CreateCollectiveRequest;
import com.example.agricultureFederation.dto.request.CreateCollectiveRequest;
import com.example.agricultureFederation.dto.response.CollectiveResponse;
import com.example.agricultureFederation.dto.response.CollectiveResponse;
import com.example.agricultureFederation.service.CollectiveService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collectivities")
public class CollectiveController {

    private final CollectiveService collectiveService;

    public CollectiveController(CollectiveService collectiveService) {
        this.collectiveService = collectiveService;
    }

    @PostMapping
    public ResponseEntity<?> createCollectivities(
            @RequestBody List<CreateCollectiveRequest> requests) {
        try {
            List<CollectiveResponse> responses = collectiveService.createCollectivities(requests);
            return ResponseEntity.status(HttpStatus.CREATED).body(responses);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCollectivity(@PathVariable String id) {
        try {
            CollectiveResponse response = collectiveService.getById(id);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error.");
        }
    }

    @PutMapping("/{id}/informations")
    public ResponseEntity<?> assignIdentity(
            @PathVariable String id,
            @RequestBody AssignCollectivityIdentityRequest request) {
        try {
            CollectiveResponse response = collectiveService.assignIdentity(id, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error.");
        }
    }
}