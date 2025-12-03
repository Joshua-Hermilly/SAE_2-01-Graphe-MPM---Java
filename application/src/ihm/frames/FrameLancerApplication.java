package src.ihm.frames;

import src.Controleur;
import src.metier.DateFr;
import src.metier.gestionErreur.CodeErreur;
import src.ihm.panels.PanelLancerApplication;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/*--------------------------------- */
/*  Class FrameLancerApplication    */
/*--------------------------------- */
public class FrameLancerApplication extends JFrame
{
	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/
	private PanelLancerApplication panel;

	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/
	public FrameLancerApplication()
	{
		this.setTitle( "Lancer l'application" );
		this.setSize( 400, 300 );
		this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		this.setLocation(400, 300);

		/*---------------------------------*/
		/* Création des composants         */
		/*---------------------------------*/
		this.panel = new PanelLancerApplication( this );

		/*---------------------------------*/
		/* Positionnement des composants   */
		/*---------------------------------*/
		this.add ( this.panel );

		this.setVisible( true );
	}

	/*-------------------------------*/
	/* Méthodes                      */
	/*-------------------------------*/
	public void setControleur( char deteRef, DateFr dateInit, String cheminFichier ) 
	{
		//try 
		//{
			Controleur ctrl = new Controleur( deteRef, dateInit, cheminFichier );
			//System.out.println( "Lancement de l'application avec les paramètres : " + deteRef + ", " + dateInit + ", " + cheminFichier );
		//	this.dispose();
		//} catch (Exception e) { Controleur.gestionErreur( CodeErreur.ERREUR_DEFAUT ); }
	}
}
