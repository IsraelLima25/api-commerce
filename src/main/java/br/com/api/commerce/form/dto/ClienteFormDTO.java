package br.com.api.commerce.form.dto;

import br.com.api.commerce.controller.ClienteController;
import br.com.api.commerce.model.Cliente;
import br.com.api.commerce.validators.global.UniqueValue;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

public record ClienteFormDTO(@NotBlank String nome, @CPF @NotBlank @UniqueValue(domainClass = Cliente.class, fieldName = "cpf") String cpf) {
}
