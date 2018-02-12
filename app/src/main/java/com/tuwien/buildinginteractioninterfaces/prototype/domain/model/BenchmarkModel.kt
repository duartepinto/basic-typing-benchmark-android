package com.tuwien.buildinginteractioninterfaces.prototype.domain.model

import com.tuwien.buildinginteractioninterfaces.prototype.util.Benchmarks
import java.io.Serializable
import java.util.*

class BenchmarkModel(var options: OptionsModel): Serializable {
    var correctChars: Int = 0
        set(value) {
            field = value
            updateKeystrokesPerChar()
        }
    var correctWords: Int = 0
        set(value) {
            field = value
            updateWordsPerMinute()
        }
    var errors: Int = 0
    var charsPerSec: Float = 0.0f
    var wordsPerSec: Float = 0.0f
    var totalWords: Int = 0
    var timeInMiliseconds:Float = 0.0f
        set(value) {
            field = value
            updateWordsPerMinute()
            updateKeystrokesPerSecond()
        }

    var backspace: Int = 0
    var keystrokes: Int = 0
        set(value) {
            field = value
            updateKeystrokesPerSecond()
            updateKeystrokesPerChar()
        }
    var characters: Int = 0
    var timestamp: Date = Calendar.getInstance().getTime()
    //Entry rates
    var wordsPerMinute: Float = 0.0f
    var keystrokesPerSecond: Float = 0.0f
    //Error rates
    var keystrokesPerChar: Double = 0.0
    var minimumStringDistanceErrorRate: Double = 0.0

    fun updateKeystrokesPerChar(){
        keystrokesPerChar = Benchmarks.keystrokesPerChar(keystrokes,correctChars)
    }

    fun updateWordsPerMinute(){
        wordsPerMinute = correctWords.toFloat()/ timeInMiliseconds * 60 * 1000
    }

    fun updateKeystrokesPerSecond(){
        keystrokesPerSecond = keystrokes.toFloat() / timeInMiliseconds * 1000
    }

    override fun toString(): String {
        return "BenchmarkModel(" +
                "options=$options,\n" +
                "correctChars=$correctChars,\n" +
                "correctWords=$correctWords,\n" +
                "errors=$errors,\n" +
                "charsPerSec=$charsPerSec,\n" +
                "wordsPerSec=$wordsPerSec,\n" +
                "totalWords=$totalWords,\n" +
                "timeInMiliseconds=$timeInMiliseconds,\n" +
                "backspace=$backspace,\n" +
                "keystrokes=$keystrokes,\n" +
                "characters=$characters,\n" +
                "keystrokesPerChar=$keystrokesPerChar,\n" +
                "minimumStringDistanceErrorRate=$minimumStringDistanceErrorRate,\n" +
                "timestamp=$timestamp)"
    }


}