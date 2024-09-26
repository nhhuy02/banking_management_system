-- Table storing loan type information
CREATE TABLE loan_type
(
    loan_type_id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    loan_type_name             VARCHAR(255)  NOT NULL UNIQUE,                                  -- Loan type name
    annual_interest_rate       DECIMAL(5, 2) NOT NULL CHECK (annual_interest_rate >= 0),       -- Annual interest rate (%)
    past_due_interest_rate     DECIMAL(5, 2) NOT NULL CHECK (past_due_interest_rate >= 0),     -- Default past-due interest rate (e.g. 150%)
    late_payment_interest_rate DECIMAL(5, 2) NOT NULL CHECK (late_payment_interest_rate >= 0), -- Default late payment interest rate (e.g. 10% per annum)
    prepayment_penalty_rate    DECIMAL(5, 2) NOT NULL CHECK (prepayment_penalty_rate >= 0),    -- Default prepayment penalty rate (e.g. 3% of the outstanding balance)
    max_loan_amount            BIGINT        NOT NULL CHECK (max_loan_amount >= 0),            -- Maximum loan amount (VND)
    max_loan_term_months       INT           NOT NULL CHECK (max_loan_term_months > 0),        -- Maximum loan term (months)
    requires_collateral        BOOLEAN       NOT NULL DEFAULT FALSE,                           -- Indicates whether collateral is required
    review_time_days           INT           NOT NULL CHECK (review_time_days > 0),            -- Time required to approve loan application (days)
    description                TEXT,                                                           -- Description of the loan type may include an estimated interest rate
    is_active                  BOOLEAN       NOT NULL DEFAULT TRUE,                            -- Indicates if the loan type is currently active
    -- -- --
    created_by                 VARCHAR(255)  NOT NULL DEFAULT 'SYSTEM',
    created_date               TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_by           VARCHAR(255)           DEFAULT 'SYSTEM',
    last_modified_date         TIMESTAMP              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Table storing loan application information
CREATE TABLE loan_application
(
    loan_application_id       BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id               BIGINT                                                                     NOT NULL,                                      -- Customer ID (reference from Customer Service)
    account_id                BIGINT,                                                                                                                   -- Account ID for receiving disbursement and making repayments
    monthly_income            BIGINT                                                                     NOT NULL CHECK (monthly_income >= 0),          -- Monthly income
    occupation                VARCHAR(255)                                                               NOT NULL,                                      -- Customer's occupation
    loan_type_id              BIGINT                                                                     NOT NULL,                                      -- Loan type ID (reference to loan_type table)
    desired_loan_amount       BIGINT                                                                     NOT NULL CHECK (desired_loan_amount >= 0),     -- Desired loan amount (VND)
    desired_loan_term_months  INT                                                                        NOT NULL CHECK (desired_loan_term_months > 0), -- Desired loan term in months
    repayment_method          ENUM ('EQUAL_INSTALLMENTS', 'REDUCING_BALANCE')                            NOT NULL DEFAULT 'EQUAL_INSTALLMENTS',         -- Repayment method: equal installments or reducing balance
    desired_disbursement_date DATE                                                                       NOT NULL,                                      -- Desired disbursement date
    interest_rate_type        ENUM ('FIXED', 'FLOATING')                                                 NOT NULL,                                      -- Fixed or floating interest rate
    loan_purpose              TEXT,                                                                                                                     -- Customer's loan purpose
    credit_score              INT CHECK (credit_score >= 0 AND credit_score <= 750),                                                                    -- Customer credit score (range: 0-750)
    application_status        ENUM ('PENDING', 'REVIEWING', 'APPROVED', 'REJECTED', 'DOCUMENT_REQUIRED') NOT NULL DEFAULT 'PENDING',                    -- Application status
    submission_date           DATE                                                                       NOT NULL,                                      -- Loan application submission date
    review_due_date           DATE,                                                                                                                     -- Expected date for bank staff to complete the review
    review_date               DATE,                                                                                                                     -- Loan application review date
    review_notes              TEXT,                                                                                                                     -- Review notes, reason for the decision
    -- -- --
    created_by                VARCHAR(255)                                                               NOT NULL DEFAULT 'SYSTEM',
    created_date              TIMESTAMP                                                                  NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_by          VARCHAR(255)                                                                        DEFAULT 'SYSTEM',
    last_modified_date        TIMESTAMP                                                                           DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Table to store documents related to the loan
CREATE TABLE document
(
    document_id         BIGINT AUTO_INCREMENT PRIMARY KEY,           -- Document ID
    loan_application_id BIGINT,                                      -- Reference to loan_application
    document_type       ENUM ('COLLATERAL_PROOF', 'OTHER') NOT NULL, -- Type of document
    file_name           VARCHAR(255)                       NOT NULL, -- Name of the uploaded file
    file_type           VARCHAR(100)                       NOT NULL, -- Type of the file (e.g., application/pdf)
    file_size           BIGINT                             NOT NULL, -- Size of the file in bytes
    file_path           VARCHAR(255)                       NOT NULL, -- Path to the uploaded file
    description         TEXT,                                        -- Description of the document
    -- -- --
    created_by          VARCHAR(255)                       NOT NULL DEFAULT 'SYSTEM',
    created_date        TIMESTAMP                          NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_by    VARCHAR(255)                                DEFAULT 'SYSTEM',
    last_modified_date  TIMESTAMP                                   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Table to store information about collateral
CREATE TABLE collateral
(
    collateral_id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    loan_id                BIGINT,                                                             -- References to the loan
    loan_application_id    BIGINT,                                                             -- References to the loan application
    document_id            BIGINT UNIQUE,                                                      -- References to the document
    collateral_type        VARCHAR(255)                             NOT NULL,                  -- Type of collateral (e.g., house, car, valuable papers)
    collateral_value       BIGINT CHECK (collateral_value >= 0),                               -- Collateral value (VND)
    description            TEXT,                                                               -- Detailed description of the collateral
    status                 ENUM ('ACTIVE', 'RECLAIMED', 'RELEASED') NOT NULL DEFAULT 'ACTIVE', -- Collateral status
    reclaim_date           DATE,                                                               -- Date when the collateral is reclaimed
    reason_for_reclamation VARCHAR(255),                                                       -- Reason for the collateral being reclaimed
    release_date           DATE,                                                               -- Date when the collateral is released
    -- -- --
    created_by             VARCHAR(255)                             NOT NULL DEFAULT 'SYSTEM',
    created_date           TIMESTAMP                                NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_by       VARCHAR(255)                                      DEFAULT 'SYSTEM',
    last_modified_date     TIMESTAMP                                         DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Table to store loan details
CREATE TABLE loan
(
    loan_id                      BIGINT AUTO_INCREMENT PRIMARY KEY,
    loan_application_id          BIGINT                                          NOT NULL UNIQUE,                                   -- Reference to loan_application
    customer_id                  BIGINT                                          NOT NULL,                                          -- Customer ID (reference from Customer Service)
    loan_contract_no             CHAR(20)                                        NOT NULL UNIQUE
        CHECK (REGEXP_LIKE(loan_contract_no, '^BNK-LN-[0-9]{4}-[0-9]{2}-[0-9]{5}$')),                                               -- Loan contract number, e.g., BNK-LN-2024-09-01234
    customer_confirmation_status ENUM ('PENDING', 'CONFIRMED', 'REJECTED')       NOT NULL DEFAULT 'PENDING',                        -- Borrower's confirmation status
    customer_confirmation_date   TIMESTAMP,                                                                                         -- The date the borrower confirms the loan
    loan_type_id                 BIGINT                                          NOT NULL,                                          -- Loan type ID (reference to loan_type)
    loan_amount                  BIGINT                                          NOT NULL CHECK (loan_amount >= 0),                 -- Loan amount (VND)
    interest_rate_type           ENUM ('FIXED', 'FLOATING')                      NOT NULL,                                          -- Interest rate type: fixed or floating
    current_interest_rate_id     BIGINT                                          NOT NULL,                                          -- Reference to interest_rate
    repayment_method             ENUM ('EQUAL_INSTALLMENTS', 'REDUCING_BALANCE') NOT NULL DEFAULT 'EQUAL_INSTALLMENTS',             -- Repayment method: equal installments or reducing balance
    loan_term_months             INT                                             NOT NULL CHECK (loan_term_months > 0),             -- Loan term in months
    disbursement_date            DATE                                            NOT NULL,                                          -- Loan disbursement date
    maturity_date                DATE                                            NOT NULL,                                          -- Loan maturity date
    settlement_date              DATE,                                                                                              -- Loan settlement date
    renewal_count                INT                                             NOT NULL DEFAULT 0 CHECK (renewal_count >= 0),     -- Number of renewals, default is 0 (no renewals yet)
    remaining_balance            BIGINT                                          NOT NULL CHECK (remaining_balance >= 0),           -- Remaining loan balance (VND)
    total_paid_amount            BIGINT                                          NOT NULL DEFAULT 0 CHECK (total_paid_amount >= 0), -- Total amount paid (VND)
    is_bad_debt                  BOOLEAN                                         NOT NULL DEFAULT FALSE,
    bad_debt_date                DATE,
    bad_debt_reason              VARCHAR(255),
    debt_classification          ENUM (
        'NORMAL',                                                                                                                   -- Group 1 (NORMAL - Standard debt)
        'SPECIAL_MENTION',                                                                                                          -- Group 2 (SPECIAL_MENTION - Watchlist debt)
        'SUBSTANDARD',                                                                                                              -- Group 3 (SUBSTANDARD - Substandard debt)
        'DOUBTFUL',                                                                                                                 -- Group 4 (DOUBTFUL - Doubtful debt)
        'LOSS'                                                                                                                      -- Group 5 (LOSS - Loss debt)
        )                                                                        NOT NULL DEFAULT 'NORMAL',
    status                       ENUM (
        'PENDING_CUSTOMER_APPROVAL',                                                                                                -- The loan has been approved by the system and is pending customer approval
        'CANCELLED',                                                                                                                -- The loan was cancelled before becoming active due to customer rejection or other reasons
        'ACTIVE',                                                                                                                   -- Loan is active and being repaid
        'SETTLED_ON_TIME',                                                                                                          -- Loan settled on time
        'SETTLED_EARLY',                                                                                                            -- Loan settled early
        'SETTLED_LATE',                                                                                                             -- Loan settled late
        'PAST_DUE',                                                                                                                 -- Loan is past due and not yet repaid
        'RENEWAL'                                                                                                                   -- Loan has been renewed
        )                                                                        NOT NULL DEFAULT 'ACTIVE',
    -- -- --
    created_by                   VARCHAR(255)                                    NOT NULL DEFAULT 'SYSTEM',
    created_date                 TIMESTAMP                                       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_by             VARCHAR(255)                                             DEFAULT 'SYSTEM',
    last_modified_date           TIMESTAMP                                                DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Table to store the history of loan interest rate changes
CREATE TABLE loan_interest_rate
(
    loan_interest_rate_id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    loan_id                    BIGINT        NOT NULL,

    annual_interest_rate       DECIMAL(5, 2) NOT NULL CHECK (annual_interest_rate >= 0),       -- Annual interest rate (%)
    past_due_interest_rate     DECIMAL(5, 2) NOT NULL CHECK (past_due_interest_rate >= 0),     -- Past due interest rate (e.g. 150%)
    late_payment_interest_rate DECIMAL(5, 2) NOT NULL CHECK (late_payment_interest_rate >= 0), -- Late payment interest rate (e.g. 10% per annum)
    prepayment_penalty_rate    DECIMAL(5, 2) NOT NULL CHECK (prepayment_penalty_rate >= 0),    -- Prepayment penalty rate (e.g. 3% of the remaining balance paid before the due date)
    effective_from             DATE          NOT NULL,                                         -- Date when the interest rate becomes effective
    effective_to               DATE,                                                           -- Date when the interest rate expires
    is_current                 BOOLEAN       NOT NULL DEFAULT FALSE,                           -- Indicates if this is the current interest rate
    -- -- --
    created_by                 VARCHAR(255)  NOT NULL DEFAULT 'SYSTEM',
    created_date               TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_by           VARCHAR(255)           DEFAULT 'SYSTEM',
    last_modified_date         TIMESTAMP              DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Table managing periodic loan repayment schedules
CREATE TABLE loan_repayment
(
    loan_payment_id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    loan_id                      BIGINT                              NOT NULL,                                                     -- Reference to the loan
    principal_amount             BIGINT                              NOT NULL CHECK (principal_amount >= 0),                       -- Principal amount to be repaid
    interest_amount              BIGINT                              NOT NULL CHECK (interest_amount >= 0),                        -- Interest amount to be repaid
    late_payment_interest_amount BIGINT                              NOT NULL DEFAULT 0 CHECK (late_payment_interest_amount >= 0), -- Late payment interest amount
    total_amount                 BIGINT                              NOT NULL CHECK (total_amount >= 0),                           -- Total amount to be repaid
    payment_due_date             DATE                                NOT NULL,                                                     -- Repayment due date
    actual_payment_date          DATE,                                                                                             -- Actual repayment date
    account_id                   BIGINT,                                                                                           -- Account used for repayment
    payment_status               ENUM ('PENDING', 'PAID', 'OVERDUE') NOT NULL DEFAULT 'PENDING',                                   -- Status of the repayment schedule
    is_late                      BOOLEAN                             NOT NULL DEFAULT FALSE,                                       -- Flag indicating whether the payment is late
    -- -- --
    created_by                   VARCHAR(255)                        NOT NULL DEFAULT 'SYSTEM',
    created_date                 TIMESTAMP                           NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_by             VARCHAR(255)                                 DEFAULT 'SYSTEM',
    last_modified_date           TIMESTAMP                                    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Table storing loan settlements
CREATE TABLE loan_settlement
(
    loan_settlement_id           BIGINT AUTO_INCREMENT PRIMARY KEY,
    loan_id                      BIGINT                                       NOT NULL,                                                     -- Reference to the loan
    settlement_date              DATE                                         NOT NULL,                                                     -- Loan settlement date
    past_due_interest_amount     BIGINT                                       NOT NULL DEFAULT 0 CHECK (past_due_interest_amount >= 0),     -- Past-due interest amount
    late_payment_interest_amount BIGINT                                       NOT NULL DEFAULT 0 CHECK (late_payment_interest_amount >= 0), -- Late payment interest amount
    prepayment_penalty_amount    BIGINT                                       NOT NULL DEFAULT 0 CHECK (prepayment_penalty_amount >= 0),    -- Prepayment penalty amount
    settlement_amount            BIGINT                                       NOT NULL CHECK (settlement_amount >= 0),                      -- Total settlement amount
    settlement_status            ENUM ('PENDING', 'EARLY', 'ON_TIME', 'LATE') NOT NULL,                                                     -- Settlement status: early, on-time, or late
    -- -- --
    created_by                   VARCHAR(255)                                 NOT NULL DEFAULT 'SYSTEM',
    created_date                 TIMESTAMP                                    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_modified_by             VARCHAR(255)                                          DEFAULT 'SYSTEM',
    last_modified_date           TIMESTAMP                                             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Add foreign key to loan_application table
ALTER TABLE loan_application
    ADD CONSTRAINT fk_loan_application_loan_type
        FOREIGN KEY (loan_type_id) REFERENCES loan_type (loan_type_id);

-- Add foreign key to document table
ALTER TABLE document
    ADD CONSTRAINT fk_document_loan_application
        FOREIGN KEY (loan_application_id) REFERENCES loan_application (loan_application_id);

-- Add foreign keys to loan table
ALTER TABLE loan
    ADD CONSTRAINT fk_loan_loan_application
        FOREIGN KEY (loan_application_id) REFERENCES loan_application (loan_application_id),
    ADD CONSTRAINT fk_loan_loan_type
        FOREIGN KEY (loan_type_id) REFERENCES loan_type (loan_type_id),
    ADD CONSTRAINT fk_loan_interest_rate
        FOREIGN KEY (current_interest_rate_id) REFERENCES loan_interest_rate (loan_interest_rate_id);

-- Add foreign key to loan_interest_rate table
ALTER TABLE loan_interest_rate
    ADD CONSTRAINT fk_loan_interest_rate_loan
        FOREIGN KEY (loan_id) REFERENCES loan (loan_id);

-- Add foreign keys to collateral table
ALTER TABLE collateral
    ADD CONSTRAINT fk_collateral_loan
        FOREIGN KEY (loan_id) REFERENCES loan (loan_id),
    ADD CONSTRAINT fk_collateral_loan_application
        FOREIGN KEY (loan_application_id) REFERENCES loan_application (loan_application_id),
    ADD CONSTRAINT fk_collateral_document
        FOREIGN KEY (document_id) REFERENCES document (document_id);

-- Add foreign key to loan_repayment table
ALTER TABLE loan_repayment
    ADD CONSTRAINT fk_loan_repayment_loan
        FOREIGN KEY (loan_id) REFERENCES loan (loan_id);

-- Add foreign key to loan_settlement table
ALTER TABLE loan_settlement
    ADD CONSTRAINT fk_loan_settlement_loan
        FOREIGN KEY (loan_id) REFERENCES loan (loan_id);