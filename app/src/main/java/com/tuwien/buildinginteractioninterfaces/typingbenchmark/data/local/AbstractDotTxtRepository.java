package com.tuwien.buildinginteractioninterfaces.typingbenchmark.data.local;

import android.content.Context;

import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.repository.local.DictionaryRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import kotlin.NotImplementedError;

abstract class AbstractDotTxtRepository  implements DictionaryRepository {
    protected final Context context;
    protected final String[] dictionary;
    private Random random = new Random(System.currentTimeMillis());

    AbstractDotTxtRepository(Context context) {
        this.context = context;
        dictionary = readFileFromRawDirectory(getDictionaryRawFile()).split(System.getProperty("line.separator"));
    }

    abstract int getDictionaryRawFile();

    @SuppressWarnings("ResultOfMethodCallIgnored")
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
        return byteStream != null ? byteStream.toString() : "";
    }

    private String getLine(int line){
        return dictionary[line];
    }

    @Override
    public String getRandomString() {
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

        return getLine(line);
    }

    @Override
    public String getRandomString(int numLetters) {
        throw new NotImplementedError();
    }
}
