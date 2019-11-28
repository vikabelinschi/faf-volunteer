package md.faf.volunteer.service.impl;

import md.faf.volunteer.service.VolunteerRequestService;
import md.faf.volunteer.domain.VolunteerRequest;
import md.faf.volunteer.repository.VolunteerRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link VolunteerRequest}.
 */
@Service
@Transactional
public class VolunteerRequestServiceImpl implements VolunteerRequestService {

    private final Logger log = LoggerFactory.getLogger(VolunteerRequestServiceImpl.class);

    private final VolunteerRequestRepository volunteerRequestRepository;

    public VolunteerRequestServiceImpl(VolunteerRequestRepository volunteerRequestRepository) {
        this.volunteerRequestRepository = volunteerRequestRepository;
    }

    /**
     * Save a volunteerRequest.
     *
     * @param volunteerRequest the entity to save.
     * @return the persisted entity.
     */
    @Override
    public VolunteerRequest save(VolunteerRequest volunteerRequest) {
        log.debug("Request to save VolunteerRequest : {}", volunteerRequest);
        return volunteerRequestRepository.save(volunteerRequest);
    }

    /**
     * Get all the volunteerRequests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<VolunteerRequest> findAll(Pageable pageable) {
        log.debug("Request to get all VolunteerRequests");
        return volunteerRequestRepository.findAll(pageable);
    }


    /**
     * Get one volunteerRequest by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<VolunteerRequest> findOne(Long id) {
        log.debug("Request to get VolunteerRequest : {}", id);
        return volunteerRequestRepository.findById(id);
    }

    /**
     * Delete the volunteerRequest by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete VolunteerRequest : {}", id);
        volunteerRequestRepository.deleteById(id);
    }
}
