package com.tvdgapp.dtos.rider;

import com.tvdgapp.constants.DateConstants;
import com.tvdgapp.dtos.user.UserDto;
import com.tvdgapp.utils.annotation.ExcelColumn;
import com.tvdgapp.validators.CustomDate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Collection;

@Data
public class RiderUserDto extends UserDto {

    @NotEmpty
    Collection<Integer> roles;
    @NotEmpty
    @Size(min = 2, max = 100)
    @Schema(
            description = "User Tittle",
            example = "MR."

    )
    protected String title;
    private Long shipmentId;


    @Nullable
    @Schema(
            description = "Date format: " + DateConstants.DATE_INPUT_FORMAT,
            example = "1995-05-05"

    )
    @CustomDate(message="Date not in valid format")
    @ExcelColumn(name = "Date Of Birth", col = 8,errMsg = "Invalid Date Of Birth- {val},expected format ["+DateConstants.DATE_INPUT_FORMAT+"]")
    protected String dateOfBirth;
    protected  String loginUrl;

}