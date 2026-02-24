package com.minimal.jezz;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.minimal.jezz.screen.GameScreen;
import com.minimal.jezz.screen.MainMenuScreen;

public class ActionBouton {

    public ActionBouton() {
    }

    public void niveauListener(final MyGdxGame game, TextButton bouton, final int niveau) {
        bouton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Variables.niveauSelectione = niveau;
                try {
                    game.music.stop();
                    game.setScreen(new GameScreen(game));
                } catch (Exception e) {
                    game.setScreen(new MainMenuScreen(game));
                }
            }
        });
    }
}
