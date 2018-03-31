package com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain;

import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.model.BenchmarkModel;
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.model.OptionsModel;
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.util.Benchmarks;
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.util.Chronometer;

@SuppressWarnings("WeakerAccess")
public class Benchmarker {
    private final Chronometer chronometer;
    private final Callback callback;
    private final BenchmarkModel benchmark;


    public interface Callback{
        void updateStats(float wpm, float ksps,float kspc,float msdErrorRate, int correctWords, int failedWords);
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
        callback.updateStats(benchmark.getWordsPerMinute(), benchmark.getKeystrokesPerSecond(), (float) benchmark.getKeystrokesPerChar(), (float) benchmark.getMinimumStringDistanceErrorRate(), benchmark.getCorrectWords(), benchmark.getErrors());
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

    public void incrementErrorCount(){
        benchmark.setErrors(benchmark.getErrors()+1);
    }

    public void addToTranscribedString(String correctInput){
        benchmark.addToTranscribedString(correctInput);
    }

    public void addSubmittedInput(String word){
        benchmark.addToInputString(word);

        updateMinimumStringErrorRate();
        updateErrorRates();
    }

    private void updateErrorRates() {
        benchmark.setCorrectedErrorRate(Benchmarks.correctedErrorRate(
                benchmark.getInputString().toString(),
                benchmark.getTranscribedString().toString(),
                benchmark.getBackspace()
        ));
        benchmark.setUncorrectedErrorRate(Benchmarks.uncorrectedErrorRate(
                benchmark.getInputString().toString(),
                benchmark.getTranscribedString().toString(),
                benchmark.getBackspace()
        ));
        benchmark.setTotalErrorRate(Benchmarks.totalErrorRate(
                benchmark.getInputString().toString(),
                benchmark.getTranscribedString().toString(),
                benchmark.getBackspace()
        ));
    }

    public void addToInputStream(String word){
        benchmark.addToInputStream(word);
    }

    private void updateMinimumStringErrorRate(){
        benchmark.setMinimumStringDistanceErrorRate(Benchmarks.msdErrorRate(benchmark.getInputString().toString(),benchmark.getTranscribedString().toString()));
    }

    public void incrementBackspace(){
        benchmark.setBackspace(benchmark.getBackspace()+1);
    }

    public void incrementKeyStrokes() {
        benchmark.setKeystrokes(benchmark.getKeystrokes()+1);
    }

}

