package com.minimal.jezz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.minimal.jezz.body.Balle;

public class LevelHandler {
	private static Array<Balle> balles;
	private static World world;
	private static Camera camera;
	private float objectif;
	private float vitesse;
	private int nbBalles;
	
	public LevelHandler(World world, Camera camera, Array<Balle> balles){
		this.balles = balles;
		this.world = world;
		this.camera = camera;
		
		objectif = 0;
		vitesse = 0;
		nbBalles = 0;
	}
	
	public void loadLevel(int niveauChoisi) {
		System.out.println("loading level");
		FileHandle file = Gdx.files.internal("Niveaux.txt");
		String[] fileContent = file.readString().split("\n");
		for(int i = 0; i < fileContent.length; i++) {
			String[] blockData = fileContent[i].split(",");
			if (blockData.length == 4) {
				int niveau;
				
				if(Integer.valueOf(blockData[0]) == niveauChoisi){
					try{
						niveau = Integer.valueOf(blockData[0]);
						nbBalles = Integer.valueOf(blockData[1]);
						vitesse = Float.valueOf(blockData[2]);
						objectif = Float.valueOf(blockData[3]);
						
						for(int j = 0; j < nbBalles; j++){
							Balle balle = new Balle(world, camera, camera.viewportWidth/5, camera.viewportHeight/2);
							balles.add(balle);
						}
						
						Variables.vitesseBalle = vitesse;
						Variables.objectif = objectif;
						
					}catch(Exception e){
						System.out.println("Exception : " + e);	
					}
				}
			}
		}
	}
	
	public void setNbBalles(int balle){
		nbBalles = balle;
	}
	
	public int getNbBalles(){
		return nbBalles;
	}
	
	public void setVitesse(float speed){
		vitesse = speed;
	}
	
	public float getVitesse(){
		return vitesse;
	}
	
	public void setObjectif(float obj){
		objectif = obj;
	}
	
	public float getObjectif(){
		return objectif;
	}
}
