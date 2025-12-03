package src.metier;

import src.interfaces.IGrapheAffichage;
import src.metier.gestionErreur.CodeErreur;
import src.metier.gestionFichier.Ecriture;
import src.metier.gestionFichier.Lecture;

import java.util.List;


import java.util.ArrayList;

/*-------------------------------*/
/* Class MPM                     */
/*-------------------------------*/
public class MPM implements IGrapheAffichage
{
	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/
	private static char    dateRef;
	private static DateFr  dateInit; 
	private static boolean affichageDate;
	private static boolean afficherCritique;

	private List<List<Tache>>    ensTachesParNiveau;
	private List<Tache>          ensTaches;
	private List<CheminCritique> ensCheminCritiques;

	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/
	public MPM( char dateRef, DateFr dateInit, String cheminFichier )
	{
		MPM.dateRef  = dateRef;
		MPM.dateInit = new DateFr( dateInit ); 
		MPM.affichageDate = false;

		this.ensTaches           = new ArrayList<Tache>         ();
		this.ensTachesParNiveau  = new ArrayList<List<Tache>>   ();
		this.ensCheminCritiques  = new ArrayList<CheminCritique>();


	}

	/*-------------------------------*/
	/* Accesseurs                    */
	/*-------------------------------*/
	public char                 getDateRef            () { return MPM.dateRef;                          }
	public DateFr               getDateInit           () { return MPM.dateInit;                         }
	public List<List<Tache>>    getListTacheParNiveau () { return this.ensTachesParNiveau;              }
	public List<Tache>          getListTache          () { return this.ensTaches ;                      }
	public List<CheminCritique> getListCheminCritique () { return this.ensCheminCritiques;              }
	public int                  getNbTache            () { return this.ensTaches.size();                }
	public int                  getNbJourMax          () { return this.getTache("FIN").getDte_tard();   }
	public boolean              getAffichageDate      () { return MPM.affichageDate;                    }


	public Tache getTache( String nom )
	{
		for ( Tache t : this.ensTaches )
		{
			if ( t.getNom().equals(nom) ) return t;
		}
		return null;
	}

	public List<Tache> getTachesPossisble ( Tache t )
	{
		if ( t == null ) return null;
		if ( t.getNom().equals( "DÉBUT" ) ) return new ArrayList<Tache>();

		List<Tache> lstTaches = new ArrayList<Tache>();
		for ( Tache tache : this.ensTaches )
		{
			if (  t.getNom() != tache.getNom() && !tache.getNom().equals( "FIN")      &&
			     !t.getlstPrc().contains( tache ) && !tache.getlstPrc().contains( t ) && 
				 !t.getlstSvt().contains( tache ) && !tache.getlstSvt().contains( t ) &&
				 !tache.estCyclique( t ) && !t.estCyclique( tache )                      )
			{
			
				lstTaches.add( tache );
			}
		}

		return lstTaches;
	}

	public boolean estTacheCritique( Tache t )
	{
		if ( t == null ) return false;

		for ( CheminCritique c : this.ensCheminCritiques )
		{
			if ( c.getCheminCritique().contains( t ) ) return true;
		}
		
		return false;
	}

	public boolean afficherCritique() { return MPM.afficherCritique; }


	/*-------------------------------*/
	/* Méthodes                      */
	/*-------------------------------*/
	public void setDateRef      ( char    dateRef  )  { MPM.dateRef       = dateRef;                }
	public void setDateInit     ( DateFr  dateInit  ) { MPM.dateInit      = new DateFr( dateInit ); }
	public void setAffichageDate( boolean affichage ) { MPM.affichageDate = affichage;              }
	public void forcerMaj () { this.afficherCritique = false;}
	
	public void setAfficherCritique()
	{
		this.calculerCheminCritique();
		MPM.afficherCritique = !MPM.afficherCritique;
	}

	public boolean ajouterTache( Tache t )
	{
		for ( Tache tache : this.ensTaches )
		{
			if ( t.getNom().equals( tache.getNom() ) )
				return false;
		}

		if ( t == null || this.ensTaches.contains( t ) )
			return false;

		this.ensTaches.add( t );

		return true;
	}

	private void supprimerTache( Tache t )
	{
		if ( t == null ) return;

		// prc
		List<Tache> listePrc = new ArrayList<>(t.getlstPrc());
		for ( Tache tPrc : listePrc )
			tPrc.supprimerSvt( t );
		
		// svt
		List<Tache> listeSvt = new ArrayList<>(t.getlstSvt());
		for ( Tache tSvt : listeSvt )
			tSvt.supprimerPrc( t );

		this.ensTaches.remove( t );
		this.maj();
	}	

