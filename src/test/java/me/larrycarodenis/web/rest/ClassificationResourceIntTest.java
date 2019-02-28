package me.larrycarodenis.web.rest;

import me.larrycarodenis.BackofficeApp;

import me.larrycarodenis.domain.Classification;
import me.larrycarodenis.repository.ClassificationRepository;
import me.larrycarodenis.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;


import static me.larrycarodenis.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import me.larrycarodenis.domain.enumeration.Gender;
import me.larrycarodenis.domain.enumeration.Emotion;
/**
 * Test class for the ClassificationResource REST controller.
 *
 * @see ClassificationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BackofficeApp.class)
public class ClassificationResourceIntTest {

    private static final String DEFAULT_PERSON_ID = "AAAAAAAAAA";
    private static final String UPDATED_PERSON_ID = "BBBBBBBBBB";

    private static final Instant DEFAULT_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_AGE = 1;
    private static final Integer UPDATED_AGE = 2;

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    private static final Emotion DEFAULT_EMOTION = Emotion.ANGRY;
    private static final Emotion UPDATED_EMOTION = Emotion.DISGUST;

    @Autowired
    private ClassificationRepository classificationRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restClassificationMockMvc;

    private Classification classification;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ClassificationResource classificationResource = new ClassificationResource(classificationRepository);
        this.restClassificationMockMvc = MockMvcBuilders.standaloneSetup(classificationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Classification createEntity(EntityManager em) {
        Classification classification = new Classification()
            .personId(DEFAULT_PERSON_ID)
            .timestamp(DEFAULT_TIMESTAMP)
            .age(DEFAULT_AGE)
            .gender(DEFAULT_GENDER)
            .emotion(DEFAULT_EMOTION);
        return classification;
    }

    @Before
    public void initTest() {
        classification = createEntity(em);
    }

    @Test
    @Transactional
    public void createClassification() throws Exception {
        int databaseSizeBeforeCreate = classificationRepository.findAll().size();

        // Create the Classification
        restClassificationMockMvc.perform(post("/api/classifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classification)))
            .andExpect(status().isCreated());

        // Validate the Classification in the database
        List<Classification> classificationList = classificationRepository.findAll();
        assertThat(classificationList).hasSize(databaseSizeBeforeCreate + 1);
        Classification testClassification = classificationList.get(classificationList.size() - 1);
        assertThat(testClassification.getPersonId()).isEqualTo(DEFAULT_PERSON_ID);
        assertThat(testClassification.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);
        assertThat(testClassification.getAge()).isEqualTo(DEFAULT_AGE);
        assertThat(testClassification.getGender()).isEqualTo(DEFAULT_GENDER);
        assertThat(testClassification.getEmotion()).isEqualTo(DEFAULT_EMOTION);
    }

    @Test
    @Transactional
    public void createClassificationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = classificationRepository.findAll().size();

        // Create the Classification with an existing ID
        classification.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClassificationMockMvc.perform(post("/api/classifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classification)))
            .andExpect(status().isBadRequest());

        // Validate the Classification in the database
        List<Classification> classificationList = classificationRepository.findAll();
        assertThat(classificationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkPersonIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = classificationRepository.findAll().size();
        // set the field null
        classification.setPersonId(null);

        // Create the Classification, which fails.

        restClassificationMockMvc.perform(post("/api/classifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classification)))
            .andExpect(status().isBadRequest());

        List<Classification> classificationList = classificationRepository.findAll();
        assertThat(classificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTimestampIsRequired() throws Exception {
        int databaseSizeBeforeTest = classificationRepository.findAll().size();
        // set the field null
        classification.setTimestamp(null);

        // Create the Classification, which fails.

        restClassificationMockMvc.perform(post("/api/classifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classification)))
            .andExpect(status().isBadRequest());

        List<Classification> classificationList = classificationRepository.findAll();
        assertThat(classificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAgeIsRequired() throws Exception {
        int databaseSizeBeforeTest = classificationRepository.findAll().size();
        // set the field null
        classification.setAge(null);

        // Create the Classification, which fails.

        restClassificationMockMvc.perform(post("/api/classifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classification)))
            .andExpect(status().isBadRequest());

        List<Classification> classificationList = classificationRepository.findAll();
        assertThat(classificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGenderIsRequired() throws Exception {
        int databaseSizeBeforeTest = classificationRepository.findAll().size();
        // set the field null
        classification.setGender(null);

        // Create the Classification, which fails.

        restClassificationMockMvc.perform(post("/api/classifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classification)))
            .andExpect(status().isBadRequest());

        List<Classification> classificationList = classificationRepository.findAll();
        assertThat(classificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEmotionIsRequired() throws Exception {
        int databaseSizeBeforeTest = classificationRepository.findAll().size();
        // set the field null
        classification.setEmotion(null);

        // Create the Classification, which fails.

        restClassificationMockMvc.perform(post("/api/classifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classification)))
            .andExpect(status().isBadRequest());

        List<Classification> classificationList = classificationRepository.findAll();
        assertThat(classificationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllClassifications() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classification);

        // Get all the classificationList
        restClassificationMockMvc.perform(get("/api/classifications?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classification.getId().intValue())))
            .andExpect(jsonPath("$.[*].personId").value(hasItem(DEFAULT_PERSON_ID.toString())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())))
            .andExpect(jsonPath("$.[*].age").value(hasItem(DEFAULT_AGE)))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())))
            .andExpect(jsonPath("$.[*].emotion").value(hasItem(DEFAULT_EMOTION.toString())));
    }
    
    @Test
    @Transactional
    public void getClassification() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classification);

        // Get the classification
        restClassificationMockMvc.perform(get("/api/classifications/{id}", classification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(classification.getId().intValue()))
            .andExpect(jsonPath("$.personId").value(DEFAULT_PERSON_ID.toString()))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.toString()))
            .andExpect(jsonPath("$.age").value(DEFAULT_AGE))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()))
            .andExpect(jsonPath("$.emotion").value(DEFAULT_EMOTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingClassification() throws Exception {
        // Get the classification
        restClassificationMockMvc.perform(get("/api/classifications/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClassification() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classification);

        int databaseSizeBeforeUpdate = classificationRepository.findAll().size();

        // Update the classification
        Classification updatedClassification = classificationRepository.findById(classification.getId()).get();
        // Disconnect from session so that the updates on updatedClassification are not directly saved in db
        em.detach(updatedClassification);
        updatedClassification
            .personId(UPDATED_PERSON_ID)
            .timestamp(UPDATED_TIMESTAMP)
            .age(UPDATED_AGE)
            .gender(UPDATED_GENDER)
            .emotion(UPDATED_EMOTION);

        restClassificationMockMvc.perform(put("/api/classifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedClassification)))
            .andExpect(status().isOk());

        // Validate the Classification in the database
        List<Classification> classificationList = classificationRepository.findAll();
        assertThat(classificationList).hasSize(databaseSizeBeforeUpdate);
        Classification testClassification = classificationList.get(classificationList.size() - 1);
        assertThat(testClassification.getPersonId()).isEqualTo(UPDATED_PERSON_ID);
        assertThat(testClassification.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);
        assertThat(testClassification.getAge()).isEqualTo(UPDATED_AGE);
        assertThat(testClassification.getGender()).isEqualTo(UPDATED_GENDER);
        assertThat(testClassification.getEmotion()).isEqualTo(UPDATED_EMOTION);
    }

    @Test
    @Transactional
    public void updateNonExistingClassification() throws Exception {
        int databaseSizeBeforeUpdate = classificationRepository.findAll().size();

        // Create the Classification

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClassificationMockMvc.perform(put("/api/classifications")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classification)))
            .andExpect(status().isBadRequest());

        // Validate the Classification in the database
        List<Classification> classificationList = classificationRepository.findAll();
        assertThat(classificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteClassification() throws Exception {
        // Initialize the database
        classificationRepository.saveAndFlush(classification);

        int databaseSizeBeforeDelete = classificationRepository.findAll().size();

        // Delete the classification
        restClassificationMockMvc.perform(delete("/api/classifications/{id}", classification.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Classification> classificationList = classificationRepository.findAll();
        assertThat(classificationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Classification.class);
        Classification classification1 = new Classification();
        classification1.setId(1L);
        Classification classification2 = new Classification();
        classification2.setId(classification1.getId());
        assertThat(classification1).isEqualTo(classification2);
        classification2.setId(2L);
        assertThat(classification1).isNotEqualTo(classification2);
        classification1.setId(null);
        assertThat(classification1).isNotEqualTo(classification2);
    }
}
