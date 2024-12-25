package com.si_charginganimation.nilesh_charginganimation.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import com.si_charginganimation.nilesh_charginganimation.R;
import com.si_charginganimation.nilesh_charginganimation.act.ExplosivePerviewAct;
import com.si_charginganimation.nilesh_charginganimation.showActivity.ActShowGame;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GameSurface extends SurfaceView implements SurfaceHolder.Callback {

    private GameThread gameThread;
    Bitmap scaled;
    private final List<ChibiCharacter> chibiList = new ArrayList<ChibiCharacter>();
    private final List<Explosion> explosionList = new ArrayList<Explosion>();

    private static final int MAX_STREAMS = 100;
    private int soundIdExplosion;
    private boolean soundPoolLoaded;
    private SoundPool soundPool;

    int speed=1;
    MyListener ml;


    public GameSurface(Context context, MyListener listener, int i) {
        super(context);

         this.setFocusable(true);
        this.ml = listener;

        speed=i;
        this.getHolder().addCallback(this);
         this.initSoundPool();
    }

    private void initSoundPool() {
         if (Build.VERSION.SDK_INT >= 21) {

            AudioAttributes audioAttrib = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            SoundPool.Builder builder = new SoundPool.Builder();
            builder.setAudioAttributes(audioAttrib).setMaxStreams(MAX_STREAMS);

            this.soundPool = builder.build();
        }
         else {
             this.soundPool = new SoundPool(MAX_STREAMS, AudioManager.STREAM_MUSIC, 0);
        }

         this.soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                soundPoolLoaded = true;
            }
        });


        this.soundIdExplosion = this.soundPool.load(this.getContext(),  R.raw.explosion, 1);


    }

    public void playSoundExplosion() {
        if (this.soundPoolLoaded) {
            float leftVolumn = 0.8f;
            float rightVolumn = 0.8f;

            int streamId = this.soundPool.play(this.soundIdExplosion, leftVolumn, rightVolumn, 1, 0, 1f);
        }
    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            int x = (int) event.getX();
            int y = (int) event.getY();

            Iterator<ChibiCharacter> iterator = this.chibiList.iterator();


            while (iterator.hasNext()) {
                ChibiCharacter chibi = iterator.next();
                if (chibi.getX() < x && x < chibi.getX() + chibi.getWidth()
                        && chibi.getY() < y && y < chibi.getY() + chibi.getHeight()) {

                    iterator.remove();
                    Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.ca_explosion);
                    Explosion explosion = new Explosion(this, bitmap, chibi.getX(), chibi.getY());

                    this.explosionList.add(explosion);

                }
            }


            for (ChibiCharacter chibi : chibiList) {
                int movingVectorX = x - chibi.getX();
                int movingVectorY = y - chibi.getY();
                chibi.setMovingVector(movingVectorX, movingVectorY);
            }

            return true;
        }
        return false;
    }

    public void update() {
        for (ChibiCharacter chibi : chibiList) {
            chibi.update(speed);
        }
        for (Explosion explosion : this.explosionList) {
            explosion.update();
        }

        Iterator<Explosion> iterator = this.explosionList.iterator();
        while (iterator.hasNext()) {
            Explosion explosion = iterator.next();

            if (explosion.isFinish()) {

                 iterator.remove();

                Bitmap chibiBitmap1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.ca_man);
                ChibiCharacter chibi1 = new ChibiCharacter(this, chibiBitmap1, 100, 50);
                this.chibiList.add(chibi1);
                ExplosivePerviewAct.finsh();
               ActShowGame.finsh();
               continue;
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
          for (ChibiCharacter chibi : chibiList) {
            chibi.draw(canvas);
        }

        for (Explosion explosion : this.explosionList) {
            explosion.draw(canvas);
        }

    }

     @Override
    public void surfaceCreated(SurfaceHolder holder) {

        Bitmap chibiBitmap1 = BitmapFactory.decodeResource(this.getResources(), R.drawable.ca_man);
        ChibiCharacter chibi1 = new ChibiCharacter(this, chibiBitmap1, 100, 50);


        this.chibiList.add(chibi1);


        this.gameThread = new GameThread(this, holder,speed);
        this.gameThread.setRunning(true);
        this.gameThread.start();


    }

     @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

     @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {

            this.gameThread.setRunning(false);


            try {
                this.gameThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            retry = false;
        }
    }


}

