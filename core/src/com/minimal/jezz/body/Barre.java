package com.minimal.jezz.body;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.utils.Array;
import com.minimal.jezz.Donnees;
import com.minimal.jezz.MyGdxGame;
import com.minimal.jezz.Point;
import com.minimal.jezz.Surface;
import com.minimal.jezz.Variables;
import com.minimal.jezz.enums.EtatBarre;
import com.minimal.jezz.enums.EtatPoint;

public class Barre extends PolygonShape{

	final MyGdxGame game;
	public boolean visible, vertical;
	public Body barre1;
	private PolygonShape polygonShape;
	private BodyDef bodyDef;
	private FixtureDef fixtureDef;
	private float width;
	private float height;
	private Fixture fixtureBarre;
	private World world;
	//private Camera camera;
	private float X, Y, longueur, contactInit, contactMur1, contactMur2, speed;
	private EtatBarre etatBarre;
	private Array<Point> pointsBarre;
	public static Sound sonSurface;
	
	public Barre(final MyGdxGame game, World world, Camera camera, float X, float Y, boolean vertical, Array<Point> points){
		super();
		this.game = game;
		this.world = world;
		//this.camera = camera;
		this.vertical = vertical;
		this.X = X;
		this.Y = Y;

		sonSurface = game.assets.get("Sons/surface_complete.mp3", Sound.class);
		
		width = Variables.largeurBordure;
		height = width;
		longueur = width;
		etatBarre = EtatBarre.croissante;
		contactMur1 = 0;
		contactMur2 = 0;
		speed = 8.2f;
    	this.setAsBox(width , height);
    	visible = true;
    	pointsBarre = new Array<Point>();
        
    	bodyDef = new BodyDef();
    	bodyDef.type = BodyType.DynamicBody;
    	
		fixtureDef = new FixtureDef();
		fixtureDef.shape = this;
        fixtureDef.density = 1.0f;  
        fixtureDef.friction = 0.0f;  
        fixtureDef.restitution = 1f; 
        
        creationBarre(points);
	}
	
	public Barre(final MyGdxGame game, World world, Camera camera, float X, float Y, float width, float height){
		super();
		this.game = game;
		this.world = world;
		//this.camera = camera;
		this.X = X;
		this.Y = Y;
		this.width = width;
		this.height = height;

		sonSurface = game.assets.get("Sons/surface_complete.mp3", Sound.class);

		visible = true;
    	this.setAsBox(width , height);
        
    	bodyDef = new BodyDef();
    	bodyDef.type = BodyType.StaticBody;
    	
        bodyDef.position.set(new Vector2(X,Y));
        barre1 = world.createBody(bodyDef);
        barre1.setUserData("Bordure");
        fixtureBarre = barre1.createFixture(this, 0.0f);
        barre1.setFixedRotation(true);
	}
	
