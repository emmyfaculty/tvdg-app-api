package com.tvdgapp.utils;



import com.tvdgapp.constants.DateConstants;
import com.tvdgapp.exceptions.TvdgAppSystemException;
import org.apache.commons.lang3.time.DateUtils;

import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/**
 * FourFourTwoDateUtils Object for transforming date to String and back
 *
 */

public class TvdgAppDateUtils {

    public static Date formatStringToDate(String dateString) {
        /*if (dateString == null) {
            return null;
        }*/
        Objects.requireNonNull(dateString);
        try {
            SimpleDateFormat sdFormatter = new SimpleDateFormat(
                    DateConstants.DATE_DB_FORMAT);
            return sdFormatter.parse(dateString);
        } catch (ParseException e) {
            throw new TvdgAppSystemException(e);
        }

    }

    public static Date formatStringToDate(String dateString, String pattern) {
        /*if (dateString == null||dateString.isBlank()) {
            return null;
        }*/
        Objects.requireNonNull(dateString);
        Objects.requireNonNull(pattern);
        try {
            SimpleDateFormat sdFormatter = new SimpleDateFormat(pattern);
            return sdFormatter.parse(dateString);
        } catch (ParseException e) {
            throw new TvdgAppSystemException(e);
        }

    }

    public static String formatDateToString(Date date) {
        // Create an instance of SimpleDateFormat used for formatting
        // the string representation of date (month/day/year)
        /*if (date == null) {
            return null;
        }*/
        Objects.requireNonNull(date);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        // Using DateFormat format method we can create a string
        // representation of a date with the defined format.
        return df.format(date);
    }

    public static String formatDateToString(Date date, String pattern) {
        // Create an instance of SimpleDateFormat used for formatting
        // the string representation of date (month/day/year)
        /*if (date == null) {
            return null;
        }*/
        Objects.requireNonNull(date);
        Objects.requireNonNull(pattern);
        DateFormat df = new SimpleDateFormat(pattern);
        // Using DateFormat format method we can create a string
        // representation of a date with the defined format.
        return df.format(date);

    }

    public static int compareDate(String dateFrom, String dateTo, String pattern) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTime(formatStringToDate(dateFrom, pattern));
        cal2.setTime(formatStringToDate(dateTo, pattern));
        if (cal1.before(cal2)) {
            return 0;
        } else if (cal1.after(cal2)) {
            return 1;
        } else {
            return -1;
        }
    }

   /* public static Date today() {
       return Date.from(Instant.now());
    }*/

    public static Date today() {
        return DateUtils.truncate(now(),Calendar.DATE);
    }

    public static Date today(String pattern) {
        SimpleDateFormat formatter = new SimpleDateFormat(
                pattern);
        try {
            return DateUtils.truncate(formatter.parse(formatter.format(Date.from(Instant.now()))),Calendar.DATE);
        } catch (ParseException e) {
            throw new TvdgAppSystemException(e);
        }
    }

//    public static LocalDateTime now() {
//        return LocalDateTime.from(Instant.now());
//    }
    public static Date now() {
        return Date.from(Instant.now());
    }

    public static String formatStringToString(String convertDateStr, String fromFormat, String toFormat) {
        SimpleDateFormat format1 = new SimpleDateFormat(fromFormat);
        SimpleDateFormat format2 = new SimpleDateFormat(toFormat);
        Date date = null;
        try {
            date = format1.parse(convertDateStr);
        } catch (ParseException e) {
            throw new TvdgAppSystemException(e);
        }
        return format2.format(date);
    }

    public static String formatStringToString(String convertDateStr, String toFormat) {
        SimpleDateFormat format1 = new SimpleDateFormat(DateConstants.DATE_INPUT_FORMAT);
        SimpleDateFormat format2 = new SimpleDateFormat(toFormat);
        Date date = null;
        try {
            date = format1.parse(convertDateStr);
        } catch (ParseException e) {
            throw new TvdgAppSystemException(e);
        }
        return format2.format(date);
    }

    public static String getPresentYear() {

        Date dt = new Date();

        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        return format.format(new Date(dt.getTime()));
    }

    public static Date getDateFromDateTime(Date date) {
        return DateUtils.truncate(date,Calendar.DATE);
    }

    public static boolean isValidFormat(String format, String value) {
        if(value==null){
          return false;
        }
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return date != null;
    }

    public static String getShortMonthName(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        final String[] months = new DateFormatSymbols(Locale.ENGLISH).getShortMonths();
        return months[calendar.get(Calendar.MONTH)];
    }

    public static int[] getDateParts(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH) + 1;
        int year = cal.get(Calendar.YEAR);
       return new int[]{month, year};
    }

    public static LocalDate convertDateToLocalDate(Date date){
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static String getMonthName(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        final String[] months = new DateFormatSymbols(Locale.ENGLISH).getMonths();
        return months[calendar.get(Calendar.MONTH)];
    }


    public static Date convertLocalDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date convertLocalDateTimeToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDateTime convertDateToLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static LocalDate lastDayOfMonth(short year, short month) {
        return YearMonth.of( year , month ).atEndOfMonth();
    }

    public static LocalDate firstDayOfMonth(short year, short month) {
        return YearMonth.of( year , month ).atDay(1);
    }


//    public static Date getDateInCurrMonthUpToCurrentDate(int todaysDayNum) {
//       return DateUtils.addDays(today(), -(CommonUtils.getRandomInt(0, todaysDayNum)));
//    }

    public static LocalDate firstDayOfMonth() {
        YearMonth ym = YearMonth.from(Instant.now().atZone(ZoneId.of("UTC")));
        return ym.atDay(1);
    }
}
