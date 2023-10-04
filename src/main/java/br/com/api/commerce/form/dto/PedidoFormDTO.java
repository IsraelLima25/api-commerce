package br.com.api.commerce.form.dto;

import java.util.List;

import br.com.api.commerce.indicador.FormaPagamentoIndicador;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PedidoFormDTO(
		@NotBlank String cpfCliente, 
		@NotNull FormaPagamentoIndicador formaPagamento, 
		@NotNull List<PedidoProdutoFormDTO> itens) 
{ }

