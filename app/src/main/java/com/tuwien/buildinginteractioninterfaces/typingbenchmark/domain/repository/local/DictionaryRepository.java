package com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.repository.local;

public interface DictionaryRepository {
    String getRandomWord();
    String getRandomWord(int numLetters);
}
