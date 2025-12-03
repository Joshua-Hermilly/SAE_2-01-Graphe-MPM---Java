package src.ihm.panels;

import src.Controleur;
import src.ihm.frames.FramePrincipale;
import src.metier.gestionErreur.CodeErreur;

import javax.swing.*;

import java.awt.event.*;

/*---------------------------------*/
/*  Class PanelBtn                 */
/*---------------------------------*/
public class PanelBtn extends JPanel implements ActionListener, ItemListener
{
	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/
	private Controleur      ctrl;
	private FramePrincipale frame;

	private JButton  btnTot;
	private JButton  btnTard;
	private JButton  btnCritique;
	private JButton  btnAjouter;
	private JButton  btnRepositionner;

	private JRadioButton rbNbJour;
	private JRadioButton rbDate;

	private int      cptDate = 0;

	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/
	public PanelBtn( Controleur ctrl, FramePrincipale frame ) 
	{
		this.ctrl  = ctrl;
		this.frame = frame;
		this.cptDate = 1;

		//this.setLayout( new GridLayout( 1, 6, 10, 2) );
		/*-------------------------------*/
		/* Création des composants       */
		/*-------------------------------*/
		this.btnTot  = new JButton("+ tôt");

		this.btnTard = new JButton("+ tard");
		this.btnTard.setEnabled(false);

		this.btnCritique = new JButton("Chemin critique");
		this.btnCritique.setEnabled(false); 

		ButtonGroup bg = new ButtonGroup();
		this.rbNbJour  = new JRadioButton( "Nb de jours" );
		this.rbNbJour.setSelected(true);
		this.rbDate    = new JRadioButton( "Date jj/mm"  );
		bg.add(this.rbNbJour);
		bg.add(this.rbDate);

		this.btnAjouter = new JButton("Ajouter une tâche");
		this.btnRepositionner = new JButton("Repositionner");

		/* ----------------------------- */
		/* Positionnement des composants */
		/* ----------------------------- */
		this.add( this.btnCritique );
		this.add( this.btnTot      );
		this.add( this.btnTard     );
		this.add( this.rbNbJour    );
		this.add( this.rbDate      );
		this.add( this.btnAjouter  );
		this.add( this.btnRepositionner );


		/* ------------------------------ */
		/* Activation des composants      */
		/* ------------------------------ */
		this.btnTot     .addActionListener( this );
		this.btnTard    .addActionListener( this );
		this.btnCritique.addActionListener( this );
		this.rbNbJour   .addItemListener  ( this );
		this.rbDate     .addItemListener  ( this );
		this.btnAjouter .addActionListener( this );
		this.btnRepositionner.addActionListener( this );
	}

	/*-------------------------------*/
	/* Méthodes                      */
	/*-------------------------------*/
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == this.btnTot) 
		{
			if ( this.ctrl.afficherDate( this.cptDate, '-' ) )
			{
				this.cptDate++;
			}
			else { this.gestionErreur( CodeErreur.ERREUR_NIVEAU_INVALIDE ); }
		}

		if (e.getSource() == this.btnTard) 
		{
			this.cptDate--;
			if ( !this.ctrl.afficherDate( this.cptDate, '+' ) ) 
			{
				this.cptDate++;
				this.gestionErreur( CodeErreur.ERREUR_NIVEAU_INVALIDE );
			}
		}

		if (e.getSource() == this.btnCritique)
		{
			this.ctrl.afficherCheminCritique();
		}

		if (e.getSource() == this.btnAjouter)  { this.ctrl.creerTache(); }
		if (e.getSource() == this.btnRepositionner) { this.ctrl.repositionner(); }

		this.majBtn();
	}

	public void itemStateChanged(ItemEvent e) 
	{
		if (e.getSource() == this.rbNbJour)  this.ctrl.setAffichageDate( false );
		if (e.getSource() == this.rbDate  )  this.ctrl.setAffichageDate( true  );
	}

	private void majBtn() 
	{
		int maxNiveau = this.ctrl.getListTacheParNiveau().size() - 1;

		if (  this.btnTot.isEnabled() ) this.btnTot .setEnabled( this.cptDate <= maxNiveau );
		if ( !this.btnTot.isEnabled() ) this.btnTard.setEnabled( this.cptDate > 0          );

		if ( !this.btnTot.isEnabled() && !this.btnTard.isEnabled() ) 
		{
			this.btnCritique.setEnabled( true );
		}
	}

	public void resetBtn(  ) 
	{
		this.cptDate =0;
		this.majBtn();
	}


	public static void gestionErreur( CodeErreur code )
	{
		JOptionPane.showMessageDialog( null, code.toString(), "Erreur", JOptionPane.ERROR_MESSAGE );
	}
}
