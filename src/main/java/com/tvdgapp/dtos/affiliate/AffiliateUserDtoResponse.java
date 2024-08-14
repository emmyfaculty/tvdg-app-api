package com.tvdgapp.dtos.affiliate;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tvdgapp.constants.DateConstants;
import com.tvdgapp.dtos.user.UserDto;
import com.tvdgapp.models.user.UserStatus;
import com.tvdgapp.utils.annotation.ExcelColumn;
import com.tvdgapp.validators.CustomDate;
import com.tvdgapp.validators.Enum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.tvdgapp.constants.Constants.EMAIL_REGEX;
import static com.tvdgapp.constants.SchemaConstant.*;
import static com.tvdgapp.constants.SchemaConstant.PHONE_NO_COL_SIZE;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.ALWAYS)
public class AffiliateUserDtoResponse {

    protected static final long serialVersionUID = -6196458615228927518L;

    private Long id;
    @NotEmpty
    @Size(min = 2, max = 100)
    @Schema(
            description = "User Tittle",
            example = "MR."

    )
    protected String tittle;
    @NotEmpty
    @Size(min=1,max = FIRST_NAME_COL_SIZE)
    protected String firstName;
    @NotEmpty
    @Size(min=1,max = LAST_NAME_COL_SIZE)
    protected String lastName;
    @NotEmpty
    @Email(regexp = EMAIL_REGEX)
    @Size(max = EMAIL_COL_SIZE)
    @ExcelColumn(name = "Email Address", col = 7,errMsg = "Invalid Email Address- {val}")
    @Size(max = 100)
    protected String email;
    @Size(max = PHONE_NO_COL_SIZE)
    @ExcelColumn(name = "Mobile Number", col = 6)
    protected String phone;
    @Enum(enumClass= UserStatus.class)
    @NotEmpty(message = "Field is required")
    protected String status= UserStatus.ACTIVE.name();
    private String profilePic;
    @NotNull
    @Size(min = 1, max = 50)
    private String username;
    @NotEmpty
    @Size(max = 100)
    @Schema(
            description = "Company address, Minimum of 3 and Maximum of 100 character",
            example = "32B Ladipo Kasumu street, Ikeja"

    )
    private String streetAddress;

    @NotEmpty
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

    @NotEmpty
    @Size(max = 100)
    @Schema(
            description = "State Postal Code",
            example = "100001"

    )
    protected String postalCode;
    @NotNull
    @Size(min = 1, max = 50)
    private String country;
    @NotNull
    private String identificationNumber;
    @NotNull
    private String gender;
    @NotNull
    @Size(min = 6, max = 8)
    private String referralCode;
    @Nullable
    @Schema(
            description = "Date format: " + DateConstants.DATE_INPUT_FORMAT,
            example = "05-05-1995"

    )
    @CustomDate(message="Date not in valid format")
    @ExcelColumn(name = "Date Of Birth", col = 8,errMsg = "Invalid Date Of Birth- {val},expected format ["+DateConstants.DATE_INPUT_FORMAT+"]")
    protected String dateOfBirth;

}