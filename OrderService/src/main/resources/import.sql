INSERT INTO orders (user_id, status, created_at, updated_at) VALUES (1,  'COMPLETED', '2021-01-01', '2021-01-01');
INSERT INTO orders (user_id, status, created_at, updated_at) VALUES (2, 'PENDING', '2021-01-01', null);

INSERT INTO order_lines (order_id, product_id, quantity, unit_price) VALUES (1, 1, 1,699.99);
INSERT INTO order_lines (order_id, product_id, quantity, unit_price) VALUES (1, 2, 2,1299.99);
INSERT INTO order_lines (order_id, product_id, quantity, unit_price) VALUES (2, 3, 3,199.99);
INSERT INTO order_lines (order_id, product_id, quantity, unit_price) VALUES (2, 4, 4,149.99);