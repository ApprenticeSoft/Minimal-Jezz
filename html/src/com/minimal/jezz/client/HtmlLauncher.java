package com.minimal.jezz.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.minimal.jezz.ActionResolver;
import com.minimal.jezz.MyGdxGame;
import com.google.gwt.user.client.Window;

public class HtmlLauncher extends GwtApplication {

    private static class HtmlActionResolver implements ActionResolver {
        @Override
        public void preloadInterstitial() {
            // No-op on web build.
        }

        @Override
        public void onNaturalBreak() {
            // No-op on web build.
        }

        @Override
        public void showBanner() {
            // No-op on web build.
        }

        @Override
        public void hideBanner() {
            // No-op on web build.
        }

        @Override
        public int getBannerHeightPx() {
            return 0;
        }
    }

    @Override
    public GwtApplicationConfiguration getConfig() {
        int browserWidth = Math.max(1, Window.getClientWidth());
        int browserHeight = Math.max(1, Window.getClientHeight());
        float portraitAspect = 9f / 16f;
        int targetWidth = Math.min(browserWidth, Math.round(browserHeight * portraitAspect));
        int targetHeight = browserHeight;
        GwtApplicationConfiguration config = new GwtApplicationConfiguration(targetWidth, targetHeight);
        config.padHorizontal = 0;
        config.padVertical = 0;
        config.useDebugGL = false;
        return config;
    }

    @Override
    public ApplicationListener createApplicationListener() {
        return new MyGdxGame(new HtmlActionResolver());
    }
}
