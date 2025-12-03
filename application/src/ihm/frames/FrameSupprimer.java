package src.ihm.frames;

import src.Controleur;
import src.metier.Tache;

import src.ihm.panels.PanelSupprimer;

import javax.swing.*;

import java.awt.BorderLayout;
import java.awt.event.*;
import java.awt.Color;

import java.util.List;


/*---------------------------------*/
/*  Class FrameGestion             */
/*---------------------------------*/
public class FrameSupprimer extends JFrame implements ActionListener
{	
	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/
	private Controleur      ctrl;
	private Tache           tache;
	private List<Tache>     lstPrc;
	private List<Tache>     lstSvt;

	private PanelSupprimer panelGestion;
	private JButton         btnValider;

	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/	
	public FrameSupprimer( Controleur ctrl, Tache tache, List<Tache> lstPrc, List<Tache> lstSvt  )
	{
		this.ctrl   = ctrl;
		this.tache  = tache;

		this.setTitle("Suppression de la tâche : " + tache.getNom());
		this.setLocation(400, 300);

		/*-------------------------------*/
		/* Création des composants       */
		/*-------------------------------*/
		this.panelGestion = new PanelSupprimer( this.ctrl, this.tache, lstPrc, lstSvt );
		this.btnValider   = new JButton( "Valider" );

		this.btnValider.setBackground( Color.RED   );
		this.btnValider.setForeground( Color.WHITE );

		
		/*-------------------------------*/
		/* Positionnement des composants */
		/*-------------------------------*/
		this.add( this.panelGestion, BorderLayout.CENTER );
		this.add( this.btnValider  , BorderLayout.NORTH  );

		/*-------------------------------*/
		/* Gestion des événements        */
		/*-------------------------------*/
		this.btnValider.addActionListener( this );

		this.pack();
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
	
	/*-------------------------------*/
	/* Méthodes                      */
	/*-------------------------------*/	
	public void actionPerformed( ActionEvent e )
	{
		if (e.getSource() == this.btnValider)
		{
			this.ctrl.validerTache( this.tache );

			this.dispose();
		}
	}	
}
