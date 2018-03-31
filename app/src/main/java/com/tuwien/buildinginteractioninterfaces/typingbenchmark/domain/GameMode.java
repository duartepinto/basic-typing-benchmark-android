package com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain;

import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import com.tuwien.buildinginteractioninterfaces.typingbenchmark.data.local.AndroidSystemClock;
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.model.OptionsModel;
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.repository.local.BenchmarkRepository;
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.repository.local.Clock;
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.repository.local.DictionaryRepository;
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.util.Chronometer;

@SuppressWarnings("WeakerAccess")
public abstract class GameMode implements TextWatcher, Chronometer.OnChronometerTickListener{

    final BenchmarkRepository benchmarkRepository;
    boolean finishedGame = false;
    int strSize = 0;
    int previousCompleteWords = 0;
    Benchmarker benchmarker;
    Callback callback;
    OptionsModel options;

    public interface Callback{
        void updateWords(String currentInput, String nextInput);
        void finishGame();
    }

    Chronometer chronometer;
    DictionaryRepository dictionaryRepository;

    String currentInput;
    private String nextInput;
    private Clock clock = new AndroidSystemClock();

    public GameMode(Callback callback,
                    Benchmarker.Callback benchmarkerCallback,
                    DictionaryRepository dictionaryRepository,
                    BenchmarkRepository benchmarkRepository,
                    Chronometer chronometer,
                    OptionsModel options,
                    String keyboardApp, Clock clock) {
        this.dictionaryRepository = dictionaryRepository;
        this.chronometer = chronometer;
        this.callback = callback;
        this.options = options;
        this.benchmarkRepository = benchmarkRepository;
        this.clock = clock;

        benchmarker = new Benchmarker(chronometer, benchmarkerCallback, options, keyboardApp);
        chronometer.setOnChronometerTickListener(this);

        startWords();
        chronometer.setBase(this.clock.elapsedRealtime());
        chronometer.start();
    }


    public GameMode(Callback callback,
                    Benchmarker.Callback benchmarkerCallback,
                    DictionaryRepository dictionaryRepository,
                    BenchmarkRepository benchmarkRepository,
                    Chronometer chronometer,
                    OptionsModel options,
                    String keyboardApp){
        this(callback, benchmarkerCallback, dictionaryRepository, benchmarkRepository,chronometer,options, keyboardApp, new AndroidSystemClock());
    }

    void startWords(){
        currentInput = dictionaryRepository.getRandomString();
        nextInput = dictionaryRepository.getRandomString();
        benchmarker.appendNextWord(currentInput);
        callback.updateWords(currentInput, nextInput);
    }

    void generateNextWord(){
        currentInput = nextInput;
        nextInput = dictionaryRepository.getRandomString();
        benchmarker.appendNextWord(currentInput);
        callback.updateWords(currentInput, nextInput);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    void incrementBackspace(Editable s) {
        if(strSize > s.length() ){
            benchmarker.incrementBackspace();
        }
    }

    void incrementKeyStrokes(Editable s) {
        if(strSize != s.length())
            benchmarker.incrementKeyStrokes();
    }

    /*
     * Deletes the first word, the right space next to it, and the left spaces from the EditText
     */
    void skipToNextWord(Editable s, String[] splited, int trimmedLeftSpaces){
        int cuttingLength = splited[0].length() + 1 + trimmedLeftSpaces;

        // strSize has to be updated before s.replace otherwise there might be conflicts when the s.replace() triggers the afterTextChanged() function
        strSize = s.length() - cuttingLength;

        s.replace(0, cuttingLength, "", 0,0);
        generateNextWord();
    }

    boolean isSkipOnFail(){
        return benchmarker.getBenchmark().getOptions().getSkipOnFail();
    }

    boolean shouldGameFinish() {
        return (options.getTypeGame()== OptionsModel.TypeGame.NUM_WORDS && benchmarker.getBenchmark().getTotalWords() >= options.getFinishMark())
                || (options.getTypeGame()== OptionsModel.TypeGame.NUM_CORRECT_WORDS && benchmarker.getBenchmark().getCorrectWords() >= options.getFinishMark())
                || (options.getTypeGame()== OptionsModel.TypeGame.NUM_ERRORS && benchmarker.getBenchmark().getErrors() >= options.getFinishMark());
    }

    public Benchmarker getBenchmarker() {
        return benchmarker;
    }

    synchronized void finishGame(){
        if(finishedGame)
            return;

        finishedGame = true;
        saveBenchmark();
        callback.finishGame();
    }

    @Override
    public void onChronometerTick(Chronometer chronometer) {
        //See if timeElapsed has run out;
        if(options.getTypeGame()== OptionsModel.TypeGame.TIME && chronometer.getTimeElapsed() >= options.getFinishMark()*1000){
            finishGame();
        }
    }

    public synchronized void pauseGame(){
        saveBenchmark();
    }

    protected void saveBenchmark(){
        Log.d("Saving benchmark", benchmarker.getBenchmark().toString());
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                benchmarkRepository.insertAll(benchmarker.getBenchmark());
            }
        });
    }
}
