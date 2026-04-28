package com.example.agricultureFederation.dto.request;

import java.util.List;

public class CreateCollectiveRequest {

    private boolean federationApproval;
    private CollectiveStructureRequest structure;
    private List<String> members;
    private String location;

    public boolean isFederationApproval() { return federationApproval; }
    public void setFederationApproval(boolean federationApproval) { this.federationApproval = federationApproval; }

    public CollectiveStructureRequest getStructure() { return structure; }
    public void setStructure(CollectiveStructureRequest structure) { this.structure = structure; }

    public List<String> getMembers() { return members; }
    public void setMembers(List<String> members) { this.members = members; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public static class CollectiveStructureRequest {
        private String president;
        private String vicePresident;
        private String treasurer;
        private String secretary;

        public String getPresident() { return president; }
        public void setPresident(String president) { this.president = president; }

        public String getVicePresident() { return vicePresident; }
        public void setVicePresident(String vicePresident) { this.vicePresident = vicePresident; }

        public String getTreasurer() { return treasurer; }
        public void setTreasurer(String treasurer) { this.treasurer = treasurer; }

        public String getSecretary() { return secretary; }
        public void setSecretary(String secretary) { this.secretary = secretary; }
    }
}