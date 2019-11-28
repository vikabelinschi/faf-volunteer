package md.faf.volunteer.web.rest;

import md.faf.volunteer.VolunteerApp;
import md.faf.volunteer.domain.Ong;
import md.faf.volunteer.domain.Project;
import md.faf.volunteer.repository.OngRepository;
import md.faf.volunteer.service.OngService;
import md.faf.volunteer.web.rest.errors.ExceptionTranslator;
import md.faf.volunteer.service.dto.OngCriteria;
import md.faf.volunteer.service.OngQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static md.faf.volunteer.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link OngResource} REST controller.
 */
@SpringBootTest(classes = VolunteerApp.class)
public class OngResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_IDNO = "AAAAAAAAAA";
    private static final String UPDATED_IDNO = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    @Autowired
    private OngRepository ongRepository;

    @Autowired
    private OngService ongService;

    @Autowired
    private OngQueryService ongQueryService;

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

    private MockMvc restOngMockMvc;

    private Ong ong;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OngResource ongResource = new OngResource(ongService, ongQueryService);
        this.restOngMockMvc = MockMvcBuilders.standaloneSetup(ongResource)
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
    public static Ong createEntity(EntityManager em) {
        Ong ong = new Ong()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .idno(DEFAULT_IDNO)
            .email(DEFAULT_EMAIL)
            .address(DEFAULT_ADDRESS)
            .phone(DEFAULT_PHONE);
        return ong;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ong createUpdatedEntity(EntityManager em) {
        Ong ong = new Ong()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .idno(UPDATED_IDNO)
            .email(UPDATED_EMAIL)
            .address(UPDATED_ADDRESS)
            .phone(UPDATED_PHONE);
        return ong;
    }

    @BeforeEach
    public void initTest() {
        ong = createEntity(em);
    }

    @Test
    @Transactional
    public void createOng() throws Exception {
        int databaseSizeBeforeCreate = ongRepository.findAll().size();

        // Create the Ong
        restOngMockMvc.perform(post("/api/ongs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ong)))
            .andExpect(status().isCreated());

        // Validate the Ong in the database
        List<Ong> ongList = ongRepository.findAll();
        assertThat(ongList).hasSize(databaseSizeBeforeCreate + 1);
        Ong testOng = ongList.get(ongList.size() - 1);
        assertThat(testOng.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOng.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testOng.getIdno()).isEqualTo(DEFAULT_IDNO);
        assertThat(testOng.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testOng.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testOng.getPhone()).isEqualTo(DEFAULT_PHONE);
    }

    @Test
    @Transactional
    public void createOngWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ongRepository.findAll().size();

        // Create the Ong with an existing ID
        ong.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOngMockMvc.perform(post("/api/ongs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ong)))
            .andExpect(status().isBadRequest());

        // Validate the Ong in the database
        List<Ong> ongList = ongRepository.findAll();
        assertThat(ongList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = ongRepository.findAll().size();
        // set the field null
        ong.setName(null);

        // Create the Ong, which fails.

        restOngMockMvc.perform(post("/api/ongs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ong)))
            .andExpect(status().isBadRequest());

        List<Ong> ongList = ongRepository.findAll();
        assertThat(ongList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = ongRepository.findAll().size();
        // set the field null
        ong.setDescription(null);

        // Create the Ong, which fails.

        restOngMockMvc.perform(post("/api/ongs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ong)))
            .andExpect(status().isBadRequest());

        List<Ong> ongList = ongRepository.findAll();
        assertThat(ongList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIdnoIsRequired() throws Exception {
        int databaseSizeBeforeTest = ongRepository.findAll().size();
        // set the field null
        ong.setIdno(null);

        // Create the Ong, which fails.

        restOngMockMvc.perform(post("/api/ongs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ong)))
            .andExpect(status().isBadRequest());

        List<Ong> ongList = ongRepository.findAll();
        assertThat(ongList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOngs() throws Exception {
        // Initialize the database
        ongRepository.saveAndFlush(ong);

        // Get all the ongList
        restOngMockMvc.perform(get("/api/ongs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ong.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].idno").value(hasItem(DEFAULT_IDNO)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)));
    }
    
    @Test
    @Transactional
    public void getOng() throws Exception {
        // Initialize the database
        ongRepository.saveAndFlush(ong);

        // Get the ong
        restOngMockMvc.perform(get("/api/ongs/{id}", ong.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ong.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.idno").value(DEFAULT_IDNO))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE));
    }


    @Test
    @Transactional
    public void getOngsByIdFiltering() throws Exception {
        // Initialize the database
        ongRepository.saveAndFlush(ong);

        Long id = ong.getId();

        defaultOngShouldBeFound("id.equals=" + id);
        defaultOngShouldNotBeFound("id.notEquals=" + id);

        defaultOngShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOngShouldNotBeFound("id.greaterThan=" + id);

        defaultOngShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOngShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllOngsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        ongRepository.saveAndFlush(ong);

        // Get all the ongList where name equals to DEFAULT_NAME
        defaultOngShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the ongList where name equals to UPDATED_NAME
        defaultOngShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOngsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ongRepository.saveAndFlush(ong);

        // Get all the ongList where name not equals to DEFAULT_NAME
        defaultOngShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the ongList where name not equals to UPDATED_NAME
        defaultOngShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOngsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        ongRepository.saveAndFlush(ong);

        // Get all the ongList where name in DEFAULT_NAME or UPDATED_NAME
        defaultOngShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the ongList where name equals to UPDATED_NAME
        defaultOngShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOngsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        ongRepository.saveAndFlush(ong);

        // Get all the ongList where name is not null
        defaultOngShouldBeFound("name.specified=true");

        // Get all the ongList where name is null
        defaultOngShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllOngsByNameContainsSomething() throws Exception {
        // Initialize the database
        ongRepository.saveAndFlush(ong);

        // Get all the ongList where name contains DEFAULT_NAME
        defaultOngShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the ongList where name contains UPDATED_NAME
        defaultOngShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOngsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        ongRepository.saveAndFlush(ong);

        // Get all the ongList where name does not contain DEFAULT_NAME
        defaultOngShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the ongList where name does not contain UPDATED_NAME
        defaultOngShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllOngsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        ongRepository.saveAndFlush(ong);

        // Get all the ongList where description equals to DEFAULT_DESCRIPTION
        defaultOngShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the ongList where description equals to UPDATED_DESCRIPTION
        defaultOngShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllOngsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ongRepository.saveAndFlush(ong);

        // Get all the ongList where description not equals to DEFAULT_DESCRIPTION
        defaultOngShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the ongList where description not equals to UPDATED_DESCRIPTION
        defaultOngShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllOngsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        ongRepository.saveAndFlush(ong);

        // Get all the ongList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultOngShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the ongList where description equals to UPDATED_DESCRIPTION
        defaultOngShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllOngsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        ongRepository.saveAndFlush(ong);

        // Get all the ongList where description is not null
        defaultOngShouldBeFound("description.specified=true");

        // Get all the ongList where description is null
        defaultOngShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllOngsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        ongRepository.saveAndFlush(ong);

        // Get all the ongList where description contains DEFAULT_DESCRIPTION
        defaultOngShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the ongList where description contains UPDATED_DESCRIPTION
        defaultOngShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllOngsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        ongRepository.saveAndFlush(ong);

        // Get all the ongList where description does not contain DEFAULT_DESCRIPTION
        defaultOngShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the ongList where description does not contain UPDATED_DESCRIPTION
        defaultOngShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllOngsByIdnoIsEqualToSomething() throws Exception {
        // Initialize the database
        ongRepository.saveAndFlush(ong);

        // Get all the ongList where idno equals to DEFAULT_IDNO
        defaultOngShouldBeFound("idno.equals=" + DEFAULT_IDNO);

        // Get all the ongList where idno equals to UPDATED_IDNO
        defaultOngShouldNotBeFound("idno.equals=" + UPDATED_IDNO);
    }

    @Test
    @Transactional
    public void getAllOngsByIdnoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ongRepository.saveAndFlush(ong);

        // Get all the ongList where idno not equals to DEFAULT_IDNO
        defaultOngShouldNotBeFound("idno.notEquals=" + DEFAULT_IDNO);

        // Get all the ongList where idno not equals to UPDATED_IDNO
        defaultOngShouldBeFound("idno.notEquals=" + UPDATED_IDNO);
    }

    @Test
    @Transactional
    public void getAllOngsByIdnoIsInShouldWork() throws Exception {
        // Initialize the database
        ongRepository.saveAndFlush(ong);

        // Get all the ongList where idno in DEFAULT_IDNO or UPDATED_IDNO
        defaultOngShouldBeFound("idno.in=" + DEFAULT_IDNO + "," + UPDATED_IDNO);

        // Get all the ongList where idno equals to UPDATED_IDNO
        defaultOngShouldNotBeFound("idno.in=" + UPDATED_IDNO);
    }

    @Test
    @Transactional
    public void getAllOngsByIdnoIsNullOrNotNull() throws Exception {
        // Initialize the database
        ongRepository.saveAndFlush(ong);

        // Get all the ongList where idno is not null
        defaultOngShouldBeFound("idno.specified=true");

        // Get all the ongList where idno is null
        defaultOngShouldNotBeFound("idno.specified=false");
    }
                @Test
    @Transactional
    public void getAllOngsByIdnoContainsSomething() throws Exception {
        // Initialize the database
        ongRepository.saveAndFlush(ong);

        // Get all the ongList where idno contains DEFAULT_IDNO
        defaultOngShouldBeFound("idno.contains=" + DEFAULT_IDNO);

        // Get all the ongList where idno contains UPDATED_IDNO
        defaultOngShouldNotBeFound("idno.contains=" + UPDATED_IDNO);
    }

    @Test
    @Transactional
    public void getAllOngsByIdnoNotContainsSomething() throws Exception {
        // Initialize the database
        ongRepository.saveAndFlush(ong);

        // Get all the ongList where idno does not contain DEFAULT_IDNO
        defaultOngShouldNotBeFound("idno.doesNotContain=" + DEFAULT_IDNO);

        // Get all the ongList where idno does not contain UPDATED_IDNO
        defaultOngShouldBeFound("idno.doesNotContain=" + UPDATED_IDNO);
    }


    @Test
    @Transactional
    public void getAllOngsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        ongRepository.saveAndFlush(ong);

        // Get all the ongList where email equals to DEFAULT_EMAIL
        defaultOngShouldBeFound("email.equals=" + DEFAULT_EMAIL);

        // Get all the ongList where email equals to UPDATED_EMAIL
        defaultOngShouldNotBeFound("email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllOngsByEmailIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ongRepository.saveAndFlush(ong);

        // Get all the ongList where email not equals to DEFAULT_EMAIL
        defaultOngShouldNotBeFound("email.notEquals=" + DEFAULT_EMAIL);

        // Get all the ongList where email not equals to UPDATED_EMAIL
        defaultOngShouldBeFound("email.notEquals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllOngsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        ongRepository.saveAndFlush(ong);

        // Get all the ongList where email in DEFAULT_EMAIL or UPDATED_EMAIL
        defaultOngShouldBeFound("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL);

        // Get all the ongList where email equals to UPDATED_EMAIL
        defaultOngShouldNotBeFound("email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllOngsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        ongRepository.saveAndFlush(ong);

        // Get all the ongList where email is not null
        defaultOngShouldBeFound("email.specified=true");

        // Get all the ongList where email is null
        defaultOngShouldNotBeFound("email.specified=false");
    }
                @Test
    @Transactional
    public void getAllOngsByEmailContainsSomething() throws Exception {
        // Initialize the database
        ongRepository.saveAndFlush(ong);

        // Get all the ongList where email contains DEFAULT_EMAIL
        defaultOngShouldBeFound("email.contains=" + DEFAULT_EMAIL);

        // Get all the ongList where email contains UPDATED_EMAIL
        defaultOngShouldNotBeFound("email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    public void getAllOngsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        ongRepository.saveAndFlush(ong);

        // Get all the ongList where email does not contain DEFAULT_EMAIL
        defaultOngShouldNotBeFound("email.doesNotContain=" + DEFAULT_EMAIL);

        // Get all the ongList where email does not contain UPDATED_EMAIL
        defaultOngShouldBeFound("email.doesNotContain=" + UPDATED_EMAIL);
    }


    @Test
    @Transactional
    public void getAllOngsByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        ongRepository.saveAndFlush(ong);

        // Get all the ongList where address equals to DEFAULT_ADDRESS
        defaultOngShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the ongList where address equals to UPDATED_ADDRESS
        defaultOngShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllOngsByAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ongRepository.saveAndFlush(ong);

        // Get all the ongList where address not equals to DEFAULT_ADDRESS
        defaultOngShouldNotBeFound("address.notEquals=" + DEFAULT_ADDRESS);

        // Get all the ongList where address not equals to UPDATED_ADDRESS
        defaultOngShouldBeFound("address.notEquals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllOngsByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        ongRepository.saveAndFlush(ong);

        // Get all the ongList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultOngShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the ongList where address equals to UPDATED_ADDRESS
        defaultOngShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllOngsByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        ongRepository.saveAndFlush(ong);

        // Get all the ongList where address is not null
        defaultOngShouldBeFound("address.specified=true");

        // Get all the ongList where address is null
        defaultOngShouldNotBeFound("address.specified=false");
    }
                @Test
    @Transactional
    public void getAllOngsByAddressContainsSomething() throws Exception {
        // Initialize the database
        ongRepository.saveAndFlush(ong);

        // Get all the ongList where address contains DEFAULT_ADDRESS
        defaultOngShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the ongList where address contains UPDATED_ADDRESS
        defaultOngShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllOngsByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        ongRepository.saveAndFlush(ong);

        // Get all the ongList where address does not contain DEFAULT_ADDRESS
        defaultOngShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the ongList where address does not contain UPDATED_ADDRESS
        defaultOngShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }


    @Test
    @Transactional
    public void getAllOngsByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        ongRepository.saveAndFlush(ong);

        // Get all the ongList where phone equals to DEFAULT_PHONE
        defaultOngShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the ongList where phone equals to UPDATED_PHONE
        defaultOngShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllOngsByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ongRepository.saveAndFlush(ong);

        // Get all the ongList where phone not equals to DEFAULT_PHONE
        defaultOngShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the ongList where phone not equals to UPDATED_PHONE
        defaultOngShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllOngsByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        ongRepository.saveAndFlush(ong);

        // Get all the ongList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultOngShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the ongList where phone equals to UPDATED_PHONE
        defaultOngShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllOngsByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        ongRepository.saveAndFlush(ong);

        // Get all the ongList where phone is not null
        defaultOngShouldBeFound("phone.specified=true");

        // Get all the ongList where phone is null
        defaultOngShouldNotBeFound("phone.specified=false");
    }
                @Test
    @Transactional
    public void getAllOngsByPhoneContainsSomething() throws Exception {
        // Initialize the database
        ongRepository.saveAndFlush(ong);

        // Get all the ongList where phone contains DEFAULT_PHONE
        defaultOngShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the ongList where phone contains UPDATED_PHONE
        defaultOngShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllOngsByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        ongRepository.saveAndFlush(ong);

        // Get all the ongList where phone does not contain DEFAULT_PHONE
        defaultOngShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the ongList where phone does not contain UPDATED_PHONE
        defaultOngShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }


    @Test
    @Transactional
    public void getAllOngsByProjectIsEqualToSomething() throws Exception {
        // Initialize the database
        ongRepository.saveAndFlush(ong);
        Project project = ProjectResourceIT.createEntity(em);
        em.persist(project);
        em.flush();
        ong.addProject(project);
        ongRepository.saveAndFlush(ong);
        Long projectId = project.getId();

        // Get all the ongList where project equals to projectId
        defaultOngShouldBeFound("projectId.equals=" + projectId);

        // Get all the ongList where project equals to projectId + 1
        defaultOngShouldNotBeFound("projectId.equals=" + (projectId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOngShouldBeFound(String filter) throws Exception {
        restOngMockMvc.perform(get("/api/ongs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ong.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].idno").value(hasItem(DEFAULT_IDNO)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)));

        // Check, that the count call also returns 1
        restOngMockMvc.perform(get("/api/ongs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOngShouldNotBeFound(String filter) throws Exception {
        restOngMockMvc.perform(get("/api/ongs?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOngMockMvc.perform(get("/api/ongs/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingOng() throws Exception {
        // Get the ong
        restOngMockMvc.perform(get("/api/ongs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOng() throws Exception {
        // Initialize the database
        ongService.save(ong);

        int databaseSizeBeforeUpdate = ongRepository.findAll().size();

        // Update the ong
        Ong updatedOng = ongRepository.findById(ong.getId()).get();
        // Disconnect from session so that the updates on updatedOng are not directly saved in db
        em.detach(updatedOng);
        updatedOng
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .idno(UPDATED_IDNO)
            .email(UPDATED_EMAIL)
            .address(UPDATED_ADDRESS)
            .phone(UPDATED_PHONE);

        restOngMockMvc.perform(put("/api/ongs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOng)))
            .andExpect(status().isOk());

        // Validate the Ong in the database
        List<Ong> ongList = ongRepository.findAll();
        assertThat(ongList).hasSize(databaseSizeBeforeUpdate);
        Ong testOng = ongList.get(ongList.size() - 1);
        assertThat(testOng.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOng.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testOng.getIdno()).isEqualTo(UPDATED_IDNO);
        assertThat(testOng.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testOng.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testOng.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void updateNonExistingOng() throws Exception {
        int databaseSizeBeforeUpdate = ongRepository.findAll().size();

        // Create the Ong

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOngMockMvc.perform(put("/api/ongs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ong)))
            .andExpect(status().isBadRequest());

        // Validate the Ong in the database
        List<Ong> ongList = ongRepository.findAll();
        assertThat(ongList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOng() throws Exception {
        // Initialize the database
        ongService.save(ong);

        int databaseSizeBeforeDelete = ongRepository.findAll().size();

        // Delete the ong
        restOngMockMvc.perform(delete("/api/ongs/{id}", ong.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ong> ongList = ongRepository.findAll();
        assertThat(ongList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
