package br.com.api.commerce.form.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PedidoProdutoFormDTO (@NotNull UUID id, @NotNull @Positive int quantidade) { }
