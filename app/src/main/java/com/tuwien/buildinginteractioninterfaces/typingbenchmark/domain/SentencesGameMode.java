package com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain;

import android.text.Editable;

import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.model.OptionsModel;
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.repository.local.BenchmarkRepository;
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.repository.local.Clock;
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.repository.local.DictionaryRepository;
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.util.Chronometer;

public class SentencesGameMode extends GameMode {
    public SentencesGameMode(Callback callback, Benchmarker.Callback benchmarkerCallback, DictionaryRepository dictionaryRepository, BenchmarkRepository benchmarkRepository, Chronometer chronometer, OptionsModel options, String keyboardApp, Clock clock) {
        super(callback, benchmarkerCallback, dictionaryRepository, benchmarkRepository, chronometer, options, keyboardApp, clock);
    }

    public SentencesGameMode(Callback callback, Benchmarker.Callback benchmarkerCallback, DictionaryRepository dictionaryRepository, BenchmarkRepository benchmarkRepository, Chronometer chronometer, OptionsModel options, String keyboardApp) {
        super(callback, benchmarkerCallback, dictionaryRepository, benchmarkRepository, chronometer, options, keyboardApp);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /*
     * Deletes the first word, the right space next to it, and the left spaces from the EditText
     */
    void skipToNextInput(Editable s, String[] splited, int trimmedLeftSpaces){
        int cuttingLength = splited[0].length() + 1 + trimmedLeftSpaces;

        // strSize has to be updated before s.replace otherwise there might be conflicts when the s.replace() triggers the afterTextChanged() function
        strSize = s.length() - cuttingLength;

        s.replace(0, cuttingLength, "", 0,0);
        generateNextWord();
    }
}
