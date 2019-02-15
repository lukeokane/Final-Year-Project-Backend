package com.itlc.thelearningzone.web.rest;

import com.itlc.thelearningzone.ThelearningzoneApp;
import com.itlc.thelearningzone.domain.User;
import com.itlc.thelearningzone.domain.UserInfo;
import com.itlc.thelearningzone.repository.UserInfoRepository;
import com.itlc.thelearningzone.repository.UserRepository;
import com.itlc.thelearningzone.security.AuthoritiesConstants;
import com.itlc.thelearningzone.service.MailService;
import com.itlc.thelearningzone.service.UserInfoService;
import com.itlc.thelearningzone.service.UserService;
import com.itlc.thelearningzone.service.dto.UserInfoDTO;
import com.itlc.thelearningzone.service.mapper.UserInfoMapper;
import com.itlc.thelearningzone.web.rest.errors.ExceptionTranslator;
import com.itlc.thelearningzone.web.rest.vm.ManagedUserVM;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.util.Collections;
import java.util.List;


import static com.itlc.thelearningzone.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the UserInfoResource REST controller.
 *
 * @see UserInfoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ThelearningzoneApp.class)
public class UserInfoResourceIntTest {

    private static final String DEFAULT_TUTOR_SKILLS = "AAAAAAAAAA";
    private static final String UPDATED_TUTOR_SKILLS = "BBBBBBBBBB";

    private static final String DEFAULT_PROFILE_IMAGE_URL = "AAAAAAAAAA";
    private static final String UPDATED_PROFILE_IMAGE_URL = "BBBBBBBBBB";
    
    private static final String DEFAULT_LOGIN = "johndoe";
    
    private static final String DEFAULT_PASSWORD = "passjohndoe";
    private static final String UPDATED_PASSWORD = "passjhipster";

    private static final String DEFAULT_EMAIL = "johndoe@localhost";
    private static final String UPDATED_EMAIL = "jhipster@localhost";

    private static final String DEFAULT_FIRSTNAME = "john";
    private static final String UPDATED_FIRSTNAME = "jhipsterFirstName";

    private static final String DEFAULT_LASTNAME = "doe";
    private static final String UPDATED_LASTNAME = "jhipsterLastName";

    private static final String DEFAULT_IMAGEURL = "http://placehold.it/50x50";
    private static final String UPDATED_IMAGEURL = "http://placehold.it/40x40";

    private static final String DEFAULT_LANGKEY = "en";
    private static final String UPDATED_LANGKEY = "fr";

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restUserInfoMockMvc;

    private UserInfo userInfo;
    
    private User user;
    
    @Autowired
    private CacheManager cacheManager;

    private MockMvc restUserMockMvc;
    
    @Autowired
    private MailService mailService;

    @Autowired
    private UserService userService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserInfoResource userInfoResource = new UserInfoResource(userInfoService);
        this.restUserInfoMockMvc = MockMvcBuilders.standaloneSetup(userInfoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
        
        cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE).clear();
        cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE).clear();
        UserResource userResource = new UserResource(userService, userRepository, mailService);

        this.restUserMockMvc = MockMvcBuilders.standaloneSetup(userResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter)
            .build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserInfo createEntity(EntityManager em) {
        UserInfo userInfo = new UserInfo()
            .tutorSkills(DEFAULT_TUTOR_SKILLS)
            .profileImageURL(DEFAULT_PROFILE_IMAGE_URL);
        return userInfo;
    }
    
    public static User createUserEntity(EntityManager em) {
		User user = new User();
		user.setLogin(DEFAULT_LOGIN + RandomStringUtils.randomAlphabetic(5));
		user.setPassword(RandomStringUtils.random(60));

		return user;
	}

    @Before
    public void initTest() {
    	user = createUserEntity(em);
        userInfo = createEntity(em);
    }

