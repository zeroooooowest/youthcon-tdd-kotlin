DROP TABLE IF EXISTS tdd.review;

CREATE TABLE tdd.review
(
    ID BIGINT PRIMARY KEY,
    CONTENT VARCHAR(255),
    PHONE_NUMBER VARCHAR(255),
    SENT BIT(1)
);