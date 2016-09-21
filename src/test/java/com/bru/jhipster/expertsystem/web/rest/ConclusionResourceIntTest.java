package com.bru.jhipster.expertsystem.web.rest;

import com.bru.jhipster.expertsystem.ExpertSystemApp;

import com.bru.jhipster.expertsystem.domain.Conclusion;
import com.bru.jhipster.expertsystem.repository.ConclusionRepository;

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
 * Test class for the ConclusionResource REST controller.
 *
 * @see ConclusionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ExpertSystemApp.class)
public class ConclusionResourceIntTest {

    private static final String DEFAULT_TEXT = "AAAAA";
    private static final String UPDATED_TEXT = "BBBBB";

    @Inject
    private ConclusionRepository conclusionRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restConclusionMockMvc;

    private Conclusion conclusion;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ConclusionResource conclusionResource = new ConclusionResource();
        ReflectionTestUtils.setField(conclusionResource, "conclusionRepository", conclusionRepository);
        this.restConclusionMockMvc = MockMvcBuilders.standaloneSetup(conclusionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Conclusion createEntity(EntityManager em) {
        Conclusion conclusion = new Conclusion()
                .text(DEFAULT_TEXT);
        return conclusion;
    }

    @Before
    public void initTest() {
        conclusion = createEntity(em);
    }

    @Test
    @Transactional
    public void createConclusion() throws Exception {
        int databaseSizeBeforeCreate = conclusionRepository.findAll().size();

        // Create the Conclusion

        restConclusionMockMvc.perform(post("/api/conclusions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(conclusion)))
                .andExpect(status().isCreated());

        // Validate the Conclusion in the database
        List<Conclusion> conclusions = conclusionRepository.findAll();
        assertThat(conclusions).hasSize(databaseSizeBeforeCreate + 1);
        Conclusion testConclusion = conclusions.get(conclusions.size() - 1);
        assertThat(testConclusion.getText()).isEqualTo(DEFAULT_TEXT);
    }

    @Test
    @Transactional
    public void getAllConclusions() throws Exception {
        // Initialize the database
        conclusionRepository.saveAndFlush(conclusion);

        // Get all the conclusions
        restConclusionMockMvc.perform(get("/api/conclusions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(conclusion.getId().intValue())))
                .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())));
    }

    @Test
    @Transactional
    public void getConclusion() throws Exception {
        // Initialize the database
        conclusionRepository.saveAndFlush(conclusion);

        // Get the conclusion
        restConclusionMockMvc.perform(get("/api/conclusions/{id}", conclusion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(conclusion.getId().intValue()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingConclusion() throws Exception {
        // Get the conclusion
        restConclusionMockMvc.perform(get("/api/conclusions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateConclusion() throws Exception {
        // Initialize the database
        conclusionRepository.saveAndFlush(conclusion);
        int databaseSizeBeforeUpdate = conclusionRepository.findAll().size();

        // Update the conclusion
        Conclusion updatedConclusion = conclusionRepository.findOne(conclusion.getId());
        updatedConclusion
                .text(UPDATED_TEXT);

        restConclusionMockMvc.perform(put("/api/conclusions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedConclusion)))
                .andExpect(status().isOk());

        // Validate the Conclusion in the database
        List<Conclusion> conclusions = conclusionRepository.findAll();
        assertThat(conclusions).hasSize(databaseSizeBeforeUpdate);
        Conclusion testConclusion = conclusions.get(conclusions.size() - 1);
        assertThat(testConclusion.getText()).isEqualTo(UPDATED_TEXT);
    }

    @Test
    @Transactional
    public void deleteConclusion() throws Exception {
        // Initialize the database
        conclusionRepository.saveAndFlush(conclusion);
        int databaseSizeBeforeDelete = conclusionRepository.findAll().size();

        // Get the conclusion
        restConclusionMockMvc.perform(delete("/api/conclusions/{id}", conclusion.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Conclusion> conclusions = conclusionRepository.findAll();
        assertThat(conclusions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
