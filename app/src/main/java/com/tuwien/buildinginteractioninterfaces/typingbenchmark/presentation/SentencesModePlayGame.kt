package com.tuwien.buildinginteractioninterfaces.typingbenchmark.presentation

import android.os.Build
import android.provider.Settings
import android.text.Html
import android.widget.TextView
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.R
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.data.local.room.RoomDatabase
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.Benchmarker
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.GameMode
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.SentencesGameMode
import kotlinx.android.synthetic.main.benchmarks_ingame_stats.*
import kotlinx.android.synthetic.main.play_input_menu.*

class SentencesModePlayGame : AbstractPlayGame() {
    override fun setContentView() {
        setContentView(R.layout.activity_sentences_mode_play_game)
    }

    override fun createGameMode(): GameMode {
        @Suppress("DEPRECATION")
        val gameCallback = object: SentencesGameMode.Callback {
            override fun inputStateUpdate(currentInputStateful :String?) {
                if(currentInputStateful != null) {
                    var input :String
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        val green = String.format("#%06X", (0xFFFFFF and getColor(R.color.feedback_correct)))
                        val red = String.format("#%06X", (0xFFFFFF and getColor(R.color.feedback_incorrect)))

                        input = currentInputStateful.replace("<font color='green'>", "<font color='"+green+"'>")
                        input = input.replace("<font color='red'>", "<font color='"+red+"'>")
                    }else{
                        input = currentInputStateful
                    }
                    current_input.setText(Html.fromHtml(input),TextView.BufferType.SPANNABLE)
                }
            }

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