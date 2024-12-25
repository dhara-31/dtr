package com.si_charginganimation.nilesh_charginganimation.act;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.si_charginganimation.nilesh_charginganimation.R;
import com.si_charginganimation.nilesh_charginganimation.databinding.ActAnimFullPreviewBinding;
import com.si_charginganimation.nilesh_charginganimation.model.BatteryInfo;
import com.si_charginganimation.nilesh_charginganimation.model.DBetNilChrAnimopeaippOpen;
import com.si_charginganimation.nilesh_charginganimation.utils.ShCAPreference;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AnimPreviewFullAct extends Activity {
    ActAnimFullPreviewBinding b;
    VideoView videoView;
    public static TextView tv_per;
    ConstraintLayout cv, cvImage, cvVideo, cvDate1;
    private long lastTouchTime = 0;
    private long currentTouchTime = 0;
    ShCAPreference shCAPreference;
    int click = 2;
    ImageView image;


    public static Activity activity = null;

    PowerManager.WakeLock screenLock;
    public AudioManager am;
    private TextView tvDate;
    private TextView tvTime;
    private String type;
    private String path;
    String fontStyle;
    int fontColor;

    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActAnimFullPreviewBinding.inflate(getLayoutInflater());
        DBetNilChrAnimopeaippOpen.doNotDisplApChBetryNilllon = true;
        setContentView(b.getRoot());


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        screenLock = ((PowerManager) getSystemService(POWER_SERVICE)).newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        screenLock.acquire();

        activity = this;
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "CurrentScreen: " + getClass().getSimpleName(), null);

        tv_per = findViewById(R.id.tv_percentage);
        cv = findViewById(R.id.cv);
        videoView = findViewById(R.id.videoView);
        cvImage = findViewById(R.id.cvImage);
        cvVideo = findViewById(R.id.cvVideo);
        cvDate1 = findViewById(R.id.cvDate1);
        image = findViewById(R.id.image);
        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);

        shCAPreference = new ShCAPreference(this);


        Bundle bundle = getIntent().getExtras();
        type = bundle.getString("type");
        path = bundle.getString("path");
        fontStyle = bundle.getString("fontStyle");
        fontColor = bundle.getInt("fontColor");


        tv_per.setText(shCAPreference.getLastPer_ca());
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE, dd LLLL");
        String dateTime = simpleDateFormat.format(calendar.getTime()).toString();
        tvDate.setText(dateTime);
        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (click == 1) {
                    onBackPressed();
                } else {
                    lastTouchTime = currentTouchTime;
                    currentTouchTime = System.currentTimeMillis();

                    if (currentTouchTime - lastTouchTime < 250) {

                        lastTouchTime = 0;
                        currentTouchTime = 0;


                        finish();
                    }
                }


            }
        });

        setScreen();


    }

    private void setScreen() {


        click = shCAPreference.getClosed_ca();

        boolean per = shCAPreference.getShowPercentage();


        if (!per) {
            tv_per.setVisibility(View.GONE);
        }

        if (type.equals("image")) {

            String iUri = path;
            cvVideo.setVisibility(View.GONE);
            cvDate1.setVisibility(View.GONE);
            cvImage.setVisibility(View.VISIBLE);
            Glide.with(this).load(iUri).error(R.drawable.icb_battery_capacity).into(image);

        } else if (type.equals("video")) {
            String vUri = path;
            cvVideo.setVisibility(View.VISIBLE);
            cvImage.setVisibility(View.GONE);
            cvDate1.setVisibility(View.GONE);
            setVideo(vUri);


        } else if (type.equals("anim_video")) {
            String vUri = path;
            cvVideo.setVisibility(View.VISIBLE);
            cvImage.setVisibility(View.GONE);
            cvDate1.setVisibility(View.VISIBLE);
            setVideo(vUri);
            tv_per.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
            tvTime.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
            tvDate.setTypeface(Typeface.createFromAsset(getAssets(), fontStyle));
            tvTime.setTextColor(fontColor);
            tv_per.setTextColor(fontColor);
            tvDate.setTextColor(fontColor);

        }
        if (1 == shCAPreference.getClosed_ca()) {

            b.tvClick.setText("Tap To Exit");
        } else {
            b.tvClick.setText("Double Tap To Exit");
        }


    }

    private void setVideo(String sUri) {
        videoView.setVisibility(View.VISIBLE);


        File sFile = new File(sUri);

        if (sFile.exists()) {

            Uri uriForFile = FileProvider.getUriForFile(AnimPreviewFullAct.this, getPackageName() + ".fileprovider", new File(sUri));


            videoView.setVideoURI(uriForFile);

            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.setLooping(true);
                    if (!shCAPreference.getOnOffSound()) {
                        mediaPlayer.setVolume(0f, 0f);
                    }
                    videoStart();


                }
            });




            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    videoStart();
                }
            });
            videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    return false;
                }
            });



        } else {

            setAnim();
        }


    }


    private void setAnim() {
        String iUri = shCAPreference.getImage_ca();
        cvVideo.setVisibility(View.GONE);
        cvImage.setVisibility(View.VISIBLE);
        Glide.with(this).load(iUri).error(R.drawable.icb_battery_capacity).into(image);

    }


    private static void updateView(BatteryInfo batteryInfo) {


        tv_per.setText(getLevel(batteryInfo) + "%");


    }

    public static void closed() {
        if (activity != null) {
            activity.finish();

        }
    }


    private void videoStart() {

        videoView.start();

    }


    private static String getLevel(BatteryInfo batteryInfo) {
        return String.valueOf(batteryInfo.getLevel());
    }


    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.BATTERY_CHANGED");
        registerReceiver(this.broadcastReceiver, intentFilter);
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if ("android.intent.action.BATTERY_CHANGED".equals(intent.getAction())) {
                updateView(new BatteryInfo(intent));
            }
        }
    };


    @Override
    protected void onPause() {
        super.onPause();

        if (videoView != null) {
            if (videoView.isPlaying()) {
                videoView.pause();
                b.videoView.stopPlayback();
            }

        }
        unregisterReceiver(this.broadcastReceiver);
    }


    @Override
    public void onBackPressed() {
        DBetNilChrAnimopeaippOpen.doNotDisplApChBetryNilllon = false;
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DBetNilChrAnimopeaippOpen.doNotDisplApChBetryNilllon = false;

        try {
            if (videoView != null) {
                if (videoView.isPlaying()) {

                    videoView.pause();
                    b.videoView.stopPlayback();
                }
            }
        } catch (Exception e) {

        }

        try {
            screenLock.release();
        } catch (Exception e) {

        }

    }
}
