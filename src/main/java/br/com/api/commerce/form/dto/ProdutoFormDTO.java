package br.com.api.commerce.form.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProdutoFormDTO(
		@NotBlank String descricao, 
		@NotNull @Positive BigDecimal precoUnitario) 
{ }

