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

import md.faf.volunteer.domain.Ong;
import md.faf.volunteer.domain.*; // for static metamodels
import md.faf.volunteer.repository.OngRepository;
import md.faf.volunteer.service.dto.OngCriteria;

/**
 * Service for executing complex queries for {@link Ong} entities in the database.
 * The main input is a {@link OngCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Ong} or a {@link Page} of {@link Ong} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OngQueryService extends QueryService<Ong> {

    private final Logger log = LoggerFactory.getLogger(OngQueryService.class);

    private final OngRepository ongRepository;

    public OngQueryService(OngRepository ongRepository) {
        this.ongRepository = ongRepository;
    }

    /**
     * Return a {@link List} of {@link Ong} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Ong> findByCriteria(OngCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Ong> specification = createSpecification(criteria);
        return ongRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Ong} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Ong> findByCriteria(OngCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Ong> specification = createSpecification(criteria);
        return ongRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OngCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Ong> specification = createSpecification(criteria);
        return ongRepository.count(specification);
    }

    /**
     * Function to convert {@link OngCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Ong> createSpecification(OngCriteria criteria) {
        Specification<Ong> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Ong_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Ong_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Ong_.description));
            }
            if (criteria.getIdno() != null) {
                specification = specification.and(buildStringSpecification(criteria.getIdno(), Ong_.idno));
            }
            if (criteria.getEmail() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmail(), Ong_.email));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), Ong_.address));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), Ong_.phone));
            }
            if (criteria.getProjectId() != null) {
                specification = specification.and(buildSpecification(criteria.getProjectId(),
                    root -> root.join(Ong_.projects, JoinType.LEFT).get(Project_.id)));
            }
        }
        return specification;
    }
}
