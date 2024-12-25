package com.si_charginganimation.nilesh_charginganimation.act;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.core.graphics.drawable.DrawableCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.si_charginganimation.nilesh_charginganimation.R;
import com.si_charginganimation.nilesh_charginganimation.databinding.ActMediaPreviewBinding;
import com.si_charginganimation.nilesh_charginganimation.game.GoChBetryNils;
import com.si_charginganimation.nilesh_charginganimation.other.HelperResize;
import com.si_charginganimation.nilesh_charginganimation.other.ManyCAUSed;
import com.si_charginganimation.nilesh_charginganimation.service.ChargingCAService;
import com.si_charginganimation.nilesh_charginganimation.utils.ShCAPreference;
import com.si_charginganimation.nilesh_charginganimation.wallCAApi.NatBetsAll;


import java.io.File;

public class MediaPreviewAct extends Activity {
    ActMediaPreviewBinding b;

    String type;
    String filePath;
    private int lastSec = 0;

    private AlertDialog.Builder builder;
    AlertDialog create;

    private String filePath_high;
    File videoPath = null;

    public AudioManager am;
    private ShCAPreference shCAPreference;
    private int themeColor;

    ConstraintLayout btPreview, btApply;
    ImageView btBack;
    TextView tvApply;
VideoView videoView;
ImageView ivImage;
 ImageView   ivApply,    ivPer    ,ivEdit;
 ConstraintLayout cvImage,cvVideo;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActMediaPreviewBinding.inflate(getLayoutInflater());

        setContentView(R.layout.act_media_preview);
        btApply = findViewById(R.id.btApply);
        btPreview = findViewById(R.id.btPreview);
        btBack = findViewById(R.id.btBack);
        videoView = findViewById(R.id.videoView);
        ivImage = findViewById(R.id.ivImage);
        ivApply = findViewById(R.id.ivApply);
        ivPer = findViewById(R.id.ivPer);
        ivEdit = findViewById(R.id.ivEdit);
        tvApply = findViewById(R.id.tvApply);
        cvVideo = findViewById(R.id.cvVideo);
        cvImage = findViewById(R.id.cvImage);

