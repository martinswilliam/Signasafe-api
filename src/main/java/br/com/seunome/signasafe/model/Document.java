package br.com.seunome.signasafe.model;



import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "documents") // Define o nome da tabela no banco de dados
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String fileName;

    @Column(columnDefinition = "TEXT")
    private String documentPath; // Caminho para o arquivo no storage (S3, Supabase, etc.)

    @Column(columnDefinition = "TEXT")
    private String documentHash; // Hash SHA-256 do arquivo, em Base64

    @ManyToOne // Define a relação: Muitos documentos podem pertencer a um usuário.
    @JoinColumn(name = "owner_id") // Define a coluna de chave estrangeira no banco
    private User owner;

    private LocalDateTime createdAt;
}