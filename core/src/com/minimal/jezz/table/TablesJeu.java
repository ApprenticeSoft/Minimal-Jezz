package com.minimal.jezz.table;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.minimal.jezz.Donnees;
import com.minimal.jezz.LevelHandler;
import com.minimal.jezz.MyGdxGame;
import com.minimal.jezz.Variables;
import com.minimal.jezz.body.Barre;
import com.minimal.jezz.screen.GameScreen;
import com.minimal.jezz.screen.MainMenuScreen;

public class TablesJeu {

	final MyGdxGame game;
	
	private Table tableDebut, tableFin, tableJeuFini, tablePerdu, tablePause;
	private TextButtonStyle textButtonStyle, textButtonStyleDebut;
	private TextButton okBouton, nextBouton, replayBouton, replayBouton2, menuBouton, menuBouton2, menuBouton3, resumeBouton, quitBouton;
	private LabelStyle labelStyle, labelStyleDebut;
	private Image imageDebut, imageFin, imagePause;
	private GlyphLayout glyphLayout;
	
	
	public TablesJeu(final MyGdxGame game, Stage stage, Skin skin, LevelHandler levelHandler, Color couleur1, Color couleur2){
		this.game = game;
		
		 //Début du jeu
        textButtonStyleDebut = new TextButtonStyle();
        if(Gdx.graphics.getHeight() >= 1000){
			textButtonStyleDebut.up = skin.getDrawable("BoutonDebut");
			textButtonStyleDebut.down = skin.getDrawable("BoutonDebutChecked");
		}
		else{
			textButtonStyleDebut.up = skin.getDrawable("BoutonDebut");
			textButtonStyleDebut.down = skin.getDrawable("BoutonDebutChecked");
		}
		textButtonStyleDebut.font = game.assets.get("font1.ttf", BitmapFont.class);
		textButtonStyleDebut.fontColor = couleur1;
		textButtonStyleDebut.downFontColor = couleur1;
		
		okBouton = new TextButton("OK", textButtonStyleDebut);
		
		labelStyleDebut = new LabelStyle(game.assets.get("font1.ttf", BitmapFont.class), couleur1);
        
		tableDebut = new Table();
		tableDebut.add(new Label(game.langue.balles + " : ", labelStyleDebut)).right();
		tableDebut.add(new Label("" + levelHandler.getNbBalles(), labelStyleDebut)).left().row();
		tableDebut.add(new Label(game.langue.vitesse + " : ", labelStyleDebut)).right();
		tableDebut.add(new Label("" + (int)(100*levelHandler.getVitesse()/1.5) + " %", labelStyleDebut)).left().row();
		tableDebut.add(new Label(game.langue.objectif + " : ", labelStyleDebut)).right();
		tableDebut.add(new Label("" + (int)levelHandler.getObjectif() + " %", labelStyleDebut)).left().row();
		tableDebut.add(okBouton).height(Gdx.graphics.getHeight()/18).width(Gdx.graphics.getWidth()/5).colspan(2).padTop(Gdx.graphics.getHeight()/50);
		tableDebut.setX(Gdx.graphics.getWidth()/2);
		tableDebut.setY(Gdx.graphics.getHeight()/2);
	
		imageDebut = new Image(skin.getDrawable("BoutonChecked"));
		imageDebut.setColor(couleur2);
		imageDebut.setWidth(1.2f*tableDebut.getPrefWidth());
		imageDebut.setHeight(1.2f*tableDebut.getPrefHeight());
		imageDebut.setX(tableDebut.getX() - imageDebut.getWidth()/2);
		imageDebut.setY(tableDebut.getY() - imageDebut.getHeight()/2);
				
		//Table de fin de niveau
        textButtonStyle = new TextButtonStyle();
        if(Gdx.graphics.getHeight() >= 1000){
			textButtonStyle.up = skin.getDrawable("Bouton");
			textButtonStyle.down = skin.getDrawable("BoutonChecked");
		}
		else{
			textButtonStyle.up = skin.getDrawable("BoutonPetit");
			textButtonStyle.down = skin.getDrawable("BoutonPetitChecked");
		}
		textButtonStyle.font = game.assets.get("font1.ttf", BitmapFont.class);
		textButtonStyle.fontColor = Color.WHITE;
		textButtonStyle.downFontColor = couleur2;
		
		nextBouton = new TextButton(game.langue.suivant, textButtonStyle);	
		replayBouton = new TextButton(game.langue.menu, textButtonStyle);
		
		labelStyle = new LabelStyle(game.assets.get("fontTextTableJeu.ttf", BitmapFont.class), Color.WHITE);
	
		tableFin = new Table();
		tableFin.row().colspan(2);
		tableFin.add(new Label(game.langue.niveauComplete, labelStyle)).padBottom(Gdx.graphics.getHeight()/22);
		tableFin.row().height(Gdx.graphics.getHeight()/13).width(Gdx.graphics.getWidth()/4);
		tableFin.add(nextBouton).spaceRight(Gdx.graphics.getWidth()/100);
		tableFin.add(replayBouton);
		tableFin.setX(-Gdx.graphics.getWidth()/2);
		tableFin.setY(Gdx.graphics.getHeight()/2 + tableFin.getPrefHeight()/4);
	
		imageFin = new Image(skin.getDrawable("imageTable"));
		imageFin.setColor(couleur2);
		imageFin.setWidth(1.15f*tableFin.getPrefWidth());
		imageFin.setHeight(1.15f*tableFin.getPrefHeight());
		imageFin.setX(tableFin.getX() - imageFin.getWidth()/2);
		imageFin.setY(tableFin.getY() - imageFin.getHeight()/2);
		imageFin.addAction(Actions.alpha(0));
        
        imagePause = new Image(skin.getDrawable("Fond"));
		imagePause.setWidth(Gdx.graphics.getWidth());
		imagePause.setHeight(Gdx.graphics.getHeight());
		imagePause.setX(0);
		imagePause.setY(0);
		imagePause.addAction(Actions.alpha(0));
		
		//Table de niveau perdu
		tablePerdu = new Table();
		replayBouton2 = new TextButton(game.langue.rejouer, textButtonStyle);	
		menuBouton2 = new TextButton(game.langue.menu, textButtonStyle);
		
		tablePerdu.row().colspan(2);
		tablePerdu.add(new Label(game.langue.perdu, labelStyle)).padBottom(Gdx.graphics.getHeight()/22);
		tablePerdu.row().height(Gdx.graphics.getHeight()/13).width(Gdx.graphics.getWidth()/4);
		tablePerdu.add(replayBouton2);
		tablePerdu.add(menuBouton2);
		tablePerdu.setX(-Gdx.graphics.getWidth()/2);
		tablePerdu.setY(Gdx.graphics.getHeight()/2 + tablePerdu.getPrefHeight()/4);
		tablePerdu.addAction(Actions.alpha(0));
		
		//Table de pause
		glyphLayout = new GlyphLayout();
		glyphLayout.setText(game.assets.get("fontTitre.ttf", BitmapFont.class), game.langue.recommencer);

		resumeBouton = new TextButton(game.langue.reprendre, textButtonStyle);
		menuBouton = new TextButton(game.langue.menu, textButtonStyle);
		quitBouton = new TextButton(game.langue.quitter, textButtonStyle);
		
		tablePause = new Table();
		if(Donnees.getLangue() == 2)
			tablePause.defaults().height(Gdx.graphics.getHeight()/12).width(0.5f*glyphLayout.width).space(Gdx.graphics.getHeight()/100);
		else
			tablePause.defaults().height(Gdx.graphics.getHeight()/12).width(0.75f*glyphLayout.width).space(Gdx.graphics.getHeight()/100);
		tablePause.add(resumeBouton).row();
		tablePause.add(menuBouton).row();
		tablePause.add(quitBouton).row();
		tablePause.setSize(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2);
		tablePause.setX(2 * Gdx.graphics.getWidth());
		tablePause.setY(Gdx.graphics.getHeight()/2 - tablePause.getHeight()/2);
		
		//Table de fin de jeu
		menuBouton3 = new TextButton(game.langue.menu, textButtonStyle);
		
		tableJeuFini = new Table();
		tableJeuFini.add(new Label(game.langue.jeuComplete, labelStyle)).padBottom(Gdx.graphics.getHeight()/22);
		tableJeuFini.row().height(Gdx.graphics.getHeight()/13).width(Gdx.graphics.getWidth()/4);
		tableJeuFini.add(menuBouton3);
		tableJeuFini.setX(-Gdx.graphics.getWidth()/2);
		tableJeuFini.setY(Gdx.graphics.getHeight()/2);
		

		stage.addActor(imagePause);
		stage.addActor(imageFin);
		stage.addActor(tableFin);
		stage.addActor(tablePause);
		stage.addActor(tablePerdu);
		stage.addActor(imageDebut);
		stage.addActor(tableDebut);
		stage.addActor(tableJeuFini);	
	}
	
