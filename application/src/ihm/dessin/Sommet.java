package src.ihm.dessin;

import src.metier.Tache;
import src.interfaces.IGrapheAffichage;

import java.awt.*;

/*------------------------*/
/* Class Sommet           */
/*------------------------*/
public class Sommet extends Element
{
	/*---------------*/
	/* Attributs     */
	/*---------------*/
	private IGrapheAffichage iGraphe;
	private Tache            tache;

	/*---------------*/
	/* Constructeur  */
	/*---------------*/
	public Sommet ( IGrapheAffichage iGraphe, Tache t, int centreX, int centreY, int tailleX, int tailleY )
	{
		super(centreX, centreY, tailleX, tailleY);

		this.iGraphe = iGraphe;
		this.tache  = t;
	}

	/*---------------*/
	/* Accesseurs    */
	/*---------------*/
	public Tache getTache() { return this.tache; }

	/*---------------*/
	/* Modificateurs */
	/*---------------*/
	public void deplacerX (int x) 
	{ 
		super.deplacerX(x);
		this.tache.setPosX(this.getCentreX());
		
	}
	
	public void deplacerY (int y) 
	{ 
		super.deplacerY(y);
		this.tache.setPosY(this.getCentreY());
		
	}

	/*-----------------*/
	/* Autres méthodes */
	/*-----------------*/
	public boolean possede ( int x, int y )
	{
		int x1 = this.getCentreX() - this.getTailleX() /2;
		int x2 = this.getCentreX() + this.getTailleX() /2;
		int y1 = this.getCentreY() - this.getTailleY() /2;
		int y2 = this.getCentreY() + this.getTailleY() /2;
		
		return ( ( x1<= x && x<=x2 ) && (y1<= y && y<=y2 ) ); 
	}


	/*-----------------*/
	/* Dessiner        */
	/*-----------------*/
	public void dessiner(Graphics g)
	{
		/*--------*/
		/* DONNES */
		/*--------*/
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(2));
		
		int posX    = this.getCentreX() - this.getTailleX() / 2;
		int posY    = this.getCentreY() - this.getTailleY() / 2;
		int largeur = this.getTailleX();
		int hauteur = this.getTailleY();

		Color bordure =  IGrapheAffichage.COULEUR_TACHE;
		if ( this.iGraphe.afficherCritique() && this.iGraphe.estTacheCritique( tache ) )
		{
			bordure =  IGrapheAffichage.COULEUR_TACHE_CRTIQUE;
		}

		Color fond    = IGrapheAffichage.COULEUR_TACHE_FOND;
		Color tot     = IGrapheAffichage.COULEUR_DATE_TOT;
		Color tard    = IGrapheAffichage.COULEUR_DATE_TARD;


		// ----
		// RECTANGLE
		// ----
		// Fond
		g2.setColor( fond );
		g2.fillRect(posX, posY, largeur, hauteur);

		// Contour
		g2.setColor( bordure );
		g2.drawRect(posX, posY, largeur, hauteur);

		// Ligne horizontale
		int hauteurLigneH = posY + hauteur/2;
		g2.drawLine(posX, hauteurLigneH, posX + largeur, hauteurLigneH);

		// Ligne verticale
		int posXLigneV = posX + largeur/2;
		g2.drawLine(posXLigneV, hauteurLigneH, posXLigneV, posY + hauteur);

		// ----
		// TEXTE
		// ----
		// Nom
		String nom     = this.tache.getNom();
		int largeurNom = g2.getFontMetrics().stringWidth(nom);
		int posXNom    = posX + (largeur - largeurNom) / 2;
		int posYNom    = posY + hauteur/4 + g2.getFontMetrics().getAscent()/2;
		g2.drawString(nom, posXNom, posYNom);

		// --- 
		// Duree
		// ---
		// posY des dates
		int posYDates = hauteurLigneH + (hauteur/2 + g2.getFontMetrics().getAscent())/2;

		
		// plus tôt
		g2.setColor( tot );
		if ( this.tache.getCalculee()[0] || this.tache.getNom().equals("DÉBUT") )
		{
			String dateTot = "";
			if ( this.iGraphe.getAffichageDate() )
			{
				dateTot = this.tache.afficherDateTot( this.iGraphe.getDateRef(), 
				                                      this.iGraphe.getDateInit(), 
				                                      this.iGraphe.getNbJourMax() );
			}
			else { dateTot = this.tache.getDte_tot() + ""; }

			// dessin
			int largeurDateTot = g2.getFontMetrics().stringWidth(dateTot);
			g2.drawString(dateTot, posX + (largeur/4) - largeurDateTot/2, posYDates);

		}

		// plus tard
		g2.setColor( tard );
		if (this.tache.getCalculee()[1])
		{
			String dateTard = "";
			if ( this.iGraphe.getAffichageDate() )
			{
				dateTard = this.tache.afficherDateTard( this.iGraphe.getDateRef(), 
				                                        this.iGraphe.getDateInit(), 
				                                        this.iGraphe.getNbJourMax() );
			}
			else { dateTard = this.tache.getDte_tard() + ""; }

			// dessin
			int largeurDateTard = g2.getFontMetrics().stringWidth(dateTard);
			g2.drawString(dateTard, posX + (3*largeur/4) - largeurDateTard/2, posYDates);
		}
	}
}