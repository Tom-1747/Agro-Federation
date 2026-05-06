package com.example.agricultureFederation.dto.response;

import java.math.BigDecimal;

public class MemberStatDto {

    private String memberId;
    private String firstName;
    private String lastName;
    private BigDecimal totalCollected;
    private BigDecimal potentialUnpaid;

    public MemberStatDto() {}

    public MemberStatDto(String memberId, String firstName, String lastName,
                         BigDecimal totalCollected, BigDecimal potentialUnpaid) {
        this.memberId = memberId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.totalCollected = totalCollected;
        this.potentialUnpaid = potentialUnpaid;
    }

    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public BigDecimal getTotalCollected() { return totalCollected; }
    public void setTotalCollected(BigDecimal totalCollected) { this.totalCollected = totalCollected; }

    public BigDecimal getPotentialUnpaid() { return potentialUnpaid; }
    public void setPotentialUnpaid(BigDecimal potentialUnpaid) { this.potentialUnpaid = potentialUnpaid; }
}