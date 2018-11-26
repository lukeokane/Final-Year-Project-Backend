package com.itlc.thelearningzone.web.rest;

import com.itlc.thelearningzone.ThelearningzoneApp;

import com.itlc.thelearningzone.domain.SemesterGroup;
import com.itlc.thelearningzone.repository.SemesterGroupRepository;
import com.itlc.thelearningzone.service.SemesterGroupService;
import com.itlc.thelearningzone.service.dto.SemesterGroupDTO;
import com.itlc.thelearningzone.service.mapper.SemesterGroupMapper;
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
 * Test class for the SemesterGroupResource REST controller.
 *
 * @see SemesterGroupResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ThelearningzoneApp.class)
public class SemesterGroupResourceIntTest {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    @Autowired
    private SemesterGroupRepository semesterGroupRepository;

    @Autowired
    private SemesterGroupMapper semesterGroupMapper;

    @Autowired
    private SemesterGroupService semesterGroupService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSemesterGroupMockMvc;

    private SemesterGroup semesterGroup;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final SemesterGroupResource semesterGroupResource = new SemesterGroupResource(semesterGroupService);
        this.restSemesterGroupMockMvc = MockMvcBuilders.standaloneSetup(semesterGroupResource)
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
    public static SemesterGroup createEntity(EntityManager em) {
        SemesterGroup semesterGroup = new SemesterGroup()
            .title(DEFAULT_TITLE);
        return semesterGroup;
    }

    @Before
    public void initTest() {
        semesterGroup = createEntity(em);
    }

