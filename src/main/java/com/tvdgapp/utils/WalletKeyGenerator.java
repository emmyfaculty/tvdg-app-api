package com.tvdgapp.utils;

import java.util.Random;

public class WalletKeyGenerator {

    public static String generateWalletKey(){
        Random random = new Random();

        return String.valueOf(generateRandomNumber(random, 10));
    }

    public static long generateRandomNumber(Random random, int digits) {
        long min = (long) Math.pow(10, digits - 1);
        long max = (long) Math.pow(10, digits) - 1;
        return min + ((long) (random.nextDouble() * (max - min)));
    }

}
