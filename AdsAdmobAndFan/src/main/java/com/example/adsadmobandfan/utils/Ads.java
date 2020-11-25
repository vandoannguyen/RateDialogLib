package com.example.adsadmobandfan.utils;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.adsadmobandfan.R;
import com.example.adsadmobandfan.ads.AdsInterNativeUtils;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAdExtendedListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;

import static com.facebook.ads.AdSettings.IntegrationErrorMode.INTEGRATION_ERROR_CRASH_DEBUG_MODE;

public class Ads {
    public static int ads_click_main_percents = 50;
    public static boolean is_show_splash_inter = true;
    public static boolean is_show_inter_back = true;
    Activity activity;
    private static Ads ads;
    public static String banner_id_admob = "ca-app-pub-3940256099942544/6300978111";
    public static String inter_id_admob = "ca-app-pub-3940256099942544/1033173712";
    public static String native_id_admob = "ca-app-pub-3940256099942544/2247696110";
    public static String rewar_id_admob = "ca-app-pub-3940256099942544/5224354917";
    public static String banner_id_fan = "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID";
    public static String inter_id_fan = "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID";
    public static String native_id_fan = "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID";
    public static String rewar_id_fan = "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID";
    public static boolean is_show_banner = true;
    public static boolean is_show_inter = true;
    public static boolean is_show_native = true;
    public static boolean is_show_admob = true;
    public static boolean is_load_failed = true;

    public void initAds() {
        MobileAds.initialize(activity, "ca-app-pub-3940256099942544~3347511713");
        AudienceNetworkAds.initialize(activity);
        AdSettings.setIntegrationErrorMode(INTEGRATION_ERROR_CRASH_DEBUG_MODE);
    }

    public static Ads getInstance(Activity activity) {
        if (ads != null) {
            ads.activity = activity;
        } else ads = new Ads(activity);
        return ads;
    }

    private Ads(Activity activity) {
        this.activity = activity;
    }

    public void banner(ViewGroup frameAds, AdsSize adsSize) {
        if (is_show_admob) {
            AdsUtils.getInstance(activity).bannerAdMob(frameAds,
                    banner_id_admob,
                    adsSize == AdsSize.BANNER ? AdSize.BANNER
                            : adsSize == AdsSize.LARGE
                            ? AdSize.LARGE_BANNER
                            : AdSize.MEDIUM_RECTANGLE);
        } else {
            AdsUtils.getInstance(activity).bannerFan(frameAds, native_id_fan,
                    adsSize == AdsSize.BANNER
                            ? com.facebook.ads.AdSize.BANNER_HEIGHT_50
                            : adsSize == AdsSize.LARGE
                            ? com.facebook.ads.AdSize.BANNER_HEIGHT_90
                            : com.facebook.ads.AdSize.RECTANGLE_HEIGHT_250, new AdsUtils.FanNativeCallBack() {
                        @Override
                        public void onError() {
                            if (is_load_failed)
                                AdsUtils.getInstance(activity).bannerAdMob(frameAds,
                                        banner_id_admob,
                                        adsSize == AdsSize.BANNER ? AdSize.BANNER
                                                : adsSize == AdsSize.LARGE
                                                ? AdSize.LARGE_BANNER
                                                : AdSize.MEDIUM_RECTANGLE);
                            else frameAds.setVisibility(View.GONE);
                        }

                        @Override
                        public void onSuccess() {

                        }
                    });
        }
    }

    public void banner(ViewGroup frameAds, AdsSize adsSize, boolean is_dmob) {
        if (is_dmob) {
            AdsUtils.getInstance(activity).bannerAdMob(frameAds,
                    banner_id_admob,
                    adsSize == AdsSize.BANNER ? AdSize.BANNER
                            : adsSize == AdsSize.LARGE
                            ? AdSize.LARGE_BANNER
                            : AdSize.MEDIUM_RECTANGLE);
        } else {
            AdsUtils.getInstance(activity).bannerFan(frameAds, banner_id_fan, adsSize == AdsSize.BANNER ? com.facebook.ads.AdSize.BANNER_HEIGHT_50
                    : adsSize == AdsSize.LARGE
                    ? com.facebook.ads.AdSize.BANNER_HEIGHT_90
                    : com.facebook.ads.AdSize.RECTANGLE_HEIGHT_250, new AdsUtils.FanNativeCallBack() {
                @Override
                public void onError() {
                    if (is_load_failed)
                        AdsUtils.getInstance(activity).bannerAdMob(frameAds,
                                banner_id_admob,
                                adsSize == AdsSize.BANNER ? AdSize.BANNER
                                        : adsSize == AdsSize.LARGE
                                        ? AdSize.LARGE_BANNER
                                        : AdSize.MEDIUM_RECTANGLE);
                }

                @Override
                public void onSuccess() {

                }
            });
        }
    }

