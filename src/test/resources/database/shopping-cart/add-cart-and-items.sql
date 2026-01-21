INSERT INTO roles (id, role_name) VALUES (1, 'USER');
INSERT INTO users (id, email, password, first_name, last_name, is_deleted)
VALUES (1, 'user@example.com', '$2a$10$2H7oXmD0.z6A8X8zY6X9Oe5vX5X5X5X5X5X5X5X5X5X5X5', 'John', 'Doe', false);
INSERT INTO users_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO books (id, title, author, isbn, price, is_deleted)
VALUES (1, 'Effective Java', 'Joshua Bloch', '978-0134685991', 45.00, false);
INSERT INTO shopping_carts (id, is_deleted) VALUES (1, false);
INSERT INTO cart_items (id, shopping_cart_id, book_id, quantity)
VALUES (1, 1, 1, 1);
