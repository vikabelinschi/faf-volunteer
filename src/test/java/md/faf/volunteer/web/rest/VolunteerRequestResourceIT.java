package md.faf.volunteer.web.rest;

import md.faf.volunteer.VolunteerApp;
import md.faf.volunteer.domain.VolunteerRequest;
import md.faf.volunteer.domain.User;
import md.faf.volunteer.domain.Project;
import md.faf.volunteer.repository.VolunteerRequestRepository;
import md.faf.volunteer.service.VolunteerRequestService;
import md.faf.volunteer.web.rest.errors.ExceptionTranslator;
import md.faf.volunteer.service.dto.VolunteerRequestCriteria;
import md.faf.volunteer.service.VolunteerRequestQueryService;

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
 * Integration tests for the {@link VolunteerRequestResource} REST controller.
 */
@SpringBootTest(classes = VolunteerApp.class)
public class VolunteerRequestResourceIT {

    private static final LocalDate DEFAULT_REGISTRATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REGISTRATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REGISTRATION_DATE = LocalDate.ofEpochDay(-1L);

    @Autowired
    private VolunteerRequestRepository volunteerRequestRepository;

    @Autowired
    private VolunteerRequestService volunteerRequestService;

    @Autowired
    private VolunteerRequestQueryService volunteerRequestQueryService;

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

    private MockMvc restVolunteerRequestMockMvc;

