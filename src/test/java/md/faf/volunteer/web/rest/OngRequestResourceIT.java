package md.faf.volunteer.web.rest;

import md.faf.volunteer.VolunteerApp;
import md.faf.volunteer.domain.OngRequest;
import md.faf.volunteer.domain.User;
import md.faf.volunteer.repository.OngRequestRepository;
import md.faf.volunteer.service.OngRequestService;
import md.faf.volunteer.web.rest.errors.ExceptionTranslator;
import md.faf.volunteer.service.dto.OngRequestCriteria;
import md.faf.volunteer.service.OngRequestQueryService;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static md.faf.volunteer.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link OngRequestResource} REST controller.
 */
@SpringBootTest(classes = VolunteerApp.class)
public class OngRequestResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_IDNO = "AAAAAAAAAA";
    private static final String UPDATED_IDNO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_REGISTRATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REGISTRATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REGISTRATION_DATE = LocalDate.ofEpochDay(-1L);

    @Autowired
    private OngRequestRepository ongRequestRepository;

    @Autowired
    private OngRequestService ongRequestService;

    @Autowired
    private OngRequestQueryService ongRequestQueryService;

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

    private MockMvc restOngRequestMockMvc;

    private OngRequest ongRequest;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final OngRequestResource ongRequestResource = new OngRequestResource(ongRequestService, ongRequestQueryService);
        this.restOngRequestMockMvc = MockMvcBuilders.standaloneSetup(ongRequestResource)
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
    public static OngRequest createEntity(EntityManager em) {
        OngRequest ongRequest = new OngRequest()
            .name(DEFAULT_NAME)
            .idno(DEFAULT_IDNO)
            .registrationDate(DEFAULT_REGISTRATION_DATE);
        return ongRequest;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OngRequest createUpdatedEntity(EntityManager em) {
        OngRequest ongRequest = new OngRequest()
            .name(UPDATED_NAME)
            .idno(UPDATED_IDNO)
            .registrationDate(UPDATED_REGISTRATION_DATE);
        return ongRequest;
    }

    @BeforeEach
    public void initTest() {
        ongRequest = createEntity(em);
    }

    @Test
    @Transactional
    public void createOngRequest() throws Exception {
        int databaseSizeBeforeCreate = ongRequestRepository.findAll().size();

        // Create the OngRequest
        restOngRequestMockMvc.perform(post("/api/ong-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ongRequest)))
            .andExpect(status().isCreated());

        // Validate the OngRequest in the database
        List<OngRequest> ongRequestList = ongRequestRepository.findAll();
        assertThat(ongRequestList).hasSize(databaseSizeBeforeCreate + 1);
        OngRequest testOngRequest = ongRequestList.get(ongRequestList.size() - 1);
        assertThat(testOngRequest.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testOngRequest.getIdno()).isEqualTo(DEFAULT_IDNO);
        assertThat(testOngRequest.getRegistrationDate()).isEqualTo(DEFAULT_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    public void createOngRequestWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = ongRequestRepository.findAll().size();

        // Create the OngRequest with an existing ID
        ongRequest.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOngRequestMockMvc.perform(post("/api/ong-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ongRequest)))
            .andExpect(status().isBadRequest());

        // Validate the OngRequest in the database
        List<OngRequest> ongRequestList = ongRequestRepository.findAll();
        assertThat(ongRequestList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = ongRequestRepository.findAll().size();
        // set the field null
        ongRequest.setName(null);

        // Create the OngRequest, which fails.

        restOngRequestMockMvc.perform(post("/api/ong-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ongRequest)))
            .andExpect(status().isBadRequest());

        List<OngRequest> ongRequestList = ongRequestRepository.findAll();
        assertThat(ongRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkIdnoIsRequired() throws Exception {
        int databaseSizeBeforeTest = ongRequestRepository.findAll().size();
        // set the field null
        ongRequest.setIdno(null);

        // Create the OngRequest, which fails.

        restOngRequestMockMvc.perform(post("/api/ong-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ongRequest)))
            .andExpect(status().isBadRequest());

        List<OngRequest> ongRequestList = ongRequestRepository.findAll();
        assertThat(ongRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllOngRequests() throws Exception {
        // Initialize the database
        ongRequestRepository.saveAndFlush(ongRequest);

        // Get all the ongRequestList
        restOngRequestMockMvc.perform(get("/api/ong-requests?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ongRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].idno").value(hasItem(DEFAULT_IDNO)))
            .andExpect(jsonPath("$.[*].registrationDate").value(hasItem(DEFAULT_REGISTRATION_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getOngRequest() throws Exception {
        // Initialize the database
        ongRequestRepository.saveAndFlush(ongRequest);

        // Get the ongRequest
        restOngRequestMockMvc.perform(get("/api/ong-requests/{id}", ongRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(ongRequest.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.idno").value(DEFAULT_IDNO))
            .andExpect(jsonPath("$.registrationDate").value(DEFAULT_REGISTRATION_DATE.toString()));
    }


    @Test
    @Transactional
    public void getOngRequestsByIdFiltering() throws Exception {
        // Initialize the database
        ongRequestRepository.saveAndFlush(ongRequest);

        Long id = ongRequest.getId();

        defaultOngRequestShouldBeFound("id.equals=" + id);
        defaultOngRequestShouldNotBeFound("id.notEquals=" + id);

        defaultOngRequestShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultOngRequestShouldNotBeFound("id.greaterThan=" + id);

        defaultOngRequestShouldBeFound("id.lessThanOrEqual=" + id);
        defaultOngRequestShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllOngRequestsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        ongRequestRepository.saveAndFlush(ongRequest);

        // Get all the ongRequestList where name equals to DEFAULT_NAME
        defaultOngRequestShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the ongRequestList where name equals to UPDATED_NAME
        defaultOngRequestShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOngRequestsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ongRequestRepository.saveAndFlush(ongRequest);

        // Get all the ongRequestList where name not equals to DEFAULT_NAME
        defaultOngRequestShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the ongRequestList where name not equals to UPDATED_NAME
        defaultOngRequestShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOngRequestsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        ongRequestRepository.saveAndFlush(ongRequest);

        // Get all the ongRequestList where name in DEFAULT_NAME or UPDATED_NAME
        defaultOngRequestShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the ongRequestList where name equals to UPDATED_NAME
        defaultOngRequestShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOngRequestsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        ongRequestRepository.saveAndFlush(ongRequest);

        // Get all the ongRequestList where name is not null
        defaultOngRequestShouldBeFound("name.specified=true");

        // Get all the ongRequestList where name is null
        defaultOngRequestShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllOngRequestsByNameContainsSomething() throws Exception {
        // Initialize the database
        ongRequestRepository.saveAndFlush(ongRequest);

        // Get all the ongRequestList where name contains DEFAULT_NAME
        defaultOngRequestShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the ongRequestList where name contains UPDATED_NAME
        defaultOngRequestShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllOngRequestsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        ongRequestRepository.saveAndFlush(ongRequest);

        // Get all the ongRequestList where name does not contain DEFAULT_NAME
        defaultOngRequestShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the ongRequestList where name does not contain UPDATED_NAME
        defaultOngRequestShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllOngRequestsByIdnoIsEqualToSomething() throws Exception {
        // Initialize the database
        ongRequestRepository.saveAndFlush(ongRequest);

        // Get all the ongRequestList where idno equals to DEFAULT_IDNO
        defaultOngRequestShouldBeFound("idno.equals=" + DEFAULT_IDNO);

        // Get all the ongRequestList where idno equals to UPDATED_IDNO
        defaultOngRequestShouldNotBeFound("idno.equals=" + UPDATED_IDNO);
    }

    @Test
    @Transactional
    public void getAllOngRequestsByIdnoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ongRequestRepository.saveAndFlush(ongRequest);

        // Get all the ongRequestList where idno not equals to DEFAULT_IDNO
        defaultOngRequestShouldNotBeFound("idno.notEquals=" + DEFAULT_IDNO);

        // Get all the ongRequestList where idno not equals to UPDATED_IDNO
        defaultOngRequestShouldBeFound("idno.notEquals=" + UPDATED_IDNO);
    }

    @Test
    @Transactional
    public void getAllOngRequestsByIdnoIsInShouldWork() throws Exception {
        // Initialize the database
        ongRequestRepository.saveAndFlush(ongRequest);

        // Get all the ongRequestList where idno in DEFAULT_IDNO or UPDATED_IDNO
        defaultOngRequestShouldBeFound("idno.in=" + DEFAULT_IDNO + "," + UPDATED_IDNO);

        // Get all the ongRequestList where idno equals to UPDATED_IDNO
        defaultOngRequestShouldNotBeFound("idno.in=" + UPDATED_IDNO);
    }

    @Test
    @Transactional
    public void getAllOngRequestsByIdnoIsNullOrNotNull() throws Exception {
        // Initialize the database
        ongRequestRepository.saveAndFlush(ongRequest);

        // Get all the ongRequestList where idno is not null
        defaultOngRequestShouldBeFound("idno.specified=true");

        // Get all the ongRequestList where idno is null
        defaultOngRequestShouldNotBeFound("idno.specified=false");
    }
                @Test
    @Transactional
    public void getAllOngRequestsByIdnoContainsSomething() throws Exception {
        // Initialize the database
        ongRequestRepository.saveAndFlush(ongRequest);

        // Get all the ongRequestList where idno contains DEFAULT_IDNO
        defaultOngRequestShouldBeFound("idno.contains=" + DEFAULT_IDNO);

        // Get all the ongRequestList where idno contains UPDATED_IDNO
        defaultOngRequestShouldNotBeFound("idno.contains=" + UPDATED_IDNO);
    }

    @Test
    @Transactional
    public void getAllOngRequestsByIdnoNotContainsSomething() throws Exception {
        // Initialize the database
        ongRequestRepository.saveAndFlush(ongRequest);

        // Get all the ongRequestList where idno does not contain DEFAULT_IDNO
        defaultOngRequestShouldNotBeFound("idno.doesNotContain=" + DEFAULT_IDNO);

        // Get all the ongRequestList where idno does not contain UPDATED_IDNO
        defaultOngRequestShouldBeFound("idno.doesNotContain=" + UPDATED_IDNO);
    }


    @Test
    @Transactional
    public void getAllOngRequestsByRegistrationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        ongRequestRepository.saveAndFlush(ongRequest);

        // Get all the ongRequestList where registrationDate equals to DEFAULT_REGISTRATION_DATE
        defaultOngRequestShouldBeFound("registrationDate.equals=" + DEFAULT_REGISTRATION_DATE);

        // Get all the ongRequestList where registrationDate equals to UPDATED_REGISTRATION_DATE
        defaultOngRequestShouldNotBeFound("registrationDate.equals=" + UPDATED_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    public void getAllOngRequestsByRegistrationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        ongRequestRepository.saveAndFlush(ongRequest);

        // Get all the ongRequestList where registrationDate not equals to DEFAULT_REGISTRATION_DATE
        defaultOngRequestShouldNotBeFound("registrationDate.notEquals=" + DEFAULT_REGISTRATION_DATE);

        // Get all the ongRequestList where registrationDate not equals to UPDATED_REGISTRATION_DATE
        defaultOngRequestShouldBeFound("registrationDate.notEquals=" + UPDATED_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    public void getAllOngRequestsByRegistrationDateIsInShouldWork() throws Exception {
        // Initialize the database
        ongRequestRepository.saveAndFlush(ongRequest);

        // Get all the ongRequestList where registrationDate in DEFAULT_REGISTRATION_DATE or UPDATED_REGISTRATION_DATE
        defaultOngRequestShouldBeFound("registrationDate.in=" + DEFAULT_REGISTRATION_DATE + "," + UPDATED_REGISTRATION_DATE);

        // Get all the ongRequestList where registrationDate equals to UPDATED_REGISTRATION_DATE
        defaultOngRequestShouldNotBeFound("registrationDate.in=" + UPDATED_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    public void getAllOngRequestsByRegistrationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        ongRequestRepository.saveAndFlush(ongRequest);

        // Get all the ongRequestList where registrationDate is not null
        defaultOngRequestShouldBeFound("registrationDate.specified=true");

        // Get all the ongRequestList where registrationDate is null
        defaultOngRequestShouldNotBeFound("registrationDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllOngRequestsByRegistrationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ongRequestRepository.saveAndFlush(ongRequest);

        // Get all the ongRequestList where registrationDate is greater than or equal to DEFAULT_REGISTRATION_DATE
        defaultOngRequestShouldBeFound("registrationDate.greaterThanOrEqual=" + DEFAULT_REGISTRATION_DATE);

        // Get all the ongRequestList where registrationDate is greater than or equal to UPDATED_REGISTRATION_DATE
        defaultOngRequestShouldNotBeFound("registrationDate.greaterThanOrEqual=" + UPDATED_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    public void getAllOngRequestsByRegistrationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        ongRequestRepository.saveAndFlush(ongRequest);

        // Get all the ongRequestList where registrationDate is less than or equal to DEFAULT_REGISTRATION_DATE
        defaultOngRequestShouldBeFound("registrationDate.lessThanOrEqual=" + DEFAULT_REGISTRATION_DATE);

        // Get all the ongRequestList where registrationDate is less than or equal to SMALLER_REGISTRATION_DATE
        defaultOngRequestShouldNotBeFound("registrationDate.lessThanOrEqual=" + SMALLER_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    public void getAllOngRequestsByRegistrationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        ongRequestRepository.saveAndFlush(ongRequest);

        // Get all the ongRequestList where registrationDate is less than DEFAULT_REGISTRATION_DATE
        defaultOngRequestShouldNotBeFound("registrationDate.lessThan=" + DEFAULT_REGISTRATION_DATE);

        // Get all the ongRequestList where registrationDate is less than UPDATED_REGISTRATION_DATE
        defaultOngRequestShouldBeFound("registrationDate.lessThan=" + UPDATED_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    public void getAllOngRequestsByRegistrationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        ongRequestRepository.saveAndFlush(ongRequest);

        // Get all the ongRequestList where registrationDate is greater than DEFAULT_REGISTRATION_DATE
        defaultOngRequestShouldNotBeFound("registrationDate.greaterThan=" + DEFAULT_REGISTRATION_DATE);

        // Get all the ongRequestList where registrationDate is greater than SMALLER_REGISTRATION_DATE
        defaultOngRequestShouldBeFound("registrationDate.greaterThan=" + SMALLER_REGISTRATION_DATE);
    }


    @Test
    @Transactional
    public void getAllOngRequestsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        ongRequestRepository.saveAndFlush(ongRequest);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        ongRequest.setUser(user);
        ongRequestRepository.saveAndFlush(ongRequest);
        Long userId = user.getId();

        // Get all the ongRequestList where user equals to userId
        defaultOngRequestShouldBeFound("userId.equals=" + userId);

        // Get all the ongRequestList where user equals to userId + 1
        defaultOngRequestShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOngRequestShouldBeFound(String filter) throws Exception {
        restOngRequestMockMvc.perform(get("/api/ong-requests?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ongRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].idno").value(hasItem(DEFAULT_IDNO)))
            .andExpect(jsonPath("$.[*].registrationDate").value(hasItem(DEFAULT_REGISTRATION_DATE.toString())));

        // Check, that the count call also returns 1
        restOngRequestMockMvc.perform(get("/api/ong-requests/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOngRequestShouldNotBeFound(String filter) throws Exception {
        restOngRequestMockMvc.perform(get("/api/ong-requests?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOngRequestMockMvc.perform(get("/api/ong-requests/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingOngRequest() throws Exception {
        // Get the ongRequest
        restOngRequestMockMvc.perform(get("/api/ong-requests/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOngRequest() throws Exception {
        // Initialize the database
        ongRequestService.save(ongRequest);

        int databaseSizeBeforeUpdate = ongRequestRepository.findAll().size();

        // Update the ongRequest
        OngRequest updatedOngRequest = ongRequestRepository.findById(ongRequest.getId()).get();
        // Disconnect from session so that the updates on updatedOngRequest are not directly saved in db
        em.detach(updatedOngRequest);
        updatedOngRequest
            .name(UPDATED_NAME)
            .idno(UPDATED_IDNO)
            .registrationDate(UPDATED_REGISTRATION_DATE);

        restOngRequestMockMvc.perform(put("/api/ong-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedOngRequest)))
            .andExpect(status().isOk());

        // Validate the OngRequest in the database
        List<OngRequest> ongRequestList = ongRequestRepository.findAll();
        assertThat(ongRequestList).hasSize(databaseSizeBeforeUpdate);
        OngRequest testOngRequest = ongRequestList.get(ongRequestList.size() - 1);
        assertThat(testOngRequest.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testOngRequest.getIdno()).isEqualTo(UPDATED_IDNO);
        assertThat(testOngRequest.getRegistrationDate()).isEqualTo(UPDATED_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingOngRequest() throws Exception {
        int databaseSizeBeforeUpdate = ongRequestRepository.findAll().size();

        // Create the OngRequest

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOngRequestMockMvc.perform(put("/api/ong-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(ongRequest)))
            .andExpect(status().isBadRequest());

        // Validate the OngRequest in the database
        List<OngRequest> ongRequestList = ongRequestRepository.findAll();
        assertThat(ongRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOngRequest() throws Exception {
        // Initialize the database
        ongRequestService.save(ongRequest);

        int databaseSizeBeforeDelete = ongRequestRepository.findAll().size();

        // Delete the ongRequest
        restOngRequestMockMvc.perform(delete("/api/ong-requests/{id}", ongRequest.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OngRequest> ongRequestList = ongRequestRepository.findAll();
        assertThat(ongRequestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
