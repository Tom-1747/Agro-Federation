package com.example.agricultureFederation.controller;

import com.example.agricultureFederation.dto.request.CreateMemberRequest;
import com.example.agricultureFederation.dto.response.MemberResponse;
import com.example.agricultureFederation.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<?> createMembers(
            @RequestBody List<CreateMemberRequest> requests) {
        try {
            List<MemberResponse> responses = memberService.createMembers(requests);
            return ResponseEntity.status(HttpStatus.CREATED).body(responses);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error.");
        }
    }
}