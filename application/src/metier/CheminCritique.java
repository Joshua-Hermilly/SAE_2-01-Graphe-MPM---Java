package src.metier;

import java.util.List;
import java.util.ArrayList;

/*---------------------------------*/
/*  Class CheminCritique           */
/*---------------------------------*/
public class CheminCritique
{
	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/
	private List<Tache> cheminCritique;

	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/
	public CheminCritique() { this.cheminCritique = new ArrayList<Tache>(); }
	
	public CheminCritique(CheminCritique c)
	{
		this.cheminCritique = new ArrayList<Tache>();
		
		for (Tache t : c.cheminCritique) 
			this.cheminCritique.add(t);
	}

	/*-------------------------------*/
	/* Modificateurs                */
	/*-------------------------------*/	
	public void ajouterTache(Tache t)
	{
		if ( t == null || this.cheminCritique.contains( t ) ) return;
		
		this.cheminCritique.add(t);
	}
	
	/*-------------------------------*/
	/* Accesseurs                    */
	/*-------------------------------*/
	public List<Tache> getCheminCritique() { return this.cheminCritique; }
	public List<Tache> getChemin        () { return this.cheminCritique; }

	
	/*-------------------------------*/
	/* Méthodes                      */
	/*-------------------------------*/
	public String toString()
	{
		String res ="";
		
		for ( Tache t : this.cheminCritique)
			res += t.getNom() + "->";
		if ( res.length() > 0 ) res = res.substring( 0, res.length() - 2 );
		return res+"\n";
	}
}