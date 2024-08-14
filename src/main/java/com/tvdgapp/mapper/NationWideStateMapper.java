package com.tvdgapp.mapper;

import com.tvdgapp.dtos.shipment.nationwide.CreateNationWideStateDto;
import com.tvdgapp.dtos.shipment.nationwide.NationWideStateDto;
import com.tvdgapp.dtos.shipment.nationwide.UpdateNationWideStateDto;
import com.tvdgapp.models.shipment.nationwide.NationWideRegion;
import com.tvdgapp.models.shipment.nationwide.NationWideState;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NationWideStateMapper {

    @Mapping(source = "region.id", target = "regionId")
    NationWideStateDto toDto(NationWideState entity);

    @Mapping(target = "region", source = "regionId")
    NationWideState toEntity(CreateNationWideStateDto createDto);

    @Mapping(target = "region", source = "regionId")
    @Mapping(target = "id", ignore = true)
    NationWideState toEntity(UpdateNationWideStateDto updateDto);

    // Custom mapping methods to handle Long to NationWideRegion conversion
    default NationWideRegion map(Long regionId) {
        if (regionId == null) {
            return null;
        }
        NationWideRegion region = new NationWideRegion();
        region.setId(regionId);
        return region;
    }
}
