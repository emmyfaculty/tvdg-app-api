package com.tvdgapp.validators;


import com.tvdgapp.utils.TvdgAppDateUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.commons.lang3.StringUtils;



@SuppressWarnings("NullAway.Init")
public class CustomDateValidator implements
		ConstraintValidator<CustomDate, String> {

	private String format;

	@Override
	public void initialize(CustomDate customDate) {
		format = customDate.format();
	}

	@Override
	public boolean isValid(String date, ConstraintValidatorContext context) {

		if(StringUtils.isEmpty(date)){
			return true;
		}

		return TvdgAppDateUtils.isValidFormat(format,date);

	}


}
