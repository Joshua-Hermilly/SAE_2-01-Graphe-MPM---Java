package src.metier.gestionFichier;

import src.metier.gestionErreur.CodeErreur;
import src.metier.MPM;
import src.metier.Tache;

import java.io.File;
import java.io.PrintWriter;
import java.io.FileOutputStream;


/*---------------------------------*/
/*  Class Ecriture                 */
/*---------------------------------*/
public abstract class Ecriture
{
	/*-------------------------------*/
	/* Sauvegarde                    */
	/*-------------------------------*/
	public static CodeErreur sauvegarde( MPM graphe, String nomFichier )
	{
		if ( nomFichier == null || nomFichier.isEmpty() ) return CodeErreur.ERREUR_ECRITURE_NOM_INVALIDE;
		if ( graphe == null ) return CodeErreur.ERREUR_ECRITURE_DONNEES_NULL;

		// Le fichier exciste ?
		File dossier = new File("./../data/save/");
		if ( !dossier.exists() || !dossier.isDirectory() ) return CodeErreur.ERREUR_ECRITURE_DOSSIER_INEXISTANT;
		
		File[] fichiers = dossier.listFiles();
		if ( fichiers != null )
		{
			for (File f : fichiers)
				if ( f.getName().equals( nomFichier + ".mpm2" ) || f.getName().equals( nomFichier + ".mpm" ) ) 
					return CodeErreur.ERREUR_ECRITURE_FICHIER_EXISTANT;
		}
	
		// .mpm2 ?
		if ( nomFichier.endsWith(".mpm2") )
		{
			nomFichier = nomFichier.substring( 0, nomFichier.length() - 5 );
			return Ecriture.sauvegardeMPM2( graphe, nomFichier );
		}
		
		// .mpm ?
		if ( nomFichier.endsWith(".mpm") )
		{
			nomFichier = nomFichier.substring( 0, nomFichier.length() - 4 );
			return Ecriture.sauvegardeMPM ( graphe, nomFichier );
		}

		return CodeErreur.ERREUR_ECRITURE_MAUVAISE_EXTENSION;
	}

	/*-------------------------------*/
	/* Sauvegarde  MPM2              */
	/*-------------------------------*/
	public static CodeErreur sauvegardeMPM2( MPM graphe, String nomFichier  )
	{
		if ( graphe == null ) return CodeErreur.ERREUR_ECRITURE_DONNEES_NULL;
		if ( nomFichier == null || nomFichier.isEmpty() ) return CodeErreur.ERREUR_ECRITURE_NOM_INVALIDE;
		
		String prc;
		try
		{
			PrintWriter pw = new PrintWriter( new FileOutputStream(  "./../data/save/" + nomFichier + ".mpm2" ) );
			for( Tache t : graphe.getListTache() )
			{
				if ( t == null ) return CodeErreur.ERREUR_ECRITURE_DONNEES_NULLES;
				
				prc = "";
				if ( t.getlstPrc() != null )
				{
					//Calcul de la liste des précédents
					for( int cpt = 0; cpt < t.getlstPrc().size(); cpt++ )
					{
						if ( cpt < t.getlstPrc().size() - 1 ) prc += t.getlstPrc().get(cpt).getNom() + ",";
						else                                  prc += t.getlstPrc().get(cpt).getNom();
					}
				}

				String calculee =  t.getCalculee()[0] + "," + t.getCalculee()[1];

				pw.println( t.getNom     () + "|" +
				            t.getDuree   () + "|" +
				            prc             + "|" +
							calculee		+ "|" +
				            t.getDte_tot () + "|" +
				            t.getDte_tard() + "|" +
				            t.getNiveau  () + "|" +
				            t.getPosX    () + "|" +
				            t.getPosY    ()         );
			}

			pw.close();
	
		} catch (Exception e) { return CodeErreur.ERREUR_ECRITURE_IO; }

		
		return CodeErreur.OK;
	}

	/*-------------------------------*/
	/* Sauvegarde  MPM               */
	/*-------------------------------*/
	public static CodeErreur sauvegardeMPM( MPM graphe, String nomFichier  )
	{
		if ( graphe == null ) return CodeErreur.ERREUR_ECRITURE_DONNEES_NULL;
		if ( nomFichier == null || nomFichier.isEmpty() ) return CodeErreur.ERREUR_ECRITURE_NOM_INVALIDE;
		
		String prc;
		try
		{
			PrintWriter pw = new PrintWriter( new FileOutputStream(  "./../data/save/" + nomFichier + ".mpm" ) );
			for( Tache t : graphe.getListTache() )
			{
				if ( t == null ) return CodeErreur.ERREUR_ECRITURE_DONNEES_NULLES;
				
				prc = "";
				if ( t.getlstPrc() != null )
				{
					//Calcul de la liste des précédents
					for( int cpt = 0; cpt < t.getlstPrc().size(); cpt++ )
					{
						if ( cpt < t.getlstPrc().size() - 1 ) prc += t.getlstPrc().get(cpt).getNom() + ",";
						else                                  prc += t.getlstPrc().get(cpt).getNom();
					}
				}

				pw.println( t.getNom     () + "|" +
				            t.getDuree   () + "|" +
				            prc             + "|" );
			}

			pw.close();
	
		} catch (Exception e) {return CodeErreur.ERREUR_ECRITURE_IO; }
		
		return CodeErreur.OK;
	}
	
}
