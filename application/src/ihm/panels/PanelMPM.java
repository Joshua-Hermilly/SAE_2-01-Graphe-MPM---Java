package src.ihm.panels;

import src.Controleur;

import src.metier.Tache;

import src.ihm.dessin.*;

import src.ihm.frames.FramePrincipale;
import src.ihm.panels.gestionSouris.GererSourisPanelMPM;

import javax.swing.*;
import java.awt.*;

/*---------------------------------*/
/*  Class PanelMPM                 */
/*---------------------------------*/
public class PanelMPM extends JPanel
{	
	/*-------------------------------*/
	/* Composants                    */
	/*-------------------------------*/
	private Controleur        ctrl;
	private FramePrincipale   frame;
	private MetierDessin     metierDessin;

	// SOUS CLASSE
	private GererSourisPanelMPM gereSouris;

	// DESSIN
	private Graphics2D        g2;

	
	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/	
	public PanelMPM( Controleur ctrl, FramePrincipale frame  )
	{	
		this.ctrl             = ctrl;
		this.frame            = frame;
		this.metierDessin     = new MetierDessin( ctrl, frame );

		this.setLayout( null );

		/*-------------------------------*/
		/* Création des composants       */
		/*-------------------------------*/

		// avec pos ?
		boolean refairePos = true;
		if ( !this.ctrl.getListTache().isEmpty() ) 
		{
			for (Tache t : ctrl.getListTache())
			{
				if (t.getPosX() >= 0 && t.getPosY() >= 0) 
				{
					refairePos = false;
					break;
				}
			}
		}
		this.metierDessin.creerListElement( refairePos );

		/*--------------------------------*/
		/* Positionnement des composants  */
		/*--------------------------------*/

		this.majTaille();

		/*--------------------------------*/
		/* Gestion de la souris           */
		/*--------------------------------*/
		this.gereSouris = new GererSourisPanelMPM( this.ctrl, this, this.metierDessin );
		this.addMouseListener      ( this.gereSouris );
		this.addMouseMotionListener( this.gereSouris );
    }

	/*-------------------------------*/
	/*     PAINT                     */
	/*-------------------------------*/
	protected void paintComponent( Graphics g ) 
	{
		super.paintComponent(g);
		
		this.g2 = (Graphics2D) g;
	
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // plus jolie avec

		// DESSINER LES ELEMENTS
		if ( this.metierDessin.getListElement() != null && ! this.metierDessin.getListElement().isEmpty() ) 
		{
			//FLECHES
			for ( Element elt : this.metierDessin.getListElement() ) 
			{
				if ( elt instanceof Sommet ) 
				{
					Sommet sommet = (Sommet) elt;
							for ( Tache tPrc : sommet.getTache().getlstPrc() )
					{
						Fleche.dessiner( g2, tPrc, sommet.getTache(), this.ctrl.getGraphe() );
					}
				}
			}

			// SOMMET
			for ( Element elt : this.metierDessin.getListElement() ) 
			{
				if ( elt instanceof Sommet ) 
				{
					Sommet sommet = (Sommet) elt;
					sommet.dessiner( g2 );
				}
			}
		}
	}	
	

	/*-------------------------------*/
	/* Maj                           */
	/*-------------------------------*/
	public void reset(  Boolean nouvellePosition ) 
	{
		this.metierDessin.creerListElement( nouvellePosition );	
		this.maj();
	}
	
	public void maj() 
	{
		this.majTaille();
		this.repaint();
	}

	private void majTaille() 
	{
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxX = Integer.MIN_VALUE;
		int maxY = Integer.MIN_VALUE;
		
		for ( Element elt : this.metierDessin.getListElement() ) 
		{
			if ( !( elt instanceof Sommet) ) continue;

			Sommet sommet = (Sommet) elt;
			int sommetMinX = sommet.getCentreX() - sommet.getTailleX() / 2;
			int sommetMinY = sommet.getCentreY() - sommet.getTailleY() / 2;
			int sommetMaxX = sommet.getCentreX() + sommet.getTailleX() / 2;
			int sommetMaxY = sommet.getCentreY() + sommet.getTailleY() / 2;
			
			// maj des dimension
			minX = Math.min( minX, sommetMinX );
			minY = Math.min( minY, sommetMinY );
			maxX = Math.max( maxX, sommetMaxX );
			maxY = Math.max( maxY, sommetMaxY );
		}
		
		// ajout de marge
		int decalageX = Math.max( 0, -minX + 50 );
		int decalageY = Math.max( 0, -minY + 50 );
		
		// decalage si pos negative
		if (decalageX > 0 || decalageY > 0) 
		{
			for ( Element elt : this.metierDessin.getListElement() ) 
			{
				if ( elt instanceof Sommet ) 
				{
					Sommet sommet = (Sommet) elt;
					sommet.setCentreX( sommet.getCentreX() + decalageX );
					sommet.setCentreY( sommet.getCentreY() + decalageY );
					
					sommet.getTache().setPosX( sommet.getCentreX() );
					sommet.getTache().setPosY( sommet.getCentreY() );
				}
			}
			
			
			maxX += decalageX;
			maxY += decalageY;
		}
		
		// maj taille avec marge
		int largeurPanel = Math.max( 800, maxX + 100 ); 
		int hauteurPanel = Math.max( 600, maxY + 100 ); 

		this.setPreferredSize(new Dimension( largeurPanel, hauteurPanel ) );

		this.revalidate(); // pr scorlpane
	}
}
