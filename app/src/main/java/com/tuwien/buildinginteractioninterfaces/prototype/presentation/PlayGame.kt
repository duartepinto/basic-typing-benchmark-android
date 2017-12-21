package com.tuwien.buildinginteractioninterfaces.prototype.presentation

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import com.tuwien.buildinginteractioninterfaces.prototype.R
import com.tuwien.buildinginteractioninterfaces.prototype.data.local.TwelveDictsDictionaryRepository
import com.tuwien.buildinginteractioninterfaces.prototype.domain.executor.impl.ThreadExecutor
import com.tuwien.buildinginteractioninterfaces.prototype.domain.interactors.Benchmarker
import com.tuwien.buildinginteractioninterfaces.prototype.domain.interactors.GameInteractor
import com.tuwien.buildinginteractioninterfaces.prototype.domain.interactors.GameInteractor.Callback
import com.tuwien.buildinginteractioninterfaces.prototype.domain.repository.local.DictionaryRepository
import com.tuwien.buildinginteractioninterfaces.prototype.threading.MainThreadImpl
import kotlinx.android.synthetic.main.activity_play_game.*

class PlayGame : AppCompatActivity() {

    lateinit var dictionaryRepository: DictionaryRepository
    lateinit var game: GameInteractor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_game)


        start_button.setOnClickListener { restartGame() }
        pause_button.setOnClickListener { pauseGame() }
        continue_button.setOnClickListener { continueGame() }
        restart_button.setOnClickListener { restartGame() }


    }


    fun restartGame(){
        start_button.visibility = View.GONE
        pause_menu.visibility = View.GONE
        pause_button.visibility =  View.VISIBLE
        keyboard_input.visibility = View.VISIBLE


        dictionaryRepository = TwelveDictsDictionaryRepository(this);
        var gameCallback = object: Callback {
            override fun updateWords(currentWord: String?, nextWord: String?) {
                if (currentWord != null) {
                    if (nextWord != null) {
                        updateWordsTextViews(currentWord,nextWord)
                    }
                };
            }
        }

        var benchmarkerCallback = object: Benchmarker.Callback{
            override fun updateStats(velocity: Float, correctWords: Int, failedWords: Int) {
                updateStatsTextViews(velocity, correctWords, failedWords)
            }
        }

        game = GameInteractor(ThreadExecutor.getInstance(),MainThreadImpl.getInstance(),
                gameCallback,
                benchmarkerCallback,
                dictionaryRepository,
                chronometer,
                keyboard_input);
        game.run();
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
        current_word.text = currentWord;
        next_word.text = nextWord
    }

    fun updateStatsTextViews(velocity: Float, correctWords: Int, failedWords: Int){
        velocity_stat.text = velocity.toString()
        correct_words.text = correctWords.toString()
        failed_words.text = failedWords.toString()
    }
}
