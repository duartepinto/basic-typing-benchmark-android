package com.tuwien.buildinginteractioninterfaces.prototype.domain.interactors;

import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.tuwien.buildinginteractioninterfaces.prototype.domain.executor.Executor;
import com.tuwien.buildinginteractioninterfaces.prototype.domain.executor.MainThread;
import com.tuwien.buildinginteractioninterfaces.prototype.domain.repository.local.DictionaryRepository;
import com.tuwien.buildinginteractioninterfaces.prototype.util.Chronometer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameInteractor extends AbstractInteractor implements TextWatcher {

    private int strSize = 0;
    private int previousNumSpaces = 0;

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
    private long correctChars = 0;
    private int correctWords = 0;
    private int failedWords = 0;

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
        float velocity = chronometer.getTimeElapsed() == 0 ? 0 : (float) correctChars / (chronometer.getTimeElapsed() / 1000 );
        Log.d("timeElapsed", chronometer.getTimeElapsed() + "");
        Log.d("correctChars", correctChars + "");
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
        Log.d("spaces", spaces + "");
        if(found){
            String[] splited = str.split("\\s+");
            if(spaces > 0){
                if(splited[0].equals(currentWord.trim())){
                    correctWords++;
                    correctChars += currentWord.length();
                    str = str.replaceFirst(currentWord.concat(" "),"");
                    s.replace(0, currentWord.length(), "", 0,0);
                    generateNextWord();

                }else{
                    if(strSize < s.length() && previousNumSpaces < spaces){
                        failedWords++;
                    }
                }
                updateStats();

            }
        }

        previousNumSpaces = spaces;
        strSize = s.length();
    }
}
