package com.tuwien.buildinginteractioninterfaces.prototype

import android.os.SystemClock
import android.text.Editable
import android.util.Log
import android.widget.EditText
import com.tuwien.buildinginteractioninterfaces.prototype.domain.executor.Executor
import com.tuwien.buildinginteractioninterfaces.prototype.domain.executor.MainThread
import com.tuwien.buildinginteractioninterfaces.prototype.domain.interactors.Benchmarker
import com.tuwien.buildinginteractioninterfaces.prototype.domain.interactors.GameInteractor
import com.tuwien.buildinginteractioninterfaces.prototype.domain.model.OptionsModel
import com.tuwien.buildinginteractioninterfaces.prototype.domain.repository.local.BenchmarkRepository
import com.tuwien.buildinginteractioninterfaces.prototype.domain.repository.local.DictionaryRepository
import com.tuwien.buildinginteractioninterfaces.prototype.util.Chronometer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.util.*

class GameInteractorTest{

    class TestData{
        val input = ArrayList<String>()
        val wordList = ArrayList<String>()

        init {

        }

        fun getInputAsString(): String{
            val str = StringBuilder()
            for(item in input){
                str.append(item + "\n")
            }
            return str.toString()
        }

        fun getWordListAsString(n: Int): String{
            val str = StringBuilder()

            for (i in 0..n){
                str.append(wordList[i] + "\n")
            }

            return str.toString()
        }
    }

    fun createGameInteractor(testData: TestData): GameInteractor{
        val mockDictionaryRepository = mock(DictionaryRepository::class.java)
        var randomWordIt = 0

        `when`(mockDictionaryRepository.randomWord).thenAnswer {
            val newWord = testData.wordList.get(randomWordIt)
            randomWordIt++
            newWord
        }

        val mockChronometer = mock(Chronometer::class.java)

        val mockExecutor = mock(Executor::class.java)
        val mockMainThread = mock(MainThread::class.java)
        val mockCallback = mock(GameInteractor.Callback::class.java)
        val mockBenchmarkerCallback = mock(Benchmarker.Callback::class.java)
        val mockEditText = mock(EditText::class.java)
        val mockBenchmarkRepository = mock(BenchmarkRepository::class.java)

        val options = OptionsModel(OptionsModel.TypeGame.NO_END, skipOnFail = false,source = OptionsModel.Source.TWELVE_DICTS,autoCorrect = true )
        val keyboardApp = "com.test"

        return GameInteractor(mockExecutor,
                mockMainThread,
                mockCallback,
                mockBenchmarkerCallback,
                mockDictionaryRepository,
                mockChronometer,
                mockEditText,
                options,
                keyboardApp,
                mockBenchmarkRepository)

    }

    fun runTest(testData: TestData,gameInteractor: GameInteractor){
        var editable = MockEditable(testData.input[0])

        gameInteractor.startWords()

        for (item in testData.input){
            if(item.equals("") && testData.input.indexOf(item) > 0){
                assert(editable.toString().equals(""))
            }else{
                editable = MockEditable(item)
            }
            gameInteractor.afterTextChanged(editable)
        }
    }

