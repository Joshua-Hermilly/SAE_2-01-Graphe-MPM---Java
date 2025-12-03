package src.ihm.menu;

import src.Controleur;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;

/*---------------------------------*/
/*  Class BarreMenu                */
/*---------------------------------*/
public class BarreMenu extends JMenuBar implements ActionListener
{
	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/
	private Controleur ctrl;

	private JMenuItem menuiOuvrir;
	private JMenuItem menuiSauvegarder;
	private JMenuItem menuiQuitter;

	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/
	public BarreMenu( Controleur ctrl )
	{
		this.ctrl = ctrl;
		
		/*----------------------------*/
		/* Création des composants    */
		/*----------------------------*/
		// menBar
		JMenuBar menuBar = new JMenuBar();
		
		// menu
		JMenu menuFichier   = new JMenu( "Fichier" );

		// item
		this.menuiOuvrir      = new JMenuItem( "Ouvrir"      );
		this.menuiSauvegarder = new JMenuItem( "Sauvegarder" );
		this.menuiQuitter     = new JMenuItem( "Quitter"     );
		
		// raccourcis
		this.menuiOuvrir      .setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_O, ActionEvent.CTRL_MASK ) );
		this.menuiSauvegarder .setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_S, ActionEvent.CTRL_MASK ) );
		this.menuiQuitter     .setAccelerator( KeyStroke.getKeyStroke( KeyEvent.VK_Q, ActionEvent.CTRL_MASK ) );

		/*-------------------------------*/
		/* positionnement des composants */
		/*-------------------------------*/
		menuFichier.add( this.menuiOuvrir      );
		menuFichier.add( this.menuiSauvegarder );
		menuFichier.add( this.menuiQuitter     );

		menuBar.add( menuFichier );

		this.add( menuBar );

		/*-------------------------------*/
		/* Activation des composants     */
		/*-------------------------------*/
		this.menuiOuvrir      .addActionListener ( this );
		this.menuiSauvegarder .addActionListener ( this );
		this.menuiQuitter     .addActionListener ( this );

	}

	public void actionPerformed ( ActionEvent e )
	{
		if ( e.getSource() == this.menuiOuvrir      )
		{
			JFileChooser fileChooser = new JFileChooser( "./../data/" );
			if ( fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION )
				this.ctrl.charger ( fileChooser.getSelectedFile().getAbsolutePath() );
		}

		if ( e.getSource() == this.menuiSauvegarder )
		{
			JOptionPane.showMessageDialog( this, 
			                               "La sauvegarde se fait dans le dossier data/save"        +
			                               "\n .mpm pour ne pas sauvegarder les positions et dates" +
			                               "\n .mpm2 pour sauvegarder les positions et dates"       , 
			                               "Information",
			                               JOptionPane.INFORMATION_MESSAGE );

			JFileChooser fileChooser = new JFileChooser( "./../data/save/" );
			fileChooser.setDialogTitle ( "Sauvegarder le projet MPM"   );
			fileChooser.setSelectedFile( new File( "Sauvegarde.mpm2" ) );
			
			
			if ( fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION ) 
			{
				String cheminFichier = fileChooser.getSelectedFile().getAbsolutePath();
				
				// Bonne extension ?
				if ( !cheminFichier.endsWith(".mpm2") && !cheminFichier.endsWith(".mpm") )
				{
					System.out.println( cheminFichier);
					this.messageErreur("Le fichier doit avoir l'extension .mpm2 ou .mpm");
					return;
				}
				
				// Récupere le nom du fichier
				String nomFichier = fileChooser.getSelectedFile().getName();

				if ( ! this.ctrl.sauvegarder( nomFichier ) )
				{
					this.messageErreur( "Erreur lors de la sauvegarde. \nLe fichier existe peut-être déjà." );
				}
			}
		}
		
		if ( e.getSource() == this.menuiQuitter) { System.exit(0); }
	}

	private void messageErreur( String message )
	{
		JOptionPane.showMessageDialog( this, message, "Erreur", JOptionPane.ERROR_MESSAGE );
	}
}