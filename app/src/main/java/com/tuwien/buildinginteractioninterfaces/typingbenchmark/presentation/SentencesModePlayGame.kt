package com.tuwien.buildinginteractioninterfaces.typingbenchmark.presentation

import com.tuwien.buildinginteractioninterfaces.typingbenchmark.R
import com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.GameMode

class SentencesModePlayGame : AbstractPlayGame() {
    override fun setContentView() {
        setContentView(R.layout.activity_sentences_mode_play_game)
    }

    override fun createGameMode(): GameMode {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}