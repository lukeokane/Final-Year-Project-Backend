package com.itlc.thelearningzone.service.impl;

import com.itlc.thelearningzone.service.SubjectService;
import com.itlc.thelearningzone.service.TopicService;
import com.itlc.thelearningzone.domain.Topic;
import com.itlc.thelearningzone.repository.TopicRepository;
import com.itlc.thelearningzone.service.dto.SubjectDTO;
import com.itlc.thelearningzone.service.dto.TopicDTO;
import com.itlc.thelearningzone.service.mapper.TopicMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing Topic.
 */
@Service
@Transactional
public class TopicServiceImpl implements TopicService {

    private final Logger log = LoggerFactory.getLogger(TopicServiceImpl.class);

    private final TopicRepository topicRepository;

    private final SubjectService subjectService;

    private final TopicMapper topicMapper;

    public TopicServiceImpl(TopicRepository topicRepository, TopicMapper topicMapper,SubjectService subjectService) {
        this.subjectService = subjectService;
    	this.topicRepository = topicRepository;
        this.topicMapper = topicMapper;
    }

    /**
     * Save a topic.
     *
     * @param topicDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TopicDTO save(TopicDTO topicDTO) {
        log.debug("Request to save Topic : {}", topicDTO);

        Topic topic = topicMapper.toEntity(topicDTO);
        topic = topicRepository.save(topic);
        return topicMapper.toDto(topic);
    }

    /**
     * Get all the topics.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TopicDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Topics");
        return topicRepository.findAll(pageable)
            .map(topicMapper::toDto);
    }


    /**
     * Get one topic by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TopicDTO> findOne(Long id) {
        log.debug("Request to get Topic : {}", id);
        return topicRepository.findById(id)
            .map(topicMapper::toDto);
    }

    /**
     * Delete the topic by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Topic : {}", id);
        topicRepository.deleteById(id);
    }
    
    /**
     * Get all the topics by subject ID.
     *
     * @param array of subject id by semester group
     * @return the list of entities
     */
    public List<TopicDTO> findTopicsList(Long[] subjectsId){
		List<TopicDTO> topics = new ArrayList<>();
		log.debug("SERVICE IMP");
		log.debug("LENGTH "+  subjectsId.length);

    	for(int i =0; i < subjectsId.length;i++) {
    		Optional<SubjectDTO> optS = subjectService.findOne(subjectsId[i]);
    		SubjectDTO s=optS.get();
    		List<TopicDTO> tempDTO = new ArrayList<>();
    		for (TopicDTO t : s.getTopics())
    			tempDTO.add(t);
    		topics.addAll(tempDTO);
    	}
    	return topics;

    }
}
