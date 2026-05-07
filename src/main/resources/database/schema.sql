

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



CREATE INDEX idx_member_collective  ON member(id_collective);
CREATE INDEX idx_member_resigned    ON member(is_resigned);
CREATE INDEX idx_member_join_date   ON member(join_date);

CREATE INDEX idx_mandate_member     ON position_mandate(id_member);
CREATE INDEX idx_mandate_collective ON position_mandate(id_collective);
CREATE INDEX idx_mandate_year       ON position_mandate(calendar_year);

CREATE INDEX idx_account_collective ON account(id_collective);

CREATE INDEX idx_contribution_member     ON contribution(id_member);
CREATE INDEX idx_contribution_collective ON contribution(id_collective);

CREATE INDEX idx_fees_contribution  ON membershipfees(id_contribution);
CREATE INDEX idx_fees_account       ON membershipfees(id_account);
CREATE INDEX idx_fees_date          ON membershipfees(membershipfees_date);

CREATE INDEX idx_activity_collective ON activity(id_collective);
CREATE INDEX idx_activity_date       ON activity(activity_date);

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

ALTER TABLE contribution ADD COLUMN IF NOT EXISTS is_active BOOLEAN NOT NULL DEFAULT TRUE;

CREATE INDEX IF NOT EXISTS idx_contribution_is_active ON contribution(is_active);



INSERT INTO federation (name, headquarters) VALUES ('Fédération Agricole Madagascar', 'Antananarivo');

INSERT INTO agricultural_specialty (name, sector) VALUES ('Riziculture', 'Agriculture');

INSERT INTO provincial_branch (province, capital_city) VALUES ('Analamanga', 'Antananarivo');

INSERT INTO agricultural_trade (label) VALUES ('Agriculteur');

INSERT INTO collective (id_federation, id_specialty, id_branch, name, location, creation_date)
VALUES (1, 1, 1, 'Collective Test', 'Antananarivo', '2024-01-01');
-- Insérer 10 membres (avec join_date ancienne pour avoir 6 mois d ancienneté)
INSERT INTO member (id_collective, id_trade, last_name, first_name, birth_date, gender, address, phone, email, join_date)
VALUES
    (1, 1, 'Rakoto', 'Jean', '1990-01-01', 'Male', 'Antananarivo', '034000001', 'jean@mail.com', '2025-01-01'),
    (1, 1, 'Rabe', 'Marie', '1992-03-15', 'Female', 'Antananarivo', '034000002', 'marie@mail.com', '2025-01-01'),
    (1, 1, 'Razafy', 'Paul', '1988-07-20', 'Male', 'Antananarivo', '034000003', 'paul@mail.com', '2025-01-01'),
    (1, 1, 'Andry', 'Soa', '1995-11-10', 'Female', 'Antananarivo', '034000004', 'soa@mail.com', '2025-01-01'),
    (1, 1, 'Ravo', 'Luc', '1985-05-05', 'Male', 'Antananarivo', '034000005', 'luc@mail.com', '2025-01-01'),
    (1, 1, 'Niry', 'Haja', '1993-09-25', 'Male', 'Antananarivo', '034000006', 'haja@mail.com', '2025-11-01'),
    (1, 1, 'Tiana', 'Fara', '1991-02-14', 'Female', 'Antananarivo', '034000007', 'fara@mail.com', '2025-11-01'),
    (1, 1, 'Mamy', 'Zo', '1987-08-30', 'Male', 'Antananarivo', '034000008', 'zo@mail.com', '2025-11-01'),
    (1, 1, 'Seheno', 'Lala', '1996-04-18', 'Female', 'Antananarivo', '034000009', 'lala@mail.com', '2025-11-01'),
    (1, 1, 'Faniry', 'Rado', '1989-12-01', 'Male', 'Antananarivo', '034000010', 'rado@mail.com', '2025-11-01');





TRUNCATE collectivity_transaction, membershipfees, contribution,
    account, position_mandate, member, collective,
    agricultural_specialty, agricultural_trade, provincial_branch,
    federation RESTART IDENTITY CASCADE;

-- FEDERATION
INSERT INTO federation (name, headquarters)
VALUES ('Fédération Agricole Madagascar', 'Antananarivo');

