package com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain;

import java.util.ArrayList;
import java.util.Collections;

import static com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.SentencesInputState.State.BLANK;
import static com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.SentencesInputState.State.CORRECT;
import static com.tuwien.buildinginteractioninterfaces.typingbenchmark.domain.SentencesInputState.State.INCORRECT;

public class SentencesInputState {
    private ArrayList<State> inputState;

    enum State{
        CORRECT,
        INCORRECT,
        BLANK
    }

    SentencesInputState(String[] inputArray){
        this.inputState = new ArrayList<>(Collections.nCopies(inputArray.length, BLANK));
    }

    private SentencesInputState(ArrayList<State> inputState){
        this.inputState = new ArrayList<>(inputState);
    }

    SentencesInputState(SentencesInputState oldState){
        this(oldState.inputState);
    }

    public void flagAsCorrect(int index){
        this.inputState.set(index, CORRECT);
    }

    public void flagAsIncorrect(int index){
        this.inputState.set(index, INCORRECT);
    }

    public void flagAsBlank(int index){
        this.inputState.set(index, BLANK);
    }

    public static void lookForStateDifferences(Benchmarker benchmarker, String[] splited,SentencesInputState oldInputState, SentencesInputState newInputState){
        for(int i = 0; i < oldInputState.inputState.size(); i++){
            State oldState = oldInputState.inputState.get(i);
            State newState = newInputState.inputState.get(i);

            if(oldState == BLANK && newState == CORRECT ||
                    oldState == INCORRECT && newState == CORRECT){
                benchmarker.incrementWordCount(splited[i]);
                benchmarker.incrementCorrectWordsCount(splited[i]);
                break;
            }

            if(oldState == BLANK && newState == INCORRECT ||
                    oldState == CORRECT && newState == INCORRECT){
                benchmarker.incrementWordCount(splited[i]);
                benchmarker.incrementErrorCount();
                break;
            }
        }
    }

    public static String getColoredString(String[] currentInput, SentencesInputState inputState){
        StringBuilder text = new StringBuilder();
        String word;
        State state;
        for(int i = 0; i< currentInput.length; i++){
            word = currentInput[i];
            state = inputState.inputState.get(i);

            switch (state){
                case CORRECT:
                    text.append("<font color='green'>").append(word).append("</font> ");
                    break;
                case INCORRECT:
                    text.append("<font color='red'>").append(word).append("</font> ");
                    break;
                case BLANK:
                    text.append("<font color='black'>").append(word).append("</font> ");
                    break;
            }
        }

        return text.toString();
    }
}
