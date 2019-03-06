package me.larrycarodenis.web.rest;

import me.larrycarodenis.BackofficeApp;

import me.larrycarodenis.domain.Personel;
import me.larrycarodenis.repository.PersonelRepository;
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
import org.springframework.util.Base64Utils;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;


import static me.larrycarodenis.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PersonelResource REST controller.
 *
 * @see PersonelResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BackofficeApp.class)
public class PersonelResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_IGNORED = false;
    private static final Boolean UPDATED_IS_IGNORED = true;

    private static final byte[] DEFAULT_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_PHOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_PHOTO_CONTENT_TYPE = "image/png";

    @Autowired
    private PersonelRepository personelRepository;

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

    private MockMvc restPersonelMockMvc;

    private Personel personel;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PersonelResource personelResource = new PersonelResource(personelRepository);
        this.restPersonelMockMvc = MockMvcBuilders.standaloneSetup(personelResource)
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
    public static Personel createEntity(EntityManager em) {
        Personel personel = new Personel()
            .name(DEFAULT_NAME)
            .isIgnored(DEFAULT_IS_IGNORED)
            .photo(DEFAULT_PHOTO)
            .photoContentType(DEFAULT_PHOTO_CONTENT_TYPE);
        return personel;
    }

    @Before
    public void initTest() {
        personel = createEntity(em);
    }

    @Test
    @Transactional
    public void createPersonel() throws Exception {
        int databaseSizeBeforeCreate = personelRepository.findAll().size();

        // Create the Personel
        restPersonelMockMvc.perform(post("/api/personels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(personel)))
            .andExpect(status().isCreated());

        // Validate the Personel in the database
        List<Personel> personelList = personelRepository.findAll();
        assertThat(personelList).hasSize(databaseSizeBeforeCreate + 1);
        Personel testPersonel = personelList.get(personelList.size() - 1);
        assertThat(testPersonel.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPersonel.isIsIgnored()).isEqualTo(DEFAULT_IS_IGNORED);
        assertThat(testPersonel.getPhoto()).isEqualTo(DEFAULT_PHOTO);
        assertThat(testPersonel.getPhotoContentType()).isEqualTo(DEFAULT_PHOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void createPersonelWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = personelRepository.findAll().size();

        // Create the Personel with an existing ID
        personel.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPersonelMockMvc.perform(post("/api/personels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(personel)))
            .andExpect(status().isBadRequest());

        // Validate the Personel in the database
        List<Personel> personelList = personelRepository.findAll();
        assertThat(personelList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = personelRepository.findAll().size();
        // set the field null
        personel.setName(null);

        // Create the Personel, which fails.

        restPersonelMockMvc.perform(post("/api/personels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(personel)))
            .andExpect(status().isBadRequest());

        List<Personel> personelList = personelRepository.findAll();
        assertThat(personelList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPersonels() throws Exception {
        // Initialize the database
        personelRepository.saveAndFlush(personel);

        // Get all the personelList
        restPersonelMockMvc.perform(get("/api/personels?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(personel.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].isIgnored").value(hasItem(DEFAULT_IS_IGNORED.booleanValue())))
            .andExpect(jsonPath("$.[*].photoContentType").value(hasItem(DEFAULT_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].photo").value(hasItem(Base64Utils.encodeToString(DEFAULT_PHOTO))));
    }
    
    @Test
    @Transactional
    public void getPersonel() throws Exception {
        // Initialize the database
        personelRepository.saveAndFlush(personel);

        // Get the personel
        restPersonelMockMvc.perform(get("/api/personels/{id}", personel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(personel.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.isIgnored").value(DEFAULT_IS_IGNORED.booleanValue()))
            .andExpect(jsonPath("$.photoContentType").value(DEFAULT_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.photo").value(Base64Utils.encodeToString(DEFAULT_PHOTO)));
    }

    @Test
    @Transactional
    public void getNonExistingPersonel() throws Exception {
        // Get the personel
        restPersonelMockMvc.perform(get("/api/personels/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePersonel() throws Exception {
        // Initialize the database
        personelRepository.saveAndFlush(personel);

        int databaseSizeBeforeUpdate = personelRepository.findAll().size();

        // Update the personel
        Personel updatedPersonel = personelRepository.findById(personel.getId()).get();
        // Disconnect from session so that the updates on updatedPersonel are not directly saved in db
        em.detach(updatedPersonel);
        updatedPersonel
            .name(UPDATED_NAME)
            .isIgnored(UPDATED_IS_IGNORED)
            .photo(UPDATED_PHOTO)
            .photoContentType(UPDATED_PHOTO_CONTENT_TYPE);

        restPersonelMockMvc.perform(put("/api/personels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPersonel)))
            .andExpect(status().isOk());

        // Validate the Personel in the database
        List<Personel> personelList = personelRepository.findAll();
        assertThat(personelList).hasSize(databaseSizeBeforeUpdate);
        Personel testPersonel = personelList.get(personelList.size() - 1);
        assertThat(testPersonel.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPersonel.isIsIgnored()).isEqualTo(UPDATED_IS_IGNORED);
        assertThat(testPersonel.getPhoto()).isEqualTo(UPDATED_PHOTO);
        assertThat(testPersonel.getPhotoContentType()).isEqualTo(UPDATED_PHOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingPersonel() throws Exception {
        int databaseSizeBeforeUpdate = personelRepository.findAll().size();

        // Create the Personel

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPersonelMockMvc.perform(put("/api/personels")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(personel)))
            .andExpect(status().isBadRequest());

        // Validate the Personel in the database
        List<Personel> personelList = personelRepository.findAll();
        assertThat(personelList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePersonel() throws Exception {
        // Initialize the database
        personelRepository.saveAndFlush(personel);

        int databaseSizeBeforeDelete = personelRepository.findAll().size();

        // Delete the personel
        restPersonelMockMvc.perform(delete("/api/personels/{id}", personel.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Personel> personelList = personelRepository.findAll();
        assertThat(personelList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Personel.class);
        Personel personel1 = new Personel();
        personel1.setId(1L);
        Personel personel2 = new Personel();
        personel2.setId(personel1.getId());
        assertThat(personel1).isEqualTo(personel2);
        personel2.setId(2L);
        assertThat(personel1).isNotEqualTo(personel2);
        personel1.setId(null);
        assertThat(personel1).isNotEqualTo(personel2);
    }
}
