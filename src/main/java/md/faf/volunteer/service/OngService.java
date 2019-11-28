package md.faf.volunteer.service;

import md.faf.volunteer.domain.Ong;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link Ong}.
 */
public interface OngService {

    /**
     * Save a ong.
     *
     * @param ong the entity to save.
     * @return the persisted entity.
     */
    Ong save(Ong ong);

    /**
     * Get all the ongs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Ong> findAll(Pageable pageable);


    /**
     * Get the "id" ong.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Ong> findOne(Long id);

    /**
     * Delete the "id" ong.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
