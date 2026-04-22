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

    // PUT /collectivities/{id}/informations
    @PutMapping("/collectivities/{id}/informations")
    public ResponseEntity<ApiResponse<CollectiveResponse>> updateInformation(
            @PathVariable String id,
            @RequestBody CollectivityInformationRequest request) {
        // TODO: implémenter via CollectiveService
        return ResponseEntity.ok(new ApiResponse<>(200, "Information updated", null));
    }

    // GET /collectivities/{id}/membershipFees
    @GetMapping("/collectivities/{id}/membershipFees")
    public ResponseEntity<ApiResponse<List<MembershipFeeResponse>>> getMembershipFees(
            @PathVariable String id) {
        // TODO: implémenter via MembershipFeeService
        return ResponseEntity.ok(new ApiResponse<>(200, "Membership fees retrieved", null));
    }

    // POST /collectivities/{id}/membershipFees
    @PostMapping("/collectivities/{id}/membershipFees")
    public ResponseEntity<ApiResponse<List<MembershipFeeResponse>>> createMembershipFees(
            @PathVariable String id,
            @RequestBody List<CreateMembershipFeeRequest> requests) {
        // TODO: implémenter via MembershipFeeService
        return ResponseEntity.ok(new ApiResponse<>(200, "Membership fees created", null));
    }

    // GET /collectivities/{id}/transactions
    @GetMapping("/collectivities/{id}/transactions")
    public ResponseEntity<ApiResponse<List<CollectivityTransactionResponse>>> getTransactions(
            @PathVariable String id,
            @RequestParam LocalDate from,
            @RequestParam LocalDate to) {
        // TODO: implémenter via TransactionService
        return ResponseEntity.ok(new ApiResponse<>(200, "Transactions retrieved", null));
    }

    // POST /members/{id}/payments
    @PostMapping("/members/{id}/payments")
    public ResponseEntity<ApiResponse<List<MemberPaymentResponse>>> createPayments(
            @PathVariable String id,
            @RequestBody List<CreateMemberPaymentRequest> requests) {
        // TODO: implémenter via MemberPaymentService
        return ResponseEntity.status(201).body(new ApiResponse<>(201, "Payments created", null));
    }
}

