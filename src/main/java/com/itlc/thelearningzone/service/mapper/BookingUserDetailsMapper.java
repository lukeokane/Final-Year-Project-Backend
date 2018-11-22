package com.itlc.thelearningzone.service.mapper;

import com.itlc.thelearningzone.domain.*;
import com.itlc.thelearningzone.service.dto.BookingUserDetailsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity BookingUserDetails and its DTO BookingUserDetailsDTO.
 */
@Mapper(componentModel = "spring", uses = {UserInfoMapper.class, BookingMapper.class})
public interface BookingUserDetailsMapper extends EntityMapper<BookingUserDetailsDTO, BookingUserDetails> {

    @Mapping(source = "userInfo.id", target = "userInfoId")
    @Mapping(source = "booking.id", target = "bookingId")
    BookingUserDetailsDTO toDto(BookingUserDetails bookingUserDetails);

    @Mapping(source = "userInfoId", target = "userInfo")
    @Mapping(source = "bookingId", target = "booking")
    BookingUserDetails toEntity(BookingUserDetailsDTO bookingUserDetailsDTO);

    default BookingUserDetails fromId(Long id) {
        if (id == null) {
            return null;
        }
        BookingUserDetails bookingUserDetails = new BookingUserDetails();
        bookingUserDetails.setId(id);
        return bookingUserDetails;
    }
}
