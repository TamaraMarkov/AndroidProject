package com.example.mathematics;

import androidx.annotation.ColorInt;

public  class Slice {
    @ColorInt
    private int color;



    public Slice(@ColorInt int color) {
        this.color = color;

    }

    @ColorInt
    public int getColor() {
        return color;
    }

    public void setColor(int selectedColor) {
        color=selectedColor;
    }
}
