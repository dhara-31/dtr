package com.si_charginganimation.nilesh_charginganimation.act;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import androidx.annotation.Nullable;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.si_charginganimation.nilesh_charginganimation.databinding.ActOwlPerviewBinding;
import com.si_charginganimation.nilesh_charginganimation.model.DBetNilChrAnimopeaippOpen;
import com.si_charginganimation.nilesh_charginganimation.other.ManyCAUSed;

public class OwlThemePerview extends Activity {
    ActOwlPerviewBinding b;
    static Handler handler;
    static Runnable r;
    static float aa;
    boolean update = true;
    String fontStyle = "font/font1.ttf";
    private int cn = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        b = ActOwlPerviewBinding.inflate(getLayoutInflater());
        DBetNilChrAnimopeaippOpen.doNotDisplApChBetryNilllon=true;
        setContentView(b.getRoot());
        Bundle bundle = getIntent().getExtras();
        fontStyle = bundle.getString("fontStyle");


        setData();
        setTimer(100);

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "CurrentScreen: " + getClass().getSimpleName(), null);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        DBetNilChrAnimopeaippOpen.doNotDisplApChBetryNilllon=false;
    }

    private void setData() {
        b.tvDate.setText(ManyCAUSed.getDate4());

        b.tvDate.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
        b.tvTime.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
        b.tvPer.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));


    }

    public void setTimer(int ll) {

        aa = 0;
        handler = new Handler();
        handler.postDelayed(r = new Runnable() {
            @Override
            public void run() {


                float progress = 1.0f - (((float) aa * 10 / 1000.0f));
                b.waveView.setWaveXAxisPositionMultiplier(progress);
                b.waveView2.setWaveXAxisPositionMultiplier(progress);
                b.waveView3.setWaveXAxisPositionMultiplier(progress);
                b.waveView4.setWaveXAxisPositionMultiplier(progress);
                b.waveView5.setWaveXAxisPositionMultiplier(progress);

                if (update) {
                    aa = aa + 0.2f;
                } else {
                    aa = aa - 0.2f;
                }

                if (aa > ll) {
                    update = false;

                }

                if (aa < 1f) {
                    update = true;
                    cn = cn + 1;
                    if (cn == 10) {
                        cn = 0;
                    }




                }
                if (update) {
                    handler.postDelayed(r, 50);
                } else {
                    setAnim();
                }
                
            }
        }, 50);


    }

    private void setAnim() {
        Animation anim = new AlphaAnimation(0.3f, 1.0f);
        anim.setDuration(1500);
        anim.setStartOffset(10);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);
        b.linearLayout.startAnimation(anim);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DBetNilChrAnimopeaippOpen.doNotDisplApChBetryNilllon=false;
        if (handler != null)
            handler.removeCallbacks(r);
    }
}
