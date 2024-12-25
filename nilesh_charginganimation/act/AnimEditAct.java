package com.si_charginganimation.nilesh_charginganimation.act;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.si_charginganimation.nilesh_charginganimation.R;
import com.si_charginganimation.nilesh_charginganimation.adapter.ColorCAAdapter;
import com.si_charginganimation.nilesh_charginganimation.adapter.FontCAAdapter;
import com.si_charginganimation.nilesh_charginganimation.databinding.ActAnimPreviewBinding;
import com.si_charginganimation.nilesh_charginganimation.game.GoChBetryNils;
import com.si_charginganimation.nilesh_charginganimation.other.HelperResize;
import com.si_charginganimation.nilesh_charginganimation.other.ManyCAUSed;
import com.si_charginganimation.nilesh_charginganimation.service.ChargingCAService;
import com.si_charginganimation.nilesh_charginganimation.utils.ShCAPreference;
import com.si_charginganimation.nilesh_charginganimation.wallCAApi.NatBetsAll;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class AnimEditAct extends Activity {
    ActAnimPreviewBinding b;
    String type;
    String filePath;
    private int lastSec = 0;
    ShCAPreference shCAPreference;
    private AlertDialog.Builder builder;
    AlertDialog create;
    private AlertDialog.Builder dowBuilder;
    AlertDialog dowCreate;
    private String filePath_high;
    File videoPath = null;
    boolean downAndApply = false;
    private TextView tvDowPer;
    public AudioManager am;
    String fontStyle = "font/font1.ttf";
    int fontColor = Color.WHITE;
    static String[] fonts = {"font/font1.ttf", "font/font2.otf", "font/font3.otf", "font/font4.ttf", "font/font5.ttf", "font/font6.ttf", "font/font7.ttf", "font/font8.otf", "font/font9.ttf", "font/font10.ttf", "font/font11.ttf", "font/font12.ttf", "font/font13.ttf", "font/font14.ttf", "font/font15.ttf", "font/font16.ttf", "font/font17.ttf", "font/font19.otf", "font/font21.ttf", "font/font22.ttf"};
    private int review_position = 0;
    private int themeColor;
    private ConstraintLayout cvLBG;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActAnimPreviewBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        FrameLayout admobNativeLarge2 = findViewById(R.id.admobNative_Banner);
        CardView cardView = findViewById(R.id.c);
        FrameLayout banner = findViewById(R.id.banner);
        NatBetsAll.getInstance().natVolBetsl(AnimEditAct.this, banner, findViewById(R.id.cardBAnner), admobNativeLarge2, cardView);


        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "CurrentScreen: " + getClass().getSimpleName(), null);

        shCAPreference = new ShCAPreference(this);
        Bundle bundle = getIntent().getExtras();
        type = bundle.getString("type");
        filePath = bundle.getString("path");
        filePath_high = bundle.getString("path_high");

        b.btApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ManyCAUSed.isMyServiceRunning(ChargingCAService.class, AnimEditAct.this)) {
                    saveData();
                } else {
                    openServiceDialog();
                }
            }
        });
        b.btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        b.btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEditDialog();
            }
        });
        b.btPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(AnimEditAct.this, AnimPreviewFullAct.class);
                intent.putExtra("type", "anim_video");
                intent.putExtra("path", filePath);
                intent.putExtra("fontStyle", fontStyle);
                intent.putExtra("fontColor", fontColor);
                startActivity(intent);


            }
        });

        setThemeApp();
        createDialog();
        downlaodownDialog();
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
        b.ivApply.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
        b.ivPer.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
        b.ivEdit.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
    }

    private void openEditDialog() {
        fontColor = shCAPreference.getAnimFontColor_ca();
        fontStyle = shCAPreference.getAnimFontStyle_ca();
        AlertDialog.Builder builder;
        AlertDialog create;
        builder = new AlertDialog.Builder(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_edit_font, (ViewGroup) null);
        builder.setView(inflate);
        builder.setCancelable(true);
        create = builder.create();
        create.getWindow().setBackgroundDrawable(new ColorDrawable(0));


        RecyclerView rvFont = inflate.findViewById(R.id.rvFont);
        RecyclerView rvFontColor = inflate.findViewById(R.id.rvFontColor);
        TextView btSave = inflate.findViewById(R.id.btSave);
        btSave.setTextColor(themeColor);

        rvFont.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        FontCAAdapter font_styleAdapter = new FontCAAdapter(this, fonts, themeColor);
        rvFont.setAdapter(font_styleAdapter);
        font_styleAdapter.setOnItemClickListener(new FontCAAdapter.OnItemClickListenera() {
            @Override
            public void onItemClickS(String pos) {
                fontStyle = pos;

            }
        });
        int[] colorNumberList = this.getResources().getIntArray(R.array.colorNumberList);


        rvFontColor.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        ColorCAAdapter clockColorCAAdapter = new ColorCAAdapter(this, colorNumberList, themeColor);
        rvFontColor.setAdapter(clockColorCAAdapter);

        clockColorCAAdapter.OnItemClickListenerS(new ColorCAAdapter.OnItemClickListenerS() {
            @Override
            public void onItemClick1(View view, int i, int position) {
                fontColor = i;
            }
        });
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                b.tvApply.setText("Apply");
                b.btApply.setAlpha(1f);
                create.dismiss();
            }
        });

        create.show();

    }

    private void createDialog() {
        builder = new AlertDialog.Builder(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_lodding, (ViewGroup) null);
        builder.setView(inflate);
        builder.setCancelable(false);

        create = builder.create();
        create.getWindow().setBackgroundDrawable(new ColorDrawable(0));


    }

    private void downlaodownDialog() {


        dowBuilder = new AlertDialog.Builder(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_download2, (ViewGroup) null);
        dowBuilder.setView(inflate);
        dowBuilder.setCancelable(false);
        dowCreate = dowBuilder.create();
        dowCreate.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        tvDowPer = inflate.findViewById(R.id.tvDownPer);
        cvLBG = inflate.findViewById(R.id.cvLBG);
        Drawable unwrappedDrawable = AppCompatResources.getDrawable(this, R.drawable.dr_btn_bg2);
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable, themeColor);
        cvLBG.setBackground(wrappedDrawable);
    }

    private void setButton() {


        if (type.equals("animVideo")) {
            String fileName = URLUtil.guessFileName(filePath_high, null, null);

            File file = new File(getFilesDir(), "bca");

            File videoPathCheck = new File(file, fileName);
            if (videoPathCheck.exists()) {


            }

            if (shCAPreference.getType_ca().equals("anim_video")) {
                if (shCAPreference.getVideoUri_ca() != null) {
                    if (videoPathCheck.getAbsolutePath().equals(shCAPreference.getVideoUri_ca())) {

                        b.tvApply.setText("Applied");
                        b.btApply.setAlpha(0.4f);
                    }
                }
            }
        }
    }

    private void saveData() {
        shCAPreference.setAnimFontStyle_ca(fontStyle);
        shCAPreference.setAnimFontColor_ca(fontColor);

        downloadAndAply();


    }

    private void downloadAndAply() {
        String fileName = URLUtil.guessFileName(filePath_high, null, null);

        File file = new File(getFilesDir(), "bca");

        File videoPathCheck = new File(file, fileName);
        if (videoPathCheck.exists()) {

            shCAPreference.setVideo(videoPathCheck.getAbsolutePath());
            shCAPreference.setType_ca("anim_video");

            b.tvApply.setText("Applied");
            b.btApply.setAlpha(0.4f);
        } else {
            openQulityDialog();
        }


    }

    private void openQulityDialog() {

        AlertDialog.Builder builder;
        AlertDialog create;
        builder = new AlertDialog.Builder(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_select_quality, (ViewGroup) null);
        builder.setView(inflate);
        builder.setCancelable(true);
        create = builder.create();
        create.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        TextView btHigh = inflate.findViewById(R.id.btHigh);
        TextView btLow = inflate.findViewById(R.id.btLow);
        btHigh.setTextColor(themeColor);
        btLow.setTextColor(themeColor);


        btHigh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ManyCAUSed.isNetworkAvailable(AnimEditAct.this)) {
                    create.dismiss();
                    downAndApply = true;
                    new downlaod().execute("high");
                } else {
                    Toast.makeText(AnimEditAct.this , "Please On Internet..",Toast.LENGTH_LONG).show();
                }


            }
        });

        btLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create.dismiss();


                shCAPreference.setVideo(filePath);
                shCAPreference.setType_ca("anim_video");
                //b.btApply.setBackground(getResources().getDrawable(R.drawable.cba_applied_btn));
                b.tvApply.setText("Applied");
                b.btApply.setAlpha(0.4f);
            }
        });
        create.show();


    }

    private void setData() {

        create.show();

        b.videoView.setVisibility(View.VISIBLE);


        String fileName = URLUtil.guessFileName(filePath_high, null, null);

        File file = new File(getFilesDir(), "bca");

        File videoPathCheck = new File(file, fileName);


        b.videoView.setVideoURI(Uri.parse(filePath));
        try {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(filePath);

            int width = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
            int height = Integer.valueOf(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));

            if (width > height) {
                HelperResize.getheightandwidth(this);
                HelperResize.setSize(b.videoView, 920, 1100, true);
            } else {
                HelperResize.getheightandwidth(this);
                HelperResize.setHeight(this, b.videoView, 1100);
            }
            retriever.release();
        } catch (Exception e) {

        }
        b.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
                videoStart();


                if (create != null) {
                    create.dismiss();
                }

            }
        });
        b.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                videoStart();
            }
        });
        //Toast.makeText(this,"Tap to Play Or Pause Video",Toast.LENGTH_LONG).show();
        //
        b.videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {


                return true;
            }
        });

    }

    private void videoStart() {


        b.videoView.start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!type.equals("image")) {
            if (b.videoView != null) {

                b.videoView.seekTo(lastSec);


            }
        }


    }


    @Override
    protected void onPause() {
        super.onPause();
        if (b.videoView != null) {
            if (b.videoView.isPlaying()) {
                b.videoView.pause();

                b.videoView.stopPlayback();

            }
            lastSec = b.videoView.getCurrentPosition();
        }

    }


    @Override
    protected void onDestroy() {

        super.onDestroy();

        if (b.videoView != null) {
            if (b.videoView.isPlaying()) {

                b.videoView.pause();
                b.videoView.stopPlayback();
            }

        }
    }

    private class downlaod extends AsyncTask<String, String, String> {

        int count;

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            dowCreate.show();
            tvDowPer.setText("0%");
            b.videoView.pause();
        }

        @Override
        public String doInBackground(String... strings) {

            String chekQ = strings[0];
            String urls;

            if (chekQ.equals("high")) {
                urls = filePath_high;
            } else {
                urls = filePath;


            }

            File file = new File(getFilesDir(), "bca");
            if (!file.exists()) {
                file.mkdirs();

            }


            String fileName = URLUtil.guessFileName(urls, null, null);
            videoPath = new File(file, fileName);
            URL url = null;
            try {
                url = new URL(urls);

                URLConnection connection = url.openConnection();
                connection.connect();
                int lenghtOfFile = connection.getContentLength();


                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                FileOutputStream output = new FileOutputStream(videoPath);

                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    output.write(data, 0, count);
                }


                output.flush();

                output.close();
                input.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

            return videoPath.getAbsolutePath();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);

            tvDowPer.setText(values[0] + "%");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                if (dowCreate != null) {
                    dowCreate.dismiss();
                }
            } catch (Exception e) {

            }
            if (s != null) {
                if (downAndApply) {

                    shCAPreference.setVideo(s);
                    shCAPreference.setType_ca("anim_video");

                    b.tvApply.setText("Applied");
                    b.btApply.setAlpha(0.4f);
                }

            } else {
                videoPath.delete();
                Toast.makeText(AnimEditAct.this, "Download Failed...", Toast.LENGTH_SHORT).show();

            }
            videoStart();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            videoPath.delete();
            videoStart();
            Toast.makeText(AnimEditAct.this, "Download Failed...", Toast.LENGTH_SHORT).show();

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
                GoChBetryNils.getInstance().showChBetryNilster(AnimEditAct.this, new GoChBetryNils.AChBetryNilInterface() {
                    @Override
                    public void aChBetryNilsCall() {
                        Intent intent = new Intent(AnimEditAct.this, SettingAct.class);
                        startActivity(intent);

                    }
                });


            }
        });
        create.show();


    }


}
