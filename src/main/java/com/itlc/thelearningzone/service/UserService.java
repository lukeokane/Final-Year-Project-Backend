package com.itlc.thelearningzone.service;

import com.itlc.thelearningzone.config.Constants;
import com.itlc.thelearningzone.domain.Authority;
import com.itlc.thelearningzone.domain.CourseYear;
import com.itlc.thelearningzone.domain.User;
import com.itlc.thelearningzone.domain.UserInfo;
import com.itlc.thelearningzone.repository.AuthorityRepository;
import com.itlc.thelearningzone.repository.UserInfoRepository;
import com.itlc.thelearningzone.repository.UserRepository;
import com.itlc.thelearningzone.repository.CourseYearRepository;
import com.itlc.thelearningzone.security.AuthoritiesConstants;
import com.itlc.thelearningzone.security.SecurityUtils;
import com.itlc.thelearningzone.service.dto.UserDTO;
import com.itlc.thelearningzone.service.util.RandomUtil;
import com.itlc.thelearningzone.web.rest.errors.*;
import com.itlc.thelearningzone.service.mapper.UserMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    
    private final UserInfoRepository userInfoRepository;
    
    private final CourseYearRepository courseYearRepository;
    
    private final UserInfoService userInfoService;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    private final CacheManager cacheManager;

    public UserService(UserRepository userRepository, UserInfoRepository userInfoRepository, CourseYearRepository courseYearRepository, UserInfoService userInfoService, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository, CacheManager cacheManager) {
        this.userRepository = userRepository;
        this.userInfoRepository = userInfoRepository;
        this.courseYearRepository = courseYearRepository;
        this.userInfoService = userInfoService;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
        this.cacheManager = cacheManager;
    }

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository.findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                this.clearUserCaches(user);
                log.debug("Activated user: {}", user);
                return user;
            });
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        return userRepository.findOneByResetKey(key)
            .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400)))
            .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                this.clearUserCaches(user);
                return user;
            });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository.findOneByEmailIgnoreCase(mail)
            .filter(User::getActivated)
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(Instant.now());
                this.clearUserCaches(user);
                return user;
            });
    }

    public User registerUser(UserDTO userDTO, String password) {
    	
        userRepository.findOneByLogin(userDTO.getLogin().toLowerCase()).ifPresent(existingUser -> {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed) {
                throw new LoginAlreadyUsedException();
            }
        });
        userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).ifPresent(existingUser -> {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed) {
                throw new EmailAlreadyUsedException();
            }
        });
        User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(userDTO.getLogin().toLowerCase());
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(userDTO.getFirstName());
        newUser.setLastName(userDTO.getLastName());
        newUser.setEmail(userDTO.getEmail().toLowerCase());
        newUser.setImageUrl(userDTO.getImageUrl());
        newUser.setLangKey(userDTO.getLangKey());
        // new user is not active
        newUser.setActivated(false);
        
        Set<Authority> authorities = new HashSet<>();
        
        // If user is a tutor, set authority but don't make activation key, admin will activate a tutor.
        if (userDTO.getAuthorities() != null && userDTO.getAuthorities().contains(AuthoritiesConstants.TUTOR)) {
        	authorityRepository.findById(AuthoritiesConstants.TUTOR).ifPresent(authorities::add);
        }
        else {
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);       
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        }
        
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);     
        log.debug("Created Information for User: {}", newUser);
        
        UserInfo newUserInfo = new UserInfo();
        newUserInfo.setUser(newUser);
        userInfoRepository.save(newUserInfo);
        log.debug("Created Information for User Info: {}", newUserInfo);
        
        return newUser;
    }
    
    public User registerUser(UserDTO userDTO, String password, Long courseYearId) {
    	Optional<CourseYear> courseYear = courseYearRepository.findById(courseYearId);
        if (courseYear.isPresent()) {
        	
        	User newUser = this.registerUser(userDTO, password);
        	
        	// Get user info of user from repository
        	UserInfo userInfo = userInfoRepository.findById(newUser.getId()).orElse(null);
        	      		
        	// Set course year
        	if (userInfo != null) {
        	userInfo.setCourseYear(courseYear.get());
        	} else {
        		throw new IllegalArgumentException("User " + userDTO.getLogin() + " does not exist");
        	}
        	
        	// save back in repository
        	userInfoRepository.save(userInfo);
        	
            log.debug("Created Information for UserInfo: {}", userInfo);
            
            return newUser;
    	} else {
    		throw new IllegalArgumentException("Course Year ID does not exist");
    	}
    }
    
    private boolean removeNonActivatedUser(User existingUser){
        if (existingUser.getActivated()) {
             return false;
        }       
        
        // Delete user info
        UserInfo existingUserInfo = userInfoRepository.findById(existingUser.getId()).orElse(null);
        userInfoRepository.delete(existingUserInfo);
        userInfoRepository.flush();
        
        // Delete user
        userRepository.delete(existingUser);
        userRepository.flush();
        this.clearUserCaches(existingUser);

        
        return true;
    }

    // Updated method to Create and save the UserInfo entity for this User entity
    public User createUser(UserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin().toLowerCase());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail().toLowerCase());
        user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getLangKey() == null) {
            user.setLangKey(Constants.DEFAULT_LANGUAGE); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = userDTO.getAuthorities().stream()
                .map(authorityRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
            user.setAuthorities(authorities);
        }
        userRepository.save(user);
        this.clearUserCaches(user);
        log.debug("Created Information for User: {}", user);
        
     // Create and save the UserInfo entity
        UserInfo newUserInfo = new UserInfo();
        newUserInfo.setUser(user);
        userInfoRepository.save(newUserInfo);
        log.debug("Created Information for UserInfo: {}", newUserInfo);
        
        return user;
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user
     * @param lastName last name of user
     * @param email email id of user
     * @param langKey language key
     * @param imageUrl image URL of user
     */
    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setEmail(email.toLowerCase());
                user.setLangKey(langKey);
                user.setImageUrl(imageUrl);
                this.clearUserCaches(user);
                log.debug("Changed Information for User: {}", user);
            });
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update
     * @return updated user
     */
    public Optional<UserDTO> updateUser(UserDTO userDTO) {
        return Optional.of(userRepository
            .findById(userDTO.getId()))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .map(user -> {
                this.clearUserCaches(user);
                user.setLogin(userDTO.getLogin().toLowerCase());
                user.setFirstName(userDTO.getFirstName());
                user.setLastName(userDTO.getLastName());
                user.setEmail(userDTO.getEmail().toLowerCase());
                user.setImageUrl(userDTO.getImageUrl());
                user.setActivated(userDTO.isActivated());
                user.setLangKey(userDTO.getLangKey());
                Set<Authority> managedAuthorities = user.getAuthorities();
                managedAuthorities.clear();
                userDTO.getAuthorities().stream()
                    .map(authorityRepository::findById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .forEach(managedAuthorities::add);
                this.clearUserCaches(user);
                log.debug("Changed Information for User: {}", user);
                return user;
            })
            .map(UserDTO::new);
    }

    // Added delete UserInfo functionality before parent entity (User) is deleted
    public void deleteUser(String login) {
    	Optional<User> searchUser = userRepository.findOneByLogin(login);
    	if (searchUser.isPresent()) {
    		userInfoService.findOne(searchUser.get().getId()).ifPresent(userInfo -> {
                userInfoService.delete(userInfo.getId());
                log.debug("Deleted UserInfo: {}", userInfo);
            });
    		userRepository.findOneByLogin(login).ifPresent(user -> {
                userRepository.delete(user);
                this.clearUserCaches(user);
                log.debug("Deleted User: {}", user);
            });
    	} else {
    		throw new IllegalArgumentException("User login does not exist");
    	}
    }

    public void changePassword(String currentClearTextPassword, String newPassword) {
        SecurityUtils.getCurrentUserLogin()
            .flatMap(userRepository::findOneByLogin)
            .ifPresent(user -> {
                String currentEncryptedPassword = user.getPassword();
                if (!passwordEncoder.matches(currentClearTextPassword, currentEncryptedPassword)) {
                    throw new InvalidPasswordException();
                }
                String encryptedPassword = passwordEncoder.encode(newPassword);
                user.setPassword(encryptedPassword);
                this.clearUserCaches(user);
                log.debug("Changed password for User: {}", user);
            });
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAllByLoginNot(pageable, Constants.ANONYMOUS_USER).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities(Long id) {
        return userRepository.findOneWithAuthoritiesById(id);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        userRepository
            .findAllByActivatedIsFalseAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS))
            .forEach(user -> {
                log.debug("Deleting not activated user {}", user.getLogin());
                userRepository.delete(user);
                this.clearUserCaches(user);
            });
    }

    /**
     * @return a list of all the authorities
     */
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }
    
    /**
     * Return users that have a certain role and are activated or not activated
     * 
     * @param pageable the pagination information
     * @param role authority of the user group
     * @param activated activation status of the user group
     */
    public Page<UserDTO> findAllByRoleAndActivationStatus(Pageable pageable, @Param(value = "role") String role, @Param(value = "activated") boolean activated) {
    	Page<User> users = userRepository.findAllByRoleAndActivationStatus(pageable, role, activated);
    	long totalUsers = users.getTotalElements();
    	return new PageImpl<UserDTO>(users.stream().map(user -> new UserDTO(user))
    			.collect(Collectors.toList()), pageable, totalUsers);
    }

    private void clearUserCaches(User user) {
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE)).evict(user.getLogin());
        Objects.requireNonNull(cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE)).evict(user.getEmail());
    }
}
