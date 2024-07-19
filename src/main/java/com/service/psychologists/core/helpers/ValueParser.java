package com.service.psychologists.core.helpers;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class ValueParser {

    public static Object parseStringToNumber(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ignored) {
        }

        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException ignored) {
        }

        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException ignored) {
        }

        return value;
    }

    public static Object parseStringToDate(String value) {
        try {
            return Date.from(LocalDateTime.parse(value).atZone(ZoneId.systemDefault()).toInstant());
        } catch (DateTimeParseException ignored) {
        }

        try {
            return Date.from(OffsetDateTime.parse(value).toLocalDateTime().atZone(ZoneId.systemDefault()).toInstant());
        } catch (DateTimeParseException ignored) {
        }

        return value;

    }

    public static Object parseStringToBoolean(String string) {
        if (string.equalsIgnoreCase("true")) {
            return true;
        } else if (string.equalsIgnoreCase("false")) {
            return false;
        } else {
            return string;
        }
    }

    public static Object parseStringToNull(String string) {
        if (string.equalsIgnoreCase("null")) {
            return null;
        }
        return string;
    }

    public static Object parseString(String string) {
        Object parsedToNumber = parseStringToNumber(string);

        if (!(parsedToNumber instanceof String)) {
            return parsedToNumber;
        }

        Object parsedToDate = parseStringToDate(string);

        if (!(parsedToDate instanceof String)) {
            return parsedToDate;
        }

        Object parsedToBoolean = parseStringToBoolean(string);

        if (!(parsedToBoolean instanceof Boolean)) {
            return parsedToBoolean;
        }

        Object parsedToNull = parseStringToNull(string);

        if (!(parsedToNull instanceof String)) {
            return parsedToNull;
        }

        return string;
    }
}


