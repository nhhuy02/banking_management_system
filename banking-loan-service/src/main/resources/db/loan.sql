insert into loan_service.loan (loan_id, loan_application_id, account_id, loan_contract_no, customer_confirmation_status, customer_confirmation_date, loan_type_id, loan_amount, interest_rate_type, current_interest_rate_id, repayment_method, loan_term_months, disbursement_date, maturity_date, settlement_date, renewal_count, remaining_balance, total_paid_amount, is_bad_debt, bad_debt_date, bad_debt_reason, debt_classification, status, created_by, created_date, last_modified_by, last_modified_date)
values  (1, 1, 2001, 'BNK-LN-2024-09-00001', 'PENDING', null, 1, 300000000.00, 'FIXED', 1, 'EQUAL_INSTALLMENTS', 120, '2024-10-03', '2034-10-01', null, 0, 300000000.00, 0.00, 0, null, null, 'NORMAL', 'ACTIVE', 'SYSTEM', '2024-10-03 03:29:21', 'SYSTEM', '2024-10-03 11:16:16'),
        (2, 2, 2001, 'BNK-LN-2024-09-00002', 'PENDING', null, 2, 150000000.00, 'FIXED', 2, 'REDUCING_BALANCE', 48, '2024-11-01', '2028-11-01', null, 0, 150000000.00, 0.00, 0, null, null, 'NORMAL', 'PENDING', 'SYSTEM', '2024-10-03 03:29:21', 'SYSTEM', '2024-10-03 04:15:58'),
        (3, 3, 2002, 'BNK-LN-2024-09-00003', 'PENDING', null, 1, 200000000.00, 'FLOATING', 3, 'EQUAL_INSTALLMENTS', 180, '2024-10-15', '2034-10-15', null, 0, 200000000.00, 0.00, 0, null, null, 'NORMAL', 'ACTIVE', 'SYSTEM', '2024-10-03 03:29:21', 'SYSTEM', '2024-10-03 03:29:21'),
        (4, 4, 2002, 'BNK-LN-2024-09-00004', 'PENDING', null, 2, 120000000.00, 'FIXED', 4, 'EQUAL_INSTALLMENTS', 36, '2024-11-20', '2027-11-20', null, 0, 120000000.00, 0.00, 0, null, null, 'NORMAL', 'ACTIVE', 'SYSTEM', '2024-10-03 03:29:21', 'SYSTEM', '2024-10-03 03:29:21'),
        (7, 7, 66, 'BNK-LN-2024-10-40839', 'PENDING', null, 1, 50000000.00, 'FIXED', 5, 'EQUAL_INSTALLMENTS', 12, '2024-10-04', '2025-10-01', null, 0, 45833333.33, 4479166.67, 0, null, null, 'NORMAL', 'ACTIVE', 'SYSTEM', '2024-10-03 17:59:37', 'SYSTEM', '2024-10-04 16:34:39'),
        (9, 6, 66, 'BNK-LN-2024-10-18123', 'PENDING', null, 2, 100000000.00, 'FIXED', 6, 'EQUAL_INSTALLMENTS', 60, '2024-10-04', '2028-10-01', null, 0, 100000000.00, 0.00, 0, null, null, 'NORMAL', 'ACTIVE', 'SYSTEM', '2024-10-03 18:09:41', 'SYSTEM', '2024-10-04 09:36:36');