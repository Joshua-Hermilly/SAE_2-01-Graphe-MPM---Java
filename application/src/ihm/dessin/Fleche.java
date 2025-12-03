package src.ihm.dessin;

import src.interfaces.IGrapheAffichage;
import src.metier.Tache;

import java.awt.*;

/*---------------------------------*/
/*  Class Fleche                   */
/*---------------------------------*/
public abstract class Fleche
{
	/*-------------------------------*/
	/* Dessin                        */
	/*-------------------------------*/		
	public static void dessiner(Graphics g, Tache tachePrc, Tache tacheSvt, IGrapheAffichage iGraphe ) 
	{		
		/*--------*/
		/* DONNES */
		/*--------*/
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(2));

		int largeurSommet = CreateurElement.largeurSommet;

		Point debut   = new Point( tachePrc.getPosX() + largeurSommet / 2, tachePrc.getPosY()  );
		Point arrivee = new Point( tacheSvt.getPosX() - largeurSommet / 2, tacheSvt.getPosY()  );
		Point milieu  = new Point( (debut.x + arrivee.x) / 2, (debut.y + arrivee.y) / 2);

		Color droite =  IGrapheAffichage.COULEUR_FLECHE;
		if ( iGraphe.afficherCritique() )
		{
			boolean pcrCrit = iGraphe.estTacheCritique( tachePrc );
			boolean svtCrit = iGraphe.estTacheCritique( tacheSvt );
			boolean suivie  = tachePrc.getDte_tot() + tachePrc.getDuree() == tacheSvt.getDte_tot();

			if ( pcrCrit && svtCrit && suivie )
			{
				droite = IGrapheAffichage.COULEUR_FLECHE_CRITIQUE;
			}
		}

		Color fond = IGrapheAffichage.COULEUR_FOND;
		//----
		// DROITE
		//----
		g2.setColor ( droite );
		g2.setStroke(new BasicStroke(2));
		g2.drawLine (debut.x, debut.y, arrivee.x, arrivee.y);

		//----
		// DUREE
		//----
		// position du texte
		String dureeText = tachePrc.getDuree() + "";
		FontMetrics fm   = g2.getFontMetrics();
		int largeurTxt   = fm.stringWidth(dureeText);
		int hauteurTxt   = fm.getHeight();

		// fond pour duree
		int rectL  = largeurTxt + 8;
		int rectH = hauteurTxt + 4;
		g2.setColor( fond );
		g2.fillRect( milieu.x - rectL / 2, milieu.y - rectH / 2, rectL, rectH );

		// dessin
		g2.setColor  ( Color.RED );
		g2.drawString( dureeText, milieu.x - largeurTxt / 2, milieu.y + fm.getAscent() / 2 - 2 );

		//----
		// flèche
		//----
		g2.setColor( droite );
		int longFleche = 12;
		int largFleche = 7;
		double angle   = Math.atan2(arrivee.y - debut.y, arrivee.x - debut.x);

		Point pointe = new Point(arrivee.x, arrivee.y);

		Point base1  = new Point( (int)(pointe.x - longFleche * Math.cos(angle) + largFleche * Math.sin(angle) / 2),
			                      (int)(pointe.y - longFleche * Math.sin(angle) - largFleche * Math.cos(angle) / 2) );

		Point base2  = new Point( (int)(pointe.x - longFleche * Math.cos(angle) - largFleche * Math.sin(angle) / 2),
			                      (int)(pointe.y - longFleche * Math.sin(angle) + largFleche * Math.cos(angle) / 2) );

		int[] taxPosX = {pointe.x, base1.x, base2.x};
		int[] taxPosY = {pointe.y, base1.y, base2.y};
		g2.fillPolygon(taxPosX, taxPosY, 3);
	}
}
