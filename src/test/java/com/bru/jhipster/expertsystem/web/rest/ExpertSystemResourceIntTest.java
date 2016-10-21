package com.bru.jhipster.expertsystem.web.rest;

import com.bru.jhipster.expertsystem.ExpertSystemApp;

import com.bru.jhipster.expertsystem.domain.ExpertSystem;
import com.bru.jhipster.expertsystem.repository.ExpertSystemRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the ExpertSystemResource REST controller.
 *
 * @see ExpertSystemResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ExpertSystemApp.class)
public class ExpertSystemResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";
    private static final String DEFAULT_XML = "AAAAA";
    private static final String UPDATED_XML = "BBBBB";

    @Inject
    private ExpertSystemRepository expertSystemRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restExpertSystemMockMvc;

    private ExpertSystem expertSystem;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ExpertSystemResource expertSystemResource = new ExpertSystemResource();
        ReflectionTestUtils.setField(expertSystemResource, "expertSystemRepository", expertSystemRepository);
        this.restExpertSystemMockMvc = MockMvcBuilders.standaloneSetup(expertSystemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExpertSystem createEntity(EntityManager em) {
        ExpertSystem expertSystem = new ExpertSystem()
                .title(DEFAULT_TITLE)
                .xml(DEFAULT_XML);
        return expertSystem;
    }

    @Before
    public void initTest() {
        expertSystem = createEntity(em);
    }

    @Test
    @Transactional
    public void createExpertSystem() throws Exception {
        int databaseSizeBeforeCreate = expertSystemRepository.findAll().size();

        // Create the ExpertSystem

        restExpertSystemMockMvc.perform(post("/api/expert-systems")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(expertSystem)))
                .andExpect(status().isCreated());

        // Validate the ExpertSystem in the database
        List<ExpertSystem> expertSystems = expertSystemRepository.findAll();
        assertThat(expertSystems).hasSize(databaseSizeBeforeCreate + 1);
        ExpertSystem testExpertSystem = expertSystems.get(expertSystems.size() - 1);
        assertThat(testExpertSystem.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testExpertSystem.getXml()).isEqualTo(DEFAULT_XML);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = expertSystemRepository.findAll().size();
        // set the field null
        expertSystem.setTitle(null);

        // Create the ExpertSystem, which fails.

        restExpertSystemMockMvc.perform(post("/api/expert-systems")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(expertSystem)))
                .andExpect(status().isBadRequest());

        List<ExpertSystem> expertSystems = expertSystemRepository.findAll();
        assertThat(expertSystems).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkXmlIsRequired() throws Exception {
        int databaseSizeBeforeTest = expertSystemRepository.findAll().size();
        // set the field null
        expertSystem.setXml(null);

        // Create the ExpertSystem, which fails.

        restExpertSystemMockMvc.perform(post("/api/expert-systems")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(expertSystem)))
                .andExpect(status().isBadRequest());

        List<ExpertSystem> expertSystems = expertSystemRepository.findAll();
        assertThat(expertSystems).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllExpertSystems() throws Exception {
        // Initialize the database
        expertSystemRepository.saveAndFlush(expertSystem);

        // Get all the expertSystems
        restExpertSystemMockMvc.perform(get("/api/expert-systems?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(expertSystem.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].xml").value(hasItem(DEFAULT_XML.toString())));
    }

    @Test
    @Transactional
    public void getExpertSystem() throws Exception {
        // Initialize the database
        expertSystemRepository.saveAndFlush(expertSystem);

        // Get the expertSystem
        restExpertSystemMockMvc.perform(get("/api/expert-systems/{id}", expertSystem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(expertSystem.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.xml").value(DEFAULT_XML.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingExpertSystem() throws Exception {
        // Get the expertSystem
        restExpertSystemMockMvc.perform(get("/api/expert-systems/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateExpertSystem() throws Exception {
        // Initialize the database
        expertSystemRepository.saveAndFlush(expertSystem);
        int databaseSizeBeforeUpdate = expertSystemRepository.findAll().size();

        // Update the expertSystem
        ExpertSystem updatedExpertSystem = expertSystemRepository.findOne(expertSystem.getId());
        updatedExpertSystem
                .title(UPDATED_TITLE)
                .xml(UPDATED_XML);

        restExpertSystemMockMvc.perform(put("/api/expert-systems")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedExpertSystem)))
                .andExpect(status().isOk());

        // Validate the ExpertSystem in the database
        List<ExpertSystem> expertSystems = expertSystemRepository.findAll();
        assertThat(expertSystems).hasSize(databaseSizeBeforeUpdate);
        ExpertSystem testExpertSystem = expertSystems.get(expertSystems.size() - 1);
        assertThat(testExpertSystem.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testExpertSystem.getXml()).isEqualTo(UPDATED_XML);
    }

    @Test
    @Transactional
    public void deleteExpertSystem() throws Exception {
        // Initialize the database
        expertSystemRepository.saveAndFlush(expertSystem);
        int databaseSizeBeforeDelete = expertSystemRepository.findAll().size();

        // Get the expertSystem
        restExpertSystemMockMvc.perform(delete("/api/expert-systems/{id}", expertSystem.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ExpertSystem> expertSystems = expertSystemRepository.findAll();
        assertThat(expertSystems).hasSize(databaseSizeBeforeDelete - 1);
    }
}
