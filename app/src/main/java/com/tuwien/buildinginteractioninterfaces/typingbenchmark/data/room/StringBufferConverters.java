package com.tuwien.buildinginteractioninterfaces.typingbenchmark.data.room;

import android.arch.persistence.room.TypeConverter;

public class StringBufferConverters {
    @TypeConverter
    public static StringBuffer toStrinBuffer(String str){
        return str == null ? null : new StringBuffer(str);
    }

    @TypeConverter
    public static String fromStringBuffer(StringBuffer stringBuffer){
        return stringBuffer.toString();
    }
}
