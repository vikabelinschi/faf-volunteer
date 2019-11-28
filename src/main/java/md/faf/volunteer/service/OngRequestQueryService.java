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

import md.faf.volunteer.domain.OngRequest;
import md.faf.volunteer.domain.*; // for static metamodels
import md.faf.volunteer.repository.OngRequestRepository;
import md.faf.volunteer.service.dto.OngRequestCriteria;

/**
 * Service for executing complex queries for {@link OngRequest} entities in the database.
 * The main input is a {@link OngRequestCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OngRequest} or a {@link Page} of {@link OngRequest} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OngRequestQueryService extends QueryService<OngRequest> {

    private final Logger log = LoggerFactory.getLogger(OngRequestQueryService.class);

    private final OngRequestRepository ongRequestRepository;

    public OngRequestQueryService(OngRequestRepository ongRequestRepository) {
        this.ongRequestRepository = ongRequestRepository;
    }

    /**
     * Return a {@link List} of {@link OngRequest} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OngRequest> findByCriteria(OngRequestCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<OngRequest> specification = createSpecification(criteria);
        return ongRequestRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link OngRequest} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OngRequest> findByCriteria(OngRequestCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OngRequest> specification = createSpecification(criteria);
        return ongRequestRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OngRequestCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<OngRequest> specification = createSpecification(criteria);
        return ongRequestRepository.count(specification);
    }

    /**
     * Function to convert {@link OngRequestCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OngRequest> createSpecification(OngRequestCriteria criteria) {
        Specification<OngRequest> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), OngRequest_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), OngRequest_.name));
            }
            if (criteria.getIdno() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdno(), OngRequest_.idno));
            }
            if (criteria.getRegistrationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getRegistrationDate(), OngRequest_.registrationDate));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(OngRequest_.user, JoinType.LEFT).get(User_.id)));
            }
        }
        return specification;
    }
}
