package com.minimal.jezz.desktop;

import com.minimal.jezz.ActionResolver;

public class ActionResolverDesktop implements ActionResolver{
	
	//Google Billing
	public void removeAds(){
		System.out.println("Remove Ads");
	}
	
	public void processPurchases(){
		System.out.println("Process purchases");
	}
	
	//Publicit�s
	public void showOrLoadInterstital(){
		System.out.println("Show Or Load Interstital");
	}
	
	public void LoadInterstital(){
		//System.out.println("Load Interstital");
	}
	
	public void showAdsBottom(){
		System.out.println("Show Ads Bottom");
	}
	
	public void showAdsTop(){
		//System.out.println("Show Ads Top");
	}
	
	public void hideAds(){
		System.out.println("Hide Ads");
	}
	
	public float hauteurBanniere(){
		return 0;
	}
	
	public boolean adsListener(){
		return false;
	}
	
}
