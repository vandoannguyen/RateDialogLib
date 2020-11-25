package com.example.adsadmobandfan.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.adsadmobandfan.R;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdIconView;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import java.util.ArrayList;
import java.util.List;

public class AdsUtils {
    private static final String TAG = "AdsUtils";
    Activity activity;
    private static AdsUtils adsUtils;
    private NativeAd nativeAdFan;
    private UnifiedNativeAd nativeAdmob;
    private com.google.android.gms.ads.InterstitialAd interAdmob;
    private InterstitialAd interFan;
    private boolean isClickInterAdmobCache = false, isClickInterFanCache = false, isLoadingFan = false;

    public static AdsUtils getInstance(Activity context) {
        if (adsUtils != null) {
            adsUtils.activity = context;
        }
        adsUtils = new AdsUtils(context);
        return adsUtils;
    }

    public void onDestroy() {
        if (nativeAdmob != null)
            nativeAdmob.destroy();
        if (nativeAdFan != null)
            nativeAdFan.destroy();
    }

    private AdsUtils(Activity activity) {
        this.activity = activity;
    }

    public void bannerAdMob(ViewGroup frameAds, String idBanner, com.google.android.gms.ads.AdSize bannerSize) {
        com.google.android.gms.ads.AdView adView = new com.google.android.gms.ads.AdView(activity);
        adView.setAdUnitId(idBanner);
        adView.setAdSize(bannerSize);
        adView.loadAd(new AdRequest.Builder().build());
        frameAds.removeAllViews();
        frameAds.addView(adView);
    }

    /**
     * @param frameAds
     * @param idBanner
     * @param bannerSize
     * @param callError  Standard Banner
     *                   BANNER_50
     *                   320x50
     *                   This banner is best suited to phones
     *                   Large Banner
     *                   BANNER_90
     *                   320x90
     *                   This banner is best suited to tablets and larger devices
     *                   Medium Rectangle
     *                   RECTANGLE_HEIGHT_250
     *                   300x250
     *                   This format is best suited for scrollable feeds or end-of-level screens
     */
    public void bannerFan(ViewGroup frameAds, String idBanner, AdSize bannerSize, FanNativeCallBack callError) {
        AdView adView = new AdView(activity, idBanner, bannerSize);
        adView.setAdListener(new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                callError.onError();
            }

            @Override
            public void onAdLoaded(Ad ad) {

            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        });
        adView.loadAd();
        frameAds.removeAllViews();
        frameAds.addView(adView);
    }

    public void nativeAdmob(final FrameLayout frameLayout, final int layout, final com.google.android.gms.ads.AdListener adListener, String id_inter) {
        UnifiedNativeAd nativeAd;
        AdLoader.Builder builder = new AdLoader.Builder(activity, id_inter).withAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                adListener.onAdClosed();
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                adListener.onAdLeftApplication();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                adListener.onAdOpened();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                adListener.onAdClicked();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                adListener.onAdImpression();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                adListener.onAdFailedToLoad(errorCode);
//                refresh.setEnabled(true);
                if (frameLayout != null) frameLayout.removeAllViews();
//                frameLayout.setVisibility(View.GONE);
//                Toast.makeText(context, "Failed to load native ad: "
//                        + errorCode, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                adListener.onAdLoaded();
            }
        });

        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
//                FrameLayout frameLayout =
//                        findViewById(idFrrame);
                if (activity.isDestroyed()) {
                    unifiedNativeAd.destroy();
                    return;
                }
                // You must call destroy on old ads when you are done with them,
                // otherwise you will have a memory leak.
                if (nativeAdmob != null) {
                    nativeAdmob.destroy();
                }
                nativeAdmob = unifiedNativeAd;
                UnifiedNativeAdView adView = (UnifiedNativeAdView) activity.getLayoutInflater()
                        .inflate(layout, null);
                populateUnifiedNativeAdView(unifiedNativeAd, adView, layout);
                frameLayout.removeAllViews();
                frameLayout.addView(adView);
            }

        });
        VideoOptions videoOptions = new VideoOptions.Builder()
//                .setStartMuted(startVideoAdsMuted.isChecked())
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        builder.withNativeAdOptions(adOptions);
        AdLoader adLoader = builder.build();
        adLoader.loadAd(new PublisherAdRequest.Builder().build());
