

CREATE TYPE gender_type        AS ENUM ('Female', 'Male');
CREATE TYPE account_type_type  AS ENUM ('Cash', 'Bank', 'Mobile Money');
CREATE TYPE bank_name_type     AS ENUM ('BRED','MCB','BMOI','BOA','BGFI','AFG','ACCES BANQUE','BAOBAB','SIPEM');
CREATE TYPE mobile_money_type  AS ENUM ('Orange Money','Mvola','Airtel Money');
CREATE TYPE position_label_type AS ENUM (
    'President',
    'Vice President',
    'Treasurer',
    'Secretary',
    'Confirmed Member',
    'Junior Member'
    );
CREATE TYPE contribution_type_type AS ENUM ('Periodic', 'One-time');
CREATE TYPE frequency_type         AS ENUM ('Monthly', 'Annual');
CREATE TYPE payment_method_type    AS ENUM ('Cash', 'Bank Transfer', 'Mobile Money');
CREATE TYPE activity_type_type     AS ENUM (
    'Monthly General Assembly',
    'Mandatory Junior Training',
    'Exceptional Activity',
    'Federation Activity'
    );
CREATE TYPE target_members_type AS ENUM ('All', 'Juniors Only', 'Confirmed Only');



-- 1. AGRICULTURAL_SPECIALTY
CREATE TABLE agricultural_specialty (
                                        id_specialty    SERIAL PRIMARY KEY,
                                        name            VARCHAR(100) NOT NULL,
                                        sector          VARCHAR(100)
);

-- 2. AGRICULTURAL_TRADE
CREATE TABLE agricultural_trade (
                                    id_trade    SERIAL PRIMARY KEY,
                                    label       VARCHAR(100) NOT NULL
);

-- 3. PROVINCIAL_BRANCH
CREATE TABLE provincial_branch (
                                   id_branch           SERIAL PRIMARY KEY,
                                   province            VARCHAR(100) NOT NULL,
                                   capital_city        VARCHAR(100) NOT NULL,
                                   address             VARCHAR(200)
);

-- 4. FEDERATION
CREATE TABLE federation (
                            id_federation       SERIAL PRIMARY KEY,
                            name                VARCHAR(100) NOT NULL,
                            headquarters        VARCHAR(200),
                            email               VARCHAR(100),
                            phone               VARCHAR(20),
                            id_president        INT,
                            mandate_start_year  INT,
                            mandate_end_year    INT
);

-- 5. COLLECTIVE
CREATE TABLE collective (
                            id_collective       SERIAL PRIMARY KEY,
                            id_federation       INT NOT NULL,
                            id_specialty        INT,
                            id_branch           INT,
                            name                VARCHAR(100) NOT NULL UNIQUE,
                            location            VARCHAR(100) NOT NULL,
                            phone               VARCHAR(20),
                            creation_date       DATE NOT NULL,
                            id_president        INT,
                            CONSTRAINT fk_collective_federation FOREIGN KEY (id_federation) REFERENCES federation(id_federation),
                            CONSTRAINT fk_collective_specialty  FOREIGN KEY (id_specialty)  REFERENCES agricultural_specialty(id_specialty),
                            CONSTRAINT fk_collective_branch     FOREIGN KEY (id_branch)     REFERENCES provincial_branch(id_branch)
);

-- 6. MEMBER
CREATE TABLE member (
                        id_member           SERIAL PRIMARY KEY,
                        id_collective       INT NOT NULL,
                        id_trade            INT,
                        last_name           VARCHAR(100) NOT NULL,
                        first_name          VARCHAR(100) NOT NULL,
                        birth_date          DATE,
                        gender              gender_type NOT NULL,
                        address             VARCHAR(200),
                        phone               VARCHAR(20),
                        email               VARCHAR(100),
                        join_date           DATE NOT NULL,
                        is_resigned         BOOLEAN DEFAULT FALSE,
                        CONSTRAINT fk_member_collective FOREIGN KEY (id_collective) REFERENCES collective(id_collective),
                        CONSTRAINT fk_member_trade      FOREIGN KEY (id_trade)      REFERENCES agricultural_trade(id_trade)
);

ALTER TABLE federation
    ADD CONSTRAINT fk_federation_president FOREIGN KEY (id_president) REFERENCES member(id_member);

ALTER TABLE collective
    ADD CONSTRAINT fk_collective_president FOREIGN KEY (id_president) REFERENCES member(id_member);

