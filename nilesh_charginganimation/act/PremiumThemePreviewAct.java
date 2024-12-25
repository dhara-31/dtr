package com.si_charginganimation.nilesh_charginganimation.act;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.si_charginganimation.nilesh_charginganimation.databinding.ActPremiumPreviewBinding;
import com.si_charginganimation.nilesh_charginganimation.model.DBetNilChrAnimopeaippOpen;
import com.si_charginganimation.nilesh_charginganimation.other.ManyCAUSed;

public class PremiumThemePreviewAct extends Activity {
    ActPremiumPreviewBinding b;

    int tColor;
    int barColor;
    String filePath;
    String tag;
    String fontStyle = "font/font1.ttf";

    private int wColor;
    private int aa;
    private Handler handler;
    private Runnable r;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DBetNilChrAnimopeaippOpen.doNotDisplApChBetryNilllon = false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DBetNilChrAnimopeaippOpen.doNotDisplApChBetryNilllon=false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActPremiumPreviewBinding.inflate(getLayoutInflater());

        DBetNilChrAnimopeaippOpen.doNotDisplApChBetryNilllon = true;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        setContentView(b.getRoot());

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "CurrentScreen: " + getClass().getSimpleName(), null);

        Bundle bundle = getIntent().getExtras();

        tColor = bundle.getInt("tColor");
        barColor = bundle.getInt("barColor");
        filePath = bundle.getString("filePath");
        filePath = bundle.getString("filePath");
        tag = bundle.getString("tagText");
        wColor = bundle.getInt("waverColor");
        fontStyle = bundle.getString("fontStyle");

        float progress = 1.0f - (((float) 500 / 1000.0f));
        b.waveView.setWaveXAxisPositionMultiplier(progress);
        b.tvDay.setText(ManyCAUSed.getDate());
        setPtData();

    }

    private void setPtData() {
        b.llShowdate.setBackgroundColor(barColor);

        b.tvDay.setTextColor(tColor);
        b.tvTime.setTextColor(tColor);
        b.tvPer.setTextColor(tColor);
        b.tvContent2.setTextColor(tColor);
        b.waveView.setWaveColor(wColor);
        setPtText(tag);
        Glide.with(this).load(filePath).into(b.imageView);
        setPtFontStyle();
        setPtTimer();
    }

    private void setPtText(String tag) {

        b.tvContent2.setText(tag);
        b.tvContent2.scroller.forceFinished(true);
        b.tvContent2.scroll();

    }

    private void setPtFontStyle() {
        b.tvDay.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
        b.tvTime.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
        b.tvPer.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
        b.tvContent2.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
    }

    public void setPtTimer() {

        aa = 10;
        handler = new Handler();
        handler.postDelayed(r = new Runnable() {
            @Override
            public void run() {


                float progress = 1.0f - (((float) aa * 10 / 1000.0f));
                b.waveView.setWaveXAxisPositionMultiplier(progress);
                b.tvPer.setText(aa + "%");
                aa = aa + 1;
                if (aa > 50) {

                } else {
                    handler.postDelayed(r, 100);
                }
            }
        }, 100);


    }
}
