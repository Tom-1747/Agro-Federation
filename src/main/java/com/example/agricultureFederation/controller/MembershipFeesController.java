package com.example.agricultureFederation.controller;

import com.example.agricultureFederation.dto.request.CreateMembershipFeeRequest;
import com.example.agricultureFederation.dto.response.ApiResponse;
import com.example.agricultureFederation.dto.response.MembershipFeeResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MembershipFeesController {

    private final com.example.agricultureFederation.service.MembershipFeeService membershipFeeService;

    public MembershipFeesController(com.example.agricultureFederation.service.MembershipFeeService membershipFeeService) {
        this.membershipFeeService = membershipFeeService;
    }

    @GetMapping("/collectivities/{id}/membershipFees")
    public ResponseEntity<ApiResponse<List<MembershipFeeResponse>>> getMembershipFees(
            @PathVariable String id) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Membership fees retrieved", null));
    }

    @PostMapping("/collectivities/{id}/membershipFees")
    public ResponseEntity<ApiResponse<List<MembershipFeeResponse>>> createMembershipFees(
            @PathVariable String id,
            @RequestBody List<CreateMembershipFeeRequest> requests) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Membership fees created", null));
    }
}