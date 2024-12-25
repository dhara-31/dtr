package com.si_charginganimation.nilesh_charginganimation.act;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.si_charginganimation.nilesh_charginganimation.R;
import com.si_charginganimation.nilesh_charginganimation.adapter.LavelCAAdapter;
import com.si_charginganimation.nilesh_charginganimation.adapter.SongCAAdapter;
import com.si_charginganimation.nilesh_charginganimation.game.GoChBetryNils;
import com.si_charginganimation.nilesh_charginganimation.databinding.ActAlarmBinding;
import com.si_charginganimation.nilesh_charginganimation.model.SongModel;
import com.si_charginganimation.nilesh_charginganimation.other.ManyCAUSed;
import com.si_charginganimation.nilesh_charginganimation.service.ChargingCAService;
import com.si_charginganimation.nilesh_charginganimation.utils.ShCAPreference;
import com.si_charginganimation.nilesh_charginganimation.wallCAApi.NatBetsAll;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AlarmAct extends Activity {
    ActAlarmBinding b;
    private ShCAPreference shCAPreference;
    private int themeColor;
    MediaPlayer mediaPlayer;
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE

    };

    String[] fullLevel = {"Off", "80 %", "85 %", "90 %", "91 %", "92 %", "93 %", "94 %", "95 %", "96 %", "97 %", "98 %", "99 %", "100 %"};
    int[] fullLevelN = {0, 80, 85, 90, 91, 92, 93, 94, 95, 96, 97, 98, 99, 100};
    String[] lowLevel = {"Off", "5 %", "10 %", "15 %", "20 %", "25 %", "30 %", "35 %", "40 %", "45 %", "50 %"};
    int[] lowLevelN = {0, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50};
    String[] tempLevel = {"Off", "30°C / 86°F", "31°C / 87.8°F", "32°C / 89.6°F", "33°C / 91.4°F", "34°C / 93.2°F", "35°C / 95°F", "36°C / 96.8°F", "37°C / 98.6°F", "38°C / 100.4°F", "39°C / 102.2°F", "40°C / 104°F", "41°C / 105.8°F", "42°C / 107.6°F", "43°C / 109.4°F", "44°C / 111.2°F", "45°C / 113°F"};
    int[] tempLevelN = {0, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45};
    int fullLastPos = 0;
    int songSelectPos = 0;
    int songLastPos = 0;

    int lowLastPos = 0;
    int tempLastPos = 0;

    ArrayList<SongModel> songList = new ArrayList<SongModel>();
    private SongCAAdapter songCAAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActAlarmBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        shCAPreference = new ShCAPreference(this);


        FrameLayout admobNativeLarge2 = findViewById(R.id.admobNative_Banner);
        CardView cardView = findViewById(R.id.c);
        NatBetsAll.banaernatBetse(admobNativeLarge2, AlarmAct.this, cardView, true);

        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "CurrentScreen: " + getClass().getSimpleName(), null);

        mediaPlayer = new MediaPlayer();
        setThemeApp();
        b.btSetFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ManyCAUSed.isMyServiceRunning(ChargingCAService.class, AlarmAct.this)) {
                    openFullBatteryDialog();
                } else {
                    openServiceDialog();
                }

            }
        });
        b.btSetLow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ManyCAUSed.isMyServiceRunning(ChargingCAService.class, AlarmAct.this)) {
                    openLowBatteryDialog();
                } else {
                    openServiceDialog();
                }

            }
        });
        b.btSetTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ManyCAUSed.isMyServiceRunning(ChargingCAService.class, AlarmAct.this)) {
                    openTempBatteryDialog();

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

        b.btSetFullSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBcaPermissions()) {
                    openSetRingDialog("full");
                }
            }
        });
        b.btSetLowSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBcaPermissions()) {
                    openSetRingDialog("low");
                }
            }
        });
        b.btSetTempSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBcaPermissions()) {
                    openSetRingDialog("temp");
                }
            }
        });
        if (checkBcaPermissions()) {
            songList = getAllSong(this);

            setOld();
        }


    }

    private void openServiceDialog() {

        android.app.AlertDialog.Builder builder;
        android.app.AlertDialog create;
        builder = new android.app.AlertDialog.Builder(this);
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

                GoChBetryNils.getInstance().showChBetryNilster(AlarmAct.this, new GoChBetryNils.AChBetryNilInterface() {
                    @Override
                    public void aChBetryNilsCall() {
                        Intent intent = new Intent(AlarmAct.this, SettingAct.class);
                        startActivity(intent);
                    }
                });


            }
        });
        create.show();


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

        b.ivFull.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
        b.ivLow.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
        b.ivTemp.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
        b.ivFullPer.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
        b.ivLowPer.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
        b.ivTepm.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
        b.ivSound.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
        b.ivLowSound.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);
        b.ivTempSound.setColorFilter(themeColor, android.graphics.PorterDuff.Mode.MULTIPLY);

    }

    private boolean checkBcaPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            perDialog();

            return false;
        }
        return true;
    }

    private void perDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_permission, (ViewGroup) null);
        builder1.setView(inflate);
        builder1.setCancelable(false);


        TextView tvOk = inflate.findViewById(R.id.textView_ok);
        TextView textView17 = inflate.findViewById(R.id.textView17);

        textView17.setText("GO Back And Restart");
        tvOk.setText("Ok");


        AlertDialog create1 = builder1.create();
        create1.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        TextView textView_cancle = inflate.findViewById(R.id.textView_cancle);
        textView_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create1.dismiss();


            }
        });
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                create1.dismiss();



            }
        });


        create1.show();
    }

    private void openSetRingDialog(String label) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_select_song, (ViewGroup) null);
        builder.setView(inflate);
        builder.setCancelable(true);
        AlertDialog create = builder.create();
        create.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        RecyclerView recyclerView = inflate.findViewById(R.id.recyclerLevel);
        TextView btSet = inflate.findViewById(R.id.btSet);
        TextView tvLabel = inflate.findViewById(R.id.tvLabel);
        TextView tvNoSong = inflate.findViewById(R.id.tvNoSong);
        btSet.setTextColor(themeColor);

        GridLayoutManager linearLayoutManager = new GridLayoutManager(this, 1);

        recyclerView.setLayoutManager(linearLayoutManager);
        if (label.equals("full")) {
            songSelectPos = getFLPos();
        } else if (label.equals("low")) {
            songSelectPos = getLLPos();
        } else if (label.equals("temp")) {
            songSelectPos = getTLPos();
        }
        songCAAdapter = new SongCAAdapter(this, songList, songSelectPos, themeColor);
        recyclerView.setAdapter(songCAAdapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                songCAAdapter.setNewS(songList);
            }
        }, 50);


        if (songList.size() == 1) {
            tvNoSong.setVisibility(View.VISIBLE);
        } else {
            tvNoSong.setVisibility(View.GONE);

        }

        songCAAdapter.setOnItemClickListener(new SongCAAdapter.OnItemClickListenera() {
            @Override
            public void onItemClickS(int pos) {
                songSelectPos = pos;

            }
        });
        songCAAdapter.setOnItemClickListeneraMusic(new SongCAAdapter.OnItemClickListeneraMusic() {
            @Override
            public void onItemClickS(int pos) {


                if (mediaPlayer.isPlaying()) {
                    if (songLastPos == pos) {
                        mediaPlayer.pause();
                        songCAAdapter.setPl(false);
                    } else {
                        setSong(pos);
                        songCAAdapter.setPl(true);
                    }
                } else {
                    setSong(pos);
                    songCAAdapter.setPl(true);
                }
                songLastPos = pos;

            }
        });

        btSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (label.equals("full")) {
                    shCAPreference.setFullAlarmRing_ca(songList.get(songSelectPos).getSongPath());
                } else if (label.equals("low")) {
                    shCAPreference.setLowAlarmRing_ca(songList.get(songSelectPos).getSongPath());
                } else if (label.equals("temp")) {
                    shCAPreference.setTempAlarmRing_ca(songList.get(songSelectPos).getSongPath());
                }
                create.dismiss();

            }
        });
        create.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();

                }
                songSelectPos = 0;
            }
        });


        create.show();


    }

    private int getFLPos() {
        int pospos = 0;
        if (!songList.isEmpty()) {
            for (int i = 0; i < songList.size(); i++) {
                if (songList.get(i).getSongPath().equals(shCAPreference.getFullAlarmRing_ca())) {
                    pospos = i;
                }
            }

        }
        return pospos;
    }

    private int getLLPos() {
        int pospos = 0;
        if (!songList.isEmpty()) {
            for (int i = 0; i < songList.size(); i++) {
                if (songList.get(i).getSongPath().equals(shCAPreference.getLowAlarmRing_ca())) {
                    pospos = i;
                }
            }

        }
        return pospos;
    }

    private int getTLPos() {
        int pospos = 0;
        if (!songList.isEmpty()) {
            for (int i = 0; i < songList.size(); i++) {
                if (songList.get(i).getSongPath().equals(shCAPreference.getTempAlarmRing_ca())) {
                    pospos = i;
                }
            }

        }
        return pospos;
    }

    private void setSong(int pos) {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(songList.get(pos).getSongPath());

            mediaPlayer.prepare();
            mediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void setOld() {
        if (!shCAPreference.getFullAlarm_ca()) {
            b.fullBatteryLevel.setText("Off");
        } else {
            fullLastPos = shCAPreference.getFullAlarmPos_ca();
            b.fullBatteryLevel.setText("Alert At Battery level : " + fullLevel[fullLastPos]);
        }

        if (!shCAPreference.getLowAlarm_ca()) {
            b.lowBatteryLevel.setText("Off");
        } else {
            lowLastPos = shCAPreference.getLowAlarmPos_ca();
            b.lowBatteryLevel.setText("Alert At Battery level : " + lowLevel[lowLastPos]);
        }
        if (!shCAPreference.getTempAlarm_ca()) {
            b.tempWarnLevel.setText("Off");
        } else {
            tempLastPos = shCAPreference.getTempAlarmPos_ca();
            b.tempWarnLevel.setText("Alert At Temp level : " + tempLevel[tempLastPos]);
        }


    }

    private void openFullBatteryDialog() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_select_full_bt, (ViewGroup) null);
        builder.setView(inflate);
        builder.setCancelable(true);

        AlertDialog create = builder.create();
        create.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        RecyclerView recyclerView = inflate.findViewById(R.id.recyclerLevel);
        TextView btSet = inflate.findViewById(R.id.btSet);
        btSet.setTextColor(themeColor);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        recyclerView.setLayoutManager(linearLayoutManager);

        LavelCAAdapter lavelCAAdapter = new LavelCAAdapter(this, fullLevel, fullLastPos);
        recyclerView.setAdapter(lavelCAAdapter);

        lavelCAAdapter.setOnItemClickListener(new LavelCAAdapter.OnItemClickListenera() {
            @Override
            public void onItemClickS(int pos) {
                fullLastPos = pos;

            }
        });


        btSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFull(fullLastPos);

                create.dismiss();

            }
        });


        create.show();

    }

    private void openLowBatteryDialog() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_select_full_bt, (ViewGroup) null);
        builder.setView(inflate);
        builder.setCancelable(true);

        AlertDialog create = builder.create();
        create.getWindow().setBackgroundDrawable(new ColorDrawable(0));


        RecyclerView recyclerView = inflate.findViewById(R.id.recyclerLevel);
        TextView btSet = inflate.findViewById(R.id.btSet);
        TextView tvLabel = inflate.findViewById(R.id.tvLabel);

        btSet.setTextColor(themeColor);
        tvLabel.setText("Set Low Battery Alarm");


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);
        LavelCAAdapter lavelCAAdapter = new LavelCAAdapter(this, lowLevel, lowLastPos);
        recyclerView.setAdapter(lavelCAAdapter);


        lavelCAAdapter.setOnItemClickListener(new LavelCAAdapter.OnItemClickListenera() {
            @Override
            public void onItemClickS(int pos) {
                lowLastPos = pos;

            }
        });


        btSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLow(lowLastPos);

                create.dismiss();

            }
        });


        create.show();

    }

    private void openTempBatteryDialog() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_select_full_bt, (ViewGroup) null);
        builder.setView(inflate);
        builder.setCancelable(true);

        AlertDialog create = builder.create();
        create.getWindow().setBackgroundDrawable(new ColorDrawable(0));


        RecyclerView recyclerView = inflate.findViewById(R.id.recyclerLevel);
        TextView btSet = inflate.findViewById(R.id.btSet);
        TextView tvLabel = inflate.findViewById(R.id.tvLabel);

        btSet.setTextColor(themeColor);
        tvLabel.setText("Set Battery Temperature Alarm");


        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);

        recyclerView.setLayoutManager(linearLayoutManager);

        LavelCAAdapter lavelCAAdapter = new LavelCAAdapter(this, tempLevel, tempLastPos);
        recyclerView.setAdapter(lavelCAAdapter);

        lavelCAAdapter.setOnItemClickListener(new LavelCAAdapter.OnItemClickListenera() {
            @Override
            public void onItemClickS(int pos) {
                tempLastPos = pos;

            }
        });


        btSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTemp(tempLastPos);

                create.dismiss();

            }
        });


        create.show();

    }

    private void setTemp(int pos) {
        if (pos == 0) {
            shCAPreference.setTempAlarm_ca(false);
            b.tempWarnLevel.setText("Off");
        } else {
            shCAPreference.setTempAlarm_ca(true);
            shCAPreference.setTempAlarmLevel_ca(tempLevelN[pos]);
            shCAPreference.setTempAlarmPos_ca(pos);
            b.tempWarnLevel.setText("Alert At Temp level : " + tempLevel[pos]);

        }
    }

    private void setLow(int pos) {
        if (pos == 0) {
            shCAPreference.setLowAlarm_ca(false);
            b.lowBatteryLevel.setText("Off");
        } else {
            shCAPreference.setLowAlarm_ca(true);
            shCAPreference.setLowAlarmLevel_ca(lowLevelN[pos]);
            shCAPreference.setLowAlarmPos_ca(pos);
            b.lowBatteryLevel.setText("Alert At Battery level : " + lowLevel[pos]);

        }
    }

    private void setFull(int pos) {

        if (pos == 0) {
            shCAPreference.setFullAlarm_ca(false);
            b.fullBatteryLevel.setText("Off");
        } else {
            shCAPreference.setFullAlarm_ca(true);
            shCAPreference.setFullAlarmLevel_ca(fullLevelN[pos]);
            shCAPreference.setFullAlarmPos_ca(pos);
            b.fullBatteryLevel.setText("Alert At Battery level : " + fullLevel[pos]);

        }

    }

    @SuppressLint("Range")
    public static ArrayList<SongModel> getAllSong(Context context) {


        ArrayList<SongModel> songList = new ArrayList<SongModel>();


        SongModel songModel = new SongModel();
        songModel.setSongPath("Off");
        songList.add(songModel);

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        ContentResolver contentResolver = context.getContentResolver();

        Cursor cursor = contentResolver.query(uri, null, null, null, "title ASC");
        int id = 0;
        if (cursor != null && cursor.moveToFirst()) {
            do {

                String title = null;
                String duration = null;
                String data = null;


                long size = 0;
                SongModel audioModel = new SongModel();
                try {
                    title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    duration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION));


                } catch (Exception e) {

                    e.printStackTrace();
                    duration = "0";
                }

                try {
                    data = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                } catch (Exception e) {
                    e.printStackTrace();
                }


                try {
                    size = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.SIZE));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (new File(data).exists() && new File(data).getAbsolutePath().endsWith(".mp3")) {


                    if (duration != null) {


                        audioModel.setSongUri(Uri.parse(data));
                        audioModel.setSongPath(data);
                        audioModel.setSongTitle(title);
                        audioModel.setSongDuration(formatMilis(Long.parseLong(duration)));


                        audioModel.setSize(size);


                        songList.add(audioModel);


                    }

                }

            } while (cursor.moveToNext());


            cursor.close();
        }


        return songList;

    }

    public static String formatMilis(long time) {
        final int totalSeconds = Math.abs((int) time / 1000);
        final int seconds = totalSeconds % 60;
        final int minutes = totalSeconds % 3600 / 60;
        final int hours = totalSeconds / 3600;

        return (hours > 0 ? String.format("%d:%02d:%02d", hours, minutes, seconds) : String.format("%02d:%02d", minutes, seconds));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            songCAAdapter.setPl(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
    }
}

