package com.example.arkanoid;

import android.graphics.RectF;

import java.util.Random;

public class Ball {
    private RectF ball;
    float[] velocity = new float[2];
    static float[] defVelocity;
    float radius = 30;


    public Ball(){
        setRandomVelocity();
        defVelocity = velocity;
        ball = new RectF();
        reset();
    }

    public RectF getRect(){
        return ball;
    }
    public void update(long fps){
        ball.left = ball.left + (velocity[0] / fps);
        ball.top = ball.top + (velocity[1] / fps);
        ball.right = ball.left + radius;
        ball.bottom = ball.top - radius;
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
        velocity[0] = 200;
        velocity[1]=-400;

    }
    public void setVelocity(float[] vel){
        velocity = vel;
    }
    public float[] getVelocity(){
        return velocity;
    }

    public void moveY(float y){
        ball.bottom = y;
        ball.top = y - radius;
    }

    public void moveX(float x){
        ball.left = x;
        ball.right = x + radius;
    }

    public  void reset(){
        ball.left = (MainActivity.screenX - radius) / 2;
        Paddle pad = new Paddle();
       ball.top = MainActivity.screenY - pad.height - radius -1;
       // ball.top = MainActivity.screenY - 10 - radius;
        ball.right = (MainActivity.screenX + radius) / 2 ;
        ball.bottom = MainActivity.screenY - pad.height - 1;
     //   ball.bottom = MainActivity.screenY - radius - 30 - 10;
    }

    public void setDefaultVelocity(){
        velocity[0] = defVelocity[0];
        velocity[1] = defVelocity[1];
    }
}
