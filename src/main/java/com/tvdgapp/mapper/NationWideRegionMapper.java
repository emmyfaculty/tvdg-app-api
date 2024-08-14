package com.tvdgapp.mapper;

import com.tvdgapp.dtos.shipment.nationwide.CreateNationWideRegionDto;
import com.tvdgapp.dtos.shipment.nationwide.NationWideRegionDto;
import com.tvdgapp.dtos.shipment.nationwide.UpdateNationWideRegionDto;
import com.tvdgapp.models.shipment.nationwide.NationWideRegion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NationWideRegionMapper {

    NationWideRegionDto toDto(NationWideRegion entity);

    NationWideRegion toEntity(CreateNationWideRegionDto createDto);

    @Mapping(target = "id", ignore = true)
    NationWideRegion toEntity(UpdateNationWideRegionDto updateDto);
}
