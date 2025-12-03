package src.metier.gestionFichier;

import src.metier.gestionErreur.CodeErreur;

import src.metier.MPM;
import src.metier.Tache;

import java.io.File;
import java.util.Scanner;


import java.util.List;

/*---------------------------------*/
/*  Class Lecture                  */
/*---------------------------------*/
public abstract class Lecture 
{

	/*-------------------------------*/
	/* Accesseurs                    */
	/*-------------------------------*/

	public static CodeErreur initMpm( MPM graphe, String lien )
	{
		String nomFichier = lien      .substring( lien      .lastIndexOf("/") + 1 );
		String extension  = nomFichier.substring( nomFichier.lastIndexOf('.') + 1 );

		// Vérification du fichier
		if ( lien == null || lien.isEmpty() ) return CodeErreur.ERREUR_LECTURE_FICHIER_INEXISTANT;
		if ( !new File( lien ).exists()     ) return CodeErreur.ERREUR_LECTURE_FICHIER_INEXISTANT;
		if ( graphe == null                 ) return CodeErreur.ERREUR_LECTURE_GRAPHE_NULL;

		// Vérification de l'extension du fichier
		switch ( extension )
		{
			// 2 cas : mpm = MPM simple, mpm2 = MPM attributs completé
					case "mpm" ->
			{
				CodeErreur resultat1 = Lecture.initTachesMPM( graphe, lien );
				if ( resultat1 != CodeErreur.OK ) return resultat1;
				
				CodeErreur resultat2 = Lecture.initPrcMPM( graphe, lien );
				if ( resultat2 != CodeErreur.OK ) return resultat2;
				
				Lecture.initSvtMPM( graphe, lien );
			}			
			
			case "mpm2" ->
			{
				CodeErreur resultat1 = Lecture.initTachesMPM2( graphe, lien );
				if ( resultat1 != CodeErreur.OK ) return resultat1;
				
				CodeErreur resultat2 = Lecture.initPrcMPM2( graphe, lien );
				if ( resultat2 != CodeErreur.OK ) return resultat2;
				
				//Lecture.initSvtMPM( graphe, lien );
			}

			default ->{ return CodeErreur.ERREUR_LECTURE_MAUVAISE_EXTENSION; }
		}
		
		return CodeErreur.OK;
	}

	// InitSvt
	public static CodeErreur initSvtMPM( MPM graphe, String lien )
	{
		if ( graphe == null ) return CodeErreur.ERREUR_LECTURE_GRAPHE_NULL;
		
		Tache fin = graphe.getTache( "FIN" );
		if ( fin == null ) return CodeErreur.ERREUR_LECTURE_TACHE_NULL;
		
		for ( Tache t : graphe.getListTache() )
			if ( t.getNbSvt() == 0 && !t.getNom().equals( "FIN" ) ) 
			{
				CodeErreur resultat = fin.ajouterPrc( t );
				if (resultat != CodeErreur.OK)
					return resultat;
			}
		
		return CodeErreur.OK;
	}
	

	/*-------------------------------*/
	/* MPM                           */
	/*-------------------------------*/
	// InitTaches
	public static CodeErreur initTachesMPM( MPM graphe, String lien )
	{
		String[] ligne = null;
		String   nom   = null;
		int      duree = 0;
		int      cptL  = 0;
		Tache    tache = null;

		graphe.ajouterTache( new Tache( "DÉBUT", 0, 0 ) );
		try 
		{
			Scanner scFic = new Scanner ( new File( lien ), "UTF-8" );
		
			while ( scFic.hasNextLine() )
			{
				cptL++;

				ligne = scFic.nextLine().split("\\|");
				if ( ligne.length < 2 ) return CodeErreur.ERREUR_LECTURE_DONNEES_MANQUANTES;
				if ( ligne.length > 3 ) return CodeErreur.ERREUR_LECTURE_STRUCTURE_INVALIDE;

				nom   = ligne[0].trim();
				duree = Integer.parseInt(ligne[1].trim());

				if ( nom.isEmpty() || duree < 0 ) return CodeErreur.ERREUR_LECTURE_VALEURS_INCOHERENTES;

				tache = new Tache( nom, duree );

				if ( tache == null ) return CodeErreur.ERREUR_LECTURE_TACHE_NULL;
				
				// Ajout de la tache 
				graphe.ajouterTache( tache );
			
			}scFic.close();

		} catch (Exception e) { return CodeErreur.ERREUR_LECTURE_VALEURS_INCOHERENTES; }

		if ( cptL == 0 ) return CodeErreur.ERREUR_LECTURE_FICHIER_VIDE;

		graphe.ajouterTache ( new Tache( "FIN", 0 ) );

		return CodeErreur.OK;
	}
		
