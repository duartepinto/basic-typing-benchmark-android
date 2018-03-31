package com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain;

import android.text.Editable;

import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.model.OptionsModel;
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.repository.local.BenchmarkRepository;
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.repository.local.Clock;
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.repository.local.DictionaryRepository;
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.util.Chronometer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SingleWordsGameMode extends GameMode {

    public SingleWordsGameMode(Callback callback, Benchmarker.Callback benchmarkerCallback, DictionaryRepository dictionaryRepository, BenchmarkRepository benchmarkRepository, Chronometer chronometer, OptionsModel options, String keyboardApp, Clock clock) {
        super(callback, benchmarkerCallback, dictionaryRepository, benchmarkRepository, chronometer, options, keyboardApp, clock);
    }

    public SingleWordsGameMode(Callback callback, Benchmarker.Callback benchmarkerCallback, DictionaryRepository dictionaryRepository, BenchmarkRepository benchmarkRepository, Chronometer chronometer, OptionsModel options, String keyboardApp) {
        super(callback, benchmarkerCallback, dictionaryRepository, benchmarkRepository, chronometer, options, keyboardApp);
    }

    @Override
    public void afterTextChanged(Editable s) {
        incrementKeyStrokes(s);
        benchmarker.addToInputStream(s.toString());

        String str = getTrimmedOnLeftString(s);
        int trimmedLeftSpaces = s.toString().length() - str.length();
        int completedWords = getCompletedWords(str);

        if(completedWords > 0){
            String[] splited = str.split("\\s+");
            if(splited[0].equals(currentInput.trim())){ // For some reason currentWord comes with and extra space so it needs to be trimmed
                benchmarker.incrementWordCount(splited[0]);
                benchmarker.incrementCorrectWordsCount(splited[0]);
                benchmarker.addSubmittedInput(splited[0]);

                skipToNextInput(s, splited, trimmedLeftSpaces); // Remove the correct word

            }else{
                // Only counts as a failed word if the user is not correcting a mistake (pressing backspace for example)
                if(strSize < s.length() && previousCompleteWords < completedWords){
                    benchmarker.incrementWordCount(splited[0]);
                    benchmarker.incrementErrorCount();
                    benchmarker.addSubmittedInput(splited[0]);
                    benchmarker.addToTranscribedString(currentInput);

                    if(isSkipOnFail()){
                        skipToNextInput(s, splited, trimmedLeftSpaces); // Remove the wrong word
                    }
                }
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
    private void skipToNextInput(Editable s, String[] splited, int trimmedLeftSpaces){
        int cuttingLength = splited[0].length() + 1 + trimmedLeftSpaces;

        // strSize has to be updated before s.replace otherwise there might be conflicts when the s.replace() triggers the afterTextChanged() function
        strSize = s.length() - cuttingLength;

        s.replace(0, cuttingLength, "", 0,0);
        generateNextInput();
    }
}
