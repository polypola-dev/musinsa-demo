-- 브랜드 테이블
CREATE TABLE brand (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- 브랜드 인덱스
CREATE UNIQUE INDEX idx_brand_name ON brand (name, status);
CREATE INDEX idx_brand_status ON brand (status);

-- 카테고리 테이블
CREATE TABLE category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);

-- 카테고리 인덱스
CREATE UNIQUE INDEX idx_category_name ON category (name, status);
CREATE INDEX idx_category_status ON category (status);

-- 상품 테이블
CREATE TABLE product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    price INT NOT NULL,
    status VARCHAR(20) NOT NULL,
    brand_id BIGINT,
    category_id BIGINT,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    FOREIGN KEY (brand_id) REFERENCES brand(id),
    FOREIGN KEY (category_id) REFERENCES category(id)
);

-- 상품 인덱스
CREATE INDEX idx_product_status ON product (status);
CREATE INDEX idx_product_brand_category ON product (brand_id, category_id, status);
CREATE INDEX idx_product_category_price ON product (category_id, price, status);