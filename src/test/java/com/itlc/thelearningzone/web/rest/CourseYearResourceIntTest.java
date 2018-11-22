package com.itlc.thelearningzone.web.rest;

import com.itlc.thelearningzone.ThelearningzoneApp;

import com.itlc.thelearningzone.domain.CourseYear;
import com.itlc.thelearningzone.repository.CourseYearRepository;
import com.itlc.thelearningzone.service.CourseYearService;
import com.itlc.thelearningzone.service.dto.CourseYearDTO;
import com.itlc.thelearningzone.service.mapper.CourseYearMapper;
import com.itlc.thelearningzone.web.rest.errors.ExceptionTranslator;

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

import javax.persistence.EntityManager;
import java.util.List;


import static com.itlc.thelearningzone.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CourseYearResource REST controller.
 *
 * @see CourseYearResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ThelearningzoneApp.class)
public class CourseYearResourceIntTest {

    private static final Integer DEFAULT_COURSE_YEAR = 1;
    private static final Integer UPDATED_COURSE_YEAR = 2;

    @Autowired
    private CourseYearRepository courseYearRepository;

    @Autowired
    private CourseYearMapper courseYearMapper;

    @Autowired
    private CourseYearService courseYearService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restCourseYearMockMvc;

    private CourseYear courseYear;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CourseYearResource courseYearResource = new CourseYearResource(courseYearService);
        this.restCourseYearMockMvc = MockMvcBuilders.standaloneSetup(courseYearResource)
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
    public static CourseYear createEntity(EntityManager em) {
        CourseYear courseYear = new CourseYear()
            .courseYear(DEFAULT_COURSE_YEAR);
        return courseYear;
    }

    @Before
    public void initTest() {
        courseYear = createEntity(em);
    }

