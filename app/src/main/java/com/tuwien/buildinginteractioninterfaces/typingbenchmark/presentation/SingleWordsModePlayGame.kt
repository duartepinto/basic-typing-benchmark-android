package com.tuwien.buildinginteractioninterfaces.typingbenchmark.presentation

import android.provider.Settings
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.R
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.data.local.room.RoomDatabase
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.Benchmarker
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.GameMode
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.SingleWordsGameMode
import kotlinx.android.synthetic.main.activity_play_game.*
import kotlinx.android.synthetic.main.benchmarks_ingame_stats.*
import kotlinx.android.synthetic.main.play_input_menu.*

class SingleWordsModePlayGame : AbstractPlayGame() {
    override fun createGameMode(): GameMode {
        val gameCallback = object: GameMode.Callback {
            override fun finishGame() {
                this@SingleWordsModePlayGame.finishGame()
            }

            override fun updateWords(currentInput: String?, nextInput: String?) {
                if (currentInput != null) {
                    if (nextInput != null) {
                        updateInputTextViews(currentInput,nextInput)
                    }
                }
            }
        }

        val benchmarkerCallback = Benchmarker.Callback { wpm, ksps, kspc, msdErrorRate, correctWords, failedWords -> updateStatsTextViews(wpm, ksps, kspc, msdErrorRate,correctWords, failedWords) }

        val keyboardApp = Settings.Secure.getString(getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD)

        val benchmarkRepository = RoomDatabase.instance.getDatabase(applicationContext).benchmarkDao()

        val game = SingleWordsGameMode(gameCallback, benchmarkerCallback,
                dictionaryRepository,
                benchmarkRepository,
                chronometer,
                options,
                keyboardApp)

        return game
    }

    override fun setContentView() {
        setContentView(R.layout.activity_play_game)
    }

    fun updateInputTextViews(currentInput: String, nextInput: String){
        updateInputTextViews(currentInput)
        next_word.text = nextInput
    }
}