	public void creationBarre(Array<Point> points){
		if(vertical){
			if(etatBarre == EtatBarre.croissante){
	    		longueur += speed*Gdx.graphics.getDeltaTime();
	    		height = longueur;
				polygonShape = new PolygonShape();
		        polygonShape.setAsBox(width, height);
		        bodyDef.position.set(new Vector2(X/* - width*/, Y/* - width*/));
		        barre1 = world.createBody(bodyDef);
		        barre1.setUserData("Barre");
		        fixtureBarre = barre1.createFixture(polygonShape, 0.0f);
		        barre1.setFixedRotation(true);
		        if(barre1.getPosition().y  + longueur > contactInit && barre1.getPosition().y - longueur < contactInit){
		        	if(barre1.getPosition().y + longueur - contactInit < contactInit - (barre1.getPosition().y - longueur)){
		        		etatBarre = EtatBarre.contactHaut;
		    			contactMur1 = contactInit;     		
		        	}
		        	else if(barre1.getPosition().y + longueur - contactInit > contactInit - (barre1.getPosition().y - longueur)){
		        		etatBarre = EtatBarre.contactBas;
		    			contactMur1 = contactInit;     		
		        	}
		        	else{
		        		System.out.println("FUCKED");
		        	}
		        }
			}
			else if(etatBarre == EtatBarre.contactHaut){
	    		longueur += 0.5f*speed*Gdx.graphics.getDeltaTime();
	    		height = longueur;
				polygonShape = new PolygonShape();
		        polygonShape.setAsBox(width, height);
		        bodyDef.position.set(new Vector2(X/* - width*/, contactMur1 - longueur));
		        barre1 = world.createBody(bodyDef);
		        barre1.setUserData("Barre");
		        fixtureBarre = barre1.createFixture(polygonShape, 0.0f);
		        barre1.setFixedRotation(true);
		        if(barre1.getPosition().y - longueur < contactInit && contactInit != contactMur1){
		        	etatBarre = EtatBarre.contactDouble;
		    		contactMur2 = contactInit;
		        }	
		        else if(contactInit != contactMur1){
		        	System.out.println("Contact bas raté !!!!");
		        }
			}
			else if(etatBarre == EtatBarre.contactBas){
	    		longueur += 0.5f*speed*Gdx.graphics.getDeltaTime();
	    		height = longueur;
				polygonShape = new PolygonShape();
		        polygonShape.setAsBox(width, height);
		        bodyDef.position.set(new Vector2(X/* - width*/, contactMur1 + longueur));
		        barre1 = world.createBody(bodyDef);
		        barre1.setUserData("Barre");
		        fixtureBarre = barre1.createFixture(polygonShape, 0.0f);
		        barre1.setFixedRotation(true);
		        if(barre1.getPosition().y + longueur > contactInit && contactInit != contactMur1){
		        	etatBarre = EtatBarre.contactDouble;
		    		contactMur2 = contactInit;
		        }	
		        else if(contactInit != contactMur1){
		        	System.out.println("Contact haut raté !!!!");
		        }
			}
			else if(etatBarre == EtatBarre.contactDouble){
				polygonShape = new PolygonShape();
	    		height = Math.abs(contactMur1 - contactMur2)/2;
		        polygonShape.setAsBox(width, height);
		        bodyDef.position.set(new Vector2(X/* - width*/, Math.min(contactMur1, contactMur2) + Math.abs(contactMur1 - contactMur2)/2));
		    	bodyDef.type = BodyType.StaticBody;
		        barre1 = world.createBody(bodyDef);
		        barre1.setUserData("Bordure");
		        fixtureBarre = barre1.createFixture(polygonShape, 0.0f);
		        barre1.setFixedRotation(true);	
		        Variables.spawn = false;
    			ajoutPoint(points);
    			if(Donnees.getSon())
    				sonSurface.play();
    			etatBarre = EtatBarre.finie;		
			}
			else if(etatBarre == EtatBarre.finie){
			}
			else if(etatBarre == EtatBarre.decroissante){
	    		longueur -= 2*speed*Gdx.graphics.getDeltaTime();
	    		height = longueur;
				polygonShape = new PolygonShape();
		        polygonShape.setAsBox(width, height);
		        bodyDef.position.set(new Vector2(getX(), getY()));
		        barre1 = world.createBody(bodyDef);
		        fixtureBarre = barre1.createFixture(polygonShape, 0.0f);
		        barre1.setFixedRotation(true);
		        barre1.setUserData("Morte");
				if(longueur <= 0){	
			        Variables.spawn = false;
					longueur = 0;
					visible = false;
				}
			}
			else if(etatBarre == EtatBarre.detruite){
				System.out.println("etatBarre = " + etatBarre);
				visible = false;
			} 
		}
		else if(!vertical){
			if(etatBarre == EtatBarre.croissante){
	    		longueur += speed*Gdx.graphics.getDeltaTime();
	    		width = longueur;
				polygonShape = new PolygonShape();
		        polygonShape.setAsBox(width, height);
		        bodyDef.position.set(new Vector2(X/* - height*/, Y/* - height*/));
		        barre1 = world.createBody(bodyDef);
		        barre1.setUserData("Barre");
		        fixtureBarre = barre1.createFixture(polygonShape, 0.0f);
		        barre1.setFixedRotation(true);
		        if(barre1.getPosition().x  + longueur > contactInit && barre1.getPosition().x - longueur < contactInit){
		        	if(barre1.getPosition().x + longueur - contactInit < contactInit - (barre1.getPosition().x - longueur)){
		        		etatBarre = EtatBarre.contactDroite;
		    			contactMur1 = contactInit;     		
		        	}
		        	else if(barre1.getPosition().x + longueur - contactInit > contactInit - (barre1.getPosition().x - longueur)){
		        		etatBarre = EtatBarre.contactGauche;
		    			contactMur1 = contactInit;     		
		        	}
		        	else{
		        		System.out.println("FUCKED");
		        	}
		        }
			}
			else if(etatBarre == EtatBarre.contactDroite){
	    		longueur += 0.5f*speed*Gdx.graphics.getDeltaTime();
	    		width = longueur;
				polygonShape = new PolygonShape();
		        polygonShape.setAsBox(width, height);
		        bodyDef.position.set(new Vector2(contactMur1 - longueur, Y/* - height*/));
		        barre1 = world.createBody(bodyDef);
		        barre1.setUserData("Barre");
		        fixtureBarre = barre1.createFixture(polygonShape, 0.0f);
		        barre1.setFixedRotation(true);
		        if(barre1.getPosition().x - longueur < contactInit && contactInit != contactMur1){
		        	etatBarre = EtatBarre.contactDouble;
		    		contactMur2 = contactInit;
		        }	
		        else if(contactInit != contactMur1){
		        	System.out.println("Contact bas raté !!!!");
		        }
			}
			else if(etatBarre == EtatBarre.contactGauche){
	    		longueur += 0.5f*speed*Gdx.graphics.getDeltaTime();
	    		width = longueur;
				polygonShape = new PolygonShape();
		        polygonShape.setAsBox(width, height);
		        bodyDef.position.set(new Vector2(contactMur1 + longueur, Y/* - height*/));
		        barre1 = world.createBody(bodyDef);
		        barre1.setUserData("Barre");
		        fixtureBarre = barre1.createFixture(polygonShape, 0.0f);
		        barre1.setFixedRotation(true);
		        if(barre1.getPosition().x + longueur > contactInit && contactInit != contactMur1){
		        	etatBarre = EtatBarre.contactDouble;
		    		contactMur2 = contactInit;
		        }	
		        else if(contactInit != contactMur1){
		        	System.out.println("Contact haut raté !!!!");
		        }
			}
			else if(etatBarre == EtatBarre.contactDouble){
				polygonShape = new PolygonShape();
	    		width = Math.abs(contactMur1 - contactMur2)/2;
		        polygonShape.setAsBox(width, height);
		        bodyDef.position.set(new Vector2(Math.min(contactMur1, contactMur2) + Math.abs(contactMur1 - contactMur2)/2, Y/* - height*/));
		    	bodyDef.type = BodyType.StaticBody;
		        barre1 = world.createBody(bodyDef);
		        barre1.setUserData("Bordure");
		        fixtureBarre = barre1.createFixture(polygonShape, 0.0f);
		        barre1.setFixedRotation(true);	
		        Variables.spawn = false;
    			ajoutPoint(points);
    			if(Donnees.getSon())
    				sonSurface.play();
    			etatBarre = EtatBarre.finie;
			}
			else if(etatBarre == EtatBarre.finie){
			}
			else if(etatBarre == EtatBarre.decroissante){
	    		longueur -= 2*speed*Gdx.graphics.getDeltaTime();
	    		width = longueur;
				polygonShape = new PolygonShape();
		        polygonShape.setAsBox(width, height);
		        bodyDef.position.set(new Vector2(getX(), getY()));
		        barre1 = world.createBody(bodyDef);
		        fixtureBarre = barre1.createFixture(polygonShape, 0.0f);
		        barre1.setFixedRotation(true);
				//this.barre1.setActive(false);
		        barre1.setUserData("Morte");	
		        //Variables.spawn = false;
				if(longueur <= 0){	
			        Variables.spawn = false;
					longueur = 0;
					//etatBarre = EtatBarre.detruite;
					visible = false;
				}
			}
			else if(etatBarre == EtatBarre.detruite){
				System.out.println("etatBarre = " + etatBarre);
				visible = false;
			} 
		}
		else{
			
		}
	}
	
