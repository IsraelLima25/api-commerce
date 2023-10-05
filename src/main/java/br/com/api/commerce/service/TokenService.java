package br.com.api.commerce.service;

import br.com.api.commerce.configurations.PropertiesConfiguration;
import br.com.api.commerce.model.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    private PropertiesConfiguration configuration;

    public TokenService(PropertiesConfiguration configuration) {
        this.configuration = configuration;
    }

    public String gerarToken(Usuario usuario) {

        try {
            var algoritmo = Algorithm.HMAC256(configuration.getSecretTokenJWT());
            return JWT.create()
                    .withIssuer("API Florescer")
                    .withSubject(usuario.getLogin())
                    .withExpiresAt(dataExpiracao())
                    .sign(algoritmo);
        } catch (JWTCreationException exception){
            throw new RuntimeException("erro ao gerar token jwt", exception);
        }
    }

    private Instant dataExpiracao() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

    public String getSubject(String tokenJWT) {
        try {
            var algoritmo = Algorithm.HMAC256(configuration.getSecretTokenJWT());
            return JWT.require(algoritmo)
                    .withIssuer("API Florescer")
                    .build()
                    .verify(tokenJWT)
                    .getSubject();
        } catch (JWTVerificationException exception) {
            throw new RuntimeException("Token JWT inválido ou expirado!");
        }
    }
}
