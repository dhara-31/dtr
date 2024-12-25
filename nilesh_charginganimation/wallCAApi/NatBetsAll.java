package com.si_charginganimation.nilesh_charginganimation.wallCAApi;

import android.app.Activity;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.si_charginganimation.nilesh_charginganimation.R;
import com.si_charginganimation.nilesh_charginganimation.nilbetanim.AppBettry;
import com.si_charginganimation.nilesh_charginganimation.nilbetanim.DBettryplication;

public class NatBetsAll {


    public static Integer natiBets1 = 0;
    public static NativeAd nativBets1;


    private static NatBetsAll ourInsBetsce;

    public static NatBetsAll getInstance() {
        if (ourInsBetsce == null) {
            ourInsBetsce = new NatBetsAll();
        }
        return ourInsBetsce;
    }


    private AdView ABetsewBanner;


    public static void natVolBetsl(FrameLayout f, Activity activity, CardView cardView, FrameLayout admobNative_Banner, CardView nativesmallcard) {

        try {
            AppBettry detailApp = DBettryplication.getInstance().getAppDetail();
            if (detailApp != null && detailApp.getBignative() != null && !TextUtils.isEmpty(detailApp.getBignative()) && detailApp.getBignative().equals("1")) {
                admobNative_Banner.setVisibility(View.GONE);
                nativesmallcard.setVisibility(View.GONE);
                natiBetshowAll(f, activity, cardView);
            } else {
                admobNative_Banner.setVisibility(View.VISIBLE);
                nativesmallcard.setVisibility(View.VISIBLE);
                banaernatBetse(admobNative_Banner, activity, nativesmallcard, false);
            }
        } catch (Exception e) {

        }
    }

    public void natVolBetsl(Activity activity, FrameLayout f, RelativeLayout baner, FrameLayout nativeBanner, CardView nativesmallcard) {
        try {
            AppBettry detailApp = DBettryplication.getInstance().getAppDetail();
            if (detailApp != null && detailApp.getBannertonative() != null && !TextUtils.isEmpty(detailApp.getBannertonative()) && detailApp.getBannertonative().equals("1")) {

                nativeBanner.setVisibility(View.VISIBLE);
                nativesmallcard.setVisibility(View.VISIBLE);
                baner.setVisibility(View.GONE);
                f.setVisibility(View.GONE);
                banaernatBetse(nativeBanner, activity, nativesmallcard, false);

            } else {
                nativeBanner.setVisibility(View.GONE);
                nativesmallcard.setVisibility(View.GONE);
                ShowBanBetsr(activity, f, baner);
            }
        } catch (Exception e) {

        }
    }


    public void ShowBanBetsr(Activity activity, FrameLayout banner, RelativeLayout baner) {

        AppBettry detailApp = DBettryplication.getInstance().getAppDetail();

        if (detailApp != null && detailApp.getAdmobbanner() != null && !TextUtils.isEmpty(detailApp.getAdmobbanner()) && detailApp.getAdstatus().equalsIgnoreCase("1")) {
            ABetsewBanner = new AdView(activity);
            ABetsewBanner.setAdSize(getAdSize(activity));
            ABetsewBanner.setAdUnitId(detailApp.getAdmobbanner());
            AdRequest adRequest = new AdRequest.Builder().build();
            ABetsewBanner.loadAd(adRequest);

            ABetsewBanner.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    baner.setVisibility(View.VISIBLE);
                    banner.addView(ABetsewBanner);
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


    private AdSize getAdSize(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);
    }


    public static void banaernatBetse(FrameLayout f, Activity activity, CardView cardView, Boolean bo) {
        try {

            AppBettry detailApp = DBettryplication.getInstance().getAppDetail();

            if (detailApp != null && detailApp.getAdstatus().equals("1")) {

                if (nativBets1 != null) {
                    banershownatiBetsll(f, activity, cardView);
                    nativBets1 = null;
                    loadNatBets1(activity);
                } else {

                    bannerDefaBetst(activity, f, cardView);

                    if (nativBets1 == null) {
                        natiBets1++;
                        if (natiBets1 >= 5) {
                            natiBets1 = 0;
                            nativBets1 = null;
                            loadNatBets1(activity);
                        }
                    }


                }

            } else {
                f.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void banershownatiBetsll(FrameLayout frameLayout, Activity activity, CardView cardView) {
        cardView.setVisibility(View.VISIBLE);
        frameLayout.setVisibility(View.VISIBLE);
        NativeAdView adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.custom_native_banner, null);
        populateNatiBetsw(nativBets1, adView);
        frameLayout.removeAllViews();
        frameLayout.addView(adView);
    }


    public static void bannerDefaBetst(Activity activity, FrameLayout f, CardView cardView) {
        AppBettry appDetail = DBettryplication.getInstance().getAppDetail();
        if (appDetail != null && appDetail.getAdmobnative() != null && !TextUtils.isEmpty(appDetail.getAdmobnative()) && appDetail.getAdstatus().equals("1")) {
            AdLoader.Builder builder = new AdLoader.Builder(activity, appDetail.getAdmobnative());
            builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() {

                @Override
                public void onNativeAdLoaded(NativeAd nativeAd) {
                    boolean isDestroyed = false;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        isDestroyed = activity.isDestroyed();
                    }
                    if (isDestroyed || activity.isFinishing() || activity.isChangingConfigurations()) {
                        nativeAd.destroy();
                        return;
                    }

                    NativeAd banernativeAd_Default1 = null;

                    if (banernativeAd_Default1 != null) {
                        banernativeAd_Default1.destroy();
                    }
                    banernativeAd_Default1 = nativeAd;

                    cardView.setVisibility(View.VISIBLE);
                    f.setVisibility(View.VISIBLE);

                    NativeAdView adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.custom_native_banner, null);
                    populateNatiBetsw(banernativeAd_Default1, adView);
                    f.removeAllViews();
                    f.addView(adView);
                }
            });

            AdLoader adLoader = builder.withAdListener(
                    new AdListener() {
                        @Override
                        public void onAdFailedToLoad(LoadAdError loadAdError) {

                        }
                    })
                    .build();

            adLoader.loadAd(new AdRequest.Builder().build());
        } else {

        }
    }


