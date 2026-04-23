package com.example.agricultureFederation.entity;

import com.example.agricultureFederation.entity.enums.PositionLabelType;
import java.time.LocalDate;
import java.util.Objects;

public class PositionMandate {
    private Integer idPositionMandate;
    private Member member;
    private Integer memberId;
    private Collective collective;
    private Integer collectiveId;
    private Federation federation;
    private Integer federationId;
    private PositionLabelType positionLabel;
    private Boolean isUnique;
    private Boolean isElective;
    private Integer calendarYear;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer cumulatedMandates;

    public PositionMandate() {
        this.isUnique = true;
        this.isElective = true;
        this.cumulatedMandates = 1;
    }

    public PositionMandate(Integer memberId, Integer collectiveId, Integer federationId,
                           PositionLabelType positionLabel, Integer calendarYear,
                           LocalDate startDate, LocalDate endDate) {
        this.memberId = memberId;
        this.collectiveId = collectiveId;
        this.federationId = federationId;
        this.positionLabel = positionLabel;
        this.calendarYear = calendarYear;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isUnique = true;
        this.isElective = true;
        this.cumulatedMandates = 1;
    }

    public PositionMandate(Integer idPositionMandate, Member member, Collective collective,
                           Federation federation, PositionLabelType positionLabel, Boolean isUnique,
                           Boolean isElective, Integer calendarYear, LocalDate startDate,
                           LocalDate endDate, Integer cumulatedMandates) {
        this.idPositionMandate = idPositionMandate;
        this.member = member;
        this.collective = collective;
        this.federation = federation;
        this.positionLabel = positionLabel;
        this.isUnique = isUnique != null ? isUnique : true;
        this.isElective = isElective != null ? isElective : true;
        this.calendarYear = calendarYear;
        this.startDate = startDate;
        this.endDate = endDate;
        this.cumulatedMandates = cumulatedMandates != null ? cumulatedMandates : 1;

        if (member != null) this.memberId = member.getMemberId();
        if (collective != null) this.collectiveId = collective.getCollectiveId();
        if (federation != null) this.federationId = federation.getIdFederation();
    }

    public Integer getIdPositionMandate() { return idPositionMandate; }
    public void setIdPositionMandate(Integer idPositionMandate) { this.idPositionMandate = idPositionMandate; }

    public Member getMember() { return member; }
    public void setMember(Member member) {
        this.member = member;
        if (member != null) this.memberId = member.getMemberId();
    }

    public Integer getMemberId() { return memberId; }
    public void setMemberId(Integer memberId) { this.memberId = memberId; }

    public Collective getCollective() { return collective; }
    public void setCollective(Collective collective) {
        this.collective = collective;
        if (collective != null) this.collectiveId = collective.getCollectiveId();
    }

    public Integer getCollectiveId() { return collectiveId; }
    public void setCollectiveId(Integer collectiveId) { this.collectiveId = collectiveId; }

    public Federation getFederation() { return federation; }
    public void setFederation(Federation federation) {
        this.federation = federation;
        if (federation != null) this.federationId = federation.getIdFederation();
    }

    public Integer getFederationId() { return federationId; }
    public void setFederationId(Integer federationId) { this.federationId = federationId; }

    public PositionLabelType getPositionLabel() { return positionLabel; }
    public void setPositionLabel(PositionLabelType positionLabel) { this.positionLabel = positionLabel; }

    public Boolean getIsUnique() { return isUnique; }
    public void setIsUnique(Boolean isUnique) { this.isUnique = isUnique; }

    public Boolean getIsElective() { return isElective; }
    public void setIsElective(Boolean isElective) { this.isElective = isElective; }

    public Integer getCalendarYear() { return calendarYear; }
    public void setCalendarYear(Integer calendarYear) { this.calendarYear = calendarYear; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public Integer getCumulatedMandates() { return cumulatedMandates; }
    public void setCumulatedMandates(Integer cumulatedMandates) { this.cumulatedMandates = cumulatedMandates; }

    public boolean isActive() {
        LocalDate now = LocalDate.now();
        if (startDate != null && startDate.isAfter(now)) return false;
        if (endDate != null && endDate.isBefore(now)) return false;
        return true;
    }

    public boolean isCollectiveMandate() {
        return collectiveId != null && federationId == null;
    }

    public boolean isFederationMandate() {
        return federationId != null && collectiveId == null;
    }

    public boolean isPresident() {
        return positionLabel == PositionLabelType.President;
    }

    public boolean isVicePresident() {
        return positionLabel == PositionLabelType.Vice_President;
    }

    public boolean isTreasurer() {
        return positionLabel == PositionLabelType.Treasurer;
    }

    public boolean isSecretary() {
        return positionLabel == PositionLabelType.Secretary;
    }

    public boolean isConfirmedMember() {
        return positionLabel == PositionLabelType.Confirmed_Member;
    }

    public boolean isRequiredPosition() {
        return isPresident() || isVicePresident() || isTreasurer() || isSecretary();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PositionMandate that = (PositionMandate) o;
        return Objects.equals(idPositionMandate, that.idPositionMandate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPositionMandate);
    }

    @Override
    public String toString() {
        return "PositionMandate{" +
                "idPositionMandate=" + idPositionMandate +
                ", positionLabel=" + positionLabel +
                ", calendarYear=" + calendarYear +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}