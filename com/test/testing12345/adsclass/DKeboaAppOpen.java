package com.test.testing12345.adsclass;

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
import com.test.testing12345.activity.IntroScreen1CsActivity;
import com.test.testing12345.activity.MainCsActivity;
import com.test.testing12345.activity.StartCsActivity;


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

public class DKeboaAppOpen implements LifecycleObserver, Application.ActivityLifecycleCallbacks {

    public static String AD_UNITKeboa = "";
    public static String AD_UNIT2Keboa2 = "";

    public static boolean doNotDisplayAdKeboa = false;
    private static boolean isShowingAdKeboa = false;

    private final DKeboaApplication myApplication;
    public static boolean firstFlag = false;

    public static AppOpenAd appOpenAd = null;

    public static AppOpenAd a2Keboa = null;
    private Activity ruuningActivityKeboa;

    public static Activity SplashActivity;
    private AppOpenAd.AppOpenAdLoadCallback loadCallback;

    private long loadTimeLite = 0;
    private Integer failcount = 0;

    StoreageCkPref storeageCkPref;

    public DKeboaAppOpen(DKeboaApplication myApplication) {
        this.myApplication = myApplication;
        this.myApplication.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);


    }


    public void getLoadOpenAKeboa2() {

        if (isAdAvailable2()) {
            return;
        }

        if (!doNotDisplayAdKeboa) {
            loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull AppOpenAd ad) {
                    super.onAdLoaded(a2Keboa);

                    DKeboaAppOpen.this.a2Keboa = ad;

                    DKeboaAppOpen.this.loadTimeLite = (new Date()).getTime();

                    if (firstFlag) {

                        if (!DKeboaApplication.isSplashFinissh) {
                            showAdIfAvailable();

                            DKeboaApplication.isSplashFinissh = true;


                            AppDetailKeboa appDetail = DKeboaApplication.getInstance().getAppDetail();

                            if (appDetail != null && appDetail.getCounter() != null && !TextUtils.isEmpty(appDetail.getCounter())) {

                                if (Integer.parseInt(appDetail.getCounter()) > 1) {

                                    if (appDetail != null && appDetail.getAdmobinter() != null && !TextUtils.isEmpty(appDetail.getAdmobinter()) && appDetail.getAdstatus().equals("1")) {
                                        GogleAsKeboard.getInstance().loadAdKeboa(SplashActivity);
                                    } else if (appDetail != null && appDetail.getAdmob2interstitial() != null && !TextUtils.isEmpty(appDetail.getAdmob2interstitial()) && appDetail.getAdstatus().equals("1")) {
                                        GogleAsKeboard.getInstance().loadAd2Keboa(SplashActivity);
                                    }
                                } else {
                                    GogleAsKeboard.getInstance().loadAdKeboa(SplashActivity);
                                    GogleAsKeboard.getInstance().loadAd2Keboa(SplashActivity);
                                }
                            }

                            NativeAdsAllKeboa.getInstance().loadNativeBoth(SplashActivity);
                        }
                    }
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);

                }
            };
            AdRequest request = getAdRequest();
            AppOpenAd.load(myApplication, AD_UNIT2Keboa2, request, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);

        } else {
            doNotDisplayAdKeboa = false;
        }
    }


    public void getOpenKeboa() {

        if (isAdAvailable()) {
            return;
        }

        if (!doNotDisplayAdKeboa) {
            loadCallback = new AppOpenAd.AppOpenAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull AppOpenAd ad) {
                    super.onAdLoaded(appOpenAd);
                    failcount = 0;
                    DKeboaAppOpen.this.appOpenAd = ad;
                    DKeboaAppOpen.this.loadTimeLite = (new Date()).getTime();

                    if (firstFlag) {
                        if (!DKeboaApplication.isSplashFinissh) {

                            showAdIfAvailable();
                            DKeboaApplication.isSplashFinissh = true;

                            AppDetailKeboa appDetail = DKeboaApplication.getInstance().getAppDetail();

                            if (appDetail != null && appDetail.getCounter() != null && !TextUtils.isEmpty(appDetail.getCounter())) {
                                if (Integer.parseInt(appDetail.getCounter()) > 1) {
                                    if (appDetail != null && appDetail.getAdmobinter() != null && !TextUtils.isEmpty(appDetail.getAdmobinter()) && appDetail.getAdstatus().equals("1")) {
                                        GogleAsKeboard.getInstance().loadAdKeboa(SplashActivity);
                                    } else if (appDetail != null && appDetail.getAdmob2interstitial() != null && !TextUtils.isEmpty(appDetail.getAdmob2interstitial()) && appDetail.getAdstatus().equals("1")) {
                                        GogleAsKeboard.getInstance().loadAd2Keboa(SplashActivity);
                                    }
                                } else {
                                    GogleAsKeboard.getInstance().loadAdKeboa(SplashActivity);
                                    GogleAsKeboard.getInstance().loadAd2Keboa(SplashActivity);
                                }
                            }
                            NativeAdsAllKeboa.getInstance().loadNativeBoth(SplashActivity);


                        }
                    }
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);

                    DKeboaAppOpen.AD_UNIT2Keboa2 = myApplication.getAppDetail().getAdmob2appopen();
                    if (appOpenAd == null) {
                        myApplication.splashscreenname = "SplashActivity";

                        if (a2Keboa == null) {
                            if (myApplication.getAppDetail() != null && myApplication.getAppDetail().getAdmob2appopen() != null
                                    && !TextUtils.isEmpty(myApplication.getAppDetail().getAdmob2appopen())) {
                                getLoadOpenAKeboa2();
                            }
                        }
                    }
                }
            };
            AdRequest request = getAdRequest();

            AppOpenAd.load(myApplication, AD_UNITKeboa, request, AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT, loadCallback);

        } else {
            doNotDisplayAdKeboa = false;
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

    public boolean isAdAvailable2() {
        return a2Keboa != null && wasLoadTimeLessThanNHoursAgo(4);
    }


    public void showAdIfAvailable() {

        if (!doNotDisplayAdKeboa) {
            if (!isShowingAdKeboa && isAdAvailable()) {
                if (com.test.testing12345.adsclass.SplashActivity.appopenKeboard == true) {

                    isShowingAdKeboa = true;
                    FullScreenContentCallback fullScreenContentCallback = new FullScreenContentCallback() {

                        @Override
                        public void onAdDismissedFullScreenContent() {
                            DKeboaAppOpen.this.appOpenAd = null;

                            isShowingAdKeboa = false;

                            if (firstFlag) {

                                firstFlag = false;
                                if (com.test.testing12345.adsclass.SplashActivity.value == false) {
                                    com.test.testing12345.adsclass.SplashActivity.value = true;
                                    openNextScreenPKeboa();

                                    try {
                                        if (com.test.testing12345.adsclass.SplashActivity.handleSplashKeboard != null) {
                                            com.test.testing12345.adsclass.SplashActivity.handleSplashKeboard.removeCallbacks(com.test.testing12345.adsclass.SplashActivity.runnableSPlasjKeboard);
                                        }
                                    } catch (Exception e) {
                                    }

                                }
                            }

                            if (myApplication.getAppDetail() != null && myApplication.getAppDetail().getAdmobnew() != null
                                    && !TextUtils.isEmpty(myApplication.getAppDetail().getAdmobnew())) {
                                getOpenKeboa();
                            }

                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            isShowingAdKeboa = false;
                            if (com.test.testing12345.adsclass.SplashActivity.value == false) {
                                com.test.testing12345.adsclass.SplashActivity.value = true;
                                openNextScreenPKeboa();
                            }
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            isShowingAdKeboa = true;
                        }
                    };


                    appOpenAd.setFullScreenContentCallback(fullScreenContentCallback);
                    appOpenAd.show(ruuningActivityKeboa);
                }
            } else if (!isShowingAdKeboa && isAdAvailable2()) {

                if (com.test.testing12345.adsclass.SplashActivity.appopenKeboard == true) {

                    isShowingAdKeboa = true;
                    FullScreenContentCallback fullScreenContentCallback = new FullScreenContentCallback() {

                        @Override
                        public void onAdDismissedFullScreenContent() {
                            DKeboaAppOpen.this.a2Keboa = null;

                            isShowingAdKeboa = false;

                            if (firstFlag) {
                                firstFlag = false;

                                if (com.test.testing12345.adsclass.SplashActivity.value == false) {
                                    com.test.testing12345.adsclass.SplashActivity.value = true;
                                    openNextScreenPKeboa();

                                    try {
                                        if (com.test.testing12345.adsclass.SplashActivity.handleSplashKeboard != null) {
                                            com.test.testing12345.adsclass.SplashActivity.handleSplashKeboard.removeCallbacks(com.test.testing12345.adsclass.SplashActivity.runnableSPlasjKeboard);
                                        }
                                    } catch (Exception e) {
                                    }
                                }
                            }

                            if (appOpenAd == null) {

                                if (myApplication.getAppDetail() != null && myApplication.getAppDetail().getAdmobnew() != null
                                        && !TextUtils.isEmpty(myApplication.getAppDetail().getAdmobnew())) {
                                } else {
                                    if (a2Keboa == null) {
                                        if (!firstFlag) {
                                            if (myApplication.getAppDetail() != null && myApplication.getAppDetail().getAdmob2appopen() != null && !TextUtils.isEmpty(myApplication.getAppDetail().getAdmob2appopen())) {
                                                getLoadOpenAKeboa2();
                                            }
                                        }
                                    }
                                }

                                failcount++;
                                if (failcount > 0) {
                                    failcount = 0;
                                    if (myApplication.getAppDetail() != null && myApplication.getAppDetail().getAdmobnew() != null
                                            && !TextUtils.isEmpty(myApplication.getAppDetail().getAdmobnew())) {
                                        DKeboaAppOpen.this.a2Keboa = null;
                                        getOpenKeboa();
                                    }

                                }
                            }
                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            if (com.test.testing12345.adsclass.SplashActivity.value == false) {
                                com.test.testing12345.adsclass.SplashActivity.value = true;
                                openNextScreenPKeboa();
                            }

                        }

                        @Override
                        public void onAdShowedFullScreenContent() {

                            isShowingAdKeboa = true;
                        }
                    };
                    a2Keboa.setFullScreenContentCallback(fullScreenContentCallback);
                    a2Keboa.show(ruuningActivityKeboa);
                }
            } else {

                failcount++;
                if (appOpenAd == null && a2Keboa == null) {

                    if (DKeboaApplication.getInstance().openvalue == 2) {
                        if (myApplication.getAppDetail() != null && myApplication.getAppDetail().getAdmobnew() != null
                                && !TextUtils.isEmpty(myApplication.getAppDetail().getAdmobnew())) {

                            DKeboaAppOpen.AD_UNITKeboa = myApplication.getAppDetail().getAdmobnew();
                            getOpenKeboa();
                            failcount = 0;
                            DKeboaApplication.getInstance().openvalue = 1;
                        } else if (myApplication.getAppDetail() != null && myApplication.getAppDetail().getAdmob2appopen() != null
                                && !TextUtils.isEmpty(myApplication.getAppDetail().getAdmob2appopen())) {
                            DKeboaAppOpen.AD_UNIT2Keboa2 = myApplication.getAppDetail().getAdmob2appopen();
                            getLoadOpenAKeboa2();
                            failcount = 0;
                            DKeboaApplication.getInstance().openvalue = 1;
                        }


                    } else {

                        if (failcount > 3) {
                            if (myApplication.getAppDetail() != null && myApplication.getAppDetail().getAdmobnew() != null
                                    && !TextUtils.isEmpty(myApplication.getAppDetail().getAdmobnew())) {

                                getOpenKeboa();
                                failcount = 0;
                            }
                        }
                    }


                }
            }
        } else {
            doNotDisplayAdKeboa = false;
        }
    }

    private void openNextScreenPKeboa() {
        firstFlag = false;
        if (internetConnectionAvailable(1500) && isNetworkAvailable()) {
            SplashActivity = null;

            if (ruuningActivityKeboa != null) {


                if (myApplication.getAppDetail() != null && myApplication.getAppDetail().getAppscreennumber() != null && !TextUtils.isEmpty(myApplication.getAppDetail().getAppscreennumber()) && myApplication.getAppDetail().getAppscreennumber().equals("1")) {
                    ruuningActivityKeboa.startActivity(new Intent(ruuningActivityKeboa, StartCsActivity.class));
                    ruuningActivityKeboa.finish();
                } else {
                    storeageCkPref = new StoreageCkPref(ruuningActivityKeboa);
                    if (storeageCkPref.getAPP_FIRST()) {
                        Intent intent = new Intent(ruuningActivityKeboa, IntroScreen1CsActivity.class);
                        ruuningActivityKeboa.startActivity(intent);
                    } else {
                        Intent intent = new Intent(ruuningActivityKeboa, MainCsActivity.class);
                        ruuningActivityKeboa.startActivity(intent);
                    }
                }

            }

        } else {
            if (SplashActivity != null) {

                if (myApplication.getAppDetail() != null && myApplication.getAppDetail().getAppscreennumber() != null && !TextUtils.isEmpty(myApplication.getAppDetail().getAppscreennumber()) && myApplication.getAppDetail().getAppscreennumber().equals("1")) {
                    SplashActivity.startActivity(new Intent(SplashActivity, StartCsActivity.class));
                    SplashActivity.finish();
                } else {
                    storeageCkPref = new StoreageCkPref(SplashActivity);
                    if (storeageCkPref.getAPP_FIRST()) {
                        Intent intent = new Intent(SplashActivity, IntroScreen1CsActivity.class);
                        SplashActivity.startActivity(intent);
                    } else {
                        Intent intent = new Intent(SplashActivity, MainCsActivity.class);
                        SplashActivity.startActivity(intent);
                    }
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
        if (activity.toString().contains(DKeboaApplication.splashscreenname)) {
            ruuningActivityKeboa = activity;
        } else {
            isShowingAdKeboa = false;
        }
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        if (!activity.toString().contains(DKeboaApplication.splashscreenname)) {
            ruuningActivityKeboa = activity;
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
        ruuningActivityKeboa = null;
    }
}
