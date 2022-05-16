package com.ionc.stickies.data;

import androidx.room.TypeConverter;

import java.util.Arrays;

public class Converters {
    @TypeConverter
    public static String[] fromString(String value) {
        value = value
                .replaceAll("(\\[|\\])", "")
                .replaceAll(", ", ",");

        return value.split(",");
    }

    @TypeConverter
    public static String fromArray(String[] array) {
        return Arrays.toString(array);
    }
}
