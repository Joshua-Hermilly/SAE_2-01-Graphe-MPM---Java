package src.ihm.panels;

import src.Controleur;
import src.metier.Tache;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.util.List;
import java.util.ArrayList;

/*---------------------------------*/
/*  Class PanelVueTache            */
/*---------------------------------*/
public class PanelVueTache extends JPanel implements ActionListener
{
	/*-------------------------------*/
	/* Composants                    */
	/*-------------------------------*/
	private Controleur ctrl;
	private Tache      tache;

	private JTextField     txtNom;
	private JTextField     txtDuree;
	private JList<String>  lstPrc;
	private JScrollPane    scrollPrc;
	
	private PanelAjouterPrc   panelAjouterPrc;
	private PanelSupprimerPrc panelSupprimerPrc;

	private JButton        btnSupprimer;
	private JButton        btnModifierDate;

	private JTabbedPane    tabbedPane;

	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/
	public PanelVueTache( Controleur ctrl, Tache tache )
	{
		this.ctrl  = ctrl;
		this.tache = tache;
		this.setLayout(  new BorderLayout() );

		JPanel panelSaisie, panelPrc, panelBtn, panelGestionPrc;

		/*-------------------------------*/
		/* Création des composants       */
		/*-------------------------------*/
		if ( this.tache == null ) 
		{
			this.tache = this.ctrl.getTache( "DÉBUT" );
		}

		// ----
		// Nom & durée
		// ----
		panelSaisie = new JPanel( new GridLayout( 2, 2 ) );
		panelSaisie.setBorder( BorderFactory.createTitledBorder( "Informations - Modification" ) );
		panelSaisie.setPreferredSize( new Dimension( 400, 100 ) );

		this.txtNom = new JTextField( this.tache.getNom() );
		this.txtNom.setEditable(false);
		
		this.txtDuree = new JTextField( this.tache.getDuree());

		// ----
		// prc
		// ----
		panelPrc = new JPanel( new BorderLayout() );
		this.lstPrc = new JList<String>( );
		this.lstPrc.setBorder( BorderFactory.createTitledBorder( "Précédents actuelles " ) );
		this.initLstPrc();
		this.scrollPrc = new JScrollPane( this.lstPrc );
		
		// ----
		// Ajout Prc
		// ----
		this.panelAjouterPrc = new PanelAjouterPrc( this.ctrl, this.tache );

		// ----
		// Supprime Prc
		// ----
		this.panelSupprimerPrc = new PanelSupprimerPrc( this.ctrl, this.tache );
		
		// ----
		// Panel gestion des précédents
		// ----
		panelGestionPrc = new JPanel( new GridLayout( 2, 1, 5, 5 ) );
		panelGestionPrc.add( this.panelAjouterPrc );
		panelGestionPrc.add( this.panelSupprimerPrc );
		
		// ----
		// TabbedPane
		// ----
		this.tabbedPane = new JTabbedPane();
		this.tabbedPane.addTab("Visualisation", panelPrc);
		this.tabbedPane.addTab("Gestion des précédents", panelGestionPrc);
		
		// ----
		// Boutons
		// ----
		panelBtn = new JPanel( );
		this.btnSupprimer = new JButton( "Supprimer la tâche" );
		this.btnModifierDate = new JButton( "Modifier la durée" );

		if ( this.tache.getNom().equals("DÉBUT") || this.tache.getNom().equals("FIN") )
		{
			this.btnSupprimer.setEnabled( false );
			this.btnModifierDate.setEnabled( false );
		}

		/*-------------------------------*/
		/* Positionnement des composants */
		/*-------------------------------*/
		panelSaisie.add( new JLabel( "Nom : "  , SwingConstants.RIGHT ) );
		panelSaisie.add( txtNom );
		panelSaisie.add( new JLabel( "Durée : ", SwingConstants.RIGHT ) );
		panelSaisie.add( this.txtDuree );

		panelPrc.add( scrollPrc, BorderLayout.CENTER );

		panelBtn.add( this.btnSupprimer );
		panelBtn.add( this.btnModifierDate );

		this.add( panelSaisie, BorderLayout.NORTH );
		this.add( this.tabbedPane, BorderLayout.CENTER );
		this.add( panelBtn, BorderLayout.SOUTH );

		/*-------------------------------*/
		/* Actions                       */
		/*-------------------------------*/
		this.btnSupprimer.addActionListener( this );
		this.btnModifierDate.addActionListener( this );
	}

	/*-------------------------------*/
	/* Méthodes                      */
	/*-------------------------------*/
	public void actionPerformed(ActionEvent e) 
	{
		if ( e.getSource() == this.btnSupprimer ) { this.ctrl.supprimerTache( this.tache ); }

		if ( e.getSource() == this.btnModifierDate )
		{
			try 
			{
				// durée
				int duree = Integer.parseInt( this.txtDuree.getText() ); 
				if ( duree < 0 ) 
				{
					System.out.println( "La durée doit être >0");
					return;
				}

				this.ctrl.modifierTache( this.tache,  duree );
				
			} catch ( NumberFormatException ex ) { System.out.println( "Durée doit être nombre entier"); }
		}
	}

	public void ajouterPrc( JButton btn )
	{
		this.ctrl.modifierLienTache( btn.getText(), this.tache.getNom(), '+');
		this.setTache(this.tache);
	}

	/*-------------------------*/
	/* Maj                     */
	/*-------------------------*/
	public void setTache( Tache tache )
	{
		if ( tache == null ) return;

		this.tache = tache;
		
		this.maj();
	}

	public void maj()
	{
		this.txtNom   .setText ( this.tache.getNom()       );
		this.txtDuree .setText ( this.tache.getDuree() +"" );

		boolean estExtreme = this.tache.getNom().equals( "DÉBUT" ) || this.tache.getNom().equals( "FIN" );

		this.btnModifierDate .setEnabled( !estExtreme );
		this.btnSupprimer.setEnabled( !estExtreme );

		this.initLstPrc();
		this.panelAjouterPrc  .setTache( this.tache );
		this.panelSupprimerPrc.setTache( this.tache );
	}


	/*--------------------------*/
	/* Intialisation            */
	/*--------------------------*/
	public void initLstPrc()
	{
		int nbNivea = this.tache.getNiveau();
				
		List<String> lstPrcTemp = new ArrayList<String>( );
		DefaultListModel<String> modelPrc = new DefaultListModel<String>( );
		for  ( int cptN=0; cptN<nbNivea; cptN++ )
		{
			for ( Tache tPrc : this.ctrl.getListTacheParNiveau().get(cptN) )
			{
				if ( ! lstPrcTemp.contains( tPrc.getNom() ) ) 
				{
					modelPrc.addElement( tPrc.getNom() );
					lstPrcTemp.add( tPrc.getNom() );
				}
			}
		}
		this.lstPrc.setModel( modelPrc );
	}
}
