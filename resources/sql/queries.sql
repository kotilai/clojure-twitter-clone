-- :name create-user! :! :n
-- :doc creates a new user record
INSERT INTO users
(id, first_name, last_name, email, pass)
VALUES (:id, :first_name, :last_name, :email, :pass)

-- :name update-user! :! :n
-- :doc update an existing user record
UPDATE users
SET first_name = :first_name, last_name = :last_name, email = :email
WHERE id = :id

-- :name get-user :? :1
-- :doc retrieve a user given the id.
SELECT * FROM users
WHERE id = :id

-- :name delete-user! :! :n
-- :doc delete a user given the id
DELETE FROM users
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

-- :name get-tweets :? :*
-- :doc retrieve all tweets.
SELECT * FROM tweet

-- :name delete-tweet! :! :n
-- :doc delete a tweet given the id
DELETE FROM tweet
WHERE id = :id
