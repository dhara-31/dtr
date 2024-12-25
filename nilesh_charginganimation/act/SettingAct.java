package com.si_charginganimation.nilesh_charginganimation.act;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.ColorUtils;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.si_charginganimation.nilesh_charginganimation.R;
import com.si_charginganimation.nilesh_charginganimation.databinding.ActSettingBinding;
import com.si_charginganimation.nilesh_charginganimation.service.ChargingCAService;
import com.si_charginganimation.nilesh_charginganimation.utils.ShCAPreference;
import com.si_charginganimation.nilesh_charginganimation.wallCAApi.NatBetsAll;

public class SettingAct extends Activity {
    private int REQUEST_CODE = 20001;

    ActSettingBinding b;
    ShCAPreference shCAPreference;
    String[] timeString = {"5 Sec", "15 Sec", "30 sec", "Never"};
    String[] closeString = {"Single Click", "Double Click"};


    private int themeColor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActSettingBinding .inflate(getLayoutInflater());

        setContentView(b.getRoot());
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mFirebaseAnalytics.setCurrentScreen(this, "CurrentScreen: " + getClass().getSimpleName(), null);

        FrameLayout admobNativeLarge2 = findViewById(R.id.admobNative_Banner);
        CardView cardView = findViewById(R.id.c);
        NatBetsAll.banaernatBetse(admobNativeLarge2, SettingAct.this, cardView,false);

        shCAPreference = new ShCAPreference(this);
        ArrayAdapter aa = new ArrayAdapter(this, R.layout.simple_spinner_item1, timeString);
        aa.setDropDownViewResource(R.layout.simple_spinner_dropdown_item1);
        b.spinner1.setAdapter(aa);

        ArrayAdapter bb = new ArrayAdapter(this, R.layout.simple_spinner_item1, closeString);
        bb.setDropDownViewResource(R.layout.simple_spinner_dropdown_item1);
        b.spinner2.setAdapter(bb);


