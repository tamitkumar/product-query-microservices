CREATE TABLE cqrs.product
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    name          VARCHAR(255) NULL,
    `description` VARCHAR(255) NULL,
    price DOUBLE NULL,
    version       INT NOT NULL,
    CONSTRAINT pk_product PRIMARY KEY (id)
);