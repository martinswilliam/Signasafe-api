package br.com.seunome.signasafe.security;

import br.com.seunome.signasafe.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Marca esta classe como um componente do Spring
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
       
        System.out.println("====== SECURITY FILTER INICIADO ======"); // LOG 1
       
        // 1. Recupera o token do cabeçalho da requisição
        var token = this.recoverToken(request);

        System.out.println("Token recuperado do header: " + token); // LOG 2

        if (token != null) {
            // 2. Valida o token
            var email = tokenService.validateToken(token);

             System.out.println("Resultado da validação (email): " + email); // LOG 3

            UserDetails user = userRepository.findByEmail(email);

            if (user != null) {
                // 3. Se o usuário for válido, o autentica no contexto do Spring Security
                var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
            System.out.println("Usuário autenticado no contexto de segurança: " + user.getUsername()); // LOG 4
        } else {
            System.out.println("Usuário não encontrado no banco com o email do token."); // LOG 5
        }
    } else {
        System.out.println("Nenhum token encontrado no cabeçalho Authorization."); // LOG 6
    }

    filterChain.doFilter(request, response);
    System.out.println("====== SECURITY FILTER FINALIZADO ======"); // LOG 7
}

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        // O token vem depois de "Bearer "
        return authHeader.replace("Bearer ", "");
    }
}