-- IMPORTANT : import.sql ne supporte PAS les commentaires multi-lignes
-- et doit avoir UN statement par ligne (sans point-virgule sauf à la fin)




-- Catégories INCOME
INSERT INTO categories (name, description, type, color, icon, is_default, active, created_at, user_id) VALUES ('Salaire', 'Revenu mensuel du travail', 'INCOME', '#4CAF50', 'briefcase', true, true, NOW(), NULL);
INSERT INTO categories (name, description, type, color, icon, is_default, active, created_at, user_id) VALUES ('Freelance', 'Revenus de missions freelance', 'INCOME', '#2196F3', 'laptop', true, true, NOW(), NULL);
INSERT INTO categories (name, description, type, color, icon, is_default, active, created_at, user_id) VALUES ('Investissement', 'Dividendes et plus-values', 'INCOME', '#FF9800', 'trending-up', true, true, NOW(), NULL);
INSERT INTO categories (name, description, type, color, icon, is_default, active, created_at, user_id) VALUES ('Cadeau', 'Argent reçu en cadeau', 'INCOME', '#E91E63', 'gift', true, true, NOW(), NULL);
INSERT INTO categories (name, description, type, color, icon, is_default, active, created_at, user_id) VALUES ('Autre revenu', 'Autres sources de revenus', 'INCOME', '#9C27B0', 'plus-circle', true, true, NOW(), NULL);

-- Catégories EXPENSE
INSERT INTO categories (name, description, type, color, icon, is_default, active, created_at, user_id) VALUES ('Alimentation', 'Courses et restaurants', 'EXPENSE', '#F44336', 'shopping-cart', true, true, NOW(), NULL);
INSERT INTO categories (name, description, type, color, icon, is_default, active, created_at, user_id) VALUES ('Transport', 'Essence, transports en commun', 'EXPENSE', '#3F51B5', 'car', true, true, NOW(), NULL);
INSERT INTO categories (name, description, type, color, icon, is_default, active, created_at, user_id) VALUES ('Logement', 'Loyer, prêt immobilier', 'EXPENSE', '#795548', 'home', true, true, NOW(), NULL);
INSERT INTO categories (name, description, type, color, icon, is_default, active, created_at, user_id) VALUES ('Factures', 'Électricité, eau, internet', 'EXPENSE', '#607D8B', 'file-text', true, true, NOW(), NULL);
INSERT INTO categories (name, description, type, color, icon, is_default, active, created_at, user_id) VALUES ('Divertissement', 'Cinéma, concerts, loisirs', 'EXPENSE', '#FF5722', 'film', true, true, NOW(), NULL);
INSERT INTO categories (name, description, type, color, icon, is_default, active, created_at, user_id) VALUES ('Santé', 'Médecin, pharmacie, sport', 'EXPENSE', '#00BCD4', 'heart', true, true, NOW(), NULL);
INSERT INTO categories (name, description, type, color, icon, is_default, active, created_at, user_id) VALUES ('Shopping', 'Vêtements, accessoires', 'EXPENSE', '#E91E63', 'shopping-bag', true, true, NOW(), NULL);
INSERT INTO categories (name, description, type, color, icon, is_default, active, created_at, user_id) VALUES ('Éducation', 'Formation, livres, cours', 'EXPENSE', '#673AB7', 'book', true, true, NOW(), NULL);
INSERT INTO categories (name, description, type, color, icon, is_default, active, created_at, user_id) VALUES ('Voyage', 'Vacances, week-ends', 'EXPENSE', '#009688', 'map-pin', true, true, NOW(), NULL);
INSERT INTO categories (name, description, type, color, icon, is_default, active, created_at, user_id) VALUES ('Assurance', 'Assurances diverses', 'EXPENSE', '#5E35B1', 'shield', true, true, NOW(), NULL);
INSERT INTO categories (name, description, type, color, icon, is_default, active, created_at, user_id) VALUES ('Abonnement', 'Netflix, Spotify, etc.', 'EXPENSE', '#00897B', 'refresh-cw', true, true, NOW(), NULL);
INSERT INTO categories (name, description, type, color, icon, is_default, active, created_at, user_id) VALUES ('Autre dépense', 'Autres dépenses', 'EXPENSE', '#78909C', 'more-horizontal', true, true, NOW(), NULL);