	public void active(Array<Barre> array, Array<Point> points){
		if("Barre".equals(barre1.getUserData()) || "Morte".equals(barre1.getUserData())){
    		barre1.setActive(false);
    		world.destroyBody(barre1);
    		creationBarre(points);
    		
    		for(int i = 0; i < array.size; i++){
            	if(!array.get(i).visible){
            		array.get(i).barre1.setActive(false);
            		world.destroyBody(array.get(i).barre1);
            		array.removeIndex(i);
            	}
            }
		}
		else{
		
		}
	}
	
	public static void detectSurface(Array<Point> points, Array<Balle> balles, Array<Surface> surfaces){	
		outerloop : //Définir la "outerloop"
			for(int i = 0; i < points.size; i++){
				for(int j = 0; j < points.size; j++){
					if(points.get(i) != points.get(j) && points.get(i).getX() == points.get(j).getX() && points.get(i).getY() < points.get(j).getY()){				
						for(int k = 0; k < points.size; k++){
							if(points.get(j) != points.get(k) && points.get(j).getY() == points.get(k).getY() && points.get(j).getX() < points.get(k).getX()){					
								for(int l = 0; l < points.size; l++){
									if(points.get(k) != points.get(l) && points.get(l).getY() == points.get(i).getY() && points.get(l).getX() == points.get(k).getX()){					
										if(		(points.get(i).getEtat() == EtatPoint.BG) &&							//Sélection des points qui forment un rectangle dans le sans horaire en partant du coin bas gauche
												(points.get(i).getEtat() != points.get(j).getEtat()) &&
												(points.get(i).getEtat() != points.get(k).getEtat()) &&
												(points.get(i).getEtat() != points.get(l).getEtat()) &&
												(points.get(j).getEtat() != points.get(k).getEtat()) &&
												(points.get(j).getEtat() != points.get(l).getEtat()) &&
												(points.get(k).getEtat() != points.get(l).getEtat()))
											{
											
											Surface surface = new Surface(points.get(i).getX(),							//Création d'une surface à partir des points sélectionnés
													points.get(i).getY(),
													points.get(k).getX() - points.get(i).getX(),
													points.get(k).getY() - points.get(i).getY());
											
											if(surface.detecteBalle(balles)){											//Vérification si la surface contient au moins une balle
												//System.out.println("il y a une balle !");
											}
											else {
													surfaces.add(surface);												//Si pas de balle dans la surface, la surface est enregistrée dans 
																														//la liste des surface en jeu
													Array<Point> pointsSurface = new Array<Point>();
													pointsSurface.add(points.get(i));
													pointsSurface.add(points.get(j));
													pointsSurface.add(points.get(k));
													pointsSurface.add(points.get(l));
													
													points.removeAll(pointsSurface, true);								//les points formant cette surface sont éffacés de la liste des points en jeu

													break outerloop;
											}
										}	
									}
								}
							}
						}
					}
				}
			}	
	}
	
