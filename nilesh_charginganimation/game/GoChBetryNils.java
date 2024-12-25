package com.si_charginganimation.nilesh_charginganimation.game;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

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
import com.si_charginganimation.nilesh_charginganimation.R;
import com.si_charginganimation.nilesh_charginganimation.nilbetanim.AppBettry;
import com.si_charginganimation.nilesh_charginganimation.nilbetanim.DBettryplication;
import com.si_charginganimation.nilesh_charginganimation.nilbetanim.SplashActivity;


public class GoChBetryNils {

    private AChBetryNilInterface callBack;
    public static Integer counterNoramlCall = 0;
    public InterstitialAd mInterstitialAd1;
    private static GoChBetryNils ourInstance;

    public static Integer counter_Inter = 0;
    private long mLastClickTime1 = 0;
    private int secondtimer;
    private AdView AdViewBanner;
    public static Handler handlAHAbiter = new Handler();
    public static Runnable runnable;
    public static Dialog interdialog;

    private AdSize getAdSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);
    }


    public void ShowBanner(Activity activity, FrameLayout banner, RelativeLayout baner) {

        AppBettry detailApp = DBettryplication.getInstance().getAppDetail();

        if (detailApp != null && detailApp.getAdmobbanner() != null && !TextUtils.isEmpty(detailApp.getAdmobbanner()) && detailApp.getAdstatus().equalsIgnoreCase("1")) {

            AdViewBanner = new AdView(activity);
            AdViewBanner.setAdSize(getAdSize(activity));
            AdViewBanner.setAdUnitId(detailApp.getAdmobbanner());
            AdRequest adRequest = new AdRequest.Builder().build();
            AdViewBanner.loadAd(adRequest);

            AdViewBanner.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    baner.setVisibility(View.VISIBLE);
                    banner.addView(AdViewBanner);
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


    public interface AChBetryNilInterface {
        void aChBetryNilsCall();
    }

    public static GoChBetryNils getInstance() {
        if (ourInstance == null) {
            ourInstance = new GoChBetryNils();
        }
        return ourInstance;
    }


    public void showChBetryNilster(Activity context, AChBetryNilInterface _myCallback) {

        if (SystemClock.elapsedRealtime() - mLastClickTime1 < 700) {
            return;
        }

        mLastClickTime1 = SystemClock.elapsedRealtime();
        this.callBack = _myCallback;

        try {

            counterNoramlCall = counterNoramlCall + 1;
            AppBettry appDetail = DBettryplication.getInstance().getAppDetail();

            if (appDetail != null) {

                this.callBack = _myCallback;

                if (SplashActivity.valueibet == 0) {
                    if (mInterstitialAd1 != null) {
                        mInterstitialAd1.show(context);
                        counterNoramlCall = 0;
                        SplashActivity.valueibet = 1;
                    } else {

                        SplashActivity.valueibet = 1;
                        if (callBack != null) {
                            callBack.aChBetryNilsCall();
                            callBack = null;
                        }
                    }

                } else {

                    if (appDetail.getCounter() != null && Integer.parseInt(appDetail.getCounter()) <= 1) {
                        counterNoramlCall = Integer.parseInt(appDetail.getCounter()) + 2;
                    }

                    if (counterNoramlCall >= Integer.parseInt(appDetail.getCounter())) {

                        secondtimer = 0;

                        if (mInterstitialAd1 != null) {
                            mInterstitialAd1.show(context);
                            counterNoramlCall = 0;
                        } else {
                            if (mInterstitialAd1 == null) {
                                if (DBettryplication.getInstance() != null && DBettryplication.getInstance().isNetworkAvailable()) {

                                    loChBetryNilsa(context);

                                    try {
                                        interdialog = new Dialog(context);
                                        interdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        interdialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                                        interdialog.setContentView(R.layout.loadinter);
                                        interdialog.show();

                                        handlAHAbiter.postDelayed(runnable = new Runnable() {
                                            @Override
                                            public void run() {
                                                secondtimer++;
                                                if (mInterstitialAd1 != null) {
                                                    SplashActivity.appopenD = false;


                                                    if (interdialog != null) {
                                                        interdialog.dismiss();
                                                    }
                                                    if (DBettryplication.inter == true) {
                                                        mInterstitialAd1.show(context);
                                                    }
                                                    counterNoramlCall = 0;
                                                    if (handlAHAbiter != null) {
                                                        handlAHAbiter.removeCallbacks(runnable);
                                                    }
                                                } else {
                                                    if (secondtimer < 5) {
                                                        handlAHAbiter.postDelayed(runnable, 1000);
                                                    } else {

                                                        if (interdialog != null) {
                                                            interdialog.dismiss();
                                                        }

                                                        if (mInterstitialAd1 != null) {
                                                            mInterstitialAd1.show(context);
                                                            counterNoramlCall = 0;
                                                        } else {
                                                            if (callBack != null) {
                                                                callBack.aChBetryNilsCall();
                                                                callBack = null;
                                                            }
                                                        }
                                                        if (handlAHAbiter != null) {
                                                            handlAHAbiter.removeCallbacks(runnable);
                                                        }

                                                    }
                                                }
                                            }
                                        }, 1000);

                                    } catch (Exception e) {
                                        if (callBack != null) {
                                            callBack.aChBetryNilsCall();
                                            callBack = null;
                                        }
                                    }
                                }

                                else {
                                    if (callBack != null) {
                                        callBack.aChBetryNilsCall();
                                        callBack = null;
                                    }

                                }
                            } else {
                                if (callBack != null) {
                                    callBack.aChBetryNilsCall();
                                    callBack = null;
                                }
                            }


                        }

                    } else {
                        if (callBack != null) {
                            callBack.aChBetryNilsCall();
                            callBack = null;
                        }
                    }
                }

            } else {

                if (callBack != null) {
                    callBack.aChBetryNilsCall();
                    callBack = null;
                }
            }
        } catch (Exception e) {
            if (callBack != null) {
                callBack.aChBetryNilsCall();
                callBack = null;
            }
        }
    }


    public void loChBetryNilsa(final Activity context) {
        AdRequest adRequest = new AdRequest.Builder().build();
        AppBettry detailApp = DBettryplication.getInstance().getAppDetail();
        if (detailApp != null) {
            if (detailApp != null && detailApp.getAdstatus().equalsIgnoreCase("1") && detailApp.getAdmobinter() != null && !TextUtils.isEmpty(detailApp.getAdmobinter())) {
                InterstitialAd.load(context, detailApp.getAdmobinter(), adRequest, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd1 = interstitialAd;
                        counter_Inter = 0;

                        interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdDismissedFullScreenContent() {

                                mInterstitialAd1 = null;


                                if (callBack != null) {
                                    callBack.aChBetryNilsCall();
                                    callBack = null;
                                }
                                SplashActivity.appopenD = true;
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                mInterstitialAd1 = null;
                                SplashActivity.appopenD = true;
                                if (callBack != null) {
                                    callBack.aChBetryNilsCall();
                                    callBack = null;
                                }
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                counter_Inter = 0;
                                SplashActivity.appopenD = false;
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mInterstitialAd1 = null;
                    }
                });
            }
        }
    }


}
