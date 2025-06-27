package br.com.seunome.signasafe.dto;
import java.time.LocalDateTime;
import java.util.UUID;

public record DocumentDetailsDTO(
        UUID id,
        String fileName,
        String ownerEmail,
        LocalDateTime createdAt
) {}