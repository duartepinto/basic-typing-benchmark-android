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
                if(splited[0].equals(currentInput.trim())){ // For some reason currentWord comes with and extra space so it needs to be trimmed
                    benchmarker.incrementWordCount(splited[0]);
                    benchmarker.incrementCorrectWordsCount(currentInput);

                    skipToNextWord(s, splited, trimmedLeftSpaces); // Remove the correct word

                }else{
                    // Only counts as a failed word if the user is not correcting a mistake (pressing backspace for example)
                    if(strSize < s.length() && previousCompleteWords < completedWords){
                        benchmarker.incrementWordCount(splited[0]);
                        benchmarker.incrementErrorCount(splited[0],currentInput);

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

}
