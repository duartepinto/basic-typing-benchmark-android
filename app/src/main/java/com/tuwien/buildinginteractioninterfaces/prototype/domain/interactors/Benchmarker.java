package com.tuwien.buildinginteractioninterfaces.prototype.domain.interactors;

import com.tuwien.buildinginteractioninterfaces.prototype.domain.model.BenchmarkModel;
import com.tuwien.buildinginteractioninterfaces.prototype.domain.model.OptionsModel;
import com.tuwien.buildinginteractioninterfaces.prototype.util.Chronometer;

public class Benchmarker {
    private final Chronometer chronometer;
    private final Callback callback;
    private final BenchmarkModel benchmark;

    public interface Callback{
        void updateStats(float velocity, int correctWords, int failedWords);
    }

    public Benchmarker(Chronometer chronometer,
                       Callback callback,
                       OptionsModel options) {

        this.chronometer = chronometer;
        this.callback = callback;
        this.benchmark = new BenchmarkModel(options);

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                updateStats();
            }
        });
    }

    void updateStats(){
        float velocity = chronometer.getTimeElapsed() == 0 ? 0 : (float) benchmark.getCorrectChars() / (chronometer.getTimeElapsed() / 1000 );
        benchmark.setCharsPerSec(velocity);
        callback.updateStats(benchmark.getCharsPerSec(), benchmark.getCorrectWords(), benchmark.getErrors());
    }

    public BenchmarkModel getBenchmark() {
        return benchmark;
    }

    public void incrementCorrectWordsCount(String word){
        benchmark.incrementCorrectChars(word.length());
        benchmark.incrementCorrectWords();
    }

    public void incrementWordCount(String word){
        benchmark.setTotalWords(benchmark.getTotalWords()+1);
        benchmark.setCharacters(benchmark.getCharacters()+word.length());
    }

    public void incrementErrorCount(String word, String correctWord){
        benchmark.incrementErrors();
        //TODO Update minimumStringDistanceError
    }

    public void incrementBackspace(){
        benchmark.setBackspace(benchmark.getBackspace()+1);
    }

    public void incrementKeyStrokes() {
        benchmark.setKeystrokes(benchmark.getKeystrokes());
    }

}

