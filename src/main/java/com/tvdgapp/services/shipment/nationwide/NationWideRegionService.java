//package com.tvdgapp.services.shipment.nationwide;
//
//import com.tvdgapp.dtos.shipment.nationwide.CreateNationWideRegionDto;
//import com.tvdgapp.dtos.shipment.nationwide.NationWideRegionDto;
//import com.tvdgapp.dtos.shipment.nationwide.UpdateNationWideRegionDto;
//import com.tvdgapp.exceptions.ResourceNotFoundException;
//import com.tvdgapp.mapper.NationWideRegionMapper;
//import com.tvdgapp.models.shipment.nationwide.NationWideRegion;
//import com.tvdgapp.repositories.shipment.nationwide.NationWideRegionRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class NationWideRegionService {
//
//    private final NationWideRegionRepository nationWideRegionRepository;
//
//    private final NationWideRegionMapper nationWideRegionMapper;
//
//    public NationWideRegionDto createRegion(CreateNationWideRegionDto createDto) {
//        NationWideRegion region = nationWideRegionMapper.toEntity(createDto);
//        NationWideRegion savedRegion = nationWideRegionRepository.save(region);
//        return nationWideRegionMapper.toDto(savedRegion);
//    }
//
//    public NationWideRegionDto getRegionById(Long id) {
//        NationWideRegion region = nationWideRegionRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Region not found"));
//        return nationWideRegionMapper.toDto(region);
//    }
//
//    public NationWideRegionDto updateRegion(Long id, UpdateNationWideRegionDto updateDto) {
//        NationWideRegion region = nationWideRegionRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Region not found"));
//
//        region.setName(updateDto.getName());
//        region.setDescription(updateDto.getDescription());
//        NationWideRegion updatedRegion = nationWideRegionRepository.save(region);
//        return nationWideRegionMapper.toDto(updatedRegion);
//    }
//
//    public void deleteRegion(Long id) {
//        if (!nationWideRegionRepository.existsById(id)) {
//            throw new ResourceNotFoundException("Region not found");
//        }
//        nationWideRegionRepository.deleteById(id);
//    }
//
//    public List<NationWideRegionDto> getAllRegions() {
//        return nationWideRegionRepository.findAll().stream()
//                .map(nationWideRegionMapper::toDto)
//                .collect(Collectors.toList());
//    }
//}
