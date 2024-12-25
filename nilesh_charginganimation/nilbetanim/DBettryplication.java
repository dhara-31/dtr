

package com.si_charginganimation.nilesh_charginganimation.nilbetanim;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.si_charginganimation.nilesh_charginganimation.game.GoChBetryNils;
import com.si_charginganimation.nilesh_charginganimation.model.DBetNilChrAnimopeaippOpen;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class DBettryplication extends Application implements Application.ActivityLifecycleCallbacks {


    public static boolean isSplashFBettryh = false;

    public static String splashscreeBettryme = "SplashActivity";

    public Integer openvaBettryue = 0;

    public Integer intervaBettrye = 0;

    private static DBettryplication ourInstance;

    public SharedPreferences prefereBettryes;
    public static  Boolean inter = true ;
    public static DBettryplication getInstance() {
        return ourInstance;
    }

    public static final String MyPREFERBettryES = "MyAdsPrefs";
    private static final String PREF_APP_DEBettryLS = "app_details", PREF_ADS_DETAILS = "ads_details";

    private ArrayList<DetailAds> detailAds;

    public static DBetNilChrAnimopeaippOpen appOpenManBettryr;

    public static DBettryplication c() {
        return ourInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ourInstance = this;
        registerActivityLifecycleCallbacks(this);

        prefereBettryes = getApplicationContext().getSharedPreferences(MyPREFERBettryES, MODE_PRIVATE);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
    }



    public void initializeOpenVideo() {
        appOpenManBettryr = new DBetNilChrAnimopeaippOpen(this);
    }


    public AppBettry getAppDetail() {
        return new Gson().fromJson(prefereBettryes.getString(PREF_APP_DEBettryLS, ""), AppBettry.class);
    }



    public boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            isAvailable = true;
        }
        return isAvailable;
    }


    public void setAppDetail(AppBettry appDetail) {
        prefereBettryes.edit().putString(PREF_APP_DEBettryLS, new Gson().toJson(appDetail)).apply();
    }

    public ArrayList<DetailAds> getAdsDetails() {
        return new Gson().fromJson(prefereBettryes.getString(PREF_ADS_DETAILS, ""), new TypeToken<ArrayList<DetailAds>>() {
        }.getType());
    }

    public void setAdsDetails(ArrayList<DetailAds> adsDetails) {
        this.detailAds = adsDetails;
        prefereBettryes.edit().putString(PREF_ADS_DETAILS, new Gson().toJson(adsDetails)).apply();
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

        try {


            if (activity != null && !activity.toString().contains("SplashActivity")) {
                if (appOpenManBettryr == null) {
                    appOpenManBettryr = new DBetNilChrAnimopeaippOpen(this);
                    openvaBettryue = 2;
                    intervaBettrye = 2;
                }
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        inter = true ;
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        try {
            inter = false ;
            if (GoChBetryNils.getInstance() != null) {
                if (GoChBetryNils.getInstance().handlAHAbiter != null) {
                    if (GoChBetryNils.getInstance().handlAHAbiter != null) {
                        GoChBetryNils.getInstance().handlAHAbiter.removeCallbacks(GoChBetryNils.getInstance().runnable);
                    }
                }
                if (GoChBetryNils.getInstance().interdialog != null) {
                    GoChBetryNils.getInstance().interdialog.dismiss();
                }

            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }
}
