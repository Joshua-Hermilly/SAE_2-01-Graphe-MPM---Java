package src.ihm.panels;

import src.Controleur;
//import src.metier.CheminCritique;
//import src.metier.Tache;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/*----------------------------------*/
/*  Class PanelInfo                 */
/*----------------------------------*/
public class PanelInfo extends JPanel implements ActionListener
{
	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/
	private Controleur ctrl;
	
	private JRadioButton rbDebut;
	private JRadioButton rbFin;
	private JTextField tfDate;
	private JButton btnModifier;

	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/
	public PanelInfo( Controleur ctrl )
	{
		this.ctrl = ctrl;

		this.setLayout(new GridLayout(1, 2, 5, 5));
		//this.setBorder(BorderFactory.createTitledBorder("Informations du projet")); // affiche en double au 2 reset
		/*-------------------------------*/
		/* Création des composants       */
		/*-------------------------------*/		
		JPanel panelDroit = new JPanel( new GridLayout( 6, 1, 5, 5 ) );
		
		// Nom du fichier
		String nomFichier = this.ctrl.getCheminFichier();
		if (nomFichier != null && nomFichier.contains("/")) 
		{
			nomFichier = nomFichier.substring(nomFichier.lastIndexOf("/") + 1);
		}
		JLabel lblNomFichier = new JLabel("Nom du fichier: " + nomFichier);

		// Panel pour changer le flag et la date
		JPanel panelDateFlag = new JPanel(new FlowLayout(FlowLayout.LEFT));
		this.rbDebut = new JRadioButton("Date de début", this.ctrl.getDateRef() == 'D');
		this.rbFin   = new JRadioButton("Date de fin"  , this.ctrl.getDateRef() == 'F');
		ButtonGroup bgDateType = new ButtonGroup();

		// Champ de texte pour la date
		this.tfDate      = new JTextField( this.ctrl.getDateInit().toString("jj/mm/aaaa"), 10) ;
		this.btnModifier = new JButton("Modifier");
		

		// Date de projet
		String dateProjet = "";
		if ( this.ctrl.getDateRef() == 'D') dateProjet = "Date de début : " + this.ctrl.getDateInit().toString( "jj/mm/aaaa" );
		else                                dateProjet = "Date de fin : "   + this.ctrl.getDateInit().toString( "jj/mm/aaaa" );
		JLabel lblDateProjet = new JLabel( dateProjet );

		// Durée totale
		JLabel lblDureeTotale = new JLabel("Durée totale: " + this.ctrl.getNbJour() + " jours");
		
		// nbTaches
		JLabel lblNbTaches = new JLabel("Nombre de tâches: " + this.ctrl.getNbTaches() );
		

		/*-------------------------------*/
		/* Ajout des composants         */
		/*-------------------------------*/
		
		bgDateType.add(this.rbDebut);
		bgDateType.add(this.rbFin);

		panelDateFlag.add(this.rbDebut);
		panelDateFlag.add(this.rbFin);
		panelDateFlag.add(this.tfDate);
		panelDateFlag.add(this.btnModifier);

		
		panelDroit.add( lblNomFichier  );
		panelDroit.add( lblDateProjet  );
		panelDroit.add( panelDateFlag  );
		panelDroit.add( lblDureeTotale );
		panelDroit.add( lblNbTaches    );

		this.add ( panelDroit );

		/*-------------------------------*/
		/* Gestion des événements        */
		/*-------------------------------*/
		this.btnModifier.addActionListener(this);
	}

	/*-------------------------------*/
	/* Méthodes                      */
	/*-------------------------------*/
	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == this.btnModifier)
		{
			String dateStr = this.tfDate.getText().trim();
			char   dateRef = 'D';
			if ( this.rbFin.isSelected() ) dateRef = 'F';

			this.ctrl.changerDate(dateRef, dateStr);	
		}
	}
	
	public void actualiser()
	{
		this.removeAll();

		this.add( new PanelInfo( this.ctrl ) );
		this.revalidate();
	}
}