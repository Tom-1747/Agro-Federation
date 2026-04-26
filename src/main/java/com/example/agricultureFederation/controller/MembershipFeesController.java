package com.example.agricultureFederation.controller;

import com.example.agricultureFederation.dto.request.CollectivityInformationRequest;
import com.example.agricultureFederation.dto.request.CreateMemberPaymentRequest;
import com.example.agricultureFederation.dto.request.CreateMembershipFeeRequest;
import com.example.agricultureFederation.dto.response.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class MembershipFeesController {

    @PutMapping("/collectivities/{id}/informations")
    public ResponseEntity<ApiResponse<CollectivityResponse>> updateInformation(
            @PathVariable String id,
            @RequestBody CollectivityInformationRequest request) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Information updated", null));
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

    @GetMapping("/collectivities/{id}/transactions")
    public ResponseEntity<ApiResponse<List<CollectivityTransactionResponse>>> getTransactions(
            @PathVariable String id,
            @RequestParam LocalDate from,
            @RequestParam LocalDate to) {
        return ResponseEntity.ok(new ApiResponse<>(200, "Transactions retrieved", null));
    }

    @PostMapping("/members/{id}/payments")
    public ResponseEntity<ApiResponse<List<MemberPaymentResponse>>> createPayments(
            @PathVariable String id,
            @RequestBody List<CreateMemberPaymentRequest> requests) {
        return ResponseEntity.status(201).body(new ApiResponse<>(201, "Payments created", null));
    }
}

