package com.tuwien.buildinginteractioninterfaces.prototype.domain.interactors;

import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.tuwien.buildinginteractioninterfaces.prototype.domain.executor.Executor;
import com.tuwien.buildinginteractioninterfaces.prototype.domain.executor.MainThread;
import com.tuwien.buildinginteractioninterfaces.prototype.domain.repository.local.DictionaryRepository;
import com.tuwien.buildinginteractioninterfaces.prototype.util.Chronometer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameInteractor extends AbstractInteractor implements TextWatcher {

    public interface Callback{
        void updateStats(float velocity, int correctWords, int failedWords);
        void updateWords(String currentWord, String nextWord);
        void updateInput(String input);
    }

    Callback callback;
    Chronometer chronometer;
    EditText input;
    DictionaryRepository dictionaryRepository;

    private String currentWord;
    private String nextWord;
    private long correctChars;
    private int correctWords;
    private int failedWords;

    public GameInteractor(Executor threadExecutor,
                          MainThread mainThread,
                          Callback callback,
                          DictionaryRepository dictionaryRepository,
                          Chronometer chronometer,
                          EditText input) {
        super(threadExecutor, mainThread);

        this.dictionaryRepository = dictionaryRepository;
        this.callback = callback;
        this.chronometer = chronometer;
        this.input = input;


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

    void updateStats(){
        float velocity = correctChars == 0 ? 0 : correctChars / chronometer.getTimeElapsed();
        callback.updateStats(velocity, correctWords, failedWords);
    }

    @Override
    public void run() {
        input.addTextChangedListener(this);
        startWords();
        input.setText("");
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                updateStats();
            }
        });
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
        str = str.replaceAll("^\\s+", "");
        Pattern pattern = Pattern.compile("\\s");
        Matcher matcher = pattern.matcher(str);
        boolean found = matcher.find();
        int spaces = str.length() - str.replace(" ", "").length();
        if(found){
            String[] splited = str.split("\\s+");
            for(int i = 0; i < spaces; i++){
                if(splited[0].equals(currentWord)){
                    correctWords++;
                    correctChars += currentWord.length();
                    str = str.replaceFirst(currentWord.concat(" "),"");
                    s.clear();
                    s.append(str);

                }else{
                    failedWords++;
                }
                updateStats();

            }
        }
    }
}
