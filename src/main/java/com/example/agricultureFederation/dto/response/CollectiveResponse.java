package com.example.agricultureFederation.dto.response;

import java.util.List;

public class CollectiveResponse {

    private String id;
    private String name;
    private String number;
    private String location;
    private CollectivityStructureResponse structure;
    private List<MemberResponse> members;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getNumber() { return number; }
    public void setNumber(String number) { this.number = number; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public CollectivityStructureResponse getStructure() { return structure; }
    public void setStructure(CollectivityStructureResponse structure) { this.structure = structure; }

    public List<MemberResponse> getMembers() { return members; }
    public void setMembers(List<MemberResponse> members) { this.members = members; }
}