	public void supprimerTacheEtReconnecter( Tache tSupprimer )
	{
		// reconnecte les tâches si nécessaire ( que si il y a plus de lien prc ou svt)
		List<Tache> listePrc = new ArrayList<>(tSupprimer.getlstPrc());
		List<Tache> listeSvt = new ArrayList<>(tSupprimer.getlstSvt());

		for (Tache tPrc : listePrc) 
		{
			if (tPrc.getNbSvt() == 1) 
			{
				for (Tache tSvt : listeSvt)
					tSvt.ajouterPrc(tPrc);
			}
		}

		for (Tache tSvt : listeSvt) 
		{
			if (tSvt.getNbPrc() == 1) 
			{
				for (Tache tPrc : listePrc)
					tSvt.ajouterPrc(tPrc);
			}
		}
		this.supprimerTache( tSupprimer );

		// Maj
		this.maj();
	}
	
	/*-------------------------------*/
	/* Niveaux                       */
	/*-------------------------------*/
	public void calculerNiveau() 
	{
		// Vérifier qu'il y a au moins une tâche
		if (this.ensTaches.isEmpty())  return;
		
		for ( Tache t : this.ensTaches )
			t.setNiveau(0);
		
		// Chercher la tâche DÉBUT et la définir à niveau 0
		Tache tacheDebut = this.getTache("DÉBUT");
		if (tacheDebut != null)     tacheDebut.setNiveau(0);
		else                        this.ensTaches.get(0).setNiveau(0);
		

		// parcours largeur ( niveau modif, on recommence )
		boolean modifie = true;
		while ( modifie ) 
		{
			modifie = false;
			for ( Tache t : this.ensTaches )
			{
				for ( Tache tSvt : t.getlstSvt() )
				{
					if ( tSvt.getNiveau() == -1 || tSvt.getNiveau() < t.getNiveau() + 1 )
					{
						tSvt.setNiveau(t.getNiveau() + 1);
						modifie = true;
					}
				}
			}
		}

		this.setListTacheParNiveau();
	}


	public void setListTacheParNiveau()
	{
		this.ensTachesParNiveau.clear();

		// plus grand niveau
		int maxNiveau = 0;
		for (Tache t : this.ensTaches) 
		{
			if ( t.getNiveau() > maxNiveau )
				maxNiveau = t.getNiveau();
		}

		// créer liste
		this. ensTachesParNiveau = new ArrayList<>();
		for (int cptNv = 0; cptNv <= maxNiveau; cptNv++) 
			this.ensTachesParNiveau.add( new ArrayList<>() );

		// ajout
		for (Tache t : this.ensTaches) 
			this.ensTachesParNiveau.get( t.getNiveau() ).add( t );	
	}

	/*--------------------------------*/
	/* Dates                          */
	/*--------------------------------*/
	public void calculerDates()
	{
		this.resetDates();

		// dates au plus tôt
		for ( int niveau=0; niveau < this.ensTachesParNiveau.size(); niveau++ )
		{
			for ( Tache t : this.ensTachesParNiveau.get(niveau) )
			{
				int max = 0;
				for ( Tache tPrc : t.getlstPrc() )
					max = Math.max( max, tPrc.getDte_tot() + tPrc.getDuree() );
				
				t.setDateTot( max );
			}
		}
		
		// dates au plus tard
		for (int niveau = this.ensTachesParNiveau.size() - 1; niveau >= 0; niveau--)
		{
			for (Tache t : this.ensTachesParNiveau.get(niveau))
			{
				int min = 10000;
				for (Tache tSvt : t.getlstSvt())
					min = Math.min(min, tSvt.getDte_tard() - t.getDuree());
				
				if ( min == 10000 ) t.setDateTard( t.getDte_tot() );
				else                t.setDateTard( min );
			}
		}

		int cptTacheDuNv = 0; // vérifier si il y esapce vide entre les niveaux

		// mis a jour des attributs d'affichage (si tache ajouté)
		for (int niveau = this.ensTachesParNiveau.size() - 1; niveau >= 0; niveau--)
		{
			for (Tache t : this.ensTachesParNiveau.get( niveau ) )
			{
				if ( t.getCalculee()[0] || t.getCalculee()[1] )
					for ( Tache t2 : this.ensTachesParNiveau.get(niveau) )
					{
						t2.setAfficherDateTot ( t.getCalculee()[0] );
						t2.setAfficherDateTard( t.getCalculee()[1] );
					}
			}
		}
	}

