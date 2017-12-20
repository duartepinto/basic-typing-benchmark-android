package com.tuwien.buildinginteractioninterfaces.prototype.data.local;

import android.content.Context;
import android.os.health.SystemHealthManager;

import com.tuwien.buildinginteractioninterfaces.prototype.R;
import com.tuwien.buildinginteractioninterfaces.prototype.domain.repository.local.AbstractDictionaryRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import kotlin.NotImplementedError;

/**
 * This repository accesses the 2of12inf.txt file found in the American folder of the list collection of English words 12dicts.
 * Read more in http://wordlist.aspell.net/12dicts/
 */

public class TwelveDictsDictionaryRepository extends AbstractDictionaryRepository {
    private final Context context;
    private final int dictionaryRawFile = R.raw.two_of_twelve; // The name of the file had to be changed to two_of_twelve.txt because of compiler restrictions
    private final String[] dictionary;

    Random random = new Random(System.currentTimeMillis());
    public TwelveDictsDictionaryRepository(Context context){
        this.context = context;
        dictionary = readFileFromRawDirectory(dictionaryRawFile).split(System.getProperty("line.separator"));
    }

    private String readFileFromRawDirectory(int resourceId) {

        InputStream iStream = context.getResources().openRawResource(resourceId);
        ByteArrayOutputStream byteStream = null;
        try {
            byte[] buffer = new byte[iStream.available()];
            iStream.read(buffer);
            byteStream = new ByteArrayOutputStream();
            byteStream.write(buffer);
            byteStream.close();
            iStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteStream.toString();
    }

    private String getWord(int line){
        return dictionary[line];
    }
    @Override
    public String getRandomWord() {
        int min = 0; int max = dictionary.length;
        int line;
//      This is not working because ThreadLocalRandom always uses the same seed?
        /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ThreadLocalRandom random = ThreadLocalRandom.current();
            line = random.nextInt(min, max + 1);
        } else {
            Random random = new Random(System.currentTimeMillis());
            line = random.nextInt((max - min) + 1) + min;
        }*/
        line = random.nextInt(max - min + 1) + min;

        return getWord(line);
    }

    @Override
    public String getRandomWord(int numLetters) {
        throw new NotImplementedError();
    }
}
