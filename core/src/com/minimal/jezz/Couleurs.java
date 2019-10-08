package com.minimal.jezz;

import com.badlogic.gdx.graphics.Color;

public class Couleurs {

	int couleur;
	private Color couleur1, couleur2, couleur3, couleur4, couleur5, couleur6;
	
	public Couleurs(int couleur){
		this.couleur = couleur;
		

		if(couleur == 1){ 							//COULEURS DU MENU	
			couleur1 = new Color().set(1,1,1,1);		
			couleur2 = new Color().set(35/256f,59/256f,95/256f, 1);	
			couleur3 = new Color().set(126/256f,0/256f,10/256f, 1);			
			couleur4 = new Color().set(250/256f,163/256f,27/256f,1);
			couleur5 = new Color().set(182/256f,217/256f,216/256f, 1);
			couleur6 = new Color().set(220/256f,71/256f,24/256f, 1);
		}
		else if(couleur == 2){ 								
			couleur5 = new Color().set(1,1,1,1);		
			couleur1 = new Color().set(253/256f,240/256f,213/256f, 1);
			couleur4 = new Color().set(216/256f,30/256f,91/256f, 1);
			couleur3 = new Color().set(240/256f,84/256f,79/256f,1);
			couleur2 = new Color().set(58/256f,51/256f,53/256f, 1);				
			couleur6 = new Color().set(188/256f,216/256f,211/256f,1);
		}
		else if(couleur == 3){ 										
			couleur1 = new Color().set(253/256f,240/256f,213/256f, 1);
			couleur2 = new Color().set(58/256f,51/256f,53/256f, 1);
			couleur3 = new Color().set(240/256f,84/256f,79/256f,1);
		}
		else if(couleur == 4){ 								
			couleur1 = new Color().set(1,1,1,1);		
			couleur2 = new Color().set(58/256f,51/256f,53/256f, 1);
			couleur3 = new Color().set(216/256f,30/256f,91/256f, 1);
		}
		else if(couleur == 5){ 								
			couleur1 = new Color().set(1,1,1,1);		
			couleur2 = new Color().set(76/256f,141/256f,166/256f, 1);				
			couleur3 = new Color().set(91/256f,96/256f,140/256f,1);
		}
		else if(couleur == 6){ 								
			couleur1 = new Color().set(1,1,1,1);		
			couleur2 = new Color().set(76/256f,141/256f,166/256f, 1);
			couleur3 = new Color().set(250/256f,110/256f,105/256f, 1);
		}
		else if(couleur == 7){ 							
			couleur1 = new Color().set(213/256f,79/256f,88/256f, 1);		
			couleur2 = new Color().set(243/256f,237/256f,211/256f, 1);
			couleur3 = new Color().set(17/256f,63/256f,89/256f, 1);
		}
		else if(couleur == 8){ 								
			couleur1 = new Color().set(1,1,1,1);		
			couleur2 = new Color().set(230/256f,102/256f,63/256f, 1);
			couleur3 = new Color().set(92/256f,126/256f,138/256f, 1);
		}
		else if(couleur == 9){ 						
			couleur1 = new Color().set(254/256f,29/256f,57/256f, 1);	
			couleur2 = new Color().set(145/256f,142/256f,139/256f, 1);
			couleur3 = new Color().set(54/256f,55/256f,55/256f, 1);
		}
		else if(couleur == 10){ 					
			couleur1 = new Color().set(1,1,1,1);				
			couleur2 = new Color().set(224/256f,194/256f,132/256f, 1);
			couleur3 = new Color().set(9/256f,112/256f,104/256f, 1);
		}
		else if(couleur == 11){ 					
			couleur1 = new Color().set(1,1,1,1);				
			couleur2 = new Color().set(221/256f,97/256f,74/256f, 1);
			couleur3 = new Color().set(115/256f,165/256f,128/256f, 1);
		}
		else if(couleur == 12){ 					
			couleur1 = new Color().set(1,1,1,1);				
			couleur2 = new Color().set(122/256f,147/256f,172/256f, 1);
			couleur3 = new Color().set(221/256f,97/256f,74/256f, 1);
		}
		else if(couleur == 13){ 					
			couleur1 = new Color().set(236/256f,235/256f,243/256f, 1);				
			couleur2 = new Color().set(199/256f,214/256f,213/256f, 1);
			couleur3 = new Color().set(93/256f,163/256f,153/256f, 1);
		}
		else if(couleur == 14){ 					
			couleur1 = new Color().set(1,1,1,1);				
			couleur2 = new Color().set(213/256f,198/256f,122/256f, 1);
			couleur3 = new Color().set(37/256f,92/256f,153/256f, 1);
		}
		else if(couleur == 15){ 					
			couleur1 = new Color().set(1,1,1,1);				
			couleur2 = new Color().set(84/256f,75/256f,61/256f, 1);
			couleur3 = new Color().set(78/256f,110/256f,88/256f, 1);
		}

	}
	
	public Color getCouleur1(){
		return couleur1;
	}
	
	public Color getCouleur2(){
		return couleur2;
	}
	
	public Color getCouleur3(){
		return couleur3;
	}
	
	public Color getCouleur4(){
		return couleur4;
	}
	
	public Color getCouleur5(){
		return couleur5;
	}
	
	public Color getCouleur6(){
		return couleur6;
	}
}
