-- Java Essentials → Programming
INSERT INTO books_categories (book_id, category_id)
SELECT id, 7 FROM books WHERE isbn = '111-ABC';

-- Spring Boot Starter → Programming
INSERT INTO books_categories (book_id, category_id)
SELECT id, 7 FROM books WHERE isbn = '222-DEF';

-- Docker for Developers → Programming
INSERT INTO books_categories (book_id, category_id)
SELECT id, 7 FROM books WHERE isbn = '333-GHI';
