package com.tuwien.buildinginteractioninterfaces.prototype.domain.interactors;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.tuwien.buildinginteractioninterfaces.prototype.domain.executor.Executor;
import com.tuwien.buildinginteractioninterfaces.prototype.domain.executor.MainThread;
import com.tuwien.buildinginteractioninterfaces.prototype.domain.model.OptionsModel;
import com.tuwien.buildinginteractioninterfaces.prototype.domain.repository.local.BenchmarkRepository;
import com.tuwien.buildinginteractioninterfaces.prototype.domain.repository.local.DictionaryRepository;
import com.tuwien.buildinginteractioninterfaces.prototype.util.Chronometer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("WeakerAccess")
public class GameInteractor extends AbstractInteractor implements TextWatcher, Chronometer.OnChronometerTickListener{

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
                          EditText input,
                          OptionsModel options,
                          String keyboardApp,
                          BenchmarkRepository benchmarkRepository) {
        super(threadExecutor, mainThread);

        this.dictionaryRepository = dictionaryRepository;
        this.chronometer = chronometer;
        this.input = input;
        this.callback = callback;
        this.options = options;
        this.benchmarkRepository = benchmarkRepository;

        benchmarker = new Benchmarker(chronometer, benchmarkerCallback, options, keyboardApp);
        chronometer.setOnChronometerTickListener(this);

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
        benchmarker.incrementKeyStrokes();
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

        if(strSize > s.length() ){
            benchmarker.incrementBackspace();
        }

        previousCompleteWords = completedWords;
        strSize = s.length();

        if(shouldGameFinish())
            finishGame();
    }

    /*
     * Deletes the first word, the right space next to it, and the left spaces from the EditText
     */
    private void skipToNextWord(Editable s, String[] splited, int trimmedLeftSpaces){
        int cuttingLength = splited[0].length() + 1 + trimmedLeftSpaces;
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

    public void setBenchmarker(Benchmarker benchmarker) {
        this.benchmarker = benchmarker;
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
