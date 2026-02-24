package com.minimal.jezz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Donnees {

    public static Preferences prefs;

    public static void Load() {
        prefs = Gdx.app.getPreferences("MinimalJezz.Data");

        if (!prefs.contains("Niveau")) {
            prefs.putInteger("Niveau", 1);
        }

        if (!prefs.contains("Langue")) {
            prefs.putInteger("Langue", 1);
        }

        if (!prefs.contains("Son")) {
            prefs.putBoolean("Son", true);
        }

        if (!prefs.contains("RateCount")) {
            prefs.putInteger("RateCount", 3);
        }

        if (!prefs.contains("Rate")) {
            prefs.putBoolean("Rate", false);
        }

        if (!prefs.contains("PROMOTE_COSMONAUT")) {
            prefs.putBoolean("PROMOTE_COSMONAUT", false);
        }

        prefs.flush();
    }

    public static void setNiveau(int val) {
        prefs.putInteger("Niveau", val);
        prefs.flush();
    }

    public static int getNiveau() {
        return prefs.getInteger("Niveau");
    }

    public static void setLangue(int val) {
        prefs.putInteger("Langue", val);
        prefs.flush();
    }

    public static int getLangue() {
        return prefs.getInteger("Langue");
    }

    public static void setSon(boolean val) {
        prefs.putBoolean("Son", val);
        prefs.flush();
    }

    public static boolean getSon() {
        return prefs.getBoolean("Son");
    }

    public static void setRateCount(int val) {
        prefs.putInteger("RateCount", val);
        prefs.flush();
    }

    public static int getRateCount() {
        return prefs.getInteger("RateCount");
    }

    public static void setRate(boolean val) {
        prefs.putBoolean("Rate", val);
        prefs.flush();
    }

    public static boolean getRate() {
        return prefs.getBoolean("Rate");
    }

    public static void setPromoteCosmonaut(boolean val) {
        prefs.putBoolean("PROMOTE_COSMONAUT", val);
        prefs.flush();
    }

    public static boolean getPromoteCosmonaut() {
        return prefs.getBoolean("PROMOTE_COSMONAUT");
    }
}
