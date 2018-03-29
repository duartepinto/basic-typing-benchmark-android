package com.tuwien.buildinginteractioninterfaces.typingbenchmark;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputFilter;

public class MockEditable implements Editable {
    private String str;

    public MockEditable(String str) {
        this.str = str;
    }

    @Override @NonNull
    public String toString() {
        return str;
    }

    @Override
    public int length() {
        return str.length();
    }

    @Override
    public char charAt(int i) {
        return str.charAt(i);
    }

    @Override
    public CharSequence subSequence(int i, int i1) {
        return str.subSequence(i, i1);
    }

    @Override
    public Editable replace(int i, int i1, CharSequence charSequence, int i2, int i3) {
        String initStr = str.substring(0,i);
        CharSequence replaceStr = charSequence.subSequence(i2, i3);
        String endStr = str.substring(i1);

        str = initStr + charSequence + endStr;
        return this;
    }

    @Override
    public Editable replace(int i, int i1, CharSequence charSequence) {
        return this;
    }

    @Override
    public Editable insert(int i, CharSequence charSequence, int i1, int i2) {
        return this;
    }

    @Override
    public Editable insert(int i, CharSequence charSequence) {
        return this;
    }

    @Override
    public Editable delete(int i, int i1) {
        return this;
    }

    @Override
    public Editable append(CharSequence charSequence) {
        return this;
    }

    @Override
    public Editable append(CharSequence charSequence, int i, int i1) {
        return this;
    }

    @Override
    public Editable append(char c) {
        return this;
    }

    @Override
    public void clear() {
    }

    @Override
    public void clearSpans() {
    }

    @Override
    public void setFilters(InputFilter[] inputFilters) {
    }

    @Override
    public InputFilter[] getFilters() {
        return new InputFilter[0];
    }

    @Override
    public void getChars(int i, int i1, char[] chars, int i2) {
    }

    @Override
    public void setSpan(Object o, int i, int i1, int i2) {
    }

    @Override
    public void removeSpan(Object o) {
    }

    @Override
    public <T> T[] getSpans(int i, int i1, Class<T> aClass) {
        return null;
    }

    @Override
    public int getSpanStart(Object o) {
        return 0;
    }

    @Override
    public int getSpanEnd(Object o) {
        return 0;
    }

    @Override
    public int getSpanFlags(Object o) {
        return 0;
    }

    @Override
    public int nextSpanTransition(int i, int i1, Class aClass) {
        return 0;
    }
}
