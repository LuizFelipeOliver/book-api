CREATE TABLE books(
    id  BigInt PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    author VARCHAR(100) NOT NULL,
    available Boolean DEFAULT(FALSE)
)