	public void pause(){
	  	Variables.pause = true;
       	Variables.BOX_STEP = 0;
       	imagePause.addAction(Actions.alpha(0.75f, 0.25f));
		tablePause.addAction(Actions.sequence(Actions.moveTo(Gdx.graphics.getWidth()/2 - tablePause.getWidth()/2, tablePause.getY()),
												Actions.alpha(1, 0.25f)));
	}
	
	public void pauseFinie(){
		Variables.pause = false;
		Variables.BOX_STEP = 1/60f;
       	imagePause.addAction(Actions.alpha(0, 0.25f));
		tablePause.addAction(Actions.sequence(Actions.alpha(0, 0.25f),
												Actions.moveTo(2 * Gdx.graphics.getWidth(), tablePause.getY())));
	}
	
	public void perdu(){
		Variables.BOX_STEP = 0;

		imageFin.setWidth(tablePerdu.getPrefWidth() + Gdx.graphics.getWidth()/20);
		imageFin.setHeight(tablePerdu.getPrefHeight() + Gdx.graphics.getWidth()/20);
		
		imagePause.addAction(Actions.alpha(0.75f, 0.25f));	
		tablePerdu.addAction(Actions.sequence(Actions.moveTo(Gdx.graphics.getWidth()/2, tablePerdu.getY()),
												Actions.alpha(1, 0.25f)));
		imageFin.addAction(Actions.sequence(Actions.moveTo(tablePerdu.getX() - imageFin.getWidth()/2, 
															tablePerdu.getY() - imageFin.getHeight()/2),
											Actions.alpha(1, 0.25f)));		
	}
	
