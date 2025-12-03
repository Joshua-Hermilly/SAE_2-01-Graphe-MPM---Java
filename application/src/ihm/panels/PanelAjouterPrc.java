package src.ihm.panels;

import src.Controleur;
import src.metier.Tache;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/*---------------------------------*/
/*  Class PanelAjouterPrc          */
/*---------------------------------*/
public class PanelAjouterPrc extends JPanel implements ActionListener
{
	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/
	private Controleur ctrl;
	private Tache      tache;

	private JList<String> lstTachePrcPossible;
	private JScrollPane   scPrc;

	private JButton       btnAjouterLien;

	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/
	public PanelAjouterPrc( Controleur ctrl, Tache t )
	{
		this.ctrl  = ctrl;
		this.tache = t;

		this.setLayout( new BorderLayout() );
		this.setBorder( BorderFactory.createTitledBorder( "Ajouter un lien" ) );
		/*-------------------------------*/
		/* Création des composants       */
		/*-------------------------------*/
		///lis
		this.lstTachePrcPossible = new JList<String>();
		this.scPrc               = new JScrollPane();
		if ( this.tache != null )
		{
			this.initList();
		}
		this.scPrc.add ( this.lstTachePrcPossible );

		//btn
		this.btnAjouterLien = new JButton( "Ajouter lien" );

		/*-------------------------------*/
		/* Positionnement des composants */
		/*-------------------------------*/
		this.add( this.scPrc         , BorderLayout.CENTER );
		this.add( this.btnAjouterLien, BorderLayout.SOUTH  );

		/* ------------------------------ */
		/* Activation des composants      */
		/* ------------------------------ */
		this.btnAjouterLien.addActionListener( this );
	}


	/* ------------------------------ */
	/* Méthodes                       */
	/* ------------------------------ */
	public void actionPerformed (ActionEvent e )
	{
		if ( e.getSource() == this.btnAjouterLien )
		{
			String tPrc = this.lstTachePrcPossible.getSelectedValue();

			this.ctrl.modifierLienTache( tPrc, this.tache.getNom(), '+' );
			this.setTache( this.tache );
		}
	}
	
	
	
	private void initList ( )
	{
		DefaultListModel<String> listModel = new DefaultListModel<String>();
	
		for ( Tache t : this.ctrl.getTachesPossible( this.tache ) )
		{
			listModel.addElement ( t.getNom() );
		}
		this.lstTachePrcPossible.setModel( listModel );
		this.scPrc.setViewportView( this.lstTachePrcPossible );
	}

	public void setTache( Tache t )
	{
		this.tache = t;

		this.initList();
		this.revalidate();
	}
}
