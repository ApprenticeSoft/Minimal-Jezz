package com.minimal.jezz.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.utils.Array;
import com.minimal.jezz.Couleurs;
import com.minimal.jezz.LevelHandler;
import com.minimal.jezz.Donnees;
import com.minimal.jezz.MyGestureListener;
import com.minimal.jezz.Point;
import com.minimal.jezz.Surface;
import com.minimal.jezz.Variables;
import com.minimal.jezz.MyGdxGame;
import com.minimal.jezz.body.Balle;
import com.minimal.jezz.body.Barre;
import com.minimal.jezz.enums.EtatBarre;
import com.minimal.jezz.enums.EtatPoint;
import com.minimal.jezz.table.TablesJeu;

public class GameScreen extends InputAdapter implements Screen{

	final MyGdxGame game;
	private OrthographicCamera camera;
	
	private World world;
    private Array<Barre> barres;
    private Array<Balle> balles;
    private Array<Point> points;
    private Array<Surface> surfaces;
	private TextureAtlas textureAtlas;
	private Skin skin;
	private int vies;
	private float airTotale, pourcentageAir;
	private TablesJeu tablesJeu;
	
	private Stage stage;
	private LabelStyle labelStylePourcentage;
	private Label labelPourcentage;

	//private Box2DDebugRenderer debugRenderer; 
	private MyGestureListener gestureListener;
	private InputMultiplexer inputMultiplexer;
	private LevelHandler level;
	private Couleurs couleurs;
	private Color couleurBarre, couleurBalle, couleurSurface, couleurFond;
	private Sound sonContact, sonNiveauFini, sonPerdu;
	
