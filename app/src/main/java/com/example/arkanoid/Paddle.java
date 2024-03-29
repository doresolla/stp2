package com.example.arkanoid;
import android.graphics.RectF;
public class Paddle {
    private RectF rect;
    private float len;
    private float height;
    private float x;
    private float y;
    private float speed;
    public final int STOPPED = 0;
    public final int LEFT = 1;
    public final int RIGHT = 2;
    private int paddleMoving = STOPPED;
    public Paddle(int screenX, int screenY){
        len = 200;
        height = 50;
        x = (screenX - len)/2;
        y = screenY - height;
        //float[] outR = new float[]{5,5,5,5,5,5,5,5};
        rect = new RectF(x, y, x+len, screenY);
        speed = 400;
    }

    public RectF getRect(){
        return rect;
    }

    public void setMovementState(int state){
        paddleMoving = state;
    }
    public int getMovementState(){
        return paddleMoving;
    }
    public void setSpeed(float s){
        speed = s;
    }
    public float getSpeed(){
        return speed;
    }

    public void update(long fps){
        if (paddleMoving == LEFT)
            x = x - speed / fps;
        else if (paddleMoving == RIGHT)
            x = x + speed / fps;
        rect.left = x;
        rect.right = x + len;
        if (rect.left < 0){
            rect.left = 0;
            rect.right = len;
        }
        else if (rect.right > MainActivity.screenX) {
            rect.left = MainActivity.screenX - len;
            rect.right = MainActivity.screenX;
        }
    }
}
