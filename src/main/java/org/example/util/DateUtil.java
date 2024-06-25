package org.example.util;

import java.sql.Date;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    public static String getDate() {
        Instant instant = Instant.now();
        // Convert to local time zone for formatting
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss.SSS zzz yyyy");
        String formattedDate = instant.atZone(ZoneId.systemDefault()).format(formatter);
        return formattedDate;
    }

}
