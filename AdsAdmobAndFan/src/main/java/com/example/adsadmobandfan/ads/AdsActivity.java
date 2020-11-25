package com.example.adsadmobandfan.ads;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.adsadmobandfan.R;
import com.example.adsadmobandfan.utils.Ads;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdIconView;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAdView;

import java.util.ArrayList;
import java.util.List;

import static com.facebook.ads.AdSettings.IntegrationErrorMode.INTEGRATION_ERROR_CRASH_DEBUG_MODE;

public class AdsActivity extends Activity implements AdsLoadListener {
    private static final String TAG = "MainActivity";
    FrameLayout frmAdsDemo;
    ImageView btnCloseAds;
    ProgressBar progressLoading;
    public static String id_fan = "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID";
    public static String id_admob = "ca-app-pub-3940256099942544/2247696110";
    public static boolean isShowAdmob = false;
    private static boolean isLoaded = false;
    NativeAd nativeAd;
    UnifiedNativeAd nativeAdAdmob;
    private static boolean isLoadFailed = false;

    public static void setId(String s) {
        id_admob = s;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_ads);
        initAds();
        Intent intent = getIntent();
        id_fan = intent.getStringExtra("fan_id");
        id_admob = intent.getStringExtra("admob_id");
        isShowAdmob = intent.getBooleanExtra("isShowAdmob", true);
        initView();
        showNativeAds();
        setTheme(android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);
    }

    private void showNativeAds() {
        if (AdsInterNativeUtils.unifiedNativeAd != null || AdsInterNativeUtils.nativeAd != null) {
            isLoaded = true;
            showCancel();
            if (AdsInterNativeUtils.unifiedNativeAd != null && Ads.is_show_admob) {
                inFlateAdView(R.layout.ad_unified_draw_navigator_inter_native, AdsInterNativeUtils.unifiedNativeAd);
            } else {
                if (AdsInterNativeUtils.nativeAd != null) {
                    if (AdsInterNativeUtils.nativeAd.isAdLoaded())
                        inflateAd(this, AdsInterNativeUtils.nativeAd, frmAdsDemo, R.layout.fan_native_layout_medium_inter_native);
                    else {
                        AdsInterNativeUtils.nativeAd.destroy();
                        AdsInterNativeUtils.nativeAd = null;
                        nativeFan(frmAdsDemo, id_fan, R.layout.fan_native_layout_medium_inter_native, this);
                    }
                } else {
                    if (AdsInterNativeUtils.unifiedNativeAd != null)
                        inFlateAdView(R.layout.ad_unified_draw_navigator_inter_native, AdsInterNativeUtils.unifiedNativeAd);
                    else {
                        adClosed();
                        finish();
                    }
                }
            }
        } else {
            if (isShowAdmob) {
                if (id_admob == null) throw new NullPointerException("Id Admob is null");
                else
                    nativeAdmob(frmAdsDemo, R.layout.ad_unified_draw_navigator_inter_native, this, id_admob);
            } else {
                if (id_fan == null) throw new NullPointerException("Id Fan is null");
                else
                    nativeFan(frmAdsDemo, id_fan, R.layout.fan_native_layout_medium_inter_native, this);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    public void initAds() {
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        AudienceNetworkAds.initialize(this);
        AdSettings.setIntegrationErrorMode(INTEGRATION_ERROR_CRASH_DEBUG_MODE);
    }

    public static boolean isIsLoadFailed() {
        return isLoadFailed;
    }

    public static boolean isIsLoaded() {
        return isLoaded;
    }

    private void initView() {
        frmAdsDemo = findViewById(R.id.frmAdsDemo);
        btnCloseAds = findViewById(R.id.btnCloseAds);
        progressLoading = findViewById(R.id.progressLoading);
        btnCloseAds.setOnClickListener((v) -> {
            onBackPressed();
        });
    }

    @Override
    public void onBackPressed() {
        if (isLoaded || isLoadFailed) {
            adClosed();
            super.onBackPressed();
            isLoaded = false;
            isLoadFailed = false;
        }
    }

    public void nativeAdmob(final FrameLayout frameLayout, final int layout, AdsLoadListener adListener, String id_inter) {
        UnifiedNativeAd nativeAd;
        AdLoader.Builder builder = new AdLoader.Builder(this, id_inter).withAdListener(new com.google.android.gms.ads.AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                adListener.adClosed();
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
//                adListener.onAdLeftApplication();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
//                adListener.adP/
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                adListener.onAdClicked();
            }

            @Override
            public void onAdImpression() {
                super.onAdImpression();
                adListener.adImpression();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                adListener.adLoadFailed();
                if (frameLayout != null) frameLayout.removeAllViews();
                Toast.makeText(AdsActivity.this, "Failed to load native ad: "
                        + errorCode, Toast.LENGTH_SHORT).show();
                showCancel();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                adListener.adLoaded();
                showCancel();
            }
        });
        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
            @Override
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                adListener.adLoadMediaViewSuccess();
//                unifiedNativeAdCache = unifiedNativeAd;
                if (isDestroyed()) {
                    unifiedNativeAd.destroy();
                    return;
                }
                // You must call destroy on old ads when you are done with them,
                // otherwise you will have a memory leak.
                if (nativeAdAdmob != null) {
                    nativeAdAdmob.destroy();
                }
                nativeAdAdmob = unifiedNativeAd;
                inFlateAdView(layout, unifiedNativeAd);
            }

        });
        VideoOptions videoOptions = new VideoOptions.Builder().build();
        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        builder.withNativeAdOptions(adOptions);
        AdLoader adLoader = builder.build();
        adLoader.loadAd(new PublisherAdRequest.Builder().build());
    }

    private void inFlateAdView(int layout, UnifiedNativeAd unifiedNativeAd) {
        UnifiedNativeAdView adView = (UnifiedNativeAdView) getLayoutInflater()
                .inflate(layout, null);
        populateUnifiedNativeAdView(unifiedNativeAd, adView);
        frmAdsDemo.removeAllViews();
        frmAdsDemo.addView(adView);
    }

    private void populateUnifiedNativeAdView(UnifiedNativeAd unifiedNativeAd, UnifiedNativeAdView adView) {
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setMediaView((com.google.android.gms.ads.formats.MediaView) adView.findViewById(R.id.ad_media));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));

        if (adView.getPriceView() != null) {
            if (unifiedNativeAd.getPrice() == null)
                adView.getPriceView().setVisibility(View.GONE);
            else {
                ((TextView) adView.getPriceView()).setText(unifiedNativeAd.getPrice());
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
            if (unifiedNativeAd.getHeadline() == null)
                adView.getHeadlineView().setVisibility(View.GONE);
            else {
                adView.getHeadlineView().setVisibility(View.VISIBLE);
                ((TextView) adView.getHeadlineView()).setText(unifiedNativeAd.getHeadline());
            }
        }
        if (adView.getAdvertiserView() != null) {
            if (unifiedNativeAd.getAdvertiser() == null)
                adView.getAdvertiserView().setVisibility(View.GONE);
            else {
                adView.getAdvertiserView().setVisibility(View.VISIBLE);
                ((TextView) adView.getAdvertiserView()).setText(unifiedNativeAd.getAdvertiser());
            }
        }
        if (adView.getBodyView() != null) {
            if (unifiedNativeAd.getBody() == null)
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
        if (adView.getMediaView() != null) {
            if (unifiedNativeAd.getMediaContent() == null)
                adView.getMediaView().setVisibility(View.GONE);
            else {
                adView.getMediaView().setVisibility(View.VISIBLE);
                ((com.google.android.gms.ads.formats.MediaView) adView.getMediaView()).setMediaContent(unifiedNativeAd.getMediaContent());
            }
        }
        if (adView.getPriceView() != null) {
            if (unifiedNativeAd.getPrice() == null)
                adView.getPriceView().setVisibility(View.GONE);
            else {
                adView.getPriceView().setVisibility(View.VISIBLE);
                ((TextView) adView.getPriceView()).setText(unifiedNativeAd.getPrice());
                if (unifiedNativeAd.getPrice().equals("0"))
                    adView.getPriceView().setVisibility(View.GONE);

            }
        }
        if (adView.getCallToActionView() != null) {
            if (unifiedNativeAd.getCallToAction() == null)
                adView.getCallToActionView().setVisibility(View.GONE);
            else {
                adView.getCallToActionView().setVisibility(View.VISIBLE);
                ((TextView) adView.getCallToActionView()).setText(unifiedNativeAd.getCallToAction());
            }
        }
        if (adView.getStoreView() != null) {
            if (unifiedNativeAd.getStore() == null)
                adView.getStoreView().setVisibility(View.GONE);
            else {
                adView.getStoreView().setVisibility(View.VISIBLE);
                ((TextView) adView.getStoreView()).setText(unifiedNativeAd.getStore());
            }
        }
//        ad_store
        adView.setNativeAd(unifiedNativeAd);
    }

    private void showCancel() {
        btnCloseAds.setVisibility(View.VISIBLE);
        progressLoading.setVisibility(View.GONE);
    }

    public void nativeFan(ViewGroup frameAds, String idNative, int layout, AdsLoadListener listener) {
        nativeAd = new NativeAd(this, idNative);
        nativeAd.setAdListener(new NativeAdListener() {

            @Override
            public void onMediaDownloaded(Ad ad) {
                listener.adLoadMediaViewSuccess();
                if (nativeAd == null || nativeAd != ad) {
                    return;
                }
                inflateAd(AdsActivity.this, nativeAd, frameAds, layout);
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                listener.adLoadFailed();
            }

            @Override
            public void onAdLoaded(Ad ad) {
                if (nativeAd == null || nativeAd != ad) {
                    return;
                }
                inflateAd(AdsActivity.this, nativeAd, frameAds, layout);
                listener.adLoaded();
            }

            @Override
            public void onAdClicked(Ad ad) {
                listener.onAdClicked();
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                listener.adImpression();
            }
        });
        nativeAd.loadAd();
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
        clickableViews.add(nativeAdBody);
        clickableViews.add(nativeAdSocialContext);
        clickableViews.add(sponsoredLabel);
        clickableViews.add(nativeAdMedia);
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

    @Override
    public void adLoadFailed() {
        isLoadFailed = true;
        Intent intent = new Intent(AdsInterNativeUtils.ACTION_LOAD_FAIL);
        // You can also include some extra data.
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        finish();
    }

    @Override
    public void adLoadMediaViewSuccess() {
        isLoaded = true;
        Intent intent = new Intent(AdsInterNativeUtils.ACTION_LOAD_MEDIA_SUCCESS);
        // You can also include some extra data.
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        showCancel();
    }

    @Override
    public void adLoaded() {
        Intent intent = new Intent(AdsInterNativeUtils.ACTION_AD_LOADED);
        // You can also include some extra data.
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void onAdClicked() {
        Intent intent = new Intent(AdsInterNativeUtils.ACTION_AD_CLICKED);
        // You can also include some extra data.
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void adImpression() {
        Intent intent = new Intent(AdsInterNativeUtils.ACTION_AD_IMPRESSION);
        // You can also include some extra data.
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public void adClosed() {
        Intent intent = new Intent(AdsInterNativeUtils.ACTION_CLOSE);
        // You can also include some extra data.
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}