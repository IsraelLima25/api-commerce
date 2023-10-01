package br.com.api.commerce.view.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;

public class ProdutoViewDTO extends RepresentationModel<ProdutoViewDTO> {
	
	private UUID id; 
	private String descricao;
	private BigDecimal precoUnitario;
	private LocalDate dataCadastro;
	
	@Deprecated
	public ProdutoViewDTO() { }
	
	public ProdutoViewDTO(UUID id, String descricao, BigDecimal precoUnitario, LocalDate dataCadastro) {
		this.id = id;
		this.descricao = descricao;
		this.precoUnitario = precoUnitario;
		this.dataCadastro = dataCadastro;
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
	
}
