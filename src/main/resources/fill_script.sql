CREATE EXTENSION IF NOT EXISTS pgcrypto;

INSERT INTO PUPITRE (nom)
VALUES ('Trompette'),
       ('Trombone'),
       ('Saxophone'),
       ('Percussions'),
       ('Clarinette');

INSERT INTO GROUPE (nom)
VALUES ('Commission prestation'),
       ('Commission artistique'),
       ('Commission communication');

INSERT INTO EVENEMENT (type, nom, date, duree, lieu, description)
VALUES ('Concert',    'Fête de la musique', '2026-06-21 20:00:00', 120, 'Lyon Centre',       'Concert en plein air'),
       ('Répétition', 'Répétition hebdo',   '2026-05-10 19:00:00', 90,  'Salle municipale',  'Répétition générale'),
       ('Parade',     'Carnaval',            '2026-07-14 15:00:00', 180, 'Villeurbanne',      'Défilé festif');

INSERT INTO HACHAGE (cle)
VALUES (crypt('test123', gen_salt('bf'))),
       (crypt('test123', gen_salt('bf'))),
       (crypt('test123', gen_salt('bf'))),
       (crypt('test123', gen_salt('bf'))),
       (crypt('test123', gen_salt('bf')));

INSERT INTO FANFARON (identifiant, email, prenom, nom, genre,
                      contrainte_Alimentaire, date_Creation, date_Derniere_Connexion,
                      est_Admin, id_mdp)
VALUES ('amartin',  'amartin@mail.com',  'Alice',   'Martin',  'F', 'Aucune',      NOW(), NOW(), TRUE,  1),
       ('bdurand',  'bdurand@mail.com',  'Bob',     'Durand',  'M', 'Végétarien',  NOW(), NOW(), FALSE, 2),
       ('clemoine', 'clemoine@mail.com', 'Charlie', 'Lemoine', 'M', 'Sans gluten', NOW(), NOW(), FALSE, 3),
       ('dmorel',   'dmorel@mail.com',   'Diane',   'Morel',   'F', NULL,          NOW(), NOW(), FALSE, 4),
       ('epetit',   'epetit@mail.com',   'Emma',    'Petit',   'F', 'Vegan',       NOW(), NOW(), FALSE, 5);

-- Tables d'association : FK hardcodées, inchangées
INSERT INTO PARTICIPER (id_technique, id_groupe)
VALUES (1, 1),
       (2, 1),
       (3, 2),
       (4, 3),
       (5, 2);

INSERT INTO INSCRIRE (id_technique, id_evenement, id_pupitre, statut)
VALUES (1, 1, 1, 'Confirmé'),
       (2, 1, 2, 'Confirmé'),
       (3, 2, 3, 'En attente'),
       (4, 2, 4, 'Confirmé'),
       (5, 3, 1, 'Annulé');