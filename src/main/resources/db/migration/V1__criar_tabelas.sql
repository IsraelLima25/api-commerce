CREATE TABLE tbl_produto(
id VARCHAR(255) NOT NULL PRIMARY KEY, 
descricao VARCHAR(255) NOT NULL, 
data_cadastro DATE NOT NULL,
preco_unitario DECIMAL(8,2) NOT NULL);

CREATE TABLE tbl_cliente(
id VARCHAR(255) NOT NULL PRIMARY KEY,
nome VARCHAR(50) NOT NULL,
cpf VARCHAR(11) NOT NULL
);

CREATE TABLE tbl_pedido(
id VARCHAR(255) NOT NULL PRIMARY KEY,
data_hora_local DATETIME NOT NULL,
status VARCHAR(30) NOT NULL,
forma_pagamento VARCHAR(30) NOT NULL,
id_cliente VARCHAR(255) NOT NULL,
preco_total_itens DECIMAL(8,2) NOT NULL
);

CREATE TABLE tbl_pedido_produto(
id BIGINT AUTO_INCREMENT PRIMARY KEY,
id_pedido VARCHAR(255) NOT NULL,
id_produto VARCHAR(255) NOT NULL,
quantidade BIGINT NOT NULL
);

