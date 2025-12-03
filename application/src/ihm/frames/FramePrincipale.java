package src.ihm.frames;

import src.Controleur;
import src.metier.Tache;

import src.ihm.panels.PanelBtn;
import src.ihm.panels.PanelInfoDetail;
import src.ihm.panels.PanelMPM;
import src.ihm.panels.PanelVueTache;
import src.ihm.menu.BarreMenu;


import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/*---------------------------------*/
/*  Class FramePrincipale          */
/*---------------------------------*/
public class FramePrincipale extends JFrame
{
	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/
	private Controleur ctrl;

	private BarreMenu       barMenu;

	private PanelMPM       panelMPM;
	private JScrollPane     spMPM;
	private PanelVueTache   panelVueTache;

	private PanelBtn        panelBtn;
	private PanelInfoDetail panelDetail;
	
	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/
	public FramePrincipale( Controleur ctrl )
	{
		this.ctrl = ctrl;
		this.setTitle   ( "MPM"     );
		this.setSize(Toolkit.getDefaultToolkit().getScreenSize()); //taille de l'ecran mais grand écran
		this.setLayout( new BorderLayout() );

		/*-------------------------------*/
		/* Création des composants       */
		/*-------------------------------*/
		// menu
		this.barMenu  = new BarreMenu  ( ctrl );

		// nort
		this.panelBtn = new PanelBtn ( ctrl, this );

		// centre
		JPanel panelCentre = new JPanel();
		panelCentre.setLayout( new BorderLayout() );

		this.panelMPM = new PanelMPM ( ctrl, this );
		this.spMPM    = new JScrollPane( this.panelMPM , 
		                                 JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED  ,
		                                 JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED );
		// centre droit
		this.panelVueTache = new PanelVueTache( ctrl, null );

		// sud
		this.panelDetail = new PanelInfoDetail ( ctrl, -1 , ' ' );

		
		// size pour certains
		this.panelVueTache.setPreferredSize( new Dimension( 400, 200 ) );
		this.panelDetail.setPreferredSize  ( new Dimension( this.getWidth(), 200 ) );

		// Bordure des composants
		this.panelMPM     .setBorder( new LineBorder( Color.BLACK, 1 ) );
		this.spMPM        .setBorder( new LineBorder( Color.BLACK, 1 ) );
		this.panelVueTache.setBorder( new LineBorder( Color.BLACK, 1 ) );
		this.panelBtn     .setBorder( new LineBorder( Color.BLACK, 1 ) );
		this.panelDetail  .setBorder( new LineBorder( Color.BLACK, 1 ) );

		/*-------------------------------*/
		/* Positionnement des composants */
		/*-------------------------------*/
		this.setJMenuBar( this.barMenu );

		panelCentre.add( spMPM     , BorderLayout.CENTER );
		panelCentre.add( this.panelVueTache, BorderLayout.EAST   );

		this.add( this.panelBtn   , BorderLayout.NORTH  );
		this.add( panelCentre     , BorderLayout.CENTER );
		this.add( this.panelDetail, BorderLayout.SOUTH  );

		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}


	/*-------------------------------*/
	/* Accesseurs                    */
	/*-------------------------------*/
	public int getHauteurPanelMPM() 
	{ 
		if ( this.panelMPM != null ) return this.panelMPM.getHeight();
		else                         return 600; // pour la 1ere fois
	}

	/*-------------------------------*/
	/* Méthodes                      */
	/*-------------------------------*/
	public void majMPM    ()           { this.panelMPM.maj();   }
	public void resetMPM  (Boolean b)  { this.panelMPM.reset(b);}

	public void afficherVueTache (Tache t ) { this.panelVueTache.setTache( t );}
	public void majVue           ()         { this.panelVueTache.maj();        }

	public void actualiserFichier() 
	{
		this.panelDetail.actualiser();
		this.panelBtn.resetBtn();
	}
	
	public void afficherDetail (int niveau, char type ) 
	{ 
		this.remove(this.panelDetail);
		
		this.panelDetail = new PanelInfoDetail( this.ctrl, niveau, type );
		
		this.panelDetail.setBorder( new LineBorder( Color.BLACK, 1 ) );
		this.panelDetail.setPreferredSize( new Dimension( this.getWidth(), 200 ) );
		
		this.add( this.panelDetail, BorderLayout.SOUTH );
		
		this.revalidate();
		this.repaint();		
	}

}
