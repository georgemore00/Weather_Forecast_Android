package com.example.laboration1.model;
import androidx.room.TypeConverter;
import java.util.Date;

/**
 * Used by Room Database to convert a date into a Long.
 */
public class DateConverter {

    /**
     * Takes a Long and converts it to a date.
     * @param timestamp
     * @return Date
     */
    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    /**
     * Takes a date and stores it as a Long consisting of milliseconds.
     * @param date
     * @return Long
     */
    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}