-- Insert Loan Types
INSERT INTO loan_type (loan_type_name, annual_interest_rate, past_due_interest_rate, late_payment_interest_rate, prepayment_penalty_rate, max_loan_amount, max_loan_term_months, requires_collateral, review_time_days, description)
VALUES
    ('Home Loan', 7.50, 150.00, 10.00, 3.00, 500000000, 240, TRUE, 14, 'Loan for purchasing or renovating a home.'),
    ('Car Loan', 9.00, 160.00, 12.00, 4.00, 300000000, 60, TRUE, 7, 'Loan for purchasing a vehicle.');

-- Insert Loan Applications
INSERT INTO loan_application (customer_id, account_id, monthly_income, occupation, loan_type_id, desired_loan_amount, desired_loan_term_months, repayment_method, desired_disbursement_date, interest_rate_type, loan_purpose, credit_score, application_status, submission_date, review_due_date)
VALUES
    -- Customer 1: Home Loan Application
    (1001, 2001, 20000000, 'Software Engineer', 1, 300000000, 120, 'EQUAL_INSTALLMENTS', '2024-10-01', 'FIXED', 'Home Renovation', 700, 'PENDING', '2024-09-01', '2024-09-15'),

    -- Customer 1: Car Loan Application
    (1001, 2001, 20000000, 'Software Engineer', 2, 150000000, 48, 'REDUCING_BALANCE', '2024-11-01', 'FIXED', 'Purchase New Car', 720, 'PENDING', '2024-09-05', '2024-09-12'),

    -- Customer 2: Home Loan Application
    (1002, 2002, 15000000, 'Teacher', 1, 200000000, 180, 'EQUAL_INSTALLMENTS', '2024-10-15', 'FLOATING', 'Buy a Bigger House', 680, 'PENDING', '2024-09-10', '2024-09-24'),

    -- Customer 2: Car Loan Application
    (1002, 2002, 15000000, 'Teacher', 2, 120000000, 36, 'EQUAL_INSTALLMENTS', '2024-11-20', 'FIXED', 'Purchase Used Car', 690, 'PENDING', '2024-09-12', '2024-09-19');

-- Insert Documents for Loan Applications
INSERT INTO document (loan_application_id, document_type, file_name, file_type, file_size, file_path, description)
VALUES
    -- Documents for Loan Application 1 (Home Loan)
    (1, 'COLLATERAL_PROOF', 'house_deed.pdf', 'application/pdf', 102400, '/documents/house_deed.pdf', 'Proof of ownership for the house.'),

    -- Documents for Loan Application 2 (Car Loan)
    (2, 'COLLATERAL_PROOF', 'car_registration.pdf', 'application/pdf', 51200, '/documents/car_registration.pdf', 'Proof of ownership for the car.'),

    -- Documents for Loan Application 3 (Home Loan)
    (3, 'COLLATERAL_PROOF', 'house_deed.pdf', 'application/pdf', 102400, '/documents/house_deed.pdf', 'Proof of ownership for the house.'),

    -- Documents for Loan Application 4 (Car Loan)
    (4, 'COLLATERAL_PROOF', 'car_registration.pdf', 'application/pdf', 51200, '/documents/car_registration.pdf', 'Proof of ownership for the car.');

-- Insert Collateral for Loan Applications
INSERT INTO collateral (loan_application_id, document_id, collateral_type, collateral_value, description, status, reclaim_date, reason_for_reclamation, release_date)
VALUES
    -- Collateral for Loan Application 1 (Home Loan)
    (1, 1, 'House', 300000000, '3-bedroom house in downtown area.', 'ACTIVE', NULL, NULL, NULL),

    -- Collateral for Loan Application 2 (Car Loan)
    (2, 2, 'Car', 150000000, 'Toyota Camry 2020.', 'ACTIVE', NULL, NULL, NULL),

    -- Collateral for Loan Application 3 (Home Loan)
    (3, 3, 'House', 200000000, '4-bedroom house in suburban area.', 'ACTIVE', NULL, NULL, NULL),

    -- Collateral for Loan Application 4 (Car Loan)
    (4, 4, 'Car', 120000000, 'Honda Civic 2018.', 'ACTIVE', NULL, NULL, NULL);


-- Insert Loans (current_interest_rate_id set to NULL initially)
INSERT INTO loan (loan_application_id, customer_id, loan_contract_no, customer_confirmation_status, customer_confirmation_date, loan_type_id, loan_amount, interest_rate_type, repayment_method, loan_term_months, disbursement_date, maturity_date, settlement_date, renewal_count, remaining_balance, total_paid_amount, is_bad_debt, bad_debt_date, bad_debt_reason, debt_classification, status)
VALUES
    -- Loan for Loan Application 1 (Home Loan)
    (1, 1001, 'BNK-LN-2024-09-00001', 'PENDING', NULL, 1, 300000000, 'FIXED', 'EQUAL_INSTALLMENTS', 120, '2024-10-01', '2034-10-01', NULL, 0, 300000000, 0, FALSE, NULL, NULL, 'NORMAL', 'ACTIVE'),

    -- Loan for Loan Application 2 (Car Loan)
    (2, 1001, 'BNK-LN-2024-09-00002', 'PENDING', NULL, 2, 150000000, 'FIXED', 'REDUCING_BALANCE', 48, '2024-11-01', '2028-11-01', NULL, 0, 150000000, 0, FALSE, NULL, NULL, 'NORMAL', 'ACTIVE'),

    -- Loan for Loan Application 3 (Home Loan)
    (3, 1002, 'BNK-LN-2024-09-00003', 'PENDING', NULL, 1, 200000000, 'FLOATING', 'EQUAL_INSTALLMENTS', 180, '2024-10-15', '2034-10-15', NULL, 0, 200000000, 0, FALSE, NULL, NULL, 'NORMAL', 'ACTIVE'),

    -- Loan for Loan Application 4 (Car Loan)
    (4, 1002, 'BNK-LN-2024-09-00004', 'PENDING', NULL, 2, 120000000, 'FIXED', 'EQUAL_INSTALLMENTS', 36, '2024-11-20', '2027-11-20', NULL, 0, 120000000, 0, FALSE, NULL, NULL, 'NORMAL', 'ACTIVE');


