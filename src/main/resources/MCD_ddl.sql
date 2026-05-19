CREATE SEQUENCE seq_pupitre     START 1 INCREMENT 1;
CREATE SEQUENCE seq_evenement   START 1 INCREMENT 1;
CREATE SEQUENCE seq_groupe      START 1 INCREMENT 1;
CREATE SEQUENCE seq_hachage     START 1 INCREMENT 1;
CREATE SEQUENCE seq_fanfaron    START 1 INCREMENT 1;

CREATE TABLE PUPITRE
(
    id  INTEGER NOT NULL DEFAULT nextval('seq_pupitre'),
    nom VARCHAR(100),
    PRIMARY KEY (id)
);
ALTER SEQUENCE seq_pupitre OWNED BY PUPITRE.id;

CREATE TABLE EVENEMENT
(
    id          INTEGER NOT NULL DEFAULT nextval('seq_evenement'),
    type        VARCHAR(50),
    nom         VARCHAR(100),
    date        TIMESTAMP,
    duree       INTEGER, -- en minutes
    lieu        VARCHAR(100),
    description VARCHAR(255),
    PRIMARY KEY (id)
);
ALTER SEQUENCE seq_evenement OWNED BY EVENEMENT.id;

CREATE TABLE GROUPE
(
    id  INTEGER NOT NULL DEFAULT nextval('seq_groupe'),
    nom VARCHAR(100),
    PRIMARY KEY (id)
);
ALTER SEQUENCE seq_groupe OWNED BY GROUPE.id;

CREATE TABLE HACHAGE
(
    id_mdp INTEGER NOT NULL DEFAULT nextval('seq_hachage'),
    cle    VARCHAR(255),
    PRIMARY KEY (id_mdp)
);
ALTER SEQUENCE seq_hachage OWNED BY HACHAGE.id_mdp;

CREATE TABLE FANFARON
(
    id_technique            INTEGER NOT NULL DEFAULT nextval('seq_fanfaron'),
    identifiant             VARCHAR(42) UNIQUE,
    email                   VARCHAR(255) UNIQUE,
    prenom                  VARCHAR(42),
    nom                     VARCHAR(42),
    genre                   VARCHAR(20),
    contrainte_Alimentaire  VARCHAR(100),
    date_Creation           TIMESTAMP,
    date_Derniere_Connexion TIMESTAMP,
    est_Admin               BOOLEAN DEFAULT FALSE,
    id_mdp                  INTEGER NOT NULL,
    PRIMARY KEY (id_technique),
    FOREIGN KEY (id_mdp) REFERENCES HACHAGE (id_mdp) ON DELETE CASCADE
);
ALTER SEQUENCE seq_fanfaron OWNED BY FANFARON.id_technique;

CREATE TABLE APPARTENIR
(
    id_technique INTEGER NOT NULL,
    id_pupitre   INTEGER NOT NULL,
    PRIMARY KEY (id_technique, id_pupitre),
    FOREIGN KEY (id_technique) REFERENCES FANFARON (id_technique) ON DELETE CASCADE,
    FOREIGN KEY (id_pupitre) REFERENCES PUPITRE (id) ON DELETE CASCADE
);

CREATE TABLE INSCRIRE
(
    id_technique INTEGER NOT NULL,
    id_evenement INTEGER NOT NULL,
    id_pupitre   INTEGER NOT NULL,
    statut       VARCHAR(50),
    PRIMARY KEY (id_technique, id_evenement, id_pupitre),
    FOREIGN KEY (id_technique) REFERENCES FANFARON (id_technique) ON DELETE CASCADE,
    FOREIGN KEY (id_evenement) REFERENCES EVENEMENT (id) ON DELETE CASCADE,
    FOREIGN KEY (id_pupitre) REFERENCES PUPITRE (id) ON DELETE CASCADE
);

CREATE TABLE PARTICIPER
(
    id_technique INTEGER NOT NULL,
    id_groupe    INTEGER NOT NULL,
    PRIMARY KEY (id_technique, id_groupe),
    FOREIGN KEY (id_technique) REFERENCES FANFARON (id_technique) ON DELETE CASCADE,
    FOREIGN KEY (id_groupe) REFERENCES GROUPE (id) ON DELETE CASCADE
);

-- droits webuser
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO webuser;

ALTER DEFAULT PRIVILEGES IN SCHEMA public
    GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO webuser;

GRANT USAGE, SELECT, UPDATE ON ALL SEQUENCES IN SCHEMA public TO webuser;

ALTER DEFAULT PRIVILEGES IN SCHEMA public
    GRANT USAGE, SELECT, UPDATE ON SEQUENCES TO webuser;