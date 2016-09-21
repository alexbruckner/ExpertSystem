package com.bru.jhipster.expertsystem.web.rest;

import com.bru.jhipster.expertsystem.ExpertSystemApp;

import com.bru.jhipster.expertsystem.domain.Answer;
import com.bru.jhipster.expertsystem.domain.Question;
import com.bru.jhipster.expertsystem.repository.AnswerRepository;

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
 * Test class for the AnswerResource REST controller.
 *
 * @see AnswerResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ExpertSystemApp.class)
public class AnswerResourceIntTest {

    private static final String DEFAULT_TEXT = "AAAAA";
    private static final String UPDATED_TEXT = "BBBBB";

    @Inject
    private AnswerRepository answerRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restAnswerMockMvc;

    private Answer answer;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AnswerResource answerResource = new AnswerResource();
        ReflectionTestUtils.setField(answerResource, "answerRepository", answerRepository);
        this.restAnswerMockMvc = MockMvcBuilders.standaloneSetup(answerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Answer createEntity(EntityManager em) {
        Answer answer = new Answer()
                .text(DEFAULT_TEXT);
        // Add required entity
        Question question = QuestionResourceIntTest.createEntity(em);
        em.persist(question);
        em.flush();
        answer.setQuestion(question);
        return answer;
    }

    @Before
    public void initTest() {
        answer = createEntity(em);
    }

    @Test
    @Transactional
    public void createAnswer() throws Exception {
        int databaseSizeBeforeCreate = answerRepository.findAll().size();

        // Create the Answer

        restAnswerMockMvc.perform(post("/api/answers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(answer)))
                .andExpect(status().isCreated());

        // Validate the Answer in the database
        List<Answer> answers = answerRepository.findAll();
        assertThat(answers).hasSize(databaseSizeBeforeCreate + 1);
        Answer testAnswer = answers.get(answers.size() - 1);
        assertThat(testAnswer.getText()).isEqualTo(DEFAULT_TEXT);
    }

    @Test
    @Transactional
    public void checkTextIsRequired() throws Exception {
        int databaseSizeBeforeTest = answerRepository.findAll().size();
        // set the field null
        answer.setText(null);

        // Create the Answer, which fails.

        restAnswerMockMvc.perform(post("/api/answers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(answer)))
                .andExpect(status().isBadRequest());

        List<Answer> answers = answerRepository.findAll();
        assertThat(answers).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAnswers() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get all the answers
        restAnswerMockMvc.perform(get("/api/answers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(answer.getId().intValue())))
                .andExpect(jsonPath("$.[*].text").value(hasItem(DEFAULT_TEXT.toString())));
    }

    @Test
    @Transactional
    public void getAnswer() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);

        // Get the answer
        restAnswerMockMvc.perform(get("/api/answers/{id}", answer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(answer.getId().intValue()))
            .andExpect(jsonPath("$.text").value(DEFAULT_TEXT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAnswer() throws Exception {
        // Get the answer
        restAnswerMockMvc.perform(get("/api/answers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAnswer() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);
        int databaseSizeBeforeUpdate = answerRepository.findAll().size();

        // Update the answer
        Answer updatedAnswer = answerRepository.findOne(answer.getId());
        updatedAnswer
                .text(UPDATED_TEXT);

        restAnswerMockMvc.perform(put("/api/answers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAnswer)))
                .andExpect(status().isOk());

        // Validate the Answer in the database
        List<Answer> answers = answerRepository.findAll();
        assertThat(answers).hasSize(databaseSizeBeforeUpdate);
        Answer testAnswer = answers.get(answers.size() - 1);
        assertThat(testAnswer.getText()).isEqualTo(UPDATED_TEXT);
    }

    @Test
    @Transactional
    public void deleteAnswer() throws Exception {
        // Initialize the database
        answerRepository.saveAndFlush(answer);
        int databaseSizeBeforeDelete = answerRepository.findAll().size();

        // Get the answer
        restAnswerMockMvc.perform(delete("/api/answers/{id}", answer.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Answer> answers = answerRepository.findAll();
        assertThat(answers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
