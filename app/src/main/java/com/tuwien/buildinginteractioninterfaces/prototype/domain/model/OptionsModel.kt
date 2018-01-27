package com.tuwien.buildinginteractioninterfaces.prototype.domain.model



class OptionsModel(var typeGame: TypeGame, var autoCorrect: Boolean) {
    enum class TypeGame{
        TIME, NUM_WORDS, NUM_ERRORS, NUM_CORRECT_WORDS, TEXT, NO_END
    }


}