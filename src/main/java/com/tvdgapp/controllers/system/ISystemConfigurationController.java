package com.tvdgapp.controllers.system;

import com.tvdgapp.apiresponse.ApiDataResponse;
import com.tvdgapp.dtos.system.SystemConfigurationDto;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collection;
import java.util.Map;

@Api(tags = { "System Configuration Controller" })
public interface ISystemConfigurationController {

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Resource retrieved successfully"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!")})
    @ApiOperation(value = "Fetch configurations",notes = "authorities[manageCoreConfiguration], Valid group values: general,email ", authorizations = {@Authorization(value = "JWT") })
    ResponseEntity<ApiDataResponse<Collection<SystemConfigurationDto>>> fetchConfigurations(@PathVariable String group) throws Exception;

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Resource retrieved successfully"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!")})
    @ApiOperation(value = "Update configurations", notes = "authorities[manageCoreConfiguration], Valid group values: general,email ", authorizations = {@Authorization(value = "JWT") })
    ResponseEntity<ApiDataResponse<Collection<SystemConfigurationDto>>> updateConfigurations(@PathVariable String group, Map<String, String> dto) throws Exception;
}
