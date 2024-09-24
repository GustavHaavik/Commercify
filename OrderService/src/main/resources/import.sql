INSERT INTO orders (user_id, status, created_date, updated_date) VALUES (1,  'COMPLETED', '2021-01-01', '2021-01-01');
INSERT INTO orders (user_id, status, created_date, updated_date) VALUES (2, 'PENDING', '2021-01-01', '2021-01-01');

INSERT INTO order_lines (order_id, product_id, quantity) VALUES (1, 1, 1);
INSERT INTO order_lines (order_id, product_id, quantity) VALUES (1, 2, 2);
INSERT INTO order_lines (order_id, product_id, quantity) VALUES (2, 3, 3);
INSERT INTO order_lines (order_id, product_id, quantity) VALUES (2, 4, 4);