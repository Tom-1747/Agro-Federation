

CREATE TYPE gender_type AS ENUM (
    'Female',
    'Male'
    );

CREATE TYPE account_type_type AS ENUM (
    'Cash',
    'Bank',
    'Mobile_Money'
    );

CREATE TYPE bank_name_type AS ENUM (
    'BRED', 'MCB', 'BMOI', 'BOA', 'BGFI',
    'AFG', 'ACCES_BANQUE', 'BAOBAB', 'SIPEM'
    );

CREATE TYPE mobile_money_type AS ENUM (
    'Orange_Money',
    'Mvola',
    'Airtel_Money'
    );

CREATE TYPE payment_method_type AS ENUM (
    'Cash',
    'Bank_Transfer',
    'Mobile_Money'
    );

CREATE TYPE contribution_type_type AS ENUM (
    'Periodic',
    'One_time'
    );

CREATE TYPE frequency_type AS ENUM (
    'Monthly',
    'Annual'
    );

CREATE TYPE activity_type_type AS ENUM (
    'Monthly_General_Assembly',
    'Mandatory_Junior_Training',
    'Exceptional_Activity',
    'Federation_Activity'
    );

CREATE TYPE target_members_type AS ENUM (
    'All',
    'Juniors_Only',
    'Confirmed_Only'
    );

CREATE TYPE position_label_type AS ENUM (
    'President',
    'Vice_President',
    'Treasurer',
    'Secretary',
    'Confirmed_Member',
    'Junior_Member'
    );


CREATE TABLE agricultural_specialty (
                                        id_specialty  SERIAL PRIMARY KEY,
                                        name          VARCHAR(100) NOT NULL,
                                        sector        VARCHAR(100) NOT NULL
);

CREATE TABLE agricultural_trade (
                                    id_trade  SERIAL PRIMARY KEY,
                                    label     VARCHAR(100) NOT NULL
);

CREATE TABLE provincial_branch (
                                   id_branch    SERIAL PRIMARY KEY,
                                   province     VARCHAR(100) NOT NULL,
                                   capital_city VARCHAR(100),
                                   address      VARCHAR(255)
);


CREATE TABLE federation (
                            id_federation       SERIAL PRIMARY KEY,
                            name                VARCHAR(200) NOT NULL,
                            headquarters        VARCHAR(255),
                            email               VARCHAR(150),
                            phone               VARCHAR(20),
                            mandate_start_year  INTEGER,
                            mandate_end_year    INTEGER,
                            president_id        INTEGER  -- FK vers member, ajoutée après
);


CREATE TABLE collective (
                            id_collective  SERIAL PRIMARY KEY,
                            id_federation  INTEGER NOT NULL REFERENCES federation(id_federation),
                            id_specialty   INTEGER          REFERENCES agricultural_specialty(id_specialty),
                            id_branch      INTEGER          REFERENCES provincial_branch(id_branch),
                            name           VARCHAR(200) NOT NULL,
                            location       VARCHAR(255),
                            phone          VARCHAR(20),
                            creation_date  DATE NOT NULL DEFAULT CURRENT_DATE,
                            president_id   INTEGER  -- FK vers member, ajoutée après
);


CREATE TABLE member (
                        id_member      SERIAL PRIMARY KEY,
                        id_collective  INTEGER NOT NULL REFERENCES collective(id_collective),
                        id_trade       INTEGER          REFERENCES agricultural_trade(id_trade),
                        last_name      VARCHAR(100) NOT NULL,
                        first_name     VARCHAR(100) NOT NULL,
                        birth_date     DATE,
                        gender         gender_type,
                        address        VARCHAR(255),
                        phone          VARCHAR(20),
                        email          VARCHAR(150),
                        join_date      DATE NOT NULL DEFAULT CURRENT_DATE,
                        is_resigned    BOOLEAN NOT NULL DEFAULT FALSE
);

ALTER TABLE federation
    ADD CONSTRAINT fk_federation_president
        FOREIGN KEY (president_id) REFERENCES member(id_member)
            DEFERRABLE INITIALLY DEFERRED;

ALTER TABLE collective
    ADD CONSTRAINT fk_collective_president
        FOREIGN KEY (president_id) REFERENCES member(id_member)
            DEFERRABLE INITIALLY DEFERRED;


