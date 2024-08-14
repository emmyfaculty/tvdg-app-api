package com.tvdgapp.mapper;// AccessCodeMapper.java
import com.tvdgapp.dtos.user.admin.AccessCodeDTO;
import com.tvdgapp.models.user.admin.AccessCode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccessCodeMapper {

    @Mapping(source = "entity.userId", target = "userId")
    @Mapping(source = "entity.accessCode", target = "accessCode")
    @Mapping(source = "entity.createdBy", target = "createdBy")
    @Mapping(source = "entity.updatedBy", target = "updatedBy")
    @Mapping(source = "entity.createdAt", target = "createdAt")
    @Mapping(source = "entity.updatedAt", target = "updatedAt")
    @Mapping(source = "entity.tsCreated", target = "tsCreated")
    @Mapping(source = "entity.tsModified", target = "tsModified")
    AccessCodeDTO toDto(AccessCode entity);

    @Mapping(source = "dto.userId", target = "userId")
    @Mapping(source = "dto.accessCode", target = "accessCode")
    @Mapping(source = "dto.createdBy", target = "createdBy")
    @Mapping(source = "dto.updatedBy", target = "updatedBy")
    @Mapping(source = "dto.createdAt", target = "createdAt")
    @Mapping(source = "dto.updatedAt", target = "updatedAt")
    @Mapping(source = "dto.tsCreated", target = "tsCreated")
    @Mapping(source = "dto.tsModified", target = "tsModified")
    AccessCode toEntity(AccessCodeDTO dto);
}
