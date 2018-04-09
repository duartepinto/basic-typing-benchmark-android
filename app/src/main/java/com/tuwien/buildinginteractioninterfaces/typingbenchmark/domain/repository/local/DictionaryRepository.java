package com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.repository.local;

public interface DictionaryRepository {
    String getRandomString();
    String getRandomString(int numLetters);
}