CREATE TABLE position_mandate (
                                  id_position_mandate  SERIAL PRIMARY KEY,
                                  id_member            INTEGER NOT NULL REFERENCES member(id_member),
                                  id_collective        INTEGER          REFERENCES collective(id_collective),
                                  id_federation        INTEGER          REFERENCES federation(id_federation),
                                  position_label       position_label_type NOT NULL,
                                  is_unique            BOOLEAN NOT NULL DEFAULT TRUE,
                                  is_elective          BOOLEAN NOT NULL DEFAULT TRUE,
                                  calendar_year        INTEGER NOT NULL,
                                  start_date           DATE,
                                  end_date             DATE,
                                  cumulated_mandates   INTEGER NOT NULL DEFAULT 1,

                                  CONSTRAINT chk_mandate_context
                                      CHECK (id_collective IS NOT NULL OR id_federation IS NOT NULL)
);


CREATE TABLE account (
                         id_account          SERIAL PRIMARY KEY,
                         id_collective       INTEGER REFERENCES collective(id_collective),
                         id_federation       INTEGER REFERENCES federation(id_federation),
                         account_type        account_type_type NOT NULL,
                         account_holder      VARCHAR(200),
                         bank_name           bank_name_type,
                         mobile_money_service mobile_money_type,
                         bank_account_number VARCHAR(50),
                         phone_number        VARCHAR(20),
                         balance             NUMERIC(15, 2) NOT NULL DEFAULT 0.00,
                         CONSTRAINT chk_account_owner
                             CHECK (id_collective IS NOT NULL OR id_federation IS NOT NULL)
);


CREATE TABLE contribution (
                              id_contribution    SERIAL PRIMARY KEY,
                              id_member          INTEGER          REFERENCES member(id_member),
                              id_collective      INTEGER NOT NULL REFERENCES collective(id_collective),
                              contribution_type  contribution_type_type NOT NULL,
                              frequency          frequency_type,
                              amount             NUMERIC(12, 2) NOT NULL,
                              due_date           DATE,
                              is_paid            BOOLEAN NOT NULL DEFAULT FALSE
);


CREATE TABLE membershipfees (
                                id_membershipfees  SERIAL PRIMARY KEY,
                                id_contribution    INTEGER NOT NULL REFERENCES contribution(id_contribution),
                                id_account         INTEGER NOT NULL REFERENCES account(id_account),
                                amount             NUMERIC(12, 2) NOT NULL,
                                membershipfees_date DATE NOT NULL DEFAULT CURRENT_DATE,
                                payment_method     payment_method_type NOT NULL,
                                federation_share   NUMERIC(12, 2) NOT NULL DEFAULT 0.00
);


CREATE TABLE collectivity_transaction (
                                          id_transaction  SERIAL PRIMARY KEY,
                                          id_collective   INTEGER NOT NULL REFERENCES collective(id_collective),
                                          id_member       INTEGER NOT NULL REFERENCES member(id_member),
                                          id_account      INTEGER NOT NULL REFERENCES account(id_account),
                                          amount          NUMERIC(12, 2) NOT NULL,
                                          payment_method  payment_method_type NOT NULL,
                                          creation_date   DATE NOT NULL DEFAULT CURRENT_DATE
);


CREATE TABLE activity (
                          id_activity          SERIAL PRIMARY KEY,
                          id_collective        INTEGER REFERENCES collective(id_collective),
                          id_federation        INTEGER REFERENCES federation(id_federation),
                          title                VARCHAR(200) NOT NULL,
                          activity_type        activity_type_type NOT NULL,
                          activity_date        DATE NOT NULL,
                          attendance_required  BOOLEAN NOT NULL DEFAULT TRUE,
                          target_members       target_members_type NOT NULL DEFAULT 'All',
                          is_federation        BOOLEAN NOT NULL DEFAULT FALSE,
                          created_at           TIMESTAMP DEFAULT NOW(),
                          updated_at           TIMESTAMP DEFAULT NOW(),
                          CONSTRAINT chk_activity_owner
                              CHECK (id_collective IS NOT NULL OR id_federation IS NOT NULL)
);


