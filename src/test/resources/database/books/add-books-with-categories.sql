INSERT INTO books (id, title, author, isbn, price, description, cover_image, is_deleted)
VALUES (1, 'Test Book', 'Author', 'isbn-111', 10.00, 'Description', 'image.jpg', false);

INSERT INTO books_categories (book_id, category_id)
VALUES (1, 1);