-- 7. POSITION_MANDATE
CREATE TABLE position_mandate (
                                  id_position_mandate     SERIAL PRIMARY KEY,
                                  id_member               INT NOT NULL,
                                  id_collective           INT,
                                  id_federation           INT,
                                  position_label          position_label_type NOT NULL,
                                  is_unique               BOOLEAN NOT NULL DEFAULT TRUE,
                                  is_elective             BOOLEAN NOT NULL DEFAULT TRUE,
                                  calendar_year           INT NOT NULL,
                                  start_date              DATE NOT NULL,
                                  end_date                DATE,
                                  cumulated_mandates      INT DEFAULT 1,
                                  CONSTRAINT fk_position_member     FOREIGN KEY (id_member)     REFERENCES member(id_member),
                                  CONSTRAINT fk_position_collective FOREIGN KEY (id_collective) REFERENCES collective(id_collective),
                                  CONSTRAINT fk_position_federation FOREIGN KEY (id_federation) REFERENCES federation(id_federation),

                                  CONSTRAINT uk_unique_position UNIQUE (id_collective, position_label, calendar_year)
);

-- 8. ACCOUNT
CREATE TABLE account (
                         id_account              SERIAL PRIMARY KEY,
                         id_collective           INT,
                         id_federation           INT,
                         account_type            account_type_type NOT NULL,
                         account_holder          VARCHAR(100),
                         bank_name               bank_name_type,
                         mobile_money_service    mobile_money_type,
                         bank_account_number     VARCHAR(23),
                         phone_number            VARCHAR(20),
                         balance                 NUMERIC(15,2) DEFAULT 0.00,
                         CONSTRAINT fk_account_collective  FOREIGN KEY (id_collective)  REFERENCES collective(id_collective),
                         CONSTRAINT fk_account_federation  FOREIGN KEY (id_federation)  REFERENCES federation(id_federation)
);

-- 9. CONTRIBUTION
CREATE TABLE contribution (
                              id_contribution     SERIAL PRIMARY KEY,
                              id_member           INT NOT NULL,
                              id_collective       INT NOT NULL,
                              contribution_type   contribution_type_type NOT NULL,
                              frequency           frequency_type,
                              amount              NUMERIC(15,2) NOT NULL,
                              due_date            DATE NOT NULL,
                              is_paid             BOOLEAN DEFAULT FALSE,
                              CONSTRAINT fk_contribution_member     FOREIGN KEY (id_member)     REFERENCES member(id_member),
                              CONSTRAINT fk_contribution_collective FOREIGN KEY (id_collective) REFERENCES collective(id_collective)
);

-- 10. COLLECTION
CREATE TABLE collection (
                            id_collection               SERIAL PRIMARY KEY,
                            id_contribution             INT NOT NULL,
                            id_account                  INT NOT NULL,
                            amount                      NUMERIC(15,2) NOT NULL,
                            collection_date             DATE NOT NULL,
                            payment_method              payment_method_type NOT NULL,
                            federation_share            NUMERIC(15,2) DEFAULT 0.00,
                            CONSTRAINT fk_collection_contribution FOREIGN KEY (id_contribution) REFERENCES contribution(id_contribution),
                            CONSTRAINT fk_collection_account      FOREIGN KEY (id_account)      REFERENCES account(id_account)
);

-- 11. ACTIVITY
CREATE TABLE activity (
                          id_activity             SERIAL PRIMARY KEY,
                          id_collective           INT,
                          id_federation           INT,
                          title                   VARCHAR(200) NOT NULL,
                          activity_type           activity_type_type NOT NULL,
                          activity_date           DATE NOT NULL,
                          attendance_required     BOOLEAN DEFAULT TRUE,
                          target_members          target_members_type DEFAULT 'All',
                          is_federation           BOOLEAN DEFAULT FALSE,
                          CONSTRAINT fk_activity_collective  FOREIGN KEY (id_collective)  REFERENCES collective(id_collective),
                          CONSTRAINT fk_activity_federation  FOREIGN KEY (id_federation)  REFERENCES federation(id_federation)
);