CREATE TABLE attendance (
                            id_attendance   SERIAL PRIMARY KEY,
                            id_activity     INTEGER NOT NULL REFERENCES activity(id_activity),
                            id_member       INTEGER NOT NULL REFERENCES member(id_member),
                            is_present      BOOLEAN NOT NULL DEFAULT FALSE,
                            is_excused      BOOLEAN NOT NULL DEFAULT FALSE,
                            absence_reason  TEXT,
                            recorded_at     TIMESTAMP DEFAULT NOW(),
                            updated_at      TIMESTAMP DEFAULT NOW(),
                            UNIQUE (id_activity, id_member)
);



-- member
CREATE INDEX idx_member_collective  ON member(id_collective);
CREATE INDEX idx_member_resigned    ON member(is_resigned);
CREATE INDEX idx_member_join_date   ON member(join_date);

-- position_mandate
CREATE INDEX idx_mandate_member     ON position_mandate(id_member);
CREATE INDEX idx_mandate_collective ON position_mandate(id_collective);
CREATE INDEX idx_mandate_year       ON position_mandate(calendar_year);

-- account
CREATE INDEX idx_account_collective ON account(id_collective);

-- contribution
CREATE INDEX idx_contribution_member     ON contribution(id_member);
CREATE INDEX idx_contribution_collective ON contribution(id_collective);

-- membershipfees
CREATE INDEX idx_fees_contribution  ON membershipfees(id_contribution);
CREATE INDEX idx_fees_account       ON membershipfees(id_account);
CREATE INDEX idx_fees_date          ON membershipfees(membershipfees_date);

-- activity
CREATE INDEX idx_activity_collective ON activity(id_collective);
CREATE INDEX idx_activity_date       ON activity(activity_date);

-- attendance
CREATE INDEX idx_attendance_activity ON attendance(id_activity);
CREATE INDEX idx_attendance_member   ON attendance(id_member);


CREATE OR REPLACE VIEW v_account_balance_by_collective AS
SELECT
    a.id_collective,
    a.id_account,
    a.account_type,
    a.account_holder,
    a.balance,
    COALESCE(SUM(mf.amount), 0) AS total_collected
FROM account a
         LEFT JOIN membershipfees mf ON mf.id_account = a.id_account
GROUP BY a.id_collective, a.id_account, a.account_type, a.account_holder, a.balance;

-- Vue : paiements membres enrichis
CREATE OR REPLACE VIEW v_member_payments AS
SELECT
    mf.id_membershipfees,
    mf.id_contribution,
    mf.id_account,
    mf.amount,
    mf.membershipfees_date,
    mf.payment_method,
    mf.federation_share,
    c.id_member,
    c.id_collective,
    c.contribution_type,
    c.frequency,
    m.last_name,
    m.first_name
FROM membershipfees mf
         JOIN contribution c ON mf.id_contribution = c.id_contribution
         JOIN member       m ON c.id_member        = m.id_member;

-- Vue : membres avec leur poste actuel (mandats actifs)
CREATE OR REPLACE VIEW v_member_positions AS
SELECT
    m.id_member,
    m.id_collective,
    m.last_name,
    m.first_name,
    m.join_date,
    m.is_resigned,
    pm.position_label,
    pm.calendar_year,
    pm.start_date,
    pm.end_date
FROM member m
         LEFT JOIN position_mandate pm ON pm.id_member = m.id_member
WHERE m.is_resigned = FALSE;

-- Vue : taux de présence par activité
CREATE OR REPLACE VIEW v_attendance_summary AS
SELECT
    a.id_activity,
    a.title,
    a.activity_date,
    a.activity_type,
    a.id_collective,
    COUNT(att.id_member)                                          AS total_expected,
    SUM(CASE WHEN att.is_present THEN 1 ELSE 0 END)              AS total_present,
    SUM(CASE WHEN att.is_excused THEN 1 ELSE 0 END)              AS total_excused,
    ROUND(
            100.0 * SUM(CASE WHEN att.is_present THEN 1 ELSE 0 END)
                / NULLIF(COUNT(att.id_member), 0),
            2
    )                                                             AS attendance_rate_pct
FROM activity a
         LEFT JOIN attendance att ON att.id_activity = a.id_activity
GROUP BY a.id_activity, a.title, a.activity_date, a.activity_type, a.id_collective;


