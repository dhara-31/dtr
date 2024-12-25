package com.test.testing12345.adsclass;

import android.app.Activity;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.nativead.MediaView;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.test.testing12345.R;


public class NativeAdsAllKeboa {


    public static Integer nativeCount1Keboa = 0;
    public static Integer nativecCunt2Keboa = 0;

    public static NativeAd nativeAd2Keboa2;
    public static NativeAd nativeAd1Keboa1;


    private static NativeAdsAllKeboa ourInstance;

    public static NativeAdsAllKeboa getInstance() {
        if (ourInstance == null) {
            ourInstance = new NativeAdsAllKeboa();
        }
        return ourInstance;
    }


    public static void banerAllShowKeboa(FrameLayout f, Activity activity, CardView cardView) {
        try {
            AppDetailKeboa detailApp = DKeboaApplication.getInstance().getAppDetail();
            if (detailApp != null && detailApp.getAdstatus().equals("1")) {

                if (nativeAd1Keboa1 != null) {
                    banershownativewithbaner(f, activity, cardView);
                    nativeAd1Keboa1 = null;
                    loadNative1Keboa(activity);
                } else if (nativeAd2Keboa2 != null) {

                    banershownViKeboa(f, activity, cardView);
                    nativeAd2Keboa2 = null;
                    loadNative2Keboa(activity);

                    if (nativeAd1Keboa1 == null) {
                        nativeCount1Keboa++;
                        if (nativeCount1Keboa >= 5) {
                            nativeCount1Keboa = 0;
                            nativeAd1Keboa1 = null;
                            loadNative1Keboa(activity);
                        }
                    }

                } else {

                    bannerDefaultKeboa(activity, f, cardView);

                    if (nativeAd2Keboa2 == null) {
                        nativecCunt2Keboa++;
                        if (nativecCunt2Keboa >= 5) {
                            nativecCunt2Keboa = 0;
                            nativeAd2Keboa2 = null;
                            loadNative2Keboa(activity);
                        }
                    }

                    if (nativeAd1Keboa1 == null) {
                        nativeCount1Keboa++;
                        if (nativeCount1Keboa >= 5) {
                            nativeCount1Keboa = 0;
                            nativeAd1Keboa1 = null;
                            loadNative1Keboa(activity);
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


    public static void banershownativewithbaner(FrameLayout frameLayout, Activity activity, CardView cardView) {
        cardView.setVisibility(View.VISIBLE);
        frameLayout.setVisibility(View.VISIBLE);
        NativeAdView adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.custom_native_banner, null);
        populateNativeAdView(nativeAd1Keboa1, adView);
        frameLayout.removeAllViews();
        frameLayout.addView(adView);
    }

    public static void banershownViKeboa(FrameLayout frameLayout, Activity activity, CardView cardView) {
        cardView.setVisibility(View.VISIBLE);
        frameLayout.setVisibility(View.VISIBLE);
        NativeAdView adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.custom_native_banner, null);
        populateNativeAdView(nativeAd2Keboa2, adView);
        frameLayout.removeAllViews();
        frameLayout.addView(adView);
    }


    public static void bannerDefaultKeboa(Activity activity, FrameLayout f, CardView cardView) {
        AppDetailKeboa appDetail = DKeboaApplication.getInstance().getAppDetail();
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
                    populateNativeAdView(banernativeAd_Default1, adView);
                    f.removeAllViews();
                    f.addView(adView);
                }
            });

            AdLoader adLoader = builder.withAdListener(
                    new AdListener() {
                        @Override
                        public void onAdFailedToLoad(LoadAdError loadAdError) {
                            bannerDefault2Keboa(activity, f, cardView);
                        }
                    })
                    .build();

            adLoader.loadAd(new AdRequest.Builder().build());
        } else {
            bannerDefault2Keboa(activity, f, cardView);
        }
    }


