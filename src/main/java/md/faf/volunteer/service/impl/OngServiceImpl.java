package md.faf.volunteer.service.impl;

import md.faf.volunteer.service.OngService;
import md.faf.volunteer.domain.Ong;
import md.faf.volunteer.repository.OngRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Ong}.
 */
@Service
@Transactional
public class OngServiceImpl implements OngService {

    private final Logger log = LoggerFactory.getLogger(OngServiceImpl.class);

    private final OngRepository ongRepository;

    public OngServiceImpl(OngRepository ongRepository) {
        this.ongRepository = ongRepository;
    }

    /**
     * Save a ong.
     *
     * @param ong the entity to save.
     * @return the persisted entity.
     */
    @Override
    public Ong save(Ong ong) {
        log.debug("Request to save Ong : {}", ong);
        return ongRepository.save(ong);
    }

    /**
     * Get all the ongs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Ong> findAll(Pageable pageable) {
        log.debug("Request to get all Ongs");
        return ongRepository.findAll(pageable);
    }


    /**
     * Get one ong by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Ong> findOne(Long id) {
        log.debug("Request to get Ong : {}", id);
        return ongRepository.findById(id);
    }

    /**
     * Delete the ong by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ong : {}", id);
        ongRepository.deleteById(id);
    }
}
