package com.itlc.thelearningzone.service.mapper;

import com.itlc.thelearningzone.domain.*;
import com.itlc.thelearningzone.service.dto.UserInfoDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity UserInfo and its DTO UserInfoDTO.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, SemesterGroupMapper.class})
public interface UserInfoMapper extends EntityMapper<UserInfoDTO, UserInfo> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "semesterGroup.id", target = "semesterGroupId")
    UserInfoDTO toDto(UserInfo userInfo);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "semesterGroupId", target = "semesterGroup")
    @Mapping(target = "bookingUserDetails", ignore = true)
    @Mapping(target = "sentNotifications", ignore = true)
    @Mapping(target = "receivedNotifications", ignore = true)
    @Mapping(target = "bookings", ignore = true)
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
