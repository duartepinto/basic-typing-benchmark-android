package com.tuwien.buildinginteractioninterfaces.prototype.presentation

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.tuwien.buildinginteractioninterfaces.prototype.R
import com.tuwien.buildinginteractioninterfaces.prototype.data.local.TwelveDictsDictionaryRepository
import com.tuwien.buildinginteractioninterfaces.prototype.domain.executor.impl.ThreadExecutor
import com.tuwien.buildinginteractioninterfaces.prototype.domain.interactors.Benchmarker
import com.tuwien.buildinginteractioninterfaces.prototype.domain.interactors.GameInteractor
import com.tuwien.buildinginteractioninterfaces.prototype.domain.interactors.GameInteractor.Callback
import com.tuwien.buildinginteractioninterfaces.prototype.domain.model.OptionsModel
import com.tuwien.buildinginteractioninterfaces.prototype.domain.repository.local.DictionaryRepository
import com.tuwien.buildinginteractioninterfaces.prototype.threading.MainThreadImpl
import kotlinx.android.synthetic.main.activity_play_game.*

class PlayGame : AppCompatActivity() {

    lateinit var dictionaryRepository: DictionaryRepository
    lateinit var game: GameInteractor
    lateinit var options: OptionsModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_game)

        options = intent.extras["OPTIONS"] as OptionsModel

        setOptions()

        start_button.setOnClickListener { restartGame() }
        pause_button.setOnClickListener { pauseGame() }
        continue_button.setOnClickListener { continueGame() }
        restart_button.setOnClickListener { restartGame() }
        benchmark_button.setOnClickListener { viewBenchmark() }

    }

    fun setOptions(){
        // By default it already comes with auto-correct
        if(!options.autoCorrect){
            findViewById<EditText>(R.id.keyboard_input).inputType = InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
        }

        /*when(options.typeGame){
            OptionsModel.TypeGame.TIME -> {}
            OptionsModel.TypeGame.NUM_WORDS -> {}
            OptionsModel.TypeGame.NUM_ERRORS -> {}
            OptionsModel.TypeGame.NUM_CORRECT_WORDS -> {}
            OptionsModel.TypeGame.NO_END -> {}
        }*/

        dictionaryRepository = when(options.source){
            OptionsModel.Source.TWELVE_DICTS -> TwelveDictsDictionaryRepository(this)
            else -> {
                TwelveDictsDictionaryRepository(this)
            }
        }


    }


    fun restartGame(){
        start_button.visibility = View.GONE
        pause_menu.visibility = View.GONE
        pause_button.visibility =  View.VISIBLE
        keyboard_input.visibility = View.VISIBLE
        continue_button.isEnabled = true;

        //To prevent from bugging with game type TIME
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();

        val gameCallback = object: Callback {
            override fun finishGame() {
                this@PlayGame.finishGame();
            }

            override fun updateWords(currentWord: String?, nextWord: String?) {
                if (currentWord != null) {
                    if (nextWord != null) {
                        updateWordsTextViews(currentWord,nextWord)
                    }
                }
            }
        }

        val benchmarkerCallback = object: Benchmarker.Callback{
            override fun updateStats(velocity: Float, correctWords: Int, failedWords: Int) {
                updateStatsTextViews(velocity, correctWords, failedWords)
            }
        }

        game = GameInteractor(ThreadExecutor.getInstance(),MainThreadImpl.getInstance(),
                gameCallback,
                benchmarkerCallback,
                dictionaryRepository,
                chronometer,
                keyboard_input,
                options)
        game.run()
    }

    fun finishGame(){
        pauseGame();
        continue_button.isEnabled = false;
        Toast.makeText(this, getString(R.string.game_over), Toast.LENGTH_SHORT).show()
    }

    fun pauseGame(){
        chronometer.stop()
        pause_button.visibility =  View.GONE
        pause_menu.visibility = View.VISIBLE
        keyboard_input.visibility = View.INVISIBLE
    }

    fun continueGame(){
        chronometer.start()
        pause_menu.visibility = View.GONE
        pause_button.visibility =  View.VISIBLE
        keyboard_input.visibility = View.VISIBLE
    }

    fun updateWordsTextViews(currentWord: String, nextWord: String){
        current_word.text = currentWord
        next_word.text = nextWord
    }

    fun updateStatsTextViews(velocity: Float, correctWords: Int, failedWords: Int){
        velocity_stat.text = velocity.toString()
        correct_words.text = correctWords.toString()
        failed_words.text = failedWords.toString()
    }

    fun viewBenchmark(){
        val intent = Intent(this, BenchmarkActivity::class.java)
        intent.putExtra("BENCHMARK", game.benchmarker.benchmark)
        startActivity(intent)
    }
}
