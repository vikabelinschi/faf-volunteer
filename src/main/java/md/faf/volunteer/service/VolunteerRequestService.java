package md.faf.volunteer.service;

import md.faf.volunteer.domain.VolunteerRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link VolunteerRequest}.
 */
public interface VolunteerRequestService {

    /**
     * Save a volunteerRequest.
     *
     * @param volunteerRequest the entity to save.
     * @return the persisted entity.
     */
    VolunteerRequest save(VolunteerRequest volunteerRequest);

    /**
     * Get all the volunteerRequests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<VolunteerRequest> findAll(Pageable pageable);


    /**
     * Get the "id" volunteerRequest.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<VolunteerRequest> findOne(Long id);

    /**
     * Delete the "id" volunteerRequest.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
