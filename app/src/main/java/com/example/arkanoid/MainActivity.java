package com.example.arkanoid;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewConfiguration;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    AppView view;
    static int screenX, screenY;
    Timer timer_win;
    Timer timer_lost;
    boolean show_win = false;
    boolean show_lost = false;
    Canvas canvas;


    @Override
    public boolean supportRequestWindowFeature(int featureId) {
        return super.supportRequestWindowFeature(featureId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        view = new AppView(this);
        super.onCreate(savedInstanceState);
//        Paint paint1 = new Paint();
//        paint1.setColor(Color.BLACK);
//        timer_win.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                Toast toast = Toast.makeText(getApplicationContext(), "Победа", Toast.LENGTH_SHORT);
//                toast.show();
////                if (show_win){
////                    show_win=false;
////                    canvas.drawText("Победа!", screenX/2, screenY/2, paint1);
////                }
//            }
//        }, 0, 5000);
//
//        timer_lost.schedule(new TimerTask() {
//                                @Override
//                                public void run() {
//                                    Toast toast = Toast.makeText(getApplicationContext(), "Проигрыш", Toast.LENGTH_SHORT);
//                                    toast.show();
//                                }
//                            },        0, 11000);
        setContentView(view);
    }



    class AppView extends SurfaceView implements Runnable{
        Thread gameThread = null;
        SurfaceHolder holder;
        volatile boolean playing;
        boolean paused = true;
        Paint paint;
        long fps;
        private long timeThisFrame;

        Paddle pad;
        Ball ball;
        int score = 0;
        int lives = 3;
        Brick[] bricks = new Brick[200];
        int numBricks = 0;



        public AppView(Context context){
            super(context);
            holder = getHolder();
            paint = new Paint();
            timer_win = new Timer();
            timer_lost = new Timer();
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenX = size.x;
            screenY = size.y;
            Log.d("SCREEN X ", screenX +"");
            Log.d("SCREEN Y ", screenY+"");
            pad = new Paddle();
            ball = new Ball();
            createBricksAndRestart();

        }




        public void createBricksAndRestart() {
            show_lost = false;
            show_win = false;
     //       paused = false;

            // Put the ball back to the start
            ball.reset();
            pad.reset();

            int brickWidth = screenX / 8;
            int brickHeight = screenY / 15;

            // Build a wall of bricks
            numBricks = 0;
            int[] colors = new int[]{Color.RED, Color.YELLOW, Color.GREEN};
            for (int column = 0; column < 8; column++) {
                for (int row = 0; row < 3; row++) {
                    bricks[numBricks] = new Brick(row, column, brickWidth, brickHeight,colors[row]);
                    numBricks++;
                }
            }
            // if game over reset scores and lives
            if (lives == 0) {
                score = 0;
                lives = 3;
            }
        }
        @Override
        public void run() {
            while (playing){
                long startFrameTime = System.currentTimeMillis();
                if (!paused) update();
                draw();
                timeThisFrame = System.currentTimeMillis() - startFrameTime;
                if (timeThisFrame >= 1)
                    fps = 1000/timeThisFrame;
            }
        }

        private void draw() {
            if(holder.getSurface().isValid()){
                canvas = holder.lockCanvas();
                canvas.drawColor(Color.WHITE); //цвет фона
                paint.setColor(Color.BLUE);
                canvas.drawRect(pad.getRect(), paint); //каретка голубого цвета
                paint.setColor(Color.MAGENTA);
                canvas.drawOval(ball.getRect(), paint); //мяч желтого цвета

                // Draw the bricks if visible
                for (int i = 0; i < numBricks; i++) {
                    if (bricks[i].getVisibility()) {
                        paint.setColor(bricks[i].getColor());
                        canvas.drawRect(bricks[i].getRect(), paint);
                    }
                }

                // Choose the brush color for drawing
                paint.setColor(Color.BLACK);
                // Draw the score
                paint.setTextSize(40);
                canvas.drawText("Score: " + score + "   Lives: " + lives, 10, 50, paint);
                // Has the player cleared the screen?
                if (score == numBricks * 10) {
                    paint.setTextSize(50);
                    paint.setColor(Color.BLACK);
                    show_win = true;
                    show_lost = false;
                    canvas.drawText("Победа!", screenX/2, screenY/2, paint);

                 //   paused = true;
                }

                else if (lives <= 0){
                    paint.setTextSize(50);
                    paint.setColor(Color.BLACK);
                    canvas.drawText("Проигрыш", screenX/2, screenY/2, paint);
                  //  paused = true;
                    show_lost = true;
                    show_win = false;
                }
                // Draw everything to the screen
                holder.unlockCanvasAndPost(canvas);
            }
        }

        @SuppressLint("ClickableViewAccessibility")

        private float mDownX;
        private float mDownY;
        private final float SCROLL_THRESHOLD = 10;
        public boolean isOnClick;
        @Override
        public boolean onTouchEvent(MotionEvent ev ) {
            switch (ev.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    handler.postDelayed(mLongPressed, ViewConfiguration.getLongPressTimeout());
                    mDownX = ev.getX();
                    mDownY = ev.getY();
                    isOnClick = true;

                    Log.d("Debug: ", "Action was DOWN");

                    paused = false;
                //    playing = true;
                    if(ev.getX() > screenX / 2)
                        pad.setMovementState(pad.RIGHT);
                    else
                        pad.setMovementState(pad.LEFT);
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    handler.removeCallbacks(mLongPressed);
                    if (isOnClick) {
                        Log.d("event", "onClick ");
                        pad.setMovementState(pad.STOPPED);
                    }
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (isOnClick && (Math.abs(mDownX - ev.getX()) > SCROLL_THRESHOLD || Math.abs(mDownY - ev.getY()) > SCROLL_THRESHOLD)) {
                        Log.d("event", "movement detected");
                        isOnClick = false;
                    }
                    break;
                default:
                    break;
            }
            return true;
        }
        final Handler handler = new Handler();
        Runnable mLongPressed = new Runnable() {
            public void run() {
                Log.i("", "Long press!");
                pad.setSpeed(700);
            }
        };

        private void update() {
            pad.update(fps);
            ball.update(fps);
            float[] new_velocity = ball.getVelocity();
            float[] old_velocity = ball.getVelocity();

            //столкновение с блоками
            for (int i = 0; i < numBricks; i++) {
                if (bricks[i].getVisibility()) {
                    if (RectF.intersects(bricks[i].getRect(), ball.getRect())) {
                        bricks[i].setInvisible();
                        new_velocity[1] = -new_velocity[1];
                        score = score + 10;
                    }
                }
            }

            //столкновение мяча и каретки
            if (RectF.intersects(pad.getRect(), ball.getRect())) {
                if(pad.getMovementState() == pad.LEFT){
                    new_velocity[0] = old_velocity[0] - pad.getSpeed() / 2;
                }
                else if(pad.getMovementState() == pad.RIGHT){
                    new_velocity[0] = old_velocity[0] + pad.getSpeed() / 2;
                }
                new_velocity[1] = -new_velocity[1];
                ball.moveY(pad.getRect().top - 2);
            }


            //мяч коснулся нижней стороны
            if (ball.getRect().bottom > screenY) {
                lives -= 1;
                //сброс состояния мяча
                ball.reset();
                paint.setTextSize(40);
//                paint.setColor(Color.BLACK);
//                canvas.drawText("Score: " + score + "   Lives: " + lives, 10, 50, paint);
                new_velocity[1] = -new_velocity[1];
                ball.moveY(screenY - 2);

                // проиграл ли пользователь
                if (lives <= 0) {
                    paused = true;
                    show_lost = true;
                    createBricksAndRestart();
                }

            }

            // мяч ударился о потолок
            if (ball.getRect().top <= 0) {
            //    Log.d("Top",ball.getRect().top + " " + ball.getRect().left + " " + ball.getRect().bottom + " " + ball.getRect().right);
                new_velocity[1] = -new_velocity[1];
                ball.moveY(ball.radius + 2);
               // ball.moveY(12);
            }

            // мяч ударился о левую стенку
            if (ball.getRect().left < 0) {
                Log.d("Left",ball.getRect().top + " " + ball.getRect().left + " " + ball.getRect().bottom + " " + ball.getRect().right);
  //              ball.setDefaultVelocity();
                new_velocity[0] = -new_velocity[0];
                ball.moveX(2);
            }
            // мяч ударился о правую стенку
            if (ball.getRect().right > screenX - 10) {
                Log.d("Right",ball.getRect().top + " " + ball.getRect().left + " " + ball.getRect().bottom + " " + ball.getRect().right);
                new_velocity[0] = -new_velocity[0];
                ball.moveX(screenX - 42);
            }
            // все блоки удалены
            if (score == numBricks * 10) {
                paused = true;
                createBricksAndRestart();
            }
            //каретка достигла правого края экрана
            if (pad.getRect().right > screenX - 10){
                pad.setMovementState(pad.LEFT);
                pad.setMovementState(pad.STOPPED);
            }
            //каретка достигла левого края экрана
            if (pad.getRect().left < 10){
                pad.setMovementState(pad.RIGHT);
                pad.setMovementState(pad.STOPPED);
            }
            //изменение направления движения после столкновения мяча с кареткой
            ball.setVelocity(new_velocity);
        }
        public void pause(){
            playing = false;
            try{
                gameThread.join();
            } catch (InterruptedException e) {
                Log.e("Error: ", "joining thread");
            }
        }
        public void resume(){
            playing = true;
          //  paused = false;
//            paint.setColor(Color.WHITE);
//            if (show_win){
//                canvas.drawText("Победа", screenX/2, screenY/2, paint);
//                show_win = false;
//            }
//            else if (show_lost){
//                canvas.drawText("Проигрыш!", screenX/2, screenY/2, paint);
//                show_lost = false;
//            }
            gameThread = new Thread(this);
            gameThread.start();
        }
    }
    @Override
    protected void onResume(){
        super.onResume();
        view.resume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        view.pause();
    }
}