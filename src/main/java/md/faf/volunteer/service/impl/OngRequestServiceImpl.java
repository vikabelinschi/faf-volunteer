package md.faf.volunteer.service.impl;

import md.faf.volunteer.service.OngRequestService;
import md.faf.volunteer.domain.OngRequest;
import md.faf.volunteer.repository.OngRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link OngRequest}.
 */
@Service
@Transactional
public class OngRequestServiceImpl implements OngRequestService {

    private final Logger log = LoggerFactory.getLogger(OngRequestServiceImpl.class);

    private final OngRequestRepository ongRequestRepository;

    public OngRequestServiceImpl(OngRequestRepository ongRequestRepository) {
        this.ongRequestRepository = ongRequestRepository;
    }

    /**
     * Save a ongRequest.
     *
     * @param ongRequest the entity to save.
     * @return the persisted entity.
     */
    @Override
    public OngRequest save(OngRequest ongRequest) {
        log.debug("Request to save OngRequest : {}", ongRequest);
        return ongRequestRepository.save(ongRequest);
    }

    /**
     * Get all the ongRequests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OngRequest> findAll(Pageable pageable) {
        log.debug("Request to get all OngRequests");
        return ongRequestRepository.findAll(pageable);
    }


    /**
     * Get one ongRequest by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<OngRequest> findOne(Long id) {
        log.debug("Request to get OngRequest : {}", id);
        return ongRequestRepository.findById(id);
    }

    /**
     * Delete the ongRequest by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete OngRequest : {}", id);
        ongRequestRepository.deleteById(id);
    }
}
