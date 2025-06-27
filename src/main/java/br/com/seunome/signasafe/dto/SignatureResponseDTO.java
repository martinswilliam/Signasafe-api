package br.com.seunome.signasafe.dto;
import java.time.LocalDateTime;
import java.util.UUID;

public record SignatureResponseDTO(
        UUID signatureId,
        UUID documentId,
        String signerEmail,
        LocalDateTime signedAt
) {}