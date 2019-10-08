package com.minimal.jezz;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.minimal.jezz.MyGdxGame;
import com.minimal.jezz.Variables;
import com.minimal.jezz.screen.GameScreen;

public class ActionBouton {
	
	public ActionBouton(){
	}
	
	public void niveauListener(final MyGdxGame game, TextButton bouton, final int niveau){
		bouton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				Variables.niveauSelectione = niveau;
				try{
					game.music.stop();
					game.getScreen().dispose();
					game.setScreen(new GameScreen(game));
				}
					catch(Exception e){
					System.out.println("Le niveau n'existe pas !");
				}
				System.out.println("Numéro du niveau : " + Variables.niveauSelectione);
			}
		});
	}
}
