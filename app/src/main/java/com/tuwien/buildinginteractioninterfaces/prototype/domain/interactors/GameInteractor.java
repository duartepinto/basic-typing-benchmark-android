package com.tuwien.buildinginteractioninterfaces.prototype.domain.interactors;

import android.os.SystemClock;
import android.telecom.Call;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.tuwien.buildinginteractioninterfaces.prototype.domain.executor.Executor;
import com.tuwien.buildinginteractioninterfaces.prototype.domain.executor.MainThread;
import com.tuwien.buildinginteractioninterfaces.prototype.domain.executor.impl.ThreadExecutor;
import com.tuwien.buildinginteractioninterfaces.prototype.domain.repository.local.DictionaryRepository;
import com.tuwien.buildinginteractioninterfaces.prototype.threading.MainThreadImpl;
import com.tuwien.buildinginteractioninterfaces.prototype.util.Chronometer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameInteractor extends AbstractInteractor implements TextWatcher {

    private int strSize = 0;
    private int previousCompleteWords = 0;
    private Benchmarker benchmarker;
    private Callback callback;

    public interface Callback{
        void updateWords(String currentWord, String nextWord);
    }

    Chronometer chronometer;
    EditText input;
    DictionaryRepository dictionaryRepository;

    private String currentWord;
    private String nextWord;

    public GameInteractor(Executor threadExecutor,
                          MainThread mainThread,
                          Callback callback,
                          Benchmarker.Callback benchmarkerCallback,
                          DictionaryRepository dictionaryRepository,
                          Chronometer chronometer,
                          EditText input) {
        super(threadExecutor, mainThread);

        this.dictionaryRepository = dictionaryRepository;
        this.chronometer = chronometer;
        this.input = input;
        this.callback = callback;

        benchmarker = new Benchmarker(chronometer, benchmarkerCallback);
    }

    void startWords(){
        currentWord = dictionaryRepository.getRandomWord();
        nextWord = dictionaryRepository.getRandomWord();
        callback.updateWords(currentWord, nextWord);
    }

    void generateNextWord(){
        currentWord = nextWord;
        nextWord = dictionaryRepository.getRandomWord();
        callback.updateWords(currentWord,nextWord);
    }



    @Override
    public void run() {
        input.addTextChangedListener(this);
        startWords();
        input.setText("");
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String str = s.toString();
        str = str.replaceAll("^\\s+", ""); // Trim the left side of the string.
        int trimmedLeftSpaces = s.toString().length() - str.length();

        Pattern pattern = Pattern.compile("\\s");
        Matcher matcher = pattern.matcher(str);
        boolean found = matcher.find();
        int completedWords = str.length() - str.replace(" ", "").length(); // A word is completed if it has at least a blank space on it's right
        if(found){
            String[] splited = str.split("\\s+");
            if(completedWords > 0){
                if(splited[0].equals(currentWord.trim())){ // For some reason currentWord comes with and extra space so it needs to be trimmed
                    benchmarker.getBenchmark().incrementCorrectWords();
                    benchmarker.getBenchmark().incrementCorrectChars(currentWord.length());

                    s.replace(0, splited[0].length() + 1 + trimmedLeftSpaces, "", 0,0);// Remove the correct word, the right space next to it, and the left spaces from the EditText
                    generateNextWord();

                }else{
                    // Only counts as a failed word if the user
                    if(strSize < s.length() && previousCompleteWords < completedWords){
                        benchmarker.getBenchmark().incrementFailedWords();
                    }
                }
                benchmarker.updateStats();

            }
        }

        previousCompleteWords = completedWords;
        strSize = s.length();
    }

    public Benchmarker getBenchmarker() {
        return benchmarker;
    }

    public void setBenchmarker(Benchmarker benchmarker) {
        this.benchmarker = benchmarker;
    }
}
