package me.larrycarodenis.web.rest;

import me.larrycarodenis.BackofficeApp;

import me.larrycarodenis.domain.HomePage;
import me.larrycarodenis.repository.HomePageRepository;
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
import java.util.List;


import static me.larrycarodenis.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the HomePageResource REST controller.
 *
 * @see HomePageResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BackofficeApp.class)
public class HomePageResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_YOUNG_MALE_URL = "AAAAAAAAAA";
    private static final String UPDATED_YOUNG_MALE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_YOUNG_FEMALE_URL = "AAAAAAAAAA";
    private static final String UPDATED_YOUNG_FEMALE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_ADULT_MALE_URL = "AAAAAAAAAA";
    private static final String UPDATED_ADULT_MALE_URL = "BBBBBBBBBB";

    private static final String DEFAULT_ADULT_FEMALE_URL = "AAAAAAAAAA";
    private static final String UPDATED_ADULT_FEMALE_URL = "BBBBBBBBBB";

    @Autowired
    private HomePageRepository homePageRepository;

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

    private MockMvc restHomePageMockMvc;

    private HomePage homePage;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HomePageResource homePageResource = new HomePageResource(homePageRepository);
        this.restHomePageMockMvc = MockMvcBuilders.standaloneSetup(homePageResource)
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
    public static HomePage createEntity(EntityManager em) {
        HomePage homePage = new HomePage()
            .name(DEFAULT_NAME)
            .youngMaleUrl(DEFAULT_YOUNG_MALE_URL)
            .youngFemaleUrl(DEFAULT_YOUNG_FEMALE_URL)
            .adultMaleUrl(DEFAULT_ADULT_MALE_URL)
            .adultFemaleUrl(DEFAULT_ADULT_FEMALE_URL);
        return homePage;
    }

    @Before
    public void initTest() {
        homePage = createEntity(em);
    }

    @Test
    @Transactional
    public void createHomePage() throws Exception {
        int databaseSizeBeforeCreate = homePageRepository.findAll().size();

        // Create the HomePage
        restHomePageMockMvc.perform(post("/api/home-pages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(homePage)))
            .andExpect(status().isCreated());

        // Validate the HomePage in the database
        List<HomePage> homePageList = homePageRepository.findAll();
        assertThat(homePageList).hasSize(databaseSizeBeforeCreate + 1);
        HomePage testHomePage = homePageList.get(homePageList.size() - 1);
        assertThat(testHomePage.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testHomePage.getYoungMaleUrl()).isEqualTo(DEFAULT_YOUNG_MALE_URL);
        assertThat(testHomePage.getYoungFemaleUrl()).isEqualTo(DEFAULT_YOUNG_FEMALE_URL);
        assertThat(testHomePage.getAdultMaleUrl()).isEqualTo(DEFAULT_ADULT_MALE_URL);
        assertThat(testHomePage.getAdultFemaleUrl()).isEqualTo(DEFAULT_ADULT_FEMALE_URL);
    }

    @Test
    @Transactional
    public void createHomePageWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = homePageRepository.findAll().size();

        // Create the HomePage with an existing ID
        homePage.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHomePageMockMvc.perform(post("/api/home-pages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(homePage)))
            .andExpect(status().isBadRequest());

        // Validate the HomePage in the database
        List<HomePage> homePageList = homePageRepository.findAll();
        assertThat(homePageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = homePageRepository.findAll().size();
        // set the field null
        homePage.setName(null);

        // Create the HomePage, which fails.

        restHomePageMockMvc.perform(post("/api/home-pages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(homePage)))
            .andExpect(status().isBadRequest());

        List<HomePage> homePageList = homePageRepository.findAll();
        assertThat(homePageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkYoungMaleUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = homePageRepository.findAll().size();
        // set the field null
        homePage.setYoungMaleUrl(null);

        // Create the HomePage, which fails.

        restHomePageMockMvc.perform(post("/api/home-pages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(homePage)))
            .andExpect(status().isBadRequest());

        List<HomePage> homePageList = homePageRepository.findAll();
        assertThat(homePageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkYoungFemaleUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = homePageRepository.findAll().size();
        // set the field null
        homePage.setYoungFemaleUrl(null);

        // Create the HomePage, which fails.

        restHomePageMockMvc.perform(post("/api/home-pages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(homePage)))
            .andExpect(status().isBadRequest());

        List<HomePage> homePageList = homePageRepository.findAll();
        assertThat(homePageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAdultMaleUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = homePageRepository.findAll().size();
        // set the field null
        homePage.setAdultMaleUrl(null);

        // Create the HomePage, which fails.

        restHomePageMockMvc.perform(post("/api/home-pages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(homePage)))
            .andExpect(status().isBadRequest());

        List<HomePage> homePageList = homePageRepository.findAll();
        assertThat(homePageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAdultFemaleUrlIsRequired() throws Exception {
        int databaseSizeBeforeTest = homePageRepository.findAll().size();
        // set the field null
        homePage.setAdultFemaleUrl(null);

        // Create the HomePage, which fails.

        restHomePageMockMvc.perform(post("/api/home-pages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(homePage)))
            .andExpect(status().isBadRequest());

        List<HomePage> homePageList = homePageRepository.findAll();
        assertThat(homePageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllHomePages() throws Exception {
        // Initialize the database
        homePageRepository.saveAndFlush(homePage);

        // Get all the homePageList
        restHomePageMockMvc.perform(get("/api/home-pages?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(homePage.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].youngMaleUrl").value(hasItem(DEFAULT_YOUNG_MALE_URL.toString())))
            .andExpect(jsonPath("$.[*].youngFemaleUrl").value(hasItem(DEFAULT_YOUNG_FEMALE_URL.toString())))
            .andExpect(jsonPath("$.[*].adultMaleUrl").value(hasItem(DEFAULT_ADULT_MALE_URL.toString())))
            .andExpect(jsonPath("$.[*].adultFemaleUrl").value(hasItem(DEFAULT_ADULT_FEMALE_URL.toString())));
    }
    
    @Test
    @Transactional
    public void getHomePage() throws Exception {
        // Initialize the database
        homePageRepository.saveAndFlush(homePage);

        // Get the homePage
        restHomePageMockMvc.perform(get("/api/home-pages/{id}", homePage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(homePage.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.youngMaleUrl").value(DEFAULT_YOUNG_MALE_URL.toString()))
            .andExpect(jsonPath("$.youngFemaleUrl").value(DEFAULT_YOUNG_FEMALE_URL.toString()))
            .andExpect(jsonPath("$.adultMaleUrl").value(DEFAULT_ADULT_MALE_URL.toString()))
            .andExpect(jsonPath("$.adultFemaleUrl").value(DEFAULT_ADULT_FEMALE_URL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHomePage() throws Exception {
        // Get the homePage
        restHomePageMockMvc.perform(get("/api/home-pages/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHomePage() throws Exception {
        // Initialize the database
        homePageRepository.saveAndFlush(homePage);

        int databaseSizeBeforeUpdate = homePageRepository.findAll().size();

        // Update the homePage
        HomePage updatedHomePage = homePageRepository.findById(homePage.getId()).get();
        // Disconnect from session so that the updates on updatedHomePage are not directly saved in db
        em.detach(updatedHomePage);
        updatedHomePage
            .name(UPDATED_NAME)
            .youngMaleUrl(UPDATED_YOUNG_MALE_URL)
            .youngFemaleUrl(UPDATED_YOUNG_FEMALE_URL)
            .adultMaleUrl(UPDATED_ADULT_MALE_URL)
            .adultFemaleUrl(UPDATED_ADULT_FEMALE_URL);

        restHomePageMockMvc.perform(put("/api/home-pages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedHomePage)))
            .andExpect(status().isOk());

        // Validate the HomePage in the database
        List<HomePage> homePageList = homePageRepository.findAll();
        assertThat(homePageList).hasSize(databaseSizeBeforeUpdate);
        HomePage testHomePage = homePageList.get(homePageList.size() - 1);
        assertThat(testHomePage.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testHomePage.getYoungMaleUrl()).isEqualTo(UPDATED_YOUNG_MALE_URL);
        assertThat(testHomePage.getYoungFemaleUrl()).isEqualTo(UPDATED_YOUNG_FEMALE_URL);
        assertThat(testHomePage.getAdultMaleUrl()).isEqualTo(UPDATED_ADULT_MALE_URL);
        assertThat(testHomePage.getAdultFemaleUrl()).isEqualTo(UPDATED_ADULT_FEMALE_URL);
    }

    @Test
    @Transactional
    public void updateNonExistingHomePage() throws Exception {
        int databaseSizeBeforeUpdate = homePageRepository.findAll().size();

        // Create the HomePage

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHomePageMockMvc.perform(put("/api/home-pages")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(homePage)))
            .andExpect(status().isBadRequest());

        // Validate the HomePage in the database
        List<HomePage> homePageList = homePageRepository.findAll();
        assertThat(homePageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteHomePage() throws Exception {
        // Initialize the database
        homePageRepository.saveAndFlush(homePage);

        int databaseSizeBeforeDelete = homePageRepository.findAll().size();

        // Delete the homePage
        restHomePageMockMvc.perform(delete("/api/home-pages/{id}", homePage.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<HomePage> homePageList = homePageRepository.findAll();
        assertThat(homePageList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(HomePage.class);
        HomePage homePage1 = new HomePage();
        homePage1.setId(1L);
        HomePage homePage2 = new HomePage();
        homePage2.setId(homePage1.getId());
        assertThat(homePage1).isEqualTo(homePage2);
        homePage2.setId(2L);
        assertThat(homePage1).isNotEqualTo(homePage2);
        homePage1.setId(null);
        assertThat(homePage1).isNotEqualTo(homePage2);
    }
}
