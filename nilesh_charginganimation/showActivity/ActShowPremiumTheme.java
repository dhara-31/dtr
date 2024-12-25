package com.si_charginganimation.nilesh_charginganimation.showActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.si_charginganimation.nilesh_charginganimation.databinding.ActivityPremiumShowBinding;
import com.si_charginganimation.nilesh_charginganimation.model.BatteryInfo;
import com.si_charginganimation.nilesh_charginganimation.model.DBetNilChrAnimopeaippOpen;
import com.si_charginganimation.nilesh_charginganimation.other.ManyCAUSed;
import com.si_charginganimation.nilesh_charginganimation.service.ChargingCAService;
import com.si_charginganimation.nilesh_charginganimation.utils.ShCAPreference;


public class ActShowPremiumTheme extends Activity {
    static ActivityPremiumShowBinding b;
    private static Activity activity;
    public static boolean update = false;
    int tColor;
    int barColor;
    String filePath;
    String tag;
    String fontStyle = "font/font1.ttf";
    PowerManager.WakeLock screenLock;
    private int wColor;
    ShCAPreference shCAPreference;
    static Handler handler;
    static Runnable r;
    static int aa;
    private boolean locked=false;
    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        DBetNilChrAnimopeaippOpen.doNotDisplApChBetryNilllon = true;


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        b = ActivityPremiumShowBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        screenLock = ((PowerManager) getSystemService(POWER_SERVICE)).newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        screenLock.acquire();

        shCAPreference = new ShCAPreference(this);


        b.tvDay.setText(ManyCAUSed.getDate());
        setData();

        KeyguardManager myKM = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        if( myKM.inKeyguardRestrictedInputMode() ) {
            locked=true;
        } else {
            //it is not locked
            locked=false;
        }
    }

    public void setData() {



        filePath = shCAPreference.getPtFilepath_ca();
        tColor = shCAPreference.getPtTextColor_ca();
        barColor = shCAPreference.getPtBarColor_ca();
        tag = shCAPreference.getPtTagText_ca();
        wColor = shCAPreference.getPtWaveColor_ca();
        fontStyle = shCAPreference.getPtFont();


        b.llShowdate.setBackgroundColor(barColor);

        b.tvDay.setTextColor(tColor);
        b.tvTime.setTextColor(tColor);
        b.tvPer.setTextColor(tColor);
        b.tvContent2.setTextColor(tColor);
        b.waveView.setWaveColor(wColor);
        update = false;
        setText(tag);
        Glide.with(this).load(filePath).into(b.imageView);
        setFontStyle();


        setPerAndWave(ChargingCAService.level);

        setTimer(Integer.parseInt(ChargingCAService.level));
    }


    private void setText(String tag) {

        b.tvContent2.setText(tag);
        b.tvContent2.scroller.forceFinished(true);
        b.tvContent2.scroll();

    }

    private void setFontStyle() {
        b.tvDay.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
        b.tvTime.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
        b.tvPer.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
        b.tvContent2.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DBetNilChrAnimopeaippOpen.doNotDisplApChBetryNilllon = false;


        screenLock.release();
    }

    public static void setPer(Intent intent) {
        if (b != null) {
            updateView(new BatteryInfo(intent));
        }
    }

    private static void updateView(BatteryInfo batteryInfo) {


        setPerAndWave(getLevel(batteryInfo));


    }

    public static void setPerAndWave(String level) {
        if (update) {
            b.tvPer.setText(level + "%");
            setWaveLave(level);
        }
    }

    private static void setWaveLave(String level) {
        int ll = Integer.parseInt(level);


        float progress = 1.0f - (((float) ll * 10 / 1000.0f));

        b.waveView.setWaveXAxisPositionMultiplier(progress);



    }

    public static void setTimer(int ll) {

        aa = 0;
        handler = new Handler();
        handler.postDelayed(r = new Runnable() {
            @Override
            public void run() {


                float progress = 1.0f - (((float) aa * 10 / 1000.0f));
                b.waveView.setWaveXAxisPositionMultiplier(progress);
                b.tvPer.setText(aa + "%");
                aa = aa + 1;
                if (aa > ll) {
                    update = true;
                } else {
                    handler.postDelayed(r, 100);
                }
            }
        }, 100);


    }

    public static void closed() {
        if (activity != null) {
            activity.finish();
            //activity.finishAffinity();
        }
    }

    private static String getLevel(BatteryInfo batteryInfo) {
        return String.valueOf(batteryInfo.getLevel());
    }

    @Override
    protected void onStop() {


        if(!locked) {
            activity.finish();
        }

        super.onStop();
    }

    @Override
    public void onBackPressed() {
        DBetNilChrAnimopeaippOpen.doNotDisplApChBetryNilllon = false;

        finish();
    }
}
