package com.si_charginganimation.nilesh_charginganimation.act;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.si_charginganimation.nilesh_charginganimation.R;
import com.si_charginganimation.nilesh_charginganimation.databinding.ActBtInfoBinding;
import com.si_charginganimation.nilesh_charginganimation.utils.ShCAPreference;
import com.si_charginganimation.nilesh_charginganimation.wallCAApi.NatBetsAll;

public class BtInfoAct extends Activity {
    public static ActBtInfoBinding b;
    String batteryPctTv;
    public static String chargingStatusTv;
    String healthTv;
    public static String pluggedTv;

    ShCAPreference shCAPreference;
    String technologyTv;
    String tempTv;
    String totalCapacity;
    String voltageTv;
    private int themeColor;

    public static void setData(String plugged, String chargingStatusTv) {
        if (b != null) {
            b.tvPlugged.setText(plugged);
            b.tvChargingStatus.setText(chargingStatusTv);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        b = ActBtInfoBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        shCAPreference = new ShCAPreference(this);
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "CurrentScreen: " + getClass().getSimpleName(), null);



        this.totalCapacity = getIntent().getStringExtra("totalCapacity");
        this.healthTv = getIntent().getStringExtra("health");

        FrameLayout f = findViewById(R.id.admobNativeLarge);
        CardView c = findViewById(R.id.c);
        NatBetsAll.getInstance().natVolBetsl(f, BtInfoAct.this, c, findViewById(R.id.admobNative_Banner), findViewById(R.id.nativesmallcard));



        this.batteryPctTv = getIntent().getStringExtra("batteryPct");
        this.pluggedTv = getIntent().getStringExtra("plugged");
        this.chargingStatusTv = getIntent().getStringExtra("chargingStatus");
        this.voltageTv = getIntent().getStringExtra("voltage");
        this.tempTv = getIntent().getStringExtra("temp");
        this.technologyTv = getIntent().getStringExtra("technology");


        String upperString = chargingStatusTv.substring(0, 1).toUpperCase() + chargingStatusTv.substring(1).toLowerCase();
        b.tvCapacity.setText(this.totalCapacity + "mAh");
        b.tvTxtHealth.setText(this.healthTv);
        b.tvBatteryPct.setText(this.batteryPctTv);
        b.tvPlugged.setText(this.pluggedTv);
        b.tvChargingStatus.setText(upperString);
        b.tvVoltage.setText(this.voltageTv);
        b.tvTemp.setText(this.tempTv);
        b.tvTechnology.setText(this.technologyTv);
        b.btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setThemeApp();
    }

    private void setThemeApp() {

        if (shCAPreference.getThemeType() == 1) {
            themeColor = getResources().getColor(R.color.th_1);
        } else if (shCAPreference.getThemeType() == 2) {

            themeColor = getResources().getColor(R.color.th_2);
        } else if (shCAPreference.getThemeType() == 3) {

            themeColor = getResources().getColor(R.color.th_3);
        } else if (shCAPreference.getThemeType() == 4) {
            themeColor = getResources().getColor(R.color.th_4);
        }

        b.iv1.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
        b.iv2.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
        b.iv3.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
        b.iv4.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
        b.iv5.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
        b.iv6.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
        b.iv7.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
        b.iv8.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
        b.tvCapacity.setTextColor(themeColor);
        b.tvTxtHealth.setTextColor(themeColor);
        b.tvBatteryPct.setTextColor(themeColor);
        b.tvPlugged.setTextColor(themeColor);
        b.tvChargingStatus.setTextColor(themeColor);
        b.tvVoltage.setTextColor(themeColor);
        b.tvTemp.setTextColor(themeColor);
        b.tvTechnology.setTextColor(themeColor);


    }
}
