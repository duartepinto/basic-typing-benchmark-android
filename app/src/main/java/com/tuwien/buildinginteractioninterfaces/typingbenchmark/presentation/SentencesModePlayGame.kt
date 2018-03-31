package com.tuwien.buildinginteractioninterfaces.typingbenchmark.presentation

import android.provider.Settings
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.R
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.data.local.room.RoomDatabase
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.Benchmarker
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.GameMode
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.SentencesGameMode
import kotlinx.android.synthetic.main.benchmarks_ingame_stats.*

class SentencesModePlayGame : AbstractPlayGame() {
    override fun setContentView() {
        setContentView(R.layout.activity_sentences_mode_play_game)
    }

    override fun createGameMode(): GameMode {
        val gameCallback = object: GameMode.Callback {
            override fun finishGame() {
                this@SentencesModePlayGame.finishGame()
            }

            override fun updateWords(currentInput: String?, nextInput: String?) {
                if (currentInput != null) {
                    if (nextInput != null) {
                        updateInputTextViews(currentInput)
                    }
                }
            }
        }

        val benchmarkerCallback = Benchmarker.Callback { wpm, ksps, kspc, msdErrorRate, correctWords, failedWords -> updateStatsTextViews(wpm, ksps, kspc, msdErrorRate,correctWords, failedWords) }

        val keyboardApp = Settings.Secure.getString(getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD)

        val benchmarkRepository = RoomDatabase.instance.getDatabase(applicationContext).benchmarkDao()

        val game = SentencesGameMode(gameCallback, benchmarkerCallback,
                dictionaryRepository,
                benchmarkRepository,
                chronometer,
                options,
                keyboardApp)

        return game
    }

}