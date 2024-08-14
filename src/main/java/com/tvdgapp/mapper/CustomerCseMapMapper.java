package com.tvdgapp.mapper;

import com.tvdgapp.dtos.user.admin.CustomerCseMapDTO;
import com.tvdgapp.models.user.admin.CustomerCseMap;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CustomerCseMapMapper {

    CustomerCseMapMapper INSTANCE = Mappers.getMapper(CustomerCseMapMapper.class);


    CustomerCseMapDTO toDto(CustomerCseMap customerCseMap);

    CustomerCseMap toEntity(CustomerCseMapDTO customerCseMapDTO);
}

