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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("WeakerAccess")
public class GameInteractor implements TextWatcher, Chronometer.OnChronometerTickListener{

    private final BenchmarkRepository benchmarkRepository;
    private boolean finishedGame = false;
    private int strSize = 0;
    private int previousCompleteWords = 0;
    private Benchmarker benchmarker;
    private Callback callback;
    private OptionsModel options;

    public interface Callback{
        void updateWords(String currentWord, String nextWord);
        void finishGame();
    }

    Chronometer chronometer;
    DictionaryRepository dictionaryRepository;

    private String currentWord;
    private String nextWord;
    private Clock clock = new AndroidSystemClock();

    public GameInteractor(Callback callback,
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


    public GameInteractor(Callback callback,
                          Benchmarker.Callback benchmarkerCallback,
                          DictionaryRepository dictionaryRepository,
                          BenchmarkRepository benchmarkRepository,
                          Chronometer chronometer,
                          OptionsModel options,
                          String keyboardApp){
        this(callback, benchmarkerCallback, dictionaryRepository, benchmarkRepository,chronometer,options, keyboardApp, new AndroidSystemClock());
    }

    void startWords(){
        currentWord = dictionaryRepository.getRandomWord();
        nextWord = dictionaryRepository.getRandomWord();
        benchmarker.appendNextWord(currentWord);
        callback.updateWords(currentWord, nextWord);
    }

    void generateNextWord(){
        currentWord = nextWord;
        nextWord = dictionaryRepository.getRandomWord();
        benchmarker.appendNextWord(currentWord);
        callback.updateWords(currentWord,nextWord);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        incrementKeyStrokes(s);
        benchmarker.addToInputStream(s.toString());

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
                    benchmarker.incrementWordCount(splited[0]);
                    benchmarker.incrementCorrectWordsCount(currentWord);

                    skipToNextWord(s, splited, trimmedLeftSpaces); // Remove the correct word

                }else{
                    // Only counts as a failed word if the user is not correcting a mistake (pressing backspace for example)
                    if(strSize < s.length() && previousCompleteWords < completedWords){
                        benchmarker.incrementWordCount(splited[0]);
                        benchmarker.incrementErrorCount(splited[0],currentWord);

                        if(isSkipOnFail()){
                            skipToNextWord(s, splited, trimmedLeftSpaces); // Remove the wrong word
                        }
                    }
                }
                benchmarker.updateStats();
            }
        }

        incrementBackspace(s);

        previousCompleteWords = completedWords;
        strSize = s.length();

        if(shouldGameFinish())
            finishGame();
    }

    private void incrementBackspace(Editable s) {
        if(strSize > s.length() ){
            benchmarker.incrementBackspace();
        }
    }

    private void incrementKeyStrokes(Editable s) {
        if(strSize != s.length())
            benchmarker.incrementKeyStrokes();
    }

    /*
     * Deletes the first word, the right space next to it, and the left spaces from the EditText
     */
    private void skipToNextWord(Editable s, String[] splited, int trimmedLeftSpaces){
        int cuttingLength = splited[0].length() + 1 + trimmedLeftSpaces;

        // strSize has to be updated before s.replace otherwise there might be conflicts when the s.replace() triggers the afterTextChanged() function
        strSize = s.length() - cuttingLength;

        s.replace(0, cuttingLength, "", 0,0);
        generateNextWord();
    }

    private boolean isSkipOnFail(){
        return benchmarker.getBenchmark().getOptions().getSkipOnFail();
    }

    private boolean shouldGameFinish() {
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
