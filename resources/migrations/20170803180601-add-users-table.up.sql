CREATE TABLE user
(id VARCHAR(20) PRIMARY KEY,
 username VARCHAR(30),
 first_name VARCHAR(30),
 last_name VARCHAR(30),
 email VARCHAR(30),
 admin BOOLEAN,
 last_login TIME,
 is_active BOOLEAN,
 pass VARCHAR(300));
