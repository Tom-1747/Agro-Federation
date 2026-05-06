

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
