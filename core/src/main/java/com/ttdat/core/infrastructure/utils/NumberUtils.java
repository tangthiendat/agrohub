package com.ttdat.core.infrastructure.utils;

import com.ttdat.core.domain.entities.TrendType;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberUtils {
    private NumberUtils() {

    }

    public static BigDecimal roundToUnits(BigDecimal value) {
        return value.setScale(0, RoundingMode.HALF_UP);
    }

    public static BigDecimal roundToTwoDecimalPlaces(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }

    public static TrendType getTrendType(BigDecimal previousValue, BigDecimal currentValue) {
        if (previousValue.compareTo(BigDecimal.ZERO) == 0 || currentValue.compareTo(previousValue) == 0) {
            return TrendType.STABLE;
        }
        if (currentValue.compareTo(previousValue) > 0) {
            return TrendType.UP;
        }
        return TrendType.DOWN;
    }

    public static BigDecimal getChangePercentage(BigDecimal previousValue, BigDecimal currentValue) {
        if (previousValue.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        BigDecimal ratio = currentValue.divide(previousValue, 2, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));
        return ratio.compareTo(BigDecimal.valueOf(100)) >= 0 ? ratio.subtract(BigDecimal.valueOf(100)) : BigDecimal.valueOf(100).subtract(ratio);
    }
}