//        videoStatus.setText("");
    }

    public void nativeFan(ViewGroup frameAds, String idNative, int layout, FanNativeCallBack callError) {
        nativeAdFan = new NativeAd(activity, idNative);
        nativeAdFan.setAdListener(new NativeAdListener() {

            @Override
            public void onMediaDownloaded(Ad ad) {
                if (nativeAdFan == null || nativeAdFan != ad) {
                    return;
                }
                inflateAd(activity, nativeAdFan, frameAds, layout);
                callError.onSuccess();
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                if (callError != null) {
                    callError.onError();
                }
            }

            @Override
            public void onAdLoaded(Ad ad) {
                if (nativeAdFan == null || nativeAdFan != ad) {
                    return;
                }
                inflateAd(activity, nativeAdFan, frameAds, layout);
                callError.onSuccess();
            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        });
        nativeAdFan.loadAd();
    }

    public void interAdmob(String idInter, com.google.android.gms.ads.AdListener listener) {
//        if (interAdmob == null) {
        com.google.android.gms.ads.InterstitialAd interstitialAd = new com.google.android.gms.ads.InterstitialAd(activity);
        interstitialAd.setAdUnitId(idInter);
//        }

        interstitialAd.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                listener.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                listener.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                listener.onAdLeftApplication();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                listener.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                listener.onAdLoaded();
                interstitialAd.show();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                listener.onAdClicked();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                listener.onAdImpression();
            }
        });
        interstitialAd.loadAd(new AdRequest.Builder().build());
    }

    public void interAdmobCache(String idInter, com.google.android.gms.ads.AdListener listener) {
        if (interAdmob == null) {
            interAdmob = new com.google.android.gms.ads.InterstitialAd(activity);
            interAdmob.setAdUnitId(idInter);
        }
        if (interAdmob.isLoading())
            isClickInterAdmobCache = true;
        if (interAdmob.isLoaded()) {
            interAdmob.show();
        }
        interAdmob.setAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                isClickInterAdmobCache = false;
                interAdmob.loadAd(new AdRequest.Builder().build());
                if (listener != null)
                    listener.onAdClosed();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                if (isClickInterAdmobCache) {
                    if (listener != null)
                        listener.onAdFailedToLoad(i);
                }
                interAdmob.loadAd(new AdRequest.Builder().build());
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                if (listener != null)
                    listener.onAdLeftApplication();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                if (listener != null)
                    listener.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                if (listener != null)
                    listener.onAdLoaded();
                if (isClickInterAdmobCache)
                    interAdmob.show();
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                if (listener != null)
                    listener.onAdClicked();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                if (listener != null)
                    listener.onAdImpression();
            }
        });
        if (!interAdmob.isLoading())
            interAdmob.loadAd(new AdRequest.Builder().build());
    }

    public void interFan(String idInter, InterstitialAdListener interstitialAdListener) {
        InterstitialAd interstitialAd = new InterstitialAd(activity, idInter);
        interstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                interstitialAdListener.onInterstitialDisplayed(ad);
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                interstitialAdListener.onInterstitialDismissed(ad);
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                interstitialAdListener.onError(ad, adError);
            }

            @Override
            public void onAdLoaded(Ad ad) {
                interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                interstitialAdListener.onAdClicked(ad);
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                interstitialAdListener.onLoggingImpression(ad);
            }
        });
        interstitialAd.loadAd();
    }

    public void interFanCache(String idInter, InterstitialAdListener interstitialAdListener) {

        if (interFan == null) {
            interFan = new InterstitialAd(activity, idInter);
        }
        if (isLoadingFan) {
            isClickInterFanCache = true;
        }
        if (interFan.isAdLoaded()) {
            interFan.show();
        }
        interFan.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                if (interstitialAdListener != null)
                    interstitialAdListener.onInterstitialDisplayed(ad);
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                isClickInterFanCache = false;
                if (interstitialAdListener != null)
                    interstitialAdListener.onInterstitialDismissed(ad);
                interFan.loadAd();
                isLoadingFan = true;
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                if (isClickInterFanCache)
                    if (interstitialAdListener != null)
                        interstitialAdListener.onError(ad, adError);
                interFan.loadAd();
                isLoadingFan = true;
            }

            @Override
            public void onAdLoaded(Ad ad) {
                isLoadingFan = false;
                if (isClickInterFanCache)
                    interFan.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                if (interstitialAdListener != null)
                    interstitialAdListener.onAdClicked(ad);
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                if (interstitialAdListener != null)
                    interstitialAdListener.onLoggingImpression(ad);
            }
        });
        if (!isLoadingFan) {
            isLoadingFan = true;
            interFan.loadAd();
        }
    }

    private void inflateAd(Context context, NativeAd nativeAd, ViewGroup frmads, int layout) {
        nativeAd.unregisterView();
        NativeAdLayout nativeAdLayout = new NativeAdLayout(context);
        // Add the Ad view into the ad container.
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
        View adView = (LinearLayout) inflater.inflate(layout, nativeAdLayout, false);
        nativeAdLayout.addView(adView);

        // Add the AdOptionsView
        LinearLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(context, nativeAd, nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        // Create native UI using the ad metadata.
        AdIconView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
        MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

        // Create a list of clickable views
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);
        clickableViews.add(nativeAdMedia);
        clickableViews.add(nativeAdIcon);
        clickableViews.add(nativeAdBody);
        // Register the Title and CTA button to listen for clicks.
        nativeAd.registerViewForInteraction(
                adView,
                nativeAdMedia,
                nativeAdIcon,
                clickableViews);
        Log.e(TAG, "inflateAd: " + frmads.getChildCount());
        frmads.removeAllViews();
        frmads.addView(nativeAdLayout);
    }

    private void populateUnifiedNativeAdView(UnifiedNativeAd unifiedNativeAd, UnifiedNativeAdView adView, int layout) {
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setMediaView((com.google.android.gms.ads.formats.MediaView) adView.findViewById(R.id.ad_media));
//        adView.setMediaView(null);
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        if (adView.getPriceView() != null) {
            if (unifiedNativeAd.getPrice() == null || unifiedNativeAd.getPrice().isEmpty())
                adView.getPriceView().setVisibility(View.GONE);
            else {
                ((TextView) adView.getPriceView()).setText(unifiedNativeAd.getPrice());
                if (unifiedNativeAd.getPrice().equals("0"))
                    adView.getPriceView().setVisibility(View.GONE);
            }
        }
        if (adView.getStoreView() != null) {
            if (unifiedNativeAd.getStore() == null || unifiedNativeAd.getStore().isEmpty())
                adView.getStoreView().setVisibility(View.GONE);
            else {
                ((TextView) adView.getStoreView()).setText(unifiedNativeAd.getStore());
            }
        }
        if (adView.getIconView() != null) {
            if (unifiedNativeAd.getIcon() == null)
                adView.getIconView().setVisibility(View.GONE);
            else {
                adView.getIconView().setVisibility(View.VISIBLE);
                ((ImageView) adView.getIconView()).setImageDrawable(unifiedNativeAd.getIcon().getDrawable());
            }
        }
        if (adView.getHeadlineView() != null) {
            if (unifiedNativeAd.getHeadline() == null || unifiedNativeAd.getHeadline().isEmpty())
                adView.getHeadlineView().setVisibility(View.GONE);
            else {
                adView.getHeadlineView().setVisibility(View.VISIBLE);
                ((TextView) adView.getHeadlineView()).setText(unifiedNativeAd.getHeadline());
            }
        }
        if (adView.getAdvertiserView() != null) {
            if (unifiedNativeAd.getAdvertiser() == null || unifiedNativeAd.getAdvertiser().isEmpty())
                adView.getAdvertiserView().setVisibility(View.GONE);
            else {
                adView.getAdvertiserView().setVisibility(View.VISIBLE);
                ((TextView) adView.getAdvertiserView()).setText(unifiedNativeAd.getAdvertiser());
            }
        }
        if (adView.getBodyView() != null) {
            if (unifiedNativeAd.getBody() == null || unifiedNativeAd.getBody().isEmpty())
                adView.getBodyView().setVisibility(View.GONE);
            else {
                adView.getBodyView().setVisibility(View.VISIBLE);
                ((TextView) adView.getBodyView()).setText(unifiedNativeAd.getBody());
            }
        }
        if (adView.getStarRatingView() != null) {
            if (unifiedNativeAd.getStarRating() == null)
                adView.getStarRatingView().setVisibility(View.GONE);
            else {
                adView.getStarRatingView().setVisibility(View.VISIBLE);
                ((RatingBar) adView.getStarRatingView()).setRating(unifiedNativeAd.getStarRating().floatValue());
            }
        }
        if (adView.getMediaView() != null && layout != R.layout.ad_unified_draw_navigator_small) {
            if (unifiedNativeAd.getMediaContent() == null)
                adView.getMediaView().setVisibility(View.GONE);
            else {
                adView.getMediaView().setVisibility(View.VISIBLE);
                ((com.google.android.gms.ads.formats.MediaView) adView.getMediaView()).setMediaContent(unifiedNativeAd.getMediaContent());
            }
        }
        if (adView.getCallToActionView() != null) {
            if (unifiedNativeAd.getCallToAction() == null || unifiedNativeAd.getCallToAction().isEmpty())
                adView.getCallToActionView().setVisibility(View.GONE);
            else {
                adView.getCallToActionView().setVisibility(View.VISIBLE);
                ((TextView) adView.getCallToActionView()).setText(unifiedNativeAd.getCallToAction());
            }
        }
        adView.setNativeAd(unifiedNativeAd);
    }

    public interface FanNativeCallBack {
        void onError();

        void onSuccess();
    }

}