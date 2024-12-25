package com.si_charginganimation.nilesh_charginganimation.model;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.appopen.AppOpenAd;
import com.si_charginganimation.nilesh_charginganimation.act.MainAct;
import com.si_charginganimation.nilesh_charginganimation.act.StartAct;
import com.si_charginganimation.nilesh_charginganimation.nilbetanim.DBettryplication;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static androidx.lifecycle.Lifecycle.Event.ON_START;
import static com.si_charginganimation.nilesh_charginganimation.nilbetanim.SplashActivity.screennumibetr;

public class DBetNilChrAnimopeaippOpen implements LifecycleObserver, Application.ActivityLifecycleCallbacks {

    public static String AD_UNIT = "";


    public static boolean doNotDisplApChBetryNilllon = false;
    private static boolean isShowingAd = false;

    private final DBettryplication myApplication;
    public static boolean firstFlag = false;

    public static AppOpenAd appOpenAd = null;

    private Activity ruuningActivity;

    public static Activity SplashActivity;
    private AppOpenAd.AppOpenAdLoadCallback loadCallback;

    private long loadTimeLite = 0;
    private Integer failcount = 0;


    public DBetNilChrAnimopeaippOpen(DBettryplication myApplication) {
        this.myApplication = myApplication;
        this.myApplication.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }


    public void getOpenAds() {

        if (isAdAvailable()) {
            return;
        }

        if (!doNotDisplApChBetryNilllon) {

            loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull AppOpenAd ad) {
                    super.onAdLoaded(appOpenAd);
                    failcount = 0;
                    DBetNilChrAnimopeaippOpen.this.appOpenAd = ad;
                    DBetNilChrAnimopeaippOpen.this.loadTimeLite = (new Date()).getTime();

                    if (firstFlag) {
                        if (!DBettryplication.isSplashFBettryh) {
                            showAdIfAvailable();
                            DBettryplication.isSplashFBettryh = true;
                        }
                    }
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                }
            };
            AdRequest request = getAdRequest();
            AppOpenAd.load(myApplication, AD_UNIT, request, loadCallback);

        } else {
            doNotDisplApChBetryNilllon = false;
        }
    }


    private AdRequest getAdRequest() {
        return new AdRequest.Builder().build();
    }


    private boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
        long dateDifference = (new Date()).getTime() - this.loadTimeLite;
        long numMilliSecondsPerHour = 3600000;
        return (dateDifference < (numMilliSecondsPerHour * numHours));
    }

    public boolean isAdAvailable() {
        return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4);
    }


    public void showAdIfAvailable() {
        if (!doNotDisplApChBetryNilllon) {
            if (!isShowingAd && isAdAvailable()) {
                if (com.si_charginganimation.nilesh_charginganimation.nilbetanim.SplashActivity.appopenD == true) {

                    isShowingAd = true;
                    FullScreenContentCallback fullScreenContentCallback = new FullScreenContentCallback() {

                        @Override
                        public void onAdDismissedFullScreenContent() {
                            DBetNilChrAnimopeaippOpen.this.appOpenAd = null;

                            isShowingAd = false;

                            if (firstFlag) {
                                firstFlag = false;
                                if (com.si_charginganimation.nilesh_charginganimation.nilbetanim.SplashActivity.vaibete == false) {
                                    com.si_charginganimation.nilesh_charginganimation.nilbetanim.SplashActivity.vaibete = true;
                                    openNextScreenPhoto();

                                    try {
                                        if (com.si_charginganimation.nilesh_charginganimation.nilbetanim.SplashActivity.handleSplibeth != null) {
                                            com.si_charginganimation.nilesh_charginganimation.nilbetanim.SplashActivity.handleSplibeth.removeCallbacks(com.si_charginganimation.nilesh_charginganimation.nilbetanim.SplashActivity.runnableSPlaibetD);
                                        }
                                    } catch (Exception e) {
                                    }

                                }
                            }

                            if (myApplication.getAppDetail() != null && myApplication.getAppDetail().getAdmobappopen() != null
                                    && !TextUtils.isEmpty(myApplication.getAppDetail().getAdmobappopen())) {
                                getOpenAds();
                            }

                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            isShowingAd = false;
                            if (com.si_charginganimation.nilesh_charginganimation.nilbetanim.SplashActivity.vaibete == false) {
                                com.si_charginganimation.nilesh_charginganimation.nilbetanim.SplashActivity.vaibete = true;
                                openNextScreenPhoto();
                            }
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            isShowingAd = true;
                        }
                    };


                    appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
                    appOpenAd.show(ruuningActivity);
                }
            } else {

                failcount++;
                if (appOpenAd == null) {

                    if (DBettryplication.getInstance().openvaBettryue == 2) {
                        if (myApplication.getAppDetail() != null && myApplication.getAppDetail().getAdmobappopen() != null
                                && !TextUtils.isEmpty(myApplication.getAppDetail().getAdmobappopen())) {

                            DBetNilChrAnimopeaippOpen.AD_UNIT = myApplication.getAppDetail().getAdmobappopen();
                            getOpenAds();
                            failcount = 0;
                            DBettryplication.getInstance().openvaBettryue = 1;
                        }


                    } else {

                        if (failcount > 3) {
                            if (myApplication.getAppDetail() != null && myApplication.getAppDetail().getAdmobappopen() != null
                                    && !TextUtils.isEmpty(myApplication.getAppDetail().getAdmobappopen())) {

                                getOpenAds();
                                failcount = 0;
                            }
                        }
                    }
                }
            }
        } else {
            doNotDisplApChBetryNilllon = false;
        }
    }

    private void openNextScreenPhoto() {
        firstFlag = false;
        if (internetConnectionAvailable(1500) && isNetworkAvailable()) {
            SplashActivity = null;

            if (ruuningActivity != null) {

                if (screennumibetr >= 1) {
                    ruuningActivity.startActivity(new Intent(ruuningActivity, StartAct.class));
                    ruuningActivity.finish();
                } else {
                    ruuningActivity.startActivity(new Intent(ruuningActivity, MainAct.class));
                    ruuningActivity.finish();
                }
            }

        } else {
            if (SplashActivity != null) {
                if (screennumibetr >= 1) {
                    SplashActivity.startActivity(new Intent(SplashActivity, StartAct.class));
                    SplashActivity.finish();
                } else {
                    SplashActivity.startActivity(new Intent(SplashActivity, MainAct.class));
                    SplashActivity.finish();

                }
            }
        }
    }


    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) myApplication.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }
        return isAvailable;
    }


    private boolean internetConnectionAvailable(int timeOut) {
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

    @OnLifecycleEvent(ON_START)
    public void onStart() {
        showAdIfAvailable();
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
        if (activity.toString().contains(DBettryplication.splashscreeBettryme)) {
            ruuningActivity = activity;
        } else {
            isShowingAd = false;
        }
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        if (!activity.toString().contains(DBettryplication.splashscreeBettryme)) {
            ruuningActivity = activity;
        }
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        ruuningActivity = null;
    }
}
