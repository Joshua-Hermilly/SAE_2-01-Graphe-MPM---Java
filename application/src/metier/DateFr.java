package src.metier;

import java.util.GregorianCalendar;

/*---------------------------------*/
/*  Class DateFr                   */
/*---------------------------------*/
public class DateFr extends GregorianCalendar
{
	private static final int      DAY      =   20;
	private static final String[] TAB_JOUR = new String[] { "lundi", "mardi", "mercredi", "jeudi", "vendredi", "samedi", "dimanche"                                                 };
	private static final String[] TAB_MOIS = new String[] { "janvier", "février", "mars", "avril", "mai", "juin", "juillet", "auot", "septembre", "octobre", "novembre", "decembre" };

	/*------------------*/
	/* Constructeurs    */
	/*------------------*/
	public DateFr ()
	{
		super();
	}

	public DateFr ( int jour, int mois, int annee )
	{
		this( jour, mois, annee, 0, 0, 0 );
	}

	public DateFr ( int jour, int mois, int annee, int heure, int minute )
	{
		this( jour, mois, annee, heure, minute, 0 );
	}

	public DateFr ( int jour, int mois, int annee, int heure, int minute, int seconde )	
	{
		super( annee, mois-1, jour, heure, minute, seconde );
	}

	public DateFr ( String date )
	{
		int jour, mois, annee;
		jour  = Integer.parseInt( date.substring(0,  2) );
		mois  = Integer.parseInt( date.substring(3,  5) );
		annee = Integer.parseInt( date.substring(6    ) );

		this.set( annee, mois-1, jour, 0, 0, 0 );
	}

	public DateFr ( DateFr autreDate)
	{
		this ( autreDate.get( DateFr.DAY     ),
			   autreDate.get( DateFr.MONTH   ),
			   autreDate.get( DateFr.YEAR    ),
			   autreDate.get( DateFr.HOUR    ),
			   autreDate.get( DateFr.MINUTE  ),
			   autreDate.get( DateFr.SECOND  ) );
	}


	/*------------------*/
	/* Accesseur        */
	/*------------------*/
	public int get ( int field )
	{
		int iRet = -1;

		switch ( field ) 
		{
			case  DateFr.YEAR  , DateFr.MINUTE, 
				  DateFr.SECOND, DateFr.WEEK_OF_YEAR -> iRet = super.get( field );
			
			case  DateFr.DAY                         -> iRet = super.get( DateFr.DAY_OF_MONTH );

			case DateFr.MONTH                        -> iRet = super.get( field ) +1;

			case DateFr.HOUR                         -> iRet = super.get( HOUR_OF_DAY );

			case DateFr.DAY_OF_WEEK                  ->
			{
				iRet = super.get( DateFr.DAY_OF_WEEK );

				if    ( iRet == 1 ) iRet=7;
				else                iRet--;
			}
		}

		return iRet;
	}

	/*------------------*/
	/* Autres méthodes  */
	/*------------------*/
	public boolean estFerie( DateFr date )
	{
		//Dates fixes
		if ( date.get( DAY_OF_MONTH ) ==   1 && date.get( MONTH ) ==  1  ) return true; // Jour de l'an
		if ( date.get( DAY_OF_MONTH ) ==   1 && date.get( MONTH ) ==  5  ) return true; //Fete du travail
		if ( date.get( DAY_OF_MONTH ) ==   8 && date.get( MONTH ) ==  5  ) return true; //Victoire 1945
		if ( date.get( DAY_OF_MONTH ) ==  14 && date.get( MONTH ) ==  7  ) return true; //Fête Nationale
		if ( date.get( DAY_OF_MONTH ) ==  11 && date.get( MONTH ) == 11  ) return true; //Armistice 1918

		//Dates variables
		//Pâques
		int annee      = date.get( YEAR );
		int a          = annee % 19;
		int b          = annee % 4;
		int c          = annee % 7;
		int d          = ( 19 * a + 24 ) % 30;
		int e          = ( 2 * b + 4 * c + 6 * d + 5 ) % 7;
		int jourPaques = 22 + d + e;

		//M - A ?
		int moisPaques = 3;
		if ( jourPaques > 31 ) 
		{
			jourPaques -= 31;
			moisPaques = 4; 
		}

		// Dimanche de Pâques
		if ( date.get( DAY_OF_MONTH ) == jourPaques     && date.get( MONTH ) == moisPaques ) return true;
		// Lundi de Pâques
		if ( date.get( DAY_OF_MONTH ) == jourPaques + 1 && date.get( MONTH ) == moisPaques ) return true;
		
		// Jeudi de l'Ascension (+39)
		int jourAscension = jourPaques + 39;
		int moisAscension = moisPaques;
		if ( jourAscension > 31 && moisPaques == 3 ) 
		{
			jourAscension -= 31;
			moisAscension = 4;
		} 
		else if ( jourAscension > 30 && moisPaques == 4 ) 
		{
			jourAscension -= 30;
			moisAscension = 5;
		}
		if ( date.get( DAY_OF_MONTH ) == jourAscension && date.get( MONTH ) == moisAscension ) return true;

		// Dimanche de Pentecôte (+49)
		int jourPentecote = jourPaques + 49;
		int moisPentecote = moisPaques;
		if ( jourPentecote > 31 && moisPaques == 3 ) 
		{
			jourPentecote -= 31;
			moisPentecote = 4;
		} 
		else if ( jourPentecote > 30 && moisPaques == 4 ) 
		{
			jourPentecote -= 30;
			moisPentecote = 5;
		}
		if ( date.get( DAY_OF_MONTH ) == jourPentecote && date.get( MONTH ) == moisPentecote ) return true;

		// Lundi de Pentecôte (+50)
		int jourLundiPentecote = jourPaques + 50;
		int moisLundiPentecote = moisPaques;
		if ( jourLundiPentecote > 31 && moisPaques == 3 ) 
		{
			jourLundiPentecote -= 31;
			moisLundiPentecote = 4;
		} 
		else if ( jourLundiPentecote > 30 && moisPaques == 4 ) 
		{
			jourLundiPentecote -= 30;
			moisLundiPentecote = 5;
		}
		if ( date.get( DAY_OF_MONTH ) == jourLundiPentecote && date.get( MONTH ) == moisLundiPentecote ) return true;

		return false;
	}


