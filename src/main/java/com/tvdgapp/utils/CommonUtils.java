package com.tvdgapp.utils;

import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

;

@Slf4j
public class CommonUtils {


    public static boolean fieldChanged(String val1, String val2) {
        return !val1.equals(val2);
    }

    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

//    public static boolean isValidEmail(String email) {
//        if (EmailValidator.getInstance().isValid(email)) {
//            return true;
//        }
//        return false;
//    }

    public static String generatePassword() {
        return RandomStringUtils.randomAlphanumeric(6);
    }

    public static boolean checkDuplicateList(Collection inputList) {

        Set inputSet = new HashSet(inputList);

        if (inputSet.size() < inputList.size()) {
            return true;
        }
        return false;
    }

    public static boolean isLong(String str) {
        try {
            Long.parseLong(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

    public static int round(double d) {
        double dAbs = Math.abs(d);
        int i = (int) dAbs;
        double result = dAbs - i;
        if (result < 0.5) {
            return d < 0 ? -i : i;
        } else {
            return d < 0 ? -(i + 1) : i + 1;
        }
    }

    public static double roundWithPrecision(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

//    public static Date[] getTrackingPeriodDateRanges(ClientTrackingPeriod period) {
//
//        Date today = FellowshipDateUtils.today();
//        Date[] dateranges = new Date[2];
//        Date startDateRange = null;
//        Date endDateRange = null;
//        switch (period) {
//
//            case TODAY:
//                startDateRange = endDateRange = DateUtils.addDays(today, 0);
//                break;
//
//            case YESTERDAY:
//                startDateRange = endDateRange = DateUtils.addDays(today, -1);
//                break;
//
//            case LAST7DAYS:
//                startDateRange = DateUtils.addDays(today, -7);
//                endDateRange = today;
//                break;
//
//            case LAST30DAYS:
//                startDateRange = DateUtils.addMonths(today, -1);
//                endDateRange = today;
//                break;
//
//
//            case THISMONTH:case LAST3MONTHS:
//                Calendar c = Calendar.getInstance();
//                c.setTime(today);
//                if(period.equals(ClientTrackingPeriod.THISMONTH)) {
//                    //get start date of the current month
//                    c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
//                    startDateRange = c.getTime();
//                    //end date is current date
//                    endDateRange = today;
//                }else{
//                    //cal last date of prev month = first date of current mnth minus 1 day
//                    c.set(Calendar.DATE, c.getActualMinimum(Calendar.DATE));
//                    Date firstDateOfCurrMnth=c.getTime();
//                    Date lastDateOfPrevious=DateUtils.addDays(firstDateOfCurrMnth,-1);
//                    //first date of last 3rd month is startdate range
//                    startDateRange=DateUtils.addMonths(lastDateOfPrevious,-3);
//                    //end date of prev month is end date range
//                    endDateRange=lastDateOfPrevious;
//                }
//                break;
//
//        }
//        dateranges[0] = startDateRange;
//        dateranges[1] = endDateRange;
//        return dateranges;
//    }

    /**
     * generate random number between low and high.
     * @param low
     * @param high
     * @return
     */
    public static int getRandomInt(int low,int high){
        if(high==low){
           low=high-1;
        }
        Random r=new Random();
        return r.nextInt((high)-low) + low;
    }

    public static int initialCollectionCapacity(int collectionSize,float loadFactor){
        return Math.round(collectionSize/loadFactor)+1;
    }

    public static String generateDownloadedFileName(String fileName, String extension) {
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateTime = dateFormatter.format(new Date());
        return String.format("attachment; filename=%s%s.%s",fileName,currentDateTime,extension);
    }

    public static String msg(String msg,Object... args){
      return   String.format(msg, args) ;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();
        return BigDecimal.valueOf(value).setScale(places, RoundingMode.HALF_UP).doubleValue();
    }

    @SuppressWarnings("NullAway")
    public static <T> T castToNonNull(@Nullable T x) {
        if (x == null) {
            // YOUR ERROR LOGGING AND REPORTING LOGIC GOES HERE
           log.warn(x+"is null") ;
        }
        return x;
    }

    public static int calculateAge(LocalDate birthDate, LocalDate currentDate) {
        if ((birthDate != null) && (currentDate != null)) {
            return Period.between(birthDate, currentDate).getYears();
        } else {
            return 0;
        }
    }

//    public static  <T> void validateDto(T dto, ValidationErrors validationErrors) {
//        TypeValidator<T> typeValidator = new TypeValidator<>(dto);
//        Set<ConstraintViolation<T>> validationResultViolations = typeValidator.validate();
//        if (CollectionUtils.isNotEmpty(validationResultViolations)) {
//            validationResultViolations.forEach(cv -> {
//                validationErrors.addError(cv.getRootBeanClass().getSimpleName(),
//                        ((PathImpl) cv.getPropertyPath()).getLeafNode().asString(), cv.getInvalidValue(), cv.getMessage());
//            });
//        }
//    }

}



