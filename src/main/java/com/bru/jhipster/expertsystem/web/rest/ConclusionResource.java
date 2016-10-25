package com.bru.jhipster.expertsystem.web.rest;

import com.bru.jhipster.expertsystem.domain.Conclusion;
import com.bru.jhipster.expertsystem.repository.ConclusionRepository;
import com.bru.jhipster.expertsystem.web.rest.util.HeaderUtil;
import com.bru.jhipster.expertsystem.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Conclusion.
 */
@RestController
@RequestMapping("/api")
public class ConclusionResource {

    private final Logger log = LoggerFactory.getLogger(ConclusionResource.class);

    @Inject
    private ConclusionRepository conclusionRepository;

    /**
     * POST  /conclusions : Create a new conclusion.
     *
     * @param conclusion the conclusion to create
     * @return the ResponseEntity with status 201 (Created) and with body the new conclusion, or with status 400 (Bad Request) if the conclusion has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/conclusions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Conclusion> createConclusion(@RequestBody Conclusion conclusion) throws URISyntaxException {
        log.debug("REST request to save Conclusion : {}", conclusion);
        if (conclusion.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("conclusion", "idexists", "A new conclusion cannot already have an ID")).body(null);
        }
        Conclusion result = conclusionRepository.save(conclusion);
        return ResponseEntity.created(new URI("/api/conclusions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("conclusion", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /conclusions : Updates an existing conclusion.
     *
     * @param conclusion the conclusion to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated conclusion,
     * or with status 400 (Bad Request) if the conclusion is not valid,
     * or with status 500 (Internal Server Error) if the conclusion couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/conclusions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Conclusion> updateConclusion(@RequestBody Conclusion conclusion) throws URISyntaxException {
        log.debug("REST request to update Conclusion : {}", conclusion);
        if (conclusion.getId() == null) {
            return createConclusion(conclusion);
        }
        Conclusion result = conclusionRepository.save(conclusion);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("conclusion", conclusion.getId().toString()))
            .body(result);
    }

    /**
     * GET  /conclusions : get all the conclusions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of conclusions in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/conclusions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Conclusion>> getAllConclusions(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Conclusions");
        Page<Conclusion> page = conclusionRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/conclusions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /conclusions/:id : get the "id" conclusion.
     *
     * @param id the id of the conclusion to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the conclusion, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/conclusions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Conclusion> getConclusion(@PathVariable Long id) {
        log.debug("REST request to get Conclusion : {}", id);
        Conclusion conclusion = conclusionRepository.findOne(id);
        return Optional.ofNullable(conclusion)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /conclusions/:id : delete the "id" conclusion.
     *
     * @param id the id of the conclusion to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/conclusions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteConclusion(@PathVariable Long id) {
        log.debug("REST request to delete Conclusion : {}", id);
        conclusionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("conclusion", id.toString())).build();
    }

}
