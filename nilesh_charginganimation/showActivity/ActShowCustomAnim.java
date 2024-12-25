package com.si_charginganimation.nilesh_charginganimation.showActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;


import com.si_charginganimation.nilesh_charginganimation.R;
import com.si_charginganimation.nilesh_charginganimation.databinding.ActivityShowCsBinding;
import com.si_charginganimation.nilesh_charginganimation.model.BatteryInfo;
import com.si_charginganimation.nilesh_charginganimation.model.DBetNilChrAnimopeaippOpen;
import com.si_charginganimation.nilesh_charginganimation.service.ChargingCAService;
import com.si_charginganimation.nilesh_charginganimation.utils.ShCAPreference;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import me.itangqi.waveloadingview.WaveLoadingView;


public class ActShowCustomAnim extends Activity {
    public static ActivityShowCsBinding b;
    static Activity activity;
    int clicka = 2;
    private long lastTouchTime = 0;
    private long currentTouchTime = 0;
    int shapea = 1;
    String fontStylea = "font/font1.ttf";
    String clockPosa = "top";
    int clockColora = 0xFFFFFFFF;
    int amplitudea;
    float borderWidtha;
    int colorBordera;
    int waveColora;
    int bgcolora;
    PowerManager.WakeLock screenLock;
    private boolean locked = false;
    ShCAPreference shCAPreference;

    public static void setPer(Intent intent) {
        if (b != null) {
            updateView(new BatteryInfo(intent));
        } else {

        }
    }

    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DBetNilChrAnimopeaippOpen.doNotDisplApChBetryNilllon = true;
        b = ActivityShowCsBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        shCAPreference = new ShCAPreference(this);
        activity = this;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        screenLock = ((PowerManager) getSystemService(POWER_SERVICE)).newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        screenLock.acquire();

        DBetNilChrAnimopeaippOpen.doNotDisplApChBetryNilllon = true;

        b.cvMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicka == 1) {
                    onBackPressed();
                } else {
                    lastTouchTime = currentTouchTime;
                    currentTouchTime = System.currentTimeMillis();

                    if (currentTouchTime - lastTouchTime < 250) {

                        lastTouchTime = 0;
                        currentTouchTime = 0;
                        onBackPressed();
                    }
                }
            }
        });


        setWaveData();
        KeyguardManager myKM = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        if (myKM.inKeyguardRestrictedInputMode()) {
            locked = true;
        } else {
            //it is not locked
            locked = false;
        }
    }


    private void setWaveData() {
        shapea = shCAPreference.getShapeType_ca();
        clockColora = shCAPreference.getClockColor_ca();
        clockPosa = shCAPreference.getClockPos_ca();
        amplitudea = shCAPreference.getAmplitude_ca();
        borderWidtha = shCAPreference.getBorderWidth_ca();
        bgcolora = shCAPreference.getBgColor_ca();
        colorBordera = shCAPreference.getcolorBorder();
        fontStylea = shCAPreference.getFontStyle_ca();
        waveColora = shCAPreference.getWaveColor_ca();
        if (shapea == 4) {
            b.cvAnim1.setVisibility(View.GONE);
            b.cvAnim2.setVisibility(View.VISIBLE);

            b.tvPer3.setTextColor(clockColora);
            b.tvDate3.setTextColor(clockColora);
            b.tvTime3.setTextColor(clockColora);
            b.view.setBackgroundColor(clockColora);
            b.iv.setColorFilter(clockColora);
            b.tvDate3.setTypeface(Typeface.createFromAsset(getAssets(), fontStylea));
            b.tvTime3.setTypeface(Typeface.createFromAsset(getAssets(), fontStylea));
            b.tvPer3.setTypeface(Typeface.createFromAsset(getAssets(), fontStylea));
            b.waveLoadingView2.setAnimDuration(2000);
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
            b.waveLoadingView.setAnimDuration(2000);
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
        b.tvPer3.setText(ChargingCAService.level + "%");
        b.tvPer2.setText(ChargingCAService.level + "%");
        setWaveLave(ChargingCAService.level);
        clicka = shCAPreference.getClosed_ca();

        boolean per = shCAPreference.getShowPercentage();

        if (!per) {
            b.tvPer3.setVisibility(View.GONE);
            b.iv.setVisibility(View.GONE);
            b.view.setVisibility(View.GONE);
            b.tvPer2.setVisibility(View.GONE);
        }
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

    private static void updateView(BatteryInfo batteryInfo) {


        b.tvPer3.setText(getLevel(batteryInfo) + "%");
        b.tvPer2.setText(getLevel(batteryInfo) + "%");

        setWaveLave(getLevel(batteryInfo));


    }

    private static void setWaveLave(String level) {
        int ll = Integer.parseInt(level);
        if (ll > 90) {
            b.waveLoadingView.setProgressValue(90);
            b.waveLoadingView2.setProgressValue(90);
        } else if (ll < 11) {
            b.waveLoadingView.setProgressValue(10);
            b.waveLoadingView2.setProgressValue(10);
        } else {
            b.waveLoadingView.setProgressValue(ll);
            b.waveLoadingView2.setProgressValue(ll);
        }
    }

    private static String getLevel(BatteryInfo batteryInfo) {
        return String.valueOf(batteryInfo.getLevel());
    }

    public static void closed() {
        if (activity != null) {
            activity.finish();
            //activity.finishAffinity();
        }
    }

    @Override
    protected void onStop() {

        if (!locked) {
            activity.finish();
        }

        super.onStop();
    }

    @Override
    public void onBackPressed() {

        finish();

        DBetNilChrAnimopeaippOpen.doNotDisplApChBetryNilllon = false;
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        DBetNilChrAnimopeaippOpen.doNotDisplApChBetryNilllon = false;

        screenLock.release();
    }

}
