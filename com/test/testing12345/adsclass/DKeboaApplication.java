

package com.test.testing12345.adsclass;

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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class DKeboaApplication extends Application implements Application.ActivityLifecycleCallbacks {


    public static boolean isSplashFinissh = false;

    public static String splashscreenname = "SplashActivity";

    public Integer openvalue = 0;

    public Integer intervalue = 0;

    private static DKeboaApplication ourInstance;

    public SharedPreferences preferences;

    public static DKeboaApplication getInstance() {
        return ourInstance;
    }


    public static final String MyPREFERENCES = "MyAdsPrefs";
    private static final String PREF_APP_DETAILS = "app_details", PREF_ADS_DETAILS = "ads_details";

    private ArrayList<DetailAds> detailAds;

    public static DKeboaAppOpen appOpenManager;

    public static DKeboaApplication c() {
        return ourInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ourInstance = this;
        registerActivityLifecycleCallbacks(this);

        preferences = getApplicationContext().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
    }


    public void initializeOpenVideo() {
        appOpenManager = new DKeboaAppOpen(this);
    }


    public AppDetailKeboa getAppDetail() {
        return new Gson().fromJson(preferences.getString(PREF_APP_DETAILS, ""), AppDetailKeboa.class);
    }


    public boolean isConAvailable(int timeOut) {
        InetAddress inetAddress = null;
        try {
            Future<InetAddress> future = Executors.newSingleThreadExecutor().submit(new Callable<InetAddress>() {
                @Override
                public InetAddress call() {
                    try {
                        return InetAddress.getByName("google.com");
                    } catch (UnknownHostException e) {
                        return null;
                    }
                }
            });
            inetAddress = future.get(timeOut, TimeUnit.MILLISECONDS);
            future.cancel(true);
        } catch (InterruptedException e) {
        } catch (ExecutionException e) {
        } catch (TimeoutException e) {
        }
        return inetAddress != null && !inetAddress.equals("");
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


    public void setAppDetail(AppDetailKeboa appDetail) {
        preferences.edit().putString(PREF_APP_DETAILS, new Gson().toJson(appDetail)).apply();
    }

    public ArrayList<DetailAds> getAdsDetails() {
        return new Gson().fromJson(preferences.getString(PREF_ADS_DETAILS, ""), new TypeToken<ArrayList<DetailAds>>() {
        }.getType());
    }

    public void setAdsDetails(ArrayList<DetailAds> adsDetails) {
        this.detailAds = adsDetails;
        preferences.edit().putString(PREF_ADS_DETAILS, new Gson().toJson(adsDetails)).apply();
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

        try {


            if (activity != null && !activity.toString().contains("SplashActivity")) {
                if (appOpenManager == null) {
                    appOpenManager = new DKeboaAppOpen(this);
                    openvalue = 2;
                    intervalue = 2;
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
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

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