	// InitPrc
	public static CodeErreur initPrcMPM( MPM graphe, String lien )
	{
		String[] ligne;
		String[] tabPrc;
		Tache    tache;
		int      cptP = 0;
		Scanner  scFic;
		
		try 
		{
			scFic = new Scanner ( new File( lien ), "UTF-8" );
			
			while ( scFic.hasNextLine() )
			{
				ligne = scFic.nextLine().split("\\|");

				String nomTache = ligne[0].trim();
				tache = graphe.getTache( nomTache );

				if ( tache != null )
				{
					// a des precedents ?
					if ( ligne.length > 2 && !ligne[2].trim().isEmpty() )
					{
						tabPrc = ligne[2].split(",");
						cptP = 0;						for (String prec : tabPrc) 
						{
							prec = prec.trim();
							Tache tachePrc = graphe.getTache( prec );

							if ( tachePrc == null ) return CodeErreur.ERREUR_LECTURE_TACHE_NULL;
							
							CodeErreur resultat = tache.ajouterPrc( tachePrc );
							if ( resultat != CodeErreur.OK ) return resultat;
							
							cptP++;							
						}
					
						if ( cptP == 0 ) return CodeErreur.ERREUR_LECTURE_DONNEES_MANQUANTES;
					}
					else
					{
						if ( !tache.getNom().equals("DÉBUT") && !tache.getNom().equals("FIN") )
						{
							tache.ajouterPrc( graphe.getTache("DÉBUT") );
						}
					}
				}
				else  { return CodeErreur.ERREUR_LECTURE_TACHE_NULL; }
			
			}
			scFic.close();

		} catch (Exception e) { return CodeErreur.ERREUR_LECTURE_VALEURS_INCOHERENTES;  }

		return CodeErreur.OK;
	}


	/*-------------------------------*/
	/* MPM2                          */
	/*-------------------------------*/
	public static CodeErreur initTachesMPM2( MPM graphe, String lien )
	{
		int duree, dteTot, dteTard, niveau, posX, posY;
		boolean calculee0, calculee1;

		String[] ligne = null;
		String   nom   = null;
		Tache    tache = null;
		int      cptL = 0;

		List<Tache> lstPrc ;

		duree = dteTot = dteTard = niveau = posX = posY = 0;

		try 
		{
			Scanner scFic = new Scanner ( new File( lien ), "UTF-8" );

			while ( scFic.hasNextLine() )
			{
				cptL++;

				ligne = scFic.nextLine().split("\\|");

				if ( ligne.length < 8 ) return CodeErreur.ERREUR_LECTURE_DONNEES_MANQUANTES;
				if ( ligne.length > 9 ) return CodeErreur.ERREUR_LECTURE_STRUCTURE_INVALIDE;

				nom     =                   ligne[0].trim()  ;
				duree   = Integer.parseInt( ligne[1].trim() );
				
				String[] calculeeTab = ligne[3].trim().split(",");
				calculee0 = Boolean.parseBoolean( calculeeTab[0].trim() );
				calculee1 = Boolean.parseBoolean( calculeeTab[1].trim() );
				
				dteTot  = Integer.parseInt( ligne[4].trim() );
				dteTard = Integer.parseInt( ligne[5].trim() );
				niveau  = Integer.parseInt( ligne[6].trim() );
				posX    = Integer.parseInt( ligne[7].trim() );
				posY    = Integer.parseInt( ligne[8].trim() );
				
				// Vérification des valeurs (DÉBUT et FIN peuvent avoir durée = 0)
				if ( nom.isEmpty() || duree < 0 || dteTot < 0 || dteTard < 0 || niveau < 0 || posX < 0 || posY < 0 )
					return CodeErreur.ERREUR_LECTURE_VALEURS_INCOHERENTES;

				tache = new Tache( nom, duree, dteTot, dteTard, niveau, posX, posY );

				if ( tache == null ) return CodeErreur.ERREUR_LECTURE_TACHE_NULL;

				// attribut d'affichage
				tache.setAfficherDateTot ( calculee0 );
				tache.setAfficherDateTard( calculee1 );

				graphe.ajouterTache(tache);
			}

			scFic.close();
		} catch (Exception e) { return CodeErreur.ERREUR_LECTURE_VALEURS_INCOHERENTES; }

		if ( cptL == 0 ) return CodeErreur.ERREUR_LECTURE_FICHIER_VIDE;
		if ( graphe.getTache("DÉBUT") == null || graphe.getTache("FIN") == null )
			return CodeErreur.ERREUR_LECTURE_DONNEES_MANQUANTES;

		return CodeErreur.OK;
	}


	// InitPrc pour fichiers .mpm2
	public static CodeErreur initPrcMPM2( MPM graphe, String lien )
	{
		String[] ligne  = null;
		String[] tabPrc = null;
		Tache    tache  = null;
		int      cptP   = 0;
	
		try 
		{
			Scanner scFic = new Scanner ( new File( lien ), "UTF-8" );
			
			while ( scFic.hasNextLine() )
			{
				ligne = scFic.nextLine().split("\\|");

				String nomTache = ligne[0].trim();
				tache = graphe.getTache( nomTache );

				if ( tache != null )
				{
					if ( ligne.length > 2 && !ligne[2].trim().isEmpty() )
					{
						cptP = 0;
						tabPrc = ligne[2].split(",");
								for (String prec : tabPrc ) 
						{
							prec = prec.trim();
							Tache tachePrc = graphe.getTache( prec );

							if ( tachePrc == null ) return CodeErreur.ERREUR_LECTURE_TACHE_NULL;
							
							CodeErreur resultat = tache.ajouterPrc( tachePrc );
							if ( resultat != CodeErreur.OK ) return resultat;
							
							cptP++;	
						}
					}
				}
				else  { return CodeErreur.ERREUR_LECTURE_TACHE_NULL; }	
			} scFic.close();
		} catch (Exception e) { return CodeErreur.ERREUR_LECTURE_VALEURS_INCOHERENTES;  }

		return CodeErreur.OK;
	}
}
