package br.com.seunome.signasafe;

import java.util.TimeZone; // 1. ADICIONE ESTA IMPORTAÇÃO

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct; // 2. E ESTA IMPORTAÇÃO

@SpringBootApplication
public class SignasafeApplication {

    // 3. ADICIONE ESTE MÉTODO INTEIRO
    @PostConstruct
    public void init() {
        // Define o fuso horário padrão para toda a aplicação como UTC.
        // Isso garante que todas as operações de data e hora no servidor
        // sejam consistentes e não dependam do fuso horário da máquina.
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        System.out.println("--- Aplicação configurada para rodar em fuso horário UTC ---");
    }

    public static void main(String[] args) {
        SpringApplication.run(SignasafeApplication.class, args);
    }

}