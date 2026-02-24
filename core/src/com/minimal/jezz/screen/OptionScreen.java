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
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.minimal.jezz.Couleurs;
import com.minimal.jezz.Donnees;
import com.minimal.jezz.Variables;
import com.minimal.jezz.screen.MainMenuScreen;
import com.minimal.jezz.MyGdxGame;
import com.minimal.jezz.ui.UiActorUtils;

public class OptionScreen implements Screen{
	private static final float OPTION_TITLE_WIDTH_RATIO = 0.25f;
	private static final float OPTION_TITLE_Y_RATIO = 0.85f;

	final MyGdxGame game;
	OrthographicCamera camera;
	private Stage stage;
	private Skin skin;
	private TextureAtlas textureAtlas;
	private TextButton langueBouton, rateBouton, moreAppsBouton, retourBouton, sonBouton, 
						englishBouton, francaisBouton, espanolBouton,
						onBouton, offBouton;
	private TextButtonStyle menuButtonStyle, optionButtonStyle;
	private Image transitionImage;
	private ButtonGroup<TextButton> langueGroupe, sonGroupe;
	private float boutonWidth, boutonHeight, boutonEcart, boutonInactifX, langueY, sonY;
	private boolean langueActif, sonActif;
	private Couleurs couleur;
	private boolean listenersBound;
	private final boolean webBuild;
	private final float baseScreenWidth;
	private final float baseScreenHeight;
	private BitmapFont optionTitleFont;
	private GlyphLayout optionTitleLayout;

