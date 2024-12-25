package com.si_charginganimation.nilesh_charginganimation.act;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.si_charginganimation.nilesh_charginganimation.R;
import com.si_charginganimation.nilesh_charginganimation.databinding.ActNewThemePerviewBinding;
import com.si_charginganimation.nilesh_charginganimation.model.DBetNilChrAnimopeaippOpen;
import com.si_charginganimation.nilesh_charginganimation.utils.ShCAPreference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class NewThemeEditPerview extends Activity {
    ActNewThemePerviewBinding b;
    String fontStyle = "font/font1.ttf";
    int waveColor;
    ShCAPreference shCAPreference;
    int walDr = R.drawable.new_wal1;
    static Handler handler;
    static Runnable r;
    static float aa;
    private int cn = 0;
    private int themeColor, colorType;
    private boolean update = true;
    private ArrayList<String> colorMultiList = new ArrayList<>();

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DBetNilChrAnimopeaippOpen.doNotDisplApChBetryNilllon = false;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActNewThemePerviewBinding.inflate(getLayoutInflater());
        DBetNilChrAnimopeaippOpen.doNotDisplApChBetryNilllon = true;
        setContentView(b.getRoot());

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "CurrentScreen: " + getClass().getSimpleName(), null);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, dd LLLL");
        String dateTime = simpleDateFormat.format(calendar.getTime()).toString();
        b.tvDate.setText(dateTime);
        b.tvDate2.setText(dateTime);

        Bundle bundle = getIntent().getExtras();

        waveColor = bundle.getInt("waveColor");
        walDr = bundle.getInt("wal");
        fontStyle = bundle.getString("fontStyle");
        colorType = bundle.getInt("colorType");

        setPerData();
        setTimer(50);
    }

    private void setPerData() {

        b.waveView.setWaveColor(waveColor);
        b.imageView.setImageDrawable(getResources().getDrawable(walDr));
        colorMultiList = NewThemeEditAct.colorMultiList;
        setNtFontStyle();
        setClockPos();

    }

    private void setClockPos() {
        if (walDr == R.drawable.new_wal3) {
            b.cvDate1.setVisibility(View.GONE);
            b.tvPer.setVisibility(View.GONE);
            b.cvDate2.setVisibility(View.VISIBLE);
            b.tvPer2.setVisibility(View.VISIBLE);
            b.cardView.setCardBackgroundColor(getResources().getColor(R.color.owl_theme));
        } else {
            b.cvDate2.setVisibility(View.GONE);
            b.tvPer2.setVisibility(View.GONE);
            b.cvDate1.setVisibility(View.VISIBLE);
            b.tvPer.setVisibility(View.VISIBLE);
            b.cardView.setCardBackgroundColor(getResources().getColor(R.color.black));
        }


    }

    private void setNtFontStyle() {
        b.tvDate.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
        b.tvTime.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
        b.tvPer.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
        b.tvDate2.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
        b.tvTime2.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
        b.tvPer2.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
    }

    public void setTimer(int ll) {

        aa = 0;
        handler = new Handler();
        handler.postDelayed(r = new Runnable() {
            @Override
            public void run() {


                float progress = 1.0f - (((float) aa * 10 / 1000.0f));
                b.waveView.setWaveXAxisPositionMultiplier(progress);

                if (update) {
                    aa = aa + 0.2f;
                } else {
                    aa = aa - 0.2f;
                }
                if (aa > ll) {
                    update = false;

                }

                if (aa < 1f) {
                    update = true;

                    if (!colorMultiList.isEmpty()) {
                        if (colorType == 2) {
                            cn = cn + 1;
                            if (cn >= colorMultiList.size()) {
                                cn = 0;
                            }


                            if (colorMultiList.size() > 0) {
                                b.waveView.setWaveColor(Integer.parseInt(colorMultiList.get(cn)));
                            }
                        }
                    }




                }
                handler.postDelayed(r, 50);

            }
        }, 50);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DBetNilChrAnimopeaippOpen.doNotDisplApChBetryNilllon = false;
        if (handler != null)
            handler.removeCallbacks(r);
    }

}
