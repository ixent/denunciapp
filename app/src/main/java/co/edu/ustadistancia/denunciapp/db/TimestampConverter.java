package co.edu.ustadistancia.denunciapp.db;

import android.arch.persistence.room.TypeConverter;
import android.icu.text.SimpleDateFormat;
import android.provider.SyncStateContract;

import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;

public class TimestampConverter {
    static DateFormat df = DateFormat.getDateTimeInstance();

    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}