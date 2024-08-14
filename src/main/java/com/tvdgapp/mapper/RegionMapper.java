package com.tvdgapp.mapper;

import com.tvdgapp.dtos.RegionDTO;
import com.tvdgapp.models.reference.Region;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RegionMapper {
    RegionDTO toDto(Region region);
    Region toEntity(RegionDTO regionDTO);
}