	public void setEtat(EtatBarre etat){
		etatBarre = etat;
	}
	
	public EtatBarre getEtat(){
		return etatBarre;
	}
	
	public void setContactMur1(float f){
		contactMur1 = f;
	}
	
	public float getContactMur1(){
		return contactMur1;
	}
	
	public void setContactMur2(float f){
		contactMur2 = f;
	}
	
	public float getContactMur2(){
		return contactMur1;
	}
	
	public void setContactInit(float f){
		contactInit = f;
	}
	
	public float getContactInit(){
		return contactMur1;
	}
	
	public float getLongueur(){
		return longueur;
	}
	
	public boolean getVertical(){
		return vertical;
	}
	
	public void ajoutPoint(Point point){
		pointsBarre.add(point);
	}
	
	public void ajoutPoint(Array<Point> points){
		if(pointsBarre.size > 0){
			for(int i = 0; i < pointsBarre.size; i++){					//On enlève les points formés par la barre en double
				for(int j = 0; j < pointsBarre.size; j++){
					try{
							if(i != j
							&& pointsBarre.get(i).getX() == pointsBarre.get(j).getX()
							&& pointsBarre.get(i).getY() == pointsBarre.get(j).getY()){
							pointsBarre.removeIndex(j);				
						}
					}catch(Exception e){
						System.out.println("Exception interceptée !!");
					}
				}
			}
		
			points.addAll(pointsBarre);									//On ajoute les points qui restent à la liste totale des points
		}
	}
	
	public void draw(SpriteBatch batch, TextureRegion textureRegion, Color couleur){
		batch.setColor(couleur);	
		batch.draw(textureRegion, 
						Variables.BOX_TO_WORLD * (this.barre1.getPosition().x - this.getWidth()), 
						Variables.BOX_TO_WORLD * (this.barre1.getPosition().y - this.getHeight()), 
						Variables.BOX_TO_WORLD * 2 * this.getWidth(), 
						Variables.BOX_TO_WORLD * 2 * this.getHeight());
						
	}
	
	public void drawOmbre(SpriteBatch batch, TextureRegion textureRegion){
		batch.setColor(0,0,0,0.2f);
		batch.draw(textureRegion, 
				Variables.BOX_TO_WORLD * (this.barre1.getPosition().x - this.getWidth()) + Gdx.graphics.getWidth()/80, 
				Variables.BOX_TO_WORLD * (this.barre1.getPosition().y - this.getHeight()) - Gdx.graphics.getWidth()/68,
				Variables.BOX_TO_WORLD * 2 * this.getWidth(),
				Variables.BOX_TO_WORLD * 2 * this.getHeight());
	}
	
	public float getX(){
		return barre1.getPosition().x;
	}
	
	public float getY(){
		return barre1.getPosition().y;
	}
	
	public float getWidth(){
		return width;
	}
	
	public float getHeight(){
		return height;
	}
	
}
