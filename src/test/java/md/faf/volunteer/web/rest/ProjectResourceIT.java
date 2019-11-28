package md.faf.volunteer.web.rest;

import md.faf.volunteer.VolunteerApp;
import md.faf.volunteer.domain.Project;
import md.faf.volunteer.domain.Status;
import md.faf.volunteer.domain.Category;
import md.faf.volunteer.domain.User;
import md.faf.volunteer.domain.Ong;
import md.faf.volunteer.repository.ProjectRepository;
import md.faf.volunteer.service.ProjectService;
import md.faf.volunteer.web.rest.errors.ExceptionTranslator;
import md.faf.volunteer.service.dto.ProjectCriteria;
import md.faf.volunteer.service.ProjectQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
import java.util.ArrayList;
import java.util.List;

import static md.faf.volunteer.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ProjectResource} REST controller.
 */
@SpringBootTest(classes = VolunteerApp.class)
public class ProjectResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_REGISTRATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_REGISTRATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_REGISTRATION_DATE = LocalDate.ofEpochDay(-1L);

    @Autowired
    private ProjectRepository projectRepository;

    @Mock
    private ProjectRepository projectRepositoryMock;

    @Mock
    private ProjectService projectServiceMock;

    @Autowired
    private ProjectService projectService;

    @Autowired
    private ProjectQueryService projectQueryService;

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

    private MockMvc restProjectMockMvc;

    private Project project;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProjectResource projectResource = new ProjectResource(projectService, projectQueryService);
        this.restProjectMockMvc = MockMvcBuilders.standaloneSetup(projectResource)
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
    public static Project createEntity(EntityManager em) {
        Project project = new Project()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .registrationDate(DEFAULT_REGISTRATION_DATE);
        return project;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Project createUpdatedEntity(EntityManager em) {
        Project project = new Project()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .registrationDate(UPDATED_REGISTRATION_DATE);
        return project;
    }

    @BeforeEach
    public void initTest() {
        project = createEntity(em);
    }

    @Test
    @Transactional
    public void createProject() throws Exception {
        int databaseSizeBeforeCreate = projectRepository.findAll().size();

        // Create the Project
        restProjectMockMvc.perform(post("/api/projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(project)))
            .andExpect(status().isCreated());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeCreate + 1);
        Project testProject = projectList.get(projectList.size() - 1);
        assertThat(testProject.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testProject.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testProject.getRegistrationDate()).isEqualTo(DEFAULT_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    public void createProjectWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = projectRepository.findAll().size();

        // Create the Project with an existing ID
        project.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProjectMockMvc.perform(post("/api/projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(project)))
            .andExpect(status().isBadRequest());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectRepository.findAll().size();
        // set the field null
        project.setName(null);

        // Create the Project, which fails.

        restProjectMockMvc.perform(post("/api/projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(project)))
            .andExpect(status().isBadRequest());

        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDescriptionIsRequired() throws Exception {
        int databaseSizeBeforeTest = projectRepository.findAll().size();
        // set the field null
        project.setDescription(null);

        // Create the Project, which fails.

        restProjectMockMvc.perform(post("/api/projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(project)))
            .andExpect(status().isBadRequest());

        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProjects() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList
        restProjectMockMvc.perform(get("/api/projects?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(project.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].registrationDate").value(hasItem(DEFAULT_REGISTRATION_DATE.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllProjectsWithEagerRelationshipsIsEnabled() throws Exception {
        ProjectResource projectResource = new ProjectResource(projectServiceMock, projectQueryService);
        when(projectServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restProjectMockMvc = MockMvcBuilders.standaloneSetup(projectResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restProjectMockMvc.perform(get("/api/projects?eagerload=true"))
        .andExpect(status().isOk());

        verify(projectServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllProjectsWithEagerRelationshipsIsNotEnabled() throws Exception {
        ProjectResource projectResource = new ProjectResource(projectServiceMock, projectQueryService);
            when(projectServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restProjectMockMvc = MockMvcBuilders.standaloneSetup(projectResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restProjectMockMvc.perform(get("/api/projects?eagerload=true"))
        .andExpect(status().isOk());

            verify(projectServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getProject() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get the project
        restProjectMockMvc.perform(get("/api/projects/{id}", project.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(project.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.registrationDate").value(DEFAULT_REGISTRATION_DATE.toString()));
    }


    @Test
    @Transactional
    public void getProjectsByIdFiltering() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        Long id = project.getId();

        defaultProjectShouldBeFound("id.equals=" + id);
        defaultProjectShouldNotBeFound("id.notEquals=" + id);

        defaultProjectShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProjectShouldNotBeFound("id.greaterThan=" + id);

        defaultProjectShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProjectShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllProjectsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where name equals to DEFAULT_NAME
        defaultProjectShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the projectList where name equals to UPDATED_NAME
        defaultProjectShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProjectsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where name not equals to DEFAULT_NAME
        defaultProjectShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the projectList where name not equals to UPDATED_NAME
        defaultProjectShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProjectsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where name in DEFAULT_NAME or UPDATED_NAME
        defaultProjectShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the projectList where name equals to UPDATED_NAME
        defaultProjectShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProjectsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where name is not null
        defaultProjectShouldBeFound("name.specified=true");

        // Get all the projectList where name is null
        defaultProjectShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllProjectsByNameContainsSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where name contains DEFAULT_NAME
        defaultProjectShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the projectList where name contains UPDATED_NAME
        defaultProjectShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllProjectsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where name does not contain DEFAULT_NAME
        defaultProjectShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the projectList where name does not contain UPDATED_NAME
        defaultProjectShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllProjectsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where description equals to DEFAULT_DESCRIPTION
        defaultProjectShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the projectList where description equals to UPDATED_DESCRIPTION
        defaultProjectShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProjectsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where description not equals to DEFAULT_DESCRIPTION
        defaultProjectShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the projectList where description not equals to UPDATED_DESCRIPTION
        defaultProjectShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProjectsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultProjectShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the projectList where description equals to UPDATED_DESCRIPTION
        defaultProjectShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProjectsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where description is not null
        defaultProjectShouldBeFound("description.specified=true");

        // Get all the projectList where description is null
        defaultProjectShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllProjectsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where description contains DEFAULT_DESCRIPTION
        defaultProjectShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the projectList where description contains UPDATED_DESCRIPTION
        defaultProjectShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllProjectsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where description does not contain DEFAULT_DESCRIPTION
        defaultProjectShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the projectList where description does not contain UPDATED_DESCRIPTION
        defaultProjectShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllProjectsByRegistrationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where registrationDate equals to DEFAULT_REGISTRATION_DATE
        defaultProjectShouldBeFound("registrationDate.equals=" + DEFAULT_REGISTRATION_DATE);

        // Get all the projectList where registrationDate equals to UPDATED_REGISTRATION_DATE
        defaultProjectShouldNotBeFound("registrationDate.equals=" + UPDATED_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    public void getAllProjectsByRegistrationDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where registrationDate not equals to DEFAULT_REGISTRATION_DATE
        defaultProjectShouldNotBeFound("registrationDate.notEquals=" + DEFAULT_REGISTRATION_DATE);

        // Get all the projectList where registrationDate not equals to UPDATED_REGISTRATION_DATE
        defaultProjectShouldBeFound("registrationDate.notEquals=" + UPDATED_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    public void getAllProjectsByRegistrationDateIsInShouldWork() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where registrationDate in DEFAULT_REGISTRATION_DATE or UPDATED_REGISTRATION_DATE
        defaultProjectShouldBeFound("registrationDate.in=" + DEFAULT_REGISTRATION_DATE + "," + UPDATED_REGISTRATION_DATE);

        // Get all the projectList where registrationDate equals to UPDATED_REGISTRATION_DATE
        defaultProjectShouldNotBeFound("registrationDate.in=" + UPDATED_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    public void getAllProjectsByRegistrationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where registrationDate is not null
        defaultProjectShouldBeFound("registrationDate.specified=true");

        // Get all the projectList where registrationDate is null
        defaultProjectShouldNotBeFound("registrationDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllProjectsByRegistrationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where registrationDate is greater than or equal to DEFAULT_REGISTRATION_DATE
        defaultProjectShouldBeFound("registrationDate.greaterThanOrEqual=" + DEFAULT_REGISTRATION_DATE);

        // Get all the projectList where registrationDate is greater than or equal to UPDATED_REGISTRATION_DATE
        defaultProjectShouldNotBeFound("registrationDate.greaterThanOrEqual=" + UPDATED_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    public void getAllProjectsByRegistrationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where registrationDate is less than or equal to DEFAULT_REGISTRATION_DATE
        defaultProjectShouldBeFound("registrationDate.lessThanOrEqual=" + DEFAULT_REGISTRATION_DATE);

        // Get all the projectList where registrationDate is less than or equal to SMALLER_REGISTRATION_DATE
        defaultProjectShouldNotBeFound("registrationDate.lessThanOrEqual=" + SMALLER_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    public void getAllProjectsByRegistrationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where registrationDate is less than DEFAULT_REGISTRATION_DATE
        defaultProjectShouldNotBeFound("registrationDate.lessThan=" + DEFAULT_REGISTRATION_DATE);

        // Get all the projectList where registrationDate is less than UPDATED_REGISTRATION_DATE
        defaultProjectShouldBeFound("registrationDate.lessThan=" + UPDATED_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    public void getAllProjectsByRegistrationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);

        // Get all the projectList where registrationDate is greater than DEFAULT_REGISTRATION_DATE
        defaultProjectShouldNotBeFound("registrationDate.greaterThan=" + DEFAULT_REGISTRATION_DATE);

        // Get all the projectList where registrationDate is greater than SMALLER_REGISTRATION_DATE
        defaultProjectShouldBeFound("registrationDate.greaterThan=" + SMALLER_REGISTRATION_DATE);
    }


    @Test
    @Transactional
    public void getAllProjectsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);
        Status status = StatusResourceIT.createEntity(em);
        em.persist(status);
        em.flush();
        project.setStatus(status);
        projectRepository.saveAndFlush(project);
        Long statusId = status.getId();

        // Get all the projectList where status equals to statusId
        defaultProjectShouldBeFound("statusId.equals=" + statusId);

        // Get all the projectList where status equals to statusId + 1
        defaultProjectShouldNotBeFound("statusId.equals=" + (statusId + 1));
    }


    @Test
    @Transactional
    public void getAllProjectsByCategoryIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);
        Category category = CategoryResourceIT.createEntity(em);
        em.persist(category);
        em.flush();
        project.setCategory(category);
        projectRepository.saveAndFlush(project);
        Long categoryId = category.getId();

        // Get all the projectList where category equals to categoryId
        defaultProjectShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the projectList where category equals to categoryId + 1
        defaultProjectShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }


    @Test
    @Transactional
    public void getAllProjectsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        project.addUser(user);
        projectRepository.saveAndFlush(project);
        Long userId = user.getId();

        // Get all the projectList where user equals to userId
        defaultProjectShouldBeFound("userId.equals=" + userId);

        // Get all the projectList where user equals to userId + 1
        defaultProjectShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllProjectsByOngIsEqualToSomething() throws Exception {
        // Initialize the database
        projectRepository.saveAndFlush(project);
        Ong ong = OngResourceIT.createEntity(em);
        em.persist(ong);
        em.flush();
        project.setOng(ong);
        projectRepository.saveAndFlush(project);
        Long ongId = ong.getId();

        // Get all the projectList where ong equals to ongId
        defaultProjectShouldBeFound("ongId.equals=" + ongId);

        // Get all the projectList where ong equals to ongId + 1
        defaultProjectShouldNotBeFound("ongId.equals=" + (ongId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProjectShouldBeFound(String filter) throws Exception {
        restProjectMockMvc.perform(get("/api/projects?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(project.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].registrationDate").value(hasItem(DEFAULT_REGISTRATION_DATE.toString())));

        // Check, that the count call also returns 1
        restProjectMockMvc.perform(get("/api/projects/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProjectShouldNotBeFound(String filter) throws Exception {
        restProjectMockMvc.perform(get("/api/projects?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProjectMockMvc.perform(get("/api/projects/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingProject() throws Exception {
        // Get the project
        restProjectMockMvc.perform(get("/api/projects/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProject() throws Exception {
        // Initialize the database
        projectService.save(project);

        int databaseSizeBeforeUpdate = projectRepository.findAll().size();

        // Update the project
        Project updatedProject = projectRepository.findById(project.getId()).get();
        // Disconnect from session so that the updates on updatedProject are not directly saved in db
        em.detach(updatedProject);
        updatedProject
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .registrationDate(UPDATED_REGISTRATION_DATE);

        restProjectMockMvc.perform(put("/api/projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedProject)))
            .andExpect(status().isOk());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
        Project testProject = projectList.get(projectList.size() - 1);
        assertThat(testProject.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testProject.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testProject.getRegistrationDate()).isEqualTo(UPDATED_REGISTRATION_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingProject() throws Exception {
        int databaseSizeBeforeUpdate = projectRepository.findAll().size();

        // Create the Project

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProjectMockMvc.perform(put("/api/projects")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(project)))
            .andExpect(status().isBadRequest());

        // Validate the Project in the database
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProject() throws Exception {
        // Initialize the database
        projectService.save(project);

        int databaseSizeBeforeDelete = projectRepository.findAll().size();

        // Delete the project
        restProjectMockMvc.perform(delete("/api/projects/{id}", project.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Project> projectList = projectRepository.findAll();
        assertThat(projectList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
