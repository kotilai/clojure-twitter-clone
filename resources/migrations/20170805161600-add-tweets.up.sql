CREATE TABLE tweet
(id VARCHAR(20) PRIMARY KEY AUTO_INCREMENT,
 -- user_id VARCHAR(20),
 posted_date TIMESTAMP,
 -- reply_to_id VARCHAR(20),
 text VARCHAR(140)
 -- FOREIGN KEY (user_id)
 --    REFERENCES user(id),
--FOREIGN KEY (reply_to_id)
--    REFERENCES tweet(id));
);
