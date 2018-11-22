package com.itlc.thelearningzone.service.mapper;

import com.itlc.thelearningzone.domain.*;
import com.itlc.thelearningzone.service.dto.TopicDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Topic and its DTO TopicDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TopicMapper extends EntityMapper<TopicDTO, Topic> {


    @Mapping(target = "resources", ignore = true)
    @Mapping(target = "subjects", ignore = true)
    Topic toEntity(TopicDTO topicDTO);

    default Topic fromId(Long id) {
        if (id == null) {
            return null;
        }
        Topic topic = new Topic();
        topic.setId(id);
        return topic;
    }
}
