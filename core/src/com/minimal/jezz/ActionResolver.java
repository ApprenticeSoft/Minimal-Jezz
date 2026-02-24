package com.minimal.jezz;

public interface ActionResolver {
    void preloadInterstitial();
    void onNaturalBreak();
    void showBanner();
    void hideBanner();
    int getBannerHeightPx();
}
