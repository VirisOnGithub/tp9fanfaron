# Projet TP9

Auteurs : 
- Clément RENIERS
- Louison PARANT

## Création des tables avant de lancer le projet

- Créer une base nommée `tp9` avec l'user root
- Créer l'utilisateur `webuser` avec le mot de passe `webpass` (infos modifiables dans le `DbConnectionManager`)
- Donner les droits à l'utilisateur `webuser` sur la base `tp9`

```sql
-- dans la base tp9
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO webuser;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO webuser;
ALTER DEFAULT PRIVILEGES IN SCHEMA public
    GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES    TO webuser;
ALTER DEFAULT PRIVILEGES IN SCHEMA public
    GRANT USAGE, SELECT ON SEQUENCES TO webuser;
```
- Exécuter le fichier `MCD_ddl.sql` pour créer les tables
- Exécuter le fichier `fill_script.sql` pour remplir les tables avec des données de test

## Lancement du projet

Lancer le projet avec la fonction d'Intellij pour lancer un serveur Tomcat (verCREATE USER webuser WITH PASSWORD 'webpass';sion 11.0.18 utilisée pour le développement)