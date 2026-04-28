package com.example.agricultureFederation.dto.response;

public class CollectivityStructureResponse {

    private MemberResponse president;
    private MemberResponse vicePresident;
    private MemberResponse treasurer;
    private MemberResponse secretary;

    public MemberResponse getPresident() { return president; }
    public void setPresident(MemberResponse president) { this.president = president; }

    public MemberResponse getVicePresident() { return vicePresident; }
    public void setVicePresident(MemberResponse vicePresident) { this.vicePresident = vicePresident; }

    public MemberResponse getTreasurer() { return treasurer; }
    public void setTreasurer(MemberResponse treasurer) { this.treasurer = treasurer; }

    public MemberResponse getSecretary() { return secretary; }
    public void setSecretary(MemberResponse secretary) { this.secretary = secretary; }
}