        b.btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        b.btStartService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMyServiceRunning(ChargingCAService.class)) {
                    Intent intent = new Intent(SettingAct.this, ChargingCAService.class);
                    stopService(intent);
                    shCAPreference.setServiceOnOff(false);
                     b.btStartService.setChecked(false);
                    Toast.makeText(SettingAct.this, "Animation Service Stop...", Toast.LENGTH_LONG).show();

                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!Settings.canDrawOverlays(SettingAct.this)) {
                          openServiceDialog();
                            b.btStartService.setChecked(false);
                        } else if (!checkBPermissions()) {
                            checkIgnorePermission();
                            b.btStartService.setChecked(false);
                        } else {
                            Intent intent = new Intent(SettingAct.this, ChargingCAService.class);
                            startService(intent);
                            shCAPreference.setServiceOnOff(true);
                            b.btStartService.setChecked(true);
                            Toast.makeText(SettingAct.this, "Animation Service Start...", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });

 

        b.btPlaySound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shCAPreference.getOnOffSound()) {
                    shCAPreference.setSound_ca(false);
                    b.btPlaySound.setChecked(false);
                } else {
                    shCAPreference.setSound_ca(true);
                    b.btPlaySound.setChecked(true);
                }

            }
        });
        b.btLcokScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shCAPreference.getShowLockScreen()) {
                    shCAPreference.setShowLockScreen(false);
                     
                    b.btLcokScreen.setChecked(false);
                } else {
                    shCAPreference.setShowLockScreen(true);
                     
                    b.btLcokScreen.setChecked(true);
                }
            }
        });
        b.btShowBtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shCAPreference.getShowPercentage()) {
                    shCAPreference.setShowPercentage(false);

                    b.btShowBtp.setChecked(false);
                } else {
                    shCAPreference.setShowPercentage(true);

                    b.btShowBtp.setChecked(true);
                }
            }
        });
        b.spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {

                    durationSaveInSh(1);

                } else if (i == 1) {
                    durationSaveInSh(2);
                } else if (i == 2) {
                    durationSaveInSh(3);

                } else if (i == 3) {
                    durationSaveInSh(4);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        b.spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {

                    closedSaveInSh(1);

                } else if (i == 1) {
                    closedSaveInSh(2);
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    setThemeApp();
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
        int c = ColorUtils.setAlphaComponent(themeColor, 100);
        b.btShowBtp.setBgOnColor(c);
        b.btShowBtp.setToggleOnColor(themeColor);
        b.btStartService.setBgOnColor(c);
        b.btStartService.setToggleOnColor(themeColor);
        b.btLcokScreen.setBgOnColor(c);
        b.btLcokScreen.setToggleOnColor(themeColor);
        b.btPlaySound.setBgOnColor(c);
        b.btPlaySound.setToggleOnColor(themeColor);



    }

    private void setButton() {

        if (shCAPreference.getShowLockScreen()) {
             
            b.btLcokScreen.setChecked(true);
        }
        if (shCAPreference.getOnOffSound()) {

            b.btPlaySound.setChecked(true);
        }
        if (shCAPreference.getShowPercentage()) {

            b.btShowBtp.setChecked(true);
        }
        int timeInt = shCAPreference.getDuration_ca();
        if (timeInt == 1) {
            b.spinner1.setSelection(0);
        } else if (timeInt == 2) {
            b.spinner1.setSelection(1);
        } else if (timeInt == 3) {
            b.spinner1.setSelection(2);
        } else if (timeInt == 4) {
            b.spinner1.setSelection(3);
        }
        int closedInt = shCAPreference.getClosed_ca();
        if (closedInt == 1) {
            b.spinner2.setSelection(0);
        } else if (closedInt == 2) {

            b.spinner2.setSelection(1);
        }


        if (shCAPreference.getType_ca().equals("image") || shCAPreference.getType_ca().equals("video") || shCAPreference.getType_ca().equals("anim_video")) {
            b.cv1.setVisibility(View.VISIBLE);
            b.cv2.setVisibility(View.VISIBLE);
            b.cv3.setVisibility(View.VISIBLE);
            b.cv4.setVisibility(View.VISIBLE);
        } else {
            b.cv1.setVisibility(View.GONE);
            b.cv2.setVisibility(View.GONE);
            b.cv3.setVisibility(View.GONE);
            b.cv4.setVisibility(View.GONE);
        }

    }

    private void closedSaveInSh(int ii) {
        shCAPreference.setClosed_ca(ii);


    }

    private void durationSaveInSh(int i) {


        shCAPreference.setDuration_ca(i);


    }

    private void checkDrawOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getApplicationContext().getPackageName()));
            final String EXTRA_FRAGMENT_ARG_KEY = ":settings:fragment_args_key";
            final String EXTRA_SHOW_FRAGMENT_ARGUMENTS = ":settings:show_fragment_args";

            Bundle bundle = new Bundle();
            String showArgs = getPackageName();

            bundle.putString(EXTRA_FRAGMENT_ARG_KEY, showArgs);
            intent.putExtra(EXTRA_FRAGMENT_ARG_KEY, showArgs);
            intent.putExtra(EXTRA_SHOW_FRAGMENT_ARGUMENTS, bundle);

            startActivityForResult(intent, REQUEST_CODE);

        }
    }
    private void openServiceDialog() {

        AlertDialog.Builder builder;
        AlertDialog create;
        builder = new AlertDialog.Builder(this);
        View inflate = LayoutInflater.from(this).inflate(R.layout.dialog_ask_setting
                , (ViewGroup) null);
        builder.setView(inflate);
        builder.setCancelable(true);
        create = builder.create();
        create.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        TextView btShow = inflate.findViewById(R.id.btSetting);
        TextView btCancel = inflate.findViewById(R.id.btCancel);
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
                checkDrawOverlayPermission();


            }
        });

        create.show();


    }

    private boolean checkBPermissions() {

        boolean b = false;
        String packageName = getPackageName();
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                b = false;
            } else {
                b = true;
            }
        }
        return b;
    }

    private void checkIgnorePermission() {
        Intent intent = new Intent();
        String packageName = getPackageName();
        PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                startActivityForResult(intent, 101);
            }
        }
    }


    private void setSwi() {
         if (isMyServiceRunning(ChargingCAService.class)) {
             b.btStartService.setChecked(true);
        } else {
             b.btStartService.setChecked(false);
        }
    }
    @SuppressLint("WrongConstant")
    private boolean isMyServiceRunning(Class<?> cls) {
        for (ActivityManager.RunningServiceInfo runningServiceInfo : ((ActivityManager) getSystemService("activity")).getRunningServices(Integer.MAX_VALUE)) {
            if (cls.getName().equals(runningServiceInfo.service.getClassName())) {

                return true;
            }
        }
        return false;
    }
    @Override
    protected void onResume() {
        super.onResume();
        setSwi();
    }
}
