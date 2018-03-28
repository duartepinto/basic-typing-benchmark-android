package com.tuwien.buildinginteractioninterfaces.typingbenchmark.data.room;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.model.BenchmarkModel;
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.repository.local.BenchmarkRepository;

@Database(entities = {BenchmarkModel.class}, version = 3)
@android.arch.persistence.room.TypeConverters({CustomConverters.class, OptionsConverters.class, StringBufferConverters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract BenchmarkDao benchmarkDao();
}
