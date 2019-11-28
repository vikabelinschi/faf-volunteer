package md.faf.volunteer.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import md.faf.volunteer.domain.VolunteerRequest;
import md.faf.volunteer.domain.*; // for static metamodels
import md.faf.volunteer.repository.VolunteerRequestRepository;
import md.faf.volunteer.service.dto.VolunteerRequestCriteria;

/**
 * Service for executing complex queries for {@link VolunteerRequest} entities in the database.
 * The main input is a {@link VolunteerRequestCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link VolunteerRequest} or a {@link Page} of {@link VolunteerRequest} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class VolunteerRequestQueryService extends QueryService<VolunteerRequest> {

    private final Logger log = LoggerFactory.getLogger(VolunteerRequestQueryService.class);

    private final VolunteerRequestRepository volunteerRequestRepository;

    public VolunteerRequestQueryService(VolunteerRequestRepository volunteerRequestRepository) {
        this.volunteerRequestRepository = volunteerRequestRepository;
    }

    /**
     * Return a {@link List} of {@link VolunteerRequest} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<VolunteerRequest> findByCriteria(VolunteerRequestCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<VolunteerRequest> specification = createSpecification(criteria);
        return volunteerRequestRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link VolunteerRequest} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<VolunteerRequest> findByCriteria(VolunteerRequestCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<VolunteerRequest> specification = createSpecification(criteria);
        return volunteerRequestRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(VolunteerRequestCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<VolunteerRequest> specification = createSpecification(criteria);
        return volunteerRequestRepository.count(specification);
    }

    /**
     * Function to convert {@link VolunteerRequestCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<VolunteerRequest> createSpecification(VolunteerRequestCriteria criteria) {
        Specification<VolunteerRequest> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), VolunteerRequest_.id));
            }
            if (criteria.getRegistrationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRegistrationDate(), VolunteerRequest_.registrationDate));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(VolunteerRequest_.user, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getProjectId() != null) {
                specification = specification.and(buildSpecification(criteria.getProjectId(),
                    root -> root.join(VolunteerRequest_.project, JoinType.LEFT).get(Project_.id)));
            }
        }
        return specification;
    }
}