-- ============================================================
-- 15. REQUÊTES CRUD PARAMÉTRÉES (style JDBC — ? = paramètre)
-- ============================================================

-- ────────────────────────────────────────────────────────────
-- 15.1 COLLECTIVE
-- ────────────────────────────────────────────────────────────

-- Insérer un collectif
INSERT INTO collective (id_federation, id_specialty, id_branch, name, location, phone, creation_date)
VALUES (?, ?, ?, ?, ?, ?, ?)
RETURNING id_collective;

-- Mettre à jour le nom d'un collectif
UPDATE collective SET name = ? WHERE id_collective = ?;

-- Trouver par ID
SELECT * FROM collective WHERE id_collective = ?;

-- Lister tous les collectifs
SELECT * FROM collective ORDER BY id_collective;

-- Compter les membres actifs d'un collectif
SELECT COUNT(*) FROM member WHERE id_collective = ? AND is_resigned = FALSE;

-- Compter les membres avec au moins 6 mois d'ancienneté
SELECT COUNT(*)
FROM member
WHERE id_collective = ?
  AND is_resigned = FALSE
  AND join_date <= CURRENT_DATE - INTERVAL '6 months';

-- Vérifier que les 4 postes obligatoires sont pourvus pour une année
SELECT COUNT(DISTINCT position_label)
FROM position_mandate
WHERE id_collective = ?
  AND calendar_year = ?
  AND position_label IN ('President', 'Vice_President', 'Treasurer', 'Secretary');
-- Retourne 4 si tous les postes sont pourvus

-- Vérifier l'existence d'un collectif par nom
SELECT COUNT(*) FROM collective WHERE name = ?;


-- ────────────────────────────────────────────────────────────
-- 15.2 MEMBER
-- ────────────────────────────────────────────────────────────

-- Insérer un nouveau membre
INSERT INTO member
(id_collective, id_trade, last_name, first_name, birth_date, gender, address, phone, email, join_date, is_resigned)
VALUES (?, ?, ?, ?, ?, ?::gender_type, ?, ?, ?, ?, FALSE)
RETURNING id_member;

-- Trouver un membre par ID
SELECT * FROM member WHERE id_member = ?;

-- Lister les membres actifs d'un collectif
SELECT * FROM member WHERE id_collective = ? AND is_resigned = FALSE ORDER BY id_member;

-- Trouver plusieurs membres par liste d'IDs (les ? sont générés dynamiquement)
SELECT * FROM member WHERE id_member IN (?, ?, ...) AND is_resigned = FALSE;

-- Vérifier si un membre a le label "Confirmed_Member"
SELECT COUNT(*)
FROM position_mandate
WHERE id_member = ?
  AND position_label = 'Confirmed_Member';
-- Retourne > 0 si confirmé

-- Vérifier l'ancienneté minimale en jours
SELECT COUNT(*)
FROM member
WHERE id_member = ?
  AND is_resigned = FALSE
  AND join_date <= CURRENT_DATE - (? * INTERVAL '1 day');
-- Retourne 1 si ancienneté suffisante

-- Démissionner un membre
UPDATE member SET is_resigned = TRUE WHERE id_member = ?;


-- ────────────────────────────────────────────────────────────
-- 15.3 ACCOUNT (FinancialAccount)
-- ────────────────────────────────────────────────────────────

-- Créer un compte Cash
INSERT INTO account (id_collective, account_type, account_holder, balance)
VALUES (?, 'Cash'::account_type_type, ?, ?)
RETURNING id_account;

-- Créer un compte Banque
INSERT INTO account (id_collective, account_type, account_holder, bank_name, bank_account_number, balance)
VALUES (?, 'Bank'::account_type_type, ?, ?::bank_name_type, ?, ?)
RETURNING id_account;

-- Créer un compte Mobile Money
INSERT INTO account (id_collective, account_type, account_holder, mobile_money_service, phone_number, balance)
VALUES (?, 'Mobile_Money'::account_type_type, ?, ?::mobile_money_type, ?, ?)
RETURNING id_account;

-- Trouver un compte par ID
SELECT * FROM account WHERE id_account = ?;

-- Lister les comptes d'un collectif
SELECT * FROM account WHERE id_collective = ? ORDER BY id_account;

