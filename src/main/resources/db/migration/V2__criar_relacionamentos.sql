ALTER TABLE tbl_pedido  ADD CONSTRAINT fk_cliente FOREIGN KEY (id_cliente) REFERENCES tbl_cliente (id);
ALTER TABLE tbl_pedido_produto  ADD CONSTRAINT fk_pedido FOREIGN KEY (id_pedido) REFERENCES tbl_pedido (id);
ALTER TABLE tbl_pedido_produto  ADD CONSTRAINT fk_produto FOREIGN KEY (id_produto) REFERENCES tbl_produto (id);