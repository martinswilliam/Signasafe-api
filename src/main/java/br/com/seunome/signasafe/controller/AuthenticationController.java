package br.com.seunome.signasafe.controller;

import br.com.seunome.signasafe.dto.LoginDTO;
import br.com.seunome.signasafe.dto.LoginResponseDTO;
import br.com.seunome.signasafe.dto.RegisterDTO;
import br.com.seunome.signasafe.model.User;
import br.com.seunome.signasafe.repository.UserRepository;
import br.com.seunome.signasafe.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginDTO data) {
        // 1. Cria um objeto de autenticação com email e senha
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());

        // 2. O Spring Security usa o nosso `AuthorizationService` para validar o usuário e a senha
        var auth = this.authenticationManager.authenticate(usernamePassword);

        // 3. Se a autenticação for bem-sucedida, gera o token
        var token = tokenService.generateToken((User) auth.getPrincipal());

        // 4. Retorna o token em um DTO
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterDTO data) {
        // Verifica se o usuário já existe
        if (this.userRepository.findByEmail(data.email()) != null) {
            return ResponseEntity.badRequest().body("User with this email already exists.");
        }

        // Criptografa a senha antes de salvar
        String encryptedPassword = passwordEncoder.encode(data.password());
        User newUser = new User();
        newUser.setEmail(data.email());
        newUser.setPassword(encryptedPassword);

        // TODO: No futuro, aqui chamaremos o CryptoService para gerar o par de chaves do usuário.

        this.userRepository.save(newUser);

        return ResponseEntity.created(null).build();
    }
}