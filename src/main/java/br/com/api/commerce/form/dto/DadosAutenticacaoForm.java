package br.com.api.commerce.form.dto;

import jakarta.validation.constraints.NotBlank;

public record DadosAutenticacaoForm (
        @NotBlank String login,
        @NotBlank String senha)
{ }
