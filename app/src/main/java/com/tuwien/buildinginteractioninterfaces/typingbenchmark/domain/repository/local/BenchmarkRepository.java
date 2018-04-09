package com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.repository.local;


import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.model.BenchmarkModel;

import java.util.List;

public interface BenchmarkRepository {
    List<BenchmarkModel> getAll();
    List<BenchmarkModel> loadAllByIds(int[] benchmarkIds);
    BenchmarkModel findByUID(int benchmarkId);
    void insertAll(BenchmarkModel... benchmarks);
    void delete(BenchmarkModel bencmarks);
}
