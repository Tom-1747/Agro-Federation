package dto.request;

import java.time.LocalDate;
import java.util.List;

public class CreateMemberRequest {

    private int collectiveId;
    private Integer jobId;
    private String lastName;
    private String firstName;
    private LocalDate birthDate;
    private String gender;
    private String address;
    private String phone;
    private String email;
    private List<SponsorRequest> sponsors;
    private double membershipFee;
    private double annualContribution;

    public int getCollectiveId() { return collectiveId; }
    public void setCollectiveId(int collectiveId) { this.collectiveId = collectiveId; }

    public Integer getJobId() { return jobId; }
    public void setJobId(Integer jobId) { this.jobId = jobId; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<SponsorRequest> getSponsors() { return sponsors; }
    public void setSponsors(List<SponsorRequest> sponsors) { this.sponsors = sponsors; }

    public double getMembershipFee() { return membershipFee; }
    public void setMembershipFee(double membershipFee) { this.membershipFee = membershipFee; }

    public double getAnnualContribution() { return annualContribution; }
    public void setAnnualContribution(double annualContribution) { this.annualContribution = annualContribution; }
}
