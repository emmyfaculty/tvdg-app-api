package com.tvdgapp.dtos.rider;

import com.tvdgapp.constants.DateConstants;
import com.tvdgapp.utils.annotation.ExcelColumn;
import com.tvdgapp.validators.CustomDate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

import static com.tvdgapp.constants.Constants.EMAIL_REGEX;
import static com.tvdgapp.constants.SchemaConstant.*;

@Data
public class UpdateRiderUserDetailDto {

    private String profilePic;
    private String riderLicence;
    @Size(min=1,max = FIRST_NAME_COL_SIZE)
    protected String firstName;
    @Size(min=1,max = LAST_NAME_COL_SIZE)
    protected String lastName;
    @Email(regexp = EMAIL_REGEX)
    @Size(max = EMAIL_COL_SIZE)
    @ExcelColumn(name = "Email Address", col = 7,errMsg = "Invalid Email Address- {val}")
    @Size(max = 100)
    protected String email;
    @Size(max = PHONE_NO_COL_SIZE)
    @ExcelColumn(name = "Mobile Number", col = 6)
    protected String phone;
    @Size(min = 1, max = 50)
    private String username;
    @Size(max = 100)
    @Schema(
            description = "Company address, Minimum of 3 and Maximum of 100 character",
            example = "32B Ladipo Kasumu street, Ikeja"

    )
    private String streetAddress;
    @Size(min = 1, max = 50)
    private String country;
    private String identificationNumber;
    @Size(min = 2, max = 100)
    @Schema(
            description = "User Tittle",
            example = "MR."

    )
    protected String tittle;
    private String gender;
    @Size(max = 100)
    @Schema(
            description = "Company Location, Minimum of 3 and Maximum of 100 character",
            example = "Surulere"

    )
    protected String city;

    @Schema(
            description = "Company Location, Minimum of 3 and Maximum of 100 character",
            example = "Lagos"

    )
    protected String state;
    @Size(max = 100)
    @Schema(
            description = "State Postal Code",
            example = "100001"

    )
    protected String postalCode;
    @Nullable
    @Schema(
            description = "Date format: " + DateConstants.DATE_INPUT_FORMAT,
            example = "05-05-1995"

    )
    @CustomDate(message="Date not in valid format")
    @ExcelColumn(name = "Date Of Birth", col = 8,errMsg = "Invalid Date Of Birth- {val},expected format ["+DateConstants.DATE_INPUT_FORMAT+"]")
    protected String dateOfBirth;

}