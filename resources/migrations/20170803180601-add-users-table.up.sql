CREATE TABLE system_user
(id BIGSERIAL PRIMARY KEY,
 username VARCHAR(30),
 first_name VARCHAR(30),
 last_name VARCHAR(30),
 email VARCHAR(30),
 admin BOOLEAN,
 last_login TIMESTAMP,
 is_active BOOLEAN,
 pass VARCHAR(300));