    public static void natiBetshowAll(FrameLayout f, Activity activity, CardView cardView) {
        try {
            AppBettry detailApp = DBettryplication.getInstance().getAppDetail();
            if (detailApp != null && detailApp.getAdstatus().equals("1")) {
                if (nativBets1 != null) {
                    showNAtBetse1(f, activity, cardView);
                    nativBets1 = null;
                    loadNatBets1(activity);
                } else {
                    loadDefaultBetsve(f, activity, cardView);

                    if (nativBets1 == null) {
                        natiBets1++;
                        if (natiBets1 >= 5) {
                            natiBets1 = 0;
                            nativBets1 = null;
                            loadNatBets1(activity);
                        }
                    }


                }

            } else {
                f.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {

        }
    }

    public static void loadNatiBetsh(final Activity activity) {
        loadNatBets1(activity);
    }


    public static void showNAtBetse1(FrameLayout frameLayout, Activity activity, CardView cardView) {
        try {
            cardView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.VISIBLE);
            NativeAdView adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.native_large, null);
            populateNaBetsl(nativBets1, adView);
            frameLayout.removeAllViews();
            frameLayout.addView(adView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void loadDefaultBetsve(FrameLayout f, Activity activity, CardView cardView) {
        AppBettry appDetail = DBettryplication.getInstance().getAppDetail();
        if (appDetail != null && appDetail.getAdmobnative() != null && !TextUtils.isEmpty(appDetail.getAdmobnative()) && appDetail.getAdstatus().equals("1")) {
            AdLoader.Builder builder = new AdLoader.Builder(activity, appDetail.getAdmobnative());
            builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() {

                @Override
                public void onNativeAdLoaded(NativeAd nativeAd) {
                    boolean isDestroyed = false;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        isDestroyed = activity.isDestroyed();
                    }
                    if (isDestroyed || activity.isFinishing() || activity.isChangingConfigurations()) {
                        nativeAd.destroy();
                        return;
                    }
                    cardView.setVisibility(View.VISIBLE);
                    f.setVisibility(View.VISIBLE);


                    NativeAd nativeDefault = null;
                    if (nativeDefault != null) {
                        nativeDefault.destroy();
                    }

                    nativeDefault = nativeAd;

                    NativeAdView adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.native_large, null);
                    populateNaBetsl(nativeDefault, adView);
                    f.removeAllViews();
                    f.addView(adView);
                }
            });

            AdLoader adLoader = builder.withAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(LoadAdError loadAdError) {

                }
            }).build();

            adLoader.loadAd(new AdRequest.Builder().build());
        } else {

        }
    }


    public static void populateNatiBetsw(NativeAd nativeAd, NativeAdView adView) {

        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }
        adView.setNativeAd(nativeAd);
        VideoController vc = nativeAd.getMediaContent().getVideoController();

        if (vc.hasVideoContent()) {
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    super.onVideoEnd();
                }
            });
        } else {
        }
    }


    public static void loadNatBets1(Activity activity) {

        if (activity.toString().contains("ActivityExit")) {
            return;
        }

        AppBettry appDetail = DBettryplication.getInstance().getAppDetail();
        if (appDetail != null && appDetail.getAdmobnative() != null && !TextUtils.isEmpty(appDetail.getAdmobnative()) && appDetail.getAdstatus().equals("1")) {
            AdLoader.Builder builder = new AdLoader.Builder(activity, appDetail.getAdmobnative());

            builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() {

                @Override
                public void onNativeAdLoaded(NativeAd nativeAd) {
                    if (nativBets1 != null) {
                        nativBets1.destroy();
                    }
                    nativBets1 = null;
                    natiBets1 = 0;
                    nativBets1 = nativeAd;
                }
            });

            AdLoader adLoader = builder.withAdListener(
                    new AdListener() {
                        @Override
                        public void onAdFailedToLoad(LoadAdError loadAdError) {
                        }
                    })
                    .build();

            adLoader.loadAd(new AdRequest.Builder().build());
        }
    }


    private static void populateNaBetsl(NativeAd nativeAd, NativeAdView adView) {
        adView.setMediaView((MediaView) adView.findViewById(R.id.ad_media));
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));


        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }


        adView.setNativeAd(nativeAd);


        VideoController vc = nativeAd.getMediaContent().getVideoController();

        if (vc.hasVideoContent()) {

            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    super.onVideoEnd();
                }
            });
        } else {
        }
    }
}
