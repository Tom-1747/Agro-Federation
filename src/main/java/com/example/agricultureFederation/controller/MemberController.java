package com.example.agricultureFederation.controller;

import com.example.agricultureFederation.dto.request.CreateMemberRequest;
import com.example.agricultureFederation.dto.response.ApiResponse;
import com.example.agricultureFederation.dto.response.MemberResponse;
import com.example.agricultureFederation.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {

        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<MemberResponse>> registerMember(
            @RequestBody CreateMemberRequest request) {
        try {
            MemberResponse response = memberService.registerMember(request);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ApiResponse<>(201, "Member registered successfully.", response));
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