-- :name create-user! :! :n
-- :doc creates a new user record
INSERT INTO user
(first_name, last_name, email, pass)
VALUES (:first_name, :last_name, :email, :pass)

-- :name update-user! :! :n
-- :doc update an existing user record
UPDATE user
SET first_name = :first_name, last_name = :last_name, email = :email
WHERE id = :id

-- :name get-user :? :1
-- :doc retrieve a user given the id.
SELECT * FROM user
WHERE id = :id

-- :name get-all-users :? :*
-- :doc retrieve all users.
SELECT * FROM user

-- :name delete-user! :! :n
-- :doc delete a user given the id
DELETE FROM user
WHERE id = :id

-- :name create-tweet! :! :n
-- :doc creates a new tweet
INSERT INTO tweet
(posted_date, text, username)
VALUES (:posted_date, :text, :username)

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
ORDER BY id ASC
LIMIT 10

-- :name delete-tweet! :! :n
-- :doc delete a tweet given the id
DELETE FROM tweet
WHERE id = :id
