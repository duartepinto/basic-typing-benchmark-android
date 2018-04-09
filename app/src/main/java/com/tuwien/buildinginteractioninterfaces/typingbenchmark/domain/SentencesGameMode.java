package com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain;

import android.text.Editable;

import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.model.OptionsModel;
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.repository.local.BenchmarkRepository;
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.repository.local.DictionaryRepository;
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.util.Chronometer;

public class SentencesGameMode extends GameMode {
    private String[] currentInputArray; // Array with the transcribed string (The phrase that is being evaluated) splited by words
    private SentencesInputState inputState;

    public interface Callback extends GameMode.Callback{
        void inputStateUpdate(String stateInputStateful);
    }

    public SentencesGameMode(Callback callback, Benchmarker.Callback benchmarkerCallback, DictionaryRepository dictionaryRepository, BenchmarkRepository benchmarkRepository, Chronometer chronometer, OptionsModel options, String keyboardApp) {
        super(callback, benchmarkerCallback, dictionaryRepository, benchmarkRepository, chronometer, options, keyboardApp);
    }

    @Override
    void startInputs() {
        super.startInputs();
        currentInputArray = currentInput.split("\\s+");
        inputState = new SentencesInputState(currentInputArray);
    }

    @Override
    void generateNextInput() {
        super.generateNextInput();
        currentInputArray = currentInput.split("\\s+");
        inputState = new SentencesInputState(currentInputArray);
    }

    @Override
    public void afterTextChanged(Editable s) {
        String str = s.toString().replace("\n", " ");

        incrementKeyStrokes(str);
        benchmarker.addToInputStream(str);
        boolean backspaced = lookForBackspaces(s);

        str = getCleanString(s);

        int completedWords = getCompletedWords(str);


        if(currentInputArray!= null
                && completedWords > 0){
            String[] splited = str.split("\\s+");

            boolean isCorrect = true;
            SentencesInputState newInputState = new SentencesInputState(inputState);

            for(int i =0; i < completedWords && i < currentInputArray.length; i++){

                if(splited[i].equals(currentInputArray[i])){
                    newInputState.flagAsCorrect(i);
                }else{
                    // Only counts as a failed word if the user is not correcting a mistake (pressing backspace for example)
                    if(strSize < s.length() && previousCompleteWords < completedWords){
                        newInputState.flagAsIncorrect(i);
                    }
                    // Still have to flag it as incorrect, to signal that there is still an error.
                    isCorrect = false;
                }
            }

            SentencesInputState.lookForStateDifferences(benchmarker, splited, inputState, newInputState);
            inputState = newInputState;
            updateInputStateful();


            if(completedWords == currentInputArray.length){
                benchmarker.addSubmittedInput(s.toString());

                if(isCorrect || isSkipOnFail()){
                    skipToNextInput(s);
                }else{
                    benchmarker.addToTranscribedString(currentInput);
                }
            }

            benchmarker.updateStats();

        }


        previousCompleteWords = completedWords;
        strSize = s.length();

        if(shouldGameFinish())
            finishGame();
    }

    private void updateInputStateful() {
        getCallback().inputStateUpdate(SentencesInputState.getColoredString(currentInputArray,inputState));
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

    private Callback getCallback(){
        return (Callback) callback;
    }
}
