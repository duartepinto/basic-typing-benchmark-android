package com.tuwien.buildinginteractioninterfaces.typingbenchmark.data.local.room;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.model.BenchmarkModel;

@Database(entities = {BenchmarkModel.class}, version = 4)
@android.arch.persistence.room.TypeConverters({CustomConverters.class, OptionsConverters.class, StringBufferConverters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract BenchmarkDao benchmarkDao();
}
