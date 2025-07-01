package br.com.seunome.signasafe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class JacksonConfig {

    @Bean
    @Primary // Marca este ObjectMapper como o principal a ser usado na aplicação
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // Registra o módulo que ensina o Jackson a lidar com os tipos do java.time (Instant, etc.)
        objectMapper.registerModule(new JavaTimeModule());

        // Esta é a configuração chave: diz para NÃO escrever datas como números (timestamps)
        // e sim como strings no formato padrão ISO-8601 (que para Instant, inclui o 'Z')
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return objectMapper;
    }
}