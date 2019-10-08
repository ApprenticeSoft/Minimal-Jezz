package com.minimal.jezz.body;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.minimal.jezz.Variables;

public class Balle extends CircleShape{

	public Body body;
	private BodyDef bodyDef;
	private float rayon;
	public World world;
	Camera camera;
    private Vector2 vectorSpeed;
    private float maxSpeed, speed;
    private float initX, initY;
	
	public Balle(World world, Camera camera, float posX, float posY){
		super();
		this.world = world;
		this.camera = camera;
		rayon = camera.viewportWidth/50;
		
		maxSpeed = Variables.vitesseBalle * camera.viewportHeight;
		
		bodyDef = new BodyDef();
        this.setRadius(rayon);	
        
		bodyDef.position.set(new Vector2(posX, posY));
        bodyDef.type = BodyType.DynamicBody; 
		body = world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();  
        fixtureDef.shape = this;  
        fixtureDef.density = 1.0f;  
        fixtureDef.friction = 0.0f;  
        fixtureDef.restitution = 1;  
        body.createFixture(fixtureDef); 
        body.setUserData("Balle");
        
		initY = MathUtils.random(-200,200);
		initX = MathUtils.random(-200,200);
		body.applyLinearImpulse((float)(initY/100), (float)(initX/100), body.getPosition().x, body.getPosition().y, true);
	}
	
	public void active(){
		//Limitation de la vitesse de la balle	
		maxSpeed = Variables.vitesseBalle * camera.viewportHeight;	
		vectorSpeed = body.getLinearVelocity();		
		speed = vectorSpeed.len();
		if(speed > maxSpeed){
			body.setLinearVelocity(vectorSpeed.limit(maxSpeed));
		}
		else if(speed < 0.8f * maxSpeed){
			body.applyLinearImpulse(vectorSpeed.nor(), new Vector2(body.getPosition().x, body.getPosition().y), true);
		}

		//La balle ne sort pas de l'écran
		if(body.getPosition().x - rayon < 0)
			body.setTransform(rayon, body.getPosition().y, 0);
		if(body.getPosition().x + rayon > camera.viewportWidth)
			body.setTransform(camera.viewportWidth - rayon, body.getPosition().y, 0);
		if(body.getPosition().y + rayon > camera.viewportHeight)
			body.setTransform(body.getPosition().x, camera.viewportHeight - rayon, 0);	
	}
	
	public void drawOmbre(SpriteBatch batch, TextureRegion textureRegion){
		batch.setColor(0, 0, 0, 0.2f);
		batch.draw(textureRegion, 
				Variables.BOX_TO_WORLD*(this.body.getPosition().x - this.rayon) + Gdx.graphics.getWidth()/80, 
				Variables.BOX_TO_WORLD*(this.body.getPosition().y - this.rayon) - Gdx.graphics.getWidth()/68, 
				Variables.BOX_TO_WORLD * 2 * this.rayon, 
				Variables.BOX_TO_WORLD * 2 * this.rayon);
		
	}
	
	public void draw(SpriteBatch batch, TextureRegion textureRegion, Color couleur){
		//batch.setColor(243/256f, 237/256f, 211/256f, 1);
		batch.setColor(couleur);
		batch.draw(textureRegion, 
				Variables.BOX_TO_WORLD*(this.body.getPosition().x - this.rayon), 
				Variables.BOX_TO_WORLD*(this.body.getPosition().y - this.rayon), 
				Variables.BOX_TO_WORLD * 2 * this.rayon, 
				Variables.BOX_TO_WORLD * 2 * this.rayon);
		
	}
	
	public void setVitesse(float vitesseBalles){
		maxSpeed = vitesseBalles * camera.viewportHeight;;
	}
}
