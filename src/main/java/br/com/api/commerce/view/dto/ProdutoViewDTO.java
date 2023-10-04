package br.com.api.commerce.view.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;

public class ProdutoViewDTO extends RepresentationModel<ProdutoViewDTO> {
	
	private UUID id; 
	private String descricao;
	private BigDecimal precoUnitario;
	private Integer quantidade;
	private LocalDate dataCadastro;
	
	/* Apply constructor JPA */
	@Deprecated
	public ProdutoViewDTO() { }
	
	private ProdutoViewDTO(UUID id, String descricao, BigDecimal precoUnitario, Integer quantidade, LocalDate dataCadastro) {
		this.id = id;
		this.descricao = descricao;
		this.precoUnitario = precoUnitario;
		this.dataCadastro = dataCadastro;
		this.quantidade = quantidade;
	}
	
	public UUID getId() {
		return id;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public BigDecimal getPrecoUnitario() {
		return precoUnitario;
	}
	
	public LocalDate getDataCadastro() {
		return dataCadastro;
	}

	public Integer getQuantidade() {
		return quantidade;
	}

	public static class Builder {

	    private UUID id;
	    private String descricao;
	    private BigDecimal precoUnitario;

		private Integer quantidade;
	    private LocalDate dataCadastro;

	    public Builder setId(UUID id) {
	        this.id = id;
	        return this;
	    }

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
	    
	    public Builder setDataCadastro(LocalDate dataCadastro) {
	        this.dataCadastro = dataCadastro;
	        return this;
	    }

	    public ProdutoViewDTO build() {
	        return new ProdutoViewDTO(id, descricao, precoUnitario, quantidade, dataCadastro);
	    }
	}
}


