package md.faf.volunteer.service;

import md.faf.volunteer.domain.OngRequest;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link OngRequest}.
 */
public interface OngRequestService {

    /**
     * Save a ongRequest.
     *
     * @param ongRequest the entity to save.
     * @return the persisted entity.
     */
    OngRequest save(OngRequest ongRequest);

    /**
     * Get all the ongRequests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OngRequest> findAll(Pageable pageable);


    /**
     * Get the "id" ongRequest.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OngRequest> findOne(Long id);

    /**
     * Delete the "id" ongRequest.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
