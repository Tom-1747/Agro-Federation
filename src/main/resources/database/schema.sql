-- ============================================================
--  SCHÉMA OPTIMISÉ EN 12 TABLES - PostgreSQL
--  Fédération de collectivités agricoles - Madagascar
-- ============================================================

-- Types ENUM PostgreSQL (déclarés en amont)
CREATE TYPE genre_type       AS ENUM ('Féminin', 'Masculin');
CREATE TYPE type_compte_type AS ENUM ('Caisse', 'Bancaire', 'Mobile Money');
CREATE TYPE nom_banque_type  AS ENUM ('BRED','MCB','BMOI','BOA','BGFI','AFG','ACCÈS BANQUE','BAOBAB','SIPEM');
CREATE TYPE mobile_money_type AS ENUM ('Orange Money','Mvola','Airtel Money');
CREATE TYPE libelle_poste_type AS ENUM (
    'Président',
    'Président adjoint',
    'Trésorier',
    'Secrétaire',
    'Membre confirmé',
    'Membre junior'
    );
CREATE TYPE type_cotisation_type AS ENUM ('Périodique', 'Ponctuelle');
CREATE TYPE periodicite_type     AS ENUM ('Mensuelle', 'Annuelle');
CREATE TYPE mode_paiement_type   AS ENUM ('Espèces', 'Virement bancaire', 'Mobile Money');
CREATE TYPE type_activite_type   AS ENUM (
    'Assemblée générale mensuelle',
    'Formation obligatoire juniors',
    'Activité exceptionnelle',
    'Activité fédération'
    );
CREATE TYPE cible_membres_type AS ENUM ('Tous', 'Juniors uniquement', 'Confirmés uniquement');

-- ============================================================

-- 1. SPECIALITE_AGRICOLE
CREATE TABLE specialite_agricole (
                                     id_specialite   SERIAL PRIMARY KEY,
                                     nom             VARCHAR(100) NOT NULL,
                                     filiere         VARCHAR(100)
);

-- 2. METIER_AGRICOLE
CREATE TABLE metier_agricole (
                                 id_metier   SERIAL PRIMARY KEY,
                                 libelle     VARCHAR(100) NOT NULL
);

-- 3. BRANCHE_PROVINCIALE
CREATE TABLE branche_provinciale (
                                     id_branche      SERIAL PRIMARY KEY,
                                     province        VARCHAR(100) NOT NULL,
                                     ville_chef_lieu VARCHAR(100) NOT NULL,
                                     adresse         VARCHAR(200)
);

-- 4. FEDERATION
--    id_president ajouté après création de membre
CREATE TABLE federation (
                            id_federation       SERIAL PRIMARY KEY,
                            nom                 VARCHAR(100) NOT NULL,
                            adresse_siege       VARCHAR(200),
                            email               VARCHAR(100),
                            telephone           VARCHAR(20),
                            id_president        INT,
                            annee_debut_mandat  INT,
                            annee_fin_mandat    INT
);

-- 5. COLLECTIF
--    id_president ajouté après création de membre
CREATE TABLE collectif (
                           id_collectif        SERIAL PRIMARY KEY,
                           id_federation       INT NOT NULL,
                           id_specialite       INT,
                           id_branche          INT,
                           nom                 VARCHAR(100) NOT NULL UNIQUE,
                           lieu_exercice       VARCHAR(100) NOT NULL,
                           telephone           VARCHAR(20),
                           date_creation       DATE NOT NULL,
                           id_president        INT,
                           CONSTRAINT fk_collectif_federation  FOREIGN KEY (id_federation) REFERENCES federation(id_federation),
                           CONSTRAINT fk_collectif_specialite  FOREIGN KEY (id_specialite) REFERENCES specialite_agricole(id_specialite),
                           CONSTRAINT fk_collectif_branche     FOREIGN KEY (id_branche)    REFERENCES branche_provinciale(id_branche)
);

-- 6. MEMBRE
CREATE TABLE membre (
                        id_membre           SERIAL PRIMARY KEY,
                        id_collectif        INT NOT NULL,
                        id_metier           INT,
                        nom                 VARCHAR(100) NOT NULL,
                        prenom              VARCHAR(100) NOT NULL,
                        date_naissance      DATE,
                        genre               genre_type NOT NULL,
                        adresse             VARCHAR(200),
                        telephone           VARCHAR(20),
                        email               VARCHAR(100),
                        date_adhesion       DATE NOT NULL,
                        est_demissionne     BOOLEAN DEFAULT FALSE,
                        CONSTRAINT fk_membre_collectif FOREIGN KEY (id_collectif) REFERENCES collectif(id_collectif),
                        CONSTRAINT fk_membre_metier    FOREIGN KEY (id_metier)    REFERENCES metier_agricole(id_metier)
);

-- Clés étrangères auto-référencées (après création de membre)
ALTER TABLE federation
    ADD CONSTRAINT fk_federation_president FOREIGN KEY (id_president) REFERENCES membre(id_membre);

ALTER TABLE collectif
    ADD CONSTRAINT fk_collectif_president FOREIGN KEY (id_president) REFERENCES membre(id_membre);

