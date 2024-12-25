package com.si_charginganimation.nilesh_charginganimation.nilbetanim;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.si_charginganimation.nilesh_charginganimation.R;
import com.si_charginganimation.nilesh_charginganimation.act.MainAct;
import com.si_charginganimation.nilesh_charginganimation.act.StartAct;
import com.si_charginganimation.nilesh_charginganimation.game.GoChBetryNils;
import com.si_charginganimation.nilesh_charginganimation.model.DBetNilChrAnimopeaippOpen;
import com.si_charginganimation.nilesh_charginganimation.wallCAApi.NatBetsAll;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class SplashActivity extends AppCompatActivity {


    public static Handler handleSplibeth;
    public static Runnable runnableSPlaibetD;
    CountDownTimer timer;
    int timerScribetn = 7000;
    public DBettryplication myApp = DBettryplication.getInstance();
    boolean checkResumed = true;

    boolean overtimerLive = false;

    public Dialog dialibetg;
    public static Boolean appopenD = true;

    public static Boolean vaibete = false;
    public static Integer valueibet = 0;
    public static Boolean exibett = false;
    public static int screennumibetr = 0;

    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DBetNilChrAnimopeaippOpen.firstFlag = true;
        setContentView(R.layout.act_splash);

        exibett = false;
        valueibet = 0;

        vaibete = false;
        DBettryplication.isSplashFBettryh = false;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        DBetNilChrAnimopeaippOpen.SplashActivity = SplashActivity.this;

        if (isNetworkAvailable()) {
            try {
                if (dialibetg != null && dialibetg.isShowing()) {
                    dialibetg.dismiss();
                }
            } catch (Exception ignored) {
            }

            getApiValues();
            startLive();
        } else {
            if (DBetNilChrAnimopeaippOpen.appOpenAd == null) {
                InternetDialogUpdate();
            }
        }
    }


    public void getApiValues() {
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder().build();
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mFirebaseRemoteConfig.setConfigSettingsAsync(configSettings);


        if (mFirebaseRemoteConfig != null) {

            mFirebaseRemoteConfig.fetchAndActivate()
                    .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                        @Override
                        public void onComplete(@NonNull Task<Boolean> task) {
                            if (task.isSuccessful()) {

                                String object = mFirebaseRemoteConfig.getString("appdetail");

                                try {
                                    JsonParser parser = new JsonParser();
                                    JsonElement mJson = parser.parse(object);
                                    Gson gson = new Gson();
                                    AppBettry object1 = gson.fromJson(mJson, AppBettry.class);
                                    myApp.setAppDetail(object1);
                                } catch (Exception e) {
                                }

                                try {
                                    Gson gson = new Gson();
                                    Type type = new TypeToken<List<DetailAds>>() {
                                    }.getType();
                                    String adsdetail = mFirebaseRemoteConfig.getString("adsdetail");
                                    ArrayList<DetailAds> contactList = gson.fromJson(adsdetail, type);
                                    myApp.setAdsDetails(contactList);
                                } catch (Exception e) {

                                }


                                if (myApp.getAppDetail() != null && myApp.getAppDetail().getAppscreennumber() != null && !TextUtils.isEmpty(myApp.getAppDetail().getAppscreennumber())) {
                                    screennumibetr = Integer.parseInt(myApp.getAppDetail().getAppscreennumber());
                                }


                                AppBettry appDetail = DBettryplication.getInstance().getAppDetail();
                                if (appDetail != null && appDetail.getCounter() != null && !TextUtils.isEmpty(appDetail.getCounter())) {
                                    if (appDetail != null && appDetail.getAdmobinter() != null && !TextUtils.isEmpty(appDetail.getAdmobinter()) && appDetail.getAdstatus().equals("1")) {
                                        GoChBetryNils.getInstance().loChBetryNilsa(SplashActivity.this);
                                    }
                                }

                                NatBetsAll.getInstance().loadNatiBetsh(SplashActivity.this);


                                if (myApp.getAppDetail().getAdstatus().equalsIgnoreCase("2")) {
                                    StartMainScreen();
                                } else if (myApp.getAppDetail().getAdstatus().equals("1")) {

                                    if (myApp.getAppDetail() != null && myApp.getAppDetail().getAdmobappopen() != null && !TextUtils.isEmpty(myApp.getAppDetail().getAdmobappopen())) {


                                        DBetNilChrAnimopeaippOpen.AD_UNIT = myApp.getAppDetail().getAdmobappopen();

                                        myApp.initializeOpenVideo();
                                        myApp.splashscreeBettryme = "SplashActivity";

                                        DBetNilChrAnimopeaippOpen.SplashActivity = SplashActivity.this;

                                        DBettryplication.appOpenManBettryr.firstFlag = true;

                                        if (DBetNilChrAnimopeaippOpen.appOpenAd == null) {
                                            DBettryplication.appOpenManBettryr.getOpenAds();
                                        }

                                    } else {
                                        StartMainScreen();
                                    }
                                }


                            } else {
                                StartMainScreen();
                            }
                        }
                    });

        } else {
            StartMainScreen();
        }

    }


    public void startLive() {

        overtimerLive = false;
        runnableSPlaibetD = new Runnable() {
            @Override
            public void run() {


                overtimerLive = true;
                DBettryplication.isSplashFBettryh = true;

                if (isNetworkAvailable()) {
                    try {
                        if (dialibetg != null && dialibetg.isShowing()) {
                            dialibetg.dismiss();
                        }
                    } catch (Exception e) {
                    }

                    if (checkResumed) {


                        if (exibett == false) {

                            vaibete = true;

                            DBettryplication.isSplashFBettryh = true;
                            DBettryplication.appOpenManBettryr.firstFlag = false;

                            try {
                                if (handleSplibeth != null) {
                                    handleSplibeth.removeCallbacks(runnableSPlaibetD);
                                }
                            } catch (Exception e) {

                            }


                            if (screennumibetr >= 1) {
                                startActivity(new Intent(SplashActivity.this, StartAct.class));
                                finish();
                            } else {
                                startActivity(new Intent(SplashActivity.this, MainAct.class));
                                finish();

                            }

                        }

                    }
                } else {
                    DBettryplication.isSplashFBettryh = true;
                    try {
                        if (dialibetg != null && dialibetg.isShowing()) {
                            dialibetg.dismiss();
                        }
                    } catch (Exception e) {
                    }
                    InternetDialogUpdate();
                }
            }
        };

        try {
            if (handleSplibeth != null) {
                handleSplibeth.removeCallbacks(runnableSPlaibetD);
            }
        } catch (Exception e) {

        }


        try {
            if (timer != null) {
                timer.cancel();
            }
        } catch (Exception e) {

        }

        handleSplibeth = new Handler();

        handleSplibeth.postDelayed(runnableSPlaibetD, timerScribetn);

        timer = new CountDownTimer(timerScribetn, 100) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
            }

        }.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        checkResumed = false;
    }

    private void StartMainScreen() {

        DBettryplication.appOpenManBettryr.firstFlag = false;

        if (screennumibetr >= 1) {
            startActivity(new Intent(SplashActivity.this, StartAct.class));
            finish();
        } else {
            startActivity(new Intent(SplashActivity.this, MainAct.class));
            finish();

        }


    }


    @Override
    protected void onResume() {
        checkResumed = true;
        super.onResume();

    }


    public void InternetDialogUpdate() {
        dialibetg = new Dialog(this);
        dialibetg.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialibetg.requestWindowFeature(1);
        dialibetg.setContentView(R.layout.net_connection);
        dialibetg.setCancelable(false);
        RelativeLayout txt_yes = (RelativeLayout) dialibetg.findViewById(R.id.yes);
        RelativeLayout r_retry = (RelativeLayout) dialibetg.findViewById(R.id.r_retry);
        TextView txt = (TextView) dialibetg.findViewById(R.id.txt);
        //  txt.setText("Internet is not working.\n" + "Start your Internet and restart app.");

        try {
            timer.cancel();
            handleSplibeth.removeCallbacks(runnableSPlaibetD);
        } catch (Exception e) {

        }

        txt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
                dialibetg.dismiss();

            }
        });
        r_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    timer.cancel();
                    handleSplibeth.removeCallbacks(runnableSPlaibetD);
                } catch (Exception e) {

                }

                if (isNetworkAvailable()) {
                    DBettryplication.isSplashFBettryh = false;
                    try {
                        if (dialibetg != null && dialibetg.isShowing()) {
                            dialibetg.dismiss();
                        }
                    } catch (Exception e) {
                    }
                    startLive();
                    getApiValues();

                } else {
                    Toast.makeText(SplashActivity.this, "Please check your internet connection!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        if (!isFinishing()) {
            dialibetg.show();
        }
    }

    private boolean isNetworkAvailable() {
        try {
            ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = manager.getActiveNetworkInfo();
            boolean isAvailable = false;
            if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                isAvailable = true;
            }
            return isAvailable;
        } catch (Exception e) {
            return false;
        }

    }


    @Override
    protected void onDestroy() {
        try {
            if (handleSplibeth != null) {
                handleSplibeth.removeCallbacks(runnableSPlaibetD);
            }
        } catch (Exception e) {

        }
        try {
            if (timer != null) {
                timer.cancel();
            }
        } catch (Exception e) {
        }
        super.onDestroy();
    }


    @Override
    public void onBackPressed() {
    }
}
