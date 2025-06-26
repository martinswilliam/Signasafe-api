package br.com.seunome.signasafe.repository;

import br.com.seunome.signasafe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    // O Spring Data JPA cria a implementação deste método automaticamente
    // baseado no nome do método. Ele vai procurar por um usuário com o email correspondente.
    UserDetails findByEmail(String email);

}