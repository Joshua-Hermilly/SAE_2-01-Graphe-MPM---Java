package src.ihm.panels;

import src.Controleur;
import src.metier.Tache;

import javax.swing.*;
import java.awt.*;

/*----------------------------------*/
/*  Class PanelInfoDetail           */
/*----------------------------------*/
public class PanelInfoDetail extends JPanel
{
	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/
	private Controleur  ctrl;

	private PanelInfo panelInfo;

	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/
	public PanelInfoDetail( Controleur ctrl, int niveau, char type )
	{
		this.ctrl = ctrl;

		this.setLayout( new GridLayout( 1, 2, 5, 5 ) );
		/*-------------------------------*/
		/* Création des composants       */
		/*-------------------------------*/
		// ---
		// Panel info
		// ---
		this.panelInfo = new PanelInfo( this.ctrl );

		// ---
		// Détails 
		// ----
		// menu
		JTabbedPane tpListPanel  = new JTabbedPane();
		tpListPanel.setBorder( BorderFactory.createTitledBorder( "Détails" ) );

		// 1ere page
		JPanel panelDetail = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
		panelDetail.setBorder( BorderFactory.createTitledBorder( "Informations du calcul de dates" ) );

		JLabel lblInfoCalculTot  = new JLabel( "Date au plus tôt: début + durée des prédécesseurs." );
		lblInfoCalculTot.setForeground  ( src.interfaces.IGrapheAffichage.COULEUR_DATE_TOT );
		JLabel lblInfoCalculTard = new JLabel( "Date au plus tard: fin - durée des successeurs" );
		lblInfoCalculTard.setForeground ( src.interfaces.IGrapheAffichage.COULEUR_DATE_TARD );

		JPanel panelTmp;
		// création des panel detail
		tpListPanel.addTab( "Informations", panelDetail );
		if ( niveau != -1 && type != ' ' )
		{
			// uniquemement detail tache
			JTabbedPane tpTaches = new JTabbedPane();
			
			for ( Tache t : this.ctrl.getListTacheParNiveau().get( niveau ) )
			{
				panelTmp = new PanelDetail( this.ctrl, t, type );
				tpTaches.addTab( t.getNom(), panelTmp );
			}

			tpTaches.setSelectedIndex(0);
			
			// Ajout du JTabbedPane des tâches dans le JTabbedPane principal
			tpListPanel.addTab( "Tâches", tpTaches );
		}
				
		/*-------------------------------*/
		/* Positionnement des composants */
		/*-------------------------------*/
		panelDetail.add( lblInfoCalculTot  );
		panelDetail.add( lblInfoCalculTard );

		this.add( this.panelInfo  );
		this.add( tpListPanel     );
	}

	/*-------------------------------*/
	/* Méthodes                      */
	/*-------------------------------*/
	public void actualiser() 
	{
		this.panelInfo.actualiser();
	}
}