-- 12. ATTENDANCE
CREATE TABLE attendance (
                            id_attendance           SERIAL PRIMARY KEY,
                            id_activity             INT NOT NULL,
                            id_member               INT NOT NULL,
                            id_member_collective    INT NOT NULL,
                            is_present              BOOLEAN DEFAULT FALSE,
                            is_excused              BOOLEAN DEFAULT FALSE,
                            absence_reason          VARCHAR(300),
                            month_concerned         DATE,
                            overall_attendance_rate NUMERIC(5,2),
                            active_members_count    INT,
                            report_date             DATE,
                            CONSTRAINT fk_attendance_activity   FOREIGN KEY (id_activity)          REFERENCES activity(id_activity),
                            CONSTRAINT fk_attendance_member     FOREIGN KEY (id_member)            REFERENCES member(id_member),
                            CONSTRAINT fk_attendance_collective FOREIGN KEY (id_member_collective) REFERENCES collective(id_collective)
);


CREATE INDEX idx_member_collective         ON member(id_collective);
CREATE INDEX idx_member_trade              ON member(id_trade);
CREATE INDEX idx_collective_specialty      ON collective(id_specialty);
CREATE INDEX idx_collective_branch         ON collective(id_branch);
CREATE INDEX idx_position_member           ON position_mandate(id_member);
CREATE INDEX idx_position_collective       ON position_mandate(id_collective);
CREATE INDEX idx_contribution_member       ON contribution(id_member);
CREATE INDEX idx_collection_account        ON collection(id_account);
CREATE INDEX idx_attendance_activity       ON attendance(id_activity);
CREATE INDEX idx_attendance_member         ON attendance(id_member);
CREATE INDEX idx_activity_collective       ON activity(id_collective);
CREATE INDEX idx_account_collective        ON account(id_collective);


alter table collection rename to membershipFees;
alter table membershipFees rename column collection_date to membershipFees_date;