-- Insert Loan Interest Rates
INSERT INTO loan_interest_rate (loan_id, annual_interest_rate, past_due_interest_rate, late_payment_interest_rate, prepayment_penalty_rate, effective_from, effective_to)
VALUES
    -- Interest Rate for Loan 1
    (1, 7.50, 150.00, 10.00, 3.00, '2024-10-01', NULL),

    -- Interest Rate for Loan 2
    (2, 9.00, 160.00, 12.00, 4.00, '2024-11-01', NULL),

    -- Interest Rate for Loan 3
    (3, 7.75, 155.00, 10.50, 3.50, '2024-10-15', NULL),

    -- Interest Rate for Loan 4
    (4, 9.25, 165.00, 12.50, 4.50, '2024-11-20', NULL);

-- Update Loans with current_interest_rate_id
UPDATE loan
SET current_interest_rate_id = (SELECT loan_interest_rate_id FROM loan_interest_rate WHERE loan_interest_rate.loan_id = loan.loan_id)
WHERE loan_id IN (1, 2, 3, 4);


-- Insert Loan Repayments for Loan 1
INSERT INTO loan_repayment (loan_id, principal_amount, interest_amount, late_payment_interest_amount, total_amount, payment_due_date, actual_payment_date, account_id, payment_status, is_late)
VALUES
    (1, 2500000, 1750000, 0, 4250000, '2025-10-01', '2025-10-01', 2001, 'PAID', FALSE),
    (1, 2500000, 1650000, 0, 4150000, '2025-11-01', '2025-11-02', 2001, 'PAID', TRUE),
    (1, 2500000, 1550000, 0, 4050000, '2025-12-01', NULL, 2001, 'PENDING', FALSE);

-- Insert Loan Repayments for Loan 2
INSERT INTO loan_repayment (loan_id, principal_amount, interest_amount, late_payment_interest_amount, total_amount, payment_due_date, actual_payment_date, account_id, payment_status, is_late)
VALUES
    (2, 3125000, 1125000, 0, 4250000, '2025-11-01', '2025-11-01', 2001, 'PAID', FALSE),
    (2, 3125000, 1125000, 0, 4250000, '2025-12-01', NULL, 2001, 'PENDING', FALSE),
    (2, 3125000, 1125000, 0, 4250000, '2026-01-01', NULL, 2001, 'PENDING', FALSE);

-- Insert Loan Repayments for Loan 3
INSERT INTO loan_repayment (loan_id, principal_amount, interest_amount, late_payment_interest_amount, total_amount, payment_due_date, actual_payment_date, account_id, payment_status, is_late)
VALUES
    (3, 1111111, 1444444, 0, 2555555, '2025-10-15', '2025-10-15', 2002, 'PAID', FALSE),
    (3, 1111111, 1444444, 0, 2555555, '2025-11-15', NULL, 2002, 'PENDING', FALSE),
    (3, 1111111, 1444444, 0, 2555555, '2025-12-15', NULL, 2002, 'PENDING', FALSE);

-- Insert Loan Repayments for Loan 4
INSERT INTO loan_repayment (loan_id, principal_amount, interest_amount, late_payment_interest_amount, total_amount, payment_due_date, actual_payment_date, account_id, payment_status, is_late)
VALUES
    (4, 3333333, 750000, 0, 4083333, '2025-11-20', '2025-11-20', 2002, 'PAID', FALSE),
    (4, 3333333, 720000, 0, 4053333, '2025-12-20', NULL, 2002, 'PENDING', FALSE),
    (4, 3333333, 690000, 0, 4023333, '2026-01-20', NULL, 2002, 'PENDING', FALSE);

-- Insert Loan Settlements for Loan 1
INSERT INTO loan_settlement (loan_id, settlement_date, past_due_interest_amount, late_payment_interest_amount, prepayment_penalty_amount, settlement_amount, settlement_status)
VALUES
    (1, '2034-10-01', 0, 0, 0, 300000000, 'ON_TIME');

-- Insert Loan Settlements for Loan 2
INSERT INTO loan_settlement (loan_id, settlement_date, past_due_interest_amount, late_payment_interest_amount, prepayment_penalty_amount, settlement_amount, settlement_status)
VALUES
    (2, '2028-11-01', 0, 0, 0, 150000000, 'ON_TIME');

