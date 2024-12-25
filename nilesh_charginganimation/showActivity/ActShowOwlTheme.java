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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import androidx.annotation.Nullable;

import com.si_charginganimation.nilesh_charginganimation.databinding.ActOwlShowBinding;
import com.si_charginganimation.nilesh_charginganimation.model.BatteryInfo;
import com.si_charginganimation.nilesh_charginganimation.model.DBetNilChrAnimopeaippOpen;
import com.si_charginganimation.nilesh_charginganimation.service.ChargingCAService;
import com.si_charginganimation.nilesh_charginganimation.utils.ShCAPreference;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ActShowOwlTheme extends Activity {
    static ActOwlShowBinding b;
    private static Activity activity;
    String fontStyle = "font/font1.ttf";
    int waveColor;
    ShCAPreference shCAPreference;

    static Handler handler;
    static Runnable r;
    static float aa;
    private int cn = 0;
     private boolean update = true;

    PowerManager.WakeLock screenLock;
    static int perL;
    private boolean locked=false;
    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActOwlShowBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        DBetNilChrAnimopeaippOpen.doNotDisplApChBetryNilllon = true;

        activity = this;

        shCAPreference =new ShCAPreference(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        screenLock = ((PowerManager) getSystemService(POWER_SERVICE)).newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        screenLock.acquire();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, dd LLLL");
        String dateTime = simpleDateFormat.format(calendar.getTime()).toString();
        b.tvDate.setText(dateTime);

        setPerData();
        setTimer(100);
        b.tvPer.setText(ChargingCAService.level + "%");

        KeyguardManager myKM = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        if( myKM.inKeyguardRestrictedInputMode() ) {
            locked=true;
        } else {
            //it is not locked
            locked=false;
        }
    }

    private void setPerData() {

        fontStyle = shCAPreference.getowlFont();






        setNtFontStyle();


    }

    private void setNtFontStyle() {
        b.tvDate.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
        b.tvTime.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
        b.tvPer.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));

    }

    public void setTimer(int ll) {

        aa = 0;
        handler = new Handler();
        handler.postDelayed(r = new Runnable() {
            @Override
            public void run() {


                float progress = 1.0f - (((float) aa * 10 / 1000.0f));
                b.waveView.setWaveXAxisPositionMultiplier(progress);
                b.waveView2.setWaveXAxisPositionMultiplier(progress);
                b.waveView3.setWaveXAxisPositionMultiplier(progress);
                b.waveView4.setWaveXAxisPositionMultiplier(progress);
                b.waveView5.setWaveXAxisPositionMultiplier(progress);

                if (update) {
                    aa = aa + 0.2f;
                } else {
                    aa = aa - 0.2f;
                }

                if (aa > ll) {
                    update = false;

                }

                if (aa == 0) {
                    update = true;
                    cn = cn + 1;
                    if (cn == 10) {
                        cn = 0;
                    }

                }
                if (update) {
                    handler.postDelayed(r, 50);
                } else {
                    setAnim();
                }

            }
        }, 50);


    }
    private void setAnim() {
        Animation anim = new AlphaAnimation(0.3f, 1.0f);
        anim.setDuration(1500);
        anim.setStartOffset(10);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        b.linearLayout.startAnimation(anim);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null)
            handler.removeCallbacks(r);
        screenLock.release();
        DBetNilChrAnimopeaippOpen.doNotDisplApChBetryNilllon = false;

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


        setWaveLave(level);
    }

    private static void setWaveLave(String level) {

        perL = Integer.parseInt(level);
        b.tvPer.setText(level + "%");

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

        finish();
        DBetNilChrAnimopeaippOpen.doNotDisplApChBetryNilllon = false;

    }
}
