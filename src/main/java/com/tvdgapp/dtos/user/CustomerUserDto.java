package com.tvdgapp.dtos.user;

import com.tvdgapp.constants.DateConstants;
import com.tvdgapp.models.shipment.CustomerType;
import com.tvdgapp.utils.annotation.ExcelColumn;
import com.tvdgapp.validators.CustomDate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Collection;

@Data
public class CustomerUserDto extends UserDto{

    private CustomerType customerType;
    private Integer pricingLevelId;
    //    @NotEmpty
    Collection<Integer> roles;

    @NotEmpty
    @Size(min = 3, max = 100)
    @Schema(
            description = "Company name, Minimum of 3 and Maximum of 100 character",
            example = "Toladol Ventures Deluxe Global"

    )
    protected String companyName;

    @NotEmpty
    @Size(min = 2, max = 100)
    @Schema(
            description = "User Tittle",
            example = "MR."

    )
    protected String title;

    @NotEmpty
    @Size( max = 100)
    @Schema(
            description = "Company name, Minimum of 3 and Maximum of 100 character",
            example = "Toladol Ventures Deluxe Global"

    )
    protected String companyContactName;

    @NotEmpty
    @Size(min = 3, max = 100)
    @Schema(
            description = "Company Email Address, Minimum of 3 and Maximum of 100 character",
            example = "Tvdg@gmail.com"

    )
    protected String companyEmail;

    @NotEmpty
    @Size(min = 3, max = 100)
    @Schema(
            description = "Company Phone Number, Minimum of 3 and Maximum of 100 character",
            example = "+2347061659384"

    )
    protected String companyPhoneNo;
    @NotEmpty
    @Size(min = 3, max = 100)
    @Schema(
            description = "Company Registration Number, Minimum of 3 and Maximum of 100 character",
            example = "+2347061659384"

    )
    protected String companyRegNumber;

    @NotEmpty
    @Size(max = 100)
    @Schema(
            description = "Role at work, Minimum of 3 and Maximum of 100 character",
            example = "Software Engineer"

    )
    protected String designation;

    @NotEmpty
    @Size(max = 100)
    @Schema(
            description = "Company address, Minimum of 3 and Maximum of 100 character",
            example = "32B Ladipo Kasumu street, Ikeja"

    )
    protected String address;

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

    @NotEmpty
    @Size(max = 100)
    @Schema(
            description = "This is the Industry your Company belongs to",
            example = "Telecommunications"

    )
    protected String industry;

    @Nullable
    @Schema(
            description = "Date format: " + DateConstants.DATE_INPUT_FORMAT,
            example = "05-05-1995"

    )
    @CustomDate(message="Date not in valid format")
    @ExcelColumn(name = "Date Of Birth", col = 8,errMsg = "Invalid Date Of Birth- {val},expected format ["+DateConstants.DATE_INPUT_FORMAT+"]")
    protected String dateOfBirth;

    @Nullable
    @Size(max = 100)
    @Schema(
            description = "Business Description, Minimum of 3 and Maximum of 100 character",
            example = "Tell us what the business is about"

    )
    protected String natureOfBusiness;

    protected String loginUrl;
    private String currency;


//    public CustomerUserDto(String title, String companyName, String companyContactName, String companyEmail,
//                           String address, String city, String state, String postalCode, String industry, Date dateOfBirth, String natureOfBusiness ) {
//        this.title = title;
//        this.companyName = companyName;
//        this.companyContactName = companyContactName;
//        this.companyEmail = companyEmail;
//        this.address = address;
//        this.city = city;
//        this.state = state;
//        this.postalCode = postalCode;
//        this.industry = industry;
//        this.dateOfBirth = String.valueOf(dateOfBirth);
//        this.natureOfBusiness = natureOfBusiness;
//    }
}
