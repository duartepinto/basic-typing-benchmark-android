package com.tuwien.buildinginteractioninterfaces.prototype.domain.model;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

public class BenchmarkModel implements Serializable{
    private int correctChars;
    private int correctWords;
    private int errors;
    private float charsPerSec;
    private float wordsPerSec;
    private int totalWords;
    private float time;
    private int backspace;
    private int keystrokes;
    private int characters;
    private double keystrokesPerChar;
    private double minimumStringDistanceErrorRate;
    private OptionsModel options;
    private Date timestamp = Calendar.getInstance().getTime();

    public BenchmarkModel(OptionsModel options) {
        this.options = options;
    }

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

    public int getErrors() {
        return errors;
    }

    public void setErrors(int errors) {
        this.errors = errors;
    }

    public float getCharsPerSec() {
        return charsPerSec;
    }

    public void setCharsPerSec(float charsPerSec) {
        this.charsPerSec = charsPerSec;
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
    public void incrementErrors(){
        errors++;
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
        updateKeystrokesPerChar();
    }

    public int getCharacters() {
        return characters;
    }

    public void setCharacters(int characters) {
        this.characters = characters;
        updateKeystrokesPerChar();
    }

    public double getKeystrokesPerChar() {
        return keystrokesPerChar;
    }

    public void setKeystrokesPerChar(double keystrokesPerChar) {
        this.keystrokesPerChar = keystrokesPerChar;
    }

    public void updateKeystrokesPerChar(){
        keystrokesPerChar = (double) keystrokes / (double) characters;
    }

    public double getMinimumStringDistanceErrorRate(){
        return this.minimumStringDistanceErrorRate;
    }

    public void setMinimumStringDistanceErrorRate(double minimumStringDistanceErrorRate){
        this.minimumStringDistanceErrorRate = minimumStringDistanceErrorRate;
    }

    public OptionsModel getOptions() {
        return options;
    }

    public void setOptions(OptionsModel options) {
        this.options = options;
    }

    public float getWordsPerSec() {
        return wordsPerSec;
    }

    public void setWordsPerSec(float wordsPerSec) {
        this.wordsPerSec = wordsPerSec;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "BenchmarkModel{" +
                "correctChars=" + correctChars + "\n" +
                ", correctWords=" + correctWords + "\n" +
                ", errors=" + errors + "\n" +
                ", charsPerSec=" + charsPerSec + "\n" +
                ", wordsPerSec=" + wordsPerSec + "\n" +
                ", totalWords=" + totalWords + "\n" +
                ", time=" + time + "\n" +
                ", backspace=" + backspace + "\n" +
                ", keystrokes=" + keystrokes + "\n" +
                ", characters=" + characters + "\n" +
                ", keystrokesPerChar=" + keystrokesPerChar + "\n" +
                ", minimumStringDistanceErrorRate=" + minimumStringDistanceErrorRate + "\n" +
                ", options=" + options + "\n" +
                ", timestamp=" + timestamp + "\n" +
                '}';
    }
}
