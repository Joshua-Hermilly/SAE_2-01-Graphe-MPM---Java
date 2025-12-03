package src.ihm.panels;

import src.Controleur;
import src.metier.Tache;

import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;


public class PanelDetail extends JPanel
{
	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/
	private Controleur ctrl;
	private Tache      tache;

	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/
	public PanelDetail( Controleur ctrl, Tache tache, char type )
	{
		this.ctrl = ctrl;
		this.tache = tache;

		this.setLayout(new BorderLayout());

		// Titre
		String titre; 
		if (type == '-') titre = "Calcul de la date au plus tôt de "  + tache.getNom();
		else             titre = "Calcul de la date au plus tard de " + tache.getNom();
		
		this.setBorder( BorderFactory.createTitledBorder( titre ) );

		/*-------------------------------*/
		/* Création des composants       */
		/*-------------------------------*/
		JPanel panelCalculs = new JPanel(new GridLayout(0, 1, 5, 5));

		// ----
		// DATE AU PLUS TOT
		// ----
		if (type == '-') 
		{
			if ( this.tache.getlstPrc() != null && ! this.tache.getlstPrc().isEmpty() ) 
			{
				panelCalculs.add( new JLabel( "Calculs des prédécesseurs :" ) );
				
				int maxVal = -1;
				String maxTache = "";
				
				for ( Tache tPrc : tache.getlstPrc() ) 
				{
					int val = tPrc.getDte_tot() + tPrc.getDuree();
					String calcul = tPrc.getNom() + " : " + tPrc.getDte_tot() + " + " + tPrc.getDuree() + " = " + val;
					
					if (val > maxVal) 
					{
						maxVal   = val;
						maxTache = calcul;
					}
					
					panelCalculs.add( new JLabel("  - " + calcul) );
				}
				
				panelCalculs.add( new JLabel( ) );
				
				JLabel resultat = new JLabel("Résultat : " + maxVal + " (vient de " + maxTache.split(" :")[0] + ")");
				resultat.setForeground( new Color ( 42, 156, 96 ) );
				
				panelCalculs.add(resultat);
			} 
			// pas de prc
			else  { panelCalculs.add(new JLabel("Aucun prédécesseur - Date au plus tôt = 0")); }

		} 
		
		// ----
		// DATE AU PLUS TARD
		// ----
		else
		{			
			if ( this.tache.getlstSvt() != null && ! this.tache.getlstSvt().isEmpty() ) 
			{
				panelCalculs.add( new JLabel("Calculs des successeurs :") );
				
				int minVal = 100000;
				String minTache = "";
				
				for ( Tache tSvt : tache.getlstSvt() )
				{
					int val = tSvt.getDte_tard() - this.tache.getDuree();
					String calcul = tSvt.getNom() + " : " + tSvt.getDte_tard() + " - " + this.tache.getDuree() + " = " + val;
					
					if (val < minVal) 
					{
						minVal = val;
						minTache = calcul;
					}
					 
					panelCalculs.add( new JLabel("  - " + calcul) );
				}
				
				panelCalculs.add(new JLabel()); 
				
				JLabel resultat = new JLabel("Résultat : " + minVal + " (vient de " + minTache.split(" :")[0] + ")");
				resultat.setForeground( Color.RED );
				
				panelCalculs.add(resultat);

			} 
			// pas de svt
			else  { panelCalculs.add(new JLabel("Aucun successeur")); }
		}


		/*-------------------------------*/
		/* Positionnement des composants */
		/*-------------------------------*/
		this.add(panelCalculs, BorderLayout.CENTER);
	}	
}
