package br.com.api.commerce.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.api.commerce.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, UUID> {
	
	Optional<List<Pedido>> findByClienteCpf(String cpf);

}
