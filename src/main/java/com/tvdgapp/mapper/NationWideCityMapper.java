package com.tvdgapp.mapper;

import com.tvdgapp.dtos.shipment.nationwide.NationWideCityDTO;
import com.tvdgapp.models.shipment.nationwide.NationWideCity;
import com.tvdgapp.models.shipment.nationwide.NationWideState;
import org.springframework.stereotype.Component;

@Component
public class NationWideCityMapper {

    public NationWideCity toEntity(NationWideCityDTO dto, NationWideState state) {
        NationWideCity city = new NationWideCity();
//        city.setId(dto.getId());
        city.setName(dto.getName());
        city.setState(state);
        city.setDescription(dto.getDescription());
        return city;
    }

    public NationWideCityDTO toDTO(NationWideCity city) {
        NationWideCityDTO dto = new NationWideCityDTO();
        dto.setId(city.getId());
        dto.setName(city.getName());
        dto.setStateId(city.getState().getId());
        dto.setDescription(city.getDescription());
        return dto;
    }
}
