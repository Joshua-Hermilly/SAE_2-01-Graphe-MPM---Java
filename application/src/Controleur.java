package src;

import src.metier.*;
import src.ihm.frames.*;

import src.metier.gestionErreur.CodeErreur;
import src.metier.gestionFichier.Lecture;
import src.metier.gestionFichier.Ecriture;

import src.interfaces.IGrapheAffichage;

import javax.swing.JOptionPane;

import java.util.ArrayList;
import java.util.List;


/*---------------------------------*/
/*  Class Controleur               */
/*---------------------------------*/
public class Controleur 
{
	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/
	private MPM             graphe;
	private FramePrincipale ihm;
	private String          chemin;

	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/
	public Controleur( char dateRef, DateFr dateInit, String cheminFichier )
	{
		this.chemin = cheminFichier;

		this.graphe = new MPM( dateRef, dateInit, cheminFichier );
			
		// Chargement du fichier MPM
		CodeErreur erreur = this.graphe.charger( cheminFichier );
		
		if ( erreur != CodeErreur.OK )  Controleur.gestionErreur( erreur );
		
		
		this.ihm = new FramePrincipale( this );
	}

	/*-------------------------------*/
	/* Accesseurs                    */
	/*-------------------------------*/
	public char                 getDateRef            () { return this.graphe.getDateRef();             }
	public DateFr               getDateInit           () { return this.graphe.getDateInit();            }
	public int                  getNbJour             () { return this.graphe.getNbJourMax();           }
	public String               getCheminFichier      () { return this.chemin;                          }
	public int                  getNbTaches           () { return this.graphe.getNbTache();             }

	public IGrapheAffichage     getGraphe             () { return this.graphe;                          }
	public FramePrincipale      getIhm                () { return this.ihm;                             }
	public List<Tache>          getListTache          () { return this.graphe.getListTache();           }
	public List<List<Tache>>    getListTacheParNiveau () { return this.graphe.getListTacheParNiveau();  }
	public List<CheminCritique> getCheminCritique     () { return this.graphe.getListCheminCritique();  }

	public Tache                getTache           ( String nom ) { return this.graphe.getTache( nom );         }
	public List<Tache>          getTachesPossible  ( Tache    t ) { return this.graphe.getTachesPossisble( t ); }

	/*-------------------------------*/
	/* Méthodes d'affichages         */
	/*-------------------------------*/
	public void setAffichageDate( boolean affichage ) 
	{ 
		this.graphe.setAffichageDate( affichage ); 
		this.maj();
	}

	public boolean afficherDate ( int niveau, char type )
	{
		try 
		{
			this.graphe.afficherDate  ( niveau, type );
			this.ihm   .afficherDetail( niveau, type );
			return true;
		} 
		catch ( Exception e ) {} //{ CodeErreur.ERREUR_NIVEAU_INVALIDE; } // pas de rest du projetn (gérer dans la frame)

		return false;
	}

	public void afficherCheminCritique() 
	{
		this.graphe.setAfficherCritique();
		this.maj();
	}

	public void afficherVueTache ( Tache t ) 
	{ 
		this.ihm.afficherVueTache( t ); 
	}


	/*-------------------------------*/
	/* Méthodes chargement et maj    */
	/*-------------------------------*/
	public void charger ( String chemin ) 
	{  
		this.chemin = chemin;

		CodeErreur erreur = this.graphe.charger(chemin);
		if (erreur != CodeErreur.OK )
		{
			Controleur.gestionErreur(erreur);
		}
		else
		{
			this.ihm.actualiserFichier();
			this.graphe.forcerMaj();

			if ( chemin.endsWith(".mpm") ) this.ihm.resetMPM( true  );
			else                           this.ihm.resetMPM( false );
		}	
	}

	public boolean sauvegarder ( String chemin ) 
	{ 
		CodeErreur erreur = Ecriture.sauvegarde(this.graphe, chemin);
		if ( erreur != CodeErreur.OK ) 
		{
			Controleur.gestionErreur(erreur);
			return false;
		}
		return true;  
	}

	public void repositionner() 
	{
		this.ihm.resetMPM( true);
	}

	public void changerDate( char dateRef, String dateINit )
	{
		try 
		{
			this.graphe.setDateRef ( dateRef );
			this.graphe.setDateInit( new DateFr( dateINit ) );
		} 
		catch ( Exception e ) 
		{
			Controleur.gestionErreur( CodeErreur.ERREUR_DEFAUT );
			return;
		}

		this.maj();	
	}

	public void maj()
	{
		this.graphe.maj();
		this.ihm.majMPM();
	}
	

	/*-------------------------------*/
	/* Gestion des Tâches            */
	/*-------------------------------*/
	public void creerTache() { new FrameAjouter( this ); }

	public boolean ajouterTache( Tache t )
	{
		boolean ajoutee = false;

		ajoutee = this.graphe.ajouterTache( t );

		if ( ajoutee )
		{
			this.graphe.maj();
			this.ihm.resetMPM( true );
		}
	
		return ajoutee;
	}

	public void supprimerTache( Tache t )
	{
		List<Tache> listePrc = new ArrayList<Tache>( t.getlstPrc() );
		List<Tache> listeSvt = new ArrayList<Tache>( t.getlstSvt() );
		
		new FrameSupprimer( this, t, listePrc, listeSvt );
	}
	
	public void validerTache( Tache tSupprimer )
	{
		if ( tSupprimer != null ) 
		{
			this.graphe.supprimerTacheEtReconnecter( tSupprimer );

			this.graphe.maj();
			this.ihm.resetMPM( true );
		}
	}

	public void modifierTache( Tache t, int duree )
	{
		if ( t == null ) return;
		if ( duree <= 0 ) 
		{
			Controleur.gestionErreur( CodeErreur.ERREUR_TACHE_DUREE_INVALIDE );
			return;
		}

		t.setDuree( duree );

		this.graphe.calculerDates();
		this.maj();
	}

	public void modifierLienTache ( String nomTache1, String nomTache2, char code )
	{
		Tache t1 = this.graphe.getTache( nomTache1 );
		Tache t2 = this.graphe.getTache( nomTache2 );

		if ( t1 == null || t2 == null ) return;

		// - : suppression
		// + : ajout
		if ( code == '-' ) 
		{
			t2.supprimerPrc( t1 );
		}
		else 
		{
			CodeErreur erreur = t2.ajouterPrc( t1 );
			
			if (erreur != CodeErreur.OK) 
			{
				Controleur.gestionErreur(erreur);
				return;
			}
		}
			
		this.graphe.maj();
		this.ihm.resetMPM( true );
		this.ihm.majVue();
	}


	/*------------------*/
	/* MAIN             */
	/*------------------*/
	public static void main(String[] args)  
	{ 
		Controleur.lancer();
	}

	public static void lancer()
	{
		new FrameLancerApplication();
	}

	public static void gestionErreur( CodeErreur code )
	{
		JOptionPane.showMessageDialog( null, code.toString(), "Erreur", JOptionPane.ERROR_MESSAGE );

		System.exit( code.getCode() );
	}
}
