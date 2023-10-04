package br.com.api.commerce.form.dto;

import jakarta.validation.constraints.NotBlank;

public record ClienteFormDTO(@NotBlank String nome, @NotBlank String cpf) {

}