    @Test
    fun allInputIsCorrectTest(){
        val testData = TestData()
        testData.wordList.add("unit")
        testData.wordList.add("testing")
        testData.wordList.add("is")
        testData.wordList.add("the")
        testData.wordList.add("best")
        testData.wordList.add("untestedWord")
        testData. wordList.add("untestedWord2")

        testData.input.add("")
        testData.input.add("u")
        testData.input.add("un")
        testData. input.add("uni")
        testData.input.add("unit ")
        testData.input.add("")
        testData.input.add("t")
        testData.input.add("te")
        testData.input.add("tes")
        testData.input.add("test")
        testData.input.add("testi")
        testData.input.add("testin")
        testData.input.add("testing ")
        testData.input.add("")
        testData.input.add("i")
        testData.input.add("is ")
        testData.input.add("")
        testData.input.add("t")
        testData.input.add("th")
        testData.input.add("the ")
        testData.input.add("")
        testData.input.add("b")
        testData.input.add("be")
        testData.input.add("bes")
        testData.input.add("best ")

        val gameInteractor = createGameInteractor(testData)

        runTest(testData,gameInteractor)

        val initTimestamp = System.currentTimeMillis()
        val timeElapsed= 60000L

        val benchmark = gameInteractor.benchmarker.benchmark

        benchmark.timestamp = Date(initTimestamp)
        benchmark.timeElapsed = timeElapsed

        System.out.println(benchmark)

        val correctWords=5
        val errors=0
        val charsPerSec=0.33333334f
        val wordsPerSec=0.083333336f
        val totalWords=5
        val backspace=0
        val keystrokes=20
        val characters=20
        val wordsPerMinute=5.0f
        val keystrokesPerSecond=0.33333334f
        val keystrokesPerChar=1.0
        val minimumStringDistanceErrorRate=0.0
        val correctedErrorRate=0.0f
        val uncorrectedErrorRate=0.0f
        val totalErrorRate=0.0f
        val inputStream = testData.getInputAsString()
        val inputString = "unit\ntesting\nis\nthe\nbest\n"
        val transcribedString = "unit\ntesting\nis\nthe\nbest\nuntestedWord\n"

        assertEquals(correctWords, benchmark.correctWords)
        assertEquals(correctWords,benchmark.correctWords)
        assertEquals(errors,benchmark.errors)
        assertEquals(charsPerSec,benchmark.charsPerSec)
        assertEquals(wordsPerSec,benchmark.wordsPerSec)
        assertEquals(totalWords,benchmark.totalWords)
        assertEquals(backspace,benchmark.backspace)
        assertEquals(keystrokes,benchmark.keystrokes)
        assertEquals(characters,benchmark.characters)
        assertEquals(wordsPerMinute,benchmark.wordsPerMinute)
        assertEquals(keystrokesPerSecond,benchmark.keystrokesPerSecond)
        assertEquals(keystrokesPerChar,benchmark.keystrokesPerChar,0.0)
        assertEquals(minimumStringDistanceErrorRate,benchmark.minimumStringDistanceErrorRate,0.0)
        assertEquals(correctedErrorRate,benchmark.correctedErrorRate)
        assertEquals(uncorrectedErrorRate,benchmark.uncorrectedErrorRate)
        assertEquals(totalErrorRate,benchmark.totalErrorRate)
        assertEquals(inputStream ,benchmark.inputStream.toString())
        assertEquals(inputString ,benchmark.inputString.toString())
        assertEquals(transcribedString ,benchmark.transcribedString.toString())
    }

    @Test
    fun backspaceTest(){
        val testData = TestData()
        testData.wordList.add("unit")
        testData.wordList.add("testing")
        testData.wordList.add("is")
        testData.wordList.add("the")
        testData.wordList.add("best")
        testData.wordList.add("untestedWord")
        testData. wordList.add("untestedWord2")

        testData.input.add("")
        testData.input.add("u")
        testData.input.add("un")
        testData. input.add("uni")
        testData. input.add("un") // 1 backspace
        testData. input.add("uni")
        testData.input.add("unit ")
        testData.input.add("")
        testData.input.add("t")
        testData.input.add("te")
        testData.input.add("tes")
        testData.input.add("test")
        testData.input.add("testi")
        testData.input.add("test") // 2 backspace
        testData.input.add("tes") // 3 backspace
        testData.input.add("test")
        testData.input.add("testi")
        testData.input.add("testin")
        testData.input.add("testing ")
        testData.input.add("")
        testData.input.add("i")
        testData.input.add("") // 4 backspace
        testData.input.add("i")
        testData.input.add("is ")
        testData.input.add("")
        testData.input.add("t")
        testData.input.add("th")
        testData.input.add("the ")
        testData.input.add("")
        testData.input.add("b")
        testData.input.add("be")
        testData.input.add("bes")
        testData.input.add("best ")

        val gameInteractor = createGameInteractor(testData)

        runTest(testData,gameInteractor)

        val benchmark = gameInteractor.benchmarker.benchmark

        System.out.println(benchmark)
        val backspaces = 4
        val keystrokes = 20+8

        assertEquals(backspaces, benchmark.backspace)
        assertEquals(keystrokes, benchmark.keystrokes)



    }
}

