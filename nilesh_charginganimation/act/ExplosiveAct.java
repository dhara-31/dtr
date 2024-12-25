package com.si_charginganimation.nilesh_charginganimation.act;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.drawable.DrawableCompat;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.si_charginganimation.nilesh_charginganimation.R;
import com.si_charginganimation.nilesh_charginganimation.databinding.ActExplosiveBinding;
import com.si_charginganimation.nilesh_charginganimation.game.GoChBetryNils;
import com.si_charginganimation.nilesh_charginganimation.other.ManyCAUSed;
import com.si_charginganimation.nilesh_charginganimation.service.ChargingCAService;
import com.si_charginganimation.nilesh_charginganimation.utils.ShCAPreference;
import com.si_charginganimation.nilesh_charginganimation.wallCAApi.NatBetsAll;

public class ExplosiveAct extends Activity {
    ActExplosiveBinding b;
    ShCAPreference shCAPreference;
    private int themeColor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActExplosiveBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        shCAPreference = new ShCAPreference(this);

        b.btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "CurrentScreen: " + getClass().getSimpleName(), null);

        FrameLayout admobNativeLarge2 = findViewById(R.id.admobNative_Banner);
        CardView cardView = findViewById(R.id.c);
        NatBetsAll.banaernatBetse(admobNativeLarge2, ExplosiveAct.this, cardView, false);

        b.btApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ManyCAUSed.isMyServiceRunning(ChargingCAService.class, ExplosiveAct.this)) {
                    shCAPreference.setType_ca("Game");
                    b.tvApply.setText("Applied");
                    b.btApply.setAlpha(0.4f);
                } else {
                    openServiceDialog();
                }


            }
        });

        b.btPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GoChBetryNils.getInstance().showChBetryNilster(ExplosiveAct.this, new GoChBetryNils.AChBetryNilInterface() {
                    @Override
                    public void aChBetryNilsCall() {
                        startActivity(new Intent(ExplosiveAct.this, ExplosivePerviewAct.class));
                    }
                });

            }
        });
        if (shCAPreference.getType_ca().equals("Game")) {
            b.tvApply.setText("Applied");
            b.btApply.setAlpha(0.4f);
        }

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

        b.ivApply.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
        b.ivPer.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
        b.ivEdit.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
        if (shCAPreference.getType_ca().equals("Game")) {
            b.tvApply.setText("Applied");
            b.btApply.setAlpha(0.4f);
        } else {
            b.tvApply.setText("Apply");
            b.btApply.setAlpha(1f);
        }
    }

    private void openServiceDialog() {

        AlertDialog.Builder builder;
        AlertDialog create;
        builder = new AlertDialog.Builder(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_open_over_setting
                , (ViewGroup) null);
        builder.setView(inflate);
        builder.setCancelable(true);
        create = builder.create();
        create.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        TextView btShow = inflate.findViewById(R.id.btSetting);
        Drawable unwrappedDrawable = AppCompatResources.getDrawable(this, R.drawable.dr_btn_bg2);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable, themeColor);
        btShow.setBackground(wrappedDrawable);
        TextView btCancel = inflate.findViewById(R.id.btCancel);
        btCancel.setBackground(wrappedDrawable);

        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create.dismiss();



            }
        });
        btShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create.dismiss();

                GoChBetryNils.getInstance().showChBetryNilster(ExplosiveAct.this, new GoChBetryNils.AChBetryNilInterface() {
                    @Override
                    public void aChBetryNilsCall() {
                        Intent intent = new Intent(ExplosiveAct.this, SettingAct.class);
                        startActivity(intent);
                    }
                });


            }
        });
        create.show();


    }


}
