package com.si_charginganimation.nilesh_charginganimation.showActivity;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;


import com.si_charginganimation.nilesh_charginganimation.R;
import com.si_charginganimation.nilesh_charginganimation.databinding.ActivityFullWarnignBinding;
import com.si_charginganimation.nilesh_charginganimation.model.DBetNilChrAnimopeaippOpen;
import com.si_charginganimation.nilesh_charginganimation.utils.ShCAPreference;

import java.io.IOException;

public class ActFullWarning extends Activity {
    ActivityFullWarnignBinding b;
    public static Activity activity = null;
    private String type;
    MediaPlayer mediaPlayer;
    ShCAPreference shCAPreference;
    Handler handler;
    Runnable runnable;
    private int themeColor;
    private boolean locked = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        b = ActivityFullWarnignBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        DBetNilChrAnimopeaippOpen.doNotDisplApChBetryNilllon = true;
        shCAPreference = new ShCAPreference(this);
        mediaPlayer = new MediaPlayer();
        activity = this;
        Bundle bundle = getIntent().getExtras();
        type = bundle.getString("type");

        setLay();

        b.btClosed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        b.btClosed2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setThemeApp();
        setPlayer();
        KeyguardManager myKM = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        if (myKM.inKeyguardRestrictedInputMode()) {
            locked = true;
        } else {
             locked = false;
        }
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
        Drawable unwrappedDrawable = AppCompatResources.getDrawable(this, R.drawable.dr_al_bg_w);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable, themeColor);
        b.constraintLayoutL23.setBackground(wrappedDrawable);

        b.constraintLayout23.setBackground(wrappedDrawable);
        b.ivFull.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
        b.ivLow.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
        b.btClosed.setTextColor(themeColor);
        b.btClosed2.setTextColor(themeColor);
    }

    private void setPlayer() {


        if (type.equals("low")) {
            String filePath = shCAPreference.getLowAlarmRing_ca();
            if (!filePath.equals("Off")) {
                try {
                    mediaPlayer.setDataSource(filePath);

                    mediaPlayer.prepare();
                    mediaPlayer.start();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } else {
            String filePath = shCAPreference.getFullAlarmRing_ca();
            if (!filePath.equals("Off")) {
                try {
                    mediaPlayer.setDataSource(filePath);

                    mediaPlayer.prepare();
                    mediaPlayer.start();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }


        handler = new Handler();
        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
            }
        }, 30000);

    }

    private void setLay() {
        if (type.equals("low")) {
            b.cvLow.setVisibility(View.VISIBLE);
            b.cvFull.setVisibility(View.GONE);
        } else {
            b.cvLow.setVisibility(View.GONE);
            b.cvFull.setVisibility(View.VISIBLE);
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
    protected void onDestroy() {
        try {
            DBetNilChrAnimopeaippOpen.doNotDisplApChBetryNilllon = false;
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
            }
            mediaPlayer.release();
        } catch (Exception e) {

        }
        super.onDestroy();

    }

    public static void closed() {
        if (activity != null) {
            activity.finish();
         }
    }

    @Override
    public void finish() {
        super.finish();
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
        try {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
            }
            mediaPlayer.release();
        } catch (Exception e) {

        }
    }
}