        FrameLayout admobNativeLarge2 = findViewById(R.id.admobNative_Banner);
        CardView cardView = findViewById(R.id.c);
        FrameLayout banner = findViewById(R.id.banner);
        NatBetsAll.getInstance().natVolBetsl(MediaPreviewAct.this, banner, findViewById(R.id.cardBAnner),admobNativeLarge2,cardView);

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "CurrentScreen: " + getClass().getSimpleName(), null);



        shCAPreference = new ShCAPreference(this);
        Bundle bundle = getIntent().getExtras();
        type = bundle.getString("type");
        filePath = bundle.getString("path");

        btApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ManyCAUSed.isMyServiceRunning(ChargingCAService.class, MediaPreviewAct.this)) {
                    saveData();
                } else {
                    openServiceDialog();
                }
            }
        });
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                        Intent intent = new Intent(MediaPreviewAct.this, AnimPreviewFullAct.class);
                        intent.putExtra("type", type);
                        intent.putExtra("path", filePath);
                        startActivity(intent);


            }
        });

        videoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (videoView.isPlaying()) {
                    videoView.pause();

                } else {

                    videoStart();
                }
                return false;
            }
        });


        setThemeApp();
        createDialog();

        setData();
        setButton();

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


        ivApply.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
        ivPer.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
        ivEdit.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    private void createDialog() {
        builder = new AlertDialog.Builder(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_lodding, (ViewGroup) null);
        builder.setView(inflate);
        builder.setCancelable(false);

        create = builder.create();
        create.getWindow().setBackgroundDrawable(new ColorDrawable(0));


    }


    private void setButton() {
        if (shCAPreference.getType_ca() != null) {
            if (shCAPreference.getType_ca().equals("image")) {
                if (shCAPreference.getImage_ca() != null) {
                    if (filePath.equals(shCAPreference.getImage_ca())) {

                        tvApply.setText("Applied");
                        btApply.setAlpha(0.4f);
                    }
                }
            } else if (shCAPreference.getType_ca().equals("video") && type.equals("video")) {
                if (shCAPreference.getVideoUri_ca() != null) {
                    if (filePath.equals(shCAPreference.getVideoUri_ca())) {

                        tvApply.setText("Applied");
                        btApply.setAlpha(0.4f);
                    }
                }
            }
        }


    }

    private void saveData() {


        if (type.equals("image")) {
            shCAPreference.setImage_ca(filePath);
            shCAPreference.setType_ca(type);

        } else if (type.equals("video")) {
            shCAPreference.setVideo(filePath);
            shCAPreference.setType_ca(type);

        }
        tvApply.setText("Applied");
        btApply.setAlpha(0.4f);

    }


    private void setData() {
        if (type.equals("image")) {
            cvImage.setVisibility(View.VISIBLE);
            cvVideo.setVisibility(View.GONE);
            Glide.with(this).load(filePath).into(ivImage);


        } else if (type.equals("video")) {

            cvImage.setVisibility(View.GONE);
            cvVideo.setVisibility(View.VISIBLE);
            Uri uriForFile = FileProvider.getUriForFile(MediaPreviewAct.this, getPackageName() + ".fileprovider", new File(filePath));


            videoView.setVideoURI(uriForFile);
            try {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(filePath);

                int width = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                int height = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));

                 if (width > height) {
                    HelperResize.getheightandwidth(this);
                    HelperResize.setSize(findViewById(R.id.videoView), 920, 1100, true);
                } else {
                    HelperResize.getheightandwidth(this);
                    HelperResize.setHeight(this, findViewById(R.id.videoView), 1100);
                }
                retriever.release();
            } catch (Exception e) {

            }

            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.setLooping(true);

                    videoStart();

                }
            });
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    videoStart();
                }
            });

            Toast.makeText(this, "Tap to Play Or Pause Video", Toast.LENGTH_LONG).show();
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

                GoChBetryNils.getInstance().showChBetryNilster(MediaPreviewAct.this, new GoChBetryNils.AChBetryNilInterface() {
                    @Override
                    public void aChBetryNilsCall() {
                        Intent intent = new Intent(MediaPreviewAct.this, SettingAct.class);
                        startActivity(intent);
                    }
                });


            }
        });
        create.show();


    }

    private void videoStart() {

        if (audioFocus()) {

            videoView.start();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!type.equals("image")) {
            if (videoView != null) {


                videoView.seekTo(lastSec);


            }
        }


    }


    @Override
    protected void onPause() {
        super.onPause();
        if (videoView != null) {
            if (videoView.isPlaying()) {
                videoView.pause();

            }
            lastSec = videoView.getCurrentPosition();
        }

    }


    @Override
    protected void onDestroy() {
        if (am != null) {
            am.abandonAudioFocus(focusChangeListener);
        }
        super.onDestroy();

        if (videoView != null) {
            if (videoView.isPlaying()) {

                videoView.pause();
            }

        }
    }


    public boolean audioFocus() {
        if (am != null) {
            am.abandonAudioFocus(focusChangeListener);
        }

        am = (AudioManager) this.getSystemService(AUDIO_SERVICE);
        int result = am.requestAudioFocus(focusChangeListener, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            return true;
        }
        return false;
    }

    public AudioManager.OnAudioFocusChangeListener focusChangeListener =
            new AudioManager.OnAudioFocusChangeListener() {
                public void onAudioFocusChange(int focusChange) {
                    switch (focusChange) {

                        case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK):
                            if (videoView != null) {

                                if (videoView.isPlaying()) {
                                    videoView.pause();

                                }
                                lastSec = videoView.getCurrentPosition();
                            }
                            break;
                        case (AudioManager.AUDIOFOCUS_LOSS_TRANSIENT):
                            if (videoView != null) {
                                if (videoView.isPlaying()) {
                                    videoView.pause();
                                }
                                lastSec = videoView.getCurrentPosition();
                            }
                            break;

                        case (AudioManager.AUDIOFOCUS_LOSS):

                            if (videoView != null) {
                                if (videoView.isPlaying()) {
                                    videoView.pause();
                                }
                                lastSec = videoView.getCurrentPosition();
                            }

                            break;

                        case (AudioManager.AUDIOFOCUS_GAIN):

                            break;
                        default:
                            break;
                    }
                }
            };
}
