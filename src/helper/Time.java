package helper;

import main.Main;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class Time {

    public static String changeLocalToUTC(String dateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mma");
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime,formatter);

        // Converting time to UTC and changing format of 24 hour time
        LocalDateTime utcDateTime = localDateTime
                .atZone(Main.userTimeZone)
                .withZoneSameInstant(ZoneOffset.UTC)
                .toLocalDateTime();

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return utcDateTime.format(outputFormatter);
    }

    public static LocalDateTime localToUtcDateTime(String dateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mma");
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime,formatter);

        // Convert to UTC
        LocalDateTime utcDateTime = localDateTime
                .atZone(Main.userTimeZone)
                .withZoneSameInstant(ZoneOffset.UTC)
                .toLocalDateTime();

        return utcDateTime;
    }
}
