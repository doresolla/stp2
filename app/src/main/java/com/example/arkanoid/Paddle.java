package com.example.arkanoid;
import android.graphics.RectF;
public class Paddle {
    private RectF rect;
    private float len;
    public float height = 50 ;
    private float x;
    private float speed;
    public final int STOPPED = 0;
    public final int LEFT = 1;
    public final int RIGHT = 2;
    private int paddleMoving = STOPPED;
    public Paddle(){
        len = 300;
        x = (MainActivity.screenX - len)/2;
        rect = new RectF(x,  MainActivity.screenY - height, x+len,  MainActivity.screenY );
        speed = 500;
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

    public void reset(){
        x = (MainActivity.screenX - len) / 2;
        rect.left = x;
        rect.right = x + len;

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
