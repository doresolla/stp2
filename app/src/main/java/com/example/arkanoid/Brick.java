package com.example.arkanoid;

import android.graphics.Color;
import android.graphics.RectF;

public class Brick {
    public RectF rect;
    public int Color;
    private boolean isVisible;
    public Brick(int row, int column, int width, int height, int color) {
        isVisible = true;
        Color = color;
        int padding = 1;
        rect = new RectF(column * width + padding,
                row * height + padding,
                column * width + width - padding,
                row * height + height - padding);
    }
    public RectF getRect(){
        return this.rect;
    }
    public int getColor(){
        return Color;
    }

    public void setInvisible(){
        isVisible = false;
    }

    public boolean getVisibility(){
        return isVisible;
    }
}
