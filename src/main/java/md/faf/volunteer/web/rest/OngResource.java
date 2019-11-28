package md.faf.volunteer.web.rest;

import md.faf.volunteer.domain.Ong;
import md.faf.volunteer.service.OngService;
import md.faf.volunteer.web.rest.errors.BadRequestAlertException;
import md.faf.volunteer.service.dto.OngCriteria;
import md.faf.volunteer.service.OngQueryService;

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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link md.faf.volunteer.domain.Ong}.
 */
@RestController
@RequestMapping("/api")
public class OngResource {

    private final Logger log = LoggerFactory.getLogger(OngResource.class);

    private static final String ENTITY_NAME = "ong";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OngService ongService;

    private final OngQueryService ongQueryService;

    public OngResource(OngService ongService, OngQueryService ongQueryService) {
        this.ongService = ongService;
        this.ongQueryService = ongQueryService;
    }

    /**
     * {@code POST  /ongs} : Create a new ong.
     *
     * @param ong the ong to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ong, or with status {@code 400 (Bad Request)} if the ong has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ongs")
    public ResponseEntity<Ong> createOng(@Valid @RequestBody Ong ong) throws URISyntaxException {
        log.debug("REST request to save Ong : {}", ong);
        if (ong.getId() != null) {
            throw new BadRequestAlertException("A new ong cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ong result = ongService.save(ong);
        return ResponseEntity.created(new URI("/api/ongs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ongs} : Updates an existing ong.
     *
     * @param ong the ong to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ong,
     * or with status {@code 400 (Bad Request)} if the ong is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ong couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ongs")
    public ResponseEntity<Ong> updateOng(@Valid @RequestBody Ong ong) throws URISyntaxException {
        log.debug("REST request to update Ong : {}", ong);
        if (ong.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Ong result = ongService.save(ong);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ong.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /ongs} : get all the ongs.
     *

     * @param pageable the pagination information.

     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ongs in body.
     */
    @GetMapping("/ongs")
    public ResponseEntity<List<Ong>> getAllOngs(OngCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Ongs by criteria: {}", criteria);
        Page<Ong> page = ongQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
    * {@code GET  /ongs/count} : count all the ongs.
    *
    * @param criteria the criteria which the requested entities should match.
    * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
    */
    @GetMapping("/ongs/count")
    public ResponseEntity<Long> countOngs(OngCriteria criteria) {
        log.debug("REST request to count Ongs by criteria: {}", criteria);
        return ResponseEntity.ok().body(ongQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ongs/:id} : get the "id" ong.
     *
     * @param id the id of the ong to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ong, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ongs/{id}")
    public ResponseEntity<Ong> getOng(@PathVariable Long id) {
        log.debug("REST request to get Ong : {}", id);
        Optional<Ong> ong = ongService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ong);
    }

    /**
     * {@code DELETE  /ongs/:id} : delete the "id" ong.
     *
     * @param id the id of the ong to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ongs/{id}")
    public ResponseEntity<Void> deleteOng(@PathVariable Long id) {
        log.debug("REST request to delete Ong : {}", id);
        ongService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
