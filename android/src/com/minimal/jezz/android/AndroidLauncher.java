package com.minimal.jezz.android;

import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowMetrics;
import android.widget.FrameLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.minimal.jezz.ActionResolver;
import com.minimal.jezz.MyGdxGame;

public class AndroidLauncher extends AndroidApplication implements ActionResolver {

    private static final String PROD_BANNER_ID = "ca-app-pub-7775582829834874/4673850344";
    private static final String PROD_INTERSTITIAL_ID = "ca-app-pub-7775582829834874/9104049943";
    private static final String TEST_BANNER_ID = "ca-app-pub-3940256099942544/6300978111";
    private static final String TEST_INTERSTITIAL_ID = "ca-app-pub-3940256099942544/1033173712";

    private static final int INTERSTITIAL_EVERY_BREAKS = 2;
    private static final long MIN_INTERSTITIAL_INTERVAL_MS = 60000L;

    private FrameLayout rootLayout;
    private AdView bannerView;
    private InterstitialAd interstitialAd;
    private boolean interstitialLoading;
    private int lastBannerWidthDp = -1;

    private int breaksSinceInterstitial;
    private long lastInterstitialShownAtMs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useImmersiveMode = true;
        config.useAccelerometer = false;
        config.useCompass = false;

        View gameView = initializeForView(new MyGdxGame(this), config);

        rootLayout = new FrameLayout(this);
        rootLayout.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
        ));
        rootLayout.addView(gameView);

        MobileAds.initialize(this, ignored -> { });
        setupAdaptiveBanner();
        preloadInterstitial();

        setContentView(rootLayout);
    }

    @Override
    protected void onDestroy() {
        if (bannerView != null) {
            bannerView.destroy();
            bannerView = null;
        }
        interstitialAd = null;
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bannerView != null) {
            bannerView.resume();
        }
        refreshBannerSize(false);
    }

    @Override
    protected void onPause() {
        if (bannerView != null) {
            bannerView.pause();
        }
        super.onPause();
    }

    private void setupAdaptiveBanner() {
        bannerView = new AdView(this);
        bannerView.setAdUnitId(getBannerUnitId());
        bannerView.setVisibility(View.GONE);
        FrameLayout.LayoutParams bannerParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        );
        bannerParams.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        rootLayout.addView(bannerView, bannerParams);

        rootLayout.addOnLayoutChangeListener((view, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> {
            if ((right - left) != (oldRight - oldLeft)) {
                refreshBannerSize(false);
            }
        });

        refreshBannerSize(true);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        refreshBannerSize(false);
    }

    private void refreshBannerSize(boolean forceReload) {
        if (bannerView == null) {
            return;
        }
        int adWidthDp = getAdaptiveBannerWidthDp();
        if (adWidthDp <= 0) {
            return;
        }
        boolean widthChanged = adWidthDp != lastBannerWidthDp;
        if (!forceReload && !widthChanged && bannerView.getAdSize() != null) {
            return;
        }
        lastBannerWidthDp = adWidthDp;
        bannerView.setAdSize(getAdaptiveBannerSize(adWidthDp));
        bannerView.loadAd(new AdRequest.Builder().build());
    }

    private int getAdaptiveBannerWidthDp() {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int adWidthPixels = rootLayout != null ? rootLayout.getWidth() : 0;
        if (adWidthPixels <= 0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowMetrics windowMetrics = getWindowManager().getCurrentWindowMetrics();
            adWidthPixels = windowMetrics.getBounds().width();
        } else if (adWidthPixels <= 0) {
            adWidthPixels = metrics.widthPixels;
        }
        return Math.max(1, Math.round(adWidthPixels / metrics.density));
    }

    private AdSize getAdaptiveBannerSize(int adWidthDp) {
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidthDp);
    }

    private String getBannerUnitId() {
        return isDebugBuild() ? TEST_BANNER_ID : PROD_BANNER_ID;
    }

    private String getInterstitialUnitId() {
        return isDebugBuild() ? TEST_INTERSTITIAL_ID : PROD_INTERSTITIAL_ID;
    }

    private boolean isDebugBuild() {
        return (getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
    }

    @Override
    public void preloadInterstitial() {
        runOnUiThread(() -> {
            if (interstitialLoading || interstitialAd != null) {
                return;
            }
            interstitialLoading = true;
            InterstitialAd.load(
                    this,
                    getInterstitialUnitId(),
                    new AdRequest.Builder().build(),
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(InterstitialAd ad) {
                            interstitialLoading = false;
                            interstitialAd = ad;
                            ad.setFullScreenContentCallback(new FullScreenContentCallback() {
                                @Override
                                public void onAdShowedFullScreenContent() {
                                    lastInterstitialShownAtMs = SystemClock.elapsedRealtime();
                                    breaksSinceInterstitial = 0;
                                    interstitialAd = null;
                                    hideBanner();
                                }

                                @Override
                                public void onAdDismissedFullScreenContent() {
                                    showBanner();
                                    preloadInterstitial();
                                }

                                @Override
                                public void onAdFailedToShowFullScreenContent(com.google.android.gms.ads.AdError adError) {
                                    interstitialAd = null;
                                    showBanner();
                                    preloadInterstitial();
                                }
                            });
                        }

                        @Override
                        public void onAdFailedToLoad(LoadAdError loadAdError) {
                            interstitialLoading = false;
                            interstitialAd = null;
                        }
                    }
            );
        });
    }

    @Override
    public void onNaturalBreak() {
        runOnUiThread(() -> {
            breaksSinceInterstitial++;
            long now = SystemClock.elapsedRealtime();
            boolean cooldownDone = now - lastInterstitialShownAtMs >= MIN_INTERSTITIAL_INTERVAL_MS;
            boolean eligible = breaksSinceInterstitial >= INTERSTITIAL_EVERY_BREAKS && cooldownDone;

            if (eligible && interstitialAd != null) {
                interstitialAd.show(this);
            } else {
                preloadInterstitial();
            }
        });
    }

    @Override
    public void showBanner() {
        runOnUiThread(() -> {
            refreshBannerSize(false);
            if (bannerView != null && bannerView.getVisibility() != View.VISIBLE) {
                bannerView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void hideBanner() {
        runOnUiThread(() -> {
            if (bannerView != null && bannerView.getVisibility() != View.GONE) {
                bannerView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getBannerHeightPx() {
        return bannerView == null ? 0 : bannerView.getHeight();
    }
}
