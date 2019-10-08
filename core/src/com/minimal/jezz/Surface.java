package com.minimal.jezz;

import com.badlogic.gdx.utils.Array;
import com.minimal.jezz.body.Balle;

public class Surface {

	float x, y, width, height;
	int nbBalle;
	
	public Surface(float X, float Y, float Width, float Height){
		x = X;
		y = Y;
		width = Width;
		height = Height;	
		
		nbBalle = 0;
	}
	
	public boolean detecteBalle(Array<Balle> balles){
		for(int m = 0; m < balles.size; m++){
			if(balles.get(m).body.getPosition().x > this.getX() 
				&& balles.get(m).body.getPosition().x < (this.getX() + this.getWidth())
				&& balles.get(m).body.getPosition().y > this.getY()
				&& balles.get(m).body.getPosition().y < (this.getY() + this.getHeight())){
				nbBalle++;
			}
		}
		if(nbBalle == 0)
			return false;
		else
			return true;
	}
	
	public void setX(float X){
		x = X;
	}
	
	public float getX(){
		return x;
	}
	
	public void setY(float Y){
		y = Y;
	}
	
	public float getY(){
		return y;
	}
	
	public void setWidth(float Width){
		width = Width;
	}
	
	public float getWidth(){
		return width;
	}
	
	public void setHeight(float Height){
		height = Height;
	}
	
	public float getHeight(){
		return height;
	}
	
	public void setNbBalle(int nb){
		nbBalle = nb;
	}
	
	public int getNbBalle(){
		return nbBalle;
	}
}
