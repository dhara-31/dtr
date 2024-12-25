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
import com.si_charginganimation.nilesh_charginganimation.databinding.AcShowTempWarnignBinding;
import com.si_charginganimation.nilesh_charginganimation.model.DBetNilChrAnimopeaippOpen;
import com.si_charginganimation.nilesh_charginganimation.utils.ShCAPreference;

import java.io.IOException;

public class ActTempWarning extends Activity {
    AcShowTempWarnignBinding b;
    public static Activity activity = null;
    MediaPlayer mediaPlayer;
    ShCAPreference shCAPreference;
    Handler handler;
    Runnable runnable;
    private int themeColor;
    private boolean locked = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        b = AcShowTempWarnignBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        DBetNilChrAnimopeaippOpen.doNotDisplApChBetryNilllon = true;

        activity = this;
        shCAPreference = new ShCAPreference(this);
        mediaPlayer = new MediaPlayer();

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
            //it is not locked
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


        b.ivLow.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);

        b.btClosed2.setTextColor(themeColor);
    }

    private void setPlayer() {


        String filePath = shCAPreference.getTempAlarmRing_ca();
        if (!filePath.equals("Off")) {
            try {
                mediaPlayer.setDataSource(filePath);

                mediaPlayer.prepare();
                mediaPlayer.start();

            } catch (IOException e) {
                e.printStackTrace();
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
        super.onDestroy();
        DBetNilChrAnimopeaippOpen.doNotDisplApChBetryNilllon = false;

        try {
            if (handler != null) {
                handler.removeCallbacks(runnable);
            }
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
            }
            mediaPlayer.release();
        } catch (Exception e) {

        }
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
