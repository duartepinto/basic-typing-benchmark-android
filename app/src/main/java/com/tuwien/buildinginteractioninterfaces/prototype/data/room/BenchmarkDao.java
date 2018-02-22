package com.tuwien.buildinginteractioninterfaces.prototype.data.room;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.tuwien.buildinginteractioninterfaces.prototype.domain.model.BenchmarkModel;

import java.util.List;

@Dao
public interface BenchmarkDao {
    @Query("SELECT * FROM benchmark")
    List<BenchmarkModel> getAll();

    @Query("SELECT * FROM benchmark WHERE uid IN (:benchmarkIds)")
    List<BenchmarkModel> loadAllByIds(int[] benchmarkIds);

    @Query("SELECT * FROM benchmark WHERE uid LIKE :benchmarkId")
    BenchmarkModel findByUID(int benchmarkId);

    @Insert
    void insertAll(BenchmarkModel... benchmarks);

    @Delete
    void delete(BenchmarkModel bencmarks);
}

