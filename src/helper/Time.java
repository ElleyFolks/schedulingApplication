package helper;

import main.Main;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class Time {

    public static String changeLocalToMilitary(String dateTime){
        // time in local
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mma");
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime,formatter);

        // Converting time to 24 hour time
        LocalDateTime militaryDateTime = localDateTime
                .atZone(Main.userTimeZone)
                .toLocalDateTime();

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return militaryDateTime.format(outputFormatter);
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

    public static LocalDateTime militaryToLocalTime(String dateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime parsedInput = LocalDateTime.parse(dateTime,formatter);

        // Convert to local with AM or PM
        LocalDateTime localDateTime = parsedInput.atZone(Main.userTimeZone)
                .withZoneSameInstant(ZoneOffset.UTC)
                .toLocalDateTime();

        return localDateTime;
    }
}
