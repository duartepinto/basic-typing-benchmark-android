package com.tuwien.buildinginteractioninterfaces.prototype.domain.repository.local;

public abstract class DictionaryRepository {
    public abstract String getRandomWord();
    public abstract String getRandomWord(int numLetters);
}