    public static void bannerDefault2Keboa(Activity activity, FrameLayout f, CardView cardView) {
        AppDetailKeboa appDetail = DKeboaApplication.getInstance().getAppDetail();
        if (appDetail != null && appDetail.getAdmob2native() != null && !TextUtils.isEmpty(appDetail.getAdmob2native()) && appDetail.getAdstatus().equals("1")) {
            AdLoader.Builder builder = new AdLoader.Builder(activity, appDetail.getAdmob2native());
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
                    NativeAd banernativeAd_Default2 = null;

                    if (banernativeAd_Default2 != null) {
                        banernativeAd_Default2.destroy();
                    }
                    banernativeAd_Default2 = nativeAd;

                    cardView.setVisibility(View.VISIBLE);
                    f.setVisibility(View.VISIBLE);

                    NativeAdView adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.custom_native_banner, null);
                    populateNativeAdView(banernativeAd_Default2, adView);
                    f.removeAllViews();
                    f.addView(adView);
                }
            });

            AdLoader adLoader = builder.withAdListener(
                    new AdListener() {
                        @Override
                        public void onAdFailedToLoad(LoadAdError loadAdError) {
                            f.setVisibility(View.INVISIBLE);
                            cardView.setVisibility(View.INVISIBLE);
                        }
                    })
                    .build();

            adLoader.loadAd(new AdRequest.Builder().build());
        } else {
            f.setVisibility(View.INVISIBLE);
            cardView.setVisibility(View.INVISIBLE);
        }
    }




    public static void nativeAKeboa(FrameLayout f, Activity activity, CardView cardView) {
        try {
            AppDetailKeboa detailApp = DKeboaApplication.getInstance().getAppDetail();
            if (detailApp != null && detailApp.getAdstatus().equals("1")) {
                if (nativeAd1Keboa1 != null) {
                    showNAtive1Keboa(f, activity, cardView);
                    nativeAd1Keboa1 = null;
                    loadNative1Keboa(activity);
                } else if (nativeAd2Keboa2 != null) {

                    showNative2Keboa(f, activity, cardView);
                    nativeAd2Keboa2 = null;
                    loadNative2Keboa(activity);

                    if (nativeAd1Keboa1 == null) {
                        nativeCount1Keboa++;
                        if (nativeCount1Keboa >= 5) {
                            nativeCount1Keboa = 0;
                            nativeAd1Keboa1 = null;
                            loadNative1Keboa(activity);
                        }
                    }
                } else {
                    loadDefaultNative(f, activity, cardView);


                    if (nativeAd2Keboa2 == null) {
                        nativecCunt2Keboa++;
                        if (nativecCunt2Keboa >= 5) {
                            nativecCunt2Keboa = 0;
                            nativeAd2Keboa2 = null;
                            loadNative2Keboa(activity);
                        }
                    }
                    if (nativeAd1Keboa1 == null) {
                        nativeCount1Keboa++;
                        if (nativeCount1Keboa >= 5) {
                            nativeCount1Keboa = 0;
                            nativeAd1Keboa1 = null;
                            loadNative1Keboa(activity);
                        }
                    }


                }

            } else {
                f.setVisibility(View.INVISIBLE);
            }
        } catch (Exception e) {

        }
    }

    public static void loadNativeBoth(final Activity activity) {
        loadNative1Keboa(activity);
        loadNative2Keboa(activity);
    }


    public static void showNAtive1Keboa(FrameLayout frameLayout, Activity activity, CardView cardView) {
        try {
            cardView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.VISIBLE);
            NativeAdView adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.native_large, null);
            populateNativeAdViewWall(nativeAd1Keboa1, adView);
            frameLayout.removeAllViews();
            frameLayout.addView(adView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showNative2Keboa(FrameLayout frameLayout, Activity activity, CardView cardView) {
        try {
            cardView.setVisibility(View.VISIBLE);
            frameLayout.setVisibility(View.VISIBLE);
            NativeAdView adView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.native_large, null);
            populateNativeAdViewWall(nativeAd2Keboa2, adView);
            frameLayout.removeAllViews();
            frameLayout.addView(adView);
        } catch (Exception e) {
        }
    }

    public static void loadDefaultNative(FrameLayout f, Activity activity, CardView cardView) {
        AppDetailKeboa appDetail = DKeboaApplication.getInstance().getAppDetail();
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
                    populateNativeAdViewWall(nativeDefault, adView);
                    f.removeAllViews();
                    f.addView(adView);
                }
            });

            AdLoader adLoader = builder.withAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(LoadAdError loadAdError) {
                    loadDefaultWall2Keboa2(f, activity, cardView);
                }
            }).build();

            adLoader.loadAd(new AdRequest.Builder().build());
        } else {
            loadDefaultWall2Keboa2(f, activity, cardView);
        }
    }


    public static void populateNativeAdView(NativeAd nativeAd, NativeAdView adView) {

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


    public static void loadDefaultWall2Keboa2(FrameLayout f, Activity activity, CardView cardView) {
        AppDetailKeboa appDetail = DKeboaApplication.getInstance().getAppDetail();
        if (appDetail != null && appDetail.getAdmob2native() != null && !TextUtils.isEmpty(appDetail.getAdmob2native()) && appDetail.getAdstatus().equals("1")) {
            AdLoader.Builder builder = new AdLoader.Builder(activity, appDetail.getAdmob2native());
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
                    populateNativeAdViewWall(nativeDefault, adView);
                    f.removeAllViews();
                    f.addView(adView);
                }
            });

            AdLoader adLoader = builder.withAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(LoadAdError loadAdError) {
                    cardView.setVisibility(View.INVISIBLE);
                }
            }).build();

            adLoader.loadAd(new AdRequest.Builder().build());
        } else {
            f.setVisibility(View.INVISIBLE);
            cardView.setVisibility(View.INVISIBLE);
        }
    }


    public static void loadNative1Keboa(Activity activity) {
        AppDetailKeboa appDetail = DKeboaApplication.getInstance().getAppDetail();
        if (appDetail != null && appDetail.getAdmobnative() != null && !TextUtils.isEmpty(appDetail.getAdmobnative()) && appDetail.getAdstatus().equals("1")) {
            AdLoader.Builder builder = new AdLoader.Builder(activity, appDetail.getAdmobnative());

            builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() {

                @Override
                public void onNativeAdLoaded(NativeAd nativeAd) {
                    if (nativeAd1Keboa1 != null) {
                        nativeAd1Keboa1.destroy();
                    }
                    nativeAd1Keboa1 = null;
                    nativeCount1Keboa = 0;
                    nativeAd1Keboa1 = nativeAd;
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


    public static void loadNative2Keboa(Activity activity) {

        AppDetailKeboa appDetail = DKeboaApplication.getInstance().getAppDetail();
        if (appDetail != null && appDetail.getAdmob2native() != null
                && !TextUtils.isEmpty(appDetail.getAdmob2native()) && appDetail.getAdstatus().equals("1")) {

            AdLoader.Builder builder = new AdLoader.Builder(activity, appDetail.getAdmob2native());

            builder.forNativeAd(new NativeAd.OnNativeAdLoadedListener() {

                @Override
                public void onNativeAdLoaded(NativeAd nativeAd) {

                    if (nativeAd2Keboa2 != null) {
                        nativeAd2Keboa2.destroy();
                    }
                    nativeAd2Keboa2 = null;
                    nativecCunt2Keboa = 0;
                    nativeAd2Keboa2 = nativeAd;
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


    private static void populateNativeAdViewWall(NativeAd nativeAd, NativeAdView adView) {
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
