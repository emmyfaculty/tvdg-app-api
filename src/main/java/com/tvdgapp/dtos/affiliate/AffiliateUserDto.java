package com.tvdgapp.dtos.affiliate;

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
public class AffiliateUserDto extends UserDto {

    private String username;
    @NotEmpty
    @Size(max = 100)
    @Schema(
            description = "Company address, Minimum of 3 and Maximum of 100 character",
            example = "32B Ladipo Kasumu street, Ikeja"

    )
    private String streetAddress;
    private String country;
    private String identificationNumber;
    private String identificationType;
    @NotEmpty
    @Size(min = 2, max = 100)
    @Schema(
            description = "User Tittle",
            example = "MR."

    )
    protected String title;
    private String gender;
    private String referralCode;

    @NotEmpty
    @Size(max = 100)
    @Schema(
            description = "Company Location, Minimum of 3 and Maximum of 100 character",
            example = "Surulere"

    )
    protected String city;

//    @Schema(
//            description = "Company Location, Minimum of 3 and Maximum of 100 character",
//            example = "Lagos"
//
//    )
    protected String state;

    @NotEmpty
    @Size(max = 100)
//    @Schema(
//            description = "State Postal Code",
//            example = "100001"
//
//    )
    protected String postalCode;
    @Nullable
//    @Schema(
//            description = "Date format: " + DateConstants.DATE_INPUT_FORMAT,
//            example = "05-05-1995"
//
//    )
    @CustomDate(message="Date not in valid format")
    @ExcelColumn(name = "Date Of Birth", col = 8,errMsg = "Invalid Date Of Birth- {val},expected format ["+DateConstants.DATE_INPUT_FORMAT+"]")
    protected String dateOfBirth;

    private String currency;
    protected String loginUrl;


}