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
        void updateStats(float wpm, float kspc,float msdErrorRate, int correctWords, int failedWords);
    }

    public Benchmarker(Chronometer chronometer,
                       Callback callback,
                       OptionsModel options,
                       String keyboardApp) {

        this.chronometer = chronometer;
        this.callback = callback;
        this.benchmark = new BenchmarkModel();
        benchmark.options = options;
        benchmark.keyboardApp = keyboardApp;

        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                updateStats();
            }
        });
    }

    void updateStats(){
        benchmark.setTimeElapsed(chronometer.getTimeElapsed());
        callback.updateStats(benchmark.getWordsPerMinute(), ((float) benchmark.getKeystrokesPerChar()), (float) benchmark.getMinimumStringDistanceErrorRate(), benchmark.getCorrectWords(), benchmark.getErrors());
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

    public void incrementErrorCount(String word, String correctWord){
        benchmark.setErrors(benchmark.getErrors()+1);
        updateMinimumStringErrorRate(word, correctWord);
    }

    public void addToInputStream(String word){
        benchmark.addToInputStream(word);
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

    public void appendNextWord(String word){
        benchmark.addToTranscribedString(word);
    }

}

