package com.example.demo.mapper;

import com.example.demo.model.dto.response.UserInfoResponse;
import com.example.demo.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "name", source = "name")
    @Mapping(target = "surname", source = "surname")
    @Mapping(target = "patronymic", source = "patronymic")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "dateOfBirth", source = "dateOfBirth")
    @Mapping(target = "image", source = "image")
    UserInfoResponse userToUserInfoResponse(User user);

}