	public OptionScreen(final MyGdxGame gam){
		game = gam;
		listenersBound = false;
		webBuild = Gdx.app.getType() == com.badlogic.gdx.Application.ApplicationType.WebGL;
		baseScreenWidth = Math.max(1f, Gdx.graphics.getWidth());
		baseScreenHeight = Math.max(1f, Gdx.graphics.getHeight());
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		stage = new Stage();
		skin = new Skin();
		textureAtlas = game.assets.get("Images.pack", TextureAtlas.class);
		skin.addRegions(textureAtlas);	
		
		couleur = new Couleurs(1);
		
		menuButtonStyle = new TextButtonStyle();
		if(Gdx.graphics.getHeight() > 1000){
			menuButtonStyle.up = skin.getDrawable("Bouton");
			menuButtonStyle.down = skin.getDrawable("BoutonChecked");
		}
		else{
			menuButtonStyle.up = skin.getDrawable("BoutonPetit");
			menuButtonStyle.down = skin.getDrawable("BoutonPetitChecked");
		}
		menuButtonStyle.font = game.assets.get("fontOption.ttf", BitmapFont.class);
		menuButtonStyle.fontColor = Color.WHITE;
		menuButtonStyle.downFontColor = new Color(35/256f,59/256f,95/256f, 1);
		
		optionButtonStyle = new TextButtonStyle();
		if(Gdx.graphics.getHeight() > 1000){
			optionButtonStyle.up = skin.getDrawable("Bouton");
			optionButtonStyle.down = skin.getDrawable("BoutonChecked");
			optionButtonStyle.checked = skin.getDrawable("BoutonChecked");
		}
		else{
			optionButtonStyle.up = skin.getDrawable("BoutonPetit");
			optionButtonStyle.down = skin.getDrawable("BoutonPetitChecked");
			optionButtonStyle.checked = skin.getDrawable("BoutonPetitChecked");
		}
			optionButtonStyle.font = game.assets.get("font1.ttf", BitmapFont.class);
			optionButtonStyle.fontColor = Color.WHITE;
			optionButtonStyle.downFontColor = new Color(35/256f,59/256f,95/256f, 1);
			optionButtonStyle.checkedFontColor = new Color(35/256f,59/256f,95/256f, 1);

			optionTitleFont = game.assets.get("fontTitre.ttf", BitmapFont.class);
			optionTitleFont.setUseIntegerPositions(false);
			optionTitleLayout = new GlyphLayout();
		
		boutonWidth = 48*Gdx.graphics.getWidth()/100;
		boutonHeight = 18*Gdx.graphics.getWidth()/100;
		boutonEcart = boutonHeight + Gdx.graphics.getHeight()/50;
		
		//Menu général
		langueBouton = new TextButton(gam.langue.langage.toUpperCase(), menuButtonStyle);
		langueBouton.setWidth(boutonWidth);
		langueBouton.setHeight(boutonHeight);
		langueBouton.setX(Gdx.graphics.getWidth()/2 - langueBouton.getWidth()/2);
		if(!Donnees.getRate())
			langueBouton.setY(65*Gdx.graphics.getHeight()/100);
		else
			langueBouton.setY(58*Gdx.graphics.getHeight()/100);
		sonBouton = new TextButton(gam.langue.sons.toUpperCase(), menuButtonStyle);
		sonBouton.setWidth(boutonWidth);
		sonBouton.setHeight(boutonHeight);
		sonBouton.setX(Gdx.graphics.getWidth()/2 - sonBouton.getWidth()/2);
		sonBouton.setY(langueBouton.getY() - boutonEcart);
		moreAppsBouton = new TextButton(gam.langue.plusDApp.toUpperCase(), menuButtonStyle);
		moreAppsBouton.setWidth(boutonWidth);
		moreAppsBouton.setHeight(boutonHeight);
		moreAppsBouton.setX(Gdx.graphics.getWidth()/2 - moreAppsBouton.getWidth()/2);
		moreAppsBouton.setY(sonBouton.getY() - boutonEcart);
		rateBouton = new TextButton(gam.langue.noter.toUpperCase(), menuButtonStyle);
		rateBouton.setWidth(boutonWidth);
		rateBouton.setHeight(boutonHeight);
		if(!Donnees.getRate())
			rateBouton.setX(Gdx.graphics.getWidth()/2 - rateBouton.getWidth()/2);
		else
			rateBouton.setX(-Gdx.graphics.getWidth());
		rateBouton.setY(moreAppsBouton.getY() - boutonEcart);
		if (webBuild) {
			moreAppsBouton.setX(-Gdx.graphics.getWidth());
			rateBouton.setX(-Gdx.graphics.getWidth());
		}
		
		retourBouton = new TextButton("<", menuButtonStyle);
		retourBouton.setWidth(Gdx.graphics.getWidth()/7);
		retourBouton.setHeight(Gdx.graphics.getWidth()/7);
		retourBouton.setX(Gdx.graphics.getWidth()/10);
		retourBouton.setY(Gdx.graphics.getWidth()/10);
		//Menu langue
		englishBouton = new TextButton("English", optionButtonStyle);
		francaisBouton = new TextButton("Français", optionButtonStyle);
		espanolBouton = new TextButton("Español", optionButtonStyle);	
		langueGroupe = new ButtonGroup<TextButton>();
		langueGroupe.add(englishBouton);
		langueGroupe.add(francaisBouton);
		langueGroupe.add(espanolBouton);
		langueGroupe.setMinCheckCount(1);
		langueGroupe.setMaxCheckCount(1);
		//Menu son
		onBouton = new TextButton(gam.langue.active, optionButtonStyle);
		offBouton = new TextButton(gam.langue.desactive, optionButtonStyle);	
		sonGroupe = new ButtonGroup<TextButton>();
		sonGroupe.add(onBouton);
		sonGroupe.add(offBouton);
		sonGroupe.setMinCheckCount(1);
		sonGroupe.setMaxCheckCount(1);	

		langueBouton.setVisible(false);
		sonBouton.setVisible(false);
		rateBouton.setVisible(false);
		moreAppsBouton.setVisible(false);
		onBouton.setVisible(false);
		offBouton.setVisible(false);
		francaisBouton.setVisible(false);
		englishBouton.setVisible(false);
		espanolBouton.setVisible(false);
		
		//Transition entre les écrans
		transitionImage = new Image(skin.getDrawable("Barre"));
		transitionImage.setWidth(Gdx.graphics.getWidth());
		transitionImage.setHeight(Gdx.graphics.getHeight());
		transitionImage.setColor(couleur.getCouleur2());
		transitionImage.setX(-Gdx.graphics.getWidth());
		transitionImage.setY(0);
		transitionImage.addAction(Actions.alpha(0));

		stage.addActor(langueBouton);
		stage.addActor(sonBouton);
		stage.addActor(moreAppsBouton);
		stage.addActor(rateBouton);
		
		stage.addActor(espanolBouton);
		stage.addActor(francaisBouton);
		stage.addActor(englishBouton);
		stage.addActor(offBouton);
		stage.addActor(onBouton);
		stage.addActor(transitionImage);
		stage.addActor(retourBouton);
		
		stage.draw();	
		
		langueActif = false;
		sonActif= false;

		boutonInactifX = -Gdx.graphics.getWidth();
		langueY = langueBouton.getY() - boutonEcart;
		sonY = sonBouton.getY() - boutonEcart;
		
		//Langue
		englishBouton.setWidth(1.5f * boutonHeight);
		englishBouton.setHeight(boutonHeight);
		englishBouton.setX(boutonInactifX);
		englishBouton.setY(langueY);
		francaisBouton.setWidth(1.5f * boutonHeight);
		francaisBouton.setHeight(boutonHeight);
		francaisBouton.setX(boutonInactifX);
		francaisBouton.setY(langueY);
		espanolBouton.setWidth(1.5f * boutonHeight);
		espanolBouton.setHeight(boutonHeight);
		espanolBouton.setX(boutonInactifX);
		espanolBouton.setY(langueY);
		if(Donnees.getLangue() == 1) englishBouton.setChecked(true);
		else if(Donnees.getLangue() == 2) francaisBouton.setChecked(true);
		else if(Donnees.getLangue() == 3) espanolBouton.setChecked(true);
		
		//Son
		onBouton.setWidth(1.6f*boutonHeight);
		onBouton.setHeight(boutonHeight);
		onBouton.setX(boutonInactifX);
		onBouton.setY(sonY);
		offBouton.setWidth(onBouton.getWidth());
		offBouton.setHeight(onBouton.getHeight());
		offBouton.setX(boutonInactifX);
		offBouton.setY(sonY);
		if(Donnees.getSon()) onBouton.setChecked(true);
		else offBouton.setChecked(true);		
		
		langueBouton.addAction(Actions.parallel(Actions.alpha(0), 
												Actions.addAction(Actions.alpha(0), sonBouton),
												Actions.addAction(Actions.alpha(0), rateBouton),
												Actions.addAction(Actions.alpha(0), moreAppsBouton),
												Actions.addAction(Actions.alpha(0), francaisBouton),
												Actions.addAction(Actions.alpha(0), englishBouton),
												Actions.addAction(Actions.alpha(0), espanolBouton),
												Actions.addAction(Actions.alpha(0), onBouton),
												Actions.addAction(Actions.alpha(0), offBouton),
												Actions.run(new Runnable() {
										            @Override
										            public void run() {
										            	langueBouton.setVisible(true);
											    		sonBouton.setVisible(true);
											    		rateBouton.setVisible(true);
											    		moreAppsBouton.setVisible(true);	
										            }})));
		
		langueBouton.addAction(Actions.sequence(Actions.delay(0.1f),
												Actions.alpha(1, 0.1f),  
												Actions.addAction(Actions.alpha(1, 0.1f), sonBouton),
												Actions.delay(0.1f),
												Actions.addAction(Actions.alpha(1, 0.1f), moreAppsBouton),
												Actions.delay(0.1f),
												Actions.addAction(Actions.alpha(1, 0.1f), rateBouton),
												Actions.run(new Runnable() {
										            @Override
										            public void run() {
										        		onBouton.setVisible(true);
										        		offBouton.setVisible(true);
										        		francaisBouton.setVisible(true);
										        		englishBouton.setVisible(true);
										        		espanolBouton.setVisible(true);	
										            }})));										
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(couleur.getCouleur2().r, couleur.getCouleur2().g, couleur.getCouleur2().b, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
		updateOptionTitleLayout();
		optionTitleFont.draw(game.batch, optionTitleLayout, Gdx.graphics.getWidth() / 2f - optionTitleLayout.width / 2f,
				OPTION_TITLE_Y_RATIO * Gdx.graphics.getHeight() - game.hauteurBanniere);
		game.batch.end();
		
		stage.act();
		stage.draw();	
		
		 //Utilisation du bouton BACK
        if (Gdx.input.isKeyJustPressed(Keys.BACK)){
        	game.setScreen(new MainMenuScreen(game));
        }		
	}
	
	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
		Gdx.input.setCatchKey(Keys.BACK, true);
		game.actionResolver.showBanner();
		if (webBuild) {
			UiActorUtils.centerTextButtons(stage.getRoot());
		}
		if (listenersBound) {
			return;
		}
		listenersBound = true;
		
		//Utilisation du menu
		langueBouton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				if(langueActif){
					langueActif = false;
					sonBouton.addAction(Actions.moveBy(0, boutonEcart, 0.7f, Interpolation.exp5Out));
					moreAppsBouton.addAction(Actions.moveBy(0, boutonEcart, 0.7f, Interpolation.exp5Out));
					rateBouton.addAction(Actions.moveBy(0, boutonEcart, 0.7f, Interpolation.exp5Out));
					francaisBouton.addAction(Actions.sequence(Actions.alpha(0, 0.1f), Actions.moveTo(boutonInactifX, langueY, 0, Interpolation.exp5Out)));
					englishBouton.addAction(Actions.sequence(Actions.alpha(0, 0.1f), Actions.moveTo(boutonInactifX, langueY, 0, Interpolation.exp5Out)));
					espanolBouton.addAction(Actions.sequence(Actions.alpha(0, 0.1f), Actions.moveTo(boutonInactifX, langueY, 0, Interpolation.exp5Out)));
				}
				else if(!langueActif && !sonActif){
					langueActif = true;
					sonBouton.addAction(Actions.moveBy(0, - boutonEcart, 0.7f, Interpolation.exp5Out));
					moreAppsBouton.addAction(Actions.moveBy(0, - boutonEcart, 0.7f, Interpolation.exp5Out));
					rateBouton.addAction(Actions.moveBy(0, - boutonEcart, 0.7f, Interpolation.exp5Out));
					francaisBouton.addAction(Actions.sequence(Actions.moveTo(Gdx.graphics.getWidth()/2 - francaisBouton.getWidth()/2, langueY, 0, Interpolation.exp5Out), Actions.alpha(1, 0.35f)));
					englishBouton.addAction(Actions.sequence(Actions.moveTo(Gdx.graphics.getWidth()/2 - 1.6f*francaisBouton.getWidth()/* - boutonEcart*/, langueY, 0, Interpolation.exp5Out), Actions.alpha(1, 0.35f)));
					espanolBouton.addAction(Actions.sequence(Actions.moveTo(Gdx.graphics.getWidth()/2 + 0.6f*francaisBouton.getWidth()/* + boutonEcart*/, langueY, 0, Interpolation.exp5Out), Actions.alpha(1, 0.35f)));
				}
				//Fermeture des menus non utilisés			
				else if(!langueActif && sonActif){
					sonActif = false;
					langueActif = true;
					sonBouton.addAction(Actions.moveBy(0, - boutonEcart, 0.7f, Interpolation.exp5Out));
					onBouton.addAction(Actions.sequence(Actions.alpha(0, 0.1f), Actions.moveTo(boutonInactifX, sonY, 0, Interpolation.exp5Out)));
					offBouton.addAction(Actions.sequence(Actions.alpha(0, 0.1f), Actions.moveTo(boutonInactifX, sonY, 0, Interpolation.exp5Out)));
					francaisBouton.addAction(Actions.sequence(Actions.moveTo(Gdx.graphics.getWidth()/2 - francaisBouton.getWidth()/2, langueY, 0, Interpolation.exp5Out), Actions.alpha(1, 0.35f)));
					englishBouton.addAction(Actions.sequence(Actions.moveTo(Gdx.graphics.getWidth()/2 - 1.6f*francaisBouton.getWidth(), langueY, 0, Interpolation.exp5Out), Actions.alpha(1, 0.35f)));
					espanolBouton.addAction(Actions.sequence(Actions.moveTo(Gdx.graphics.getWidth()/2 + 0.6f*francaisBouton.getWidth(), langueY, 0, Interpolation.exp5Out), Actions.alpha(1, 0.35f)));
				}
			}
		});
		
		sonBouton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				if(sonActif){
					sonActif = false;
					moreAppsBouton.addAction(Actions.moveBy(0, + boutonEcart, 0.7f, Interpolation.exp5Out));
					rateBouton.addAction(Actions.moveBy(0, + boutonEcart, 0.7f, Interpolation.exp5Out));
					onBouton.addAction(Actions.sequence(Actions.alpha(0, 0.1f), Actions.moveTo(boutonInactifX, sonY, 0, Interpolation.exp5Out)));
					offBouton.addAction(Actions.sequence(Actions.alpha(0, 0.1f), Actions.moveTo(boutonInactifX, sonY, 0, Interpolation.exp5Out)));
				}
				else if(!sonActif && !langueActif){
					sonActif = true;
					moreAppsBouton.addAction(Actions.moveBy(0, - boutonEcart, 0.7f, Interpolation.exp5Out));
					rateBouton.addAction(Actions.moveBy(0, - boutonEcart, 0.7f, Interpolation.exp5Out));
					onBouton.addAction(Actions.sequence(Actions.moveTo(sonBouton.getX() - boutonEcart/4, sonY, 0, Interpolation.exp5Out), Actions.alpha(1, 0.35f)));
					offBouton.addAction(Actions.sequence(Actions.moveTo(sonBouton.getX() + boutonEcart/4 + sonBouton.getWidth() - offBouton.getWidth(), sonY, 0, Interpolation.exp5Out), Actions.alpha(1, 0.35f)));
				}
				//Fermeture des menus non utilisés
				else if(!sonActif && langueActif){
					langueActif = false;
					sonActif = true;
					sonBouton.addAction(Actions.moveBy(0, boutonEcart, 0.7f, Interpolation.exp5Out));
					francaisBouton.addAction(Actions.sequence(Actions.alpha(0, 0.1f), Actions.moveTo(boutonInactifX, langueY, 0, Interpolation.exp5Out)));
					englishBouton.addAction(Actions.sequence(Actions.alpha(0, 0.1f), Actions.moveTo(boutonInactifX, langueY, 0, Interpolation.exp5Out)));
					espanolBouton.addAction(Actions.sequence(Actions.alpha(0, 0.1f), Actions.moveTo(boutonInactifX, langueY, 0, Interpolation.exp5Out)));
					onBouton.addAction(Actions.sequence(Actions.moveTo(sonBouton.getX() - boutonEcart/4, sonY, 0, Interpolation.exp5Out), Actions.alpha(1, 0.35f)));
					offBouton.addAction(Actions.sequence(Actions.moveTo(sonBouton.getX() + boutonEcart/4 + sonBouton.getWidth() - offBouton.getWidth(), sonY, 0, Interpolation.exp5Out), Actions.alpha(1, 0.35f)));
				}	
			}
		});
		
		if (!webBuild) {
			rateBouton.addListener(new ClickListener(){
				@Override
				public void clicked(InputEvent event, float x, float y){
					Donnees.setRate(true);
		        	Gdx.net.openURI(Variables.GOOGLE_PLAY_GAME_URL);
		        	//Gdx.net.openURI(Variables.AMAZON_GAME_URL);
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
		
		//Utilisation des options
		//Son
		onBouton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				Donnees.setSon(true);
			}
		});
		
		offBouton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				Donnees.setSon(false);
			}
		});
		
		//Langue
		englishBouton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				Donnees.setLangue(1);
				game.langue.setLangue(1);
				setLangage();
			}
		});
		francaisBouton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				Donnees.setLangue(2);
				game.langue.setLangue(2);
				setLangage();
			}
		});
		espanolBouton.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y){
				Donnees.setLangue(3);
				game.langue.setLangue(3);
				setLangage();
			}
		});	
	}

	public void setLangage(){
		langueBouton.setText(game.langue.langage.toUpperCase()); 
		rateBouton.setText(game.langue.noter.toUpperCase());
		moreAppsBouton.setText(game.langue.plusDApp.toUpperCase());
		sonBouton.setText(game.langue.sons.toUpperCase()); 
		onBouton.setText(game.langue.active);
		offBouton.setText(game.langue.desactive);
	}
	
	@Override
	public void resize(int width, int height) {
		camera.setToOrtho(false, width, height);
		stage.getViewport().update(width, height, true);
		updateOptionTitleLayout();
		if (webBuild) {
			UiActorUtils.centerTextButtons(stage.getRoot());
		}
		
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
		System.out.println("------------------------Option screen disposed");
		stage.dispose();
		skin.dispose();
	}

	private void updateOptionTitleLayout() {
		float widthScale = Gdx.graphics.getWidth() / baseScreenWidth;
		float heightScale = Gdx.graphics.getHeight() / baseScreenHeight;
		float baseScale = Math.min(widthScale, heightScale);
		optionTitleFont.getData().setScale(baseScale);
		String titleText = game.langue.options.toUpperCase();
		optionTitleLayout.setText(optionTitleFont, titleText);
		float targetWidth = Gdx.graphics.getWidth() * OPTION_TITLE_WIDTH_RATIO;
		if (optionTitleLayout.width > 0f) {
			float fitScale = targetWidth / optionTitleLayout.width;
			optionTitleFont.getData().setScale(baseScale * fitScale);
		}
		optionTitleLayout.setText(optionTitleFont, titleText);
	}

}
