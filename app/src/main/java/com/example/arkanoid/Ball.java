package com.example.arkanoid;

import android.graphics.RectF;

import java.util.Random;

public class Ball {
    private RectF ball;
    float[] velocity = new float[2];
    float[] defVelocity = new float[2];
    float ballWidth = 15;
    float ballHeight = 15;

    public Ball(){
        setRandomVelocity();
        defVelocity = velocity;
        ball = new RectF();
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
    public void reverseXVelocity(){
        velocity[0] = -velocity[0];
  //      xVelocity = - xVelocity;
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
        velocity[0]= 200;
        velocity[1] = -400;

        Random generatorx = new Random();
        Random generatory = new Random();
        velocity[0] = xRange[generatorx.nextInt(3)];
        velocity[1] = xRange[generatory.nextInt(4)];


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
        ball.left = x /2;
        ball.top = y - 20;
        ball.right = x / 2 + ballWidth;
        ball.bottom = y - 20 - ballHeight;
        setDefaultVelocity();
    }

    public void setDefaultVelocity(){
        velocity[0] = 200;
        velocity[1] = -400;
    }
}
