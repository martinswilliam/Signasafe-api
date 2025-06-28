package br.com.seunome.signasafe.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import br.com.seunome.signasafe.model.User;

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
        System.out.println(">>> TokenService: Validando o token...");
        Algorithm algorithm = Algorithm.HMAC256(secret);

        // 1. Verificamos e extraímos o 'subject' para uma variável
        String subject = JWT.require(algorithm)
                .withIssuer("SignaSafe API")
                .build()
                .verify(token)
                .getSubject();

        // 2. Agora que temos o subject, podemos usá-lo (para o log)
        System.out.println(">>> TokenService: Validação bem-sucedida! Subject: " + subject);
        
        // 3. E finalmente, retornamos o valor da variável
        return subject;

    } catch (JWTVerificationException exception) {
        // Se a validação falhar (token inválido, expirado, etc.), o erro será capturado aqui.
        System.out.println(">>> TokenService: ERRO NA VALIDAÇÃO - " + exception.getMessage());
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