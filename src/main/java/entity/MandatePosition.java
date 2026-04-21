package entity;

import java.time.LocalDate;

public class MandatePosition {
    private int mandatePositionId;
    private int memberId;
    private Integer collectiveId;
    private Integer federationId;
    private String positionLabel;
    private int civilYear;
    private LocalDate startDate;
    private LocalDate endDate;
    private int cumulatedMandates;

    public MandatePosition() {}

    public MandatePosition(int mandatePositionId, int memberId, Integer collectiveId, Integer federationId, String positionLabel, int civilYear, LocalDate startDate, LocalDate endDate, int cumulatedMandates) {
        this.mandatePositionId = mandatePositionId;
        this.memberId = memberId;
        this.collectiveId = collectiveId;
        this.federationId = federationId;
        this.positionLabel = positionLabel;
        this.civilYear = civilYear;
        this.startDate = startDate;
        this.endDate = endDate;
        this.cumulatedMandates = cumulatedMandates;
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

    public int getCivilYear() {
        return civilYear;
    }

    public void setCivilYear(int civilYear) {
        this.civilYear = civilYear;
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
}
