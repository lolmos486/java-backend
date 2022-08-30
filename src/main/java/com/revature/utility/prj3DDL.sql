DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS users_with_accounts;
DROP TABLE IF EXISTS transaction_descriptions;
DROP TABLE IF EXISTS status_types;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS accounts;
DROP TABLE IF EXISTS account_types;
DROP TABLE IF EXISTS roles;

CREATE TABLE roles(
	id SERIAL PRIMARY KEY,
	role_name VARCHAR(30)
);

CREATE TABLE account_types (
	id SERIAL PRIMARY KEY,
	type_name VARCHAR(30)
);

CREATE TABLE accounts (
	id SERIAL PRIMARY KEY,
	type_id INT NOT NULL,
	balance BIGINT NOT NULL,
	CONSTRAINT fk_account_type_id
  		FOREIGN KEY (type_id) REFERENCES "account_types" (id)
);

CREATE TABLE users(
	id SERIAL PRIMARY KEY,
	first_name VARCHAR(30) NOT NULL,
	last_name VARCHAR(30),
	email VARCHAR(30) NOT NULL UNIQUE,
	pass BYTEA NOT NULL,
	phone VARCHAR(12) NOT NULL,
	role_id INT NOT NULL,
	primary_acc INT,
	tokenvalue BYTEA,
	CONSTRAINT fk_user_roles_id
  		FOREIGN KEY (role_id) REFERENCES "roles" (id),
  	CONSTRAINT fk_primary_acc
  		FOREIGN KEY (primary_acc) REFERENCES "accounts" (id)
);

CREATE TABLE users_with_accounts(
	account_id INT NOT NULL,
	user_id INT NOT NULL,
	PRIMARY KEY (account_id, user_id),
	CONSTRAINT fk_account1_id
  		FOREIGN KEY (account_id) REFERENCES "accounts" (id),
  	CONSTRAINT fk_user_id
  		FOREIGN KEY (user_id) REFERENCES "users" (id)
);

CREATE TABLE transaction_descriptions (
	id SERIAL PRIMARY KEY,
	description VARCHAR(30)
);

CREATE TABLE status_types(
	id SERIAL PRIMARY KEY,
	type_name VARCHAR(30)
);

CREATE TABLE transactions(
	id SERIAL PRIMARY KEY,
	requester_id INT NOT NULL,
	sending_id INT NOT NULL,
	receiving_id INT NOT NULL,
	req_time TIMESTAMP NOT NULL DEFAULT Now(),
	res_time TIMESTAMP,
	amount BIGINT NOT NULL,
	status_id INT NOT NULL,
	desc_id INT NOT NULL,
	receiving_email VARCHAR(30) NOT NULL,
	CONSTRAINT fk_trx_sending_id
  		FOREIGN KEY (sending_id) REFERENCES "accounts" (id),
  	CONSTRAINT fk_trx_receiving_id
  		FOREIGN KEY (receiving_id) REFERENCES "accounts" (id),
  	CONSTRAINT fk_description_id
  		FOREIGN KEY (desc_id) REFERENCES "transaction_descriptions" (id),
  	CONSTRAINT fk_requester_id
  		FOREIGN KEY (requester_id) REFERENCES "users" (id),
  	CONSTRAINT fk_status_id
  		FOREIGN KEY (status_id) REFERENCES "status_types" (id)
);


  ----------------------------------------INSERTS-------------------------------------------------
INSERT INTO roles (role_name) VALUES ('USER'),('EMPLOYEE'),('ATM');

INSERT INTO account_types (type_name) VALUES ('CHECKING'), ('SAVINGS');

INSERT INTO accounts (type_id, balance) VALUES
	(1, 5000),(1, 5000000), (1, 70000),(1, 5000),(1, 70000),(1, 200000),
	(2, 50000),(2, 500000), (2, 50000),(2, 50000),(2, 700000),(2, 2000000),
	(1, 50000),(2, 500000), (1, 50000),(2, 50000),(1, 700000),(2, 2000000),
	(1, 50000),(2, 500000), (1, 50000),(2, 50000);

