package com.tvdgapp.controllers.user.role;

import com.tvdgapp.apiresponse.ApiDataResponse;
import com.tvdgapp.constants.Constants;
import com.tvdgapp.dtos.common.IdResponseDto;
import com.tvdgapp.dtos.user.role.ListRoleDto;
import com.tvdgapp.dtos.user.role.RoleDetailDto;
import com.tvdgapp.dtos.user.role.RoleRequestDto;
import io.swagger.annotations.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;


@Api(tags = { "Role Controller" })
public interface IRoleController {

    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Resource created successfully"),
            @ApiResponse(code = 400, message = "Invalid request!!!"),
            @ApiResponse(code = 409, message = "non unique entity!!!"),
            @ApiResponse(code = 401, message = "not authorized!")})
    @ApiOperation(value = "Create Role", notes = "authorities[manageRole]", authorizations = {@Authorization(value = "JWT")})
    ResponseEntity<ApiDataResponse<IdResponseDto>> createRole(@Valid @RequestBody RoleRequestDto roleDto);

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Resources retrieved successfully"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!")})
    @ApiOperation(value = "List/Manage All roles", notes = "Use for listing all roles in the Admin role management module, authorities[manageRole,viewRole]", authorizations = {@Authorization(value = "JWT")})
    ResponseEntity<ApiDataResponse<Collection<ListRoleDto>>> listAllRoles() throws Exception;

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Resources retrieved successfully"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!")})
    @ApiOperation(value = "List roles",  notes ="Use for populating drop downs and menu lists. List only admin type roles when type is "+ Constants.ROLE_TYPE_ADMIN +" " +
            "and only client type roles when type is "+ Constants.ROLE_TYPE_AFFILIATE+", authorities[manageRole,viewRole]", authorizations = {@Authorization(value = "JWT")})
    @ApiImplicitParam(name = "type", value = "Accepted values "+Constants.ROLE_TYPE_ADMIN+" | "+Constants.ROLE_TYPE_AFFILIATE, dataTypeClass = String.class, paramType = "query")
    ResponseEntity<ApiDataResponse<Collection<ListRoleDto>>> listRoles(@RequestParam(name = "type", required = false) String type) throws Exception;


    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Resource retrieved successfully"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!")})
    @ApiOperation(value = "Fetch role detail", notes = "authorities[manageRole, viewRole]", authorizations = {@Authorization(value = "JWT")})
    ResponseEntity<ApiDataResponse<RoleDetailDto>> fetchRoleDetail(@PathVariable int roleId) throws Exception;

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Resource updated successfully"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!")})
    @ApiOperation(value = "Update role", notes = "authorities[manageRole]", authorizations = {@Authorization(value = "JWT")})
    ResponseEntity<ApiDataResponse<Object>> updateRole(
            @Valid @RequestBody RoleRequestDto dto, @PathVariable int roleId) throws Exception;

    @ApiOperation(value = "Delete role", notes = "authorities[manageRole]", authorizations = {@Authorization(value = "JWT")})
    ResponseEntity<ApiDataResponse<String>> deleteRole(@PathVariable int roleId) throws Exception;
}
