package utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.TreeMap;

public class Utils {
    public static boolean isInteger(final String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    public static String TIMESTAMP_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static String SQL_TIMESTAMP_PATTERN = "yyyy-MM-dd HH24:MI:SS";
    public static String[] MONTHS = new String[] {
        "",
        "January",
        "February",
        "March",
        "April",
        "May",
        "June",
        "July",
        "August",
        "September",
        "October",
        "November",
        "December"
    };
    public static int monthNumber(final String monthName) {
        for (int i = 0; i < MONTHS.length; i++) {
            if (monthName.equals(MONTHS[i])) {
                return i;
            }
        }
        return 0;
    }

    public static String monthName(final int monthNumber) {
        if (monthNumber >= MONTHS.length) {
            return null;
        }
        return MONTHS[monthNumber];
    }

    public static String[] TYPES = new String[] {
            "Credit",
            "Debit",
            "Profit"
    };
    public static int typeNumber(final String typeName) {
        for (int i = 0; i < TYPES.length; i++) {
            if (typeName.equals(TYPES[i])) {
                return i;
            }
        }
        return 0;
    }

    public static String typeName(final int typeNumber) {
        if (typeNumber >= TYPES.length) {
            return null;
        }
        return TYPES[typeNumber];
    }

    public static String getCurrentTime() {
        final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return new SimpleDateFormat(TIMESTAMP_PATTERN).format(timestamp);
    }

    public static String sqlTimestampString() {
        return "to_timestamp('%s', '%s')".formatted(getCurrentTime(), SQL_TIMESTAMP_PATTERN);
    }

    public static String sqlTimestampString(String timestampString) {
        return "to_timestamp('%s', '%s')".formatted(timestampString, SQL_TIMESTAMP_PATTERN);
    }
}
