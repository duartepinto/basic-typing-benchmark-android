package com.tuwien.buildinginteractioninterfaces.prototype.domain.model

import java.io.Serializable


class OptionsModel(var typeGame: TypeGame, var autoCorrect: Boolean) : Serializable{
    enum class TypeGame{
        TIME, NUM_WORDS, NUM_ERRORS, NUM_CORRECT_WORDS, TEXT, NO_END
    }


}