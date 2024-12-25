package com.si_charginganimation.nilesh_charginganimation.game;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameThread extends Thread {

    private boolean running;
    private GameSurface gameSurface;
    private SurfaceHolder surfaceHolder;
    int speed;

    public GameThread(GameSurface gameSurface, SurfaceHolder surfaceHolder, int speed)  {
        this.gameSurface= gameSurface;
        this.surfaceHolder= surfaceHolder;
        this.speed=speed;
    }

    @Override
    public void run()  {
        long startTime = System.nanoTime();


        while(running)  {
            Canvas canvas= null;
            try {

                canvas = this.surfaceHolder.lockCanvas();


                synchronized (canvas)  {
                    this.gameSurface.update();
                    this.gameSurface.draw(canvas);
                }
            }catch(Exception e)  {

            } finally {
                if(canvas!= null)  {

                    this.surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
            long now = System.nanoTime() ;

            long waitTime=0;
            if(speed==1) {
                waitTime = (now - startTime) / 1000000;
            }else if(speed==2){
                waitTime = (now - startTime) / 100000;
            }
            else if(speed==3){
                waitTime = (now - startTime) / 50000;
            } else if(speed==4){

            }
            if(waitTime < 10)  {
                waitTime= 150;
            }


            try {

                this.sleep(waitTime);
            } catch(InterruptedException e)  {

            }
            startTime = System.nanoTime();
            System.out.print(".");
        }
    }

    public void setRunning(boolean running)  {
        this.running= running;
    }
}
