package com.minimal.jezz;


public class Variables {
	
	//Constantes du World
	public static float WORLD_TO_BOX = 0.02f;
	public static float BOX_TO_WORLD = 1/WORLD_TO_BOX;
	public static float BOX_STEP = 1/60f; 
	public static int BOX_VELOCITY_ITERATIONS = 6;
	public static int BOX_POSITION_ITERATIONS = 2;
	
	//Gestion des niveaux
	public static int nombreNiveaux = 25;
	public static int niveauSelectione = 1;
	public static float objectif = 70;
	public static int couleurSelectionee = 1;
	
	public static boolean d�but = true;
	public static boolean pause = true;
	public static boolean perdu = true;
	public static boolean gagn� = true;
	public static int INTERSTITIAL_TRIGGER = 2;
	
	//Propri�t�s de la balle
	public static float vitesseBalle = 0.7f;
	
	//Gestion des barres
	public static boolean spawn = false;
	public static float largeurBordure; 
	public static float posBordure = 0.05f;
	
	//Liens vers les App Store et r�seaux sociaux
	public static final String GOOGLE_PLAY_GAME_URL = "https://play.google.com/store/apps/details?id=com.minimal.jezz.android";
	public static final String GOOGLE_PLAY_STORE_URL = "https://play.google.com/store/apps/developer?id=Apprentice+Soft";
	public static final String AMAZON_GAME_URL = "http://www.amazon.com/gp/mas/dl/android?p=com.minimal.jezz.android";
	public static final String AMAZON_STORE_URL = "http://www.amazon.com/gp/mas/dl/android?p=com.minimal.jezz.android&showAll=1";
	//public static final String FACEBOOK_URL = "https://m.facebook.com/profile.php?id=157533514581396";
	//public static final String FACEBOOK_APP_URL = "fb://page/157533514581396";
	//public static final String TWITTER_URL = "https://twitter.com/ApprenticeSoft";
	
	
}
