package com.appnuggets.lensminder.database;

import androidx.room.TypeConverter;

import com.appnuggets.lensminder.database.entity.State;

import java.util.Date;

public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static Integer stateToInt(State value) { return value == null ? null : value.ordinal(); }

    @TypeConverter
    public static State intToState(Integer value) { return value == null ? null : State.values()[value]; }
}
