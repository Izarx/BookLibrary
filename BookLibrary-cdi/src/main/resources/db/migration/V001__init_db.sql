CREATE TABLE authors (
    author_id SERIAL PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    create_date TIMESTAMP DEFAULT NOW()
);

CREATE TABLE books (
    book_id SERIAL PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    isbn INTEGER UNIQUE CONSTRAINT books_isbn NOT NULL,
    publisher VARCHAR(50),
    create_date TIMESTAMP DEFAULT NOW()
);

CREATE TABLE authors_books (
    author_id INTEGER
        CONSTRAINT authors_books_author_id_fkey
            REFERENCES authors ON DELETE CASCADE,
    book_id INTEGER
        CONSTRAINT authors_books_book_id_fkey
            REFERENCES books ON DELETE CASCADE
);

CREATE TABLE reviews (
    review_id SERIAL PRIMARY KEY,
    commenter_name VARCHAR(50) NOT NULL ,
    comment TEXT,
    rating INTEGER CONSTRAINT reviews_rating CHECK (rating > 0 and rating <= 5),
    create_date TIMESTAMP DEFAULT NOW(),
    book_id INTEGER
        CONSTRAINT book_fkey
            REFERENCES books ON DELETE CASCADE
);