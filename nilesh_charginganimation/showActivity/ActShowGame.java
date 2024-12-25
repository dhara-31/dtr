package com.si_charginganimation.nilesh_charginganimation.showActivity;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;


import com.si_charginganimation.nilesh_charginganimation.R;
import com.si_charginganimation.nilesh_charginganimation.game.GameSurface;
import com.si_charginganimation.nilesh_charginganimation.game.MyListener;
import com.si_charginganimation.nilesh_charginganimation.model.BatteryInfo;
import com.si_charginganimation.nilesh_charginganimation.model.DBetNilChrAnimopeaippOpen;
import com.si_charginganimation.nilesh_charginganimation.service.ChargingCAService;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class ActShowGame extends Activity implements MyListener {
    public static LinearLayout ly;
    public static Activity activity;
    public static ActShowGame activityGamePerview;
    public static TextView tvCount, tvSpeed;
    public static int count = 0;
    public static int speed = 1;
    public static TextView tvPer;
    private TextView tvDate;

    public static void finsh() {

        if(activityGamePerview!=null) {
            int cou = count + 1;
            count = cou;

            tvCount.setText(cou + "");
        }

    }
    private boolean locked=false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_explosive_show
        );
        count = 0;
        activityGamePerview = this;

        DBetNilChrAnimopeaippOpen.doNotDisplApChBetryNilllon = true;
        activity = this;
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        ly = findViewById(R.id.ll2);
        tvCount = findViewById(R.id.tvCount);
        tvSpeed = findViewById(R.id.tvSpeed);
        tvPer = findViewById(R.id.tvPer);
        tvDate = findViewById(R.id.tvDate);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, dd LLLL");
        String dateTime = simpleDateFormat.format(calendar.getTime()).toString();
        tvDate.setText(dateTime);
         tvSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (speed == 1) {
                    speed = 2;
                    tvSpeed.setText("2x");
                    ly.removeAllViews();
                    ly.addView(new GameSurface(ActShowGame.this, activityGamePerview, 2));
                } else if (speed == 2) {
                    speed = 3;
                    tvSpeed.setText("3x");
                    ly.removeAllViews();
                    ly.addView(new GameSurface(ActShowGame.this, activityGamePerview, 3));
                } else if (speed == 3) {
                    speed = 1;
                    tvSpeed.setText("1x");
                    ly.removeAllViews();
                    ly.addView(new GameSurface(ActShowGame.this, activityGamePerview, 1));
                }
            }
        });
        tvPer.setText(ChargingCAService.level + "%");
         KeyguardManager myKM = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        if( myKM.inKeyguardRestrictedInputMode() ) {
            locked=true;
        } else {
            //it is not locked
            locked=false;
        }
    }


    @Override
    public void callback() {

        count = count + 1;
        this.tvCount.setText(count + "");


    }

    @Override
    protected void onResume() {
        super.onResume();
        ly.addView(new GameSurface(ActShowGame.this, activityGamePerview, 1));
    }

    @Override
    protected void onPause() {
        super.onPause();
        ly.removeAllViews();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        DBetNilChrAnimopeaippOpen.doNotDisplApChBetryNilllon = false;
    }

    public static void closed() {
        if (activity != null) {
            activity.finish();

        }
    }

    public static void setPer(Intent intent) {
        if (tvPer != null) {
            updateView(new BatteryInfo(intent));
        }
    }

    private static void updateView(BatteryInfo batteryInfo) {


        tvPer.setText(getLevel(batteryInfo) + "%");


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
