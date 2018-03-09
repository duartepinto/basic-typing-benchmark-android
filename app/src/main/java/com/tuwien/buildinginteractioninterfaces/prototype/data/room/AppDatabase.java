package com.tuwien.buildinginteractioninterfaces.prototype.data.room;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.tuwien.buildinginteractioninterfaces.prototype.domain.model.BenchmarkModel;
import com.tuwien.buildinginteractioninterfaces.prototype.domain.repository.local.BenchmarkRepository;

@Database(entities = {BenchmarkModel.class}, version = 2)
@android.arch.persistence.room.TypeConverters({CustomConverters.class, OptionsConverters.class, StringBufferConverters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract BenchmarkDao benchmarkDao();
}
