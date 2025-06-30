#!/bin/bash

# ==============================================================================
# SCRIPT D'AUTOMATISATION POUR LA BASE DE DONNÉES DU BAR À COCKTAILS
# ==============================================================================

# --- CONFIGUREZ CES VARIABLES AVANT D'EXÉCUTER LE SCRIPT ---
# Nom de la base de données à créer
DB_NAME="bar_cocktail_db"
# Utilisateur PostgreSQL avec les droits de création de base de données
DB_USER="root"
# Mot de passe de l'utilisateur
# Recommandation : ne stockez pas les mots de passe en clair dans un script
# Utilisez plutôt la variable d'environnement PGPASSWORD
# export PGPASSWORD="passroot"
# --- FIN DE LA CONFIGURATION ---

echo "================================================="
echo "  Automatisation de la configuration de la BDD"
echo "================================================="

# --------------------------------------------------------------
# ÉTAPE 1 : VÉRIFIER SI LA BASE DE DONNÉES EXISTE ET LA CRÉER
# --------------------------------------------------------------

echo "-> Étape 1 : Vérification et création de la base de données '$DB_NAME'..."

# La commande 'psql -lqt' liste les bases de données et 'grep' cherche le nom
# Si 'grep' trouve le nom, la base de données existe
if psql -U "$DB_USER" -lqt | cut -d \| -f 1 | grep -qw "$DB_NAME"; then
    echo "   La base de données '$DB_NAME' existe déjà. On continue."
else
    echo "   La base de données '$DB_NAME' n'existe pas. Création en cours..."
    # 'createdb' est un outil client PostgreSQL pour créer des bases de données
    createdb -U "$DB_USER" "$DB_NAME"
    if [ $? -eq 0 ]; then
        echo "   Base de données '$DB_NAME' créée avec succès."
    else
        echo "   ERREUR : Impossible de créer la base de données '$DB_NAME'. Vérifiez les droits de l'utilisateur '$DB_USER'."
        exit 1
    fi
fi

# --------------------------------------------------------------
# ÉTAPE 2 : EXÉCUTER LE SCRIPT DE CRÉATION DE TABLES
# --------------------------------------------------------------

echo "-> Étape 2 : Exécution du script de création des tables dans '$DB_NAME'..."

# Utilise un "here-document" (<<EOF) pour passer le code SQL directement à psql
# Cela évite d'avoir un fichier .sql séparé, tout est dans un seul script
psql -U "$DB_USER" -d "$DB_NAME" -v ON_ERROR_STOP=1 <<-EOF
-- Création de la table Categorie
CREATE TABLE IF NOT EXISTS Categorie (
    id_categorie SERIAL PRIMARY KEY,
    nom_categorie VARCHAR(50) NOT NULL UNIQUE
);

-- Création de la table Ingredient
CREATE TABLE IF NOT EXISTS Ingredient (
    id_ingredient SERIAL PRIMARY KEY,
    nom_ingredient VARCHAR(100) NOT NULL UNIQUE
);

-- Création de la table Taille
CREATE TABLE IF NOT EXISTS Taille (
    id_taille SERIAL PRIMARY KEY,
    nom_taille VARCHAR(1) CHECK (nom_taille IN ('S', 'M', 'L')) NOT NULL UNIQUE
);

-- Création de la table Utilisateur avec un rôle
CREATE TABLE IF NOT EXISTS Utilisateur (
    id_utilisateur SERIAL PRIMARY KEY,
    nom_utilisateur VARCHAR(100) NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL,
    role_utilisateur VARCHAR(50) CHECK (role_utilisateur IN ('Client', 'Barmaker')) NOT NULL
);

-- Création de la table Cocktail (avec le barmaker créateur)
CREATE TABLE IF NOT EXISTS Cocktail (
    id_cocktail SERIAL PRIMARY KEY,
    nom_cocktail VARCHAR(100) NOT NULL,
    description_cocktail TEXT,
    id_categorie INT,
    id_createur_utilisateur INT,
    FOREIGN KEY (id_categorie) REFERENCES Categorie(id_categorie),
    FOREIGN KEY (id_createur_utilisateur) REFERENCES Utilisateur(id_utilisateur)
);

-- Création de la table de jointure Cocktail_Ingredient
CREATE TABLE IF NOT EXISTS Cocktail_Ingredient (
    id_cocktail INT,
    id_ingredient INT,
    quantite DECIMAL(10, 2),
    unite VARCHAR(20),
    PRIMARY KEY (id_cocktail, id_ingredient),
    FOREIGN KEY (id_cocktail) REFERENCES Cocktail(id_cocktail),
    FOREIGN KEY (id_ingredient) REFERENCES Ingredient(id_ingredient)
);

-- Création de la table de jointure Cocktail_Taille_Prix
CREATE TABLE IF NOT EXISTS Cocktail_Taille_Prix (
    id_cocktail INT,
    id_taille INT,
    prix DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (id_cocktail, id_taille),
    FOREIGN KEY (id_cocktail) REFERENCES Cocktail(id_cocktail),
    FOREIGN KEY (id_taille) REFERENCES Taille(id_taille)
);

-- Création de la table Commande
CREATE TABLE IF NOT EXISTS Commande (
    id_commande SERIAL PRIMARY KEY,
    date_commande TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    statut_commande VARCHAR(50) CHECK (statut_commande IN ('Commandée', 'en cours de préparation', 'Terminée')) DEFAULT 'Commandée' NOT NULL,
    id_utilisateur INT,
    FOREIGN KEY (id_utilisateur) REFERENCES Utilisateur(id_utilisateur)
);

-- Création de la table Ligne_Commande
CREATE TABLE IF NOT EXISTS Ligne_Commande (
    id_ligne_commande SERIAL PRIMARY KEY,
    id_commande INT,
    id_cocktail INT,
    id_taille INT,
    quantite INT NOT NULL,
    statut_cocktail_preparation VARCHAR(50) CHECK (statut_cocktail_preparation IN ('Préparation des Ingrédients', 'Assemblage', 'Dressage', 'Terminée')) DEFAULT 'Préparation des Ingrediens' NOT NULL,
    FOREIGN KEY (id_commande) REFERENCES Commande(id_commande),
    FOREIGN KEY (id_cocktail) REFERENCES Cocktail(id_cocktail),
    FOREIGN KEY (id_taille) REFERENCES Taille(id_taille)
);
EOF

# --------------------------------------------------------------
# ÉTAPE 3 : CONFIRMATION
# --------------------------------------------------------------

if [ $? -eq 0 ]; then
    echo "   Toutes les tables ont été créées ou vérifiées avec succès."
    echo "   La base de données '$DB_NAME' est prête à être utilisée !"
    echo "================================================="
else
    echo "   ERREUR : Une erreur est survenue lors de l'exécution du script SQL."
    echo "   La base de données n'a peut-être pas été configurée correctement."
    echo "================================================="
fi