-- data.sql

-- Insertion de données dans la table Categorie
INSERT INTO Categorie (nom_categorie) VALUES
('Classique'),
('Tropical'),
('Crémeux'),
('Sans Alcool');

-- Insertion de données dans la table Ingredient
INSERT INTO Ingredient (nom_ingredient) VALUES
('Rhum Blanc'),
('Jus de Citron Vert'),
('Menthe Fraîche'),
('Sucre de Canne'),
('Eau Gazeuse'),
('Rhum Ambré'),
('Jus d''Ananas'),
('Crème de Coco'),
('Sirop de Grenadine'),
('Vodka'),
('Jus de Cranberry'),
('Liqueur d''Orange'),
('Jus d''Orange'),
('Glaçons'),
('Cerise');

-- Insertion de données dans la table Taille
INSERT INTO Taille (nom_taille) VALUES
('S'),
('M'),
('L');

-- Insertion de données dans la table Utilisateur (pour les clients et les barmakers)
INSERT INTO Utilisateur (nom_utilisateur, mot_de_passe, role_utilisateur) VALUES
('client1', 'passclient1', 'Client'),
('barmaker1', 'passbarmaker1', 'Barmaker'),
('client2', 'passclient2', 'Client');

-- Insertion de données dans la table Cocktail
-- Assurez-vous que les IDs de catégorie et de créateur existent !
INSERT INTO Cocktail (nom_cocktail, description_cocktail, id_categorie, id_createur_utilisateur) VALUES
('Mojito', 'Un cocktail rafraîchissant à base de rhum et de menthe.', (SELECT id_categorie FROM Categorie WHERE nom_categorie = 'Classique'), (SELECT id_utilisateur FROM Utilisateur WHERE nom_utilisateur = 'barmaker1')),
('Pina Colada', 'Un cocktail tropical crémeux à base de rhum, ananas et coco.', (SELECT id_categorie FROM Categorie WHERE nom_categorie = 'Tropical'), (SELECT id_utilisateur FROM Utilisateur WHERE nom_utilisateur = 'barmaker1')),
('Sex on the Beach', 'Un cocktail fruité et coloré.', (SELECT id_categorie FROM Categorie WHERE nom_categorie = 'Classique'), (SELECT id_utilisateur FROM Utilisateur WHERE nom_utilisateur = 'barmaker1')),
('Virgin Mojito', 'La version sans alcool du célèbre Mojito.', (SELECT id_categorie FROM Categorie WHERE nom_categorie = 'Sans Alcool'), (SELECT id_utilisateur FROM Utilisateur WHERE nom_utilisateur = 'barmaker1'));

