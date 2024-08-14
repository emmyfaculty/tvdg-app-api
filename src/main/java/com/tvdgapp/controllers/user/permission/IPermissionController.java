package com.tvdgapp.controllers.user.permission;


import com.tvdgapp.apiresponse.ApiDataResponse;
import com.tvdgapp.dtos.user.permission.ListPermissionDto;
import io.swagger.annotations.*;
import org.springframework.http.ResponseEntity;

import java.util.Collection;

@Api(tags = { "Permission Controller" })
public interface IPermissionController {

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Resources retrieved successfully"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!")})
    @ApiOperation(value = "List permissions",notes = "authorities[manageRole]", authorizations = {@Authorization(value = "JWT")})
    ResponseEntity<ApiDataResponse<Collection<ListPermissionDto>>> listPermissions() throws Exception;
}