INSERT INTO users (first_name, last_name, email, pass, phone, role_id, primary_acc) VALUES
	('John', 'Doe', 'jd80@a.ca', 'Password123!', '555-555-5000', 1, 1),
	('Jane', 'Doe', 'jd81@a.ca', 'Password123!', '555-555-5001', 1, 4),
	('Johny', 'Doe', 'jd05@a.ca', 'Password123!', '555-555-5002', 1, 13),
	('Valentin', 'Vlad', 'vv@a.ca', 'Password123!', '555-555-5555', 1,19),
	('Jonathan', 'Doe', 'jd800@a.ca', 'Password123!', '555-555-5556', 2, 20);

INSERT INTO users (id,first_name, last_name, email, pass, phone, role_id) VALUES
	(999999,'ATM', '001', 'rvm001@a.ca', '-----------------', '555-555-5555', 3),
	(888888,'ATM', '002', 'rvm002@a.ca', '-----------------', '555-555-5555', 3);

INSERT INTO users_with_accounts (account_id, user_id) VALUES
	(1,1),(7,1),(2,1), (8,1),(3,1),(9,1),(4,2),(5,2),(6,2), (10,2),(11,2),(12,2),(13,3),(14,3),(15,3), (16,3),(17,3),(18,3),(19,4), (20,4),(21,5), (22,5);

INSERT INTO transaction_descriptions (description) VALUES ('Income'),('Transfer'),('Send'),('Request');

INSERT INTO status_types (type_name) VALUES ('PENDING'), ('APPROVED'), ('DECLINED');