    @Test
    @Transactional
    public void createSemesterGroup() throws Exception {
        int databaseSizeBeforeCreate = semesterGroupRepository.findAll().size();

        // Create the SemesterGroup
        SemesterGroupDTO semesterGroupDTO = semesterGroupMapper.toDto(semesterGroup);
        restSemesterGroupMockMvc.perform(post("/api/semester-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(semesterGroupDTO)))
            .andExpect(status().isCreated());

        // Validate the SemesterGroup in the database
        List<SemesterGroup> semesterGroupList = semesterGroupRepository.findAll();
        assertThat(semesterGroupList).hasSize(databaseSizeBeforeCreate + 1);
        SemesterGroup testSemesterGroup = semesterGroupList.get(semesterGroupList.size() - 1);
        assertThat(testSemesterGroup.getTitle()).isEqualTo(DEFAULT_TITLE);
    }

    @Test
    @Transactional
    public void createSemesterGroupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = semesterGroupRepository.findAll().size();

        // Create the SemesterGroup with an existing ID
        semesterGroup.setId(1L);
        SemesterGroupDTO semesterGroupDTO = semesterGroupMapper.toDto(semesterGroup);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSemesterGroupMockMvc.perform(post("/api/semester-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(semesterGroupDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SemesterGroup in the database
        List<SemesterGroup> semesterGroupList = semesterGroupRepository.findAll();
        assertThat(semesterGroupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = semesterGroupRepository.findAll().size();
        // set the field null
        semesterGroup.setTitle(null);

        // Create the SemesterGroup, which fails.
        SemesterGroupDTO semesterGroupDTO = semesterGroupMapper.toDto(semesterGroup);

        restSemesterGroupMockMvc.perform(post("/api/semester-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(semesterGroupDTO)))
            .andExpect(status().isBadRequest());

        List<SemesterGroup> semesterGroupList = semesterGroupRepository.findAll();
        assertThat(semesterGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSemesterGroups() throws Exception {
        // Initialize the database
        semesterGroupRepository.saveAndFlush(semesterGroup);

        // Get all the semesterGroupList
        restSemesterGroupMockMvc.perform(get("/api/semester-groups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(semesterGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())));
    }
    
    @Test
    @Transactional
    public void getSemesterGroup() throws Exception {
        // Initialize the database
        semesterGroupRepository.saveAndFlush(semesterGroup);

        // Get the semesterGroup
        restSemesterGroupMockMvc.perform(get("/api/semester-groups/{id}", semesterGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(semesterGroup.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSemesterGroup() throws Exception {
        // Get the semesterGroup
        restSemesterGroupMockMvc.perform(get("/api/semester-groups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSemesterGroup() throws Exception {
        // Initialize the database
        semesterGroupRepository.saveAndFlush(semesterGroup);

        int databaseSizeBeforeUpdate = semesterGroupRepository.findAll().size();

        // Update the semesterGroup
        SemesterGroup updatedSemesterGroup = semesterGroupRepository.findById(semesterGroup.getId()).get();
        // Disconnect from session so that the updates on updatedSemesterGroup are not directly saved in db
        em.detach(updatedSemesterGroup);
        updatedSemesterGroup
            .title(UPDATED_TITLE);
        SemesterGroupDTO semesterGroupDTO = semesterGroupMapper.toDto(updatedSemesterGroup);

        restSemesterGroupMockMvc.perform(put("/api/semester-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(semesterGroupDTO)))
            .andExpect(status().isOk());

        // Validate the SemesterGroup in the database
        List<SemesterGroup> semesterGroupList = semesterGroupRepository.findAll();
        assertThat(semesterGroupList).hasSize(databaseSizeBeforeUpdate);
        SemesterGroup testSemesterGroup = semesterGroupList.get(semesterGroupList.size() - 1);
        assertThat(testSemesterGroup.getTitle()).isEqualTo(UPDATED_TITLE);
    }

    @Test
    @Transactional
    public void updateNonExistingSemesterGroup() throws Exception {
        int databaseSizeBeforeUpdate = semesterGroupRepository.findAll().size();

        // Create the SemesterGroup
        SemesterGroupDTO semesterGroupDTO = semesterGroupMapper.toDto(semesterGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSemesterGroupMockMvc.perform(put("/api/semester-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(semesterGroupDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SemesterGroup in the database
        List<SemesterGroup> semesterGroupList = semesterGroupRepository.findAll();
        assertThat(semesterGroupList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSemesterGroup() throws Exception {
        // Initialize the database
        semesterGroupRepository.saveAndFlush(semesterGroup);

        int databaseSizeBeforeDelete = semesterGroupRepository.findAll().size();

        // Get the semesterGroup
        restSemesterGroupMockMvc.perform(delete("/api/semester-groups/{id}", semesterGroup.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<SemesterGroup> semesterGroupList = semesterGroupRepository.findAll();
        assertThat(semesterGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SemesterGroup.class);
        SemesterGroup semesterGroup1 = new SemesterGroup();
        semesterGroup1.setId(1L);
        SemesterGroup semesterGroup2 = new SemesterGroup();
        semesterGroup2.setId(semesterGroup1.getId());
        assertThat(semesterGroup1).isEqualTo(semesterGroup2);
        semesterGroup2.setId(2L);
        assertThat(semesterGroup1).isNotEqualTo(semesterGroup2);
        semesterGroup1.setId(null);
        assertThat(semesterGroup1).isNotEqualTo(semesterGroup2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SemesterGroupDTO.class);
        SemesterGroupDTO semesterGroupDTO1 = new SemesterGroupDTO();
        semesterGroupDTO1.setId(1L);
        SemesterGroupDTO semesterGroupDTO2 = new SemesterGroupDTO();
        assertThat(semesterGroupDTO1).isNotEqualTo(semesterGroupDTO2);
        semesterGroupDTO2.setId(semesterGroupDTO1.getId());
        assertThat(semesterGroupDTO1).isEqualTo(semesterGroupDTO2);
        semesterGroupDTO2.setId(2L);
        assertThat(semesterGroupDTO1).isNotEqualTo(semesterGroupDTO2);
        semesterGroupDTO1.setId(null);
        assertThat(semesterGroupDTO1).isNotEqualTo(semesterGroupDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(semesterGroupMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(semesterGroupMapper.fromId(null)).isNull();
    }
}