    public void bannerNative(ViewGroup frameAds, AdsSize adsSize) {
        if (is_show_admob) {
            AdsUtils.getInstance(activity).nativeAdmob((FrameLayout) frameAds, adsSize == AdsSize.BANNER
                    ? R.layout.ad_unified_draw_navigator_small
                    : adsSize == AdsSize.LARGE
                    ? R.layout.ad_unified_draw_navigator_small
                    : R.layout.ad_unified_draw_navigator, new AdListener() {
                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                    frameAds.setVisibility(View.GONE);
                }
            }, native_id_admob);
        } else {
            AdsUtils.getInstance(activity).nativeFan(frameAds, native_id_fan,
                    adsSize == AdsSize.BANNER
                            ? R.layout.fan_native_layout_medium_small
                            : adsSize == AdsSize.LARGE
                            ? R.layout.fan_native_layout_medium_small
                            : R.layout.fan_native_layout_medium, new AdsUtils.FanNativeCallBack() {
                        @Override
                        public void onError() {
                            if (is_load_failed) {
                                AdsUtils.getInstance(activity).nativeAdmob((FrameLayout) frameAds,
                                        adsSize == AdsSize.BANNER
                                                ? R.layout.ad_unified_draw_navigator_small
                                                : adsSize == AdsSize.LARGE
                                                ? R.layout.ad_unified_draw_navigator_small
                                                : R.layout.ad_unified_draw_navigator, new AdListener() {
                                            @Override
                                            public void onAdFailedToLoad(int i) {
                                                super.onAdFailedToLoad(i);
                                                frameAds.setVisibility(View.GONE);
                                            }
                                        }, native_id_admob);
                            } else frameAds.setVisibility(View.GONE);
                        }

                        @Override
                        public void onSuccess() {

                        }
                    });
        }
    }

    public void bannerNative(ViewGroup frameAds, AdsSize adsSize, boolean is_admob) {
        if (is_admob) {
            AdsUtils.getInstance(activity).nativeAdmob((FrameLayout) frameAds, adsSize == AdsSize.BANNER
                    ? R.layout.ad_unified_draw_navigator_small
                    : adsSize == AdsSize.LARGE
                    ? R.layout.ad_unified_draw_navigator_small
                    : R.layout.ad_unified_draw_navigator, new AdListener() {
                @Override
                public void onAdFailedToLoad(int i) {
                    super.onAdFailedToLoad(i);
                    frameAds.setVisibility(View.GONE);
                }
            }, native_id_admob);
        } else {
            AdsUtils.getInstance(activity).nativeFan(frameAds, native_id_fan,
                    adsSize == AdsSize.BANNER
                            ? R.layout.fan_native_layout_medium_small
                            : adsSize == AdsSize.LARGE
                            ? R.layout.fan_native_layout_medium_small
                            : R.layout.fan_native_layout_medium, new AdsUtils.FanNativeCallBack() {
                        @Override
                        public void onError() {
                            if (is_load_failed) {
                                AdsUtils.getInstance(activity).nativeAdmob((FrameLayout) frameAds,
                                        adsSize == AdsSize.BANNER
                                                ? R.layout.ad_unified_draw_navigator_small
                                                : adsSize == AdsSize.LARGE
                                                ? R.layout.ad_unified_draw_navigator_small
                                                : R.layout.ad_unified_draw_navigator, new AdListener() {
                                            @Override
                                            public void onAdFailedToLoad(int i) {
                                                super.onAdFailedToLoad(i);
                                                frameAds.setVisibility(View.GONE);
                                            }
                                        }, native_id_admob);
                            } else frameAds.setVisibility(View.GONE);
                        }

                        @Override
                        public void onSuccess() {

                        }
                    });
        }
    }

    public void inter(CallBackInter callBackInter) {
        if (is_show_admob)
            AdsUtils.getInstance(activity).interAdmob(inter_id_admob, new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    callBackInter.adClose();
                }

                @Override
                public void onAdFailedToLoad(LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    callBackInter.adLoadFailed(-1);
                }
            });
        else
            AdsUtils.getInstance(activity).interFan(inter_id_fan, new InterstitialAdExtendedListener() {
                @Override
                public void onRewardedAdCompleted() {

                }

                @Override
                public void onRewardedAdServerSucceeded() {

                }

                @Override
                public void onRewardedAdServerFailed() {

                }

                @Override
                public void onInterstitialDisplayed(Ad ad) {

                }

                @Override
                public void onInterstitialDismissed(Ad ad) {
                    callBackInter.adClose();
                }

                @Override
                public void onInterstitialActivityDestroyed() {

                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    if (is_load_failed) {
                        AdsUtils.getInstance(activity).interAdmob(inter_id_admob, new AdListener() {
                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();
                                callBackInter.adClose();
                            }

                            @Override
                            public void onAdFailedToLoad(LoadAdError loadAdError) {
                                super.onAdFailedToLoad(loadAdError);
                                callBackInter.adLoadFailed(-1);
                            }
                        });
                    } else {
                        callBackInter.adLoadFailed(-1);
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
    }

    public void inter(CallBackInter callBackInter, boolean is_admob) {
        if (is_admob)
            AdsUtils.getInstance(activity).interAdmob(inter_id_admob, new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    callBackInter.adClose();
                }

                @Override
                public void onAdFailedToLoad(LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    callBackInter.adLoadFailed(-1);
                }
            });
        else
            AdsUtils.getInstance(activity).interFan(inter_id_fan, new InterstitialAdExtendedListener() {
                @Override
                public void onRewardedAdCompleted() {

                }

                @Override
                public void onRewardedAdServerSucceeded() {

                }

                @Override
                public void onRewardedAdServerFailed() {

                }

                @Override
                public void onInterstitialDisplayed(Ad ad) {

                }

                @Override
                public void onInterstitialDismissed(Ad ad) {
                    callBackInter.adClose();
                }

                @Override
                public void onInterstitialActivityDestroyed() {

                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    if (is_load_failed) {
                        AdsUtils.getInstance(activity).interAdmob(inter_id_admob, new AdListener() {
                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();
                                callBackInter.adClose();
                            }

                            @Override
                            public void onAdFailedToLoad(LoadAdError loadAdError) {
                                super.onAdFailedToLoad(loadAdError);
                                callBackInter.adLoadFailed(-1);
                            }
                        });
                    } else {
                        callBackInter.adLoadFailed(-1);
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
    }

    public void interCache(CallBackInter callBackInter) {
        if (Ads.is_show_admob) {
            AdsUtils.getInstance(activity).interAdmobCache(inter_id_admob, callBackInter != null ? new AdListener() {
                @Override
                public void onAdFailedToLoad(LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    if (callBackInter != null)
                        callBackInter.adLoadFailed(-1);
                }

                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    if (callBackInter != null)
                        callBackInter.adClose();
                }
            } : null);
        } else {
            AdsUtils.getInstance(activity).interFanCache(inter_id_fan, callBackInter != null ? new InterstitialAdExtendedListener() {
                @Override
                public void onInterstitialActivityDestroyed() {

                }

                @Override
                public void onInterstitialDisplayed(Ad ad) {

                }

                @Override
                public void onInterstitialDismissed(Ad ad) {
                    callBackInter.adClose();
                }

                @Override
                public void onError(Ad ad, AdError adError) {
                    callBackInter.adLoadFailed(-1);
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

                @Override
                public void onRewardedAdCompleted() {

                }

                @Override
                public void onRewardedAdServerSucceeded() {

                }

                @Override
                public void onRewardedAdServerFailed() {

                }
            } : null);
        }
    }

    public void interNative(CallBackInter callBackInter) {
        if (callBackInter == null) {
            AdsInterNativeUtils.getInstance(activity).loadAds(true);
        } else {
            if (is_show_admob) {
                AdsInterNativeUtils.getInstance(activity).showAdsAdmob(native_id_admob,
                        new AdsInterNativeUtils.AdsLoadListenerCallBack() {
                            @Override
                            public void adLoadFailed() {
                                callBackInter.adLoadFailed(-1);
                            }

                            @Override
                            public void adClosed() {
                                callBackInter.adClose();
                            }
                        });
            } else {
                AdsInterNativeUtils.getInstance(activity).showAdsFan(native_id_fan,
                        new AdsInterNativeUtils.AdsLoadListenerCallBack() {
                            @Override
                            public void adLoadFailed() {
                                if (is_load_failed) {
                                    AdsInterNativeUtils.getInstance(activity).showAdsAdmob(native_id_admob, new AdsInterNativeUtils.AdsLoadListenerCallBack() {
                                        @Override
                                        public void adLoadFailed() {
                                            callBackInter.adLoadFailed(-1);
                                        }

                                        @Override
                                        public void adClosed() {
                                            callBackInter.adClose();
                                        }
                                    });
                                } else callBackInter.adLoadFailed(-1);
                            }

                            @Override
                            public void adClosed() {
                                callBackInter.adClose();
                            }
                        });
            }
        }
    }

    public void nativeAds(ViewGroup frmAds, NativeSize size) {
        if (is_show_admob) {
            AdsUtils.getInstance(activity).nativeAdmob((FrameLayout) frmAds,
                    size == NativeSize.MEDIUM ?
                            R.layout.ad_unified_draw_navigator :
                            R.layout.ad_unified_draw_navigator_small, new AdListener() {
                        @Override
                        public void onAdFailedToLoad(int i) {
                            super.onAdFailedToLoad(i);
                            frmAds.setVisibility(View.GONE);
                        }
                    }, native_id_admob);
        } else {
            AdsUtils.getInstance(activity).nativeFan(frmAds, native_id_fan,
                    size == NativeSize.MEDIUM ?
                            R.layout.fan_native_layout_medium
                            : R.layout.fan_native_layout_medium_small, new AdsUtils.FanNativeCallBack() {
                        @Override
                        public void onError() {
                            frmAds.removeAllViews();
                            if (is_load_failed)
                                AdsUtils.getInstance(activity).nativeAdmob((FrameLayout) frmAds,
                                        size == NativeSize.MEDIUM ?
                                                R.layout.ad_unified_draw_navigator :
                                                R.layout.ad_unified_draw_navigator_small, new AdListener() {
                                            @Override
                                            public void onAdFailedToLoad(int i) {
                                                super.onAdFailedToLoad(i);
                                                frmAds.setVisibility(View.GONE);
                                            }
                                        }, native_id_admob);
                            else {
                                frmAds.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onSuccess() {

                        }
                    });
        }
    }

    public void nativeAds(ViewGroup frmAds, NativeSize size, boolean is_dmob) {
        if (is_dmob) {
            AdsUtils.getInstance(activity).nativeAdmob((FrameLayout) frmAds,
                    size == NativeSize.MEDIUM ?
                            R.layout.ad_unified_draw_navigator :
                            R.layout.ad_unified_draw_navigator_small,
                    new AdListener() {
                        @Override
                        public void onAdFailedToLoad(int i) {
                            super.onAdFailedToLoad(i);
                            frmAds.setVisibility(View.GONE);
                        }
                    }, native_id_admob);
        } else {
            AdsUtils.getInstance(activity).nativeFan(frmAds, native_id_fan,
                    size == NativeSize.MEDIUM ?
                            R.layout.fan_native_layout_medium :
                            R.layout.fan_native_layout_medium_small,
                    new AdsUtils.FanNativeCallBack() {
                        @Override
                        public void onError() {
                            frmAds.removeAllViews();
                            if (is_load_failed)
                                AdsUtils.getInstance(activity).nativeAdmob((FrameLayout) frmAds,
                                        size == NativeSize.MEDIUM ?
                                                R.layout.ad_unified_draw_navigator :
                                                R.layout.ad_unified_draw_navigator_small,
                                        new AdListener() {
                                            @Override
                                            public void onAdFailedToLoad(int i) {
                                                super.onAdFailedToLoad(i);
                                                frmAds.setVisibility(View.GONE);
                                            }
                                        }, native_id_admob);
                            else frmAds.setVisibility(View.GONE);
                        }

                        @Override
                        public void onSuccess() {

                        }
                    });
        }
    }

    public void onDestroy() {
        AdsUtils.getInstance(activity).onDestroy();
    }

    public enum AdsSize {
        BANNER,
        MEDIUM,
        LARGE
    }

    public enum NativeSize {
        SMALL,
        MEDIUM
    }

    public interface CallBackInter {

        void adClose();

        void adLoadFailed(int i);
    }

    public interface CallBackRewared {

        void adClose();

        void adLoadFailed(int i);

        void adRewared();
    }
}