-- Mettre à jour le solde
UPDATE account SET balance = ? WHERE id_account = ?;

-- Calculer le total encaissé sur un compte jusqu'à une date
SELECT COALESCE(SUM(amount), 0)
FROM membershipfees
WHERE id_account = ?
  AND membershipfees_date <= ?;


-- ────────────────────────────────────────────────────────────
-- 15.4 CONTRIBUTION
-- ────────────────────────────────────────────────────────────

-- Créer une définition de cotisation pour un collectif
INSERT INTO contribution (id_member, id_collective, contribution_type, frequency, amount, due_date, is_paid)
VALUES (?, ?, ?::contribution_type_type, ?::frequency_type, ?, ?, FALSE)
RETURNING id_contribution;

-- Trouver une cotisation par ID
SELECT * FROM contribution WHERE id_contribution = ?;

-- Lister les cotisations d'un collectif
SELECT * FROM contribution WHERE id_collective = ? ORDER BY id_contribution;

-- Marquer une cotisation comme payée
UPDATE contribution SET is_paid = TRUE WHERE id_contribution = ?;

-- Cotisations en retard d'un collectif
SELECT * FROM contribution
WHERE id_collective = ?
  AND is_paid = FALSE
  AND due_date < CURRENT_DATE
ORDER BY due_date;


-- ────────────────────────────────────────────────────────────
-- 15.5 MEMBERSHIPFEES (paiements de cotisations)
-- ────────────────────────────────────────────────────────────

-- Enregistrer un paiement de cotisation
INSERT INTO membershipfees (id_contribution, id_account, amount, membershipfees_date, payment_method, federation_share)
VALUES (?, ?, ?, ?, ?::payment_method_type, ?)
RETURNING id_membershipfees;

-- Lister les paiements d'un membre (via jointure contribution)
SELECT mf.*, c.id_member
FROM membershipfees mf
         JOIN contribution c ON mf.id_contribution = c.id_contribution
WHERE c.id_member = ?
ORDER BY mf.membershipfees_date DESC;

-- Transactions d'un collectif sur une période
SELECT
    mf.id_membershipfees,
    mf.id_contribution,
    mf.id_account,
    mf.amount,
    mf.membershipfees_date,
    mf.payment_method,
    c.id_member,
    c.id_collective
FROM membershipfees mf
         JOIN contribution c ON mf.id_contribution = c.id_contribution
WHERE c.id_collective = ?
  AND mf.membershipfees_date BETWEEN ? AND ?
ORDER BY mf.membershipfees_date DESC;

-- Total encaissé par un collectif
SELECT COALESCE(SUM(mf.amount), 0) AS total
FROM membershipfees mf
         JOIN contribution c ON mf.id_contribution = c.id_contribution
WHERE c.id_collective = ?;


-- ────────────────────────────────────────────────────────────
-- 15.6 POSITION_MANDATE
-- ────────────────────────────────────────────────────────────

-- Assigner un mandat à un membre dans un collectif
INSERT INTO position_mandate
(id_member, id_collective, id_federation, position_label, calendar_year, start_date, end_date)
VALUES (?, ?, ?, ?::position_label_type, ?, ?, ?)
RETURNING id_position_mandate;

-- Vérifier les 4 postes obligatoires pour une année
SELECT COUNT(DISTINCT position_label)
FROM position_mandate
WHERE id_collective = ?
  AND calendar_year = ?
  AND position_label IN ('President', 'Vice_President', 'Treasurer', 'Secretary');

-- Vérifier si un membre a un mandat actif
SELECT COUNT(*)
FROM position_mandate
WHERE id_member = ?
  AND position_label = 'Confirmed_Member';

-- Lister les mandats d'un collectif pour une année
SELECT pm.*, m.last_name, m.first_name
FROM position_mandate pm
         JOIN member m ON pm.id_member = m.id_member
WHERE pm.id_collective = ?
  AND pm.calendar_year = ?
ORDER BY pm.position_label;

-- Trouver le président actuel d'un collectif
SELECT m.*
FROM member m
         JOIN position_mandate pm ON pm.id_member = m.id_member
WHERE pm.id_collective = ?
  AND pm.position_label = 'President'
  AND pm.calendar_year = EXTRACT(YEAR FROM CURRENT_DATE);


