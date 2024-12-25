package com.si_charginganimation.nilesh_charginganimation.showActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.mursaat.extendedtextview.AnimatedGradientTextView;
import com.si_charginganimation.nilesh_charginganimation.R;
import com.si_charginganimation.nilesh_charginganimation.model.BatteryInfo;
import com.si_charginganimation.nilesh_charginganimation.model.DBetNilChrAnimopeaippOpen;
import com.si_charginganimation.nilesh_charginganimation.service.ChargingCAService;
import com.si_charginganimation.nilesh_charginganimation.utils.ShCAPreference;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ActShowAnim extends Activity {
    VideoView videoView;
    public static TextView tv_per,tvTime;
    ConstraintLayout cv, cvImage, cvVideo, cvDate1;
    private long lastTouchTime = 0;
    private long currentTouchTime = 0;
    ShCAPreference shCAPreference;
    int click = 2;
    ImageView image;
    MediaPlayer mPlayer;
    private int lastSec = 0;
    public static Activity activity = null;

    PowerManager.WakeLock screenLock;
    public AudioManager am;
    private TextView tvDate;
    private AnimatedGradientTextView tvClick;
    private boolean locked=false;

    @SuppressLint("InvalidWakeLockTag")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim_show);


        DBetNilChrAnimopeaippOpen.doNotDisplApChBetryNilllon = true;
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        screenLock = ((PowerManager) getSystemService(POWER_SERVICE)).newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        screenLock.acquire();

        activity = this;

        tv_per = findViewById(R.id.tv_percentage);
        cv = findViewById(R.id.cv);
        videoView = findViewById(R.id.videoView);
        cvImage = findViewById(R.id.cvImage);
        cvVideo = findViewById(R.id.cvVideo);
        cvDate1 = findViewById(R.id.cvDate1);
        image = findViewById(R.id.image);
        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);
        tvClick = findViewById(R.id.tvClick);

        shCAPreference = new ShCAPreference(this);


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
                        onBackPressed();
                    }
                }


            }
        });
        KeyguardManager myKM = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        if( myKM.inKeyguardRestrictedInputMode() ) {
             locked=true;
         } else {
            //it is not locked
            locked=false;
        }

        setScreen();


    }

    private void setScreen() {

        int duration = shCAPreference.getDuration_ca();

        click = shCAPreference.getClosed_ca();

        boolean per = shCAPreference.getShowPercentage();


        if (!per) {
            tv_per.setVisibility(View.GONE);
        }

        if (shCAPreference.getType_ca().equals("image")) {

            String iUri = shCAPreference.getImage_ca();
            cvVideo.setVisibility(View.GONE);
            cvDate1.setVisibility(View.GONE);
            cvImage.setVisibility(View.VISIBLE);
            Glide.with(this).load(iUri).error(R.drawable.ca_battery_capacity_icon).into(image);

        } else if (shCAPreference.getType_ca().equals("video")) {
            String vUri = shCAPreference.getVideoUri_ca();
            cvVideo.setVisibility(View.VISIBLE);
            cvImage.setVisibility(View.GONE);
            cvDate1.setVisibility(View.GONE);
            setVideo(vUri);


        } else if (shCAPreference.getType_ca().equals("anim_video")) {
            String vUri = shCAPreference.getVideoUri_ca();
            cvVideo.setVisibility(View.VISIBLE);
            cvImage.setVisibility(View.GONE);
            cvDate1.setVisibility(View.VISIBLE);
            tvDate.setTypeface(Typeface.createFromAsset(getAssets(), shCAPreference.getAnimFontStyle_ca()));
            tvTime.setTypeface(Typeface.createFromAsset(getAssets(),  shCAPreference.getAnimFontStyle_ca()));
            tvTime.setTextColor(shCAPreference.getAnimFontColor_ca());
            tvDate.setTextColor(shCAPreference.getAnimFontColor_ca());

            setVideo(vUri);


        }
        if(1 == shCAPreference.getClosed_ca()){

           tvClick.setText("Tap To Exit");
        }else {
          tvClick.setText("Double Tap To Exit");
        }
        tv_per.setText(ChargingCAService.level + "%");
        if (duration != 4) {


            startHandler(duration);
        }

    }
    private void startHandler(int duration) {

        int timeClosed = 5000;
        if (duration == 1) {
            timeClosed = 5000;
        } else if (duration == 2) {
            timeClosed = 15000;
        } else if (duration == 3) {
            timeClosed = 30000;
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();

            }
        }, timeClosed);

    }

    private void setVideo(String sUri) {
        videoView.setVisibility(View.VISIBLE);


        File sFile = new File(sUri);

        if (sFile.exists()) {

            Uri uriForFile = FileProvider.getUriForFile(ActShowAnim.this, getPackageName() + ".fileprovider", new File(sUri));


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
                public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {


                    return true;
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

    private static void setStatus(BatteryInfo batteryInfo) {
        int status = batteryInfo.getStatus();

        if (status == 3) {

        }


    }

    public static void setPer(Intent intent) {
        if (tv_per != null) {
            updateView(new BatteryInfo(intent));
        }


    }

    private void videoStart() {

            videoView.start();

    }

    public boolean audioFocus() {
        if (am != null) {
            am.abandonAudioFocus(focusChangeListener11);
        }

        am = (AudioManager) this.getSystemService(AUDIO_SERVICE);
        int result = am.requestAudioFocus(focusChangeListener11, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            return true;
        }
        return false;
    }

    private static String getLevel(BatteryInfo batteryInfo) {
        return String.valueOf(batteryInfo.getLevel());
    }

    public AudioManager.OnAudioFocusChangeListener focusChangeListener11 =
            new AudioManager.OnAudioFocusChangeListener() {
                public void onAudioFocusChange(int focusChange) {

                    switch (focusChange) {

                        case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK):

                            if (videoView != null) {
                                if (videoView.isPlaying()) {
                                    videoView.pause();
                                }
                            }
                            break;
                        case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT):

                            break;

                        case (AudioManager.AUDIOFOCUS_LOSS):


                            if (videoView != null) {
                                if (videoView.isPlaying()) {
                                    videoView.pause();
                                }
                            }
                            break;

                        case (AudioManager.AUDIOFOCUS_GAIN):

                            break;
                        default:
                            break;
                    }
                }
            };

    @Override
    protected void onResume() {
        super.onResume();


    }


    @Override
    protected void onPause() {

        super.onPause();

        if (videoView != null) {
            if (videoView.isPlaying()) {
                videoView.pause();
                videoView.stopPlayback();
            }
            lastSec = videoView.getCurrentPosition();
        }

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

        finish();
        DBetNilChrAnimopeaippOpen.doNotDisplApChBetryNilllon = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (am != null) {
            am.abandonAudioFocus(focusChangeListener11);
        }
        DBetNilChrAnimopeaippOpen.doNotDisplApChBetryNilllon = false;
        if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
        }

        if (videoView != null) {
            if (videoView.isPlaying()) {
                videoView.stopPlayback();
                videoView.pause();
            }
        }

        screenLock.release();

    }
}
