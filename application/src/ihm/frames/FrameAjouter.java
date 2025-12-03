package src.ihm.frames;

import src.Controleur;

import src.ihm.panels.PanelAjouter;

import javax.swing.*;
import java.awt.event.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

/*---------------------------------*/
/*  Class FrameAjouter             */
/*---------------------------------*/
public class FrameAjouter extends JFrame implements ActionListener
{
	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/
	private Controleur ctrl;

	private PanelAjouter      panelAjouter;
	private JButton           btnSoumettre;

	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/
	public FrameAjouter( Controleur ctrl )
	{
		this.ctrl = ctrl;

		this.setTitle("Ajout d'une nouvelle tâche");

		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLocation(400, 300);

		/*-------------------------------*/
		/* Création des composants       */
		/*-------------------------------*/
		this.panelAjouter = new PanelAjouter( this.ctrl, this );
		
		this.btnSoumettre = new JButton("Soumettre");
		this.btnSoumettre.setBackground( Color.RED   );
		this.btnSoumettre.setForeground( Color.WHITE );
		this.btnSoumettre.setEnabled( false );

		/*-------------------------------*/
		/* Positionnement des composants */
		/*-------------------------------*/
		this.add( this.panelAjouter, BorderLayout.CENTER  );
		this.add( this.btnSoumettre, BorderLayout.SOUTH   );		

		/*-------------------------------*/
		/* Gestion des événements        */
		/*-------------------------------*/
		this.btnSoumettre.addActionListener( this );
		
		this.pack();
		this.setVisible(true);
	}

	/*-------------------------------*/
	/* Méthodes                      */
	/*-------------------------------*/
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == this.btnSoumettre) 
		{
			this.dispose();
		}
	}
}
