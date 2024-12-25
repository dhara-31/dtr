package com.si_charginganimation.nilesh_charginganimation.showActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.si_charginganimation.nilesh_charginganimation.databinding.ActivityClassicShowBinding;
import com.si_charginganimation.nilesh_charginganimation.model.BatteryInfo;
import com.si_charginganimation.nilesh_charginganimation.model.DBetNilChrAnimopeaippOpen;
import com.si_charginganimation.nilesh_charginganimation.other.ManyCAUSed;
import com.si_charginganimation.nilesh_charginganimation.service.ChargingCAService;
import com.si_charginganimation.nilesh_charginganimation.utils.ShCAPreference;


public class ActShowClassicTheme extends Activity {
    static ActivityClassicShowBinding b;
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
    private int iconColor;
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
        b = ActivityClassicShowBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        screenLock = ((PowerManager) getSystemService(POWER_SERVICE)).newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        screenLock.acquire();

        shCAPreference = new ShCAPreference(this);
        update = false;

        b.tvDay.setText(ManyCAUSed.getDate2());
        setData();

    }

    public void setData() {


        filePath = shCAPreference.getCtFilepath_ca();
        tColor = shCAPreference.getCtTextColor_ca();
        barColor = shCAPreference.getCtBarColor_ca();
        tag = shCAPreference.getCtTagText_ca();
        wColor = shCAPreference.getCtWaveColor_ca();
        fontStyle = shCAPreference.getCtFont();
        iconColor = shCAPreference.getCtIconColor_ca();


        b.tvTag.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        b.tvTag.setSelected(true);

        b.tvTag.setSingleLine(true);
        b.tvTag.setText(tag);

        b.tvDay.setTextColor(tColor);
        b.tvTime.setTextColor(tColor);
        b.tvTag.setTextColor(tColor);
        b.view.setBackgroundColor(tColor);
        b.progressPer.setTextColor(wColor);
        b.progressPer.setReachBarColor(wColor);

        Glide.with(this).load(filePath).into(b.imageView);
        GradientDrawable gradientDrawable = (GradientDrawable) b.ivIcon.getBackground();
        gradientDrawable.setColor(iconColor);


        GradientDrawable gradientDrawable2 = (GradientDrawable) b.llLife.getBackground();
        gradientDrawable2.setColor(barColor);

        setFontStyle();

        setPerAndWave(ChargingCAService.level);
        setTimer(Integer.parseInt(ChargingCAService.level));
        KeyguardManager myKM = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        if( myKM.inKeyguardRestrictedInputMode() ) {
            locked=true;
        } else {
            //it is not locked
            locked=false;
        }
    }


    private void setFontStyle() {
        b.tvDay.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
        b.tvTime.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
        b.tvTag.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
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

        if (update) {
            setPerAndWave(getLevel(batteryInfo));
        }

    }

    public static void setPerAndWave(String level) {
        b.progressPer.setProgress(Integer.parseInt(level));


    }

    @Override
    protected void onPause() {

        super.onPause();

    }

    public static void setTimer(int ll) {
        aa = 0;
        handler = new Handler();
        handler.postDelayed(r = new Runnable() {
            @Override
            public void run() {


                b.progressPer.setProgress(aa);

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
         }
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

    private static String getLevel(BatteryInfo batteryInfo) {
        return String.valueOf(batteryInfo.getLevel());
    }
}
