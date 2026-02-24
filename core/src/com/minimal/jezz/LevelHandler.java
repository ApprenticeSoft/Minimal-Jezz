package com.minimal.jezz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.minimal.jezz.body.Balle;

public class LevelHandler {
    private final Array<Balle> balles;
    private final World world;
    private final Camera camera;
    private float objectif;
    private float vitesse;
    private int nbBalles;

    public LevelHandler(World world, Camera camera, Array<Balle> balles) {
        this.balles = balles;
        this.world = world;
        this.camera = camera;

        objectif = 0;
        vitesse = 0;
        nbBalles = 0;
    }

    public void loadLevel(int niveauChoisi) {
        FileHandle file = Gdx.files.internal("Niveaux.txt");
        String[] fileContent = file.readString().split("\\n");
        for (String line : fileContent) {
            String[] blockData = line.split(",");
            if (blockData.length != 4) {
                continue;
            }
            if (Integer.parseInt(blockData[0]) != niveauChoisi) {
                continue;
            }
            try {
                nbBalles = Integer.parseInt(blockData[1]);
                vitesse = Float.parseFloat(blockData[2]);
                objectif = Float.parseFloat(blockData[3]);

                for (int j = 0; j < nbBalles; j++) {
                    Balle balle = new Balle(world, camera, camera.viewportWidth / 5f, camera.viewportHeight / 2f);
                    balles.add(balle);
                }

                Variables.vitesseBalle = vitesse;
                Variables.objectif = objectif;
                return;
            } catch (NumberFormatException ignored) {
                return;
            }
        }
    }

    public void setNbBalles(int balle) {
        nbBalles = balle;
    }

    public int getNbBalles() {
        return nbBalles;
    }

    public void setVitesse(float speed) {
        vitesse = speed;
    }

    public float getVitesse() {
        return vitesse;
    }

    public void setObjectif(float obj) {
        objectif = obj;
    }

    public float getObjectif() {
        return objectif;
    }
}