    private VolunteerRequest volunteerRequest;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final VolunteerRequestResource volunteerRequestResource = new VolunteerRequestResource(volunteerRequestService, volunteerRequestQueryService);
        this.restVolunteerRequestMockMvc = MockMvcBuilders.standaloneSetup(volunteerRequestResource)
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
    public static VolunteerRequest createEntity(EntityManager em) {
        VolunteerRequest volunteerRequest = new VolunteerRequest()
            .registrationDate(DEFAULT_REGISTRATION_DATE);
        return volunteerRequest;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static VolunteerRequest createUpdatedEntity(EntityManager em) {
        VolunteerRequest volunteerRequest = new VolunteerRequest()
            .registrationDate(UPDATED_REGISTRATION_DATE);
        return volunteerRequest;
    }

    @BeforeEach
    public void initTest() {
        volunteerRequest = createEntity(em);
    }

    @Test
    @Transactional
    public void createVolunteerRequest() throws Exception {
        int databaseSizeBeforeCreate = volunteerRequestRepository.findAll().size();

        // Create the VolunteerRequest
        restVolunteerRequestMockMvc.perform(post("/api/volunteer-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(volunteerRequest)))
            .andExpect(status().isCreated());

        // Validate the VolunteerRequest in the database
        List<VolunteerRequest> volunteerRequestList = volunteerRequestRepository.findAll();
        assertThat(volunteerRequestList).hasSize(databaseSizeBeforeCreate + 1);
        VolunteerRequest testVolunteerRequest = volunteerRequestList.get(volunteerRequestList.size() - 1);
        assertThat(testVolunteerRequest.getRegistrationDate()).isEqualTo(DEFAULT_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    public void createVolunteerRequestWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = volunteerRequestRepository.findAll().size();

        // Create the VolunteerRequest with an existing ID
        volunteerRequest.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVolunteerRequestMockMvc.perform(post("/api/volunteer-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(volunteerRequest)))
            .andExpect(status().isBadRequest());

        // Validate the VolunteerRequest in the database
        List<VolunteerRequest> volunteerRequestList = volunteerRequestRepository.findAll();
        assertThat(volunteerRequestList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllVolunteerRequests() throws Exception {
        // Initialize the database
        volunteerRequestRepository.saveAndFlush(volunteerRequest);

        // Get all the volunteerRequestList
        restVolunteerRequestMockMvc.perform(get("/api/volunteer-requests?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(volunteerRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].registrationDate").value(hasItem(DEFAULT_REGISTRATION_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getVolunteerRequest() throws Exception {
        // Initialize the database
        volunteerRequestRepository.saveAndFlush(volunteerRequest);

        // Get the volunteerRequest
        restVolunteerRequestMockMvc.perform(get("/api/volunteer-requests/{id}", volunteerRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(volunteerRequest.getId().intValue()))
            .andExpect(jsonPath("$.registrationDate").value(DEFAULT_REGISTRATION_DATE.toString()));
    }


    @Test
    @Transactional
    public void getVolunteerRequestsByIdFiltering() throws Exception {
        // Initialize the database
        volunteerRequestRepository.saveAndFlush(volunteerRequest);

        Long id = volunteerRequest.getId();

        defaultVolunteerRequestShouldBeFound("id.equals=" + id);
        defaultVolunteerRequestShouldNotBeFound("id.notEquals=" + id);

        defaultVolunteerRequestShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultVolunteerRequestShouldNotBeFound("id.greaterThan=" + id);

        defaultVolunteerRequestShouldBeFound("id.lessThanOrEqual=" + id);
        defaultVolunteerRequestShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllVolunteerRequestsByRegistrationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        volunteerRequestRepository.saveAndFlush(volunteerRequest);

        // Get all the volunteerRequestList where registrationDate equals to DEFAULT_REGISTRATION_DATE
        defaultVolunteerRequestShouldBeFound("registrationDate.equals=" + DEFAULT_REGISTRATION_DATE);

        // Get all the volunteerRequestList where registrationDate equals to UPDATED_REGISTRATION_DATE
        defaultVolunteerRequestShouldNotBeFound("registrationDate.equals=" + UPDATED_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    public void getAllVolunteerRequestsByRegistrationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        volunteerRequestRepository.saveAndFlush(volunteerRequest);

        // Get all the volunteerRequestList where registrationDate not equals to DEFAULT_REGISTRATION_DATE
        defaultVolunteerRequestShouldNotBeFound("registrationDate.notEquals=" + DEFAULT_REGISTRATION_DATE);

        // Get all the volunteerRequestList where registrationDate not equals to UPDATED_REGISTRATION_DATE
        defaultVolunteerRequestShouldBeFound("registrationDate.notEquals=" + UPDATED_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    public void getAllVolunteerRequestsByRegistrationDateIsInShouldWork() throws Exception {
        // Initialize the database
        volunteerRequestRepository.saveAndFlush(volunteerRequest);

        // Get all the volunteerRequestList where registrationDate in DEFAULT_REGISTRATION_DATE or UPDATED_REGISTRATION_DATE
        defaultVolunteerRequestShouldBeFound("registrationDate.in=" + DEFAULT_REGISTRATION_DATE + "," + UPDATED_REGISTRATION_DATE);

        // Get all the volunteerRequestList where registrationDate equals to UPDATED_REGISTRATION_DATE
        defaultVolunteerRequestShouldNotBeFound("registrationDate.in=" + UPDATED_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    public void getAllVolunteerRequestsByRegistrationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        volunteerRequestRepository.saveAndFlush(volunteerRequest);

        // Get all the volunteerRequestList where registrationDate is not null
        defaultVolunteerRequestShouldBeFound("registrationDate.specified=true");

        // Get all the volunteerRequestList where registrationDate is null
        defaultVolunteerRequestShouldNotBeFound("registrationDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllVolunteerRequestsByRegistrationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        volunteerRequestRepository.saveAndFlush(volunteerRequest);

        // Get all the volunteerRequestList where registrationDate is greater than or equal to DEFAULT_REGISTRATION_DATE
        defaultVolunteerRequestShouldBeFound("registrationDate.greaterThanOrEqual=" + DEFAULT_REGISTRATION_DATE);

        // Get all the volunteerRequestList where registrationDate is greater than or equal to UPDATED_REGISTRATION_DATE
        defaultVolunteerRequestShouldNotBeFound("registrationDate.greaterThanOrEqual=" + UPDATED_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    public void getAllVolunteerRequestsByRegistrationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        volunteerRequestRepository.saveAndFlush(volunteerRequest);

        // Get all the volunteerRequestList where registrationDate is less than or equal to DEFAULT_REGISTRATION_DATE
        defaultVolunteerRequestShouldBeFound("registrationDate.lessThanOrEqual=" + DEFAULT_REGISTRATION_DATE);

        // Get all the volunteerRequestList where registrationDate is less than or equal to SMALLER_REGISTRATION_DATE
        defaultVolunteerRequestShouldNotBeFound("registrationDate.lessThanOrEqual=" + SMALLER_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    public void getAllVolunteerRequestsByRegistrationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        volunteerRequestRepository.saveAndFlush(volunteerRequest);

        // Get all the volunteerRequestList where registrationDate is less than DEFAULT_REGISTRATION_DATE
        defaultVolunteerRequestShouldNotBeFound("registrationDate.lessThan=" + DEFAULT_REGISTRATION_DATE);

        // Get all the volunteerRequestList where registrationDate is less than UPDATED_REGISTRATION_DATE
        defaultVolunteerRequestShouldBeFound("registrationDate.lessThan=" + UPDATED_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    public void getAllVolunteerRequestsByRegistrationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        volunteerRequestRepository.saveAndFlush(volunteerRequest);

        // Get all the volunteerRequestList where registrationDate is greater than DEFAULT_REGISTRATION_DATE
        defaultVolunteerRequestShouldNotBeFound("registrationDate.greaterThan=" + DEFAULT_REGISTRATION_DATE);

        // Get all the volunteerRequestList where registrationDate is greater than SMALLER_REGISTRATION_DATE
        defaultVolunteerRequestShouldBeFound("registrationDate.greaterThan=" + SMALLER_REGISTRATION_DATE);
    }


    @Test
    @Transactional
    public void getAllVolunteerRequestsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        volunteerRequestRepository.saveAndFlush(volunteerRequest);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        volunteerRequest.setUser(user);
        volunteerRequestRepository.saveAndFlush(volunteerRequest);
        Long userId = user.getId();

        // Get all the volunteerRequestList where user equals to userId
        defaultVolunteerRequestShouldBeFound("userId.equals=" + userId);

        // Get all the volunteerRequestList where user equals to userId + 1
        defaultVolunteerRequestShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllVolunteerRequestsByProjectIsEqualToSomething() throws Exception {
        // Initialize the database
        volunteerRequestRepository.saveAndFlush(volunteerRequest);
        Project project = ProjectResourceIT.createEntity(em);
        em.persist(project);
        em.flush();
        volunteerRequest.setProject(project);
        volunteerRequestRepository.saveAndFlush(volunteerRequest);
        Long projectId = project.getId();

        // Get all the volunteerRequestList where project equals to projectId
        defaultVolunteerRequestShouldBeFound("projectId.equals=" + projectId);

        // Get all the volunteerRequestList where project equals to projectId + 1
        defaultVolunteerRequestShouldNotBeFound("projectId.equals=" + (projectId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultVolunteerRequestShouldBeFound(String filter) throws Exception {
        restVolunteerRequestMockMvc.perform(get("/api/volunteer-requests?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(volunteerRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].registrationDate").value(hasItem(DEFAULT_REGISTRATION_DATE.toString())));

        // Check, that the count call also returns 1
        restVolunteerRequestMockMvc.perform(get("/api/volunteer-requests/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultVolunteerRequestShouldNotBeFound(String filter) throws Exception {
        restVolunteerRequestMockMvc.perform(get("/api/volunteer-requests?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restVolunteerRequestMockMvc.perform(get("/api/volunteer-requests/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingVolunteerRequest() throws Exception {
        // Get the volunteerRequest
        restVolunteerRequestMockMvc.perform(get("/api/volunteer-requests/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVolunteerRequest() throws Exception {
        // Initialize the database
        volunteerRequestService.save(volunteerRequest);

        int databaseSizeBeforeUpdate = volunteerRequestRepository.findAll().size();

        // Update the volunteerRequest
        VolunteerRequest updatedVolunteerRequest = volunteerRequestRepository.findById(volunteerRequest.getId()).get();
        // Disconnect from session so that the updates on updatedVolunteerRequest are not directly saved in db
        em.detach(updatedVolunteerRequest);
        updatedVolunteerRequest
            .registrationDate(UPDATED_REGISTRATION_DATE);

        restVolunteerRequestMockMvc.perform(put("/api/volunteer-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVolunteerRequest)))
            .andExpect(status().isOk());

        // Validate the VolunteerRequest in the database
        List<VolunteerRequest> volunteerRequestList = volunteerRequestRepository.findAll();
        assertThat(volunteerRequestList).hasSize(databaseSizeBeforeUpdate);
        VolunteerRequest testVolunteerRequest = volunteerRequestList.get(volunteerRequestList.size() - 1);
        assertThat(testVolunteerRequest.getRegistrationDate()).isEqualTo(UPDATED_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingVolunteerRequest() throws Exception {
        int databaseSizeBeforeUpdate = volunteerRequestRepository.findAll().size();

        // Create the VolunteerRequest

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restVolunteerRequestMockMvc.perform(put("/api/volunteer-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(volunteerRequest)))
            .andExpect(status().isBadRequest());

        // Validate the VolunteerRequest in the database
        List<VolunteerRequest> volunteerRequestList = volunteerRequestRepository.findAll();
        assertThat(volunteerRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteVolunteerRequest() throws Exception {
        // Initialize the database
        volunteerRequestService.save(volunteerRequest);

        int databaseSizeBeforeDelete = volunteerRequestRepository.findAll().size();

        // Delete the volunteerRequest
        restVolunteerRequestMockMvc.perform(delete("/api/volunteer-requests/{id}", volunteerRequest.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<VolunteerRequest> volunteerRequestList = volunteerRequestRepository.findAll();
        assertThat(volunteerRequestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