-- SPECIALITES
INSERT INTO agricultural_specialty (name, sector) VALUES
                                                      ('Riziculture', 'Agriculture'),
                                                      ('Pisciculture', 'Aquaculture'),
                                                      ('Apiculture', 'Elevage');

-- BRANCHES
INSERT INTO provincial_branch (province, capital_city) VALUES
                                                           ('Alaotra-Mangoro', 'Ambatondrazaka'),
                                                           ('Atsinanana', 'Brickaville'),
                                                           ('Vakinankaratra', 'Antsirabe');

-- METIERS
INSERT INTO agricultural_trade (label) VALUES
                                           ('Riziculteur'), ('Agriculteur'), ('Collecteur'),
                                           ('Distributeur'), ('Apiculteur');

-- COLLECTIVITES
INSERT INTO collective (id_federation, id_specialty, id_branch, name, location, creation_date) VALUES
                                                                                                   (1, 1, 1, 'Mpanorina', 'Ambatondrazaka', '2020-01-01'),
                                                                                                   (1, 2, 1, 'Dobo voalohany', 'Ambatondrazaka', '2020-01-01'),
                                                                                                   (1, 3, 2, 'Tantely mamy', 'Brickaville', '2020-01-01');

-- MEMBRES COLLECTIVITE 1 (join_date = 01/01/2026)
INSERT INTO member (id_collective, id_trade, last_name, first_name, birth_date, gender, address, phone, email, join_date) VALUES
                                                                                                                              (1, 1, 'Nom membre 1', 'Prénom membre 1', '1980-02-01', 'Male', 'Lot II V M Ambato.', '0341234567', 'member.1@fed-agri.mg', '2026-01-01'),
                                                                                                                              (1, 2, 'Nom membre 2', 'Prénom membre 2', '1982-03-05', 'Male', 'Lot II F Ambato.', '0321234567', 'member.2@fed-agri.mg', '2026-01-01'),
                                                                                                                              (1, 3, 'Nom membre 3', 'Prénom membre 3', '1992-03-10', 'Male', 'Lot II J Ambato.', '0331234567', 'member.3@fed-agri.mg', '2026-01-01'),
                                                                                                                              (1, 4, 'Nom membre 4', 'Prénom membre 4', '1988-05-22', 'Female', 'Lot A K 50 Ambato.', '0381234567', 'member.4@fed-agri.mg', '2026-01-01'),
                                                                                                                              (1, 1, 'Nom membre 5', 'Prénom membre 5', '1999-08-21', 'Male', 'Lot UV 80 Ambato.', '0373434567', 'member.5@fed-agri.mg', '2026-01-01'),
                                                                                                                              (1, 1, 'Nom membre 6', 'Prénom membre 6', '1998-08-22', 'Female', 'Lot UV 6 Ambato.', '0372234567', 'member.6@fed-agri.mg', '2026-01-01'),
                                                                                                                              (1, 1, 'Nom membre 7', 'Prénom membre 7', '1998-01-31', 'Male', 'Lot UV 7 Ambato.', '0374234567', 'member.7@fed-agri.mg', '2026-01-01'),
                                                                                                                              (1, 1, 'Nom membre 8', 'Prénom membre 8', '1975-08-20', 'Male', 'Lot UV 8 Ambato.', '0370234567', 'member.8@fed-agri.mg', '2026-01-01');

