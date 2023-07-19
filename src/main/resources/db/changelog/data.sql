INSERT INTO account VALUES
    ('df604273-4286-4b05-afa8-820860f9dbc5','f6b632f4-fa55-11ed-be56-0242ac120002'),
    ('378a0210-fa56-11ed-be56-0242ac120002', '3ec31d96-fa56-11ed-be56-0242ac120002');

INSERT INTO balance( amount, currency, account_id) VALUES (9.99, 'USD' ,'df604273-4286-4b05-afa8-820860f9dbc5');

INSERT INTO transaction(id, account_id, currency, amount, direction, description) VALUES
            ('065c6828-1cbe-4be3-bd74-cfb831faefdb', 'df604273-4286-4b05-afa8-820860f9dbc5', 'USD', 100, 'OUT', 'Out money');