package br.com.seunome.signasafe.dto;

import java.time.Instant;
import java.util.UUID;

public record DocumentItemDTO(
    UUID id,
    String fileName,
    Instant uploadDate
) {}