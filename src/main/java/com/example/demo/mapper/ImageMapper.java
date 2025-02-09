package com.example.demo.mapper;

import com.example.demo.model.dto.request.UserImageRequest;
import com.example.demo.model.entity.UserImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ImageMapper {

    @Mapping(target = "file", source = "file")
    UserImage toEntity(UserImageRequest imageRequest);

}
