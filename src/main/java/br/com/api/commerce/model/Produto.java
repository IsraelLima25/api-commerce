package br.com.api.commerce.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import br.com.api.commerce.exception.BusinessException;
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

	@Column(name = "quantidade")
	private Integer quantidade;
	
	@Deprecated
	public Produto() { }
	
	private Produto(String descricao, BigDecimal precoUnitario, Integer quantidade) {
		this.id = UUID.randomUUID();
		this.descricao = descricao;
		this.dataCadastro = LocalDate.now();
		this.precoUnitario = precoUnitario;
		this.quantidade = quantidade;
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

	public Integer getQuantidade() {
		return quantidade;
	}

	public void atualizarProduto(String descricao, BigDecimal precoUnitario, Integer quantidade) {
		this.descricao = descricao;
		this.precoUnitario = precoUnitario;
		this.quantidade = quantidade;
	}
	public void temEstoque(Integer quantidadeCompra){
		if(quantidadeCompra > this.quantidade){
			throw new BusinessException("quantidade", "A quantidade para compra é maior que a quantidade em estoque");
		}
	}

	public void abaterEstoque(Integer quantidadeComprada) {
		this.quantidade-=quantidadeComprada;
	}

	public static class Builder {

	    private String descricao;
	    private BigDecimal precoUnitario;
		private Integer quantidade;


	    public Builder setDescricao(String descricao) {
	        this.descricao = descricao;
	        return this;
	    }

	    public Builder setPrecoUnitario(BigDecimal precoUnitario) {
	        this.precoUnitario = precoUnitario;
	        return this;
	    }

		public Builder setQuantidade(Integer quantidade) {
			this.quantidade = quantidade;
			return this;
		}
	    
	    public Produto build() {
	        return new Produto(descricao, precoUnitario, quantidade);
	    }
	}
}