	/*------------------*/
	/* toSring          */
	/*------------------*/
	public String toString()
	{
		return String.format( "%02d", this.get ( DateFr.DAY    ) ) + "/" +
			   String.format( "%02d", this.get ( DateFr.MONTH  ) ) + "/" +
			   String.format( "%4d" , this.get ( DateFr.YEAR   ) ) + " " +
			   String.format( "%02d", this.get ( DateFr.HOUR   ) ) + ":" +
			   String.format( "%02d", this.get ( DateFr.MINUTE ) ) + ":" +
			   String.format( "%02d", this.get ( DateFr.SECOND ) ) ;
	}

	public String toString ( char dateRef, int nbJourSup, int nbJourMax )
	{
		DateFr dateModifiee = new DateFr( this );

		if ( dateRef == 'D' ) 
		{
			dateModifiee.add( DateFr.DAY_OF_MONTH, nbJourSup );
		} 
		else 
		{
			dateModifiee.add( DateFr.DAY_OF_MONTH, nbJourSup - nbJourMax );
		}

		return String.format( "%02d", dateModifiee.get ( DateFr.DAY    ) ) + "/" +
			   String.format( "%02d", dateModifiee.get ( DateFr.MONTH  ) )        ;
	}

	public String toString(String format) 
	{
		int taille;
		int annee, mois, jourSemaine;

		//Annee
		annee       = this.get( DateFr.YEAR );

		format = format.replace( "aaaa", "" + annee       );
		format = format.replace( "aa"  , "" + annee % 100 );

		//Mois
		mois        = this.get( DateFr.MONTH );

		format = format.replace( "mmmm", DateFr.TAB_MOIS[ mois - 1 ]                            );
		if   ( mois == 4 || mois == 5 || mois >= 10 ) taille = 3;
		else                             taille = 4;
		format = format.replace( "mmm" , DateFr.TAB_MOIS[ mois - 1 ].substring(0, taille)  +" " );
		format = format.replace( "mm"  , String.format( "%02d", mois )                          );

		//Jour
		jourSemaine = this.get( DateFr.DAY_OF_WEEK  );

		format = format.replace( "jjjj", DateFr.TAB_JOUR[ jourSemaine - 1 ]                 );
		format = format.replace( "jjj" , DateFr.TAB_JOUR[ jourSemaine - 1 ].substring(0, 3) );
		format = format.replace( "jj"  , String.format( "%02d", this.get( DateFr.DAY ) )    );

		//Temps
		format = format.replace( "hh"  , String.format( "%02d", this.get( DateFr.HOUR    ) ) );
		format = format.replace( "mn"  , String.format( "%02d", this.get( DateFr.MINUTE  ) ) );
		format = format.replace( "sc"  , String.format( "%02d", this.get( DateFr.SECOND  ) ) );

		return format;
	}


	/*---------------------------------*/
	/*  Main                           */
	/*---------------------------------*/
	public static void main(String[] a)
	{
		DateFr dPrev;
		DateFr d1 = new DateFr( 24, 4, 2025, 8, 55 );
		DateFr d2 = new DateFr( d1 );
		DateFr d3 = new DateFr( "25/04/2025" );
		DateFr d4 = new DateFr( );

		System.out.println( "d1 : " + d1 );
		System.out.println( "d2 : " + d2 );
		System.out.println( "d2 : " + d3 );
		System.out.println( d4.toString("mmmm mmm jjjj jjj jj/mm/aaaa aa hh:mn:sc") );

		dPrev  = new DateFr( "01/01/2025");
		d1     = new DateFr( "01/01/2025");
		for ( int cpt=0; cpt<=365; cpt++ )
		{
			if ( dPrev.get ( DateFr.MONTH ) != d1.get (DateFr.MONTH ) ) 
				System.out.println( "\n" + d1.toString( "mmmm" ) );

			System.out.println( d1.toString( " jj/mm/aaaa --> jj jjj mmm aaaa" ) );
			
			dPrev =  new DateFr( d1 );
			d1.add ( DateFr.DAY_OF_MONTH, 1 );
		
		}
	}
}
