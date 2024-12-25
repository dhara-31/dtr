package com.si_charginganimation.nilesh_charginganimation.act;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.si_charginganimation.nilesh_charginganimation.databinding.ActPopularPreviewBinding;
import com.si_charginganimation.nilesh_charginganimation.model.DBetNilChrAnimopeaippOpen;


public class PopularThemePreviewAct extends Activity {
    ActPopularPreviewBinding b;

    int tColor;
    String filePath;
    String tag;
    String fontStyle = "font/font1.ttf";

    private int wColor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        b = ActPopularPreviewBinding.inflate(getLayoutInflater());
        DBetNilChrAnimopeaippOpen.doNotDisplApChBetryNilllon=true;
        setContentView(b.getRoot());
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "CurrentScreen: " + getClass().getSimpleName(), null);


        Bundle bundle = getIntent().getExtras();

        tColor = bundle.getInt("tColor");
        filePath = bundle.getString("filePath");
        filePath = bundle.getString("filePath");
        tag = bundle.getString("tagText");
        wColor = bundle.getInt("waverColor");
        fontStyle = bundle.getString("fontStyle");
        b.tvTag.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        b.tvTag.setSelected(true);

        b.tvTag.setSingleLine(true);
        //b.tvDay.setText(ManyCbaUSed.getDate3());
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
        b.tvTag.setTextColor(tColor);
        b.pPer.setColorBackground(wColor);
        setText(tag);
        Glide.with(this).load(filePath).into(b.imageView);
        setFontStyle();
    }

    private void setText(String tag) {

        b.tvTag.setText(tag);


    }

    private void setFontStyle() {
        b.tvDay.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
        b.tvTag.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));

    }

}
