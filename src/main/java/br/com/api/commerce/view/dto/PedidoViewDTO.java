package br.com.api.commerce.view.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import br.com.api.commerce.indicador.FormaPagamentoIndicador;

public record PedidoViewDTO (
		UUID codigoPedido, 
		String cpfCliente, 
		LocalDateTime dataPedido, 
		List<ItemPedidoViewDTO> itensPedido, 
		FormaPagamentoIndicador tipoPagamento, 
		BigDecimal valorTotalItens) 
{ }



