package br.com.seunome.signasafe.dto;
import java.time.Instant;
import java.util.UUID;

public record DocumentDetailsDTO(
        UUID id,
        String fileName,
        String ownerEmail,
        Instant createdAt
) {}