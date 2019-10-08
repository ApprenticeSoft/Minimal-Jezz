package com.minimal.jezz.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.minimal.jezz.ActionBouton;
import com.minimal.jezz.Couleurs;
import com.minimal.jezz.Donnees;
import com.minimal.jezz.screen.MainMenuScreen;
import com.minimal.jezz.Variables;
import com.minimal.jezz.MyGdxGame;

public class NiveauxScreen implements Screen{

	final MyGdxGame game;
	OrthographicCamera camera;
	private Stage stage;
	private Skin skin;
	private TextureAtlas textureAtlas;
	
	private TextButtonStyle textButtonStyle, textButtonStyleInactif;
	private Array<TextButton> niveaux;
	private TextButton retourBouton;
	private Image transitionImage;
	private Table tableNiveaux;
	private ActionBouton action;
	private Couleurs couleur;
	private Label titre;
	private LabelStyle titreStyle;
	
	public NiveauxScreen(final MyGdxGame gam){
		game = gam;

		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		couleur = new Couleurs(1);
		
		action = new ActionBouton();
		
		stage = new Stage();
		skin = new Skin();
		textureAtlas = game.assets.get("Images.pack", TextureAtlas.class);
		skin.addRegions(textureAtlas);	
		
		titreStyle = new LabelStyle(game.assets.get("fontTitre.ttf", BitmapFont.class), Color.WHITE);
		titre = new Label(gam.langue.choisirNiveau, titreStyle);
		titre.setAlignment(Align.center);
		titre.setX(Gdx.graphics.getWidth()/2 - titre.getWidth()/2);
		titre.setY(83*Gdx.graphics.getHeight()/100 - titre.getHeight()/2);
		
		textButtonStyle = new TextButtonStyle();
		if(Gdx.graphics.getHeight() > 1000){
			textButtonStyle.up = skin.getDrawable("Bouton");
			textButtonStyle.down = skin.getDrawable("BoutonChecked");
		}
		else{
			textButtonStyle.up = skin.getDrawable("BoutonPetit");
			textButtonStyle.down = skin.getDrawable("BoutonPetitChecked");
		}
		textButtonStyle.font = game.assets.get("font1.ttf", BitmapFont.class);
		textButtonStyle.fontColor = Color.WHITE;
		textButtonStyle.downFontColor = new Color(35/256f,59/256f,95/256f, 1);
		
		//Style des boutons des niveaux non débloqués
		textButtonStyleInactif = new TextButtonStyle();
		if(Gdx.graphics.getHeight() > 1000){
			textButtonStyleInactif.up = skin.getDrawable("BoutonInactif");
		}
		else
			textButtonStyleInactif.up = skin.getDrawable("BoutonInactifPetit");
		textButtonStyleInactif.font = game.assets.get("font1.ttf", BitmapFont.class);
		textButtonStyleInactif.fontColor = new Color().set(0, 0, 0, 0.7f);
		
		//Affichage des niveaux
		tableNiveaux = new Table();
		tableNiveaux.defaults().width(Gdx.graphics.getWidth()/7).height(Gdx.graphics.getWidth()/7).space(Gdx.graphics.getWidth()/30);
		
		niveaux = new Array<TextButton>();
		
		for(int i = 0; i < Variables.nombreNiveaux; i++){
			TextButton textButton = new TextButton("" + (i + 1), textButtonStyle);
			niveaux.add(textButton);
			if(i + 1 == 5 || i + 1 == 10 || i + 1 == 15 || i + 1 == 20) 
				tableNiveaux.add(textButton).row();
			else 
				tableNiveaux.add(textButton);
		}
		//Image noire permetant une transition entre les différents écrans
		transitionImage = new Image(skin.getDrawable("Barre"));
		transitionImage.setWidth(Gdx.graphics.getWidth());
		transitionImage.setHeight(Gdx.graphics.getHeight());
		transitionImage.setColor(couleur.getCouleur2());
		transitionImage.setX(-Gdx.graphics.getWidth());
		transitionImage.setY(0);
		transitionImage.addAction(Actions.alpha(0));
		
		//tableNiveaux.setX(tableNiveauxInactif);
		tableNiveaux.setX(Gdx.graphics.getWidth()/2);
		tableNiveaux.setY(45*Gdx.graphics.getHeight()/100);
		
		stage.addActor(tableNiveaux);
		stage.act();
		stage.draw();
		
		//Bouton retour
		retourBouton = new TextButton("<", textButtonStyle);
		retourBouton.setWidth(Gdx.graphics.getWidth()/7);
		retourBouton.setHeight(Gdx.graphics.getWidth()/7);
		retourBouton.setX(niveaux.get(0).localToStageCoordinates(new Vector2(0,0)).x);
		retourBouton.setY(retourBouton.getX());

		stage.addActor(titre);
		stage.addActor(retourBouton);
		stage.addActor(transitionImage);
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(couleur.getCouleur2().r, couleur.getCouleur2().g, couleur.getCouleur2().b, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		game.batch.setProjectionMatrix(camera.combined);
		
		//Activation et désactivation des boutons en fonctions des niveaux débloqués
		for(int i = 0; i < niveaux.size; i++){
			if((i + 1) <= Donnees.getNiveau()){
				niveaux.get(i).setStyle(textButtonStyle);
				niveaux.get(i).setTouchable(Touchable.enabled);
			}
			else {
				niveaux.get(i).setStyle(textButtonStyleInactif);
				niveaux.get(i).setTouchable(Touchable.disabled);
			}
		}
		
		retourBouton.setStyle(textButtonStyle);
		retourBouton.setTouchable(Touchable.enabled);
		
		stage.act();
		stage.draw();	
		
		 //Utilisation du bouton BACK
	    if (Gdx.input.isKeyJustPressed(Keys.BACK))
	    	game.setScreen(new MainMenuScreen(game));
		
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		Gdx.input.setCatchBackKey(true);
		
		for(int i = 0; i < niveaux.size; i++){
			if(niveaux.get(i).getStyle() == textButtonStyle)
				action.niveauListener(game, niveaux.get(i), (i+1));
		}
		
		retourBouton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				transitionImage.addAction(Actions.moveTo(0, 0));
				transitionImage.addAction(Actions.sequence(Actions.alpha(1, 0.2f),	 
															Actions.run(new Runnable() {
													            @Override
													            public void run() {
																	game.setScreen(new MainMenuScreen(game));
													            }})));
				
			}
		});
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub	
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub	
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub	
	}

	@Override
	public void dispose() {
		System.out.println("------------------------Niveaux screen disposed");
		stage.dispose();
		skin.dispose();
	}
	
}
