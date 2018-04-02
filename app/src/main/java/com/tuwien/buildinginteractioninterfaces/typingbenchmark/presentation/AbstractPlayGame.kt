package com.tuwien.buildinginteractioninterfaces.typingbenchmark.presentation

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.text.InputType
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.R
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.data.local.TextRepository
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.data.local.TwelveDictsDictionaryRepository
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.GameMode
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.model.OptionsModel
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.repository.local.DictionaryRepository
import kotlinx.android.synthetic.main.benchmarks_ingame_stats.*
import kotlinx.android.synthetic.main.play_input_menu.*

@Suppress("MemberVisibilityCanBePrivate")
abstract class AbstractPlayGame : AppCompatActivity() {

    lateinit var dictionaryRepository: DictionaryRepository
    lateinit var game: GameMode
    lateinit var options: OptionsModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView()

        options = intent.extras["OPTIONS"] as OptionsModel

        setOptions()

        start_button.setOnClickListener { restartGame() }
        pause_button.setOnClickListener { pauseGame() }
        continue_button.setOnClickListener { continueGame() }
        restart_button.setOnClickListener { restartGame() }
        benchmark_button.setOnClickListener { viewBenchmark() }

        keyboard_input.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if(!hasFocus) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0)
            }
        }

    }

    abstract fun setContentView()

    fun setOptions(){
        // By default it already comes with auto-correct
        if(!options.autoCorrect){
            findViewById<EditText>(R.id.keyboard_input).inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        }

        dictionaryRepository = when(options.source){
            OptionsModel.Source.TWELVE_DICTS -> TwelveDictsDictionaryRepository(applicationContext)
            OptionsModel.Source.TEXT -> TextRepository(this)
        }


    }

    fun focusOnInputAndShowKeyboard(){
        keyboard_input.requestFocus()
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(keyboard_input, InputMethodManager.SHOW_IMPLICIT)
    }

    fun restartGame(){
        start_button.visibility = View.GONE
        pause_menu.visibility = View.GONE
        pause_button.visibility =  View.VISIBLE
        keyboard_input.visibility = View.VISIBLE
        continue_button.isEnabled = true

        updateStatsTextViews(0f,0f,0f,0f,0,0)
        focusOnInputAndShowKeyboard()

        //To prevent from bugging with game type TIME
        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.start()

        game = createGameMode()

        keyboard_input.addTextChangedListener(game)
        keyboard_input.setText("")
    }

    abstract fun createGameMode(): GameMode

    fun finishGame(){
        pauseGame()
        continue_button.isEnabled = false
        Toast.makeText(this, getString(R.string.game_over), Toast.LENGTH_SHORT).show()
        keyboard_input.removeTextChangedListener(game)
    }

    fun pauseGame(){
        chronometer.stop()
        pause_button.visibility =  View.GONE
        pause_menu.visibility = View.VISIBLE
        keyboard_input.visibility = View.INVISIBLE
        game.pauseGame()
    }

    fun continueGame(){
        chronometer.start()
        pause_menu.visibility = View.GONE
        pause_button.visibility =  View.VISIBLE
        keyboard_input.visibility = View.VISIBLE
    }

    fun updateInputTextViews(currentInput: String){
        current_input.text = currentInput
    }

    fun updateStatsTextViews(wpm: Float, ksps: Float,kspc: Float, msdErrorRate:Float, correctWords: Int, failedWords: Int){
        wpm_stat.text = wpm.toString()
        ksps_stat.text = ksps.toString()
        kspc_stat.text = kspc.toString()
        msd_error_rate_stat.text = msdErrorRate.toString()
        correct_words.text = correctWords.toString()
        failed_words.text = failedWords.toString()
    }

    fun viewBenchmark(){
        val intent = Intent(this, BenchmarkActivity::class.java)
        intent.putExtra("BENCHMARK", game.benchmarker.benchmark)
        startActivity(intent)
    }
}
