package com.tuwien.buildinginteractioninterfaces.prototype.domain.model;

import java.io.Serializable;

import kotlin.NotImplementedError;

public class BenchmarkModel implements Serializable{
    private int correctChars;
    private int correctWords;
    private int failedWords;
    private float velocity;
    private int totalWords;
    private float time;
    private int backspace;
    private int keystrokes;
    private int characters;
    private double minimumStringDistanceError;
    private OptionsModel optionsModel;

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

    public int getTotalWords() {
        return totalWords;
    }

    public void setTotalWords(int totalWords) {
        this.totalWords = totalWords;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public int getBackspace() {
        return backspace;
    }

    public void setBackspace(int backspace) {
        this.backspace = backspace;
    }

    public int getKeystrokes() {
        return keystrokes;
    }

    public void setKeystrokes(int keystrokes) {
        this.keystrokes = keystrokes;
    }

    public int getCharacters() {
        return characters;
    }

    public void setCharacters(int characters) {
        this.characters = characters;
    }

    public double getKeystrokesPerChar(){
        return (double) keystrokes / (double) characters;
    }

    public double getMinimumStringDistanceError(){
        return this.minimumStringDistanceError;
    }

    public void setMinimumStringDistanceError(double minimumStringDistanceError){
        this.minimumStringDistanceError = minimumStringDistanceError;
    }

    public OptionsModel getOptionsModel() {
        return optionsModel;
    }

    public void setOptionsModel(OptionsModel optionsModel) {
        this.optionsModel = optionsModel;
    }
}
