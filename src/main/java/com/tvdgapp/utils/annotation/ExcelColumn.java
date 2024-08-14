package com.tvdgapp.utils.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelColumn {
    String name();
    int col();
    String numberFormat() default "General";
    String errMsg() default "";
}
