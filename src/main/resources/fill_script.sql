CREATE EXTENSION IF NOT EXISTS pgcrypto;

INSERT INTO PUPITRE (id, nom)
VALUES (1, 'Trompette'),
       (2, 'Trombone'),
       (3, 'Saxophone'),
       (4, 'Percussions'),
       (5, 'Clarinette');

INSERT INTO GROUPE (id, nom)
VALUES (1, 'Commission prestation'),
       (2, 'Commission artistique'),
       (3, 'Commission communication');

INSERT INTO EVENEMENT (id, type, nom, date, duree, lieu, description)
VALUES (1, 'Concert', 'Fête de la musique', '2026-06-21 20:00:00', 120, 'Lyon Centre', 'Concert en plein air'),
       (2, 'Répétition', 'Répétition hebdo', '2026-05-10 19:00:00', 90, 'Salle municipale', 'Répétition générale'),
       (3, 'Parade', 'Carnaval', '2026-07-14 15:00:00', 180, 'Villeurbanne', 'Défilé festif');

INSERT INTO HACHAGE (id_mdp, cle)
VALUES (1, crypt('test123', gen_salt('bf'))),
       (2, crypt('test123', gen_salt('bf'))),
       (3, crypt('test123', gen_salt('bf'))),
       (4, crypt('test123', gen_salt('bf'))),
       (5, crypt('test123', gen_salt('bf')));

INSERT INTO FANFARON (id_technique, identifiant, email, prenom, nom, genre,
                      contrainte_Alimentaire, date_Creation, date_Derniere_Connexion,
                      est_Admin, id_mdp)
VALUES (1, 'amartin', 'amartin@mail.com', 'Alice', 'Martin', 'F',
        'Aucune', NOW(), NOW(), TRUE, 1),

       (2, 'bdurand', 'bdurand@mail.com', 'Bob', 'Durand', 'M',
        'Végétarien', NOW(), NOW(), FALSE, 2),

       (3, 'clemoine', 'clemoine@mail.com', 'Charlie', 'Lemoine', 'M',
        'Sans gluten', NOW(), NOW(), FALSE, 3),

       (4, 'dmorel', 'dmorel@mail.com', 'Diane', 'Morel', 'F',
        NULL, NOW(), NOW(), FALSE, 4),

       (5, 'epetit', 'epetit@mail.com', 'Emma', 'Petit', 'F',
        'Vegan', NOW(), NOW(), FALSE, 5);

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