--Insert transactions ---
INSERT INTO transactions (requester_id, sending_id, receiving_id, req_time, res_time, status_id, amount, desc_id, receiving_email) VALUES
	(1, 2, 4, '2022-08-05 18:52:05.429', '2022-08-05 18:52:05.430', 2, 11500, 2, 'jd81@a.ca'),
	(1, 2, 5, '2022-08-07 18:52:05.429', '2022-08-07 18:52:05.430', 2, 12500, 1, 'jd81@a.ca'),
	(1, 2, 10, '2022-08-09 18:52:05.429', '2022-08-09 18:52:05.430', 2, 34500, 2, 'jd81@a.ca'),
	(1, 2, 11, '2022-08-13 18:52:05.429', '2022-08-13 18:52:05.430', 2, 12500, 1, 'jd81@a.ca'),
	(1, 2, 12, '2022-08-18 18:52:05.429', '2022-08-18 18:52:05.430', 2, 333500, 2, 'jd81@a.ca'),
	(1, 2, 13, '2022-08-22 18:52:05.429', '2022-08-22 18:52:05.430', 2, 22500, 1, 'jd81@a.ca'),
	(1, 2, 14, '2022-08-25 18:52:05.429', '2022-08-25 18:52:05.430', 2, 111500, 2, 'jd81@a.ca'),
	(1, 2, 15, '2022-08-28 18:52:05.429', '2022-08-28 18:52:05.430', 2, 50000, 1, 'jd81@a.ca'),
	(1, 7, 1, '2022-08-04 18:52:05.429', '2022-08-04 18:52:05.430', 2, 5000, 2, 'jd80@a.ca'),
	(1, 8, 3, '2022-08-02 18:52:05.429', '2022-08-02 18:52:05.430', 2, 50000, 1, 'jd80@a.ca'),
	(1, 2, 9, '2022-08-01 18:52:05.429', '2022-08-01 18:52:05.430', 2, 50670, 2, 'jd80@a.ca'),
	(1, 4, 5, '2022-08-07 18:52:05.429', '2022-08-07 18:52:05.430', 2, 504560, 1, 'jd80@a.ca'),
	(2, 6, 10, '2022-08-04 18:52:05.429', '2022-08-04 18:52:05.430', 2, 343500, 2, 'jd81@a.ca'),
	(2, 12, 11, '2022-08-06 18:52:05.429', '2022-08-06 18:52:05.430', 2, 345500, 1, 'jd81@a.ca'),
	(2, 4, 10, '2022-08-08 18:52:05.429', '2022-08-08 18:52:05.430', 2, 550780, 2, 'jd81@a.ca'),
	(2, 5, 11, '2022-08-19 18:52:05.429', '2022-08-19 18:52:05.430', 2, 50760, 1, 'jd81@a.ca'),
		(1, 2, 4, '2022-07-05 18:52:05.429', '2022-07-05 18:52:05.430', 2, 11500, 2, 'jd81@a.ca'),
	(1, 2, 5, '2022-07-07 18:52:05.429', '2022-07-07 18:52:05.430', 2, 125300, 1, 'jd81@a.ca'),
	(1, 2, 10, '2022-07-09 18:52:05.429', '2022-07-09 18:52:05.430', 2, 343500, 2, 'jd81@a.ca'),
	(1, 2, 11, '2022-07-13 18:52:05.429', '2022-07-13 18:52:05.430', 2, 123500, 1, 'jd81@a.ca'),
	(1, 2, 12, '2022-07-18 18:52:05.429', '2022-07-18 18:52:05.430', 2, 3335300, 2, 'jd81@a.ca'),
	(1, 2, 13, '2022-07-22 18:52:05.429', '2022-07-22 18:52:05.430', 2, 223500, 1, 'jd81@a.ca'),
	(1, 2, 14, '2022-07-25 18:52:05.429', '2022-07-25 18:52:05.430', 2, 3111500, 2, 'jd81@a.ca'),
	(1, 2, 15, '2022-07-28 18:52:05.429', '2022-07-28 18:52:05.430', 2, 350000, 1, 'jd81@a.ca'),
	(1, 7, 1, '2022-07-04 18:52:05.429', '2022-07-04 18:52:05.430', 2, 35000, 2, 'jd80@a.ca'),
	(1, 8, 3, '2022-07-02 18:52:05.429', '2022-07-02 18:52:05.430', 2, 350000, 1, 'jd80@a.ca'),
	(1, 2, 9, '2022-07-01 18:52:05.429', '2022-07-01 18:52:05.430', 2, 350670, 2, 'jd80@a.ca'),
	(1, 4, 5, '2022-07-07 18:52:05.429', '2022-07-07 18:52:05.430', 2, 3504560, 1, 'jd80@a.ca'),
	(2, 6, 10, '2022-07-04 18:52:05.429', '2022-07-04 18:52:05.430', 2, 3343500, 2, 'jd81@a.ca'),
	(2, 12, 11, '2022-07-06 18:52:05.429', '2022-07-06 18:52:05.430', 2, 3345500, 1, 'jd81@a.ca'),
	(2, 4, 10, '2022-07-08 18:52:05.429', '2022-07-08 18:52:05.430', 2, 3550780, 2, 'jd81@a.ca'),
	(2, 5, 11, '2022-07-19 18:52:05.429', '2022-07-19 18:52:05.430', 2, 350760, 1, 'jd81@a.ca'),
		(1, 2, 4, '2022-06-05 18:52:05.429', '2022-06-05 18:52:05.430', 2, 211500, 2, 'jd81@a.ca'),
	(1, 2, 5, '2022-06-07 18:52:05.429', '2022-06-07 18:52:05.430', 2, 1253020, 1, 'jd81@a.ca'),
	(1, 2, 10, '2022-06-09 18:52:05.429', '2022-06-09 18:52:05.430', 2, 3435020, 2, 'jd81@a.ca'),
	(1, 2, 11, '2022-06-13 18:52:05.429', '2022-06-13 18:52:05.430', 2, 1235020, 1, 'jd81@a.ca'),
	(1, 2, 12, '2022-06-18 18:52:05.429', '2022-06-18 18:52:05.430', 2, 33353020, 2, 'jd81@a.ca'),
	(1, 2, 13, '2022-06-22 18:52:05.429', '2022-06-22 18:52:05.430', 2, 2235002, 1, 'jd81@a.ca'),
	(1, 2, 14, '2022-06-25 18:52:05.429', '2022-06-25 18:52:05.430', 2, 31112500, 2, 'jd81@a.ca'),
	(1, 2, 15, '2022-06-28 18:52:05.429', '2022-06-28 18:52:05.430', 2, 35000, 1, 'jd81@a.ca'),
	(1, 7, 1, '2022-06-04 18:52:05.429', '2022-06-04 18:52:05.430', 2, 3500, 2, 'jd80@a.ca'),
	(1, 8, 3, '2022-06-02 18:52:05.429', '2022-06-02 18:52:05.430', 2, 35000, 1, 'jd80@a.ca'),
	(1, 2, 9, '2022-06-01 18:52:05.429', '2022-06-01 18:52:05.430', 2, 35670, 2, 'jd80@a.ca'),
	(1, 4, 5, '2022-06-07 18:52:05.429', '2022-06-07 18:52:05.430', 2, 354560, 1, 'jd80@a.ca'),
	(2, 6, 10, '2022-06-04 18:52:05.429', '2022-06-04 18:52:05.430', 2, 343500, 2, 'jd81@a.ca'),
	(2, 12, 11, '2022-06-06 18:52:05.429', '2022-06-06 18:52:05.430', 2, 345500, 1, 'jd81@a.ca'),
	(2, 4, 10, '2022-06-08 18:52:05.429', '2022-06-08 18:52:05.430', 2, 350780, 2, 'jd81@a.ca'),
	(2, 5, 11, '2022-06-19 18:52:05.429', '2022-06-19 18:52:05.430', 2, 30760, 1, 'jd81@a.ca'),
		(1, 2, 4, '2022-05-05 18:52:05.429', '2022-05-05 18:52:05.430', 2, 211500, 2, 'jd81@a.ca'),
	(1, 2, 5, '2022-05-07 18:52:05.429', '2022-05-07 18:52:05.430', 2, 1253020, 1, 'jd81@a.ca'),
	(1, 2, 10, '2022-05-09 18:52:05.429', '2022-05-09 18:52:05.430', 2, 3435020, 2, 'jd81@a.ca'),
	(1, 2, 11, '2022-05-13 18:52:05.429', '2022-05-13 18:52:05.430', 2, 1235020, 1, 'jd81@a.ca'),
	(1, 2, 12, '2022-05-18 18:52:05.429', '2022-05-18 18:52:05.430', 2, 33353020, 2, 'jd81@a.ca'),
	(1, 2, 13, '2022-05-22 18:52:05.429', '2022-05-22 18:52:05.430', 2, 2235002, 1, 'jd81@a.ca'),
	(1, 2, 14, '2022-05-25 18:52:05.429', '2022-05-25 18:52:05.430', 2, 31112500, 2, 'jd81@a.ca'),
	(1, 2, 15, '2022-05-28 18:52:05.429', '2022-05-28 18:52:05.430', 2, 35000, 1, 'jd81@a.ca'),
	(1, 7, 1, '2022-05-04 18:52:05.429', '2022-05-04 18:52:05.430', 2, 3500, 2, 'jd80@a.ca'),
	(1, 8, 3, '2022-05-02 18:52:05.429', '2022-05-02 18:52:05.430', 2, 35000, 1, 'jd80@a.ca'),
	(1, 2, 9, '2022-05-01 18:52:05.429', '2022-05-01 18:52:05.430', 2, 35670, 2, 'jd80@a.ca'),
	(1, 4, 5, '2022-05-07 18:52:05.429', '2022-05-07 18:52:05.430', 2, 354560, 1, 'jd80@a.ca'),
	(2, 6, 10, '2022-05-04 18:52:05.429', '2022-05-04 18:52:05.430', 2, 343500, 2, 'jd81@a.ca'),
	(2, 12, 11, '2022-05-06 18:52:05.429', '2022-05-06 18:52:05.430', 2, 345500, 1, 'jd81@a.ca'),
	(2, 4, 10, '2022-05-08 18:52:05.429', '2022-05-08 18:52:05.430', 2, 350780, 2, 'jd81@a.ca'),
	(2, 5, 11, '2022-05-19 18:52:05.429', '2022-05-19 18:52:05.430', 2, 30760, 1, 'jd81@a.ca'),
	(1, 2, 4, '2022-04-05 18:52:05.429', '2022-04-05 18:52:05.430', 2, 21500, 2, 'jd81@a.ca'),
	(1, 2, 5, '2022-04-07 18:52:05.429', '2022-04-07 18:52:05.430', 2, 1253020, 1, 'jd81@a.ca'),
	(1, 2, 10, '2022-04-09 18:52:05.429', '2022-04-09 18:52:05.430', 2, 335020, 2, 'jd81@a.ca'),
	(1, 2, 11, '2022-04-13 18:52:05.429', '2022-04-13 18:52:05.430', 2, 1235020, 1, 'jd81@a.ca'),
	(1, 2, 12, '2022-04-18 18:52:05.429', '2022-04-18 18:52:05.430', 2, 3333020, 2, 'jd81@a.ca'),
	(1, 2, 13, '2022-04-22 18:52:05.429', '2022-04-22 18:52:05.430', 2, 223002, 1, 'jd81@a.ca'),
	(1, 2, 14, '2022-04-25 18:52:05.429', '2022-04-25 18:52:05.430', 2, 31112500, 2, 'jd81@a.ca'),
	(1, 2, 15, '2022-04-28 18:52:05.429', '2022-04-28 18:52:05.430', 2, 3500, 1, 'jd81@a.ca'),
	(1, 7, 1, '2022-04-04 18:52:05.429', '2022-04-04 18:52:05.430', 2, 3500, 2, 'jd80@a.ca'),
	(1, 8, 3, '2022-04-02 18:52:05.429', '2022-04-02 18:52:05.430', 2, 3500, 1, 'jd80@a.ca'),
	(1, 2, 9, '2022-04-01 18:52:05.429', '2022-04-01 18:52:05.430', 2, 35670, 2, 'jd80@a.ca'),
	(1, 4, 5, '2022-04-07 18:52:05.429', '2022-04-07 18:52:05.430', 2, 35460, 1, 'jd80@a.ca'),
	(2, 6, 10, '2022-04-04 18:52:05.429', '2022-04-04 18:52:05.430', 2, 34300, 2, 'jd81@a.ca'),
	(2, 12, 11, '2022-04-06 18:52:05.429', '2022-04-06 18:52:05.430', 2, 345500, 1, 'jd81@a.ca'),
	(2, 4, 10, '2022-04-08 18:52:05.429', '2022-04-08 18:52:05.430', 2, 35780, 2, 'jd81@a.ca'),
	(2, 5, 11, '2022-04-19 18:52:05.429', '2022-04-19 18:52:05.430', 2, 3060, 1, 'jd81@a.ca');

