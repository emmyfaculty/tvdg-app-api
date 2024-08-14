package com.tvdgapp.dtos.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.tvdgapp.models.user.UserStatus;
import com.tvdgapp.utils.annotation.ExcelColumn;
import com.tvdgapp.validators.Enum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import static com.tvdgapp.constants.Constants.EMAIL_REGEX;
import static com.tvdgapp.constants.SchemaConstant.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.ALWAYS)
@EqualsAndHashCode(callSuper=false)
public class UserResponseDto {

    protected static final long serialVersionUID = -6196458615228927518L;

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


}