	public void afficherDate ( int niveau, char type )
	{
		if ( niveau < 0 || niveau > this.ensTachesParNiveau.size() ) return;

		if ( type == '-' )
		{
			for ( int cptNv = 0; cptNv<niveau+1; cptNv++ )
			{
				for ( Tache t : this.ensTachesParNiveau.get(cptNv) )
				{
					t.setAfficherDateTot ( true );
				}
			}
		}
		
		if ( type == '+' )
		{
			for ( int cptNv=niveau; cptNv<this.ensTachesParNiveau.size(); cptNv++ )
			{
				for ( Tache t : this.ensTachesParNiveau.get(cptNv) )
					t.setAfficherDateTard( true );
			}
		}
	}

	public void resetDates()
	{
		for ( Tache t : this.ensTaches )
			t.resetDates();
	}

	/*--------------------------------*/
	/* Calculer le chemin critique    */
	/*--------------------------------*/
	public void calculerCheminCritique()
	{
		this.ensCheminCritiques.clear();

		List<CheminCritique> pileChemin = new ArrayList<CheminCritique>();  // pile de chemin en cours
		Tache                tacheActu  = this.ensTaches.get( 0 );          // debut
		CheminCritique       cheminActu = new CheminCritique();             // chemin encours de construction
		
		// init les pile
		pileChemin.add( cheminActu );
		cheminActu.ajouterTache( tacheActu );
		
		// parcours tout les chemins
		while (!pileChemin.isEmpty())
		{
			if (tacheActu.getNbSvt() != 0)
			{
				// Explorer chaque successeur de la tâche actuelle
				for ( Tache t : tacheActu.getlstSvt() )
				{
					// si le successeur est sur un chemin critique (marge = 0)
					if (t.calculerMarge() == 0)  
					{
						// Retirer le chemin actuel de la pile
						pileChemin.remove( cheminActu );
						
						// maj du chemin actuel
						CheminCritique cheminTmp = new CheminCritique( cheminActu );
						cheminTmp.ajouterTache( t );
						
						// maj pile
						pileChemin.add( cheminTmp );
					}
				}
			}

			// parcous fini
			else 
			{
				// ajout du chemin si tâche fin est en fin
				if ( tacheActu.getNom().equals( "FIN" ) )
					this.ensCheminCritiques.add( cheminActu );
				
				pileChemin.remove( cheminActu);
			}
			
			// nouveau chemin
			if ( !pileChemin.isEmpty() ) 
				cheminActu = pileChemin.get( pileChemin.size()-1 );
			
			// pas de chemin, on prends la dernière tache
			if ( !cheminActu.getCheminCritique().isEmpty() )
				tacheActu = cheminActu.getCheminCritique().get( cheminActu.getCheminCritique().size()-1 );
		}
	}

	/*-------------------------------*/
	/* Sauvegarder et Charger        */
	/*-------------------------------*/
	public CodeErreur sauvegarder( String chemin ) 
	{ 
		return Ecriture.sauvegarde( this , chemin );
	}

	public CodeErreur charger( String chemin )
	{
		this.ensTaches         .clear();
		this.ensTachesParNiveau.clear();
		this.ensCheminCritiques.clear();

		CodeErreur codeErreur = Lecture.initMpm( this, chemin );
		
		// Ne calculer les niveaux que si le chargement a réussi et qu'il y a des tâches
		if (codeErreur == CodeErreur.OK && !this.ensTaches.isEmpty()) 
		{
			this.calculerNiveau();
			this.setListTacheParNiveau();
			this.calculerDates();
			this.calculerCheminCritique();
		}

		return codeErreur;
	}


	public void maj()
	{
		this.calculerNiveau();
		this.setListTacheParNiveau();
		this.calculerDates();
		this.calculerCheminCritique();
	}


	/*-------------------*/
	/* toString          */
	/*-------------------*/
	public String toString()
	{
		String sRet = "";

		int nbJourMax = 0;
		Tache finTache = this.getTache( "FIN" );
		if (finTache != null) {
			nbJourMax = finTache.getDte_tard();
		}

		for ( Tache t : this.ensTaches )
			sRet += t.toString( this.getDateRef(), this.getDateInit(), nbJourMax ) + "\n";
		
		return sRet;
	}
}