package com.si_charginganimation.nilesh_charginganimation.showActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.si_charginganimation.nilesh_charginganimation.databinding.ActivityPopularShowBinding;
import com.si_charginganimation.nilesh_charginganimation.model.BatteryInfo;
import com.si_charginganimation.nilesh_charginganimation.model.DBetNilChrAnimopeaippOpen;
import com.si_charginganimation.nilesh_charginganimation.other.ManyCAUSed;
import com.si_charginganimation.nilesh_charginganimation.service.ChargingCAService;
import com.si_charginganimation.nilesh_charginganimation.utils.ShCAPreference;


public class ActShowPopularTheme extends Activity {
    static ActivityPopularShowBinding b;
    private static Activity activity;

    int tColor;

    String filePath;
    String tag;
    String fontStyle = "font/font1.ttf";
    PowerManager.WakeLock screenLock;
    private int wColor;
    ShCAPreference shCAPreference;
   public static String perL="0";
    private boolean locked=false;
    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        DBetNilChrAnimopeaippOpen.doNotDisplApChBetryNilllon = true;

        perL="0";
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        b = ActivityPopularShowBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        screenLock = ((PowerManager) getSystemService(POWER_SERVICE)).newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        screenLock.acquire();

        shCAPreference = new ShCAPreference(this);

        b.tvTag.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        b.tvTag.setSelected(true);

        b.tvTag.setSingleLine(true);
        b.tvDay.setText(ManyCAUSed.getDate3());
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
        filePath = shCAPreference.getAtFilepath_ca();
        tColor = shCAPreference.getAtTextColor_ca();
        tag = shCAPreference.getAtTagText_ca();
        wColor = shCAPreference.getAtWaveColor_ca();
        fontStyle = shCAPreference.getAtFont();


        b.tvDay.setTextColor(tColor);
        b.tvTag.setTextColor(tColor);
        b.pPer.setColorBackground(wColor);
        b.pPer.setDuration(3000l);
        setText(tag);
        Glide.with(this).load(filePath).into(b.imageView);
        setFontStyle();

        setPerAndWave(ChargingCAService.level);

    }


    private void setText(String tag) {

        b.tvTag.setText(tag);


    }

    private void setFontStyle() {
        b.tvDay.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
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


        setPerAndWave(getLevel(batteryInfo));


    }

    public static void setPerAndWave(String level) {


        setWaveLave(level);
    }

    private static void setWaveLave(String level) {
        if(!perL.equals(level)) {

            int ll = Integer.parseInt(level);
            b.pPer.setProgress(ll);
            b.pPer.setLabelText(ll + "%");
            perL=level;
        }

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
