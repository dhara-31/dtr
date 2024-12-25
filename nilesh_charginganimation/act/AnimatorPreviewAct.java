package com.si_charginganimation.nilesh_charginganimation.act;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;


import com.google.firebase.analytics.FirebaseAnalytics;
import com.si_charginganimation.nilesh_charginganimation.R;
import com.si_charginganimation.nilesh_charginganimation.databinding.ActCsPreviewBinding;
import com.si_charginganimation.nilesh_charginganimation.model.DBetNilChrAnimopeaippOpen;
import com.si_charginganimation.nilesh_charginganimation.utils.ShCAPreference;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import me.itangqi.waveloadingview.WaveLoadingView;

public class AnimatorPreviewAct extends Activity {
    ActCsPreviewBinding b;
    int shapea = 1;
    String fontStylea = "font/font1.ttf";
    String clockPosa = "top";
    String seta = "Apply";
    int clockColora = 0xFFFFFFFF;
    int amplitudea;
    float borderWidtha;
    int colorBordera;
    int waveColora;
    int bgcolora;
    ShCAPreference shCAPreference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "CurrentScreen: " + getClass().getSimpleName(), null);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        DBetNilChrAnimopeaippOpen.doNotDisplApChBetryNilllon = true;
        b = ActCsPreviewBinding.inflate(getLayoutInflater());
        shCAPreference = new ShCAPreference(this);
        setContentView(b.getRoot());
        Bundle bundle = getIntent().getExtras();
        shapea = bundle.getInt("shape", 1);
        fontStylea = bundle.getString("fontStyle", "font/font1.ttf");
        clockPosa = bundle.getString("clockPos", "top");
        clockColora = bundle.getInt("clockColor", 0xFFFFFFFF);
        amplitudea = bundle.getInt("amplitude", 70);
        borderWidtha = bundle.getFloat("borderWidth", 0);
        colorBordera = bundle.getInt("colorBorder", 0);
        waveColora = bundle.getInt("waveColor", 0);
        bgcolora = bundle.getInt("bgcolor", 0);
        seta = bundle.getString("set", "Apply");

        b.waveLoadingView.setAnimDuration(2000);
        b.waveLoadingView2.setAnimDuration(2000);

        displayWaveData();

    }

    @Override
    public void onBackPressed() {
        DBetNilChrAnimopeaippOpen.doNotDisplApChBetryNilllon = false;
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        DBetNilChrAnimopeaippOpen.doNotDisplApChBetryNilllon = false;
    }

    private void displayWaveData() {
        if (shapea == 4) {
            b.cvAnim1.setVisibility(View.GONE);
            b.cvAnim2.setVisibility(View.VISIBLE);
            b.iv.setColorFilter(clockColora);

            b.view.setBackgroundColor(clockColora);
            b.tvPer3.setTextColor(clockColora);
            b.tvDate3.setTextColor(clockColora);
            b.tvTime3.setTextColor(clockColora);
            b.tvDate3.setTypeface(Typeface.createFromAsset(getAssets(), fontStylea));
            b.tvTime3.setTypeface(Typeface.createFromAsset(getAssets(), fontStylea));
            b.tvPer3.setTypeface(Typeface.createFromAsset(getAssets(), fontStylea));

            b.viewLine.setBackgroundColor(waveColora);
            b.waveLoadingView2.setShapeType(getShape(shapea));
            b.waveLoadingView2.setBorderWidth(0);
            b.waveLoadingView2.setAmplitudeRatio(amplitudea);
            b.waveLoadingView2.setWaveColor(waveColora);
            b.waveLoadingView2.setWaveBgColor(bgcolora);
            b.waveLoadingView2.setBorderColor(colorBordera);


            if (clockPosa.equals("top")) {
                ConstraintLayout constraintLayout = findViewById(R.id.cv2);
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(constraintLayout);
                constraintSet.clear(R.id.cvClock, ConstraintSet.BOTTOM);
                constraintSet.clear(R.id.cvClock, ConstraintSet.TOP);
                constraintSet.connect(R.id.cvClock, ConstraintSet.TOP, R.id.cv2, ConstraintSet.TOP, 0);
                constraintSet.applyTo(constraintLayout);
            } else if (clockPosa.equals("center")) {
                ConstraintLayout constraintLayout = findViewById(R.id.cv2);
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(constraintLayout);
                constraintSet.clear(R.id.cvClock, ConstraintSet.BOTTOM);
                constraintSet.clear(R.id.cvClock, ConstraintSet.TOP);
                constraintSet.connect(R.id.cvClock, ConstraintSet.TOP, R.id.cv2, ConstraintSet.TOP, 0);
                constraintSet.connect(R.id.cvClock, ConstraintSet.BOTTOM, R.id.cv2, ConstraintSet.BOTTOM, 0);
                constraintSet.applyTo(constraintLayout);
            } else if (clockPosa.equals("bottom")) {
                ConstraintLayout constraintLayout = findViewById(R.id.cv2);
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(constraintLayout);
                constraintSet.clear(R.id.cvClock, ConstraintSet.BOTTOM);
                constraintSet.clear(R.id.cvClock, ConstraintSet.TOP);
                constraintSet.connect(R.id.cvClock, ConstraintSet.BOTTOM, R.id.cv2, ConstraintSet.BOTTOM, 0);
                constraintSet.applyTo(constraintLayout);
            }


        } else {
            b.cvAnim2.setVisibility(View.GONE);
            b.cvAnim1.setVisibility(View.VISIBLE);
            if (shapea == 3) {
                b.cvDate1.setVisibility(View.GONE);
                b.cvDate2.setVisibility(View.VISIBLE);
            }
            b.tvDate.setTextColor(clockColora);
            b.tvDate2.setTextColor(clockColora);
            b.tvTime.setTextColor(clockColora);
            b.tvTime2.setTextColor(clockColora);
            b.tvPer2.setTextColor(clockColora);
            b.tvDate.setTypeface(Typeface.createFromAsset(getAssets(), fontStylea));
            b.tvDate2.setTypeface(Typeface.createFromAsset(getAssets(), fontStylea));
            b.tvTime2.setTypeface(Typeface.createFromAsset(getAssets(), fontStylea));
            b.tvTime.setTypeface(Typeface.createFromAsset(getAssets(), fontStylea));
            b.tvPer2.setTypeface(Typeface.createFromAsset(getAssets(), fontStylea));

            b.waveLoadingView.setShapeType(getShape(shapea));
            b.waveLoadingView.setBorderWidth(borderWidtha);
            b.waveLoadingView.setAmplitudeRatio(amplitudea);
            b.waveLoadingView.setWaveColor(waveColora);
            b.waveLoadingView.setWaveBgColor(bgcolora);
            b.waveLoadingView.setBorderColor(colorBordera);
        }

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, dd LLLL");
        String dateTime = simpleDateFormat.format(calendar.getTime()).toString();
        b.tvDate3.setText(dateTime);
        b.tvDate2.setText(dateTime);
        b.tvDate.setText(dateTime);

        boolean per = shCAPreference.getShowPercentage();
        if (!per) {
            b.tvPer2.setVisibility(View.GONE);
            b.tvPer3.setVisibility(View.GONE);
            b.iv.setVisibility(View.GONE);
            b.view.setVisibility(View.GONE);
        }
        b.tvPer2.setText("50" + "%");
        b.tvPer3.setText("50" + "%");

    }

    private WaveLoadingView.ShapeType getShape(int shape) {
        if (shape == 3) {
            return WaveLoadingView.ShapeType.TRIANGLE;
        } else if (shape == 1) {
            return WaveLoadingView.ShapeType.CIRCLE;
        } else if (shape == 2) {
            return WaveLoadingView.ShapeType.SQUARE;
        } else if (shape == 4) {
            return WaveLoadingView.ShapeType.RECTANGLE;
        } else {
            return WaveLoadingView.ShapeType.SQUARE;
        }
    }





    @Override
    protected void onResume() {
        super.onResume();

    }



    @Override
    protected void onPause() {
        super.onPause();

    }
}
