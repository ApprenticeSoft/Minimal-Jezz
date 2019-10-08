package com.minimal.jezz;

public interface ActionResolver {
	
	public String SKU_REMOVE_ADS = "remove_ads";
	//(arbitrary) request code for the purchase flow     
	static final int RC_REQUEST = 10001;
	
	public void removeAds();
	public void processPurchases();
	
	//Publicités
	public void showOrLoadInterstital();
	public void LoadInterstital();
	public void showAdsBottom();
	public void showAdsTop();
	public void hideAds();
	public float hauteurBanniere();
	public boolean adsListener();
}
