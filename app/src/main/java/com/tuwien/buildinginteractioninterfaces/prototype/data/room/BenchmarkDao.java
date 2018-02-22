package com.tuwien.buildinginteractioninterfaces.prototype.data.room;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.tuwien.buildinginteractioninterfaces.prototype.domain.model.Benchmark;

import java.util.List;

@Dao
public interface BenchmarkDao {
    @Query("SELECT * FROM benchmark")
    List<Benchmark> getAll();

    @Query("SELECT * FROM benchmark WHERE uid IN (:benchmarkIds)")
    List<Benchmark> loadAllByIds(int[] benchmarkIds);

    @Query("SELECT * FROM benchmark WHERE uid LIKE :benchmarkId")
    Benchmark findByUID(int benchmarkId);

    @Insert
    void insertAll(Benchmark... benchmarks);

    @Delete
    void delete(Benchmark bencmarks);
}

