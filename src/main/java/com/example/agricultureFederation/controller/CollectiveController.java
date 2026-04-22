package com.example.agricultureFederation.controller;

import com.example.agricultureFederation.dto.request.CreateCollectiveRequest;
import com.example.agricultureFederation.dto.response.ApiResponse;
import com.example.agricultureFederation.dto.response.CollectiveResponse;
import com.example.agricultureFederation.service.CollectiveService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/collectivities")
public class CollectiveController {

    private final CollectiveService collectiveService;

    public CollectiveController(CollectiveService collectiveService) {
        this.collectiveService = collectiveService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<CollectiveResponse>> createCollective(
            @RequestBody CreateCollectiveRequest request) {
        try {
            CollectiveResponse response = collectiveService.createCollective(request);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(201, "Collective created successfully.", response));
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(400, e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(500, "An unexpected error occurred.", null));
        }
    }
}