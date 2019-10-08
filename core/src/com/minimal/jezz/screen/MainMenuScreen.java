package com.minimal.jezz.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.minimal.jezz.Couleurs;
import com.minimal.jezz.Donnees;
import com.minimal.jezz.MyGdxGame;
import com.minimal.jezz.Variables;
import com.minimal.jezz.table.BoutonsReseauxSociaux;
import com.minimal.jezz.table.NewGameAd;
import com.minimal.jezz.table.TableNotation;

public class MainMenuScreen  implements Screen {

	final MyGdxGame game;
	private OrthographicCamera camera;
	private Stage stage;
	private Skin skin;
	private TextureAtlas textureAtlas;
	private BitmapFont fontTitre;
	private static GlyphLayout glyphLayout;
	private TextButton startBouton, optionBouton, rateBouton, removeAdsBouton, moreAppsBouton;
	private Image transitionImage;
	private TextButtonStyle textButtonStyle;
	private Couleurs couleur;
	private TableNotation tableNotation;
	private BoutonsReseauxSociaux boutonsReseauxSociaux;
	private NewGameAd cosmonautAd;
	
	public MainMenuScreen(final MyGdxGame gam){
		game = gam;
		Variables.pause = true;
	
		if(!game.music.isPlaying())
			game.music.play();
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		stage = new Stage();
		skin = new Skin();
		
		couleur = new Couleurs(1);

		fontTitre = game.assets.get("fontTitre.ttf", BitmapFont.class);
		
		textureAtlas = game.assets.get("Images.pack", TextureAtlas.class);
		skin.addRegions(textureAtlas);
		glyphLayout = new GlyphLayout();
		glyphLayout.setText(game.assets.get("fontTitre.ttf", BitmapFont.class), "MINIMAL JEZZ");
		
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
		textButtonStyle.downFontColor = new Color(35/256f,59/256f,95/256f, 1);
		
		startBouton = new TextButton(gam.langue.commencer, textButtonStyle);
		startBouton.setWidth(Gdx.graphics.getWidth()/3);
		startBouton.setHeight(Gdx.graphics.getHeight()/11);
		startBouton.setX(Gdx.graphics.getWidth()/2 - startBouton.getWidth()/2);
		startBouton.setY(60*Gdx.graphics.getHeight()/100 - startBouton.getHeight()/2 - game.hauteurBanniere);
		
		optionBouton = new TextButton(gam.langue.options, textButtonStyle);
		optionBouton.setWidth(Gdx.graphics.getWidth()/3);
		optionBouton.setHeight(Gdx.graphics.getHeight()/11);
		optionBouton.setX(startBouton.getX());
		optionBouton.setY(startBouton.getY() - 1.1f*optionBouton.getHeight());
		
		rateBouton = new TextButton(gam.langue.noter, textButtonStyle);
		rateBouton.setWidth(Gdx.graphics.getWidth()/3);
		rateBouton.setHeight(Gdx.graphics.getHeight()/11);
		rateBouton.setY(optionBouton.getY() - 1.1f*rateBouton.getHeight());
		
		moreAppsBouton = new TextButton(gam.langue.plusDApp, textButtonStyle);
		moreAppsBouton.setWidth(Gdx.graphics.getWidth()/3);
		moreAppsBouton.setHeight(Gdx.graphics.getHeight()/11);
		moreAppsBouton.setY(optionBouton.getY() - 1.1f*rateBouton.getHeight());

		if(!Donnees.getRate()){
			rateBouton.setX(startBouton.getX());
			moreAppsBouton.setX(-Gdx.graphics.getWidth());
		}
		else{
			rateBouton.setX(-Gdx.graphics.getWidth());
			moreAppsBouton.setX(startBouton.getX());
		}
		
		
		removeAdsBouton = new TextButton(gam.langue.removeAds, textButtonStyle);
		removeAdsBouton.setWidth(Gdx.graphics.getWidth()/3);
		removeAdsBouton.setHeight(Gdx.graphics.getHeight()/11);
		if(!game.actionResolver.adsListener())
			removeAdsBouton.setX(startBouton.getX());	
		else 
			removeAdsBouton.setX(-Gdx.graphics.getWidth());
		/*
		if(!Donnees.getRate())
			removeAdsBouton.setY(rateBouton.getY() - 1.1f*removeAdsBouton.getHeight());
		else
			removeAdsBouton.setY(optionBouton.getY() - 1.1f*removeAdsBouton.getHeight());
		*/
		removeAdsBouton.setY(rateBouton.getY() - 1.1f*removeAdsBouton.getHeight());
		
		transitionImage = new Image(skin.getDrawable("Barre"));
		transitionImage.setWidth(Gdx.graphics.getWidth());
		transitionImage.setHeight(Gdx.graphics.getHeight());
		transitionImage.setColor(couleur.getCouleur2());
		transitionImage.setX(-Gdx.graphics.getWidth());
		transitionImage.setY(0);
		transitionImage.addAction(Actions.alpha(0));

		boutonsReseauxSociaux = new BoutonsReseauxSociaux(gam, skin);
		
		boutonsReseauxSociaux.draw(stage);
		stage.addActor(startBouton);
		stage.addActor(optionBouton);
		stage.addActor(rateBouton);
		stage.addActor(moreAppsBouton);
		stage.addActor(removeAdsBouton);
		stage.addActor(transitionImage);
		
		if(!Donnees.getRate() && Donnees.getRateCount() < 1){
			tableNotation = new TableNotation(game, skin);
			tableNotation.draw(stage);
		}
		
		if(!Donnees.getRate()){
			startBouton.addAction(Actions.parallel(Actions.alpha(0), Actions.addAction(Actions.alpha(0), optionBouton), Actions.addAction(Actions.alpha(0), rateBouton), Actions.addAction(Actions.alpha(0), removeAdsBouton)));
			startBouton.addAction(Actions.sequence(Actions.delay(0.1f), Actions.alpha(1, 0.1f), Actions.addAction(Actions.alpha(1, 0.1f), optionBouton), Actions.delay(0.1f), Actions.addAction(Actions.alpha(1, 0.1f), rateBouton), Actions.delay(0.1f), Actions.addAction(Actions.alpha(1, 0.1f), removeAdsBouton)));
		}
		else{
			startBouton.addAction(Actions.parallel(Actions.alpha(0), Actions.addAction(Actions.alpha(0), optionBouton), Actions.addAction(Actions.alpha(0), moreAppsBouton), Actions.addAction(Actions.alpha(0), removeAdsBouton)));
			startBouton.addAction(Actions.sequence(Actions.delay(0.1f), Actions.alpha(1, 0.1f), Actions.addAction(Actions.alpha(1, 0.1f), optionBouton), Actions.delay(0.1f), Actions.addAction(Actions.alpha(1, 0.1f), moreAppsBouton), Actions.delay(0.1f), Actions.addAction(Actions.alpha(1, 0.1f), removeAdsBouton)));
		}
		
		/*
		 * TEST PUBLICITÉ NOUVEAU JEU
		 */
		if(!Donnees.getPromoteCosmonaut())
			if(Donnees.getRateCount() < 2 || Donnees.getNiveau() > 3){
				cosmonautAd = new NewGameAd(skin, "https://play.google.com/store/apps/details?id=com.cosmonaut.android");
				LabelStyle labelStyle = new LabelStyle(game.assets.get("fontTextTableJeu.ttf", BitmapFont.class), Color.WHITE);
				cosmonautAd.setLabelStyle(labelStyle);
				cosmonautAd.setTextButtonStyle(textButtonStyle);
				cosmonautAd.create(	skin.getDrawable("imageTable"),
									"Images/CosmonautImage.png",
									0.85f*Gdx.graphics.getWidth(), 
									0.65f*Gdx.graphics.getHeight(), 
									game.langue.nouveauJeu, 
									game.langue.jouer);
				cosmonautAd.setBackgroundColor(240/256f,84/256f,79/256f,1); 
				cosmonautAd.addToStage(stage);
			}
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(couleur.getCouleur2().r, couleur.getCouleur2().g, couleur.getCouleur2().b, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
		fontTitre.draw(game.batch, glyphLayout, Gdx.graphics.getWidth()/2 - glyphLayout.width/2, 85*Gdx.graphics.getHeight()/100 - game.hauteurBanniere);
		game.batch.end();
		
		stage.act();
		stage.draw();
		
		if (Gdx.input.isKeyJustPressed(Keys.BACK))	
			Gdx.app.exit();
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		
		if(!Donnees.getPromoteCosmonaut())
			if(Donnees.getRateCount() < 2 || Donnees.getNiveau() > 3)
				cosmonautAd.action();
		
		if(!Donnees.getRate() && Donnees.getRateCount() < 1){
			tableNotation.action();
		}
		boutonsReseauxSociaux.action();
		
		startBouton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				Variables.couleurSelectionee = MathUtils.random(1, 15);
				transitionImage.addAction(Actions.moveTo(0, 0));
				transitionImage.addAction(Actions.sequence(Actions.alpha(1, 0.2f),	 
															Actions.run(new Runnable() {
													            @Override
													            public void run() {
																	game.setScreen(new NiveauxScreen(game));
													            }})));
			}
		});
		
		optionBouton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				transitionImage.addAction(Actions.moveTo(0, 0));
				transitionImage.addAction(Actions.sequence(Actions.alpha(1, 0.2f),	 
															Actions.run(new Runnable() {
													            @Override
													            public void run() {
																	game.setScreen(new OptionScreen(game));
													            }})));
			}
		});
		
		rateBouton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				Donnees.setRate(true);
		       	Gdx.net.openURI(Variables.GOOGLE_PLAY_GAME_URL);
		       	//Gdx.net.openURI(Variables.AMAZON_GAME_URL);
				
			}
		});
		
		removeAdsBouton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				game.actionResolver.removeAds();
				
			}
		});
		
		moreAppsBouton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
		       	Gdx.net.openURI(Variables.GOOGLE_PLAY_STORE_URL);
		        //Gdx.net.openURI(Variables.AMAZON_STORE_URL);
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
		System.out.println("------------------------Main menu screen disposed");
		stage.dispose();
		skin.dispose();
	}
}
