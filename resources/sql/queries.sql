-- :name create-user! :! :n
-- :doc creates a new user record
INSERT INTO system_user
(username, first_name, last_name, email, admin, is_active)
VALUES (:username, :first_name, :last_name, :email, :admin, :is_active)

-- :name update-user-password! :! :n
-- :doc update an existing users password
UPDATE system_user
SET password = :password
WHERE username = :username

-- :name update-user! :! :n
-- :doc update an existing user record
UPDATE system_user
SET username = :username,
    first_name = :first_name,
    last_name = :last_name,
    email = :email,
    admin = :admin,
    is_active = :is_active
WHERE id = :id

-- :name get-user :? :1
-- :doc retrieve a user given the id.
SELECT id, username, first_name, last_name , email, admin, last_login, is_active
FROM system_user
WHERE id = :id

-- :name get-username :? :1
-- :doc retrieve a user given the username.
SELECT id, username, first_name, last_name , email, admin, last_login, is_active
FROM system_user
WHERE username = :username

-- :name get-user-password :? :1
-- :doc retrieve a user given the username.
SELECT id, username, password
FROM system_user
WHERE username = :username

-- :name get-all-users :? :*
-- :doc retrieve all users.
SELECT id, username, first_name, last_name , email, admin, last_login, is_active
FROM system_user

-- :name delete-user! :! :n
-- :doc delete a user given the id
DELETE FROM system_user
WHERE id = :id

-- :name create-tweet! :<! :1
-- :doc creates a new tweet
INSERT INTO tweet
(posted_date, text, username)
VALUES (:posted_date, :text, :username)
RETURNING id;

-- :name update-tweet! :! :n
-- :doc update an existing tweet
UPDATE tweet
SET text = :text
WHERE id = :id

-- :name get-tweet :? :1
-- :doc retrieve a tweet given the id.
SELECT * FROM tweet
WHERE id = :id

-- :name get-user-tweets :? :*
-- :doc retrieve users tweets.
SELECT * FROM tweet
WHERE username = :username

-- :name get-recent-tweets :? :*
-- :doc retrieve 10 most recent tweets.
SELECT * FROM tweet
ORDER BY posted_date DESC
LIMIT 10

-- :name delete-tweet! :! :n
-- :doc delete a tweet given the id
DELETE FROM tweet
WHERE id = :id
