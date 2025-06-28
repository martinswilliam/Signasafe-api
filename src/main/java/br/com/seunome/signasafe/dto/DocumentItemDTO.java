package br.com.seunome.signasafe.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record DocumentItemDTO(
    UUID id,
    String fileName,
    LocalDateTime uploadDate
) {}