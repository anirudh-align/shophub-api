-- Test Data for ShopHub API
-- This file will be executed automatically on startup

-- Insert Categories
INSERT INTO categories (id, name, created_at, updated_at) VALUES
('550e8400-e29b-41d4-a716-446655440001', 'Electronics', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('550e8400-e29b-41d4-a716-446655440002', 'Clothing', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('550e8400-e29b-41d4-a716-446655440003', 'Books', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('550e8400-e29b-41d4-a716-446655440004', 'Home & Garden', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

-- Insert Inventories
INSERT INTO inventories (id, stock, reserved_stock, low_stock_threshold, created_at, updated_at) VALUES
('660e8400-e29b-41d4-a716-446655440001', 100, 0, 10, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('660e8400-e29b-41d4-a716-446655440002', 50, 0, 5, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('660e8400-e29b-41d4-a716-446655440003', 200, 0, 20, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('660e8400-e29b-41d4-a716-446655440004', 75, 0, 10, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('660e8400-e29b-41d4-a716-446655440005', 30, 0, 5, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

-- Insert Products
INSERT INTO products (id, name, description, price, image_url, category_id, inventory_id, created_at, updated_at) VALUES
('770e8400-e29b-41d4-a716-446655440001', 'Laptop Pro 15', 'High-performance laptop with 16GB RAM and 512GB SSD', 1299.99, 'https://example.com/laptop.jpg', '550e8400-e29b-41d4-a716-446655440001', '660e8400-e29b-41d4-a716-446655440001', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('770e8400-e29b-41d4-a716-446655440002', 'Wireless Mouse', 'Ergonomic wireless mouse with long battery life', 29.99, 'https://example.com/mouse.jpg', '550e8400-e29b-41d4-a716-446655440001', '660e8400-e29b-41d4-a716-446655440002', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('770e8400-e29b-41d4-a716-446655440003', 'Spring Boot Guide', 'Complete guide to Spring Boot development', 49.99, 'https://example.com/book.jpg', '550e8400-e29b-41d4-a716-446655440003', '660e8400-e29b-41d4-a716-446655440003', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('770e8400-e29b-41d4-a716-446655440004', 'Garden Tool Set', 'Complete set of gardening tools', 79.99, 'https://example.com/tools.jpg', '550e8400-e29b-41d4-a716-446655440004', '660e8400-e29b-41d4-a716-446655440004', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('770e8400-e29b-41d4-a716-446655440005', 'Cotton T-Shirt', 'Comfortable 100% cotton t-shirt', 19.99, 'https://example.com/tshirt.jpg', '550e8400-e29b-41d4-a716-446655440002', '660e8400-e29b-41d4-a716-446655440005', CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());