------------SELECTS-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------
SELECT * FROM transactions;

--Select Jon's accounts --
SELECT  act.type_name, a.type_id, a.balance/100 as amount_in_dollars, a.id as acc_id, uwa.user_id
	FROM account_types act
	JOIN accounts a ON a.type_id = act.id
	JOIN users_with_accounts uwa ON a.id = uwa.account_id
	JOIN users u ON u.id = uwa.user_id
	WHERE u.email = 'jd80@a.ca';

---select getAll trx
SELECT t.id, t.requester_id, t.sending_id, t.receiving_id, t.req_time, t.res_time, t.amount, t.receiving_email,  concat_ws(' ', u.first_name,u.last_name) as initiated_by, st.type_name, td.description
	FROM transactions t
	JOIN users u ON t.requester_id = u.id
	JOIN status_types st ON t.status_id  = st.id
	JOIN transaction_descriptions td ON t.desc_id  = td.id;

SELECT a.*, at2.type_name
	FROM accounts a
	Join account_types at2 ON a.type_id = at2.id
	WHERE a.id = 2;

--get default accounts by email
SELECT u.primary_acc
	FROM users u
	WHERE u.email = 'jd80@a.ca';

UPDATE  users
	SET primary_acc = 1
	WHERE email = 'jd80@a.ca';

