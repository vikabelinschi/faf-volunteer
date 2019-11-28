package md.faf.volunteer.service;

import md.faf.volunteer.domain.Status;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Status}.
 */
public interface StatusService {

    /**
     * Save a status.
     *
     * @param status the entity to save.
     * @return the persisted entity.
     */
    Status save(Status status);

    /**
     * Get all the statuses.
     *
     * @return the list of entities.
     */
    List<Status> findAll();


    /**
     * Get the "id" status.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Status> findOne(Long id);

    /**
     * Delete the "id" status.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
