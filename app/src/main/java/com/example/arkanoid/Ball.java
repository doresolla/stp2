package com.example.arkanoid;

import android.graphics.RectF;

import java.util.Random;

public class Ball {
    private RectF ball;
    float xVelocity;
    float yVelocity;
    float ballWidth = 15;
    float ballHeight = 15;

    public Ball(){
        xVelocity = 200;
        yVelocity = -400;
        ball = new RectF();
    }

    public RectF getRect(){
        return ball;
    }
    public void update(long fps){
        ball.left = ball.left + (xVelocity / fps);
        ball.top = ball.top + (yVelocity / fps);
        ball.right = ball.left + ballWidth;
        ball.bottom = ball.top - ballHeight;
    }
    public void reverseXVelocity(){
        xVelocity = - xVelocity;
    }

    public void reverseYVelocity(){
        yVelocity = - yVelocity;
    }
    public void setRandomXVelocity(){
        Random generator = new Random();
        int answer = generator.nextInt(2);
        if (answer == 0)
            reverseXVelocity();
    }

    public void moveY(float y){
        ball.bottom = y;
        ball.top = y - ballHeight;
    }

    public void moveX(float x){
        ball.left = x;
        ball.right = x + ballWidth;
    }

    public  void reset(int x, int y){
        ball.left = x /2;
        ball.top = y - 20;
        ball.right = x / 2 + ballWidth;
        ball.bottom = y - 20 - ballHeight;
    }
}
