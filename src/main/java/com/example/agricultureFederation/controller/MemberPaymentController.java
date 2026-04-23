package com.example.agricultureFederation.controller;

import com.example.agricultureFederation.dto.request.CreateMemberPaymentRequest;
import com.example.agricultureFederation.dto.response.MemberPaymentResponse;
import com.example.agricultureFederation.service.MemberPaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberPaymentController {

    private final MemberPaymentService memberPaymentService;

    public MemberPaymentController(MemberPaymentService memberPaymentService) {
        this.memberPaymentService = memberPaymentService;
    }

    @PostMapping("/{id}/payments")
    public ResponseEntity<?> createPayments(
            @PathVariable String id,
            @RequestBody List<CreateMemberPaymentRequest> requests) {
        try {
            List<MemberPaymentResponse> responses = memberPaymentService.createPayments(id, requests);
            return ResponseEntity.status(HttpStatus.CREATED).body(responses);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error.");
        }
    }
}