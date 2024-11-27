CREATE DATABASE p2;
use p2;

CREATE TABLE roles (
    role_id BIGINT AUTO_INCREMENT PRIMARY KEY, -- Role ID (PK)
    role_name VARCHAR(255) NOT NULL UNIQUE     -- Role 이름 (예: 'ROLE_USER', 'ROLE_ADMIN')
);
CREATE TABLE user (
    user_id BIGINT AUTO_INCREMENT PRIMARY KEY, -- 사용자 ID (PK)
    name VARCHAR(255) NOT NULL,               -- 사용자 이름
    phone_num VARCHAR(30) NOT NULL,           -- 전화번호
    address VARCHAR(255) NOT NULL,            -- 주소
    gender CHAR(1) NOT NULL,                  -- 성별 ('M' 또는 'F')
    UNIQUE (phone_num)                        -- 전화번호는 고유해야 함
);

CREATE TABLE user_principal (
    user_principal_id BIGINT AUTO_INCREMENT PRIMARY KEY, -- UserPrincipal ID (PK)
    user_id BIGINT NOT NULL,                             -- UserEntity ID (FK)
    email VARCHAR(255) NOT NULL UNIQUE,                 -- 이메일 (고유)
    password VARCHAR(255) NOT NULL,                     -- 비밀번호
    CONSTRAINT fk_user_principal_user FOREIGN KEY (user_id) REFERENCES user (user_id)
        ON DELETE CASCADE                                -- UserEntity 삭제 시 연결된 UserPrincipal도 삭제
);

CREATE TABLE user_principal_roles (
    user_principal_role_id BIGINT AUTO_INCREMENT PRIMARY KEY, -- UserPrincipalRoles ID (PK)
    user_principal_id BIGINT NOT NULL,                        -- UserPrincipal ID (FK)
    role_id BIGINT NOT NULL,                                  -- Roles ID (FK)
    CONSTRAINT fk_user_principal FOREIGN KEY (user_principal_id) REFERENCES user_principal(user_principal_id)
        ON DELETE CASCADE,                                    -- UserPrincipal 삭제 시 역할 매핑도 삭제
    CONSTRAINT fk_roles FOREIGN KEY (role_id) REFERENCES roles(role_id)
        ON DELETE CASCADE                                     -- Role 삭제 시 역할 매핑도 삭제
);

CREATE TABLE product ( 
    product_id BIGINT AUTO_INCREMENT PRIMARY KEY,     -- 상품 ID
    title VARCHAR(255) NOT NULL,                      -- 상품명
    price INT NOT NULL,                               -- 가격
    contents TEXT,                                    -- 상품 설명
    product_status CHAR(1) NOT NULL,                  -- 상품 상태 (예: 'A' - 판매중, 'B' - 품절 등)
    create_at DATE NOT NULL,                          -- 상품 등록일
    update_at DATE,                                   -- 상품 수정일
    delete_at DATE,                                   -- 상품 삭제일 (논리 삭제)
    is_delete BOOLEAN NOT NULL DEFAULT FALSE          -- 삭제 여부 (논리 삭제)
);


CREATE TABLE sale (
    sale_id BIGINT AUTO_INCREMENT PRIMARY KEY,        -- 판매 ID
    product_id BIGINT NOT NULL,                       -- 상품 ID (Product 테이블 참조)
    seller_id BIGINT NOT NULL,                        -- 판매자 ID (User 테이블 참조)
    sale_price INT NOT NULL,                          -- 판매 가격 (상품 가격과 다를 수 있음)
    sale_date DATE NOT NULL,                          -- 판매 일자
    is_delete BOOLEAN NOT NULL DEFAULT FALSE,         -- 삭제 여부 (논리 삭제)
    FOREIGN KEY (product_id) REFERENCES Product(product_id) ON DELETE CASCADE,
    FOREIGN KEY (seller_id) REFERENCES User(user_id) ON DELETE CASCADE
);

CREATE TABLE basket (
    basket_id BIGINT AUTO_INCREMENT PRIMARY KEY,      -- 장바구니 ID
    product_id BIGINT NOT NULL,                       -- 상품 ID (Product 테이블 참조)
    user_id BIGINT NOT NULL,                          -- 사용자 ID (User 테이블 참조)
    quantity INT NOT NULL,                            -- 수량
    create_at DATE NOT NULL,                          -- 장바구니 추가일
    update_at DATE,                                   -- 장바구니 수정일
    delete_at DATE,                                   -- 장바구니 삭제일
    is_delete BOOLEAN NOT NULL DEFAULT FALSE,         -- 삭제 여부 (논리 삭제)
    FOREIGN KEY (product_id) REFERENCES Product(product_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE
);

CREATE TABLE purchase (
    purchase_id BIGINT AUTO_INCREMENT PRIMARY KEY,    -- 구매 ID
    user_id BIGINT NOT NULL,                          -- 사용자 ID (User 테이블 참조)
    product_id BIGINT NOT NULL,                       -- 상품 ID (Product 테이블 참조)
    purchase_date DATE NOT NULL,                      -- 구매일
    is_delete BOOLEAN NOT NULL DEFAULT FALSE,         -- 삭제 여부 (논리 삭제)
    FOREIGN KEY (user_id) REFERENCES User(user_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES Product(product_id) ON DELETE CASCADE
);


CREATE TABLE product_Image (
    image_id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- 상품 이미지 아이디 (기본 키)
    image_url VARCHAR(255) NOT NULL,              -- 이미지 URL
    product_id BIGINT NOT NULL,                   -- 상품 아이디 (Product 테이블의 외래 키)
    FOREIGN KEY (product_id) REFERENCES Product(product_id) ON DELETE CASCADE  -- Product 테이블 참조
);