	public GameScreen(final MyGdxGame gam){
		game = gam;
		
		camera = new OrthographicCamera();
        camera.viewportHeight = Gdx.graphics.getHeight() * Variables.WORLD_TO_BOX;  
        camera.viewportWidth = Gdx.graphics.getWidth() * Variables.WORLD_TO_BOX;  
        camera.position.set(camera.viewportWidth/2, camera.viewportHeight/2, 0f);  
        camera.update(); 
        Variables.largeurBordure = camera.viewportHeight/100;
        
        //Couleurs
        couleurs = new Couleurs(Variables.couleurSelectionee);
        couleurBarre = new Color();
        couleurBalle = new Color();
        couleurSurface = new Color();
        couleurFond = new Color();
        couleurBalle = couleurs.getCouleur1();
        couleurFond = couleurs.getCouleur2();
        couleurBarre = couleurs.getCouleur3();
        couleurSurface = couleurs.getCouleur3();
        
        //Sons
		sonContact = game.assets.get("Sons/contact.mp3", Sound.class);
		sonNiveauFini = game.assets.get("Sons/gagne.mp3", Sound.class);
		sonPerdu = game.assets.get("Sons/perdu.mp3", Sound.class);

		textureAtlas = game.assets.get("Images.pack", TextureAtlas.class);
		skin = new Skin();
		skin.addRegions(textureAtlas);
		world = new World(new Vector2(0, 0), true);
		World.setVelocityThreshold(0);
        //debugRenderer = new Box2DDebugRenderer();
		Variables.BOX_STEP = 0;
		Variables.pause = false;
		Variables.perdu = false;
		Variables.gagné = false;
		Variables.spawn = false;
		Variables.début = true;
        vies = 3;
        
        barres = new Array<Barre>();
        balles = new Array<Balle>();
        points = new Array<Point>();
        surfaces = new Array<Surface>();
        gestureListener = new MyGestureListener(game, world, camera, barres, points, surfaces);
      	inputMultiplexer = new InputMultiplexer();
        
      	pourcentageAir = calculAirOccupée(barres, surfaces);
      	
        Barre barreHaut = new Barre(game, world, camera, camera.viewportWidth/2, (camera.viewportHeight - Variables.posBordure*camera.viewportWidth), (0.5f - Variables.posBordure)*camera.viewportWidth - Variables.largeurBordure, Variables.largeurBordure);
        Barre barreBas = new Barre(game, world, camera, camera.viewportWidth/2, Variables.posBordure*camera.viewportWidth, (0.5f - Variables.posBordure)*camera.viewportWidth - Variables.largeurBordure, Variables.largeurBordure);
        Barre barreGauche = new Barre(game, world, camera, Variables.posBordure*camera.viewportWidth, camera.viewportHeight/2, Variables.largeurBordure, camera.viewportHeight/2);
        Barre barreDroite = new Barre(game, world, camera, (1 - Variables.posBordure)*camera.viewportWidth, camera.viewportHeight/2, Variables.largeurBordure, camera.viewportHeight/2);
        
        barres.add(barreHaut);
        barres.add(barreBas);
        barres.add(barreGauche);
        barres.add(barreDroite);
       
        points.add(new Point(barreGauche.barre1.getPosition().x + Variables.largeurBordure, barreHaut.barre1.getPosition().y - Variables.largeurBordure, EtatPoint.HG));
        points.add(new Point(barreDroite.barre1.getPosition().x - Variables.largeurBordure, barreHaut.barre1.getPosition().y - Variables.largeurBordure, EtatPoint.HD));
        points.add(new Point(barreGauche.barre1.getPosition().x + Variables.largeurBordure, barreBas.barre1.getPosition().y + Variables.largeurBordure, EtatPoint.BG));
        points.add(new Point(barreDroite.barre1.getPosition().x - Variables.largeurBordure, barreBas.barre1.getPosition().y + Variables.largeurBordure, EtatPoint.BD));
        airTotale = (barreDroite.barre1.getPosition().x - Variables.largeurBordure - barreGauche.barre1.getPosition().x + Variables.largeurBordure)
        		 * (barreHaut.barre1.getPosition().y - Variables.largeurBordure - barreBas.barre1.getPosition().y + Variables.largeurBordure);
        System.out.println("Air surface = " + airTotale);
        
        level = new LevelHandler(world, camera, balles);
        level.loadLevel(Variables.niveauSelectione);
        
        stage = new Stage();

		labelStylePourcentage = new LabelStyle(game.assets.get("fontPourcentage.ttf", BitmapFont.class), new Color(0,0,0,0.5f));
		labelPourcentage = new Label((int)pourcentageAir + "%", labelStylePourcentage);
		labelPourcentage.setX(Gdx.graphics.getWidth()/2 - labelPourcentage.getWidth()/2);
		labelPourcentage.setY(Gdx.graphics.getHeight()/2 - labelPourcentage.getHeight()/2);
		labelPourcentage.addAction(Actions.alpha(0));
		
		stage.addActor(labelPourcentage);
		tablesJeu = new TablesJeu(gam, stage, skin, level, couleurFond, couleurBarre);	
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(couleurFond.r,couleurFond.g,couleurFond.b, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		if(pourcentageAir != 100*calculAirOccupée(barres, surfaces)/airTotale && !Variables.spawn){
			pourcentageAir = 100*calculAirOccupée(barres, surfaces)/airTotale;
			labelPourcentage.setText(/*String.format("%.0f", pourcentageAir)*/ (int)pourcentageAir + "%");
			labelPourcentage.addAction(Actions.sequence(Actions.moveTo(Gdx.graphics.getWidth()/2 - labelPourcentage.getPrefWidth()/2, labelPourcentage.getY()), 
														Actions.alpha(1),
														Actions.delay(0.5f),
														Actions.alpha(0, 0.6f, Interpolation.sineIn)));
		}
		
		//debugRenderer.render(world, camera.combined);
		world.step(Variables.BOX_STEP, Variables.BOX_VELOCITY_ITERATIONS, Variables.BOX_POSITION_ITERATIONS);
		
		for(int i = 0; i < balles.size; i++){
			balles.get(i).active();
		}
		
		if(!Variables.pause && !Variables.gagné && vies > 0)
			for(Barre barre : barres){
				barre.active(barres, points);
			}
		
		Barre.detectSurface(points, balles, surfaces);
		
		//Niveau gagné
		if(100*calculAirOccupée(barres, surfaces)/airTotale > Variables.objectif && !Variables.spawn){
			if(!Variables.gagné){				
				Variables.INTERSTITIAL_TRIGGER--;
				Variables.gagné = true;
				if(Donnees.getSon()){
					Barre.sonSurface.stop();
					sonNiveauFini.play();
				}
			}
			if(Variables.niveauSelectione == 25){
				tablesJeu.jeuFini();
			}
			else{
				tablesJeu.gagne();
				if(Variables.niveauSelectione == Donnees.getNiveau())
					Donnees.setNiveau(Donnees.getNiveau() + 1);
			}
		}
		//Niveau perdu		
		if(vies <= 0){
			if(!Variables.perdu){
				Variables.INTERSTITIAL_TRIGGER--;
				Variables.perdu = true;
				if(Donnees.getSon()){
					sonContact.stop();
					sonPerdu.play();
				}
			}
			tablesJeu.perdu();
		}
		//Mettre le jeu en pause
        if (Gdx.input.isKeyJustPressed(Keys.BACK) && vies > 0 /*&& !Variables.début && !Variables.gagné*/){
        	if(Variables.début || Variables.gagné){
        		game.setScreen(new MainMenuScreen(game));
        	}
        	else if(Variables.pause){
				tablesJeu.pauseFinie();
			}
       	  	else{
       	  		tablesJeu.pause();
       	  	}
		}
		
		/***************************AFFICHAGE DES GRAPHISMES***************************/
		game.batch.begin();
		//Affichage des ombres
		for(int i = 0; i < balles.size; i++)
			balles.get(i).drawOmbre(game.batch, textureAtlas.findRegion("Balle"));
		
		for(int i = 0; i < barres.size; i++)
			barres.get(i).drawOmbre(game.batch, textureAtlas.findRegion("Barre"));
		
		for(int i = 0; i < barres.size; i++)
			barres.get(i).draw(game.batch, textureAtlas.findRegion("Barre"), couleurBarre);
		//Affichage des surfaces
		game.batch.setColor(couleurSurface);
		if(surfaces.size > 0){
			for(int i = 0; i < surfaces.size; i++){
				game.batch.draw(textureAtlas.findRegion("Barre"), 
						Variables.BOX_TO_WORLD * (surfaces.get(i).getX()), 
						Variables.BOX_TO_WORLD * (surfaces.get(i).getY()), 
						Variables.BOX_TO_WORLD * surfaces.get(i).getWidth(), 
						Variables.BOX_TO_WORLD * surfaces.get(i).getHeight());
			}
		}
		//Affichage des bordures
		game.batch.draw(textureAtlas.findRegion("Barre"), 0, 0, Variables.posBordure*Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		game.batch.draw(textureAtlas.findRegion("Barre"), (1 - Variables.posBordure)*Gdx.graphics.getWidth(), 0, Variables.posBordure*Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		game.batch.draw(textureAtlas.findRegion("Barre"), 0, 0, Gdx.graphics.getWidth(), Variables.posBordure*Gdx.graphics.getWidth());
		game.batch.draw(textureAtlas.findRegion("Barre"), 0, (Gdx.graphics.getHeight() - Variables.posBordure*Gdx.graphics.getWidth()), Gdx.graphics.getWidth(), Variables.posBordure*Gdx.graphics.getWidth());
		game.batch.end();
		/******************************************************************************/
		
		stage.act();
		stage.draw();
		
		game.batch.begin();
		//Affichage des balles
		for(int i = 0; i < balles.size; i++)
			balles.get(i).draw(game.batch, textureAtlas.findRegion("Balle"), couleurBalle);
		game.batch.end();
	}
	
	@Override
	public void show() {
		//Important pour pouvoir utiliser la touche BACK
		Gdx.input.setCatchBackKey(true);
		//Pour pouvoir agir à la fois sur un stage et sur un gestureListener
		inputMultiplexer.addProcessor(new GestureDetector(gestureListener));
		inputMultiplexer.addProcessor(stage);
		Gdx.input.setInputProcessor(inputMultiplexer);
		
		world.setContactListener(new ContactListener(){
			@Override
			public void beginContact(Contact contact) {
				Body a = contact.getFixtureA().getBody();
			    Body b = contact.getFixtureB().getBody();
			    if(a.getUserData() == "Bordure" && b.getUserData() == "Barre"){
			    	for(Barre barre : barres){
			    		if(barre.vertical){																	//Gestion des barres verticales
					    	if(a.getPosition().y > b.getPosition().y){
				    			if(b == barre.barre1){
				    				if(barre.getEtat() != EtatBarre.contactHaut){
				    					barre.setContactInit(a.getPosition().y - Variables.largeurBordure);
				    					barre.ajoutPoint(new Point(barre.barre1.getPosition().x - Variables.largeurBordure, a.getPosition().y - Variables.largeurBordure, EtatPoint.HD));
				    					barre.ajoutPoint(new Point(barre.barre1.getPosition().x + Variables.largeurBordure, a.getPosition().y - Variables.largeurBordure, EtatPoint.HG));
				    				}
				    			}
					    	}
					    	else if(a.getPosition().y < b.getPosition().y){
				    			if(b == barre.barre1){
				    				if(barre.getEtat() != EtatBarre.contactBas){								//Pour éviter que "contactInit" du haut ne soi remplacé par le "contactinit" du bas dans le cas d'un premier contact en bas
				    					barre.setContactInit(a.getPosition().y + Variables.largeurBordure);
				    					barre.ajoutPoint(new Point(barre.barre1.getPosition().x - Variables.largeurBordure, a.getPosition().y + Variables.largeurBordure, EtatPoint.BD));
				    					barre.ajoutPoint(new Point(barre.barre1.getPosition().x + Variables.largeurBordure, a.getPosition().y + Variables.largeurBordure, EtatPoint.BG));
				    				}
				    			}
					    	}
			    		}
			    		else if(!barre.vertical){															//Gestion des barres horizontales
					    	if(a.getPosition().x > b.getPosition().x){
				    			if(b == barre.barre1){
				    				if(barre.getEtat() != EtatBarre.contactDroite){
				    					barre.setContactInit(a.getPosition().x - Variables.largeurBordure);
				    					barre.ajoutPoint(new Point(a.getPosition().x - Variables.largeurBordure, barre.barre1.getPosition().y + Variables.largeurBordure, EtatPoint.BD));
				    					barre.ajoutPoint(new Point(a.getPosition().x - Variables.largeurBordure, barre.barre1.getPosition().y - Variables.largeurBordure, EtatPoint.HD));
				    				}
				    			}
					    	}
					    	else if(a.getPosition().x < b.getPosition().x){
				    			if(b == barre.barre1){
				    				if(barre.getEtat() != EtatBarre.contactGauche){
				    					barre.setContactInit(a.getPosition().x + Variables.largeurBordure);
				    					barre.ajoutPoint(new Point(a.getPosition().x + Variables.largeurBordure, barre.barre1.getPosition().y + Variables.largeurBordure, EtatPoint.BG));
				    					barre.ajoutPoint(new Point(a.getPosition().x + Variables.largeurBordure, barre.barre1.getPosition().y - Variables.largeurBordure, EtatPoint.HG));
				    				}
				    			}
					    	}
			    		}
			    	}
			    } 
			    else if((a.getUserData() == "Barre" && b.getUserData() == "Balle") || (a.getUserData() == "Balle" && b.getUserData() == "Barre")){
			    	for(Barre barre : barres){
			    		if(a == barre.barre1 || b == barre.barre1){
			    			barre.setEtat(EtatBarre.decroissante);
			    			vies--;
							if(Donnees.getSon())
								sonContact.play();
			    		}
			    	}
			    }
			}

			@Override
			public void endContact(Contact contact) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
				Body a = contact.getFixtureA().getBody();
			    Body b = contact.getFixtureB().getBody();
			    if(a.getUserData() == "Barre" && b.getUserData() == "Barre"){
			    	contact.setEnabled(false);
			    }
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
				
			}
			
		});
		
		tablesJeu.boutonListener(world, barres);
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
		System.out.println("------------------------Game screen disposed");
		skin.dispose();
		stage.dispose();
		world.dispose();
	}
	
	public float calculAirOccupée(Array<Barre> barres, Array<Surface> surfaces){
		float airOccupée = 0;
		
		for(int i = 0; i < surfaces.size; i++){
			airOccupée += surfaces.get(i).getWidth()*surfaces.get(i).getHeight();
		}
		
		for(int i = 4; i < barres.size; i++){
			airOccupée += 4*barres.get(i).getWidth()*barres.get(i).getHeight();
		}
		
		return airOccupée;
	}

}
