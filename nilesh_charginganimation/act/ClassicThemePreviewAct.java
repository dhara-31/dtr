package com.si_charginganimation.nilesh_charginganimation.act;

import android.app.Activity;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.si_charginganimation.nilesh_charginganimation.databinding.ActClassicPreviewBinding;
import com.si_charginganimation.nilesh_charginganimation.model.DBetNilChrAnimopeaippOpen;
import com.si_charginganimation.nilesh_charginganimation.other.ManyCAUSed;


public class ClassicThemePreviewAct extends Activity {
    ActClassicPreviewBinding b;

    int tColor;
    int barColor;
    String filePath;
    String tag;
    String fontStyle = "font/font1.ttf";

    private int wColor;
    private int iconColor;
    private int aa;
    private Handler handler;
    private Runnable r;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        b = ActClassicPreviewBinding.inflate(getLayoutInflater());
        DBetNilChrAnimopeaippOpen.doNotDisplApChBetryNilllon=true;
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
        iconColor = bundle.getInt("iconColor");
        b.tvDay.setText(ManyCAUSed.getDate2());


        b.tvTag.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        b.tvTag.setSelected(true);
        setData();
    }

    @Override
    public void onBackPressed() {
        DBetNilChrAnimopeaippOpen.doNotDisplApChBetryNilllon=false;
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DBetNilChrAnimopeaippOpen.doNotDisplApChBetryNilllon=false;
    }

    private void setData() {


        b.tvDay.setTextColor(tColor);
        b.tvTime.setTextColor(tColor);
        b.tvTag.setTextColor(tColor);
        b.progressPer.setTextColor(wColor);
        b.progressPer.setReachBarColor(wColor);
        b.view.setBackgroundColor(tColor);
        b.tvTag.setText(tag);

        setCtBarColor(barColor, iconColor);

        Glide.with(this).load(filePath).into(b.imageView);
        setCtFontStyle();
        setCtTimer();
    }

    private void setCtBarColor(int barColor, int color) {
        GradientDrawable gradientDrawable = (GradientDrawable) b.ivIcon.getBackground();
        gradientDrawable.setColor(color);


        GradientDrawable gradientDrawable2 = (GradientDrawable) b.llLife.getBackground();
        gradientDrawable2.setColor(barColor);

    }

    private void setCtFontStyle() {
        b.tvDay.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
        b.tvTime.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
        b.tvTag.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));

    }

    public void setCtTimer() {
        aa = 10;
        handler = new Handler();
        handler.postDelayed(r = new Runnable() {
            @Override
            public void run() {


                b.progressPer.setProgress(aa);

                aa = aa + 1;
                if (aa > 50) {

                } else {
                    handler.postDelayed(r, 100);
                }
            }
        }, 100);


    }

}
