package com.tuwien.buildinginteractioninterfaces.typingbenchmark.data.local;

import android.content.Context;

import com.tuwien.buildinginteractioninterfaces.typingbenchmark.R;
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.repository.local.DictionaryRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import kotlin.NotImplementedError;

/**
 * This repository accesses the 2of12inf.txt file found in the American folder of the list collection of English words 12dicts.
 * Read more in http://wordlist.aspell.net/12dicts/
 */

public class TwelveDictsDictionaryRepository extends AbstractDotTxtRepository {

    private final int dictionaryRawFile = R.raw.two_of_twelve; // The name of the file had to be changed to two_of_twelve.txt because of compiler restrictions

    public TwelveDictsDictionaryRepository(Context context){
        super(context);
    }

    @Override
    int getDictionaryRawFile() {
        return dictionaryRawFile;
    }

}
