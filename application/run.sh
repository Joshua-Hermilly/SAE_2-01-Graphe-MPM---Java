#!/bin/bash

# Compilation
javac -d class @compile.list
if [ $? -ne 0 ]; then
    echo "Erreur de compilation."
    exit 1
fi

# Exécution
cd class
java src.Controleur
