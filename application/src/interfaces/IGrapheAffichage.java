package src.interfaces;

import src.metier.DateFr;
import src.metier.Tache;

import java.awt.Color;
import javax.swing.JPanel;


public interface IGrapheAffichage
{
	/*--------------------------------*/
	/* Attributs                      */
	/*--------------------------------*/
	public static final Color COULEUR_TACHE_CRTIQUE   = Color.RED;                 //bordure
	public static final Color COULEUR_TACHE           = Color.BLACK;               //bordure
	public static final Color COULEUR_TACHE_FOND      = Color.WHITE;               // fond tache
	public static final Color COULEUR_DATE_TOT        = new Color( 42, 156, 96 );  // dte tot
	public static final Color COULEUR_DATE_TARD       = Color.RED;                 // dte tard

	public static final Color COULEUR_FLECHE_CRITIQUE = Color.RED;                 // fleche
	public static final Color COULEUR_FLECHE          = Color.BLUE;                // 

	public static final Color COULEUR_FOND            = new JPanel().getBackground();

	/*-------------------------------*/
	/* Méthodes                      */
	/*-------------------------------*/
	public char    getDateRef();
	public DateFr  getDateInit();
	
	public boolean getAffichageDate();

	public int     getNbJourMax();

	public boolean afficherCritique();
	public boolean estTacheCritique(Tache t);
}
