package com.example.arkanoid;

import android.graphics.Color;
import android.graphics.RectF;

public class Brick {
    public RectF rect;
    public int rectColor;
    private boolean isVisible;
    public boolean isTouched;
    public boolean isEnhanced;
    public Brick(int row, int column, int width, int height, int color, boolean enhanced) {
        isVisible = true;
        isEnhanced = enhanced;
        rectColor = color;
        int padding = 1;
        isTouched = false;
        rect = new RectF(column * width + padding,
                row * height + padding,
                column * width + width - padding,
                row * height + height - padding);
    }
    public RectF getRect(){
        return this.rect;
    }
    public int getColor(){
        return rectColor;
    }

    public void setInvisible(){
        isVisible = false;
    }
    public void setTouched(){
        isTouched = true;
//        rectColor = Color.argb(255, 128,128,128);
        rectColor = Color.GRAY;
    }

    public boolean getVisibility(){
        return isVisible;
    }
}
