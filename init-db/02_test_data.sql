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
('Cerise'),
('Gin'),
('Tonic'),
('Concombre'),
('Tequila'),
('Triple Sec'),
('Citron Jaune');

-- Insertion de données dans la table Taille
INSERT INTO Taille (nom_taille) VALUES
('S'),
('M'),
('L');

-- Insertion de données dans la table Utilisateur
INSERT INTO Utilisateur (email_utilisateur, mot_de_passe, role_utilisateur) VALUES
('barmaker@bar.com', '$2a$10$9hWkl3KlgI5J8Xy7KwCOXO5X1AYZHFh8OKY3Csl9kGNHzHwKEHhty', 'Barmaker'),
('client1@bar.com', '$2a$10$9hWkl3KlgI5J8Xy7KwCOXO5X1AYZHFh8OKY3Csl9kGNHzHwKEHhty', 'Client'),
('client2@bar.com', '$2a$10$9hWkl3KlgI5J8Xy7KwCOXO5X1AYZHFh8OKY3Csl9kGNHzHwKEHhty', 'Client');

-- Insertion de données dans la table Cocktail
INSERT INTO Cocktail (nom_cocktail, description_cocktail, imgUrl, id_categorie, id_createur_utilisateur) VALUES
('Mojito', 'Un cocktail rafraîchissant à base de rhum et de menthe.', 'https://images.unsplash.com/photo-1551024709-8f23befc6f87?w=500&fit=crop', (SELECT id_categorie FROM Categorie WHERE nom_categorie = 'Classique'), (SELECT id_utilisateur FROM Utilisateur WHERE email_utilisateur = 'barmaker@bar.com')),
('Pina Colada', 'Un cocktail tropical crémeux à base de rhum, ananas et coco.', 'https://images.unsplash.com/photo-1570598912132-0ba1dc952b7d?w=500&fit=crop', (SELECT id_categorie FROM Categorie WHERE nom_categorie = 'Tropical'), (SELECT id_utilisateur FROM Utilisateur WHERE email_utilisateur = 'barmaker@bar.com')),
('Sex on the Beach', 'Un cocktail fruité et coloré.', 'https://images.unsplash.com/photo-1541546006121-5c3bc5e8c7b9?w=500&fit=crop', (SELECT id_categorie FROM Categorie WHERE nom_categorie = 'Classique'), (SELECT id_utilisateur FROM Utilisateur WHERE email_utilisateur = 'barmaker@bar.com')),
('Virgin Mojito', 'La version sans alcool du célèbre Mojito.', 'https://images.unsplash.com/photo-1546171753-97d7676e4602?w=500&fit=crop', (SELECT id_categorie FROM Categorie WHERE nom_categorie = 'Sans Alcool'), (SELECT id_utilisateur FROM Utilisateur WHERE email_utilisateur = 'barmaker@bar.com')),
('Gin Tonic Concombre', 'Un gin tonic rafraîchissant avec une touche de concombre.', 'https://images.unsplash.com/photo-1556679343-c7306c1976bc?w=500&fit=crop', (SELECT id_categorie FROM Categorie WHERE nom_categorie = 'Classique'), (SELECT id_utilisateur FROM Utilisateur WHERE email_utilisateur = 'barmaker@bar.com')),
('Margarita', 'Le cocktail mexicain par excellence, parfait équilibre entre la tequila et les agrumes.', 'https://images.unsplash.com/photo-1556855810-ac404aa91e85?w=500&fit=crop', (SELECT id_categorie FROM Categorie WHERE nom_categorie = 'Classique'), (SELECT id_utilisateur FROM Utilisateur WHERE email_utilisateur = 'barmaker@bar.com'));

-- Insertion de données dans la table Cocktail_Ingredient
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
((SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Virgin Mojito'), (SELECT id_ingredient FROM Ingredient WHERE nom_ingredient = 'Glaçons'), 1.0, 'dose'),

((SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Gin Tonic Concombre'), (SELECT id_ingredient FROM Ingredient WHERE nom_ingredient = 'Gin'), 50.0, 'ml'),
((SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Gin Tonic Concombre'), (SELECT id_ingredient FROM Ingredient WHERE nom_ingredient = 'Tonic'), 100.0, 'ml'),
((SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Gin Tonic Concombre'), (SELECT id_ingredient FROM Ingredient WHERE nom_ingredient = 'Concombre'), 3.0, 'tranches'),
((SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Gin Tonic Concombre'), (SELECT id_ingredient FROM Ingredient WHERE nom_ingredient = 'Glaçons'), 1.0, 'dose'),

((SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Margarita'), (SELECT id_ingredient FROM Ingredient WHERE nom_ingredient = 'Tequila'), 50.0, 'ml'),
((SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Margarita'), (SELECT id_ingredient FROM Ingredient WHERE nom_ingredient = 'Triple Sec'), 25.0, 'ml'),
((SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Margarita'), (SELECT id_ingredient FROM Ingredient WHERE nom_ingredient = 'Citron Jaune'), 25.0, 'ml'),
((SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Margarita'), (SELECT id_ingredient FROM Ingredient WHERE nom_ingredient = 'Glaçons'), 1.0, 'dose');

-- Insertion de données dans la table Cocktail_Taille_Prix
INSERT INTO Cocktail_Taille_Prix (id_cocktail, id_taille, prix) VALUES
((SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Mojito'), (SELECT id_taille FROM Taille WHERE nom_taille = 'M'), 8.50),
((SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Mojito'), (SELECT id_taille FROM Taille WHERE nom_taille = 'L'), 11.00),
((SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Pina Colada'), (SELECT id_taille FROM Taille WHERE nom_taille = 'M'), 9.00),
((SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Pina Colada'), (SELECT id_taille FROM Taille WHERE nom_taille = 'L'), 12.00),
((SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Sex on the Beach'), (SELECT id_taille FROM Taille WHERE nom_taille = 'M'), 8.00),
((SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Virgin Mojito'), (SELECT id_taille FROM Taille WHERE nom_taille = 'S'), 6.00),
((SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Virgin Mojito'), (SELECT id_taille FROM Taille WHERE nom_taille = 'M'), 7.50),
((SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Gin Tonic Concombre'), (SELECT id_taille FROM Taille WHERE nom_taille = 'M'), 9.00),
((SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Gin Tonic Concombre'), (SELECT id_taille FROM Taille WHERE nom_taille = 'L'), 12.00),
((SELECT id_cocktail FROM Cocktail WHERE nom_cocktail = 'Margarita'), (SELECT id_taille FROM Taille WHERE nom_taille = 'M'), 9.50);
