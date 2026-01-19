INSERT INTO users (id, email, password, first_name, last_name, is_deleted)
VALUES (1, 'test_user@example.com', '$2a$10$2H7oXmD0.z6A8X8zY6X9Oe5vX5X5X5X5X5X5X5X5X5X5X5X5X5', 'John', 'Doe', false);

INSERT INTO users_roles (user_id, role_id) VALUES (1, 1);

INSERT INTO shopping_carts (id, user_id, is_deleted) VALUES (1, 1, false);
