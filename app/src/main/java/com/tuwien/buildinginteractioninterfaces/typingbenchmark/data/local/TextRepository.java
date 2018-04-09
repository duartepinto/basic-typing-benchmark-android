package com.tuwien.buildinginteractioninterfaces.typingbenchmark.data.local;

import android.content.Context;
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.R;

public class TextRepository extends AbstractDotTxtRepository {
    public TextRepository(Context context){
        super(context);
    }

    @Override
    int getDictionaryRawFile() {
        return R.raw.chi_phrases;
    }
}