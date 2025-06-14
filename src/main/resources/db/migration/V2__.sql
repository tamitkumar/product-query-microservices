CREATE TABLE cqrs.product_query
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    product_code  VARCHAR(255) NULL,
    name          VARCHAR(255) NULL,
    `description` VARCHAR(255) NULL,
    price DOUBLE NULL,
    version       INT NOT NULL,
    CONSTRAINT pk_product_query PRIMARY KEY (id)
);