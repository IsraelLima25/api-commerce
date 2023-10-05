package br.com.api.commerce.controller;

import br.com.api.commerce.form.dto.DadosAutenticacaoForm;
import br.com.api.commerce.model.Usuario;
import br.com.api.commerce.service.TokenService;
import br.com.api.commerce.view.dto.DadosTokenJWTView;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
public class AutenticacaoController {

    private AuthenticationManager authenticationManager;
    private TokenService tokenService;

    public AutenticacaoController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorna o token de acesso"),
            @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
            @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção"),
    })
    @PostMapping
    public ResponseEntity<DadosTokenJWTView> efetuarLogin(@RequestBody @Valid DadosAutenticacaoForm dados) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.login(), dados.senha());
        var authentication = authenticationManager.authenticate(authenticationToken);

        var tokenJWT = tokenService.gerarToken((Usuario)authentication.getPrincipal());
        return ResponseEntity.ok(new DadosTokenJWTView(tokenJWT));
    }
}
