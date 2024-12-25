package com.si_charginganimation.nilesh_charginganimation.act;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.DrawableCompat;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.si_charginganimation.nilesh_charginganimation.R;
import com.si_charginganimation.nilesh_charginganimation.databinding.ActivityExitBinding;
import com.si_charginganimation.nilesh_charginganimation.utils.ShCAPreference;
import com.si_charginganimation.nilesh_charginganimation.wallCAApi.NatBetsAll;


public class ExitAct extends Activity {
    ActivityExitBinding b;
    ShCAPreference shCAPreference;
    private int themeColor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityExitBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        shCAPreference = new ShCAPreference(this);


        FrameLayout admobNativeLarge2 = findViewById(R.id.admobNative_Banner);
        CardView cardView = findViewById(R.id.c);
        NatBetsAll.banaernatBetse(admobNativeLarge2, ExitAct.this, cardView, false);

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "CurrentScreen: " + getClass().getSimpleName(), null);

        b.btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        b.btExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                finishAffinity();
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


        Drawable unwrappedDrawable = AppCompatResources.getDrawable(this, R.drawable.dr_tab_s);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable, themeColor);
        b.btExit.setBackground(wrappedDrawable);
        b.btCancel.setTextColor(themeColor);
    }


    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }


}
