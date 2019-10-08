package com.minimal.jezz;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.minimal.jezz.body.Barre;

public class MyGestureListener implements GestureListener{

	final MyGdxGame game;
	float X, Y;
	World world;
	Camera camera;
	Vector2 direction;
	boolean vertical;
	Array<Barre> barres;
	Array<Point> points;
	Array<Surface> surfaces;
	int nbContact;
	
	public MyGestureListener(final MyGdxGame game, World world, Camera camera, Array<Barre> barres, Array<Point> points, Array<Surface> surfaces){
		this.game = game;
		this.world = world;
		this.camera = camera;
		this.barres = barres;
		this.points = points;
		this.surfaces = surfaces;
		
		nbContact = 0;
	}
	
	@Override
	public boolean touchDown(float x, float y, int pointer, int button) {
		setX(x);
		setY(y);
		return false;
	}

	@Override
	public boolean tap(float x, float y, int count, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean longPress(float x, float y) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fling(float velocityX, float velocityY, int button) {	
		
		return false;
	}

	@Override
	public boolean pan(float x, float y, float deltaX, float deltaY) {
		if(/*!Variables.pause && ! Variables.perdu*/ Variables.BOX_STEP != 0/* && !Variables.gagné*/){
			if(Math.abs(deltaX) > Math.abs(deltaY)){
				vertical = false;
				direction = new Vector2(-2f,0);
			}
			else if(Math.abs(deltaX) < Math.abs(deltaY)){
				vertical = true;
				direction = new Vector2(0,2f);
			}
			else{
			}
			
			if(!detectePointeur(getX(), getY(), surfaces, barres)){	
				if(!Variables.spawn){
					Variables.spawn = true;
					spawn();
				}
			}
			
			nbContact = 0;
		}
		return false;
	}

	@Override
	public boolean panStop(float x, float y, int pointer, int button) {
		return false;
	}

	@Override
	public boolean zoom(float initialDistance, float distance) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2,
			Vector2 pointer1, Vector2 pointer2) {
		// TODO Auto-generated method stub
		return false;
	}

	public void setX(float x){
		X = x;
	}
	
	public float getX(){
		return X;
	}

	public void setY(float y){
		Y = y;
	}
	
	public float getY(){
		return Y;
	}
	
	public void spawn(){
		Barre barre = new Barre(game, world, camera, getX()*Variables.WORLD_TO_BOX, camera.viewportHeight - getY()*Variables.WORLD_TO_BOX, vertical, points);
		barres.add(barre);
	}
	
	public boolean detectePointeur(float x, float y, Array<Surface> surfaces, Array<Barre> barres){
		//Si le pointeur est en dehors l'air de jeu
		if(x*Variables.WORLD_TO_BOX < Variables.posBordure*camera.viewportWidth + 2*Variables.largeurBordure 
			|| x*Variables.WORLD_TO_BOX > (1 - Variables.posBordure)*camera.viewportWidth - 2*Variables.largeurBordure
			|| y*Variables.WORLD_TO_BOX < Variables.posBordure*camera.viewportWidth + 2*Variables.largeurBordure
			|| y*Variables.WORLD_TO_BOX > (camera.viewportHeight - Variables.posBordure*camera.viewportWidth - 2*Variables.largeurBordure)){
				nbContact++;
			}
		
		//Si le pointeur est dans une zone déjà condamnée
		for(int i = 0; i < surfaces.size; i++){			
			if(		x*Variables.WORLD_TO_BOX >= surfaces.get(i).getX() 
				&& x*Variables.WORLD_TO_BOX <= (surfaces.get(i).getX() + surfaces.get(i).getWidth())
				&& (camera.viewportHeight - y*Variables.WORLD_TO_BOX) >= surfaces.get(i).getY()
				&& (camera.viewportHeight - y*Variables.WORLD_TO_BOX) <= (surfaces.get(i).getY() + surfaces.get(i).getHeight())){
				nbContact++;					
			}
		}
		
		//Si le pointeur est dans un mur
		if(vertical){
			for(int i = 0; i < barres.size; i++){		
				if(		x*Variables.WORLD_TO_BOX >= (barres.get(i).getX() - barres.get(i).getWidth() - Variables.largeurBordure)
					&& x*Variables.WORLD_TO_BOX <= (barres.get(i).getX() /*+ barres.get(i).getWidth()*/ + 2*Variables.largeurBordure)
					&& (camera.viewportHeight - y*Variables.WORLD_TO_BOX) >= (barres.get(i).getY() - barres.get(i).getHeight())
					&& (camera.viewportHeight - y*Variables.WORLD_TO_BOX) <= (barres.get(i).getY() + barres.get(i).getHeight())){
					nbContact++;
				}
			}
		}
		else{
			for(int i = 0; i < barres.size; i++){		
				if(		x*Variables.WORLD_TO_BOX >= (barres.get(i).getX() - barres.get(i).getWidth())
					&& x*Variables.WORLD_TO_BOX <= (barres.get(i).getX() + barres.get(i).getWidth())
					&& (camera.viewportHeight - y*Variables.WORLD_TO_BOX) >= (barres.get(i).getY() - barres.get(i).getHeight() - Variables.largeurBordure)
					&& (camera.viewportHeight - y*Variables.WORLD_TO_BOX) <= (barres.get(i).getY() /*+ barres.get(i).getHeight()*/ + 2*Variables.largeurBordure)){
					nbContact++;
				}
			}
		}
		
		if(nbContact == 0)
			return false;
		else
			return true;
	}
}
