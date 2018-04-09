package com.tuwien.buildinginteractioninterfaces.typingbenchmark.data.local.room;

import android.arch.persistence.room.TypeConverter;

import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.model.OptionsModel;
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.model.OptionsModel.*;

public class OptionsConverters {
    private static TypeGame toTypeGame(String type){
        return TypeGame.valueOf(type);
    }

    public static String fromTypeGame(TypeGame typeGame){
        return typeGame.name();
    }

    private static Source toSource(String source){
        // IMPORTANT! During development the source 'CHI_PHRASES' used to be 'TEXT'. This IF is to keep an old database from crashing
        if (source.equals("TEXT")){
            return Source.CHI_PHRASES;
        }

        return Source.valueOf(source);
    }

    public static String fromSource(Source source){
        return source.name();
    }

    @TypeConverter
    public static String fromOptions(OptionsModel model){
        return model.getTypeGame().name() + "," +
                model.getAutoCorrect() + "," +
                model.getSkipOnFail() + "," +
                model.getSource().name() + "," +
                model.getFinishMark();
    }

    @TypeConverter
    public static OptionsModel toOptions(String options){
        String[] split = options.split(",");

        OptionsModel newOptions = new OptionsModel(
                toTypeGame(split[0]),
                split[1].equals("true"),
                split[2].equals("true"),
                toSource(split[3]));

        newOptions.setFinishMark(Integer.valueOf(split[4]));
        return newOptions;
    }
}
