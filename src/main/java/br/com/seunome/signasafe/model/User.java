package br.com.seunome.signasafe.model;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
public class User implements UserDetails { // Implementa a interface UserDetails

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Usar UUID para IDs de usuário é uma boa prática
    private UUID id;
    private String email;
    private String password;
    private String publicKeyPath;

     @Column(columnDefinition = "TEXT")
    private String publicKey;

    @Column(columnDefinition = "TEXT")
    private String privateKey; // Lembrete: Apenas para fins didáticos.

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Para este projeto simples, todo usuário terá a role "USER".
        // Em um sistema complexo, as roles viriam do banco de dados.
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getUsername() {
        // Nosso "username" será o e-mail.
        return this.email;
    }

    // Os métodos abaixo controlam o estado da conta. Para nosso projeto, vamos
    // deixar todos como 'true', indicando que a conta está sempre ativa.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}