	public void gagne(){
		Variables.BOX_STEP = 0;

		imageFin.setWidth(tableFin.getPrefWidth() + Gdx.graphics.getWidth()/20);
		imageFin.setHeight(tableFin.getPrefHeight() + Gdx.graphics.getWidth()/20);
		
		imagePause.addAction(Actions.alpha(0.75f, 0.25f));
		tableFin.addAction(Actions.sequence(Actions.moveTo(Gdx.graphics.getWidth()/2, tablePerdu.getY()),
											Actions.alpha(1, 0.25f)));
		imageFin.addAction(Actions.sequence(Actions.moveTo(tableFin.getX() - imageFin.getWidth()/2, 
															tableFin.getY() - imageFin.getHeight()/2),
											Actions.alpha(1, 0.25f)));	
	}
	
	public void jeuFini(){
		Variables.BOX_STEP = 0;

		imageFin.setWidth(tableJeuFini.getPrefWidth() + Gdx.graphics.getWidth()/20);
		imageFin.setHeight(tableJeuFini.getPrefHeight() + Gdx.graphics.getWidth()/20);
		
		imagePause.addAction(Actions.alpha(0.75f, 0.25f));
		tableJeuFini.addAction(Actions.sequence(Actions.moveTo(Gdx.graphics.getWidth()/2, tablePerdu.getY()),
												Actions.alpha(1, 0.25f)));
		imageFin.addAction(Actions.sequence(Actions.moveTo(tableJeuFini.getX() - imageFin.getWidth()/2, 
															tableJeuFini.getY() - imageFin.getHeight()/2),
											Actions.alpha(1, 0.25f)));	
	}
	
	public void boutonListener(final World world, final Array<Barre> barres){
		nextBouton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				//Effacement du niveau actuel
				try{
					for(Barre barre : barres){
					world.destroyBody(barre.barre1);
					}
					barres.removeRange(0, barres.size-1);
				}catch(Exception e){
					
				}
				
				//Sortie de la pause
				Variables.BOX_STEP = 1/60f;
				
				//Choix de la couleur du niveau suivant
				int couleur = Variables.couleurSelectionee;
				while(couleur == Variables.couleurSelectionee){
					Variables.couleurSelectionee = MathUtils.random(1, 10);
				}
				
				//Transfert vers le niveau suivant s'il y en a encore un
				if(Variables.niveauSelectione < 25)
					Variables.niveauSelectione +=1;
				try{
					game.setScreen(new GameScreen(game));
				}
				catch(Exception e){
					game.setScreen(new MainMenuScreen(game));
				}
			}
		});
		
		replayBouton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				try{
					for(Barre barre : barres){
					world.destroyBody(barre.barre1);
					}
					barres.removeRange(0, barres.size-1);
				}catch(Exception e){
					
				}
				
				Variables.BOX_STEP = 1/60f;
				game.setScreen(new MainMenuScreen(game));
			}
		});
		replayBouton2.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				for(Barre barre : barres){
				world.destroyBody(barre.barre1);
				}
				barres.removeRange(0, barres.size-1);
				
				Variables.BOX_STEP = 1/60f;
				game.setScreen(new GameScreen(game));
			}
		});
		
		menuBouton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				game.setScreen(new MainMenuScreen(game));
			}
		});
		
		menuBouton2.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				game.setScreen(new MainMenuScreen(game));
			}
		});
		
		menuBouton3.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				game.setScreen(new MainMenuScreen(game));
			}
		});
		
		resumeBouton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				Variables.pause = false;
		       	Variables.BOX_STEP = 1/60f;
		       	imagePause.addAction(Actions.alpha(0, 0.25f));
		       	tablePause.addAction(Actions.moveTo(2 * Gdx.graphics.getWidth(),
							tablePause.getY(), 
							0.25f));
			}
		});
		
		quitBouton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				Gdx.app.exit();
			}
		});
		
		okBouton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				Variables.debut = false;
		       	Variables.BOX_STEP = 1/60f;
		       	imageDebut.addAction(Actions.moveTo(-Gdx.graphics.getWidth(),
						imageDebut.getY(), 
						0.25f));
		       	tableDebut.addAction(Actions.moveTo(-Gdx.graphics.getWidth(),
							tableDebut.getY(), 
							0.25f));
			}
		});
	}
}
