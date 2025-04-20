package com.ttdat.core.infrastructure.utils;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.function.Function;

public class DateUtils {

    public static boolean isInDateRange(LocalDate date, LocalDate startDate, LocalDate endDate) {
        return ((date.isAfter(startDate) || date.equals(startDate)) &&
                (date.isBefore(endDate) || date.isEqual(endDate)));
    }

    private static final Map<String, Function<String, LocalDate>> DATE_PARSERS = Map.of(
            "date", LocalDate::parse,
            "month", dateStr -> LocalDate.parse(dateStr + "-01"),
            "quarter", dateStr -> {
                String[] parts = dateStr.split("-Q");
                int year = Integer.parseInt(parts[0]);
                int quarter = Integer.parseInt(parts[1]);
                int month = (quarter - 1) * 3 + 1;
                return LocalDate.of(year, month, 1);
            },
            "year", dateStr -> LocalDate.parse(dateStr + "-01-01")
    );

    public static LocalDate parseDate(String dateStr, String type) {
        Function<String, LocalDate> parser = DATE_PARSERS.get(type.toLowerCase());
        if (parser == null) {
            throw new IllegalArgumentException("Invalid date type: " + type);
        }
        try {
            return parser.apply(dateStr);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format: " + dateStr, e);
        }
    }
}
