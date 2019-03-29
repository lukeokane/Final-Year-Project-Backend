package com.itlc.thelearningzone.service.mapper;

import com.itlc.thelearningzone.domain.*;
import com.itlc.thelearningzone.service.dto.BookingDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Booking and its DTO BookingDTO.
 */
@Mapper(componentModel = "spring", uses = {SubjectMapper.class, UserMapper.class, UserInfoMapper.class, TopicMapper.class})
public interface BookingMapper extends EntityMapper<BookingDTO, Booking> {

    @Mapping(source = "subject.id", target = "subjectId")
    @Mapping(source = "bookedBy.id", target = "bookedById")
    BookingDTO toDto(Booking booking);

    @Mapping(target = "bookingUserDetails")
    @Mapping(source = "subjectId", target = "subject")
    @Mapping(source = "bookedById", target = "bookedBy")
    @Mapping(target = "notifications", ignore = true)
    Booking toEntity(BookingDTO bookingDTO);

    default Booking fromId(Long id) {
        if (id == null) {
            return null;
        }
        Booking booking = new Booking();
        booking.setId(id);
        return booking;
    }
}
