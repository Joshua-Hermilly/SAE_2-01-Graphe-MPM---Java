package src.ihm.panels;

import src.Controleur;
import src.ihm.frames.FrameAjouter;
import src.metier.Tache;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.*;

import javax.swing.*;
import java.awt.Dimension;
import java.awt.Color;

public class PanelAjouter  extends JPanel implements ActionListener
{
	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/
	private Controleur   ctrl;
	private FrameAjouter frame;
	private Tache        tache;

	private JTextField    txtNom;
	private JTextField    txtDuree;
	private JButton       btnCreer;
	private JButton       btnAnnuler;

	private PanelAjouterPrc panelAjouterPrc;
	
	
	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/
	public PanelAjouter( Controleur ctrl, FrameAjouter frame )
	{
		this.ctrl  = ctrl;
		this.frame = frame;
		this.tache = null;

		this.setLayout( new GridLayout( 1, 1, 5, 5 ) );
		JPanel panelCentre = new JPanel( new BorderLayout() );
		/*-------------------------------*/
		/* Création des composants       */
		/*-------------------------------*/
		// ---
		// Création
		// ---
		JPanel panelInfo = new JPanel( new GridLayout( 2, 2, 5, 5 ) );
		panelInfo.setBorder( BorderFactory.createTitledBorder( "Informations de la tâche" ) );
		panelInfo.setPreferredSize( new Dimension( 400, 100 ) );

		JLabel lblNom   = new JLabel( "Nom : " );
		JLabel lblDuree = new JLabel( "Durée : " );
		
		this.txtNom   = new JTextField();
		this.txtDuree = new JTextField();

		JPanel panelBtn = new JPanel( new GridLayout( 1, 2, 5, 5 ) );
		this.btnCreer   = new JButton( "Créer tache" );
		this.btnAnnuler = new JButton( "Annuler" );
		
		this.btnCreer.setBackground( Color.GREEN );
		this.btnCreer.setForeground( Color.WHITE );

		panelInfo.setOpaque( false);
		panelBtn.setOpaque( false );
		
		// ---
		// Ajout prc
		// ---
		this.panelAjouterPrc = new PanelAjouterPrc( ctrl, tache );


		/*-------------------------------*/
		/* Positionnement des composants */
		/*-------------------------------*/
		panelInfo.add( lblNom         );
		panelInfo.add( this.txtNom    );
		panelInfo.add( lblDuree       );
		panelInfo.add( this.txtDuree  );

		panelBtn.add( this.btnAnnuler );
		panelBtn.add( this.btnCreer   );

		panelCentre.add( panelInfo, BorderLayout.CENTER );
		panelCentre.add( panelBtn , BorderLayout.SOUTH  );

		this.add( panelCentre          );
		this.add( this.panelAjouterPrc );


		/*-------------------------------*/
		/* Gestion des événements        */
		/*-------------------------------*/
		this.btnCreer  .addActionListener( this );
		this.btnAnnuler.addActionListener( this );
	}

	public void actionPerformed( ActionEvent e )
	{
		if ( e.getSource() == this.btnCreer )
		{
			String nom      = this.txtNom.getText()  .trim();
			String dureeStr = this.txtDuree.getText().trim();

			if ( nom.isEmpty() || dureeStr.isEmpty() )
			{
				this.messageErreur( "Veuillez remplir tous les champs." );
				return;
			}

			try
			{
				int duree = Integer.parseInt( dureeStr );
				if ( duree <= 0 )
				{
					this.messageErreur( "La durée doit être un nombre positif." );
					return;
				}

				this.tache = new Tache( nom, duree );
				if ( !this.ctrl.ajouterTache( this.tache ) )
				{
					this.messageErreur( "Une tâche avec ce nom existe déjà." );
					return;
				}

				this.passerAuxLiens(); 	
			}
			catch ( NumberFormatException ex ) { this.messageErreur( "La durée doit être un nombre entier." ); }
		}

		if ( e.getSource() == this.btnAnnuler )
		{
			this.ctrl.validerTache( this.tache );
			this.frame.dispose();
		}
	}

	public void passerAuxLiens( )
	{
		this.txtNom    .setEditable( false );
		this.txtDuree  .setEditable( false );
		this.btnCreer .setEnabled ( false );

		this.setBackground( Color.LIGHT_GRAY );

		this.panelAjouterPrc.setTache( this.tache );
	}


	public void messageErreur( String message )
	{
		JOptionPane.showMessageDialog( this, message, "Erreur", JOptionPane.ERROR_MESSAGE );
	}

}
