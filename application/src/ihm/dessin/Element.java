package src.ihm.dessin;

import src.metier.Tache;

import java.awt.Graphics;

/*------------------------*/
/* Class Element          */
/*------------------------*/
public abstract class Element
{
	/*---------------*/
	/* Attributs     */
	/*---------------*/
	private int centreX;
	private int centreY;
	private int tailleX;
	private int tailleY;


	/*---------------*/
	/* Constructeur  */
	/*---------------*/
	public Element ( int centreX, int centreY, int tailleX, int tailleY )
	{
		this.centreX = centreX;
		this.centreY = centreY;

		this.tailleX = tailleX;
		this.tailleY = tailleY;
	}

	/*---------------*/
	/* Accesseurs    */
	/*---------------*/
	public int getCentreX() { return this.centreX; }
	public int getCentreY() { return this.centreY; }
	public int getTailleX() { return this.tailleX; }
	public int getTailleY() { return this.tailleY; }

	/*---------------*/
	/* Modificateurs */
	/*---------------*/
	public void deplacerX (int x) { this.centreX += x; }
	public void deplacerY (int y) { this.centreY += y; }
	public void setTailleX(int x) { this.tailleX  = x; }
	public void setTailleY(int y) { this.tailleY  = y; }
	public void setCentreX(int x) { this.centreX  = x; }
	public void setCentreY(int y) { this.centreY  = y; }

	/*---------------*/
	/* Méthodes      */
	/*---------------*/
	public abstract boolean possede  ( int x, int y );
	public abstract void    dessiner ( Graphics g );
	public abstract Tache   getTache ();
}
