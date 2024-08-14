package com.tvdgapp.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class CodeGeneratorUtils {


    private static final Set<Integer> generatedValues = new HashSet<>();
    private static String lastGeneratedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
    public static String generateRefreshToken() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String generateTrackingNumber() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));

        // Reset the set if the day has changed
        if (!datePart.equals(lastGeneratedDate)) {
            generatedValues.clear();
            lastGeneratedDate = datePart;
        }

        String randomPart = generateUniqueRandomValueForDay();
        return "DLE" + datePart + randomPart;
    }


    public static String generateUniqueShipmentRef() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
        String randomPart = generateUniqueRandomValueForDay();
        return "SHP-" + datePart + "-" + randomPart;
    }

    // Generate a unique random value for the day (up to 5 digits)
    private static String generateUniqueRandomValueForDay() {
        Random random = new Random();
        int uniqueValue;
        do {
            uniqueValue = random.nextInt(100000);  // Generate a random number between 0 and 99999
        } while (!isUniqueForDay(uniqueValue));  // Ensure uniqueness for the day

        generatedValues.add(uniqueValue);  // Add the unique value to the set
        return String.format("%05d", uniqueValue);  // Pad with zeros to ensure it's 5 digits
    }

    // Check if the value is unique for the current day
    private static boolean isUniqueForDay(int value) {
        return !generatedValues.contains(value);  // Check if the value is not already in the set
    }

    public static String generateReferralCode() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    public static String generateOTP() {
        // Generate a random OTP (e.g., 6 digits)
        return String.valueOf((int)(Math.random() * 900000) + 100000);
    }


}