-- 7. POSTE_MANDAT
CREATE TABLE poste_mandat (
                              id_poste_mandat     SERIAL PRIMARY KEY,
                              id_membre           INT NOT NULL,
                              id_collectif        INT,
                              id_federation       INT,
                              libelle_poste       libelle_poste_type NOT NULL,
                              est_unique          BOOLEAN NOT NULL DEFAULT TRUE,
                              est_electif         BOOLEAN NOT NULL DEFAULT TRUE,
                              annee_civile        INT NOT NULL,
                              date_debut          DATE NOT NULL,
                              date_fin            DATE,
                              nb_mandats_cumules  INT DEFAULT 1,
                              CONSTRAINT fk_poste_membre     FOREIGN KEY (id_membre)     REFERENCES membre(id_membre),
                              CONSTRAINT fk_poste_collectif  FOREIGN KEY (id_collectif)  REFERENCES collectif(id_collectif),
                              CONSTRAINT fk_poste_federation FOREIGN KEY (id_federation) REFERENCES federation(id_federation),
    -- Un poste unique ne peut être occupé que par un seul membre par an dans un collectif
                              CONSTRAINT uk_poste_unique UNIQUE (id_collectif, libelle_poste, annee_civile)
);

-- 8. COMPTE
CREATE TABLE compte (
                        id_compte               SERIAL PRIMARY KEY,
                        id_collectif            INT,
                        id_federation           INT,
                        type_compte             type_compte_type NOT NULL,
                        titulaire_compte        VARCHAR(100),
                        nom_banque              nom_banque_type,
                        service_mob_money       mobile_money_type,
                        numero_compte_banque    VARCHAR(23),
                        numero_telephone        VARCHAR(20),
                        solde                   NUMERIC(15,2) DEFAULT 0.00,
                        CONSTRAINT fk_compte_collectif  FOREIGN KEY (id_collectif)  REFERENCES collectif(id_collectif),
                        CONSTRAINT fk_compte_federation FOREIGN KEY (id_federation) REFERENCES federation(id_federation)
);

-- 9. COTISATION
CREATE TABLE cotisation (
                            id_cotisation       SERIAL PRIMARY KEY,
                            id_membre           INT NOT NULL,
                            id_collectif        INT NOT NULL,
                            type_cotisation     type_cotisation_type NOT NULL,
                            periodicite         periodicite_type,
                            montant             NUMERIC(15,2) NOT NULL,
                            date_echeance       DATE NOT NULL,
                            est_payee           BOOLEAN DEFAULT FALSE,
                            CONSTRAINT fk_cotisation_membre    FOREIGN KEY (id_membre)    REFERENCES membre(id_membre),
                            CONSTRAINT fk_cotisation_collectif FOREIGN KEY (id_collectif) REFERENCES collectif(id_collectif)
);

-- 10. ENCAISSEMENT
CREATE TABLE encaissement (
                              id_encaissement             SERIAL PRIMARY KEY,
                              id_cotisation               INT NOT NULL,
                              id_compte                   INT NOT NULL,
                              montant                     NUMERIC(15,2) NOT NULL,
                              date_encaissement           DATE NOT NULL,
                              mode_paiement               mode_paiement_type NOT NULL,
                              part_versement_federation   NUMERIC(15,2) DEFAULT 0.00,
                              CONSTRAINT fk_encaissement_cotisation FOREIGN KEY (id_cotisation) REFERENCES cotisation(id_cotisation),
                              CONSTRAINT fk_encaissement_compte     FOREIGN KEY (id_compte)     REFERENCES compte(id_compte)
);

-- 11. ACTIVITE
CREATE TABLE activite (
                          id_activite             SERIAL PRIMARY KEY,
                          id_collectif            INT,
                          id_federation           INT,
                          titre                   VARCHAR(200) NOT NULL,
                          type_activite           type_activite_type NOT NULL,
                          date_activite           DATE NOT NULL,
                          presence_obligatoire    BOOLEAN DEFAULT TRUE,
                          cible_membres           cible_membres_type DEFAULT 'Tous',
                          est_federation          BOOLEAN DEFAULT FALSE,
                          CONSTRAINT fk_activite_collectif  FOREIGN KEY (id_collectif)  REFERENCES collectif(id_collectif),
                          CONSTRAINT fk_activite_federation FOREIGN KEY (id_federation) REFERENCES federation(id_federation)
);

-- 12. PRESENCE
CREATE TABLE presence (
                          id_presence             SERIAL PRIMARY KEY,
                          id_activite             INT NOT NULL,
                          id_membre               INT NOT NULL,
                          id_collectif_membre     INT NOT NULL,
                          est_present             BOOLEAN DEFAULT FALSE,
                          est_excuse              BOOLEAN DEFAULT FALSE,
                          motif_absence           VARCHAR(300),
                          mois_concerne           DATE,
                          taux_assiduite_global   NUMERIC(5,2),
                          nb_membres_actifs       INT,
                          date_rapport            DATE,
                          CONSTRAINT fk_presence_activite  FOREIGN KEY (id_activite)         REFERENCES activite(id_activite),
                          CONSTRAINT fk_presence_membre    FOREIGN KEY (id_membre)           REFERENCES membre(id_membre),
                          CONSTRAINT fk_presence_collectif FOREIGN KEY (id_collectif_membre) REFERENCES collectif(id_collectif)
);

-- ============================================================
--  INDEX
-- ============================================================
CREATE INDEX idx_membre_collectif       ON membre(id_collectif);
CREATE INDEX idx_membre_metier          ON membre(id_metier);
CREATE INDEX idx_collectif_specialite   ON collectif(id_specialite);
CREATE INDEX idx_collectif_branche      ON collectif(id_branche);
CREATE INDEX idx_poste_membre           ON poste_mandat(id_membre);
CREATE INDEX idx_poste_collectif        ON poste_mandat(id_collectif);
CREATE INDEX idx_cotisation_membre      ON cotisation(id_membre);
CREATE INDEX idx_encaissement_compte    ON encaissement(id_compte);
CREATE INDEX idx_presence_activite      ON presence(id_activite);
CREATE INDEX idx_presence_membre        ON presence(id_membre);
CREATE INDEX idx_activite_collectif     ON activite(id_collectif);
CREATE INDEX idx_compte_collectif       ON compte(id_collectif);