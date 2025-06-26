package br.com.seunome.signasafe.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // Indica que esta é uma classe de configuração do Spring
@EnableWebSecurity // Habilita a segurança web do Spring
public class SecurityConfig {

    @Bean // @Bean expõe o método como um "Bean" gerenciado pelo Spring
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                // 1. Desabilita o CSRF (Cross-Site Request Forgery), pois nossa autenticação será stateless via token.
                .csrf(csrf -> csrf.disable())

                // 2. Configura a política de gerenciamento de sessão para STATELESS.
                // O servidor não irá guardar nenhuma informação de sessão do usuário.
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 3. Configura as regras de autorização para as requisições HTTP.
                .authorizeHttpRequests(authorize -> authorize
                        // Permite acesso público (sem autenticação) para os endpoints de cadastro e login.
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/register").permitAll()

                        // Exige autenticação para qualquer outra requisição.
                        .anyRequest().authenticated()
                )

                // Constrói o objeto de configuração.
                .build();
    }

    // Bean para o gerenciador de autenticação, necessário para o processo de login.
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    // Bean que define o algoritmo de criptografia de senhas.
    // Usaremos o BCrypt, que é o padrão recomendado.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}