package com.tuwien.buildinginteractioninterfaces.prototype.data.room;


import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.tuwien.buildinginteractioninterfaces.prototype.domain.model.Benchmark;

@Database(entities = {Benchmark.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract BenchmarkDao benchmarkDao();
}
