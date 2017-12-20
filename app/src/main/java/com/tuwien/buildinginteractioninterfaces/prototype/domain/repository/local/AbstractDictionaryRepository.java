package com.tuwien.buildinginteractioninterfaces.prototype.domain.repository.local;

public abstract class AbstractDictionaryRepository {
    public abstract String getRandomWord();
    public abstract String getRandomWord(int numLetters);
}
