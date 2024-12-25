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

import com.si_charginganimation.nilesh_charginganimation.R;
import com.si_charginganimation.nilesh_charginganimation.act.NewThemeEditAct;
import com.si_charginganimation.nilesh_charginganimation.databinding.ActNewThemeShowBinding;
import com.si_charginganimation.nilesh_charginganimation.model.BatteryInfo;
import com.si_charginganimation.nilesh_charginganimation.model.DBetNilChrAnimopeaippOpen;
import com.si_charginganimation.nilesh_charginganimation.service.ChargingCAService;
import com.si_charginganimation.nilesh_charginganimation.utils.ShCAPreference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ActShowNewTheme extends Activity {
    static ActNewThemeShowBinding b;
    private static Activity activity;
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
    PowerManager.WakeLock screenLock;
    static int perL;
    private boolean locked=false;
    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActNewThemeShowBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        activity = this;


        DBetNilChrAnimopeaippOpen.doNotDisplApChBetryNilllon = true;
        shCAPreference =new ShCAPreference(this);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        screenLock = ((PowerManager) getSystemService(POWER_SERVICE)).newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        screenLock.acquire();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, dd LLLL");
        String dateTime = simpleDateFormat.format(calendar.getTime()).toString();
        b.tvDate.setText(dateTime);
        b.tvDate2.setText(dateTime);

        setPerData();
        setTimer(50);
        b.tvPer.setText(ChargingCAService.level + "%");
        b.tvPer2.setText(ChargingCAService.level + "%");

        KeyguardManager myKM = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        if( myKM.inKeyguardRestrictedInputMode() ) {
            locked=true;
        } else {
            //it is not locked
            locked=false;
        }
    }

    private void setPerData() {
        walDr = shCAPreference.getNtTheme_ca();
        fontStyle = shCAPreference.getNtFont_ca();
        waveColor = shCAPreference.getNtWColor();
        colorType = shCAPreference.getNtColorType_ca();
        colorMultiList = shCAPreference.getColorList();

        b.waveView.setWaveColor(waveColor);
        b.imageView.setImageDrawable(getResources().getDrawable(walDr));
        colorMultiList = NewThemeEditAct.colorMultiList;
        setNtFontStyle();
        setClockPos();

    }
    private void setClockPos() {
        if(walDr==R.drawable.new_wal3){
            b.cvDate1.setVisibility(View.GONE);
            b.tvPer.setVisibility(View.GONE);
            b.cvDate2.setVisibility(View.VISIBLE);
            b.tvPer2.setVisibility(View.VISIBLE);
            b.cardView.setCardBackgroundColor(getResources().getColor(R.color.owl_theme));
        }else {
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
        perL = ll;
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
                if (aa > perL) {
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
        b.tvPer2.setText(level + "%");

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
