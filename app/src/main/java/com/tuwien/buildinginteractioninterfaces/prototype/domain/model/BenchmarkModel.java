package com.tuwien.buildinginteractioninterfaces.prototype.domain.model;

/**
 * Created by duarte on 18-12-2017.
 */

public class BenchmarkModel {
    private int correctChars;
    private int correctWords;
    private int failedWords;
    private float velocity;

    public int getCorrectChars() {
        return correctChars;
    }

    public void setCorrectChars(int correctChars) {
        this.correctChars = correctChars;
    }

    public int getCorrectWords() {
        return correctWords;
    }

    public void setCorrectWords(int correctWords) {
        this.correctWords = correctWords;
    }

    public int getFailedWords() {
        return failedWords;
    }

    public void setFailedWords(int failedWords) {
        this.failedWords = failedWords;
    }

    public float getVelocity() {
        return velocity;
    }

    public void setVelocity(float velocity) {
        this.velocity = velocity;
    }

    public void incrementCorrectChars(){
        correctChars++;
    }
    public void incrementCorrectChars(int numChars){
        correctChars += numChars;
    }
    public void incrementCorrectWords(){
        correctWords++;
    }
    public void incrementFailedWords(){
        failedWords++;
    }
}
