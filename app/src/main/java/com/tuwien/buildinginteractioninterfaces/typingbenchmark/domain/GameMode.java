package com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.tuwien.buildinginteractioninterfaces.typingbenchmark.data.local.AndroidSystemClock;
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.model.OptionsModel;
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.repository.local.BenchmarkRepository;
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.repository.local.Clock;
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.repository.local.DictionaryRepository;
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.util.Chronometer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("WeakerAccess")
public abstract class GameMode implements TextWatcher, Chronometer.OnChronometerTickListener{

    final BenchmarkRepository benchmarkRepository;
    boolean finishedGame = false;
    int strSize = 0;
    int previousCompleteWords = 0;
    Benchmarker benchmarker;
    Callback callback;
    OptionsModel options;
    private long lastPause;

    protected int getCompletedWords(String str) {
        Pattern pattern = Pattern.compile("\\s+"); // A word is completed if after the word there is any whitespace character (equal to [\r\n\t\f\v ])
        Matcher matcher = pattern.matcher(str);
        int completedWords = 0;
        while(matcher.find())
            completedWords++;
        return completedWords;
    }

    protected String getCleanString(Editable s) {
        String str = s.toString();
        str = str.replaceAll("^\\s+", ""); // Trim the left side of the string.
        str = str.toLowerCase();
        return str;
    }

    protected void removeLastBlankspace(Editable s) {
        s.replace(s.length()-1,s.length(),"");
    }

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

        benchmarker = new Benchmarker(benchmarkerCallback, options, keyboardApp);
        chronometer.setOnChronometerTickListener(this);

        startInputs();
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

    void startInputs(){
        currentInput = dictionaryRepository.getRandomString();
        nextInput = dictionaryRepository.getRandomString();
        benchmarker.addToTranscribedString(currentInput);
        callback.updateWords(currentInput, nextInput);
    }

    void generateNextInput(){
        currentInput = nextInput;
        nextInput = dictionaryRepository.getRandomString();
        benchmarker.addToTranscribedString(currentInput);
        callback.updateWords(currentInput, nextInput);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    boolean lookForBackspaces(Editable s) {
        if(strSize > s.length() ){
            benchmarker.incrementBackspace();
            return true;
        }

        return false;
    }

    void incrementKeyStrokes(String str) {
        if(strSize != str.length())
            benchmarker.incrementKeyStrokes();
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
//        saveBenchmark();
        pauseGame();
        callback.finishGame();
    }

    @Override
    public void onChronometerTick(Chronometer chronometer) {
        //See if timeElapsed has run out;
        benchmarker.updateTimeElapsedMilliseconds(chronometer.getTimeElapsed());
        if(options.getTypeGame()== OptionsModel.TypeGame.TIME && chronometer.getTimeElapsed() >= options.getFinishMark()*1000){
            finishGame();
        }
    }

    public synchronized void pauseGame(){
        lastPause = clock.elapsedRealtime();
        chronometer.stop();
        saveBenchmark();
    }

    public synchronized void continueGame(){
        chronometer.setBase(chronometer.getBase() + clock.elapsedRealtime() - lastPause);
        chronometer.start();
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

    public void setInputSource(EditText input){
        input.addTextChangedListener(this);
        input.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    EditText editText = (EditText) v;
                    editText.setText(editText.getText() + " ");
                    return true;

                }

                return false;

            }
        });

    }
}
