package com.tvdgapp.validators;


import com.tvdgapp.constants.DateConstants;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Constraint(validatedBy = CustomDateValidator.class)
public @interface CustomDate {

    String message() default "Invalid Date Format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String format() default DateConstants.DATE_INPUT_FORMAT;
}
