package com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain;

import android.text.Editable;

import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.model.OptionsModel;
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.repository.local.BenchmarkRepository;
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.repository.local.DictionaryRepository;
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.util.Chronometer;

public class SentencesGameMode extends GameMode {
    private String[] currentInputArray;

    public SentencesGameMode(Callback callback, Benchmarker.Callback benchmarkerCallback, DictionaryRepository dictionaryRepository, BenchmarkRepository benchmarkRepository, Chronometer chronometer, OptionsModel options, String keyboardApp) {
        super(callback, benchmarkerCallback, dictionaryRepository, benchmarkRepository, chronometer, options, keyboardApp);
    }

    @Override
    void startInputs() {
        super.startInputs();
        currentInputArray = currentInput.split("\\s+");
    }

    @Override
    void generateNextInput() {
        super.generateNextInput();
        currentInputArray = currentInput.split("\\s+");
    }

    @Override
    public void afterTextChanged(Editable s) {
        incrementKeyStrokes(s);
        benchmarker.addToInputStream(s.toString());

        String str = getCleanString(s);

        int completedWords = getCompletedWords(str);

        if(currentInputArray!= null
                && completedWords == currentInputArray.length){
            String[] splited = str.split("\\s+");
            boolean isCorrect = true;

            for(int i = 0; i < currentInputArray.length; i++){
                if(splited[i].equals(currentInputArray[i])){ // For some reason currentWord comes with and extra space so it needs to be trimmed
                    benchmarker.incrementWordCount(splited[0]);
                    benchmarker.incrementCorrectWordsCount(currentInput);
                }else{
                    // Only counts as a failed word if the user is not correcting a mistake (pressing backspace for example)
                    if(strSize < s.length() && previousCompleteWords < completedWords){
                        benchmarker.incrementWordCount(splited[0]);
                        benchmarker.incrementErrorCount();
                        isCorrect = false;
                    }
                }
            }

            benchmarker.addSubmittedInput(s.toString());

            if(isCorrect || isSkipOnFail()){
                skipToNextInput(s); // Remove the wrong word
            }else{
                benchmarker.addToTranscribedString(currentInput);
            }

            benchmarker.updateStats();
        }

        incrementBackspace(s);

        previousCompleteWords = completedWords;
        strSize = s.length();

        if(shouldGameFinish())
            finishGame();
    }

    /*
     * Deletes the first word, the right space next to it, and the left spaces from the EditText
     */
    private void skipToNextInput(Editable s){
        // strSize has to be updated before s.replace otherwise there might be conflicts when the s.replace() triggers the afterTextChanged() function
        strSize = 0;

        s.replace(0, s.length(), "", 0,0);
        this.generateNextInput();
    }
}
