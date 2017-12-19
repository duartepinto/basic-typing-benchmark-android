package com.tuwien.buildinginteractioninterfaces.prototype.presentation

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.tuwien.buildinginteractioninterfaces.prototype.R

class PlayGame : AppCompatActivity() {

    lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_game)

        button = findViewById<Button>(R.id.start_button)


    }
}