-- ────────────────────────────────────────────────────────────
-- 15.7 ACTIVITY
-- ────────────────────────────────────────────────────────────

-- Créer une activité de collectif
INSERT INTO activity (id_collective, title, activity_type, activity_date, attendance_required, target_members, is_federation)
VALUES (?, ?, ?::activity_type_type, ?, ?, ?::target_members_type, FALSE)
RETURNING id_activity;

-- Créer une activité de fédération
INSERT INTO activity (id_federation, title, activity_type, activity_date, attendance_required, target_members, is_federation)
VALUES (?, ?, ?::activity_type_type, ?, ?, ?::target_members_type, TRUE)
RETURNING id_activity;

-- Lister les activités d'un collectif
SELECT * FROM activity WHERE id_collective = ? ORDER BY activity_date DESC;

-- Trouver une activité par ID
SELECT * FROM activity WHERE id_activity = ?;


-- ────────────────────────────────────────────────────────────
-- 15.8 ATTENDANCE
-- ────────────────────────────────────────────────────────────

-- Enregistrer la présence d'un membre à une activité
INSERT INTO attendance (id_activity, id_member, is_present, is_excused, absence_reason)
VALUES (?, ?, ?, ?, ?)
ON CONFLICT (id_activity, id_member)
    DO UPDATE SET
                  is_present     = EXCLUDED.is_present,
                  is_excused     = EXCLUDED.is_excused,
                  absence_reason = EXCLUDED.absence_reason,
                  updated_at     = NOW()
RETURNING id_attendance;

-- Lister les présences d'une activité
SELECT att.*, m.last_name, m.first_name
FROM attendance att
         JOIN member m ON att.id_member = m.id_member
WHERE att.id_activity = ?
ORDER BY m.last_name, m.first_name;

-- Taux de présence d'un membre sur une période
SELECT
            COUNT(*) FILTER (WHERE att.is_present)                              AS present_count,
            COUNT(*) FILTER (WHERE NOT att.is_present AND NOT att.is_excused)   AS absent_count,
            COUNT(*) FILTER (WHERE att.is_excused)                              AS excused_count,
            COUNT(*)                                                             AS total,
            ROUND(100.0 * COUNT(*) FILTER (WHERE att.is_present) / NULLIF(COUNT(*), 0), 2) AS rate_pct
FROM attendance att
         JOIN activity a ON att.id_activity = a.id_activity
WHERE att.id_member = ?
  AND a.activity_date BETWEEN ? AND ?;


-- ────────────────────────────────────────────────────────────
-- 15.9 COLLECTIVITY_TRANSACTION
-- ────────────────────────────────────────────────────────────

-- Enregistrer une transaction de collectif
INSERT INTO collectivity_transaction (id_collective, id_member, id_account, amount, payment_method, creation_date)
VALUES (?, ?, ?, ?, ?::payment_method_type, ?)
RETURNING id_transaction;

-- Lister les transactions d'un compte sur une période
SELECT * FROM collectivity_transaction
WHERE id_account = ?
  AND creation_date BETWEEN ? AND ?
ORDER BY creation_date DESC;

-- Total des transactions d'un collectif
SELECT COALESCE(SUM(amount), 0) AS total
FROM collectivity_transaction
WHERE id_collective = ?;


-- ============================================================
-- 16. DONNÉES DE TEST (optionnel)
-- ============================================================

-- Spécialités agricoles (Madagascar)
INSERT INTO agricultural_specialty (name, sector) VALUES
                                                      ('Riziculture', 'Culture'),
                                                      ('Élevage bovin', 'Élevage'),
                                                      ('Maraîchage', 'Culture'),
                                                      ('Apiculture', 'Élevage'),
                                                      ('Pêche', 'Aquaculture');

-- Métiers agricoles
INSERT INTO agricultural_trade (label) VALUES
                                           ('Agriculteur'),
                                           ('Éleveur'),
                                           ('Pêcheur'),
                                           ('Apiculteur'),
                                           ('Maraîcher');

