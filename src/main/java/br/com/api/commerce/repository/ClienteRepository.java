package br.com.api.commerce.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.api.commerce.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, UUID> {

	Optional<Cliente> findByCpf(String cpf); 

}
