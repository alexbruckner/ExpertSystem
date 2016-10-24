package com.bru.jhipster.expertsystem.web.rest;

import com.bru.jhipster.expertsystem.domain.convert.JAXBToDomainModelConverter;
import com.bru.jhipster.expertsystem.jaxb.Answer;
import com.bru.jhipster.expertsystem.jaxb.Conclusion;
import com.bru.jhipster.expertsystem.jaxb.Question;
import com.codahale.metrics.annotation.Timed;
import com.bru.jhipster.expertsystem.domain.ExpertSystem;

import com.bru.jhipster.expertsystem.repository.ExpertSystemRepository;
import com.bru.jhipster.expertsystem.web.rest.util.HeaderUtil;
import com.bru.jhipster.expertsystem.web.rest.util.PaginationUtil;
import org.codehaus.groovy.control.io.StringReaderSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.xml.sax.SAXException;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing ExpertSystem.
 */
@RestController
@RequestMapping("/api")
public class ExpertSystemResource {

    private final Logger log = LoggerFactory.getLogger(ExpertSystemResource.class);

    @Inject
    private ExpertSystemRepository expertSystemRepository;

    @Inject
    private JAXBToDomainModelConverter jaxbToDomainModelConverter;

    /**
     * POST  /expert-systems : Create a new expertSystem.
     *
     * @param expertSystem the expertSystem to create
     * @return the ResponseEntity with status 201 (Created) and with body the new expertSystem, or with status 400 (Bad Request) if the expertSystem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/expert-systems",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ExpertSystem> createExpertSystem(@Valid @RequestBody ExpertSystem expertSystem) throws URISyntaxException {
        log.debug("REST request to save ExpertSystem : {}", expertSystem);
        if (expertSystem.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("expertSystem", "idexists", "A new expertSystem cannot already have an ID")).body(null);
        }
        ExpertSystem result = expertSystemRepository.save(expertSystem);
        return ResponseEntity.created(new URI("/api/expert-systems/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("expertSystem", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /expert-systems : Updates an existing expertSystem.
     *
     * @param expertSystem the expertSystem to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated expertSystem,
     * or with status 400 (Bad Request) if the expertSystem is not valid,
     * or with status 500 (Internal Server Error) if the expertSystem couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/expert-systems",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ExpertSystem> updateExpertSystem(@Valid @RequestBody ExpertSystem expertSystem) throws URISyntaxException {
        log.debug("REST request to update ExpertSystem : {}", expertSystem);
        if (expertSystem.getId() == null) {
            return createExpertSystem(expertSystem);
        }
        ExpertSystem result = expertSystemRepository.save(expertSystem);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("expertSystem", expertSystem.getId().toString()))
            .body(result);
    }

    /**
     * GET  /expert-systems : get all the expertSystems.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of expertSystems in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/expert-systems",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<ExpertSystem>> getAllExpertSystems(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of ExpertSystems");
        Page<ExpertSystem> page = expertSystemRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/expert-systems");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /expert-systems/:id : get the "id" expertSystem.
     *
     * @param id the id of the expertSystem to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the expertSystem, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/expert-systems/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ExpertSystem> getExpertSystem(@PathVariable Long id) {
        log.debug("REST request to get ExpertSystem : {}", id);
        ExpertSystem expertSystem = expertSystemRepository.findOne(id);
        return Optional.ofNullable(expertSystem)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /expert-systems/:id : delete the "id" expertSystem.
     *
     * @param id the id of the expertSystem to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/expert-systems/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteExpertSystem(@PathVariable Long id) {
        log.debug("REST request to delete ExpertSystem : {}", id);
        expertSystemRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("expertSystem", id.toString())).build();
    }

    /**
     * POST  /expert-systems : Create a new expertSystem from an xml upload.
     *
     * @param xml the xml from which to create the expertSystem
     * @return the ResponseEntity with status 201 (Created) and with body the new expertSystem, or with status 400 (Bad Request) if the expertSystem has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/expert-systems/upload",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ExpertSystem> handleFileUpload(String xml) throws URISyntaxException {
        if (xml == null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("expertSystem", "invalid", "New expertSystem xml is null.")).body(null);
        }
        log.debug("REST request to upload ExpertSystem xml: {}", xml);
        ExpertSystem result;
        try {
            result = jaxbToDomainModelConverter.convertAndSave(xml);
        } catch (JAXBException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("expertSystem", "invalid", "New expertSystem is not well formed or does not comply with the xml schema.")).body(null);
        }
        return ResponseEntity.created(new URI("/api/expert-systems/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("expertSystem", result.getId().toString()))
            .body(result);
    }
}
