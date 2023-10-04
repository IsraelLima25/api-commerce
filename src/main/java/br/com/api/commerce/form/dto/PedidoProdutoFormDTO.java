package br.com.api.commerce.form.dto;

import java.util.UUID;

import br.com.api.commerce.model.Produto;
import br.com.api.commerce.validators.global.ExistsID;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PedidoProdutoFormDTO (@NotNull @ExistsID(domainClass = Produto.class, fieldName = "id") UUID id, @NotNull @Positive int quantidade) { }
