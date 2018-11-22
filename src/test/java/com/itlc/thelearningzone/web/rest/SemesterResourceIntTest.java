package com.itlc.thelearningzone.web.rest;

import com.itlc.thelearningzone.ThelearningzoneApp;

import com.itlc.thelearningzone.domain.Semester;
import com.itlc.thelearningzone.repository.SemesterRepository;
import com.itlc.thelearningzone.service.SemesterService;
import com.itlc.thelearningzone.service.dto.SemesterDTO;
import com.itlc.thelearningzone.service.mapper.SemesterMapper;
import com.itlc.thelearningzone.web.rest.errors.ExceptionTranslator;

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

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;


import static com.itlc.thelearningzone.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.itlc.thelearningzone.domain.enumeration.SemesterNumber;
/**
 * Test class for the SemesterResource REST controller.
 *
 * @see SemesterResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ThelearningzoneApp.class)
public class SemesterResourceIntTest {

    private static final SemesterNumber DEFAULT_SEMESTER_NUMBER = SemesterNumber.NONE;
    private static final SemesterNumber UPDATED_SEMESTER_NUMBER = SemesterNumber.ONE;

    private static final LocalDate DEFAULT_SEMESTER_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SEMESTER_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_SEMESTER_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_SEMESTER_END_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private SemesterRepository semesterRepository;

    @Mock
    private SemesterRepository semesterRepositoryMock;

    @Autowired
    private SemesterMapper semesterMapper;

    @Mock
    private SemesterService semesterServiceMock;

    @Autowired
    private SemesterService semesterService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSemesterMockMvc;

    private Semester semester;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SemesterResource semesterResource = new SemesterResource(semesterService);
        this.restSemesterMockMvc = MockMvcBuilders.standaloneSetup(semesterResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Semester createEntity(EntityManager em) {
        Semester semester = new Semester()
            .semesterNumber(DEFAULT_SEMESTER_NUMBER)
            .semesterStartDate(DEFAULT_SEMESTER_START_DATE)
            .semesterEndDate(DEFAULT_SEMESTER_END_DATE);
        return semester;
    }

    @Before
    public void initTest() {
        semester = createEntity(em);
    }

    @Test
    @Transactional
    public void createSemester() throws Exception {
        int databaseSizeBeforeCreate = semesterRepository.findAll().size();

        // Create the Semester
        SemesterDTO semesterDTO = semesterMapper.toDto(semester);
        restSemesterMockMvc.perform(post("/api/semesters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(semesterDTO)))
            .andExpect(status().isCreated());

        // Validate the Semester in the database
        List<Semester> semesterList = semesterRepository.findAll();
        assertThat(semesterList).hasSize(databaseSizeBeforeCreate + 1);
        Semester testSemester = semesterList.get(semesterList.size() - 1);
        assertThat(testSemester.getSemesterNumber()).isEqualTo(DEFAULT_SEMESTER_NUMBER);
        assertThat(testSemester.getSemesterStartDate()).isEqualTo(DEFAULT_SEMESTER_START_DATE);
        assertThat(testSemester.getSemesterEndDate()).isEqualTo(DEFAULT_SEMESTER_END_DATE);
    }

    @Test
    @Transactional
    public void createSemesterWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = semesterRepository.findAll().size();

        // Create the Semester with an existing ID
        semester.setId(1L);
        SemesterDTO semesterDTO = semesterMapper.toDto(semester);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSemesterMockMvc.perform(post("/api/semesters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(semesterDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Semester in the database
        List<Semester> semesterList = semesterRepository.findAll();
        assertThat(semesterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkSemesterNumberIsRequired() throws Exception {
        int databaseSizeBeforeTest = semesterRepository.findAll().size();
        // set the field null
        semester.setSemesterNumber(null);

        // Create the Semester, which fails.
        SemesterDTO semesterDTO = semesterMapper.toDto(semester);

        restSemesterMockMvc.perform(post("/api/semesters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(semesterDTO)))
            .andExpect(status().isBadRequest());

        List<Semester> semesterList = semesterRepository.findAll();
        assertThat(semesterList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSemesters() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get all the semesterList
        restSemesterMockMvc.perform(get("/api/semesters?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(semester.getId().intValue())))
            .andExpect(jsonPath("$.[*].semesterNumber").value(hasItem(DEFAULT_SEMESTER_NUMBER.toString())))
            .andExpect(jsonPath("$.[*].semesterStartDate").value(hasItem(DEFAULT_SEMESTER_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].semesterEndDate").value(hasItem(DEFAULT_SEMESTER_END_DATE.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllSemestersWithEagerRelationshipsIsEnabled() throws Exception {
        SemesterResource semesterResource = new SemesterResource(semesterServiceMock);
        when(semesterServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restSemesterMockMvc = MockMvcBuilders.standaloneSetup(semesterResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restSemesterMockMvc.perform(get("/api/semesters?eagerload=true"))
        .andExpect(status().isOk());

        verify(semesterServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllSemestersWithEagerRelationshipsIsNotEnabled() throws Exception {
        SemesterResource semesterResource = new SemesterResource(semesterServiceMock);
            when(semesterServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restSemesterMockMvc = MockMvcBuilders.standaloneSetup(semesterResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restSemesterMockMvc.perform(get("/api/semesters?eagerload=true"))
        .andExpect(status().isOk());

            verify(semesterServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getSemester() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        // Get the semester
        restSemesterMockMvc.perform(get("/api/semesters/{id}", semester.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(semester.getId().intValue()))
            .andExpect(jsonPath("$.semesterNumber").value(DEFAULT_SEMESTER_NUMBER.toString()))
            .andExpect(jsonPath("$.semesterStartDate").value(DEFAULT_SEMESTER_START_DATE.toString()))
            .andExpect(jsonPath("$.semesterEndDate").value(DEFAULT_SEMESTER_END_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSemester() throws Exception {
        // Get the semester
        restSemesterMockMvc.perform(get("/api/semesters/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSemester() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        int databaseSizeBeforeUpdate = semesterRepository.findAll().size();

        // Update the semester
        Semester updatedSemester = semesterRepository.findById(semester.getId()).get();
        // Disconnect from session so that the updates on updatedSemester are not directly saved in db
        em.detach(updatedSemester);
        updatedSemester
            .semesterNumber(UPDATED_SEMESTER_NUMBER)
            .semesterStartDate(UPDATED_SEMESTER_START_DATE)
            .semesterEndDate(UPDATED_SEMESTER_END_DATE);
        SemesterDTO semesterDTO = semesterMapper.toDto(updatedSemester);

        restSemesterMockMvc.perform(put("/api/semesters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(semesterDTO)))
            .andExpect(status().isOk());

        // Validate the Semester in the database
        List<Semester> semesterList = semesterRepository.findAll();
        assertThat(semesterList).hasSize(databaseSizeBeforeUpdate);
        Semester testSemester = semesterList.get(semesterList.size() - 1);
        assertThat(testSemester.getSemesterNumber()).isEqualTo(UPDATED_SEMESTER_NUMBER);
        assertThat(testSemester.getSemesterStartDate()).isEqualTo(UPDATED_SEMESTER_START_DATE);
        assertThat(testSemester.getSemesterEndDate()).isEqualTo(UPDATED_SEMESTER_END_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingSemester() throws Exception {
        int databaseSizeBeforeUpdate = semesterRepository.findAll().size();

        // Create the Semester
        SemesterDTO semesterDTO = semesterMapper.toDto(semester);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSemesterMockMvc.perform(put("/api/semesters")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(semesterDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Semester in the database
        List<Semester> semesterList = semesterRepository.findAll();
        assertThat(semesterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSemester() throws Exception {
        // Initialize the database
        semesterRepository.saveAndFlush(semester);

        int databaseSizeBeforeDelete = semesterRepository.findAll().size();

        // Get the semester
        restSemesterMockMvc.perform(delete("/api/semesters/{id}", semester.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Semester> semesterList = semesterRepository.findAll();
        assertThat(semesterList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Semester.class);
        Semester semester1 = new Semester();
        semester1.setId(1L);
        Semester semester2 = new Semester();
        semester2.setId(semester1.getId());
        assertThat(semester1).isEqualTo(semester2);
        semester2.setId(2L);
        assertThat(semester1).isNotEqualTo(semester2);
        semester1.setId(null);
        assertThat(semester1).isNotEqualTo(semester2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SemesterDTO.class);
        SemesterDTO semesterDTO1 = new SemesterDTO();
        semesterDTO1.setId(1L);
        SemesterDTO semesterDTO2 = new SemesterDTO();
        assertThat(semesterDTO1).isNotEqualTo(semesterDTO2);
        semesterDTO2.setId(semesterDTO1.getId());
        assertThat(semesterDTO1).isEqualTo(semesterDTO2);
        semesterDTO2.setId(2L);
        assertThat(semesterDTO1).isNotEqualTo(semesterDTO2);
        semesterDTO1.setId(null);
        assertThat(semesterDTO1).isNotEqualTo(semesterDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(semesterMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(semesterMapper.fromId(null)).isNull();
    }
}
