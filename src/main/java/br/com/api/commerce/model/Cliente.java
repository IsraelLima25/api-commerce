package br.com.api.commerce.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tbl_cliente")
public class Cliente {
	
	@Id
	@Column(name = "id")
	private UUID id;
	
	@Column(name = "nome", nullable = false)
	private String nome;
	
	@Column(name = "cpf", nullable = false)
	private String cpf;
	
	@Deprecated
	public Cliente() { }

	public Cliente(String nome, String cpf) {
		this.id = UUID.randomUUID();
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
