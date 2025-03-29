package com.ttdat.core.infrastructure.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberUtils {
    private NumberUtils(){

    }

    public static BigDecimal roundToUnits(BigDecimal value) {
        return value.setScale(0, RoundingMode.HALF_UP);
    }
}
