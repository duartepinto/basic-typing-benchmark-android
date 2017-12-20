package com.tuwien.buildinginteractioninterfaces.prototype.presentation

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.tuwien.buildinginteractioninterfaces.prototype.R
import com.tuwien.buildinginteractioninterfaces.prototype.data.local.TwelveDictsDictionaryRepository
import com.tuwien.buildinginteractioninterfaces.prototype.domain.repository.local.AbstractDictionaryRepository

class PlayGame : AppCompatActivity() {

    lateinit var button: Button
    lateinit var currentWord: TextView
    lateinit var nextWord: TextView
    lateinit var dictionaryRepository: AbstractDictionaryRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_game)

        button = findViewById<Button>(R.id.start_button)
        currentWord = findViewById(R.id.current_word);
        nextWord = findViewById(R.id.next_word)

        dictionaryRepository = TwelveDictsDictionaryRepository(this);
        currentWord.text = dictionaryRepository.randomWord
        nextWord.text = dictionaryRepository.randomWord

    }
}
