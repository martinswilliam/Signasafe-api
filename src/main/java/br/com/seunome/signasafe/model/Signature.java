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
@Entity(name = "signatures")
public class Signature {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "document_id") // Relação: Uma assinatura pertence a um documento
    private Document document;

    @ManyToOne
    @JoinColumn(name = "signer_id") // Relação: Uma assinatura pertence a um usuário (o signatário)
    private User signer;

    @Column(columnDefinition = "TEXT")
    private String signatureData; // A assinatura digital em si, em Base64

    private LocalDateTime signedAt;
}