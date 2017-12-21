package com.tuwien.buildinginteractioninterfaces.prototype.domain.interactors;

import com.tuwien.buildinginteractioninterfaces.prototype.domain.model.BenchmarkModel;
import com.tuwien.buildinginteractioninterfaces.prototype.util.Chronometer;

public class Benchmarker {
    private final Chronometer chronometer;
    private final Callback callback;
    private final BenchmarkModel benchmark;


    public interface Callback{
        void updateStats(float velocity, int correctWords, int failedWords);
    }

    public Benchmarker(Chronometer chronometer,
                       Callback callback) {

        this.chronometer = chronometer;
        this.callback = callback;
        this.benchmark = new BenchmarkModel();

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                updateStats();
            }
        });
    }

    void updateStats(){
        float velocity = chronometer.getTimeElapsed() == 0 ? 0 : (float) benchmark.getCorrectChars() / (chronometer.getTimeElapsed() / 1000 );
        benchmark.setVelocity(velocity);
        callback.updateStats(benchmark.getVelocity(), benchmark.getCorrectWords(), benchmark.getFailedWords());
    }

    public BenchmarkModel getBenchmark() {
        return benchmark;
    }

}
