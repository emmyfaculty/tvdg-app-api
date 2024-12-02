//package com.tvdgapp.services.shipment.nationwide;
//
//import com.tvdgapp.dtos.shipment.nationwide.NationWideCityDTO;
//import com.tvdgapp.exceptions.ResourceNotFoundException;
//import com.tvdgapp.mapper.NationWideCityMapper;
//import com.tvdgapp.models.shipment.nationwide.NationWideCity;
//import com.tvdgapp.models.shipment.nationwide.NationWideState;
//import com.tvdgapp.repositories.shipment.nationwide.NationWideCityRepository;
//import com.tvdgapp.repositories.shipment.nationwide.NationWideStateRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//public class NationWideCityService {
//
//    @Autowired
//    private NationWideCityRepository cityRepository;
//
//    @Autowired
//    private NationWideStateRepository stateRepository;
//
//    @Autowired
//    private NationWideCityMapper cityMapper;
//
//    public List<NationWideCityDTO> getAllCities() {
//        return cityRepository.findAll().stream()
//                .map(cityMapper::toDTO)
//                .collect(Collectors.toList());
//    }
//
//    public NationWideCityDTO getCityById(Long id) {
//        NationWideCity city = cityRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("City not found with id " + id));
//        return cityMapper.toDTO(city);
//    }
//
//    public NationWideCityDTO createCity(NationWideCityDTO dto) {
//        NationWideState state = stateRepository.findById(dto.getStateId())
//                .orElseThrow(() -> new ResourceNotFoundException("State not found with id " + dto.getStateId()));
//        NationWideCity city = cityMapper.toEntity(dto, state);
//        city = cityRepository.save(city);
//        return cityMapper.toDTO(city);
//    }
//
//    public NationWideCityDTO updateCity(Long id, NationWideCityDTO dto) {
//        NationWideCity city = cityRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("City not found with id " + id));
//        NationWideState state = stateRepository.findById(dto.getStateId())
//                .orElseThrow(() -> new ResourceNotFoundException("State not found with id " + dto.getStateId()));
//        city.setName(dto.getName());
//        city.setState(state);
//        city.setDescription(dto.getDescription());
//        city = cityRepository.save(city);
//        return cityMapper.toDTO(city);
//    }
//
//    public void deleteCity(Long id) {
//        if (!cityRepository.existsById(id)) {
//            throw new ResourceNotFoundException("City not found with id " + id);
//        }
//        cityRepository.deleteById(id);
//    }
//}
