package com.tuwien.buildinginteractioninterfaces.typingbenchmark

import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.Benchmarker
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.GameMode
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.SingleWordsGameMode
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.model.OptionsModel
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.repository.local.BenchmarkRepository
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.repository.local.Clock
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.repository.local.DictionaryRepository
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.util.Benchmarks
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.util.Chronometer
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.util.*

class SingleWordsModeTest{

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

        /*fun getWordListAsString(n: Int): String{
            val str = StringBuilder()

            for (i in 0..n){
                str.append(wordList[i] + "\n")
            }

            return str.toString()
        }*/
    }

    fun createGameInteractor(testData: TestData): GameMode {
        val mockDictionaryRepository = mock(DictionaryRepository::class.java)
        var randomWordIt = 0

        `when`(mockDictionaryRepository.randomString).thenAnswer {
            val newWord = testData.wordList.get(randomWordIt)
            randomWordIt++
            newWord
        }

        val mockChronometer = mock(Chronometer::class.java)

        val mockCallback = mock(GameMode.Callback::class.java)
        val mockBenchmarkerCallback = mock(Benchmarker.Callback::class.java)
        val mockBenchmarkRepository = mock(BenchmarkRepository::class.java)

        val mockOptions = OptionsModel(OptionsModel.TypeGame.NO_END, skipOnFail = false,source = OptionsModel.Source.TWELVE_DICTS,autoCorrect = true )
        val mockKeyboardApp = "com.test"

        val mockClock = mock(Clock::class.java)
        `when`(mockClock.elapsedRealtime()).thenReturn(System.currentTimeMillis())

        return SingleWordsGameMode(mockCallback, mockBenchmarkerCallback,
                mockDictionaryRepository, mockBenchmarkRepository,
                mockChronometer,
                mockOptions,
                mockKeyboardApp, mockClock)
    }

    private fun runTest(testData: TestData, singleWordsMode: GameMode){
        var editable = MockEditable(testData.input[0])



        for (item in testData.input){
            if(item.equals("") && testData.input.indexOf(item) > 0){
                assert(editable.toString().equals(""))
            }else{
                editable = MockEditable(item)
            }
            singleWordsMode.afterTextChanged(editable)
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
        testData.input.add("uni")
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


    @Test
    fun errorsTest(){
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
        testData.input.add("uni")
        testData.input.add("unit ")
        testData.input.add("")
        testData.input.add("t")
        testData.input.add("te")
        testData.input.add("tes")
        testData.input.add("test")
        testData.input.add("testi")
        testData.input.add("testin")
        testData.input.add("testinng ")
        testData.input.add("testinn ")
        testData.input.add("testin ")
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

        val benchmark = gameInteractor.benchmarker.benchmark
        benchmark.timeElapsed = 60000L

        System.out.println(benchmark)
        val backspaces = 2
        val keystrokes = 20+3
        val errors = 1
        val totalWords = 6



        assertEquals(backspaces, benchmark.backspace)
        assertEquals(keystrokes, benchmark.keystrokes)
        assertEquals(errors,benchmark.errors)
        assertEquals(totalWords,benchmark.totalWords)
    }

    @Test
    fun wordsPerMinTest(){
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
        testData.input.add("bes ") // 1 error
        testData.input.add("bes") // 1 backspace
        testData.input.add("best ")

        val gameInteractor = createGameInteractor(testData)

        runTest(testData,gameInteractor)

        val initTimestamp = System.currentTimeMillis()
        val timeElapsed= 90000L

        val benchmark = gameInteractor.benchmarker.benchmark

        benchmark.timestamp = Date(initTimestamp)
        benchmark.timeElapsed = timeElapsed

        System.out.println(benchmark)

        val correctWords=5
        val errors=1
        val charsPerSec=0.22222223f
        val wordsPerSec=0.055555556f
        val backspace=1
        val correctChars=20
        val totalWords=6
        val wordsPerMinute=3.3333335f

        assertEquals(errors,benchmark.errors)
        assertEquals(charsPerSec,benchmark.charsPerSec)
        assertEquals(wordsPerSec,benchmark.wordsPerSec)
        assertEquals(totalWords,benchmark.totalWords)
        assertEquals(backspace,benchmark.backspace)
        assertEquals(wordsPerMinute,benchmark.wordsPerMinute)
        assertEquals(correctWords, benchmark.correctWords)
        assertEquals(correctChars, benchmark.correctChars)
    }


    @Test
    fun kspsAndkspcTest(){
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
        testData.input.add("unit")
        testData.input.add("unit ") // +1 keystroke
        testData.input.add("")
        testData.input.add("t")
        testData.input.add("te")
        testData.input.add("tes")
        testData.input.add("test")
        testData.input.add("testi")
        testData.input.add("testin")
        testData.input.add("testi") //+1 keystroke +1 backspace
        testData.input.add("testi ") //+1 keystroke +1 error
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
        val timeElapsed= 30000L

        val benchmark = gameInteractor.benchmarker.benchmark

        benchmark.timestamp = Date(initTimestamp)
        benchmark.timeElapsed = timeElapsed

        System.out.println(benchmark)

        val correctWords=5
        val errors=1
        val backspace=1
        val correctChars=20
        val keystrokes = 23
        val totalWords=6
        val keystrokesPerSecond = 0.76666665f
        val keystrokesPerChar = 1.15

        assertEquals(errors,benchmark.errors)
        assertEquals(totalWords,benchmark.totalWords)
        assertEquals(backspace,benchmark.backspace)
        assertEquals(correctWords, benchmark.correctWords)
        assertEquals(correctChars, benchmark.correctChars)
        assertEquals(keystrokes, benchmark.keystrokes)
        assertEquals(keystrokesPerSecond, benchmark.keystrokesPerSecond)
        assertEquals(keystrokesPerChar, benchmark.keystrokesPerChar, 0.0)
    }

    @Test
    fun msdErrorRateTest(){
        msdNoErrors()
        msdWithErrors()
    }

    private fun msdNoErrors(){
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
        val timeElapsed= 30000L

        val benchmark = gameInteractor.benchmarker.benchmark

        benchmark.timestamp = Date(initTimestamp)
        benchmark.timeElapsed = timeElapsed

        System.out.println(benchmark)

        val correctWords=5
        val errors=0
        val backspace=0
        val correctChars=20
        val keystrokes = 20
        val totalWords=5
        val msdErrorRate = 0.0
        val correctedErrorRate = 0.0f
        val uncorrectedErrorRate = 0.0f
        val totalErrorRate = 0.0f

        assertEquals(errors,benchmark.errors)
        assertEquals(totalWords,benchmark.totalWords)
        assertEquals(backspace,benchmark.backspace)
        assertEquals(correctWords, benchmark.correctWords)
        assertEquals(correctChars, benchmark.correctChars)
        assertEquals(keystrokes, benchmark.keystrokes)
        assertEquals(msdErrorRate, benchmark.minimumStringDistanceErrorRate, 0.0)
        assertEquals(correctedErrorRate, benchmark.correctedErrorRate)
        assertEquals(uncorrectedErrorRate, benchmark.uncorrectedErrorRate)
        assertEquals(totalErrorRate, benchmark.totalErrorRate)
    }

    private fun msdWithErrors() {
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
        testData.input.add("testin ") // +1 error
        testData.input.add("testing ")
        testData.input.add("")
        testData.input.add("i ")
        testData.input.add("is ")
        testData.input.add("")
        testData.input.add(" ") // +1 error
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
        val timeElapsed= 30000L

        val benchmark = gameInteractor.benchmarker.benchmark

        benchmark.timestamp = Date(initTimestamp)
        benchmark.timeElapsed = timeElapsed

        System.out.println(benchmark)

        val errors=2
        val backspace=1
        val totalWords=7

        val inputString = "unit\ntestin\ntesting\ni\nis\nthe\nbest\n"
        var transcribedString = "unit\ntesting\ntesting\nis\nis\nthe\nbest\n"

        val msd = Benchmarks.msd(inputString,transcribedString)
        val msdErrorRate = Benchmarks.msdErrorRate(msd,inputString.length, transcribedString.length)

        transcribedString += "untestedWord\n"

        assertEquals(errors,benchmark.errors)
        assertEquals(totalWords,benchmark.totalWords)
        assertEquals(backspace,benchmark.backspace)
        assertEquals(msdErrorRate, benchmark.minimumStringDistanceErrorRate, 0.0)
        assertEquals(inputString, benchmark.inputString.toString())
        assertEquals(transcribedString, benchmark.transcribedString.toString())

    }

    @Test
    fun textUppercase() {
        val testData = TestData()
        testData.wordList.add("unit")
        testData.wordList.add("testing")
        testData.wordList.add("is")
        testData.wordList.add("the")
        testData.wordList.add("best")
        testData.wordList.add("untestedWord")
        testData. wordList.add("untestedWord2")

        testData.input.add("")
        testData.input.add("U")
        testData.input.add("UN")
        testData. input.add("UNI")
        testData.input.add("UNIT ")
        testData.input.add("")
        testData.input.add("T")
        testData.input.add("Te")
        testData.input.add("Tes")
        testData.input.add("Test")
        testData.input.add("Testi")
        testData.input.add("Testin ") // +1 error
        testData.input.add("TestinG ")
        testData.input.add("")
        testData.input.add("i ")
        testData.input.add("iS ")
        testData.input.add("")
        testData.input.add(" ") // +1 error
        testData.input.add("")
        testData.input.add("t")
        testData.input.add("tH")
        testData.input.add("tHe ")
        testData.input.add("")
        testData.input.add("b")
        testData.input.add("be")
        testData.input.add("beS")
        testData.input.add("beST ")

        val gameInteractor = createGameInteractor(testData)

        runTest(testData,gameInteractor)

        val initTimestamp = System.currentTimeMillis()
        val timeElapsed= 30000L

        val benchmark = gameInteractor.benchmarker.benchmark

        benchmark.timestamp = Date(initTimestamp)
        benchmark.timeElapsed = timeElapsed

        System.out.println(benchmark)

        val errors=2
        val backspace=1
        val totalWords=7

        val inputString = "unit\ntestin\ntesting\ni\nis\nthe\nbest\n"
        var transcribedString = "unit\ntesting\ntesting\nis\nis\nthe\nbest\n"

        val msd = Benchmarks.msd(inputString,transcribedString)
        val msdErrorRate = Benchmarks.msdErrorRate(msd,inputString.length, transcribedString.length)

        transcribedString += "untestedWord\n"

        assertEquals(errors,benchmark.errors)
        assertEquals(totalWords,benchmark.totalWords)
        assertEquals(backspace,benchmark.backspace)
        assertEquals(msdErrorRate, benchmark.minimumStringDistanceErrorRate, 0.0)
        assertEquals(inputString, benchmark.inputString.toString())
        assertEquals(transcribedString, benchmark.transcribedString.toString())

    }

}

