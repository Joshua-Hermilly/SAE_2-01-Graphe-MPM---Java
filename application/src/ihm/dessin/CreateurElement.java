package src.ihm.dessin;

import java.util.List;

import src.Controleur;
import src.metier.Tache;

/*--------------------------*/
/* Class CreateurElement    */
/*--------------------------*/
public abstract class CreateurElement 
{
	/*--------------------------*/
	/* Attributs                */
	/*--------------------------*/
	public static final int MARGE_Y = 50;
	public static final int MARGE_X = 50;

	public static int depX          = 10;

	public static int largeurSommet = 120;
	public static int hauteurSommet = 50;

	/*--------------------------*/
	/* Modificateurs            */
	/*--------------------------*/
	public static void setLargeurSommet( int largeur ) 
	{
		CreateurElement.largeurSommet = Math.max(CreateurElement.largeurSommet, largeur);
		CreateurElement.depX = largeur / 2 + 20; 
	}

	/*-------------------------------*/
	/* CAS DES SOMMETS               */
	/*-------------------------------*/	
	// DEBUT
	public static void creerSommetDebut( MetierDessin metierDessin, Controleur ctrl, int hauteurPanel ) 
	{
		Tache t = ctrl.getTache( "DÉBUT" );

		int posX =  CreateurElement.depX;                                                 // Départ gauche
		int posY = ( hauteurPanel - CreateurElement.hauteurSommet ) / 2;                  // Centré hauteur
		
		Sommet sommet = new Sommet( metierDessin.getGraphe(), t, 
		                            posX, posY, 
		                            CreateurElement.largeurSommet, CreateurElement.hauteurSommet );
		
		metierDessin.ajouterElement( sommet );
		
		// maj des pos
		t.setPosX( sommet.getCentreX() );
		t.setPosY( sommet.getCentreY() );
	}	
	
	// FIN
	public static void creerSommetFin( MetierDessin metierDessin, Controleur ctrl, int niveau, int hauteurPanel ) 
	{
		Tache t = ctrl.getTache( "FIN" );

		int posX =  CreateurElement.depX + niveau * ( CreateurElement.largeurSommet + CreateurElement.MARGE_X );     // Fin droite 
		int posY = ( hauteurPanel - CreateurElement.hauteurSommet ) / 2;                                             // Centré hauteur
		
		Sommet sommet = new Sommet( metierDessin.getGraphe(), t, 
		                            posX, posY, 
		                            CreateurElement.largeurSommet, CreateurElement.hauteurSommet );
		
		metierDessin.ajouterElement( sommet );
		
		// maj des pos
		t.setPosX( sommet.getCentreX() );
		t.setPosY( sommet.getCentreY() );
	}	
	
	// Tâche simple ( 1prc & 1svt )
	public static void creerSommetSimple1( MetierDessin metierDessin, Tache t, int niveau, int hauteurPanel  ) 
	{
		int posX =  CreateurElement.depX + niveau * ( CreateurElement.largeurSommet + CreateurElement.MARGE_X );  // niveau
		int posY = t.getlstPrc().get(0).getPosY();                                                                // Pos de prc
		
		Sommet sommet = new Sommet( metierDessin.getGraphe(), t,
		                            posX, posY, 
		                            CreateurElement.largeurSommet, CreateurElement.hauteurSommet );
		
		metierDessin.ajouterElement( sommet );
		
		// maj des pos
		t.setPosX( sommet.getCentreX() );
		t.setPosY( sommet.getCentreY() );
	}

	// Tâche simple ( >1prc & 1svt )
	public static void creerSommetSimple2( MetierDessin metierDessin, Tache t, int niveau, int hauteurPanel  ) 
	{
		int posX, posY;
		
		// Calculer de nouvelles positions (fichiers .mpm)
		Tache tPrc1 = t.getlstPrc().get( 0 );                                                                 // 1er 
		Tache tPrc2 = t.getlstPrc().get( t.getNbPrc()-1 );                                                    // dernier

		posX = CreateurElement.depX + niveau * ( CreateurElement.largeurSommet + CreateurElement.MARGE_X );   // niveau
		posY = ( tPrc1.getPosY() + tPrc2.getPosY() ) / 2;                                                     // Moyenne des prc
		
		Sommet sommet = new Sommet( metierDessin.getGraphe(), t,
		                            posX, posY, 
		                            CreateurElement.largeurSommet, CreateurElement.hauteurSommet );
		
		metierDessin.ajouterElement( sommet );
		
		// MAJ DES POS
		t.setPosX( 	sommet.getCentreX() );
		t.setPosY( 	sommet.getCentreY() );
		
	}	
	
