package br.com.api.commerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.api.commerce.model.ItemPedido;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {

}
