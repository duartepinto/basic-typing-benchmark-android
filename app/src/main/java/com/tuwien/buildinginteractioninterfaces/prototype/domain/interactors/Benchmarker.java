package com.tuwien.buildinginteractioninterfaces.prototype.domain.interactors;

import com.tuwien.buildinginteractioninterfaces.prototype.domain.model.BenchmarkModel;
import com.tuwien.buildinginteractioninterfaces.prototype.domain.model.OptionsModel;
import com.tuwien.buildinginteractioninterfaces.prototype.util.Benchmarks;
import com.tuwien.buildinginteractioninterfaces.prototype.util.Chronometer;

public class Benchmarker {
    private final Chronometer chronometer;
    private final Callback callback;
    private final BenchmarkModel benchmark;
    private String errorsString = "";
    private String correctedString = "";


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
        float charsPerSec = chronometer.getTimeElapsed() == 0 ? 0 : (float) benchmark.getCharacters() / (chronometer.getTimeElapsed() / 1000 );
        float wordsPerSec = chronometer.getTimeElapsed() == 0 ? 0 : (float) benchmark.getTotalWords() / (chronometer.getTimeElapsed() / 1000 );
        benchmark.setCharsPerSec(charsPerSec);
        benchmark.setWordsPerSec(wordsPerSec);
        benchmark.setTimeInMiliseconds(chronometer.getTimeElapsed());
        callback.updateStats(benchmark.getCharsPerSec(), benchmark.getCorrectWords(), benchmark.getErrors());
    }

    public BenchmarkModel getBenchmark() {
        return benchmark;
    }

    public void incrementCorrectWordsCount(String word){
        benchmark.setCorrectChars(benchmark.getCorrectChars() + word.length());
        benchmark.setCorrectWords(benchmark.getCorrectWords()+1);
    }

    public void incrementWordCount(String word){
        benchmark.setTotalWords(benchmark.getTotalWords()+1);
        benchmark.setCharacters(benchmark.getCharacters()+word.length());
    }

    @SuppressWarnings("unused")
    public void incrementErrorCount(String word, String correctWord){
        benchmark.setErrors(benchmark.getErrors()+1);
        updateMinimumStringErrorRate(word, correctWord);
    }

    void updateMinimumStringErrorRate(String word, String correctWord){
        errorsString += word;
        correctedString += correctWord;
        benchmark.setMinimumStringDistanceErrorRate(Benchmarks.msdErrorRate(errorsString,correctedString));
    }

    public void incrementBackspace(){
        benchmark.setBackspace(benchmark.getBackspace()+1);
    }

    public void incrementKeyStrokes() {
        benchmark.setKeystrokes(benchmark.getKeystrokes()+1);
    }

}

