package com.test.testing12345.adsclass;


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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


import com.test.testing12345.R;
import com.test.testing12345.activity.IntroScreen1CsActivity;
import com.test.testing12345.activity.MainCsActivity;
import com.test.testing12345.activity.StartCsActivity;

import retrofit2.Call;
import retrofit2.Callback;


public class SplashActivity extends AppCompatActivity {


    public static Handler handleSplashKeboard;
    public static Runnable runnableSPlasjKeboard;
    CountDownTimer timer;
    int timerScreen = 11000;
    public DKeboaApplication myAppWKeboard = DKeboaApplication.getInstance();
    boolean checkResumedLive = true;
    boolean overtimerKeboard = false;
    public Dialog dialog;
    public static Boolean appopenKeboard = true;

    public static Boolean value = false;
    public static Integer valueinterKeboard = 0;
    public static Boolean exit = false;


    Animation animation1;

    ImageView imageView2;

    StoreageCkPref storeageCkPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DKeboaAppOpen.firstFlag = true;
        setContentView(R.layout.activity_splash);

        imageView2 = findViewById(R.id.imageView2);

        exit = false;

        valueinterKeboard = 0;

        storeageCkPref = new StoreageCkPref(this);

        value = false;
        DKeboaApplication.isSplashFinissh = false;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        DKeboaAppOpen.SplashActivity = SplashActivity.this;


        animation1 = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        imageView2.startAnimation(animation1);

