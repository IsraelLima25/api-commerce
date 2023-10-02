package br.com.api.commerce.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_produto")
public class Produto {
	
	@Id
	@Column(name = "id")
	private UUID id;
	
	@Column(name = "descricao")
	private String descricao;
	
	@Column(name = "data_cadastro")
	private LocalDate dataCadastro;
	
	@Column(name = "preco_unitario")
	private BigDecimal precoUnitario;
	
	@Deprecated
	public Produto() { }
	
	public Produto(String descricao, BigDecimal precoUnitario) {
		this.id = UUID.randomUUID();
		this.descricao = descricao;
		this.dataCadastro = LocalDate.now();
		this.precoUnitario = precoUnitario;
	}
	
	public UUID getId() {
		return id;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public LocalDate getDataCadastro() {
		return dataCadastro;
	}
	
	public BigDecimal getPrecoUnitario() {
		return precoUnitario;
	}
	
	public void atualizarProduto(String descricao, BigDecimal precoUnitario) {
		this.descricao = descricao;
		this.precoUnitario = precoUnitario;
	}
}
