package src.ihm.panels.gestionSouris;

import src.Controleur;

import src.ihm.dessin.Element;
import src.ihm.dessin.MetierDessin;
import src.ihm.dessin.Sommet;

import src.ihm.panels.PanelMPM;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


/*-------------------------------*/
/* Gestion de la souris          */
/*-------------------------------*/
public class GererSourisPanelMPM extends MouseAdapter 
{
	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/
	private Controleur       ctrl;
	private PanelMPM        panelMPM;
	private MetierDessin    metierDessin;

	private Sommet   sommetSelectionne;
	private int      posXSouris;
	private int      posYSouris;

	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/
	public GererSourisPanelMPM ( Controleur ctrl, PanelMPM panelMPM, MetierDessin metierDessin )

	{
		this.ctrl            = ctrl;
		this.panelMPM        = panelMPM;
		this.metierDessin    = metierDessin;

		this.sommetSelectionne = null;
		this.posXSouris        = 0;
		this.posYSouris        = 0;
	}

	// PLUS DE SOMMET EN MAIN
	public void mouseReleased( MouseEvent e )	{ this.sommetSelectionne = null;}

	//VUE TACHE
	public void mouseClicked( MouseEvent e ) 
	{
		for (Element elt : this.metierDessin.getListElement() )
		{
			if ( elt.possede(e.getX(), e.getY()) ) 
			{
				this.ctrl.afficherVueTache( elt.getTache() );
				break;
			}
		}
	}
	
	// POSSEDE
	public void mousePressed( MouseEvent e ) 
	{
		for ( Element elt : this.metierDessin.getListElement( ) )
		{
			if ( !(elt instanceof Sommet) ) continue;

			Sommet sommet = (Sommet) elt;
			if ( elt.possede(e.getX(), e.getY()) ) 
			{
				sommetSelectionne = sommet;
				posXSouris = e.getX();
				posYSouris = e.getY();
				break;
			}
		}
	}

	// DEPLACER
	public void mouseDragged(MouseEvent e) 
	{
		if ( sommetSelectionne != null ) 
		{
			int deplacementX = e.getX() - posXSouris;
			int deplacementY = e.getY() - posYSouris;

			this.metierDessin.deplacerElement( sommetSelectionne, deplacementX, deplacementY );
			
			// maj des pos
			posXSouris = e.getX();
			posYSouris = e.getY();

			this.panelMPM.maj();
		}
	}
}