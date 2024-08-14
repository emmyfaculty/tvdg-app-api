package com.tvdgapp.services.shipment.nationwide;

import com.tvdgapp.dtos.shipment.nationwide.CreateNationWideStateDto;
import com.tvdgapp.dtos.shipment.nationwide.NationWideStateDto;
import com.tvdgapp.dtos.shipment.nationwide.UpdateNationWideStateDto;
import com.tvdgapp.exceptions.ResourceNotFoundException;
import com.tvdgapp.mapper.NationWideStateMapper;
import com.tvdgapp.models.shipment.nationwide.NationWideRegion;
import com.tvdgapp.models.shipment.nationwide.NationWideState;
import com.tvdgapp.repositories.shipment.nationwide.NationWideRegionRepository;
import com.tvdgapp.repositories.shipment.nationwide.NationWideStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NationWideStateService {

    @Autowired
    private NationWideStateRepository nationWideStateRepository;

    @Autowired
    private NationWideRegionRepository nationWideRegionRepository;

    @Autowired
    private NationWideStateMapper nationWideStateMapper;

    public NationWideStateDto createState(CreateNationWideStateDto createDto) {
        NationWideRegion region = nationWideRegionRepository.findById(createDto.getRegionId())
                .orElseThrow(() -> new ResourceNotFoundException("Region not found"));

        NationWideState state = nationWideStateMapper.toEntity(createDto);
        state.setRegion(region);
        NationWideState savedState = nationWideStateRepository.save(state);
        return nationWideStateMapper.toDto(savedState);
    }

    public NationWideStateDto getStateById(Long id) {
        NationWideState state = nationWideStateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("State not found"));
        return nationWideStateMapper.toDto(state);
    }

    public NationWideStateDto updateState(Long id, UpdateNationWideStateDto updateDto) {
        NationWideState state = nationWideStateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("State not found"));

        NationWideRegion region = nationWideRegionRepository.findById(updateDto.getRegionId())
                .orElseThrow(() -> new ResourceNotFoundException("Region not found"));

        state.setName(updateDto.getName());
        state.setRegion(region);
        state.setDescription(updateDto.getDescription());
        NationWideState updatedState = nationWideStateRepository.save(state);
        return nationWideStateMapper.toDto(updatedState);
    }

    public void deleteState(Long id) {
        if (!nationWideStateRepository.existsById(id)) {
            throw new ResourceNotFoundException("State not found");
        }
        nationWideStateRepository.deleteById(id);
    }

    public List<NationWideStateDto> getAllStates() {
        return nationWideStateRepository.findAll().stream()
                .map(nationWideStateMapper::toDto)
                .collect(Collectors.toList());
    }
}
