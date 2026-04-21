package service;

import dto.request.CreateCollectiveRequest;
import dto.response.CollectiveResponse;
import entity.Collective;
import repository.CollectiveRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Year;

public class CollectiveService {

    private final CollectiveRepository collectiveRepository;

    public CollectiveService(CollectiveRepository collectiveRepository) {
        this.collectiveRepository = collectiveRepository;
    }

    public CollectiveResponse createCollective(CreateCollectiveRequest request) throws SQLException {

        int activeMembers = collectiveRepository.countActiveMembers(request.getFederationId());
        if (activeMembers < 10) {
            throw new IllegalStateException(
                    "The collective must have at least 10 registered members. Current: " + activeMembers
            );
        }

        int seniorMembers = collectiveRepository.countMembersWithSixMonthsSeniority(request.getFederationId());
        if (seniorMembers < 5) {
            throw new IllegalStateException(
                    "At least 5 members must have 6 months seniority in the federation. Current: " + seniorMembers
            );
        }

        int currentYear = Year.now().getValue();
        boolean positionsFilled = collectiveRepository.hasAllSpecificPositionsFilled(request.getFederationId(), currentYear);
        if (!positionsFilled) {
            throw new IllegalStateException(
                    "All specific positions (President, Vice-President, Treasurer, Secretary) must be filled."
            );
        }

        if (request.getLocation() == null || request.getLocation().isBlank()) {
            throw new IllegalArgumentException("Location is required to open a new collective.");
        }
        if (request.getSpecialityId() == null) {
            throw new IllegalArgumentException("Agricultural speciality is required to open a new collective.");
        }

        Collective collective = new Collective(
                request.getFederationId(),
                request.getSpecialityId(),
                request.getBranchId(),
                request.getName(),
                request.getLocation(),
                request.getPhone(),
                request.getCreationDate() != null ? request.getCreationDate() : LocalDate.now()
        );

        Collective saved = collectiveRepository.save(collective);
        return toResponse(saved);
    }

    private CollectiveResponse toResponse(Collective collective) {
        CollectiveResponse response = new CollectiveResponse();
        response.setCollectiveId(collective.getCollectiveId());
        response.setFederationId(collective.getFederationId());
        response.setSpecialityId(collective.getSpecialityId());
        response.setBranchId(collective.getBranchId());
        response.setName(collective.getName());
        response.setLocation(collective.getLocation());
        response.setPhone(collective.getPhone());
        response.setCreationDate(collective.getCreationDate());
        return response;
    }
}