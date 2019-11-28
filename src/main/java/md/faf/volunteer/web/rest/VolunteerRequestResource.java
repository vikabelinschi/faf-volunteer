package md.faf.volunteer.web.rest;

import md.faf.volunteer.domain.VolunteerRequest;
import md.faf.volunteer.service.VolunteerRequestService;
import md.faf.volunteer.web.rest.errors.BadRequestAlertException;
import md.faf.volunteer.service.dto.VolunteerRequestCriteria;
import md.faf.volunteer.service.VolunteerRequestQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link md.faf.volunteer.domain.VolunteerRequest}.
 */
@RestController
@RequestMapping("/api")
public class VolunteerRequestResource {

    private final Logger log = LoggerFactory.getLogger(VolunteerRequestResource.class);

    private static final String ENTITY_NAME = "volunteerRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final VolunteerRequestService volunteerRequestService;

    private final VolunteerRequestQueryService volunteerRequestQueryService;

    public VolunteerRequestResource(VolunteerRequestService volunteerRequestService, VolunteerRequestQueryService volunteerRequestQueryService) {
        this.volunteerRequestService = volunteerRequestService;
        this.volunteerRequestQueryService = volunteerRequestQueryService;
    }

    /**
     * {@code POST  /volunteer-requests} : Create a new volunteerRequest.
     *
     * @param volunteerRequest the volunteerRequest to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new volunteerRequest, or with status {@code 400 (Bad Request)} if the volunteerRequest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/volunteer-requests")
    public ResponseEntity<VolunteerRequest> createVolunteerRequest(@RequestBody VolunteerRequest volunteerRequest) throws URISyntaxException {
        log.debug("REST request to save VolunteerRequest : {}", volunteerRequest);
        if (volunteerRequest.getId() != null) {
            throw new BadRequestAlertException("A new volunteerRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        VolunteerRequest result = volunteerRequestService.save(volunteerRequest);
        return ResponseEntity.created(new URI("/api/volunteer-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /volunteer-requests} : Updates an existing volunteerRequest.
     *
     * @param volunteerRequest the volunteerRequest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated volunteerRequest,
     * or with status {@code 400 (Bad Request)} if the volunteerRequest is not valid,
     * or with status {@code 500 (Internal Server Error)} if the volunteerRequest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/volunteer-requests")
    public ResponseEntity<VolunteerRequest> updateVolunteerRequest(@RequestBody VolunteerRequest volunteerRequest) throws URISyntaxException {
        log.debug("REST request to update VolunteerRequest : {}", volunteerRequest);
        if (volunteerRequest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        VolunteerRequest result = volunteerRequestService.save(volunteerRequest);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, volunteerRequest.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /volunteer-requests} : get all the volunteerRequests.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of volunteerRequests in body.
     */
    @GetMapping("/volunteer-requests")
    public ResponseEntity<List<VolunteerRequest>> getAllVolunteerRequests(VolunteerRequestCriteria criteria, Pageable pageable) {
        log.debug("REST request to get VolunteerRequests by criteria: {}", criteria);
        Page<VolunteerRequest> page = volunteerRequestQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /volunteer-requests/count} : count all the volunteerRequests.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/volunteer-requests/count")
    public ResponseEntity<Long> countVolunteerRequests(VolunteerRequestCriteria criteria) {
        log.debug("REST request to count VolunteerRequests by criteria: {}", criteria);
        return ResponseEntity.ok().body(volunteerRequestQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /volunteer-requests/:id} : get the "id" volunteerRequest.
     *
     * @param id the id of the volunteerRequest to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the volunteerRequest, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/volunteer-requests/{id}")
    public ResponseEntity<VolunteerRequest> getVolunteerRequest(@PathVariable Long id) {
        log.debug("REST request to get VolunteerRequest : {}", id);
        Optional<VolunteerRequest> volunteerRequest = volunteerRequestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(volunteerRequest);
    }

    /**
     * {@code DELETE  /volunteer-requests/:id} : delete the "id" volunteerRequest.
     *
     * @param id the id of the volunteerRequest to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/volunteer-requests/{id}")
    public ResponseEntity<Void> deleteVolunteerRequest(@PathVariable Long id) {
        log.debug("REST request to delete VolunteerRequest : {}", id);
        volunteerRequestService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
