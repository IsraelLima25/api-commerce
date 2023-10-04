package br.com.api.commerce.view.dto;

import java.util.UUID;

import org.springframework.hateoas.RepresentationModel;

public class ClienteViewDTO extends RepresentationModel<ClienteViewDTO> {
	
	private UUID id; 
	private String nome;
	private String cpf;
	
	@Deprecated
	public ClienteViewDTO() { }
	
	public ClienteViewDTO(UUID id, String nome, String cpf) {
		this.id = id;
		this.nome = nome;
		this.cpf = cpf;
	}
	
	public UUID getId() {
		return id;
	}
	
	public String getNome() {
		return nome;
	}
	
	public String getCpf() {
		return cpf;
	}

}
