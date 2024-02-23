package helper;

import main.Main;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Time {

    public static LocalDateTime localTo24DateTime(String dateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mma");
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime,formatter);

        // Convert to 24
        LocalDateTime militaryTime = localDateTime
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        return militaryTime;
    }

    public static String localTo24String(String dateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mma");
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime,formatter);

        // Convert to 24
        LocalDateTime militaryTime = localDateTime
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); //HH denotes 24-hr time

        return militaryTime.format(outputFormatter);
    }

    public static LocalDateTime getBusinessOpenInLocal(LocalDateTime startTime){
        LocalDateTime estOpenTime = LocalDateTime.of(startTime.getYear(), startTime.getMonth(), startTime.getDayOfMonth(), 8, 0);
        ZoneId estZoneId = ZoneId.of("America/New_York");
        ZonedDateTime estZonedDateOpenTime = ZonedDateTime.of(estOpenTime, estZoneId);
        ZonedDateTime userLocalOpenTime = estZonedDateOpenTime.withZoneSameInstant(ZoneId.systemDefault());
        return userLocalOpenTime.toLocalDateTime();
    }

    public static LocalDateTime getBusinessOpenInLocal(){
        LocalDateTime estOpenTime = LocalDateTime.of(2024,1,1, 8, 0);
        ZoneId estZoneId = ZoneId.of("America/New_York");
        ZonedDateTime estZonedDateOpenTime = ZonedDateTime.of(estOpenTime, estZoneId);
        ZonedDateTime userLocalOpenTime = estZonedDateOpenTime.withZoneSameInstant(ZoneId.systemDefault());
        return userLocalOpenTime.toLocalDateTime();
    }

    public static LocalDateTime getBusinessCloseInLocal(LocalDateTime endTime){
        LocalDateTime estCloseTime = LocalDateTime.of(endTime.getYear(), endTime.getMonth(), endTime.getDayOfMonth(), 22, 0);
        ZoneId estZoneId = ZoneId.of("America/New_York");
        ZonedDateTime estZonedDateCloseTime = ZonedDateTime.of(estCloseTime, estZoneId);
        ZonedDateTime userLocalCloseTime = estZonedDateCloseTime.withZoneSameInstant(ZoneId.systemDefault());
        return userLocalCloseTime.toLocalDateTime();
    }

    public static LocalDateTime getBusinessCloseInLocal(){
        LocalDateTime estCloseTime = LocalDateTime.of(2024,1,1, 22, 0);
        ZoneId estZoneId = ZoneId.of("America/New_York");
        ZonedDateTime estZonedDateCloseTime = ZonedDateTime.of(estCloseTime, estZoneId);
        ZonedDateTime userLocalCloseTime = estZonedDateCloseTime.withZoneSameInstant(ZoneId.systemDefault());
        return userLocalCloseTime.toLocalDateTime();
    }

    public static LocalDateTime localToUtcDateTime(String dateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mma");
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime,formatter);

        // Convert to UTC
        LocalDateTime utcDateTime = localDateTime
                .atZone(ZoneId.systemDefault())
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