    @Test
    @Transactional
    public void createCourseYear() throws Exception {
        int databaseSizeBeforeCreate = courseYearRepository.findAll().size();

        // Create the CourseYear
        CourseYearDTO courseYearDTO = courseYearMapper.toDto(courseYear);
        restCourseYearMockMvc.perform(post("/api/course-years")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseYearDTO)))
            .andExpect(status().isCreated());

        // Validate the CourseYear in the database
        List<CourseYear> courseYearList = courseYearRepository.findAll();
        assertThat(courseYearList).hasSize(databaseSizeBeforeCreate + 1);
        CourseYear testCourseYear = courseYearList.get(courseYearList.size() - 1);
        assertThat(testCourseYear.getCourseYear()).isEqualTo(DEFAULT_COURSE_YEAR);
    }

    @Test
    @Transactional
    public void createCourseYearWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = courseYearRepository.findAll().size();

        // Create the CourseYear with an existing ID
        courseYear.setId(1L);
        CourseYearDTO courseYearDTO = courseYearMapper.toDto(courseYear);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseYearMockMvc.perform(post("/api/course-years")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseYearDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CourseYear in the database
        List<CourseYear> courseYearList = courseYearRepository.findAll();
        assertThat(courseYearList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCourseYearIsRequired() throws Exception {
        int databaseSizeBeforeTest = courseYearRepository.findAll().size();
        // set the field null
        courseYear.setCourseYear(null);

        // Create the CourseYear, which fails.
        CourseYearDTO courseYearDTO = courseYearMapper.toDto(courseYear);

        restCourseYearMockMvc.perform(post("/api/course-years")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseYearDTO)))
            .andExpect(status().isBadRequest());

        List<CourseYear> courseYearList = courseYearRepository.findAll();
        assertThat(courseYearList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCourseYears() throws Exception {
        // Initialize the database
        courseYearRepository.saveAndFlush(courseYear);

        // Get all the courseYearList
        restCourseYearMockMvc.perform(get("/api/course-years?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(courseYear.getId().intValue())))
            .andExpect(jsonPath("$.[*].courseYear").value(hasItem(DEFAULT_COURSE_YEAR)));
    }
    
    @Test
    @Transactional
    public void getCourseYear() throws Exception {
        // Initialize the database
        courseYearRepository.saveAndFlush(courseYear);

        // Get the courseYear
        restCourseYearMockMvc.perform(get("/api/course-years/{id}", courseYear.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(courseYear.getId().intValue()))
            .andExpect(jsonPath("$.courseYear").value(DEFAULT_COURSE_YEAR));
    }

    @Test
    @Transactional
    public void getNonExistingCourseYear() throws Exception {
        // Get the courseYear
        restCourseYearMockMvc.perform(get("/api/course-years/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCourseYear() throws Exception {
        // Initialize the database
        courseYearRepository.saveAndFlush(courseYear);

        int databaseSizeBeforeUpdate = courseYearRepository.findAll().size();

        // Update the courseYear
        CourseYear updatedCourseYear = courseYearRepository.findById(courseYear.getId()).get();
        // Disconnect from session so that the updates on updatedCourseYear are not directly saved in db
        em.detach(updatedCourseYear);
        updatedCourseYear
            .courseYear(UPDATED_COURSE_YEAR);
        CourseYearDTO courseYearDTO = courseYearMapper.toDto(updatedCourseYear);

        restCourseYearMockMvc.perform(put("/api/course-years")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseYearDTO)))
            .andExpect(status().isOk());

        // Validate the CourseYear in the database
        List<CourseYear> courseYearList = courseYearRepository.findAll();
        assertThat(courseYearList).hasSize(databaseSizeBeforeUpdate);
        CourseYear testCourseYear = courseYearList.get(courseYearList.size() - 1);
        assertThat(testCourseYear.getCourseYear()).isEqualTo(UPDATED_COURSE_YEAR);
    }

    @Test
    @Transactional
    public void updateNonExistingCourseYear() throws Exception {
        int databaseSizeBeforeUpdate = courseYearRepository.findAll().size();

        // Create the CourseYear
        CourseYearDTO courseYearDTO = courseYearMapper.toDto(courseYear);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseYearMockMvc.perform(put("/api/course-years")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(courseYearDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CourseYear in the database
        List<CourseYear> courseYearList = courseYearRepository.findAll();
        assertThat(courseYearList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCourseYear() throws Exception {
        // Initialize the database
        courseYearRepository.saveAndFlush(courseYear);

        int databaseSizeBeforeDelete = courseYearRepository.findAll().size();

        // Get the courseYear
        restCourseYearMockMvc.perform(delete("/api/course-years/{id}", courseYear.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<CourseYear> courseYearList = courseYearRepository.findAll();
        assertThat(courseYearList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseYear.class);
        CourseYear courseYear1 = new CourseYear();
        courseYear1.setId(1L);
        CourseYear courseYear2 = new CourseYear();
        courseYear2.setId(courseYear1.getId());
        assertThat(courseYear1).isEqualTo(courseYear2);
        courseYear2.setId(2L);
        assertThat(courseYear1).isNotEqualTo(courseYear2);
        courseYear1.setId(null);
        assertThat(courseYear1).isNotEqualTo(courseYear2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseYearDTO.class);
        CourseYearDTO courseYearDTO1 = new CourseYearDTO();
        courseYearDTO1.setId(1L);
        CourseYearDTO courseYearDTO2 = new CourseYearDTO();
        assertThat(courseYearDTO1).isNotEqualTo(courseYearDTO2);
        courseYearDTO2.setId(courseYearDTO1.getId());
        assertThat(courseYearDTO1).isEqualTo(courseYearDTO2);
        courseYearDTO2.setId(2L);
        assertThat(courseYearDTO1).isNotEqualTo(courseYearDTO2);
        courseYearDTO1.setId(null);
        assertThat(courseYearDTO1).isNotEqualTo(courseYearDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(courseYearMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(courseYearMapper.fromId(null)).isNull();
    }
}
