package br.com.api.commerce.form.dto;

import java.util.List;

import br.com.api.commerce.indicador.FormaPagamentoIndicador;
import br.com.api.commerce.model.Cliente;
import br.com.api.commerce.validators.global.ExistsCPF;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.br.CPF;

public record PedidoFormDTO(
		@CPF @NotBlank @ExistsCPF(domainClass = Cliente.class, fieldName = "cpf") String cpfCliente,
		@NotNull FormaPagamentoIndicador formaPagamento, 
		@NotNull List<PedidoProdutoFormDTO> itens) 
{ }

