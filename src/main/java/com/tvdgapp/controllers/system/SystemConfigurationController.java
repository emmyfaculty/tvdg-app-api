package com.tvdgapp.controllers.system;

import com.tvdgapp.apiresponse.ApiDataResponse;
import com.tvdgapp.dtos.system.SystemConfigurationDto;
import com.tvdgapp.services.system.SystemConfigurationService;
import com.tvdgapp.utils.ApiResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.basepath-admin}/configuration")
@PreAuthorize("hasAuthority('manageCoreConfiguration')")
public class SystemConfigurationController implements ISystemConfigurationController {

    private final SystemConfigurationService systemConfigurationService;

    @Override
    @GetMapping("/{group}")
    @PreAuthorize("hasAuthority('manageCoreConfiguration')")
    public ResponseEntity<ApiDataResponse<Collection<SystemConfigurationDto>>> fetchConfigurations(@PathVariable String group) throws Exception {
        return ApiResponseUtils.response(HttpStatus.OK,this.systemConfigurationService.fetchSystemConfigurations(group), "Resources retrieved successfully");
    }

    @Override
    @PutMapping("/{group}")
    @PreAuthorize("hasAuthority('manageCoreConfiguration')")
    public ResponseEntity<ApiDataResponse<Collection<SystemConfigurationDto>>> updateConfigurations(@PathVariable String group,@RequestBody Map<String,String> dto) throws Exception {
        this.systemConfigurationService.updateSystemConfigurations(group,dto);
        return ApiResponseUtils.response(HttpStatus.OK,"Resources updated successfully");
    }

}
