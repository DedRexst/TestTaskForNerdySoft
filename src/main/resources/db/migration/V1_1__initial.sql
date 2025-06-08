CREATE TABLE author
(
    id   UUID NOT NULL,
    name VARCHAR(255),
    CONSTRAINT pk_author PRIMARY KEY (id)
);

ALTER TABLE author ADD CONSTRAINT uc_author_name UNIQUE (name);
CREATE TABLE books
(
    id        UUID         NOT NULL,
    title     VARCHAR(255) NOT NULL,
    author_id UUID,
    amount    INT,
    CONSTRAINT pk_books PRIMARY KEY (id)
);
ALTER TABLE books
    ADD CONSTRAINT uc_books_title UNIQUE (title);

ALTER TABLE books
    ADD CONSTRAINT FK_BOOKS_ON_AUTHOR FOREIGN KEY (author_id) REFERENCES author (id);

CREATE TABLE members
(
    id                UUID NOT NULL,
    name              VARCHAR(255),
    "membership-date" TIMESTAMP,
    CONSTRAINT pk_members PRIMARY KEY (id)
);

ALTER TABLE members
    ADD CONSTRAINT uc_members_name UNIQUE (name);
CREATE TABLE borrowed_books
(
    book_id   UUID NOT NULL,
    member_id UUID NOT NULL,
    CONSTRAINT pk_borrowed_books PRIMARY KEY (book_id, member_id)
);

ALTER TABLE borrowed_books
    ADD CONSTRAINT fk_borboo_on_book FOREIGN KEY (book_id) REFERENCES books (id);

ALTER TABLE borrowed_books
    ADD CONSTRAINT fk_borboo_on_member FOREIGN KEY (member_id) REFERENCES members (id);
INSERT INTO members (id, name, "membership-date")
VALUES
    ('a123e456-789b-12d3-a456-426614174000', 'John Doe', '1995-03-15'),
    ('b223e456-789b-12d3-a456-426614174000', 'Alice Smith', '2000-07-22'),
    ('c323e456-789b-12d3-a456-426614174000', 'Bob Johnson', '1990-12-05'),
    ('d423e456-789b-12d3-a456-426614174000', 'Emma Brown', '1998-04-18'),
    ('e523e456-789b-12d3-a456-426614174000', 'Charlie Davis', '1993-01-29');
INSERT INTO author (id, name)
VALUES
    ('88b67a74-678d-4d58-8376-4b31ffa24c35', 'John Ronald Reuel Tolkien'),
    ('efe69216-1129-4f3b-85a3-6d446e4662b6', 'Joanne Rowling'),
    ('7c492cbf-47e2-4302-892e-31b23a144f28', 'Terry Pratchett');
INSERT INTO books (id, title, author_id, amount)
VALUES
    ('cb7d0d45-598a-4c28-82d5-989281352605', 'Night Watch', '7c492cbf-47e2-4302-892e-31b23a144f28', 5),
    ('b66e6265-aacd-49fe-89d9-6bd878b04a0c', 'The Lord of the Rings: The Fellowship of the Ring', '88b67a74-678d-4d58-8376-4b31ffa24c35', 6),
    ('212ee667-6875-4d5b-af87-017fa83ef7b9', 'The Lord of the Rings: The Two Towers', '88b67a74-678d-4d58-8376-4b31ffa24c35', 6),
    ('f2e93873-00ba-4972-aa06-8280f049cf55', 'The Lord of the Rings: The Return of the King', '88b67a74-678d-4d58-8376-4b31ffa24c35', 6),
    ('2366e67a-c6e2-4380-a182-b6956a777bfb', 'Harry Potter and the Philosopher''s Stone ', 'efe69216-1129-4f3b-85a3-6d446e4662b6', 7),
    ('288a310e-80d5-4951-8482-9a681b3baf6f', 'Harry Potter and the Chamber of Secrets', 'efe69216-1129-4f3b-85a3-6d446e4662b6', 7),
    ('39f14cbf-b8d1-42b1-8ec2-39aefed5984b', 'Harry Potter and the Goblet of Fire', 'efe69216-1129-4f3b-85a3-6d446e4662b6', 7),
    ('58ba836e-0f53-461d-8db8-56ef3e96ef52', 'Harry Potter and the Order of the Phoenix', 'efe69216-1129-4f3b-85a3-6d446e4662b6', 7),
    ('743535bb-b6e3-416f-b510-854866764037', 'Harry Potter and the Half-Blood Prince', 'efe69216-1129-4f3b-85a3-6d446e4662b6', 7),
    ('3a986abb-d694-4e45-b921-e8ed076ce3d5', 'Harry Potter and the Deathly Hallows', 'efe69216-1129-4f3b-85a3-6d446e4662b6', 7),
    ('c106f5c8-2d51-4e65-8470-f88f9e27889f', 'Harry Potter and the Prisoner of Azkaban', 'efe69216-1129-4f3b-85a3-6d446e4662b6', 7);


