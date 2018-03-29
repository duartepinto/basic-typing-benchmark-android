package com.tuwien.buildinginteractioninterfaces.typingbenchmark.data.room;

import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;

import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.model.OptionsModel;

import java.util.Date;

import static com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.model.OptionsModel.*;


public class CustomConverters {


    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}