package com.example.agricultureFederation.entity;

import java.time.LocalDate;

public class MandatePosition {
    private int mandatePositionId;
    private int memberId;
    private Integer collectiveId;
    private Integer federationId;
    private String positionLabel;
    private int calendarYear;
    private LocalDate startDate;
    private LocalDate endDate;
    private int cumulatedMandates;

    public MandatePosition() {}

    public MandatePosition(int memberId, Integer collectiveId, Integer federationId,
                           String positionLabel, int calendarYear, LocalDate startDate, LocalDate endDate) {
        this.memberId = memberId;
        this.collectiveId = collectiveId;
        this.federationId = federationId;
        this.positionLabel = positionLabel;
        this.calendarYear = calendarYear;
        this.startDate = startDate;
        this.endDate = endDate;
        this.cumulatedMandates = 0;
    }

    public MandatePosition(int mandatePositionId, int memberId, Integer collectiveId,
                           Integer federationId, String positionLabel, int calendarYear,
                           LocalDate startDate, LocalDate endDate, int cumulatedMandates) {
        this.mandatePositionId = mandatePositionId;
        this.memberId = memberId;
        this.collectiveId = collectiveId;
        this.federationId = federationId;
        this.positionLabel = positionLabel;
        this.calendarYear = calendarYear;
        this.startDate = startDate;
        this.endDate = endDate;
        this.cumulatedMandates = cumulatedMandates;
    }

    public static MandatePosition collectiveMandate(int memberId, int collectiveId,
                                                    String positionLabel, int calendarYear) {
        MandatePosition mandate = new MandatePosition();
        mandate.setMemberId(memberId);
        mandate.setCollectiveId(collectiveId);
        mandate.setPositionLabel(positionLabel);
        mandate.setCalendarYear(calendarYear);
        mandate.setStartDate(LocalDate.of(calendarYear, 1, 1));
        mandate.setEndDate(LocalDate.of(calendarYear, 12, 31));
        return mandate;
    }

    public static MandatePosition federationMandate(int memberId, int federationId,
                                                    String positionLabel, int calendarYear) {
        MandatePosition mandate = new MandatePosition();
        mandate.setMemberId(memberId);
        mandate.setFederationId(federationId);
        mandate.setPositionLabel(positionLabel);
        mandate.setCalendarYear(calendarYear);
        mandate.setStartDate(LocalDate.of(calendarYear, 1, 1));
        mandate.setEndDate(LocalDate.of(calendarYear, 12, 31));
        return mandate;
    }

    public int getMandatePositionId() {
        return mandatePositionId;
    }

    public void setMandatePositionId(int mandatePositionId) {
        this.mandatePositionId = mandatePositionId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public Integer getCollectiveId() {
        return collectiveId;
    }

    public void setCollectiveId(Integer collectiveId) {
        this.collectiveId = collectiveId;
    }

    public Integer getFederationId() {
        return federationId;
    }

    public void setFederationId(Integer federationId) {
        this.federationId = federationId;
    }

    public String getPositionLabel() {
        return positionLabel;
    }

    public void setPositionLabel(String positionLabel) {
        this.positionLabel = positionLabel;
    }

    public int getCalendarYear() {
        return calendarYear;
    }

    public void setCalendarYear(int calendarYear) {
        this.calendarYear = calendarYear;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getCumulatedMandates() {
        return cumulatedMandates;
    }

    public void setCumulatedMandates(int cumulatedMandates) {
        this.cumulatedMandates = cumulatedMandates;
    }

    public boolean isActive() {
        LocalDate now = LocalDate.now();
        if (startDate != null && startDate.isAfter(now)) return false;
        if (endDate != null && endDate.isBefore(now)) return false;
        return true;
    }

    public boolean isPresident() {
        return "President".equalsIgnoreCase(positionLabel);
    }

    public boolean isVicePresident() {
        return "Vice President".equalsIgnoreCase(positionLabel);
    }

    public boolean isTreasurer() {
        return "Treasurer".equalsIgnoreCase(positionLabel);
    }

    public boolean isSecretary() {
        return "Secretary".equalsIgnoreCase(positionLabel);
    }

    public boolean isConfirmedMember() {
        return "Confirmed Member".equalsIgnoreCase(positionLabel);
    }

    public boolean isRequiredPosition() {
        return isPresident() || isVicePresident() || isTreasurer() || isSecretary();
    }

    @Override
    public String toString() {
        return "MandatePosition{" +
                "mandatePositionId=" + mandatePositionId +
                ", memberId=" + memberId +
                ", positionLabel='" + positionLabel + '\'' +
                ", calendarYear=" + calendarYear +
                '}';
    }
}