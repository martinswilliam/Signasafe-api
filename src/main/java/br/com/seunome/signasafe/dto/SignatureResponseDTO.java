package br.com.seunome.signasafe.dto;
import java.time.Instant;
import java.util.UUID;

public record SignatureResponseDTO(
        UUID signatureId,
        UUID documentId,
        String signerEmail,
        Instant signedAt
) {}