-- MEMBRES COLLECTIVITE 2 (mêmes membres 1-8, join_date = 01/01/2026)
INSERT INTO member (id_collective, id_trade, last_name, first_name, birth_date, gender, address, phone, email, join_date) VALUES
                                                                                                                              (2, 1, 'Nom membre 1', 'Prénom membre 1', '1980-02-01', 'Male', 'Lot II V M Ambato.', '0341234568', 'member.1b@fed-agri.mg', '2026-01-01'),
                                                                                                                              (2, 2, 'Nom membre 2', 'Prénom membre 2', '1982-03-05', 'Male', 'Lot II F Ambato.', '0321234568', 'member.2b@fed-agri.mg', '2026-01-01'),
                                                                                                                              (2, 3, 'Nom membre 3', 'Prénom membre 3', '1992-03-10', 'Male', 'Lot II J Ambato.', '0331234568', 'member.3b@fed-agri.mg', '2026-01-01'),
                                                                                                                              (2, 4, 'Nom membre 4', 'Prénom membre 4', '1988-05-22', 'Female', 'Lot A K 50 Ambato.', '0381234568', 'member.4b@fed-agri.mg', '2026-01-01'),
                                                                                                                              (2, 1, 'Nom membre 5', 'Prénom membre 5', '1999-08-21', 'Male', 'Lot UV 80 Ambato.', '0373434568', 'member.5b@fed-agri.mg', '2026-01-01'),
                                                                                                                              (2, 1, 'Nom membre 6', 'Prénom membre 6', '1998-08-22', 'Female', 'Lot UV 6 Ambato.', '0372234568', 'member.6b@fed-agri.mg', '2026-01-01'),
                                                                                                                              (2, 1, 'Nom membre 7', 'Prénom membre 7', '1998-01-31', 'Male', 'Lot UV 7 Ambato.', '0374234568', 'member.7b@fed-agri.mg', '2026-01-01'),
                                                                                                                              (2, 1, 'Nom membre 8', 'Prénom membre 8', '1975-08-20', 'Male', 'Lot UV 8 Ambato.', '0370234568', 'member.8b@fed-agri.mg', '2026-01-01');

-- MEMBRES COLLECTIVITE 3
INSERT INTO member (id_collective, id_trade, last_name, first_name, birth_date, gender, address, phone, email, join_date) VALUES
                                                                                                                              (3, 5, 'Nom membre 9',  'Prénom membre 9',  '1988-01-02', 'Male',   'Lot 33 J Antsirabe', '034034567',  'member.9@fed-agri.mg',  '2026-01-01'),
                                                                                                                              (3, 2, 'Nom membre 10', 'Prénom membre 10', '1982-03-05', 'Male',   'Lot 2 J Antsirabe',  '0338634567', 'member.10@fed-agri.mg', '2026-01-01'),
                                                                                                                              (3, 3, 'Nom membre 11', 'Prénom membre 11', '1992-03-12', 'Male',   'Lot 8 KM Antsirabe', '0338234567', 'member.11@fed-agri.mg', '2026-01-01'),
                                                                                                                              (3, 4, 'Nom membre 12', 'Prénom membre 12', '1988-05-10', 'Female', 'Lot A K 50 Antsirabe','0382334567', 'member.12@fed-agri.mg', '2026-01-01'),
                                                                                                                              (3, 5, 'Nom membre 13', 'Prénom membre 13', '1999-08-11', 'Male',   'Lot UV 80 Antsirabe','0373365567', 'member.13@fed-agri.mg', '2026-01-01'),
                                                                                                                              (3, 5, 'Nom membre 14', 'Prénom membre 14', '1998-08-09', 'Female', 'Lot UV 6 Antsirabe', '0378234567', 'member.14@fed-agri.mg', '2026-01-01'),
                                                                                                                              (3, 5, 'Nom membre 15', 'Prénom membre 15', '1998-01-13', 'Male',   'Lot UV 7 Antsirabe', '0374914567', 'member.15@fed-agri.mg', '2026-01-01'),
                                                                                                                              (3, 5, 'Nom membre 16', 'Prénom membre 16', '1975-08-02', 'Male',   'Lot UV 8 Antsirabe', '0370634567', 'member.16@fed-agri.mg', '2026-01-01');

-- COMPTES col-1
INSERT INTO account (id_collective, account_type, balance) VALUES (1, 'Cash', 0);
INSERT INTO account (id_collective, account_type, account_holder, mobile_money_service, phone_number, balance)
VALUES (1, 'Mobile_Money', 'Mpanorina', 'Orange_Money', '0370489612', 0);

-- COMPTES col-2
INSERT INTO account (id_collective, account_type, balance) VALUES (2, 'Cash', 0);
INSERT INTO account (id_collective, account_type, account_holder, mobile_money_service, phone_number, balance)
VALUES (2, 'Mobile_Money', 'Dobo voalohany', 'Orange_Money', '0320489612', 0);

