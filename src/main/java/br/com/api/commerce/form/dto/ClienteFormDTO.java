package br.com.api.commerce.form.dto;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CPF;

public record ClienteFormDTO(@NotBlank String nome, @CPF @NotBlank String cpf) {

}