SELECT balance
	FROM accounts a
	WHERE a.id IN(1,2,3);

SELECT * FROM transactions;

UPDATE accounts
	SET balance = balance + 1
	WHERE id = 1;

--check if owner 1 has account 23
SELECT 3 IN(
	SELECT uwa .account_id
		FROM users_with_accounts uwa
		WHERE uwa.user_id = 1
		) as owns_accounts;

--check if account 1 has balance > 500;
SELECT (
		SELECT balance FROM accounts a WHERE a.id = 2
		) >= 5000000000 as can_withdraw;

SELECT uwa .account_id
	FROM users_with_accounts uwa
	WHERE uwa.user_id = 1;

--monthly income query
SELECT  SUM(t.amount) AS monthly_income, TO_CHAR(t.res_time,'Month') as mon, TO_CHAR(t.res_time,'YYYY') as yyyy
	FROM transactions t
	WHERE t.receiving_id = 5
	GROUP BY TO_CHAR(t.res_time,'Month'), TO_CHAR(t.res_time,'YYYY');

--monthly income query (dao function takes an Account ID and two ints)
SELECT  SUM(t.amount) AS monthly_income, DATE_TRUNC('month', t.res_time) as mon, DATE_TRUNC('year', t.res_time) as yyyy
	FROM transactions t
	WHERE t.receiving_id = 4 AND DATE_TRUNC('month', t.res_time) = '2022-08-01 00:00:00.000' AND DATE_TRUNC('year', t.res_time) = '2023-01-01 00:00:00.000'
	GROUP BY DATE_TRUNC('month', t.res_time), DATE_TRUNC('year', t.res_time) ;



