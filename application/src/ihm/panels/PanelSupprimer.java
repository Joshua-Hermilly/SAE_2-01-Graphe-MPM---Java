package src.ihm.panels;

import src.Controleur;
import src.metier.Tache;

import javax.swing.*;
import java.awt.*;

import java.util.List;

/*---------------------------------*/
/*  Class PanelGestion              */
/*---------------------------------*/
public class PanelSupprimer extends JPanel
{
	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/
	private Controleur ctrl;
	private Tache      tache;

	private JList<String>  lstTache;

	
	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/
	public PanelSupprimer( Controleur ctrl, Tache tache, List<Tache> lstTachePrc, List<Tache> lstTacheSvt )
	{
		this.ctrl = ctrl;
		this.tache = tache;

		this.setLayout( new BorderLayout() );
		JPanel panelCentre = new JPanel( new GridLayout( 2, 1 ) );
	
		/*-------------------------------*/
		/* Création des composants       */
		/*-------------------------------*/
		//--------
		//Informations 
		//--------
		JPanel panelInfo = new JPanel( new BorderLayout() );
		panelInfo.setBorder(  BorderFactory.createTitledBorder( "Informations - Supressions") );
		
		JLabel lblTitre;
		lblTitre = new JLabel("Attention : Cette tâche sera supprimée et les actions seront irréversibles !", SwingConstants.CENTER);
		lblTitre.setForeground(Color.RED);

		
		JPanel panelInfoTache = new JPanel( new GridLayout(2, 2) );
		JTextField txtNom, txtDuree;
		txtNom   = new JTextField( this.tache.getNom()        );
		txtDuree = new JTextField( this.tache.getDuree() + "" );
		txtNom  .setEditable(false);
		txtDuree.setEditable(false);

		
		//--------
		// Anciennes relations
		//--------
		JPanel panelAnciennesRelations = new JPanel( new GridLayout( 1, 1 ) );
		panelAnciennesRelations.setBorder( BorderFactory.createTitledBorder( "Anciennes relations" ) );
		
		this.lstTache = new JList<String>();
		DefaultListModel<String> modelTache = new DefaultListModel<>();

		String ligne = "";
		for (Tache t : this.ctrl.getListTache())
		{
			ligne = String.format("%-10s", t.getNom()) + " : [ "; //Marche pas vraiment
			for (Tache tSvt : t.getlstSvt())
			{
				ligne += tSvt.getNom() + ", ";
			}

			if ( !t.getlstSvt().isEmpty() )  { ligne = ligne.substring(0, ligne.length() - 2); }//Enleve les ,

			ligne += " ]";
			modelTache.addElement( ligne );
		}
		this.lstTache.setModel( modelTache );
		JScrollPane scrollTache = new JScrollPane( this.lstTache );

		
		/*-------------------------------*/
		/* Positionnement des composants */
		/*-------------------------------*/
		panelInfo.add( lblTitre      , BorderLayout.NORTH  );
		panelInfo.add( panelInfoTache, BorderLayout.SOUTH  );

		panelInfoTache.add( new JLabel("Nom : ", SwingConstants.RIGHT ) );
		panelInfoTache.add( txtNom );
		panelInfoTache.add( new JLabel("Durée : ", SwingConstants.RIGHT) );
		panelInfoTache.add( txtDuree );
		
		panelAnciennesRelations.add( scrollTache );

		panelCentre.add( panelInfo               );
		panelCentre.add( panelAnciennesRelations );

		this.add( panelCentre, BorderLayout.CENTER );

	}
}
