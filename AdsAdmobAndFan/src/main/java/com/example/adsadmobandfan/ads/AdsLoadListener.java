package com.example.adsadmobandfan.ads;

public interface AdsLoadListener {
    void adLoadFailed();

    void adLoadMediaViewSuccess();

    void adLoaded();

    void onAdClicked();

    void adImpression();

    void adClosed();
}