        if (isNetworkAvailable()) {
            try {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            } catch (Exception ignored) {
            }

            getApiValues();
            startLive();
        } else {

            if (DKeboaAppOpen.appOpenAd == null && DKeboaAppOpen.a2Keboa == null) {
                InternetDialogUpdate();
            }
        }
    }


    public void getApiValues() {

        Call<ResponseApp> call = ApiClientd.getClient().create(ApiInKeboaterfaceAd.class).getAll(getPackageName());

        call.enqueue(new Callback<ResponseApp>() {
            @Override
            public void onResponse(Call<ResponseApp> call, retrofit2.Response<ResponseApp> response) {
                if (response.isSuccessful()) {


                    myAppWKeboard.setAppDetail(response.body().getAppdetail());
                    myAppWKeboard.setAdsDetails(response.body().getAdsdetail());

                    if (myAppWKeboard.getAppDetail().getAdstatus().equalsIgnoreCase("2")) {
                        StartMainScreen();
                    } else if (myAppWKeboard.getAppDetail().getAdstatus().equals("1")) {

                        if (myAppWKeboard.getAppDetail() != null && myAppWKeboard.getAppDetail().getAdmobnew() != null && !TextUtils.isEmpty(myAppWKeboard.getAppDetail().getAdmobnew())) {

                            DKeboaAppOpen.AD_UNITKeboa = myAppWKeboard.getAppDetail().getAdmobnew();

                            myAppWKeboard.initializeOpenVideo();

                            myAppWKeboard.splashscreenname = "SplashActivity";
                            DKeboaAppOpen.SplashActivity = SplashActivity.this;

                            DKeboaApplication.appOpenManager.firstFlag = true;

                            if (DKeboaAppOpen.appOpenAd == null) {
                                DKeboaApplication.appOpenManager.getOpenKeboa();
                            }

                        } else if (myAppWKeboard.getAppDetail() != null && myAppWKeboard.getAppDetail().getAdmob2appopen() != null
                                && !TextUtils.isEmpty(myAppWKeboard.getAppDetail().getAdmob2appopen())) {

                            DKeboaAppOpen.AD_UNIT2Keboa2 = myAppWKeboard.getAppDetail().getAdmob2appopen();
                            myAppWKeboard.initializeOpenVideo();

                            myAppWKeboard.splashscreenname = "SplashActivity";
                            DKeboaAppOpen.SplashActivity = SplashActivity.this;
                            DKeboaApplication.appOpenManager.firstFlag = true;

                            if (DKeboaAppOpen.a2Keboa == null) {
                                DKeboaApplication.appOpenManager.getLoadOpenAKeboa2();
                            }
                        } else {
                            StartMainScreen();
                        }
                    }

                } else {
                    StartMainScreen();
                }
            }

            @Override
            public void onFailure(Call<ResponseApp> call, Throwable t) {
                StartMainScreen();
            }
        });
    }


    public void startLive() {

        overtimerKeboard = false;
        runnableSPlasjKeboard = new Runnable() {
            @Override
            public void run() {
                overtimerKeboard = true;
                DKeboaApplication.isSplashFinissh = true;

                if (isNetworkAvailable()) {
                    try {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    } catch (Exception e) {
                    }

                    if (checkResumedLive) {


                        if (exit == false) {

                            value = true;

                            DKeboaApplication.isSplashFinissh = true;
                            AppDetailKeboa appDetail = DKeboaApplication.getInstance().getAppDetail();
                            if (appDetail != null && appDetail.getCounter() != null && !TextUtils.isEmpty(appDetail.getCounter())) {

                                if (Integer.parseInt(appDetail.getCounter()) > 1) {
                                    if (appDetail != null && appDetail.getAdmobinter() != null && !TextUtils.isEmpty(appDetail.getAdmobinter()) && appDetail.getAdstatus().equals("1")) {
                                        GogleAsKeboard.getInstance().loadAdKeboa(SplashActivity.this);

                                    } else if (appDetail != null && appDetail.getAdmob2interstitial() != null && !TextUtils.isEmpty(appDetail.getAdmob2interstitial()) && appDetail.getAdstatus().equals("1")) {
                                        GogleAsKeboard.getInstance().loadAd2Keboa(SplashActivity.this);
                                    }
                                } else {
                                    GogleAsKeboard.getInstance().loadAdKeboa(SplashActivity.this);
                                    GogleAsKeboard.getInstance().loadAd2Keboa(SplashActivity.this);
                                }
                            }

                            DKeboaApplication.appOpenManager.firstFlag = false;

                            try {
                                if (handleSplashKeboard != null) {
                                    handleSplashKeboard.removeCallbacks(runnableSPlasjKeboard);
                                }
                            } catch (Exception e) {

                            }


                            NativeAdsAllKeboa.getInstance().loadNativeBoth(SplashActivity.this);

                            if (myAppWKeboard.getAppDetail() != null && myAppWKeboard.getAppDetail().getAppscreennumber() != null && !TextUtils.isEmpty(myAppWKeboard.getAppDetail().getAppscreennumber()) && myAppWKeboard.getAppDetail().getAppscreennumber().equals("1")) {
                                startActivity(new Intent(SplashActivity.this, StartCsActivity.class));
                                finish();
                            } else {
                                if (storeageCkPref.getAPP_FIRST()) {
                                    Intent intent = new Intent(SplashActivity.this, IntroScreen1CsActivity.class);
                                    startActivity(intent);
                                } else {
                                    Intent intent = new Intent(SplashActivity.this, MainCsActivity.class);
                                    startActivity(intent);
                                }
                            }

                        }

                    }
                } else {
                    DKeboaApplication.isSplashFinissh = true;
                    try {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    } catch (Exception e) {
                    }
                    InternetDialogUpdate();
                }
            }
        };

        try {
            if (handleSplashKeboard != null) {
                handleSplashKeboard.removeCallbacks(runnableSPlasjKeboard);
            }
        } catch (Exception e) {

        }


        try {
            if (timer != null) {
                timer.cancel();
            }
        } catch (Exception e) {

        }

        handleSplashKeboard = new Handler();

        handleSplashKeboard.postDelayed(runnableSPlasjKeboard, timerScreen);

        timer = new CountDownTimer(timerScreen, 100) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
            }

        }.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        checkResumedLive = false;
    }

    private void StartMainScreen() {

        DKeboaApplication.appOpenManager.firstFlag = false;
        if (myAppWKeboard.getAppDetail() != null && myAppWKeboard.getAppDetail().getAppscreennumber() != null && !TextUtils.isEmpty(myAppWKeboard.getAppDetail().getAppscreennumber()) && myAppWKeboard.getAppDetail().getAppscreennumber().equals("1")) {
            startActivity(new Intent(SplashActivity.this, StartCsActivity.class));
            finish();
        } else {
            if (storeageCkPref.getAPP_FIRST()) {
                Intent intent = new Intent(SplashActivity.this, IntroScreen1CsActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(SplashActivity.this, MainCsActivity.class);
                startActivity(intent);
            }
        }

    }


    @Override
    protected void onResume() {
        checkResumedLive = true;
        super.onResume();

    }


    public void InternetDialogUpdate() {
        dialog = new Dialog(this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.requestWindowFeature(1);
        dialog.setContentView(R.layout.net_connection);
        dialog.setCancelable(false);
        RelativeLayout txt_yes = (RelativeLayout) dialog.findViewById(R.id.yes);
        RelativeLayout r_retry = (RelativeLayout) dialog.findViewById(R.id.r_retry);
        TextView txt = (TextView) dialog.findViewById(R.id.txt);
        //  txt.setText("Internet is not working.\n" + "Start your Internet and restart app.");

        try {
            timer.cancel();
            handleSplashKeboard.removeCallbacks(runnableSPlasjKeboard);
        } catch (Exception e) {

        }

        txt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
                dialog.dismiss();

            }
        });
        r_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    timer.cancel();
                    handleSplashKeboard.removeCallbacks(runnableSPlasjKeboard);
                } catch (Exception e) {

                }

                if (isNetworkAvailable()) {
                    DKeboaApplication.isSplashFinissh = false;
                    try {
                        if (dialog != null && dialog.isShowing()) {
                            dialog.dismiss();
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
            dialog.show();
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
            if (handleSplashKeboard != null) {
                handleSplashKeboard.removeCallbacks(runnableSPlasjKeboard);
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
