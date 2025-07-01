--
-- Fichier d'initialisation pour la base de données 'bar_cocktail_db'
-- Ce script est exécuté automatiquement par Docker au premier démarrage du conteneur.
--

-- Création de la table Categorie
CREATE TABLE Categorie (
    id_categorie SERIAL PRIMARY KEY,
    nom_categorie VARCHAR(50) NOT NULL UNIQUE
);

-- Création de la table Ingredient
CREATE TABLE Ingredient (
    id_ingredient SERIAL PRIMARY KEY,
    nom_ingredient VARCHAR(100) NOT NULL UNIQUE
);

-- Création de la table Taille
CREATE TABLE Taille (
    id_taille SERIAL PRIMARY KEY,
    nom_taille VARCHAR(1) NOT NULL UNIQUE
);

-- Création de la table Utilisateur avec un rôle
CREATE TABLE Utilisateur (
    id_utilisateur SERIAL PRIMARY KEY,
    email_utilisateur VARCHAR(100) NOT NULL UNIQUE,
    mot_de_passe VARCHAR(255) NOT NULL,
    role_utilisateur VARCHAR(50) CHECK (role_utilisateur IN ('Client', 'Barmaker')) NOT NULL
);

-- Création de la table Cocktail (avec le barmaker créateur)
CREATE TABLE Cocktail (
    id_cocktail SERIAL PRIMARY KEY,
    nom_cocktail VARCHAR(100) NOT NULL UNIQUE,
    description_cocktail TEXT,
    id_categorie INT,
    id_createur_utilisateur INT,
    FOREIGN KEY (id_categorie) REFERENCES Categorie(id_categorie),
    FOREIGN KEY (id_createur_utilisateur) REFERENCES Utilisateur(id_utilisateur)
);

-- Création de la table de jointure Cocktail_Ingredient
CREATE TABLE Cocktail_Ingredient (
    id_cocktail INT,
    id_ingredient INT,
    quantite DECIMAL(10, 2),
    unite VARCHAR(20),
    PRIMARY KEY (id_cocktail, id_ingredient),
    FOREIGN KEY (id_cocktail) REFERENCES Cocktail(id_cocktail),
    FOREIGN KEY (id_ingredient) REFERENCES Ingredient(id_ingredient)
);

-- Création de la table de jointure Cocktail_Taille_Prix
CREATE TABLE Cocktail_Taille_Prix (
    id_cocktail INT,
    id_taille INT,
    prix DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (id_cocktail, id_taille),
    FOREIGN KEY (id_cocktail) REFERENCES Cocktail(id_cocktail),
    FOREIGN KEY (id_taille) REFERENCES Taille(id_taille)
);

-- Création de la table Commande
CREATE TABLE Commande (
    id_commande SERIAL PRIMARY KEY,
    date_commande TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    statut_commande VARCHAR(50) CHECK (statut_commande IN ('Commandée', 'en cours de préparation', 'Terminée')) DEFAULT 'Commandée' NOT NULL,
    id_utilisateur INT,
    FOREIGN KEY (id_utilisateur) REFERENCES Utilisateur(id_utilisateur)
);

-- Création de la table Ligne_Commande
CREATE TABLE Ligne_Commande (
    id_ligne_commande SERIAL PRIMARY KEY,
    id_commande INT,
    id_cocktail INT,
    id_taille INT,
    quantite INT NOT NULL,
    statut_cocktail_preparation VARCHAR(50) CHECK (statut_cocktail_preparation IN ('Préparation des Ingrédients', 'Assemblage', 'Dressage', 'Terminée')) DEFAULT 'Préparation des Ingrédients' NOT NULL,
    FOREIGN KEY (id_commande) REFERENCES Commande(id_commande),
    FOREIGN KEY (id_cocktail) REFERENCES Cocktail(id_cocktail),
    FOREIGN KEY (id_taille) REFERENCES Taille(id_taille)
);