-- Antennes provinciales (6 provinces de Madagascar)
INSERT INTO provincial_branch (province, capital_city, address) VALUES
                                                                    ('Antananarivo', 'Antananarivo', 'Analakely, Antananarivo'),
                                                                    ('Fianarantsoa', 'Fianarantsoa', 'Tanambao, Fianarantsoa'),
                                                                    ('Toamasina', 'Toamasina', 'Bazar Be, Toamasina'),
                                                                    ('Mahajanga', 'Mahajanga', 'Mahabibo, Mahajanga'),
                                                                    ('Toliara', 'Toliara', 'Mahavatse, Toliara'),
                                                                    ('Antsiranana', 'Antsiranana', 'Tanambao, Antsiranana');

-- Fédération
INSERT INTO federation (name, headquarters, email, phone, mandate_start_year, mandate_end_year)
VALUES ('Fédération Nationale Agricole de Madagascar', 'Antananarivo', 'contact@fnam.mg', '+261 20 22 000 00', 2024, 2027)
RETURNING id_federation;
-- suppose id_federation = 1

-- Collectif exemple
INSERT INTO collective (id_federation, id_specialty, id_branch, name, location, phone, creation_date)
VALUES (1, 1, 1, 'Collectif Riz Imerina', 'Ambohimangakely', '+261 34 00 000 00', '2020-01-15')
RETURNING id_collective;
-- suppose id_collective = 1

-- Membres exemples
INSERT INTO member (id_collective, id_trade, last_name, first_name, birth_date, gender, address, phone, email, join_date, is_resigned)
VALUES
    (1, 1, 'Rakoto', 'Jean', '1980-05-10', 'Male'::gender_type,   'Antananarivo', '+261 34 11 111 11', 'jean.rakoto@mail.mg',  '2020-02-01', FALSE),
    (1, 1, 'Rabe',   'Marie','1985-08-22', 'Female'::gender_type, 'Ambohimangakely', '+261 34 22 222 22', 'marie.rabe@mail.mg', '2020-02-01', FALSE),
    (1, 2, 'Rasoa',  'Paul', '1978-12-03', 'Male'::gender_type,   'Tanjombato',  '+261 34 33 333 33', 'paul.rasoa@mail.mg',   '2020-03-15', FALSE);

-- Attribution des postes pour l'année 2024
INSERT INTO position_mandate (id_member, id_collective, position_label, calendar_year, start_date, end_date)
VALUES
    (1, 1, 'President'::position_label_type,       2024, '2024-01-01', '2024-12-31'),
    (2, 1, 'Secretary'::position_label_type,        2024, '2024-01-01', '2024-12-31'),
    (3, 1, 'Treasurer'::position_label_type,        2024, '2024-01-01', '2024-12-31');

-- Compte financier du collectif
INSERT INTO account (id_collective, account_type, account_holder, balance)
VALUES (1, 'Cash'::account_type_type, 'Collectif Riz Imerina', 0.00)
RETURNING id_account;
-- suppose id_account = 1

-- Définition d'une cotisation mensuelle
INSERT INTO contribution (id_member, id_collective, contribution_type, frequency, amount, due_date, is_paid)
VALUES (1, 1, 'Periodic'::contribution_type_type, 'Monthly'::frequency_type, 5000.00, '2024-01-31', FALSE)
RETURNING id_contribution;
-- suppose id_contribution = 1

-- Enregistrement d'un paiement
INSERT INTO membershipfees (id_contribution, id_account, amount, membershipfees_date, payment_method, federation_share)
VALUES (1, 1, 5000.00, '2024-01-15', 'Cash'::payment_method_type, 500.00);

-- Mise à jour du solde du compte après paiement
UPDATE account SET balance = balance + 5000.00 WHERE id_account = 1;

-- Activité : Assemblée générale mensuelle
INSERT INTO activity (id_collective, title, activity_type, activity_date, attendance_required, target_members, is_federation)
VALUES (1, 'AG Janvier 2024', 'Monthly_General_Assembly'::activity_type_type, '2024-01-20', TRUE, 'All'::target_members_type, FALSE)
RETURNING id_activity;
-- suppose id_activity = 1

-- Enregistrement des présences
INSERT INTO attendance (id_activity, id_member, is_present, is_excused) VALUES
                                                                            (1, 1, TRUE,  FALSE),
                                                                            (1, 2, TRUE,  FALSE),
                                                                            (1, 3, FALSE, TRUE);