	// Tâche simple ( 1prc & >1svt )
	public static void creerSommetSimple3( MetierDessin metierDessin, Tache t, int niveau, int hauteurPanel  ) 
	{
		int posX = CreateurElement.depX + niveau * ( CreateurElement.largeurSommet + CreateurElement.MARGE_X );  // niveau
		int posY = ( hauteurPanel - CreateurElement.hauteurSommet ) / 2;                                         // Centré hauteur
		
		Sommet sommet = new Sommet( metierDessin.getGraphe(), t,
		                            posX, posY, 
		                            CreateurElement.largeurSommet, CreateurElement.hauteurSommet );
		
		metierDessin.ajouterElement( sommet );
		
		// maj des pos
		t.setPosX( sommet.getCentreX() );
		t.setPosY( sommet.getCentreY() );
	}	
	
	// Tâche simple ( >1prc & >1svt )
	public static void creerSommetSimple4( MetierDessin metierDessin, Tache t, int niveau, int hauteurPanel ) 
	{
		int posX = CreateurElement.depX + niveau * ( CreateurElement.largeurSommet + CreateurElement.MARGE_X );  // niveau
		int posY = ( hauteurPanel - CreateurElement.hauteurSommet ) / 2;                                         // Centré hauteur
		
		Sommet sommet = new Sommet( metierDessin.getGraphe(), t,
		                            posX, posY, 
		                            CreateurElement.largeurSommet, CreateurElement.hauteurSommet );
		
		metierDessin.ajouterElement( sommet );
		
		// maj des pos
		t.setPosX( sommet.getCentreX() );
		t.setPosY( sommet.getCentreY() );
	}	
	
	// Autre cas
	public static void creerSommetsMultiples( MetierDessin metierDessin, List<Tache> lstTaches, int niveau, int hauteurPanel ) 
	{
		int nbTache = lstTaches.size() -1;
		int hauteurTotale = nbTache * CreateurElement.hauteurSommet + nbTache * MARGE_Y;   //hauteur totale de la colonne
		int posYDepart = ( hauteurPanel - hauteurTotale ) / 2;                             // Centré hauteur

		for (int cptT = 0; cptT<=nbTache; cptT++) 
		{
			Tache t  = lstTaches.get( cptT );

			int posX = CreateurElement.depX + niveau * ( CreateurElement.largeurSommet + CreateurElement.MARGE_X );        // niveau
			int posY = posYDepart + cptT * ( CreateurElement.hauteurSommet + MARGE_Y );                                    // posY en fonction de cpt

			Sommet sommet = new Sommet( metierDessin.getGraphe(), t,
			                            posX, posY, 
			                            CreateurElement.largeurSommet, CreateurElement.hauteurSommet );
			
			metierDessin.ajouterElement( sommet );
			
			// maj des pos
			t.setPosX( sommet.getCentreX() );
			t.setPosY( sommet.getCentreY() );
		}
	}


	/*---------------------*/
	/* AVEC POSITION       */
	/*---------------------*/
	public static void creerSommetAvecPos( MetierDessin metierDessin, Controleur ctrl ) 
	{
		for ( Tache tache : ctrl.getListTache() ) 
		{
			Sommet sommet = new Sommet( metierDessin.getGraphe(), tache,
			                            tache.getPosX(), tache.getPosY(),
			                            CreateurElement.largeurSommet, CreateurElement.hauteurSommet );
			
			metierDessin.ajouterElement(sommet);
			
			// maj des pos
			tache.setPosX( sommet.getCentreX() );
			tache.setPosY( sommet.getCentreY() );
		}
	}
}
