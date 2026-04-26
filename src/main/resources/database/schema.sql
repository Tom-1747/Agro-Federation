
-- ════════════════════════════════════════════════════════════
-- 1. COLLECTIVE
-- ════════════════════════════════════════════════════════════
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
-- 1.1 Insérer un nouveau collectif
INSERT INTO collective
(id_federation, id_specialty, id_branch, name, location, phone, creation_date)
VALUES (?, ?, ?, ?, ?, ?, ?)
RETURNING id_collective;

-- 1.2 Mettre à jour le nom d'un collectif
UPDATE collective
SET name = ?
WHERE id_collective = ?;

-- 1.3 Trouver un collectif par ID
SELECT *
FROM collective
WHERE id_collective = ?;

-- 1.4 Lister tous les collectifs
SELECT *
FROM collective
ORDER BY id_collective;

-- 1.5 Compter les membres actifs d'un collectif
SELECT COUNT(*)
FROM member
WHERE id_collective = ?
  AND is_resigned = FALSE;

-- 1.6 Compter les membres avec au moins 6 mois d'ancienneté
SELECT COUNT(*)
FROM member
WHERE id_collective = ?
  AND is_resigned = FALSE
  AND join_date <= CURRENT_DATE - INTERVAL '6 months';

-- 1.7 Vérifier que les 4 postes obligatoires sont pourvus pour une année
SELECT COUNT(DISTINCT position_label)
FROM position_mandate
WHERE id_collective = ?
  AND calendar_year = ?
  AND position_label IN (
                         'President',
                         'Vice President',
                         'Treasurer',
                         'Secretary'
    );
-- Retourne 4 si tous les postes sont pourvus

-- 1.8 Vérifier l'existence d'un collectif par nom
SELECT COUNT(*)
FROM collective
WHERE name = ?;


-- ════════════════════════════════════════════════════════════
-- 2. MEMBER
-- ════════════════════════════════════════════════════════════

-- 2.1 Insérer un nouveau membre
INSERT INTO member
(id_collective, id_trade, last_name, first_name,
 birth_date, gender, address, phone, email, join_date, is_resigned)
VALUES (?, ?, ?, ?, ?, ?::gender_type, ?, ?, ?, ?, FALSE)
RETURNING id_member;

-- 2.2 Trouver un membre par ID
SELECT *
FROM member
WHERE id_member = ?;

-- 2.3 Lister les membres actifs d'un collectif
SELECT *
FROM member
WHERE id_collective = ?
  AND is_resigned = FALSE
ORDER BY id_member;

-- 2.4 Trouver plusieurs membres par liste d'IDs
-- (les '?' sont générés dynamiquement en Java selon le nombre d'IDs)
SELECT *
FROM member
WHERE id_member IN (?, ?, ...)
  AND is_resigned = FALSE;

-- 2.5 Vérifier si un membre est un Membre Confirmé
SELECT COUNT(*)
FROM position_mandate
WHERE id_member = ?
  AND position_label = 'Confirmed Member';
-- Retourne > 0 si confirmé

-- 2.6 Vérifier l'ancienneté minimale d'un membre (en jours)
SELECT COUNT(*)
FROM member
WHERE id_member = ?
  AND is_resigned = FALSE
  AND join_date <= CURRENT_DATE - (? * INTERVAL '1 day');
-- Retourne 1 si l'ancienneté est suffisante


-- ════════════════════════════════════════════════════════════
-- 3. ACCOUNT (comptes financiers)
-- ════════════════════════════════════════════════════════════

-- 3.1 Trouver un compte par ID
SELECT *
FROM account
WHERE id_account = ?;

-- 3.2 Lister les comptes d'un collectif
SELECT *
FROM account
WHERE id_collective = ?
ORDER BY id_account;

-- 3.3 Mettre à jour le solde d'un compte
UPDATE account
SET balance = ?
WHERE id_account = ?;

-- 3.4 Calculer le solde cumulé d'un compte à une date donnée
--     (somme de tous les encaissements jusqu'à cette date)
SELECT COALESCE(SUM(amount), 0)
FROM membershipfees
WHERE id_account = ?
  AND membershipfees_date <= ?;


-- ════════════════════════════════════════════════════════════
-- 4. CONTRIBUTION (définitions de cotisations)
-- ════════════════════════════════════════════════════════════

-- 4.1 Créer une définition de cotisation pour un collectif
INSERT INTO contribution
(id_member, id_collective, contribution_type, frequency, amount, due_date, is_paid)
VALUES (?, ?, ?::contribution_type_type, ?::frequency_type, ?, ?, FALSE)
RETURNING id_contribution;
-- Note : id_member est le collectiveId en attendant l'assignation membre

-- 4.2 Lister les cotisations d'un collectif
SELECT *
FROM contribution
WHERE id_collective = ?
ORDER BY id_contribution;

-- 4.3 Trouver une cotisation par ID
SELECT *
FROM contribution
WHERE id_contribution = ?;


-- ════════════════════════════════════════════════════════════
-- 5. MEMBERSHIPFEES (paiements effectués = ancienne table collection)
-- ════════════════════════════════════════════════════════════

-- 5.1 Enregistrer un paiement de cotisation
INSERT INTO membershipfees
(id_contribution, id_account, amount, membershipfees_date, payment_method)
VALUES (?, ?, ?, ?, ?::payment_method_type)
RETURNING id_membershipfees;

-- 5.2 Lister les paiements d'un membre (via jointure contribution)
SELECT mf.*, c.id_member
FROM membershipfees mf
         JOIN contribution c ON mf.id_contribution = c.id_contribution
WHERE c.id_member = ?
ORDER BY mf.membershipfees_date DESC;

-- 5.3 Lister les transactions d'un collectif sur une période
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


-- ════════════════════════════════════════════════════════════
-- 6. POSITION_MANDATE
-- ════════════════════════════════════════════════════════════

-- 6.1 Vérifier que les postes obligatoires sont pourvus (voir 1.7 ci-dessus)
--     Requête identique, rappelée ici pour contexte
SELECT COUNT(DISTINCT position_label)
FROM position_mandate
WHERE id_collective = ?
  AND calendar_year = ?
  AND position_label IN (
                         'President',
                         'Vice President',
                         'Treasurer',
                         'Secretary'
    );

-- 6.2 Vérifier si un membre détient un mandat actif
SELECT COUNT(*)
FROM position_mandate
WHERE id_member = ?
  AND position_label = 'Confirmed Member';


-- ════════════════════════════════════════════════════════════
-- 7. VUES UTILES (recommandées à ajouter au schema)
-- ════════════════════════════════════════════════════════════

-- Vue : résumé financier par collectif
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

-- Vue : membres avec leur poste actuel
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
    COUNT(att.id_member)                                   AS total_expected,
    SUM(CASE WHEN att.is_present THEN 1 ELSE 0 END)       AS total_present,
    SUM(CASE WHEN att.is_excused THEN 1 ELSE 0 END)       AS total_excused,
    ROUND(
            100.0 * SUM(CASE WHEN att.is_present THEN 1 ELSE 0 END)
                / NULLIF(COUNT(att.id_member), 0),
            2
    )                                                      AS attendance_rate_pct
FROM activity a
         LEFT JOIN attendance att ON att.id_activity = a.id_activity
GROUP BY a.id_activity, a.title, a.activity_date, a.activity_type, a.id_collective;



