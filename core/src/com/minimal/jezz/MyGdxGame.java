package com.minimal.jezz;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.minimal.jezz.Donnees;
import com.minimal.jezz.Langue;
import com.minimal.jezz.screen.LoadingScreen;

public class MyGdxGame extends Game implements ApplicationListener {
	public SpriteBatch batch;
	public AssetManager assets;
	public Langue langue;
	public ActionResolver actionResolver;
	public float hauteurBanniere;
	public Music music;

	public MyGdxGame(ActionResolver actionResolver){
		this.actionResolver = actionResolver;
	}
	
	@Override
	public void create () {
		
		Donnees.Load();
		if(Donnees.getNiveau() < 1)
			Donnees.setNiveau(1);
		
		//Donnees.setNiveau(25);
		//Donnees.setRate(false);
		
		batch = new SpriteBatch();
		assets = new AssetManager();
		langue = new Langue();
		langue.setLangue(Donnees.getLangue());
		
		hauteurBanniere = actionResolver.hauteurBanniere();
		
		if(!Donnees.getRate()){
			Donnees.setRateCount(Donnees.getRateCount() - 1);
		}
		
		music = Gdx.audio.newMusic(Gdx.files.internal("Sons/Minimal Jezz - Menu.ogg"));
		music.setLooping(true);
		
		this.setScreen(new LoadingScreen(this));
	}

	@Override
	public void render () {
		super.render();
	
		if(!Donnees.getSon())
			music.setVolume(0);
		else
			music.setVolume(1);
		
		if(actionResolver.adsListener())
			Donnees.setRemoveAds(true);
		
		//if(!Donnees.getRemoveAds()){
		
		if(!actionResolver.adsListener()){		
			actionResolver.LoadInterstital();
			
			if(Variables.perdu || Variables.gagné || Variables.pause)
				actionResolver.showAdsTop();
			else
				actionResolver.hideAds();
			
			if(Variables.INTERSTITIAL_TRIGGER < 1){
				Variables.INTERSTITIAL_TRIGGER = MathUtils.random(1,2);
				actionResolver.showOrLoadInterstital();
			}
			
			if(Donnees.getInterstitial() > 5){
				actionResolver.showOrLoadInterstital();
				Donnees.setInterstitial(Donnees.getInterstitial() - 1);
			}			
		}
		else if(actionResolver.adsListener()){
			hauteurBanniere = 0;
			actionResolver.hideAds();
		}
		else  {
			hauteurBanniere = 0;
			actionResolver.hideAds();
		}
	}
	 
	public void dispose() {
		batch.dispose();
	}	
}
