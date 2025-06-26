package br.com.seunome.signasafe.security;

import br.com.seunome.signasafe.model.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service // Marca esta classe como um serviço do Spring
public class TokenService {

    // Injeta o valor da nossa chave secreta definida no application.properties
    @Value("${api.security.token.secret}")
    private String secret;

    /**
     * Gera um token JWT para um usuário autenticado.
     */
    public String generateToken(User user) {
        try {
            // Define o algoritmo de assinatura usando nossa chave secreta
            Algorithm algorithm = Algorithm.HMAC256(secret);

            String token = JWT.create()
                    .withIssuer("SignaSafe API") // Emissor do token
                    .withSubject(user.getEmail()) // Assunto do token (quem é o dono)
                    .withExpiresAt(generateExpirationDate()) // Define a data de expiração
                    .sign(algorithm); // Assina o token
            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar o token JWT", exception);
        }
    }

    /**
     * Valida um token JWT e retorna o "dono" do token (o e-mail do usuário).
     */
    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.require(algorithm)
                    .withIssuer("SignaSafe API") // Verifica se o emissor é o mesmo
                    .build()
                    .verify(token) // Verifica a assinatura e a validade
                    .getSubject(); // Retorna o assunto (o e-mail do usuário)
        } catch (JWTVerificationException exception) {
            // Se o token for inválido (expirado, assinatura incorreta), retorna uma string vazia.
            return "";
        }
    }

    /**
     * Gera a data de expiração do token.
     * Para este exemplo, o token irá expirar em 2 horas.
     */
    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}