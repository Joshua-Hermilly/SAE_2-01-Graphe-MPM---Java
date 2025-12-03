package src.metier.gestionErreur;

/*---------------------------------*/
/*  Enum CodeErreur                */
/*---------------------------------*/
public enum CodeErreur 
{
	/*-------------------------------*/
	/* Codes d'erreur                */
	/*-------------------------------*/

	/* ==== Aucune erreur ==== */
	OK (0, "Aucune erreur (Alonso champion du monde)"),

	/* ==== Erreurs ==== */
	// Erreur lors lier a la lecture d'un fichier
	ERREUR_LECTURE_                     ( 10 , "Erreur : Lors de la lecture de fichier"        ),
	ERREUR_LECTURE_MAUVAISE_EXTENSION   ( 11 , "Erreur : Mauvaise extension de fichier"        ),
    ERREUR_LECTURE_FICHIER_INEXISTANT   ( 12 , "Erreur : Fichier inexistant"                   ),
    ERREUR_LECTURE_FICHIER_VIDE         ( 13 , "Erreur : Fichier vide"                         ),
    ERREUR_LECTURE_DONNEES_MANQUANTES   ( 14 , "Erreur : Données obligatoires manquantes"      ),
    ERREUR_LECTURE_STRUCTURE_INVALIDE   ( 15 , "Erreur : Structure de données invalide"        ),
    ERREUR_LECTURE_VALEURS_INCOHERENTES ( 16 , "Erreur : Valeurs incohérentes détectées"       ),
	ERREUR_LECTURE_GRAPHE_NULL          ( 190, "Erreur : Graphe MPM est null"                  ),
	ERREUR_LECTURE_TACHE_NULL		    ( 191, "Erreur : Tâche MPM est null"                   ),

	// Erreur lors de la sauvegarde d'un fichier
	ERREUR_ECRITURE_                     (20, "Erreur : Lors de l'écriture dans le fichier"    ),
	ERREUR_ECRITURE_MAUVAISE_EXTENSION   (21, "Erreur : Mauvaise extension de fichier"         ),
	ERREUR_ECRITURE_FICHIER_EXISTANT     (22, "Erreur : Fichier déjà existant"                 ),
	ERREUR_ECRITURE_DONNEES_NULL         (23, "Erreur : Données à écrire sont nulles"          ),
	ERREUR_ECRITURE_DONNEES_NULLES       (24, "Erreur : Données à écrire sont nulles"          ),
	ERREUR_ECRITURE_IO                   (25, "Erreur : Erreur d'entrée/sortie"                ),
	ERREUR_ECRITURE_NOM_INVALIDE         (26, "Erreur : Nom de fichier invalide"               ),
	ERREUR_ECRITURE_DOSSIER_INEXISTANT   (27, "Erreur : Dossier de destination inexistant"     ),

	// Erreur lors de la gestion des tâches
	ERREUR_TACHE_NOM_VIDE                (30, "Erreur : Nom de tâche vide ou null"             ),
    ERREUR_TACHE_NOM_EXISTANT            (31, "Erreur : Une tâche avec ce nom existe déjà"     ),
    ERREUR_TACHE_DUREE_INVALIDE          (32, "Erreur : Durée de tâche invalide"               ),
    ERREUR_TACHE_INEXISTANTE             (33, "Erreur : Tâche inexistante"                     ),
    ERREUR_TACHE_CYCLE_DETECTE           (34, "Erreur : Cycle détecté dans le graphe"          ),
    ERREUR_TACHE_LIEN_IMPOSSIBLE         (35, "Erreur : Liaison impossible entre ces tâches"   ),
    ERREUR_TACHE_NIVEAU_INVALIDE         (36, "Erreur : Niveau de tâche invalide"              ),
    ERREUR_TACHE_DATES_INCOHERENTES      (37, "Erreur : Dates incohérentes"                    ),
	
	// Erreur lors de la gestion des chemins critiques
	ERREUR_CHEMIN_CRITIQUE_INEXISTANT    (40, "Erreur : Chemin critique inexistant"            ),

	// Erreur lors du lancement de l'application
	ERREUR_LANCEMENT_APPLICATION         (50, "Erreur : Impossible de lancer l'application"    ),
	ERREUR_LANCEMENT_CHAMPS_VIDE         (51, "Erreur : Champs requis vides"                   ),
	ERREUR_LANCEMENT_DATE_INVALIDE       (52, "Erreur : Date invalide"                         ),

	// Erreur lors d'un passage de niveau impossible
	ERREUR_NIVEAU_INVALIDE               (60, "Erreur : Niveau invalide, essayer de connecter les taches" ),

	// Erreur défaut
	ERREUR_DEFAUT                        (666, "Erreur : Erreur inconnue au bataillon"          );


	/*-------------------------------*/
	/* Attributs                     */
	/*-------------------------------*/
	private final int code;
	private final String message;

	/*-------------------------------*/
	/* Constructeur                  */
	/*-------------------------------*/
	CodeErreur(int code, String message) 
	{
		this.code = code;
		this.message = message;
	}

	/*-------------------------------*/
	/* Méthodes                      */
	/*-------------------------------*/
	public int    getCode    ()  { return code;    }
	public String getMessage ()  { return message; }
	public static CodeErreur getErreur( int code ) 
	{
		for (CodeErreur erreur : CodeErreur.values()) 
		{
			if (erreur.getCode() == code) 
			{
				return erreur;
			}
		}
		return ERREUR_DEFAUT; 
	}
	
	@Override
	public String toString()
	{
		return "Code " + this.code + ": " + this.message;
	}
}