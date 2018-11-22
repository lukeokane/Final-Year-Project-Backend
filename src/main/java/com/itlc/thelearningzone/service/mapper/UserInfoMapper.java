package com.itlc.thelearningzone.service.mapper;

import com.itlc.thelearningzone.domain.*;
import com.itlc.thelearningzone.service.dto.UserInfoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity UserInfo and its DTO UserInfoDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface UserInfoMapper extends EntityMapper<UserInfoDTO, UserInfo> {

    @Mapping(source = "user.id", target = "userId")
    UserInfoDTO toDto(UserInfo userInfo);

    @Mapping(source = "userId", target = "user")
    @Mapping(target = "bookingUserDetails", ignore = true)
    @Mapping(target = "senderUserInfos", ignore = true)
    @Mapping(target = "receiverUserInfos", ignore = true)
    @Mapping(target = "bookings", ignore = true)
    @Mapping(target = "semesters", ignore = true)
    UserInfo toEntity(UserInfoDTO userInfoDTO);

    default UserInfo fromId(Long id) {
        if (id == null) {
            return null;
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setId(id);
        return userInfo;
    }
}
