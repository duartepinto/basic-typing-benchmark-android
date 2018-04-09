package com.tuwien.buildinginteractioninterfaces.typingbenchmark.data.local;

import android.content.Context;

import com.tuwien.buildinginteractioninterfaces.typingbenchmark.R;

/**
 * This repository accesses the 2of12inf.txt file found in the American folder of the list collection of English words 12dicts.
 * Read more in http://wordlist.aspell.net/12dicts/
 */

public class TwelveDictsDictionaryRepository extends AbstractDotTxtRepository {

    public TwelveDictsDictionaryRepository(Context context){
        super(context);
    }

    @Override
    int getDictionaryRawFile() {
        return R.raw.two_of_twelve;
    }

}
