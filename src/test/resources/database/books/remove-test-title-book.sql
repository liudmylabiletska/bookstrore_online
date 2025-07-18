-- Видалити зв’язки з категоріями для книги
DELETE FROM books_categories
WHERE book_id = (
  SELECT id FROM books
  WHERE title = 'Test Title'
    AND price = 300.00
    AND isbn = '11111'
);

-- Видалити саму книгу
DELETE FROM books
WHERE title = 'Test Title'
  AND price = 300.00
  AND isbn = '11111';
