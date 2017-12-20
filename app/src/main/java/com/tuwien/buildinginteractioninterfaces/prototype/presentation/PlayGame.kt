package com.tuwien.buildinginteractioninterfaces.prototype.presentation

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.tuwien.buildinginteractioninterfaces.prototype.R
import com.tuwien.buildinginteractioninterfaces.prototype.data.local.TwelveDictsDictionaryRepository
import com.tuwien.buildinginteractioninterfaces.prototype.domain.repository.local.AbstractDictionaryRepository
import com.tuwien.buildinginteractioninterfaces.prototype.util.Chronometer
import kotlinx.android.synthetic.main.activity_play_game.*

class PlayGame : AppCompatActivity() {

    lateinit var dictionaryRepository: AbstractDictionaryRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_game)



        dictionaryRepository = TwelveDictsDictionaryRepository(this);
        current_word.text = dictionaryRepository.randomWord
        next_word.text = dictionaryRepository.randomWord

        start_button.setOnClickListener {
            restartGame()
        }

        pause_button.setOnClickListener {
            pauseGame()
        }

        continue_button.setOnClickListener {
            continueGame()
        }

        restart_button.setOnClickListener {
            restartGame()
        }
    }


    fun restartGame(){
        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.start()
        start_button.visibility = View.GONE
        pause_menu.visibility = View.GONE
        pause_button.visibility =  View.VISIBLE
    }

    fun pauseGame(){
        chronometer.stop()
        pause_button.visibility =  View.GONE
        pause_menu.visibility = View.VISIBLE
    }

    fun continueGame(){
        chronometer.start()
        pause_menu.visibility = View.GONE
        pause_button.visibility =  View.VISIBLE
    }
}
