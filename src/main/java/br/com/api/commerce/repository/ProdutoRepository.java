package br.com.api.commerce.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.api.commerce.model.Produto;

public interface ProdutoRepository extends JpaRepository<Produto, UUID> { }