//    @Test
//    @Transactional
//    public void createUserInfo() throws Exception {
//        int databaseSizeBeforeCreate = userInfoRepository.findAll().size();
//        
//        userRepository.saveAndFlush(user);
//        em.detach(user);
//        
//     // Create the User
//        ManagedUserVM managedUserVM = new ManagedUserVM();
//
//        managedUserVM.setLogin(DEFAULT_LOGIN);
//        managedUserVM.setPassword(DEFAULT_PASSWORD);
//        managedUserVM.setFirstName(DEFAULT_FIRSTNAME);
//        managedUserVM.setLastName(DEFAULT_LASTNAME);
//        managedUserVM.setEmail(DEFAULT_EMAIL);
//        managedUserVM.setActivated(true);
//        managedUserVM.setImageUrl(DEFAULT_IMAGEURL);
//        managedUserVM.setLangKey(DEFAULT_LANGKEY);
//        managedUserVM.setAuthorities(Collections.singleton(AuthoritiesConstants.USER));
//        
//        restUserMockMvc.perform(post("/api/users")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(managedUserVM)))
//            .andExpect(status().isCreated());
//        
//        List<User> userList = userRepository.findAll();
//        User user2 = userList.get(userList.size() - 1);
//        //UserInfo userInfo2 = new UserInfo();
//        //userInfo.setId(user2.getId());
//        //userInfo.setUser(user2);
//        
//        UserInfoDTO userInfoDTO = userInfoMapper.toDto(userInfo);
//        restUserInfoMockMvc.perform(post("/api/user-infos")
//            .contentType(TestUtil.APPLICATION_JSON_UTF8)
//            .content(TestUtil.convertObjectToJsonBytes(userInfoDTO)))
//            .andExpect(status().isCreated());
//
//        // Validate the UserInfo in the database
//        List<UserInfo> userInfoList = userInfoRepository.findAll();
//        assertThat(userInfoList).hasSize(databaseSizeBeforeCreate + 1);
//        UserInfo testUserInfo = userInfoList.get(userInfoList.size() - 1);
//        assertThat(testUserInfo.getTutorSkills()).isEqualTo(DEFAULT_TUTOR_SKILLS);
//        assertThat(testUserInfo.getProfileImageURL()).isEqualTo(DEFAULT_PROFILE_IMAGE_URL);
//    }

    @Test
    @Transactional
    public void createUserInfoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = userInfoRepository.findAll().size();

        // Create the UserInfo with an existing ID
        userInfo.setId(1L);
        UserInfoDTO userInfoDTO = userInfoMapper.toDto(userInfo);

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserInfoMockMvc.perform(post("/api/user-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userInfoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserInfo in the database
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        assertThat(userInfoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllUserInfos() throws Exception {
    	userInfo.setUser(user);
    	
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get all the userInfoList
        restUserInfoMockMvc.perform(get("/api/user-infos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userInfo.getId().intValue())))
            .andExpect(jsonPath("$.[*].tutorSkills").value(hasItem(DEFAULT_TUTOR_SKILLS.toString())))
            .andExpect(jsonPath("$.[*].profileImageURL").value(hasItem(DEFAULT_PROFILE_IMAGE_URL.toString())));
    }
    
    @Test
    @Transactional
    public void getUserInfo() throws Exception {
    	userInfo.setUser(user);
    	
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        // Get the userInfo
        restUserInfoMockMvc.perform(get("/api/user-infos/{id}", userInfo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(userInfo.getId().intValue()))
            .andExpect(jsonPath("$.tutorSkills").value(DEFAULT_TUTOR_SKILLS.toString()))
            .andExpect(jsonPath("$.profileImageURL").value(DEFAULT_PROFILE_IMAGE_URL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserInfo() throws Exception {
        // Get the userInfo
        restUserInfoMockMvc.perform(get("/api/user-infos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

   @Test
   @Transactional
   public void updateUserInfo() throws Exception {
	   userInfo.setUser(user);
	   
       // Initialize the database
       userInfoRepository.saveAndFlush(userInfo);

        int databaseSizeBeforeUpdate = userInfoRepository.findAll().size();

        // Update the userInfo
        UserInfo updatedUserInfo = userInfoRepository.findById(userInfo.getId()).get();
        // Disconnect from session so that the updates on updatedUserInfo are not directly saved in db
        em.detach(updatedUserInfo);
        updatedUserInfo
            .tutorSkills(UPDATED_TUTOR_SKILLS)
            .profileImageURL(UPDATED_PROFILE_IMAGE_URL);
        UserInfoDTO userInfoDTO = userInfoMapper.toDto(updatedUserInfo);

        restUserInfoMockMvc.perform(put("/api/user-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userInfoDTO)))
            .andExpect(status().isOk());

        // Validate the UserInfo in the database
        List<UserInfo> userInfoList = userInfoRepository.findAll();
       assertThat(userInfoList).hasSize(databaseSizeBeforeUpdate);
        UserInfo testUserInfo = userInfoList.get(userInfoList.size() - 1);
        assertThat(testUserInfo.getTutorSkills()).isEqualTo(UPDATED_TUTOR_SKILLS);
        assertThat(testUserInfo.getProfileImageURL()).isEqualTo(UPDATED_PROFILE_IMAGE_URL);
    }

    @Test
    @Transactional
    public void updateNonExistingUserInfo() throws Exception {
        int databaseSizeBeforeUpdate = userInfoRepository.findAll().size();

        // Create the UserInfo
        UserInfoDTO userInfoDTO = userInfoMapper.toDto(userInfo);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserInfoMockMvc.perform(put("/api/user-infos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(userInfoDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserInfo in the database
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        assertThat(userInfoList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteUserInfo() throws Exception {
    	userInfo.setUser(user);
    	
        // Initialize the database
        userInfoRepository.saveAndFlush(userInfo);

        int databaseSizeBeforeDelete = userInfoRepository.findAll().size();

        // Get the userInfo
        restUserInfoMockMvc.perform(delete("/api/user-infos/{id}", userInfo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<UserInfo> userInfoList = userInfoRepository.findAll();
        assertThat(userInfoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserInfo.class);
        UserInfo userInfo1 = new UserInfo();
        userInfo1.setId(1L);
        UserInfo userInfo2 = new UserInfo();
        userInfo2.setId(userInfo1.getId());
        assertThat(userInfo1).isEqualTo(userInfo2);
        userInfo2.setId(2L);
        assertThat(userInfo1).isNotEqualTo(userInfo2);
        userInfo1.setId(null);
        assertThat(userInfo1).isNotEqualTo(userInfo2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserInfoDTO.class);
        UserInfoDTO userInfoDTO1 = new UserInfoDTO();
        userInfoDTO1.setId(1L);
        UserInfoDTO userInfoDTO2 = new UserInfoDTO();
        assertThat(userInfoDTO1).isNotEqualTo(userInfoDTO2);
        userInfoDTO2.setId(userInfoDTO1.getId());
        assertThat(userInfoDTO1).isEqualTo(userInfoDTO2);
        userInfoDTO2.setId(2L);
        assertThat(userInfoDTO1).isNotEqualTo(userInfoDTO2);
        userInfoDTO1.setId(null);
        assertThat(userInfoDTO1).isNotEqualTo(userInfoDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(userInfoMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(userInfoMapper.fromId(null)).isNull();
    }
}
