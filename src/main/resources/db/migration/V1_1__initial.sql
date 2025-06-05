
CREATE TABLE author
(
    id   UUID NOT NULL,
    name VARCHAR(255),
    CONSTRAINT pk_author PRIMARY KEY (id)
);
CREATE TABLE books
(
    id        UUID         NOT NULL,
    amount    VARCHAR(255) NOT NULL,
    author_id UUID,
    CONSTRAINT pk_books PRIMARY KEY (id)
);

ALTER TABLE books
    ADD CONSTRAINT FK_BOOKS_ON_AUTHOR FOREIGN KEY (author_id) REFERENCES author (id);
CREATE TABLE members
(
    id                UUID NOT NULL,
    name              VARCHAR(255),
    "membership-date" TIMESTAMP,
    CONSTRAINT pk_members PRIMARY KEY (id)
);
INSERT INTO members (id, name, "membership-date")
VALUES
    ('a123e456-789b-12d3-a456-426614174000', 'John Doe', '1995-03-15'),
    ('b223e456-789b-12d3-a456-426614174000', 'Alice Smith', '2000-07-22'),
    ('c323e456-789b-12d3-a456-426614174000', 'Bob Johnson', '1990-12-05'),
    ('d423e456-789b-12d3-a456-426614174000', 'Emma Brown', '1998-04-18'),
    ('e523e456-789b-12d3-a456-426614174000', 'Charlie Davis', '1993-01-29');