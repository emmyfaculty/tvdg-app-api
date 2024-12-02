package com.tvdgapp.dtos.rider;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.tvdgapp.constants.DateConstants;
import com.tvdgapp.dtos.affiliate.AffiliateUserDetailDto;
import com.tvdgapp.dtos.user.UserDto;
import com.tvdgapp.dtos.user.UserResponseDto;
import com.tvdgapp.utils.annotation.ExcelColumn;
import com.tvdgapp.validators.CustomDate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Map;

@Data
public class RiderUserDetailResponseDto extends UserResponseDto {
    private Long id;
    private String profilePic;

    @NotEmpty
    @Size(min = 2, max = 100)
    @Schema(
            description = "User Tittle",
            example = "MR."

    )
    protected String title;


    @CustomDate(message="Date not in valid format")
    @ExcelColumn(name = "Date Of Birth", col = 8,errMsg = "Invalid Date Of Birth- {val},expected format ["+ DateConstants.DATE_INPUT_FORMAT+"]")
    protected String dateOfBirth;
    protected String employeeId;
}