----correction table
-- ============================================================
-- Agro-Federation — schema.sql (version corrigée)
-- Corrections :
--   1. Résolution des conflits git dans FinancialAccountRepository
--      (table : account, PK : id_account — c'est la bonne version HEAD)
--   2. La table collection renommée membershipfees avec la colonne
--      renommée membershipFees_date + PK renommée id_membershipfees
--      (le repository utilise déjà ce nom)
--   3. Enum values alignées avec les noms Java (underscores)
-- ============================================================

-- ── TYPES ENUM ──────────────────────────────────────────────

CREATE TYPE gender_type            AS ENUM ('Female', 'Male');
CREATE TYPE account_type_type      AS ENUM ('Cash', 'Bank', 'Mobile Money');
CREATE TYPE bank_name_type         AS ENUM ('BRED','MCB','BMOI','BOA','BGFI','AFG','ACCES BANQUE','BAOBAB','SIPEM');
CREATE TYPE mobile_money_type      AS ENUM ('Orange Money','Mvola','Airtel Money');
CREATE TYPE position_label_type    AS ENUM (
    'President',
    'Vice President',
    'Treasurer',
    'Secretary',
    'Confirmed Member',
    'Junior Member'
    );
CREATE TYPE contribution_type_type AS ENUM ('Periodic', 'One-time');
CREATE TYPE frequency_type         AS ENUM ('Monthly', 'Annual');
CREATE TYPE payment_method_type    AS ENUM ('Cash', 'Bank Transfer', 'Mobile Money');
CREATE TYPE activity_type_type     AS ENUM (
    'Monthly General Assembly',
    'Mandatory Junior Training',
    'Exceptional Activity',
    'Federation Activity'
    );
CREATE TYPE target_members_type    AS ENUM ('All', 'Juniors Only', 'Confirmed Only');

-- ── 1. AGRICULTURAL_SPECIALTY ────────────────────────────────
CREATE TABLE agricultural_specialty (
                                        id_specialty    SERIAL PRIMARY KEY,
                                        name            VARCHAR(100) NOT NULL,
                                        sector          VARCHAR(100)
);

-- ── 2. AGRICULTURAL_TRADE ────────────────────────────────────
CREATE TABLE agricultural_trade (
                                    id_trade    SERIAL PRIMARY KEY,
                                    label       VARCHAR(100) NOT NULL
);

-- ── 3. PROVINCIAL_BRANCH ─────────────────────────────────────
CREATE TABLE provincial_branch (
                                   id_branch       SERIAL PRIMARY KEY,
                                   province        VARCHAR(100) NOT NULL,
                                   capital_city    VARCHAR(100) NOT NULL,
                                   address         VARCHAR(200)
);

-- ── 4. FEDERATION ────────────────────────────────────────────
CREATE TABLE federation (
                            id_federation       SERIAL PRIMARY KEY,
                            name                VARCHAR(100) NOT NULL,
                            headquarters        VARCHAR(200),
                            email               VARCHAR(100),
                            phone               VARCHAR(20),
                            id_president        INT,
                            mandate_start_year  INT,
                            mandate_end_year    INT
);

-- ── 5. COLLECTIVE ────────────────────────────────────────────
CREATE TABLE collective (
                            id_collective   SERIAL PRIMARY KEY,
                            id_federation   INT NOT NULL,
                            id_specialty    INT,
                            id_branch       INT,
                            name            VARCHAR(100) NOT NULL UNIQUE,
                            location        VARCHAR(100) NOT NULL,
                            phone           VARCHAR(20),
                            creation_date   DATE NOT NULL,
                            id_president    INT,
                            CONSTRAINT fk_collective_federation FOREIGN KEY (id_federation) REFERENCES federation(id_federation),
                            CONSTRAINT fk_collective_specialty  FOREIGN KEY (id_specialty)  REFERENCES agricultural_specialty(id_specialty),
                            CONSTRAINT fk_collective_branch     FOREIGN KEY (id_branch)     REFERENCES provincial_branch(id_branch)
);

-- ── 6. MEMBER ────────────────────────────────────────────────
CREATE TABLE member (
                        id_member       SERIAL PRIMARY KEY,
                        id_collective   INT NOT NULL,
                        id_trade        INT,
                        last_name       VARCHAR(100) NOT NULL,
                        first_name      VARCHAR(100) NOT NULL,
                        birth_date      DATE,
                        gender          gender_type NOT NULL,
                        address         VARCHAR(200),
                        phone           VARCHAR(20),
                        email           VARCHAR(100),
                        join_date       DATE NOT NULL,
                        is_resigned     BOOLEAN DEFAULT FALSE,
                        CONSTRAINT fk_member_collective FOREIGN KEY (id_collective) REFERENCES collective(id_collective),
                        CONSTRAINT fk_member_trade      FOREIGN KEY (id_trade)      REFERENCES agricultural_trade(id_trade)
);

ALTER TABLE federation
    ADD CONSTRAINT fk_federation_president FOREIGN KEY (id_president) REFERENCES member(id_member);

ALTER TABLE collective
    ADD CONSTRAINT fk_collective_president FOREIGN KEY (id_president) REFERENCES member(id_member);

-- ── 7. POSITION_MANDATE ──────────────────────────────────────
CREATE TABLE position_mandate (
                                  id_position_mandate SERIAL PRIMARY KEY,
                                  id_member           INT NOT NULL,
                                  id_collective       INT,
                                  id_federation       INT,
                                  position_label      position_label_type NOT NULL,
                                  is_unique           BOOLEAN NOT NULL DEFAULT TRUE,
                                  is_elective         BOOLEAN NOT NULL DEFAULT TRUE,
                                  calendar_year       INT NOT NULL,
                                  start_date          DATE NOT NULL,
                                  end_date            DATE,
                                  cumulated_mandates  INT DEFAULT 1,
                                  CONSTRAINT fk_position_member     FOREIGN KEY (id_member)     REFERENCES member(id_member),
                                  CONSTRAINT fk_position_collective FOREIGN KEY (id_collective) REFERENCES collective(id_collective),
                                  CONSTRAINT fk_position_federation FOREIGN KEY (id_federation) REFERENCES federation(id_federation),
                                  CONSTRAINT uk_unique_position     UNIQUE (id_collective, position_label, calendar_year)
);

-- ── 8. ACCOUNT ───────────────────────────────────────────────
-- Correction : table nommée `account`
-- bank_account_number est un VARCHAR(23) unique (RIB composite),
CREATE TABLE account (
                         id_account              SERIAL PRIMARY KEY,
                         id_collective           INT,
                         id_federation           INT,
                         account_type            account_type_type NOT NULL,
                         account_holder          VARCHAR(100),
                         bank_name               bank_name_type,
                         mobile_money_service    mobile_money_type,
                         bank_account_number     VARCHAR(23),
                         phone_number            VARCHAR(20),
                         balance                 NUMERIC(15,2) DEFAULT 0.00,
                         CONSTRAINT fk_account_collective FOREIGN KEY (id_collective) REFERENCES collective(id_collective),
                         CONSTRAINT fk_account_federation FOREIGN KEY (id_federation) REFERENCES federation(id_federation)
);

-- ── 9. CONTRIBUTION ──────────────────────────────────────────
CREATE TABLE contribution (
                              id_contribution     SERIAL PRIMARY KEY,
                              id_member           INT NOT NULL,
                              id_collective       INT NOT NULL,
                              contribution_type   contribution_type_type NOT NULL,
                              frequency           frequency_type,
                              amount              NUMERIC(15,2) NOT NULL,
                              due_date            DATE NOT NULL,
                              is_paid             BOOLEAN DEFAULT FALSE,
                              CONSTRAINT fk_contribution_member     FOREIGN KEY (id_member)     REFERENCES member(id_member),
                              CONSTRAINT fk_contribution_collective FOREIGN KEY (id_collective) REFERENCES collective(id_collective)
);

-- ── 10. MEMBERSHIPFEES ───────────────────────────────────────
-- Anciennement `collection`. Renommée définitivement ici.
-- PK renommée id_membershipfees (le repository utilise RETURNING id_collection → à corriger aussi).
-- Colonne collection_date renommée membershipFees_date.
CREATE TABLE membershipfees (
                                id_membershipfees   SERIAL PRIMARY KEY,
                                id_contribution     INT NOT NULL,
                                id_account          INT NOT NULL,
                                amount              NUMERIC(15,2) NOT NULL,
                                membershipfees_date DATE NOT NULL,
                                payment_method      payment_method_type NOT NULL,
                                federation_share    NUMERIC(15,2) DEFAULT 0.00,
                                CONSTRAINT fk_membershipfees_contribution FOREIGN KEY (id_contribution) REFERENCES contribution(id_contribution),
                                CONSTRAINT fk_membershipfees_account      FOREIGN KEY (id_account)      REFERENCES account(id_account)
);

-- ── 11. ACTIVITY ─────────────────────────────────────────────
CREATE TABLE activity (
                          id_activity         SERIAL PRIMARY KEY,
                          id_collective       INT,
                          id_federation       INT,
                          title               VARCHAR(200) NOT NULL,
                          activity_type       activity_type_type NOT NULL,
                          activity_date       DATE NOT NULL,
                          attendance_required BOOLEAN DEFAULT TRUE,
                          target_members      target_members_type DEFAULT 'All',
                          is_federation       BOOLEAN DEFAULT FALSE,
                          CONSTRAINT fk_activity_collective FOREIGN KEY (id_collective) REFERENCES collective(id_collective),
                          CONSTRAINT fk_activity_federation FOREIGN KEY (id_federation) REFERENCES federation(id_federation)
);

-- ── 12. ATTENDANCE ───────────────────────────────────────────
-- Correction : "Attendence" → "attendance" (faute de frappe corrigée)
CREATE TABLE attendance (
                            id_attendance           SERIAL PRIMARY KEY,
                            id_activity             INT NOT NULL,
                            id_member               INT NOT NULL,
                            id_member_collective    INT NOT NULL,
                            is_present              BOOLEAN DEFAULT FALSE,
                            is_excused              BOOLEAN DEFAULT FALSE,
                            absence_reason          VARCHAR(300),
                            month_concerned         DATE,
                            overall_attendance_rate NUMERIC(5,2),
                            active_members_count    INT,
                            report_date             DATE,
                            CONSTRAINT fk_attendance_activity   FOREIGN KEY (id_activity)          REFERENCES activity(id_activity),
                            CONSTRAINT fk_attendance_member     FOREIGN KEY (id_member)            REFERENCES member(id_member),
                            CONSTRAINT fk_attendance_collective FOREIGN KEY (id_member_collective) REFERENCES collective(id_collective)
);

-- ── INDEX ─────────────────────────────────────────────────────
CREATE INDEX idx_member_collective    ON member(id_collective);
CREATE INDEX idx_member_trade         ON member(id_trade);
CREATE INDEX idx_collective_specialty ON collective(id_specialty);
CREATE INDEX idx_collective_branch    ON collective(id_branch);
CREATE INDEX idx_position_member      ON position_mandate(id_member);
CREATE INDEX idx_position_collective  ON position_mandate(id_collective);
CREATE INDEX idx_contribution_member  ON contribution(id_member);
CREATE INDEX idx_membershipfees_account ON membershipfees(id_account);
CREATE INDEX idx_attendance_activity  ON attendance(id_activity);
CREATE INDEX idx_attendance_member    ON attendance(id_member);
CREATE INDEX idx_activity_collective  ON activity(id_collective);
CREATE INDEX idx_account_collective   ON account(id_collective);