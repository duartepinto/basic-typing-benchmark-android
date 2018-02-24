package com.tuwien.buildinginteractioninterfaces.prototype.domain.repository.local;

public interface DictionaryRepository {
    String getRandomWord();
    String getRandomWord(int numLetters);
}
