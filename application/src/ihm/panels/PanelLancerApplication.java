package src.ihm.panels;

import src.Controleur;
import src.metier.gestionErreur.CodeErreur;

import src.ihm.frames.FrameLancerApplication;
import src.metier.DateFr;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/*---------------------------------*/
/*  Class PanelLancerApplication    */
/*---------------------------------*/
public class PanelLancerApplication extends JPanel implements ActionListener
{
	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/
	private FrameLancerApplication frame;

	private JRadioButton rbDateDebut;
	private JRadioButton rbDateFin;

	private JTextField   txtDate;

	private JTextField   txtCheminFichier;
	private JButton      btnSelcetionner;

	private JButton      btnLancer;

	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/
	public PanelLancerApplication(FrameLancerApplication frame) 
	{
		this.frame = frame;
		this.setLayout(new BorderLayout());
		
		JPanel panelCentre, panelRb, panelBtn, panelFichier, panelTxt;
		/*---------------------------------*/
		/* Création des composants         */
		/*---------------------------------*/
		panelCentre = new JPanel( new GridLayout( 6, 1, 5, 5 ) );

		// ---
		// Texte saisie
		// ---
		panelTxt = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
		this.txtDate = new JTextField( 20 );
		this.txtDate.setText( new DateFr().toString("jj/mm/aaaa") );

		// ---
		// RB
		// ---
		panelRb = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
		ButtonGroup bg   = new ButtonGroup();
		this.rbDateDebut = new JRadioButton( "Date de début" );
		this.rbDateFin   = new JRadioButton( "Date de fin"   );
		bg.add( this.rbDateDebut );
		bg.add( this.rbDateFin );
		this.rbDateDebut.setSelected( true );

		// ---
		// Chemin
		// ---
		panelFichier = new JPanel( new FlowLayout( FlowLayout.LEFT ) );
		this.txtCheminFichier = new JTextField( 20 );
		this.txtCheminFichier.setEditable( false );
		this.btnSelcetionner  = new JButton( "Sélectionner" );
	

		// ---
		// Bouton 
		// ---
		panelBtn = new JPanel( new FlowLayout( FlowLayout.RIGHT ) );
		this.btnLancer = new JButton( "Lancer l'application" );

		/*---------------------------------*/
		/* Positionnement des composants   */
		/*---------------------------------*/

		panelTxt.add( this.txtDate );

		panelRb.add( this.rbDateDebut                     );
		panelRb.add( this.rbDateFin                       );

		panelFichier.add( this.txtCheminFichier );
		panelFichier.add( this.btnSelcetionner  );

		panelCentre.add( new JLabel( "Sélectionner la référence de date : " ) );
		panelCentre.add( panelRb                                              );

		panelCentre.add( new JLabel( "Date (jj/mm/aaaa) : " )                 );
		panelCentre.add( panelTxt                                             );

		panelCentre.add( new JLabel( "Chemin du fichier de données : " )      );
		panelCentre.add( panelFichier                                         );


		panelBtn.add( this.btnLancer );

		this.add( panelCentre, BorderLayout.CENTER );
		this.add( panelBtn   , BorderLayout.SOUTH  );
		
		/*---------------------------------*/
		/* Gestion des événements          */
		/*---------------------------------*/
		this.btnLancer      .addActionListener( this );
		this.btnSelcetionner.addActionListener( this );
	}

	/*-------------------------------*/
	/* Méthodes                      */
	/*-------------------------------*/
	public void actionPerformed(ActionEvent e) 
	{
		if ( e.getSource() == this.btnLancer ) 
		{
			CodeErreur codeErreur = CodeErreur.OK;
			char  deteRef        = ' ';
			DateFr dateInit      = null;
			String cheminFichier = null;

			// DATEREF
			if ( this.rbDateDebut.isSelected() )  deteRef = 'D';
			if ( this.rbDateFin  .isSelected() )  deteRef = 'F';
			
			// CHAMS REMPLIS
			if ( this.txtCheminFichier.getText().isEmpty() || this.txtDate.getText().isEmpty() ) 
			{
				codeErreur = CodeErreur.ERREUR_LANCEMENT_CHAMPS_VIDE;
			}

			// DATE
			try 
			{
				dateInit = new DateFr( this.txtDate.getText() );

			} catch (Exception er) { codeErreur = CodeErreur.ERREUR_LANCEMENT_DATE_INVALIDE; }
			
			// CHEMIN
			cheminFichier = this.txtCheminFichier.getText().trim();
			
			if ( codeErreur != CodeErreur.OK )
			{
				Controleur.gestionErreur( codeErreur );
				this.frame.dispose();
			}
			 // LANCEMENT
			else 
			{
				this.frame.setControleur( deteRef, dateInit, cheminFichier  );
			}
		}

		if ( e.getSource() == this.btnSelcetionner ) 
		{
			JFileChooser fileChooser = new JFileChooser( "../data" );
			fileChooser.setFileSelectionMode( JFileChooser.FILES_ONLY );

			int returnValue = fileChooser.showOpenDialog(this);
			if ( returnValue == JFileChooser.APPROVE_OPTION  ) 
			{
				this.txtCheminFichier.setText(fileChooser.getSelectedFile().getAbsolutePath());
			}
		}

	}

}