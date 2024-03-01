package helper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class that contains time conversion functions and time validation functions.
 *
 * @author Elley Folks
 */
public class Time {

    /**
     * Converts a local time string in the format "yyyy-MM-dd hh:mma" to 24-hour format "yyyy-MM-dd HH:mm:ss".
     *
     * @param dateTime The input local time string.
     *
     * @return A {@link LocalDateTime} object in 24-hour format.
     */
    public static LocalDateTime dateTimeLocalTo24(String dateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mma");
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime,formatter);

        // Convert to 24
        LocalDateTime militaryTime = localDateTime
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        return militaryTime;
    }

    /**
     * Converts a local time string in the format "yyyy-MM-dd hh:mma" to a 24-hour format string.
     *
     * @param dateTime The input local time string.
     *
     * @return A string representation of the time in "yyyy-MM-dd HH:mm:ss" 24-hour format.
     */
    public static String localToMilitaryString(String dateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mma");
        LocalDateTime localDateTime = LocalDateTime.parse(dateTime,formatter);

        // Convert to 24
        LocalDateTime militaryTime = localDateTime
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); //HH denotes 24-hr time

        return militaryTime.format(outputFormatter);
    }

    /**
     * Converts the opening time of a business to the user's local time.
     *
     * @param startTime The business opening time in local time.
     *
     * @return The opening time of the business in the user's local time.
     */
    public static LocalDateTime businessStartInLocal(LocalDateTime startTime){
        LocalDateTime estOpenTime = LocalDateTime.of(startTime.getYear(), startTime.getMonth(), startTime.getDayOfMonth(), 8, 0);
        ZoneId estZoneId = ZoneId.of("America/New_York");
        ZonedDateTime estZonedDateOpenTime = ZonedDateTime.of(estOpenTime, estZoneId);
        ZonedDateTime userLocalOpenTime = estZonedDateOpenTime.withZoneSameInstant(ZoneId.systemDefault());
        return userLocalOpenTime.toLocalDateTime();
    }

    /**
     * Converts the closing time of a business to the user's local time.
     *
     * @param endTime The business closing time in local time.
     *
     * @return The closing time of the business in the user's local time.
     */
    public static LocalDateTime businessEndInLocal(LocalDateTime endTime){
        LocalDateTime estCloseTime = LocalDateTime.of(endTime.getYear(), endTime.getMonth(), endTime.getDayOfMonth(), 22, 0);
        ZoneId estZoneId = ZoneId.of("America/New_York");
        ZonedDateTime estZonedDateCloseTime = ZonedDateTime.of(estCloseTime, estZoneId);
        ZonedDateTime userLocalCloseTime = estZonedDateCloseTime.withZoneSameInstant(ZoneId.systemDefault());
        return userLocalCloseTime.toLocalDateTime();
    }
}