--monthly income query
SELECT  SUM(t.amount) AS monthly_income, TO_CHAR(t.res_time,'Month') as mon, TO_CHAR(t.res_time,'YYYY') as yyyy
	FROM transactions t
	WHERE t.receiving_id = 5
	GROUP BY TO_CHAR(t.res_time,'Month'), TO_CHAR(t.res_time,'YYYY');

--monthly income query (dao function takes an Account ID and two ints)
SELECT  SUM(t.amount) AS monthly_income, DATE_TRUNC('month', t.res_time) as mon, DATE_TRUNC('year', t.res_time) as yyyy
	FROM transactions t
	WHERE t.receiving_id = 4 AND DATE_TRUNC('month', t.res_time) = '2022-08-01 00:00:00.000' AND DATE_TRUNC('year', t.res_time) = '2022-01-01 00:00:00.000'
	GROUP BY DATE_TRUNC('month', t.res_time), DATE_TRUNC('year', t.res_time) ;

--monthly income query (dao function takes an User ID and two ints)
SELECT  SUM(t.amount) AS monthly_income, DATE_TRUNC('month', t.res_time) as mon, DATE_TRUNC('year', t.res_time) as yyyy
	FROM transactions t
	WHERE t.receiving_id IN(
		SELECT uwa .account_id
			FROM users_with_accounts uwa
			WHERE uwa.user_id = 2
	) AND DATE_TRUNC('month', t.res_time) <= '2022-08-01 00:00:00.000' AND DATE_TRUNC('year', t.res_time) <= '2022-01-01 00:00:00.000'
	GROUP BY DATE_TRUNC('month', t.res_time), DATE_TRUNC('year', t.res_time) ;

SELECT EXTRACT(YEAR FROM Now()) as curr_year, EXTRACT(MONTH FROM Now()) curr_month;

SELECT * FROM transactions t ;
SELECT * FROM accounts a ;
SELECT * FROM users u ;

SELECT u.email
	FROM users u
	JOIN users_with_accounts uwa ON uwa.user_id = u.id
	JOIN transactions t ON uwa.account_id = t.sending_id
	WHERE t.id = 6;