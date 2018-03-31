package com.tuwien.buildinginteractioninterfaces.typingbenchmark.data.local

import android.content.Context
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.R
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.repository.local.DictionaryRepository

class TextRepository(val context: Context) : AbstractDotTxtRepository(context) {

    private val dictionaryRawFile = R.raw.sentences // The name of the file had to be changed to two_of_twelve.txt because of compiler restrictions

    override fun getDictionaryRawFile(): Int {
        return dictionaryRawFile
    }
}