-- Insertion de données dans la table Cocktail_Ingredient
-- ID_COCKTAIL, ID_INGREDIENT, QUANTITE, UNITE
INSERT INTO Cocktail_Ingredient (id_cocktail, id_ingredient, quantite, unite) VALUES
((SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Mojito'), (SELECT id_ingredient FROM Ingredient WHERE nom_ingredient = 'Rhum Blanc'), 50.0, 'ml'),
((SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Mojito'), (SELECT id_ingredient FROM Ingredient WHERE nom_ingredient = 'Jus de Citron Vert'), 25.0, 'ml'),
((SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Mojito'), (SELECT id_ingredient FROM Ingredient WHERE nom_ingredient = 'Menthe Fraîche'), 10.0, 'feuilles'),
((SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Mojito'), (SELECT id_ingredient FROM Ingredient WHERE nom_ingredient = 'Sucre de Canne'), 20.0, 'ml'),
((SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Mojito'), (SELECT id_ingredient FROM Ingredient WHERE nom_ingredient = 'Eau Gazeuse'), 100.0, 'ml'),
((SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Mojito'), (SELECT id_ingredient FROM Ingredient WHERE nom_ingredient = 'Glaçons'), 1.0, 'dose'),

((SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Pina Colada'), (SELECT id_ingredient FROM Ingredient WHERE nom_ingredient = 'Rhum Ambré'), 60.0, 'ml'),
((SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Pina Colada'), (SELECT id_ingredient FROM Ingredient WHERE nom_ingredient = 'Jus d''Ananas'), 90.0, 'ml'),
((SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Pina Colada'), (SELECT id_ingredient FROM Ingredient WHERE nom_ingredient = 'Crème de Coco'), 30.0, 'ml'),
((SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Pina Colada'), (SELECT id_ingredient FROM Ingredient WHERE nom_ingredient = 'Glaçons'), 1.0, 'dose'),

((SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Sex on the Beach'), (SELECT id_ingredient FROM Ingredient WHERE nom_ingredient = 'Vodka'), 40.0, 'ml'),
((SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Sex on the Beach'), (SELECT id_ingredient FROM Ingredient WHERE nom_ingredient = 'Liqueur d''Orange'), 20.0, 'ml'),
((SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Sex on the Beach'), (SELECT id_ingredient FROM Ingredient WHERE nom_ingredient = 'Jus de Cranberry'), 60.0, 'ml'),
((SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Sex on the Beach'), (SELECT id_ingredient FROM Ingredient WHERE nom_ingredient = 'Jus d''Orange'), 60.0, 'ml'),
((SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Sex on the Beach'), (SELECT id_ingredient FROM Ingredient WHERE nom_ingredient = 'Glaçons'), 1.0, 'dose'),
((SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Sex on the Beach'), (SELECT id_ingredient FROM Ingredient WHERE nom_ingredient = 'Cerise'), 1.0, 'unité'),

((SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Virgin Mojito'), (SELECT id_ingredient FROM Ingredient WHERE nom_ingredient = 'Jus de Citron Vert'), 25.0, 'ml'),
((SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Virgin Mojito'), (SELECT id_ingredient FROM Ingredient WHERE nom_ingredient = 'Menthe Fraîche'), 10.0, 'feuilles'),
((SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Virgin Mojito'), (SELECT id_ingredient FROM Ingredient WHERE nom_ingredient = 'Sucre de Canne'), 20.0, 'ml'),
((SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Virgin Mojito'), (SELECT id_ingredient FROM Ingredient WHERE nom_ingredient = 'Eau Gazeuse'), 100.0, 'ml'),
((SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Virgin Mojito'), (SELECT id_ingredient FROM Ingredient WHERE nom_ingredient = 'Glaçons'), 1.0, 'dose');


-- Insertion de données dans la table Cocktail_Taille_Prix
-- ID_COCKTAIL, ID_TAILLE, PRIX
INSERT INTO Cocktail_Taille_Prix (id_cocktail, id_taille, prix) VALUES
((SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Mojito'), (SELECT id_taille FROM Taille WHERE nom_taille = 'M'), 8.50),
((SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Mojito'), (SELECT id_taille FROM Taille WHERE nom_taille = 'L'), 11.00),
((SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Pina Colada'), (SELECT id_taille FROM Taille WHERE nom_taille = 'M'), 9.00),
((SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Pina Colada'), (SELECT id_taille FROM Taille WHERE nom_taille = 'L'), 12.00),
((SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Sex on the Beach'), (SELECT id_taille FROM Taille WHERE nom_taille = 'M'), 8.00),
((SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Virgin Mojito'), (SELECT id_taille FROM Taille WHERE nom_taille = 'S'), 6.00),
((SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Virgin Mojito'), (SELECT id_taille FROM Taille WHERE nom_taille = 'M'), 7.50);

-- Insertion de données dans la table Commande
INSERT INTO Commande (id_commande, id_utilisateur, statut_commande) VALUES
(1, (SELECT id_utilisateur FROM Utilisateur WHERE nom_utilisateur = 'client_alice'), 'Commandée'),
(2, (SELECT id_utilisateur FROM Utilisateur WHERE nom_utilisateur = 'client_bob'), 'Commandée'),
(3, (SELECT id_utilisateur FROM Utilisateur WHERE nom_utilisateur = 'client_alice'), 'en cours de préparation');

-- Insertion de données dans la table Ligne_Commande
INSERT INTO Ligne_Commande (id_commande, id_cocktail, id_taille, quantite, statut_cocktail_preparation) VALUES
(1, (SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Mojito'), (SELECT id_taille FROM Taille WHERE nom_taille = 'M'), 2, 'Préparation des Ingrédients'),
(1, (SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Pina Colada'), (SELECT id_taille FROM Taille WHERE nom_taille = 'S'), 1, 'Préparation des Ingrédients'),
(2, (SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Bloody Mary'), (SELECT id_taille FROM Taille WHERE nom_taille = 'M'), 1, 'Préparation des Ingrédients'),
(2, (SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Virgin Mojito'), (SELECT id_taille FROM Taille WHERE nom_taille = 'M'), 1, 'Préparation des Ingrédients'),
(3, (SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Espresso Martini'), (SELECT id_taille FROM Taille WHERE nom_taille = 'M'), 1, 'Assemblage');