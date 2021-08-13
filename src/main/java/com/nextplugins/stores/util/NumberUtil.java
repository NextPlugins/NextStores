package com.nextplugins.stores.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class NumberUtil {

    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#.##");

    public static String format(double number) {
        return DECIMAL_FORMAT.format(number);
    }

}

