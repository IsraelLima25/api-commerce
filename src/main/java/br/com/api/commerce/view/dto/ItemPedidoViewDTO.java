package br.com.api.commerce.view.dto;

import java.math.BigDecimal;

public record ItemPedidoViewDTO(String nomeProduto, int quantidade, BigDecimal precoUnitario) { }
