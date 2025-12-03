package src.ihm.dessin;

import src.Controleur;
import src.metier.Tache;

import src.ihm.frames.FramePrincipale;

import src.interfaces.IGrapheAffichage;

import java.util.List;
import java.util.ArrayList;

/*---------------------------------*/
/*  Class MetierDessin             */
/*---------------------------------*/
public class MetierDessin 
{
	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/
	private Controleur      ctrl;
	private FramePrincipale frame;

	private List<Element>   lstElement;
	

	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/
	public MetierDessin(Controleur ctrl, FramePrincipale frame) 
	{
		this.ctrl  = ctrl;
		this.frame = frame;
		this.lstElement = new ArrayList<Element>();

	}

	/*-------------------------------*/
	/* Accesseurs                    */
	/*-------------------------------*/
	public List<Element>     getListElement() { return this.lstElement;       }
	public IGrapheAffichage  getGraphe     () { return this.ctrl.getGraphe(); }

	public Sommet getSommetParTache( Tache tache ) 
	{
		for (Element elt : this.lstElement) 
		{ 
			if ( elt instanceof Sommet && elt.getTache().equals( tache ) ) 
			{
				return (Sommet) elt;
			}
		}

		return null;
	}

	public void calculerLargeur()
	{
		int max = 0;
		for ( Tache t : this.ctrl.getListTache() ) 
		{
			if ( t.getNom().length()*10 > max )
			{
				max = t.getNom().length()*10;
			} 
		}
	
		CreateurElement.setLargeurSommet( max + 20 );
	}


	/*-------------------------------*/
	/* Modificateurs                 */
	/*-------------------------------*/
	public void viderElements()  { this.lstElement.clear(); }

	public void ajouterElement( Element element ) 
	{
		if ( element != null && !this.lstElement.contains(element) )
		{
			this.lstElement.add( element );

			// System.out.println("Ajout de l'élément : " + element.getTache().getNom() + " - " + element.getCentreX() + " / " + element.getCentreY() );
		}
		else { System.out.println("ERREUR D'AJOUT ELEMENT"); }
	}

	
	/*-------------------------------*/
	/* Création des sommets          */
	/*-------------------------------*/
	public void creerListElement ( boolean nouvellePosition ) 
	{
		this.viderElements();
		
		this.calculerLargeur();

		if (nouvellePosition) this.creerSommetSansPos();
		else                  this.creerSommetAvecPos();
	}

	// SANS POS
	private void creerSommetSansPos() 
	{
		int maxNiveau    = this.ctrl.getListTacheParNiveau().size() - 1;

		// Parcours des niveaux
		for (int niveau = 0; niveau<=maxNiveau; niveau++) 
		{
			List<Tache> lstTachesDuNiveau = this.ctrl.getListTacheParNiveau().get( niveau );
			Tache tache = null;
			int nbTache = lstTachesDuNiveau.size();
			int hauteurPanel = this.frame.getHauteurPanelMPM();

			/* CAS : 
			*  0 -> Début
			 
			*  1 -> Fin
			 
			*  2 -> 1 tâche sur le niveau
			*      -  1 prc & 1 svt
			*      - >1 prc & 1 svt
			*      -  1 prc & >1 svt
			*      - >1 prc & >1 svt
			*      - defaut

			*  3 -> Reste
			*      - Plusieurs tâches sur le niveau
			*/

			// DEBUT
			if ( niveau == 0 )
			{
				CreateurElement.creerSommetDebut( this, this.ctrl, hauteurPanel );
			}

			// FIN
			else if ( niveau == maxNiveau )
			{
				CreateurElement.creerSommetFin( this, this.ctrl, niveau, hauteurPanel );
			}

			// 1 seule tache sur le niveau
			else if ( nbTache == 1 )
			{
				tache = lstTachesDuNiveau.get(0);

				// 1 prc & 1 svt
				if ( tache.getNbPrc() == 1 && tache.getNbSvt() == 1 ) 
				{
					CreateurElement.creerSommetSimple1( this, tache, niveau, hauteurPanel );
				} 

				// >1 prc & 1 svt
				else if ( tache.getNbPrc() > 1 && tache.getNbSvt() == 1 ) 
				{
					CreateurElement.creerSommetSimple2( this, tache, niveau, hauteurPanel );
				}

				// 1 prc & >1 svt
				else if ( tache.getNbPrc() == 1 && tache.getNbSvt() > 1 ) 
				{
					CreateurElement.creerSommetSimple3( this, tache, niveau, hauteurPanel );
				}

				// >1 prc & >1 svt
				else if ( tache.getNbPrc() > 1 && tache.getNbSvt() > 1 ) 
				{
					CreateurElement.creerSommetSimple4( this, tache, niveau, hauteurPanel );
				}
				
				// defaut
				else
				{
					CreateurElement.creerSommetSimple4( this, tache, niveau, hauteurPanel );
				}
			}

			// Plusieurs tâches sur le niveau
			else 
			{
				CreateurElement.creerSommetsMultiples( this, lstTachesDuNiveau, niveau, hauteurPanel );
			}
		}
	}

	// AVEC POS 
	private void creerSommetAvecPos() 
	{
		CreateurElement.creerSommetAvecPos( this, this.ctrl );
	}


	/*-------------------------------*/
	/* Maj                           */
	/*-------------------------------*/
	public void resetSansPos()  { this.creerListElement( true );  }
	public void resetAvecPos()  { this.creerListElement( false ); }


	/*-------------------------------*/
	/* Déplacer                      */
	/*-------------------------------*/
	public void deplacerElement(Element element, int deltaX, int deltaY) 
	{
		if (element instanceof Sommet) 
		{
			Sommet sommet = (Sommet) element;
			
			sommet.deplacerX( deltaX );
			sommet.deplacerY( deltaY );

			sommet.getTache().setPosX( sommet.getCentreX() );
			sommet.getTache().setPosY( sommet.getCentreY() );
		}
	}

}