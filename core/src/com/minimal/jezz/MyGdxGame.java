package com.minimal.jezz;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.minimal.jezz.screen.LoadingScreen;

public class MyGdxGame extends Game implements ApplicationListener {
    public SpriteBatch batch;
    public AssetManager assets;
    public Langue langue;
    public ActionResolver actionResolver;
    public float hauteurBanniere;
    public Music music;

    public MyGdxGame(ActionResolver actionResolver) {
        this.actionResolver = actionResolver;
    }

    @Override
    public void create() {
        Donnees.Load();
        if (Donnees.getNiveau() < 1) {
            Donnees.setNiveau(1);
        }

        batch = new SpriteBatch();
        assets = new AssetManager();
        langue = new Langue();
        langue.setLangue(Donnees.getLangue());

        hauteurBanniere = actionResolver.getBannerHeightPx();

        if (!Donnees.getRate()) {
            Donnees.setRateCount(Donnees.getRateCount() - 1);
        }

        music = Gdx.audio.newMusic(Gdx.files.internal("Sons/Minimal Jezz - Menu.ogg"));
        music.setLooping(true);

        actionResolver.preloadInterstitial();

        this.setScreen(new LoadingScreen(this));
    }

    @Override
    public void render() {
        super.render();

        float targetVolume = Donnees.getSon() ? 1f : 0f;
        if (music.getVolume() != targetVolume) {
            music.setVolume(targetVolume);
        }

        hauteurBanniere = actionResolver.getBannerHeightPx();
    }

    public void dispose() {
        if (getScreen() != null) {
            getScreen().dispose();
        }
        music.dispose();
        assets.dispose();
        batch.dispose();
    }
}
