package com.tvdgapp.utils;

import jakarta.annotation.Nullable;

import java.util.Date;

public class CloneUtils {

    private CloneUtils() {
    }

    public static @Nullable Date clone(Date date) {
        if(date!=null) {
            return (Date) date.clone();
        }
        return null;
    }

}
