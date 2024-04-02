package com.example.arkanoid;

import android.graphics.RectF;

import java.util.Random;

public class Ball {
    private RectF ball;
    float[] velocity = new float[2];
    static float[] defVelocity;
    float ballWidth = 20;
    float ballHeight = 20;

    public Ball(int x, int y){
        setRandomVelocity();
        defVelocity = velocity;
        ball = new RectF();
        reset(x, y);
    }

    public RectF getRect(){
        return ball;
    }
    public void update(long fps){
        ball.left = ball.left + (velocity[0] / fps);
        ball.top = ball.top + (velocity[1] / fps);
        ball.right = ball.left + ballWidth;
        ball.bottom = ball.top - ballHeight;
    }

    public void setRandomVelocity(){
        float[] xRange = new float[3];
        xRange[0] = 100;
        xRange[1] = 200;
        xRange[2] = 300;
        float[] yRange = new float[4];
        yRange[0] = -200;
        yRange[1] = 200;
        yRange[2] = -400;
        yRange[3] = 400;

        Random generatorx = new Random();
        Random generatory = new Random();
        velocity[0] = xRange[generatorx.nextInt(3)];
        velocity[1] = yRange[generatory.nextInt(4)];
    }
    public void setVelocity(float[] vel){
        velocity = vel;
    }
    public float[] getVelocity(){
        return velocity;
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
        ball.left = (x - ballWidth) / 2;
        ball.top = y - 10 - 30;
        ball.right = (x + ballWidth) / 2 ;
        ball.bottom = y - 10 -  ballHeight - 30;
    }

    public void setDefaultVelocity(){
        velocity[0] = defVelocity[0];
        velocity[1] = defVelocity[1];
    }
}
