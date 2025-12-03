# Projet MPM - Mode d'emploi

## Lancement du projet

### Méthode 1 : Run.sh (Linux/Mac)
```bash
chmod +x run.sh
./run.sh
```

### Méthode 2 : Commandes manuelles

#### Windows
```cmd
javac "@compile.list" -d class
cd class
java src.Controleur
```

#### Linux/Mac
```bash
javac @compile.list -d class
cd class
java src.Controleur
```

## Structure
- `src/`    : Code source Java
- `class/`  : Fichiers compilés (.class)
- `data/`   : Fichiers de données et sauvegardes
