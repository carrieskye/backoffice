package me.larrycarodenis.web.rest;

import me.larrycarodenis.BackofficeApp;

import me.larrycarodenis.domain.Slide;
import me.larrycarodenis.repository.SlideRepository;
import me.larrycarodenis.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;


import static me.larrycarodenis.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import me.larrycarodenis.domain.enumeration.AgeCategory;
import me.larrycarodenis.domain.enumeration.Gender;
/**
 * Test class for the SlideResource REST controller.
 *
 * @see SlideResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BackofficeApp.class)
public class SlideResourceIntTest {

    private static final byte[] DEFAULT_IMAGE = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_IMAGE = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_IMAGE_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_IMAGE_CONTENT_TYPE = "image/png";

    private static final AgeCategory DEFAULT_AGE_CATEGORY = AgeCategory.YOUNG;
    private static final AgeCategory UPDATED_AGE_CATEGORY = AgeCategory.ADULT;

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    @Autowired
    private SlideRepository slideRepository;

    @Mock
    private SlideRepository slideRepositoryMock;

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

    private MockMvc restSlideMockMvc;

    private Slide slide;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SlideResource slideResource = new SlideResource(slideRepository);
        this.restSlideMockMvc = MockMvcBuilders.standaloneSetup(slideResource)
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
    public static Slide createEntity(EntityManager em) {
        Slide slide = new Slide()
            .image(DEFAULT_IMAGE)
            .imageContentType(DEFAULT_IMAGE_CONTENT_TYPE)
            .ageCategory(DEFAULT_AGE_CATEGORY)
            .gender(DEFAULT_GENDER);
        return slide;
    }

    @Before
    public void initTest() {
        slide = createEntity(em);
    }

    @Test
    @Transactional
    public void createSlide() throws Exception {
        int databaseSizeBeforeCreate = slideRepository.findAll().size();

        // Create the Slide
        restSlideMockMvc.perform(post("/api/slides")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(slide)))
            .andExpect(status().isCreated());

        // Validate the Slide in the database
        List<Slide> slideList = slideRepository.findAll();
        assertThat(slideList).hasSize(databaseSizeBeforeCreate + 1);
        Slide testSlide = slideList.get(slideList.size() - 1);
        assertThat(testSlide.getImage()).isEqualTo(DEFAULT_IMAGE);
        assertThat(testSlide.getImageContentType()).isEqualTo(DEFAULT_IMAGE_CONTENT_TYPE);
        assertThat(testSlide.getAgeCategory()).isEqualTo(DEFAULT_AGE_CATEGORY);
        assertThat(testSlide.getGender()).isEqualTo(DEFAULT_GENDER);
    }

    @Test
    @Transactional
    public void createSlideWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = slideRepository.findAll().size();

        // Create the Slide with an existing ID
        slide.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSlideMockMvc.perform(post("/api/slides")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(slide)))
            .andExpect(status().isBadRequest());

        // Validate the Slide in the database
        List<Slide> slideList = slideRepository.findAll();
        assertThat(slideList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkAgeCategoryIsRequired() throws Exception {
        int databaseSizeBeforeTest = slideRepository.findAll().size();
        // set the field null
        slide.setAgeCategory(null);

        // Create the Slide, which fails.

        restSlideMockMvc.perform(post("/api/slides")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(slide)))
            .andExpect(status().isBadRequest());

        List<Slide> slideList = slideRepository.findAll();
        assertThat(slideList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGenderIsRequired() throws Exception {
        int databaseSizeBeforeTest = slideRepository.findAll().size();
        // set the field null
        slide.setGender(null);

        // Create the Slide, which fails.

        restSlideMockMvc.perform(post("/api/slides")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(slide)))
            .andExpect(status().isBadRequest());

        List<Slide> slideList = slideRepository.findAll();
        assertThat(slideList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSlides() throws Exception {
        // Initialize the database
        slideRepository.saveAndFlush(slide);

        // Get all the slideList
        restSlideMockMvc.perform(get("/api/slides?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(slide.getId().intValue())))
            .andExpect(jsonPath("$.[*].imageContentType").value(hasItem(DEFAULT_IMAGE_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].image").value(hasItem(Base64Utils.encodeToString(DEFAULT_IMAGE))))
            .andExpect(jsonPath("$.[*].ageCategory").value(hasItem(DEFAULT_AGE_CATEGORY.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllSlidesWithEagerRelationshipsIsEnabled() throws Exception {
        SlideResource slideResource = new SlideResource(slideRepositoryMock);
        when(slideRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restSlideMockMvc = MockMvcBuilders.standaloneSetup(slideResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restSlideMockMvc.perform(get("/api/slides?eagerload=true"))
        .andExpect(status().isOk());

        verify(slideRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllSlidesWithEagerRelationshipsIsNotEnabled() throws Exception {
        SlideResource slideResource = new SlideResource(slideRepositoryMock);
            when(slideRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restSlideMockMvc = MockMvcBuilders.standaloneSetup(slideResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restSlideMockMvc.perform(get("/api/slides?eagerload=true"))
        .andExpect(status().isOk());

            verify(slideRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getSlide() throws Exception {
        // Initialize the database
        slideRepository.saveAndFlush(slide);

        // Get the slide
        restSlideMockMvc.perform(get("/api/slides/{id}", slide.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(slide.getId().intValue()))
            .andExpect(jsonPath("$.imageContentType").value(DEFAULT_IMAGE_CONTENT_TYPE))
            .andExpect(jsonPath("$.image").value(Base64Utils.encodeToString(DEFAULT_IMAGE)))
            .andExpect(jsonPath("$.ageCategory").value(DEFAULT_AGE_CATEGORY.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSlide() throws Exception {
        // Get the slide
        restSlideMockMvc.perform(get("/api/slides/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSlide() throws Exception {
        // Initialize the database
        slideRepository.saveAndFlush(slide);

        int databaseSizeBeforeUpdate = slideRepository.findAll().size();

        // Update the slide
        Slide updatedSlide = slideRepository.findById(slide.getId()).get();
        // Disconnect from session so that the updates on updatedSlide are not directly saved in db
        em.detach(updatedSlide);
        updatedSlide
            .image(UPDATED_IMAGE)
            .imageContentType(UPDATED_IMAGE_CONTENT_TYPE)
            .ageCategory(UPDATED_AGE_CATEGORY)
            .gender(UPDATED_GENDER);

        restSlideMockMvc.perform(put("/api/slides")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSlide)))
            .andExpect(status().isOk());

        // Validate the Slide in the database
        List<Slide> slideList = slideRepository.findAll();
        assertThat(slideList).hasSize(databaseSizeBeforeUpdate);
        Slide testSlide = slideList.get(slideList.size() - 1);
        assertThat(testSlide.getImage()).isEqualTo(UPDATED_IMAGE);
        assertThat(testSlide.getImageContentType()).isEqualTo(UPDATED_IMAGE_CONTENT_TYPE);
        assertThat(testSlide.getAgeCategory()).isEqualTo(UPDATED_AGE_CATEGORY);
        assertThat(testSlide.getGender()).isEqualTo(UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void updateNonExistingSlide() throws Exception {
        int databaseSizeBeforeUpdate = slideRepository.findAll().size();

        // Create the Slide

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSlideMockMvc.perform(put("/api/slides")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(slide)))
            .andExpect(status().isBadRequest());

        // Validate the Slide in the database
        List<Slide> slideList = slideRepository.findAll();
        assertThat(slideList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSlide() throws Exception {
        // Initialize the database
        slideRepository.saveAndFlush(slide);

        int databaseSizeBeforeDelete = slideRepository.findAll().size();

        // Delete the slide
        restSlideMockMvc.perform(delete("/api/slides/{id}", slide.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Slide> slideList = slideRepository.findAll();
        assertThat(slideList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Slide.class);
        Slide slide1 = new Slide();
        slide1.setId(1L);
        Slide slide2 = new Slide();
        slide2.setId(slide1.getId());
        assertThat(slide1).isEqualTo(slide2);
        slide2.setId(2L);
        assertThat(slide1).isNotEqualTo(slide2);
        slide1.setId(null);
        assertThat(slide1).isNotEqualTo(slide2);
    }
}