-- COMPTES col-3
INSERT INTO account (id_collective, account_type, balance) VALUES (3, 'Cash', 0);
INSERT INTO account (id_collective, account_type, account_holder, bank_name, bank_account_number, balance)
VALUES (3, 'Bank', 'Koto', 'BMOI', '000040000112345678901200', 0);
INSERT INTO account (id_collective, account_type, account_holder, bank_name, bank_account_number, balance)
VALUES (3, 'Bank', 'Naivo', 'BRED', '000080000345678901235800', 0);
INSERT INTO account (id_collective, account_type, account_holder, mobile_money_service, phone_number, balance)
VALUES (3, 'Mobile_Money', 'Kolo', 'Mvola', '0341889612', 0);

-- COTISATIONS
INSERT INTO contribution (id_collective, contribution_type, frequency, amount, due_date, is_paid, is_active) VALUES
                                                                                                                 (1, 'Periodic', 'Annual',  200000, '2026-01-01', FALSE, TRUE),
                                                                                                                 (1, 'One_time', NULL,       20000, '2026-04-30', FALSE, TRUE),
                                                                                                                 (2, 'Periodic', 'Annual',  200000, '2026-01-01', FALSE, TRUE),
                                                                                                                 (2, 'Periodic', 'Annual',  100000, '2025-01-01', FALSE, FALSE),
                                                                                                                 (3, 'Periodic', 'Monthly',  25000, '2026-04-01', FALSE, TRUE);

-- NOUVEAUX MEMBRES col-1
INSERT INTO member (id_collective, id_trade, last_name, first_name, birth_date, gender, address, phone, email, join_date) VALUES
                                                                                                                              (1, 1, 'Nouveau A1', 'Junior', '2000-01-01', 'Male', 'Antananarivo', '0340000101', 'new1a@fed.mg', '2026-04-01'),
                                                                                                                              (1, 1, 'Nouveau A2', 'Junior', '2001-02-01', 'Female', 'Antananarivo', '0340000102', 'new2a@fed.mg', '2026-04-01'),
                                                                                                                              (1, 1, 'Nouveau A3', 'Junior', '2000-03-01', 'Male', 'Antananarivo', '0340000103', 'new3a@fed.mg', '2026-05-01'),
                                                                                                                              (1, 1, 'Nouveau A4', 'Junior', '2001-04-01', 'Female', 'Antananarivo', '0340000104', 'new4a@fed.mg', '2026-06-01');

-- NOUVEAUX MEMBRES col-2
INSERT INTO member (id_collective, id_trade, last_name, first_name, birth_date, gender, address, phone, email, join_date) VALUES
                                                                                                                              (2, 1, 'Nouveau B1', 'Junior', '2000-01-01', 'Male', 'Ambatondrazaka', '0340000201', 'new1b@fed.mg', '2026-03-01'),
                                                                                                                              (2, 1, 'Nouveau B2', 'Junior', '2001-02-01', 'Female', 'Ambatondrazaka', '0340000202', 'new2b@fed.mg', '2026-03-01'),
                                                                                                                              (2, 1, 'Nouveau B3', 'Junior', '2000-03-01', 'Male', 'Ambatondrazaka', '0340000203', 'new3b@fed.mg', '2026-03-01');

-- NOUVEAUX MEMBRES col-3
INSERT INTO member (id_collective, id_trade, last_name, first_name, birth_date, gender, address, phone, email, join_date) VALUES
                                                                                                                              (3, 5, 'Nouveau C1', 'Junior', '2000-01-01', 'Male', 'Antsirabe', '0340000301', 'new1c@fed.mg', '2026-01-01'),
                                                                                                                              (3, 5, 'Nouveau C2', 'Junior', '2001-02-01', 'Female', 'Antsirabe', '0340000302', 'new2c@fed.mg', '2026-02-01'),
                                                                                                                              (3, 5, 'Nouveau C3', 'Junior', '2000-03-01', 'Male', 'Antsirabe', '0340000303', 'new3c@fed.mg', '2026-02-01'),
                                                                                                                              (3, 5, 'Nouveau C4', 'Junior', '2001-04-01', 'Female', 'Antsirabe', '0340000304', 'new4c@fed.mg', '2026-03-01'),
                                                                                                                              (3, 5, 'Nouveau C5', 'Junior', '2000-05-01', 'Male', 'Antsirabe', '0340000305', 'new5c@fed.mg', '2026-03-01'),
                                                                                                                              (3, 5, 'Nouveau C6', 'Junior', '2001-06-01', 'Female', 'Antsirabe', '0340000306', 'new6c@fed.mg', '2026-03-01');