package com.tuwien.buildinginteractioninterfaces.prototype.domain.model

import com.tuwien.buildinginteractioninterfaces.prototype.util.Benchmarks
import java.io.Serializable
import java.util.*

class BenchmarkModel(var options: OptionsModel): Serializable {
    var correctChars: Int = 0
        set(value) {
            field = value
            updateKeystrokesPerChar()
            updateCharsPerSecond()
        }
    var correctWords: Int = 0
        set(value) {
            field = value
            updateWordsPerMinute()
            updateWordsPerSec()
        }
    var errors: Int = 0
    var totalWords: Int = 0
    var timeElapsed:Long = 0 // In miliseconds
        set(value) {
            field = value
            updateWordsPerMinute()
            updateKeystrokesPerSecond()
            updateCharsPerSecond()
            updateWordsPerSec()
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

    var inputStream: StringBuffer = StringBuffer()
    var transcribedString: StringBuffer = StringBuffer()

    //Entry rates
    var wordsPerMinute: Float = 0.0f
    var keystrokesPerSecond: Float = 0.0f
    //Error rates
    var keystrokesPerChar: Double = 0.0
    var minimumStringDistanceErrorRate: Double = 0.0
    //Custom entry rates
    var charsPerSec: Float = 0.0f
    var wordsPerSec: Float = 0.0f

    fun updateKeystrokesPerChar(){
        keystrokesPerChar = Benchmarks.keystrokesPerChar(keystrokes,correctChars)
    }

    fun updateWordsPerMinute(){
        wordsPerMinute = correctWords.toFloat()/ timeElapsed * 60 * 1000
    }

    fun updateKeystrokesPerSecond(){
        keystrokesPerSecond = (keystrokes.toFloat() - 1) / timeElapsed * 1000
    }

    fun updateCharsPerSecond(){
        charsPerSec = if(timeElapsed == 0L) 0.0f else correctChars.toFloat() / timeElapsed * 1000
    }

    fun updateWordsPerSec(){
        wordsPerSec = if(timeElapsed == 0L) 0.0f else correctWords.toFloat() / timeElapsed * 1000
    }

    fun addToTranscribedString(word: String){
        transcribedString.append(word + "\n")
    }

    fun addToInputString(word: String){
        inputStream.append(word + "\n")
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
                "timeElapsed=$timeElapsed,\n" +
                "backspace=$backspace,\n" +
                "keystrokes=$keystrokes,\n" +
                "characters=$characters,\n" +
                "keystrokesPerChar=$keystrokesPerChar,\n" +
                "minimumStringDistanceErrorRate=$minimumStringDistanceErrorRate,\n" +
                "timestamp=$timestamp)"
    }


}