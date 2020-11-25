package com.example.adsadmobandfan.ads;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.adsadmobandfan.utils.Ads;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.NativeAd;
import com.facebook.ads.NativeAdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;

public class AdsInterNativeUtils {
    public static final String ACTION_CLOSE = "ACTION_CLOSE";
    public static final String ACTION_LOAD_FAIL = "ACTION_LOAD_FAIL";
    public static final String ACTION_LOAD_MEDIA_SUCCESS = "ACTION_LOAD_MEDIA_SUCCESS";
    public static final String ACTION_AD_LOADED = "ACTION_AD_LOADED";
    public static final String ACTION_AD_CLICKED = "ACTION_AD_CLICKED";
    public static final String ACTION_AD_IMPRESSION = "ACTION_AD_IMPRESSION";
    private static final String TAG = "AdsInterNativeUtils";
    Activity activity;
    private static AdsInterNativeUtils adsInterNativeUtils;
    AdsLoadListenerCallBack adsLoadListener;
    IntentFilter intentFilter;
    static UnifiedNativeAd unifiedNativeAd;
    static NativeAd nativeAd;

    private AdsInterNativeUtils(Activity activity) {
        this.activity = activity;
        intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_AD_CLICKED);
        intentFilter.addAction(ACTION_AD_IMPRESSION);
        intentFilter.addAction(ACTION_AD_LOADED);
        intentFilter.addAction(ACTION_LOAD_MEDIA_SUCCESS);
        intentFilter.addAction(ACTION_LOAD_FAIL);
        intentFilter.addAction(ACTION_CLOSE);
        LocalBroadcastManager.getInstance(activity).registerReceiver(mMessageReceiver, intentFilter);
    }

    public static AdsInterNativeUtils getInstance(Activity activity) {
        if (adsInterNativeUtils != null) {
            adsInterNativeUtils.activity = activity;
        } else {
            adsInterNativeUtils = new AdsInterNativeUtils(activity);
        }
        return adsInterNativeUtils;
    }

    public void setAdsLoadListener(AdsLoadListenerCallBack adsLoadListener) {
        this.adsLoadListener = adsLoadListener;
    }

    // Our handler for received Intents. This will be called whenever an Intent
// with an action named "custom-event-name" is broadcasted.
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e(TAG, "onReceive: " + intent.getAction());
            if (intent.getAction() != null) {
                Log.e(TAG, "onReceive: " + intent.getAction());
                switch (intent.getAction()) {
                    case ACTION_CLOSE:
                        nativeAd = null;
                        unifiedNativeAd = null;
                        if (adsLoadListener != null)
                            adsLoadListener.adClosed();
                        break;
                    case ACTION_AD_CLICKED:
                        if (adsLoadListener != null)
                            adsLoadListener.onAdClicked();
                        break;
                    case ACTION_AD_IMPRESSION:
                        if (adsLoadListener != null)
                            adsLoadListener.adImpression();
                        break;
                    case ACTION_AD_LOADED:
                        if (adsLoadListener != null)
                            adsLoadListener.adLoaded();
                        break;
                    case ACTION_LOAD_FAIL:
                        if (adsLoadListener != null)
                            adsLoadListener.adLoadFailed();
                        break;
                    case ACTION_LOAD_MEDIA_SUCCESS:
                        if (adsLoadListener != null)
                            adsLoadListener.adLoadMediaViewSuccess();
                        break;
                }
            }
        }
    };

    public void showAdsAdmob(String id_admob, AdsLoadListenerCallBack adsLoadListener) {
        this.adsLoadListener = adsLoadListener;
        Intent intent = new Intent(activity, AdsActivity.class);
        intent.putExtra("admob_id", id_admob);
        intent.putExtra("isShowAdmob", true);
        activity.startActivity(intent);
    }

    public void showAdsFan(String id_fan, AdsLoadListenerCallBack adsLoadListener) {
        this.adsLoadListener = adsLoadListener;
        Intent intent = new Intent(activity, AdsActivity.class);
        intent.putExtra("fan_id", id_fan);
        intent.putExtra("isShowAdmob", false);
        activity.startActivity(intent);
    }

    public void loadAds(boolean isShowAdmob) {
        if (isShowAdmob) {
            AdLoader.Builder builder = new AdLoader.Builder(activity, Ads.native_id_admob).withAdListener(new com.google.android.gms.ads.AdListener(){
                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                    unifiedNativeAd.destroy();
                    unifiedNativeAd = null;
                }
            });
            builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                @Override
                public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                    AdsInterNativeUtils.unifiedNativeAd = unifiedNativeAd;
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
        else {
            nativeAd = new NativeAd(activity, Ads.native_id_fan);
            nativeAd.setAdListener(new NativeAdListener() {

                @Override
                public void onMediaDownloaded(Ad ad) {
                    if (nativeAd == null || nativeAd != ad) {
                        return;
                    }
                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    if(Ads.is_load_failed){
                        AdLoader.Builder builder = new AdLoader.Builder(activity, Ads.native_id_admob).withAdListener(new com.google.android.gms.ads.AdListener(){
                            @Override
                            public void onAdFailedToLoad(int i) {
                                super.onAdFailedToLoad(i);
                            }
                        });
                        builder.forUnifiedNativeAd(new UnifiedNativeAd.OnUnifiedNativeAdLoadedListener() {
                            @Override
                            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                                AdsInterNativeUtils.unifiedNativeAd = unifiedNativeAd;
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
                    else {
                        nativeAd = null;
                    }
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
            nativeAd.loadAd();
        }
    }

    public abstract static class AdsLoadListenerCallBack {
        public void adLoadFailed() {
        }

        public void adLoadMediaViewSuccess() {
        }

        public void adLoaded() {
        }

        public void onAdClicked() {
        }

        public void adImpression() {
        }

        public void adClosed() {
        }
    }
}