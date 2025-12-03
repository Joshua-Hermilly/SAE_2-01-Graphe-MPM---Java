package src.ihm.panels;

import src.Controleur;
import src.metier.Tache;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/*---------------------------------*/
/*  Class PanelSupprimerPrc        */
/*---------------------------------*/
public class PanelSupprimerPrc extends JPanel implements ActionListener
{
	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/
	private Controleur ctrl;
	private Tache      tache;

	private JList<String> lstTachePrc;
	private JScrollPane   scPrc;

	private JButton       btnSupprimerLien;

	public PanelSupprimerPrc ( Controleur ctrl, Tache t )
	{
		this.ctrl  = ctrl;
		this.tache = t;

		this.setLayout( new BorderLayout() );
		this.setBorder( BorderFactory.createTitledBorder( "Supprimer un lien" ) );
		/*-------------------------------*/
		/* Création des composants       */
		/*-------------------------------*/
		///lis
		this.lstTachePrc = new JList<String>();
		this.scPrc               = new JScrollPane();
		if ( this.tache != null )
		{
			this.initList();
		}
		this.scPrc.add ( this.lstTachePrc );

		//btn
		this.btnSupprimerLien = new JButton( "Supprimer lien" );

		/*-------------------------------*/
		/* Positionnement des composants */
		/*-------------------------------*/
		this.add( this.scPrc           , BorderLayout.CENTER );
		this.add( this.btnSupprimerLien, BorderLayout.SOUTH  );

		/* ------------------------------ */
		/* Activation des composants      */
		/* ------------------------------ */
		this.btnSupprimerLien.addActionListener( this );
	}


	/* ------------------------------ */
	/* Méthodes                       */
	/* ------------------------------ */
	public void actionPerformed (ActionEvent e )
	{
		if ( e.getSource() == this.btnSupprimerLien )
		{
			String tPrc = this.lstTachePrc.getSelectedValue();

			this.ctrl.modifierLienTache( tPrc, this.tache.getNom(), '-' );
			this.setTache( this.tache );
		}
	}	
	
	private void initList ( )
	{
		DefaultListModel<String> listModel = new DefaultListModel<String>();
	
		for ( Tache t :  this.tache.getlstPrc() )
		{
			listModel.addElement ( t.getNom() );
		}
		this.lstTachePrc.setModel( listModel );
		this.scPrc.setViewportView( this.lstTachePrc );
	}

	public void setTache( Tache t )
	{
		this.tache = t;

		this.initList();
		this.revalidate();
	}
	
}
