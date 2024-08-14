package com.tvdgapp.dtos.user;

import com.tvdgapp.constants.DateConstants;
import com.tvdgapp.models.shipment.CustomerType;
import com.tvdgapp.models.user.UserStatus;
import com.tvdgapp.utils.annotation.ExcelColumn;
import com.tvdgapp.validators.CustomDate;
import com.tvdgapp.validators.Enum;
import jakarta.annotation.Nullable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import static com.tvdgapp.constants.Constants.EMAIL_REGEX;
import static com.tvdgapp.constants.SchemaConstant.*;
import static com.tvdgapp.constants.SchemaConstant.PHONE_NO_COL_SIZE;

@Data
public class CustomerUserDetailDto {

    private Long id;
    private String profilePic;

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
    @NotEmpty
    @Size(min = 3, max = 100)
//    @Schema(
//            description = "Company name, Minimum of 3 and Maximum of 100 character",
//            example = "Toladol Ventures Deluxe Global"
//
//    )
    protected String companyName;

    @NotEmpty
    @Size( max = 10)
//    @Schema(
//            description = "Company name, Minimum of 3 and Maximum of 100 character",
//            example = "Toladol Ventures Deluxe Global"
//
//    )
    protected String tittle;

    @NotEmpty
    @Size(min = 3, max = 100)
//    @Schema(
//            description = "User Tittle",
//            example = "MR."
//
//    )
    protected String companyContactName;

    @NotEmpty
    @Size(min = 3, max = 100)
//    @Schema(
//            description = "Company Email Address, Minimum of 3 and Maximum of 100 character",
//            example = "Tvdg@gmail.com"
//
//    )
    protected String companyEmail;

    @NotEmpty
    @Size(min = 3, max = 100)
//    @Schema(
//            description = "Company Phone Number, Minimum of 3 and Maximum of 100 character",
//            example = "+2347061659384"
//
//    )
    protected String companyPhoneNo;

    @NotEmpty
    @Size(max = 100)
//    @Schema(
//            description = "Role at work, Minimum of 3 and Maximum of 100 character",
//            example = "Software Engineer"
//
//    )
    protected String designation;

    @NotEmpty
    @Size(max = 100)
//    @Schema(
//            description = "Company address, Minimum of 3 and Maximum of 100 character",
//            example = "32B Ladipo Kasumu street, Ikeja"
//
//    )
    protected String address;

    @NotEmpty
    @Size(max = 100)
//    @Schema(
//            description = "Company Location, Minimum of 3 and Maximum of 100 character",
//            example = "Surulere"
//
//    )
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

    @NotEmpty
    @Size(max = 100)
//    @Schema(
//            description = "This is the Industry your Company belongs to",
//            example = "Telecommunications"
//
//    )
    protected String industry;

    @Nullable
//    @Schema(
//            description = "Date format: " + DateConstants.DATE_INPUT_FORMAT,
//            example = "05-05-1995"
//
//    )
    @CustomDate(message="Date not in valid format")
    @ExcelColumn(name = "Date Of Birth", col = 8,errMsg = "Invalid Date Of Birth- {val},expected format ["+DateConstants.DATE_INPUT_FORMAT+"]")
    protected String dateOfBirth;

    @Nullable
    @Size(max = 100)
//    @Schema(
//            description = "Business Description, Minimum of 3 and Maximum of 100 character",
//            example = "Tell us what the business is about"
//
//    )
    protected String natureOfBusiness;
    @Enumerated(EnumType.STRING)
    private CustomerType customerType;
    private Integer pricingLevelId;
    private String shipmentCount;

}
