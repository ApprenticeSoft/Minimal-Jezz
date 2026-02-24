package com.minimal.jezz;

public class Variables {

    // World constants
    public static float WORLD_TO_BOX = 0.02f;
    public static float BOX_TO_WORLD = 1 / WORLD_TO_BOX;
    public static float BOX_STEP = 1 / 60f;
    public static int BOX_VELOCITY_ITERATIONS = 6;
    public static int BOX_POSITION_ITERATIONS = 2;

    // Level and progression
    public static int nombreNiveaux = 25;
    public static int niveauSelectione = 1;
    public static float objectif = 70;
    public static int couleurSelectionee = 1;

    // Runtime game state
    public static boolean debut = true;
    public static boolean pause = true;
    public static boolean perdu = true;
    public static boolean gagne = true;

    // Ball properties
    public static float vitesseBalle = 0.7f;
    public static float vitesseBalleScale = 1f;

    // Bar drawing/gameplay
    public static boolean spawn = false;
    public static float largeurBordure;
    public static float posBordure = 0.05f;

    // Store links
    public static final String GOOGLE_PLAY_GAME_URL = "https://play.google.com/store/apps/details?id=com.minimal.jezz.android";
    public static final String GOOGLE_PLAY_STORE_URL = "https://play.google.com/store/apps/developer?id=Apprentice+Soft";
    public static final String AMAZON_GAME_URL = "http://www.amazon.com/gp/mas/dl/android?p=com.minimal.jezz.android";
    public static final String AMAZON_STORE_URL = "http://www.amazon.com/gp/mas/dl/android?p=com.minimal.jezz.android&showAll=1";
}
