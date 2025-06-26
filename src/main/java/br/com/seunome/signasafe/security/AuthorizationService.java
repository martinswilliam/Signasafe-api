package br.com.seunome.signasafe.security;

import br.com.seunome.signasafe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Esta classe implementa a interface UserDetailsService do Spring Security.
// Ela é responsável por carregar os detalhes de um usuário do banco de dados
// para que o Spring Security possa realizar a autenticação.
@Service
public class AuthorizationService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Aqui, nosso "username" é o e-mail. Buscamos o usuário no repositório.
        UserDetails user = userRepository.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }
        return user;
    }
}