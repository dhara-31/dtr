package com.test.testing12345.adsclass;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;


public class GogleAsKeboard {


    private AdsInterface callBack;

    public static Integer counterNoramlCallKeboa = 0;

    public InterstitialAd mInterstitialKeboa;

    private static GogleAsKeboard ourInstanceKeboa;

    private InterstitialAd mInter2;

    public static Integer counter_InterKeboa = 0;
    public static Integer countmInterstitialAd_Keboa2 = 0;

    private long mLastClickTime_Keboa2 = 0;
    private long mLastClickTime2_Keboa1 = 0;

    private AdView AdViewBanner;


    private AdSize getAdSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);
    }


    @SuppressLint("MissingPermission")
    public void ShowBanner(Activity activity, FrameLayout banner) {
        AppDetailKeboa detailApp = DKeboaApplication.getInstance().getAppDetail();
        if (detailApp != null && detailApp.getAdmobbanner() != null && !TextUtils.isEmpty(detailApp.getAdmobbanner()) && detailApp.getAdstatus().equalsIgnoreCase("1")) {
            AdViewBanner = new AdView(activity);
            AdViewBanner.setAdSize(getAdSize(activity));
            AdViewBanner.setAdUnitId(detailApp.getAdmobbanner());
            AdRequest adRequest = new AdRequest.Builder().build();
            AdViewBanner.loadAd(adRequest);

            AdViewBanner.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    banner.addView(AdViewBanner);
                    super.onAdLoaded();
                }

                @Override
                public void onAdFailedToLoad(LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    loadABanner2Keboa(activity, banner);
                }
            });

        } else {
            loadABanner2Keboa(activity, banner);
        }
    }

    public void loadABanner2Keboa(Activity activity, FrameLayout banner) {
        AppDetailKeboa detailApp = DKeboaApplication.getInstance().getAppDetail();
        if (detailApp != null && detailApp.getAdmob2banner() != null && !TextUtils.isEmpty(detailApp.getAdmob2banner())
                && detailApp.getAdstatus().equalsIgnoreCase("1")) {
            AdView AdViewBanner2 = new AdView(activity);
            AdViewBanner2.setAdSize(getAdSize(activity));
            AdViewBanner2.setAdUnitId(detailApp.getAdmob2banner());
            AdRequest adRequest = new AdRequest.Builder().build();
            AdViewBanner2.loadAd(adRequest);
            AdViewBanner2.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {

                    banner.addView(AdViewBanner2);
                    super.onAdLoaded();
                }

                @Override
                public void onAdFailedToLoad(LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    banner.setVisibility(View.INVISIBLE);

                }
            });
        } else {
            banner.setVisibility(View.INVISIBLE);
        }
    }


    public interface AdsInterface {
        void adsCall();
    }

    public static GogleAsKeboard getInstance() {
        if (ourInstanceKeboa == null) {
            ourInstanceKeboa = new GogleAsKeboard();
        }
        return ourInstanceKeboa;
    }


    public void showInterKeboa(Activity context, AdsInterface _myCallback) {

        if (SystemClock.elapsedRealtime() - mLastClickTime_Keboa2 < 700) {
            return;
        }

        mLastClickTime_Keboa2 = SystemClock.elapsedRealtime();
        this.callBack = _myCallback;

        try {

            counterNoramlCallKeboa = counterNoramlCallKeboa + 1;
            AppDetailKeboa appDetail = DKeboaApplication.getInstance().getAppDetail();

            if (appDetail != null) {

                this.callBack = _myCallback;

                if (SplashActivity.valueinterKeboard == 0) {
                    if (mInterstitialKeboa != null) {
                        mInterstitialKeboa.show(context);
                        counterNoramlCallKeboa = 0;
                        SplashActivity.valueinterKeboard = 1;
                    } else if (mInter2 != null) {
                        mInter2.show(context);
                        counterNoramlCallKeboa = 0;
                        SplashActivity.valueinterKeboard = 1;
                    } else {

                        if (mInter2 == null) {
                            if (mInterstitialKeboa == null) {
                                countmInterstitialAd_Keboa2++;
                                if (countmInterstitialAd_Keboa2 >= 2) {
                                    SplashActivity.valueinterKeboard = 1;
                                    countmInterstitialAd_Keboa2 = 0;
                                    mInter2 = null;

                                    if (DKeboaApplication.c().intervalue != 2) {
                                        loadAd2Keboa(context);
                                    }
                                }
                            }
                        }

                        if (callBack != null) {
                            callBack.adsCall();
                            callBack = null;
                        }
                    }

                } else {

                    if (DKeboaApplication.c().intervalue == 2) {
                        if (appDetail != null && appDetail.getAdmobinter() != null && !TextUtils.isEmpty(appDetail.getAdmobinter()) && appDetail.getAdstatus().equals("1")) {
                            if (mInterstitialKeboa == null) {
                                loadAdKeboa(context);
                            }
                        } else if (appDetail != null && appDetail.getAdmob2interstitial() != null && !TextUtils.isEmpty(appDetail.getAdmob2interstitial()) && appDetail.getAdstatus().equals("1")) {
                            if (mInter2 == null) {
                                loadAd2Keboa(context);
                            }
                        }
                        DKeboaApplication.c().intervalue = 1;
                    }


                    if (appDetail.getCounter() != null && Integer.parseInt(appDetail.getCounter()) <= 1) {
                        counterNoramlCallKeboa = Integer.parseInt(appDetail.getCounter()) + 2;
                    }


                    if (counterNoramlCallKeboa >= Integer.parseInt(appDetail.getCounter())) {

                        if (mInterstitialKeboa != null) {
                            mInterstitialKeboa.show(context);
                            counterNoramlCallKeboa = 0;
                        } else if (mInter2 != null) {
                            counterNoramlCallKeboa = 0;
                            mInter2.show(context);
                            countmInterstitialAd_Keboa2 = 0;

                            if (mInterstitialKeboa == null) {
                                counter_InterKeboa++;
                                if (counter_InterKeboa >= 5) {
                                    counter_InterKeboa = 0;
                                    mInterstitialKeboa = null;
                                    loadAdKeboa(context);
                                }
                            }
                        } else {

                            if (callBack != null) {
                                callBack.adsCall();
                                callBack = null;
                            }

                            if (mInterstitialKeboa == null) {
                                counter_InterKeboa++;
                                if (counter_InterKeboa >= 5) {
                                    counter_InterKeboa = 0;
                                    mInterstitialKeboa = null;
                                    loadAdKeboa(context);
                                }
                            }

                            if (mInter2 == null) {
                                countmInterstitialAd_Keboa2++;
                                if (countmInterstitialAd_Keboa2 >= 5) {
                                    countmInterstitialAd_Keboa2 = 0;
                                    mInter2 = null;
                                    loadAd2Keboa(context);
                                }
                            }
                        }

                    } else {
                        if (callBack != null) {
                            callBack.adsCall();
                            callBack = null;
                        }
                    }
                }

            } else {

                if (callBack != null) {
                    callBack.adsCall();
                    callBack = null;
                }
            }
        } catch (Exception e) {
            if (callBack != null) {
                callBack.adsCall();
                callBack = null;
            }
        }
    }


    public void showInterBackPressKeboa(Activity context, AdsInterface _myCallback) {

        if (SystemClock.elapsedRealtime() - mLastClickTime2_Keboa1 < 700) {
            return;
        }
        mLastClickTime2_Keboa1 = SystemClock.elapsedRealtime();
        this.callBack = _myCallback;

        try {


            AppDetailKeboa appDetail = DKeboaApplication.getInstance().getAppDetail();

            if (appDetail != null && appDetail.getInterstitialbackpress() != null && appDetail.getInterstitialbackpress().equals("1") && appDetail.getCounter() != null && !TextUtils.isEmpty(appDetail.getCounter())) {

                counterNoramlCallKeboa = counterNoramlCallKeboa + 1;

                this.callBack = _myCallback;

                if (counterNoramlCallKeboa >= Integer.parseInt(appDetail.getCounter())) {
                    if (mInterstitialKeboa != null) {
                        mInterstitialKeboa.show(context);
                        counterNoramlCallKeboa = 0;
                    } else if (mInter2 != null) {
                        counterNoramlCallKeboa = 0;
                        mInter2.show(context);
                        countmInterstitialAd_Keboa2 = 0;

                        if (mInterstitialKeboa == null) {
                            counter_InterKeboa++;
                            if (counter_InterKeboa >= 5) {
                                counter_InterKeboa = 0;
                                mInterstitialKeboa = null;
                                loadAdKeboa(context);
                            }
                        }
                    } else {

                        if (callBack != null) {
                            callBack.adsCall();
                            callBack = null;
                        }


                        if (mInterstitialKeboa == null) {
                            counter_InterKeboa++;
                            if (counter_InterKeboa >= 5) {
                                counter_InterKeboa = 0;
                                mInterstitialKeboa = null;
                                loadAdKeboa(context);
                            }
                        }
                        if (mInter2 == null) {
                            countmInterstitialAd_Keboa2++;
                            if (countmInterstitialAd_Keboa2 >= 5) {
                                countmInterstitialAd_Keboa2 = 0;
                                mInter2 = null;
                                loadAd2Keboa(context);
                            }
                        }

                    }

                } else {
                    if (callBack != null) {
                        callBack.adsCall();
                        callBack = null;
                    }
                }


            } else {
                if (callBack != null) {
                    callBack.adsCall();
                    callBack = null;
                }
            }
        } catch (Exception e) {
            if (callBack != null) {
                callBack.adsCall();
                callBack = null;
            }
        }
    }


    public void loadAdKeboa(final Activity context) {
        AdRequest adRequest = new AdRequest.Builder().build();
        AppDetailKeboa detailApp = DKeboaApplication.getInstance().getAppDetail();
        if (detailApp != null) {
            if (detailApp != null && detailApp.getAdstatus().equalsIgnoreCase("1") && detailApp.getAdmobinter() != null && !TextUtils.isEmpty(detailApp.getAdmobinter())) {
                InterstitialAd.load(context, detailApp.getAdmobinter(), adRequest, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialKeboa = interstitialAd;
                        counter_InterKeboa = 0;

                        interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {

                                mInterstitialKeboa = null;
                                loadAdKeboa(context);
                                if (callBack != null) {
                                    callBack.adsCall();
                                    callBack = null;
                                }
                                SplashActivity.appopenKeboard = true;
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                mInterstitialKeboa = null;
                                SplashActivity.appopenKeboard = true;
                                if (callBack != null) {
                                    callBack.adsCall();
                                    callBack = null;
                                }
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                counter_InterKeboa = 0;
                                SplashActivity.appopenKeboard = false;
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mInterstitialKeboa = null;
                    }
                });
            }
        }
    }

    public void loadAd2Keboa(final Activity context) {
        AdRequest adRequest = new AdRequest.Builder().build();
        AppDetailKeboa detailApp = DKeboaApplication.getInstance().getAppDetail();

        if (detailApp != null) {
            if (detailApp != null && detailApp.getAdstatus().equalsIgnoreCase("1") && detailApp.getAdmob2interstitial() != null && !TextUtils.isEmpty(detailApp.getAdmob2interstitial())) {

                InterstitialAd.load(context, detailApp.getAdmob2interstitial(), adRequest,
                        new InterstitialAdLoadCallback() {
                            @Override
                            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                                mInter2 = interstitialAd;
                                countmInterstitialAd_Keboa2 = 0;

                                interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {


                                        mInter2 = null;
                                        loadAd2Keboa(context);

                                        if (callBack != null) {
                                            callBack.adsCall();
                                            callBack = null;
                                        }
                                        SplashActivity.appopenKeboard = true;
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        mInter2 = null;
                                        SplashActivity.appopenKeboard = true;
                                        if (callBack != null) {
                                            callBack.adsCall();
                                            callBack = null;
                                        }
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        countmInterstitialAd_Keboa2 = 0;
                                        SplashActivity.appopenKeboard = false;
                                    }
                                });
                            }

                            @Override
                            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                                mInter2 = null;
                            }
                        });
            }

        }
    }


}
