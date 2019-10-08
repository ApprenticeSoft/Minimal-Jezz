package com.minimal.jezz;

import com.badlogic.gdx.utils.Array;
import com.minimal.jezz.enums.EtatPoint;

public class Point {
	
	float x, y;
	EtatPoint etatPoint;

	public Point(float X, float Y, EtatPoint EtatPoint){
		x = (float)((int)(Math.round(100 * X)))/100;
		y = (float)((int)(Math.round(100 * Y)))/100;
		etatPoint = EtatPoint;
	}
	
	public void setX(float X){
		x = (float)((int)(Math.round(100 * X)))/100;
	}
	
	public float getX(){
		return x;
	}
	
	public void setY(float Y){
		y = (float)((int)(Math.round(100 * Y)))/100;
	}
	
	public float getY(){
		return y;
	}
	
	public void setEtat(EtatPoint EtatPoint){
		etatPoint = EtatPoint;
	}
	
	public EtatPoint getEtat(){
		return etatPoint;
	}
	
	public static void eviteDoublon(Array<Point> array){
		for(int i = 0; i < array.size; i++){
			for(int j = 0; j < array.size; j++){
				if(i != j
					&& array.get(i).getX() == array.get(j).getX()
					&& array.get(i).getY() == array.get(j).getY()){
					array.removeIndex(j);
				}
			}
		}
	}
}
