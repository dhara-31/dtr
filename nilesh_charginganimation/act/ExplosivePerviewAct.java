package com.si_charginganimation.nilesh_charginganimation.act;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;


import com.google.firebase.analytics.FirebaseAnalytics;
import com.si_charginganimation.nilesh_charginganimation.R;
import com.si_charginganimation.nilesh_charginganimation.databinding.ActExplosivePerviewBinding;
import com.si_charginganimation.nilesh_charginganimation.game.GameSurface;
import com.si_charginganimation.nilesh_charginganimation.game.MyListener;
import com.si_charginganimation.nilesh_charginganimation.model.DBetNilChrAnimopeaippOpen;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class ExplosivePerviewAct extends Activity implements MyListener {
    ActExplosivePerviewBinding b;
    public static LinearLayout ly;
    public static Activity activity;
    public static ExplosivePerviewAct activityGamePerview;
    public static TextView tvCount, tvSpeed;
    public static int count = 0;
    public static int speed = 1;
    private TextView tvDate;

    public static void finsh() {

        if (activityGamePerview != null) {
            int cou = count + 1;
            count = cou;
            activityGamePerview.tvCount.setText(cou + "");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DBetNilChrAnimopeaippOpen.doNotDisplApChBetryNilllon = false;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActExplosivePerviewBinding.inflate(getLayoutInflater());
        DBetNilChrAnimopeaippOpen.doNotDisplApChBetryNilllon = true;
        setContentView(b.getRoot());
        count = 0;
        activityGamePerview = this;
        speed = 1;
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "CurrentScreen: " + getClass().getSimpleName(), null);

        activity = this;
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        ly = findViewById(R.id.ll2);
        tvCount = findViewById(R.id.tvCount);
        tvSpeed = findViewById(R.id.tvSpeed);

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, dd LLLL");
        String dateTime = simpleDateFormat.format(calendar.getTime()).toString();
        b.tvDate.setText(dateTime);

        TextView tv = findViewById(R.id.tvPer);
        tvSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (speed == 1) {
                    speed = 2;
                    tvSpeed.setText("2x");
                    ly.removeAllViews();
                    setView();
                } else if (speed == 2) {
                    speed = 3;
                    tvSpeed.setText("3x");
                    ly.removeAllViews();
                    setView();

                } else if (speed == 3) {
                    speed = 1;
                    tvSpeed.setText("1x");
                    ly.removeAllViews();
                    setView();

                }
            }
        });

    }

    private void setView() {
        ly.addView(new GameSurface(ExplosivePerviewAct.this, activityGamePerview, speed));
    }


    @Override
    public void callback() {

        count = count + 1;
        this.tvCount.setText(count + "");


    }

    @Override
    protected void onResume() {
        super.onResume();
        setView();
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
}
