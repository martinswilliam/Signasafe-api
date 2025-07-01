package br.com.seunome.signasafe.service;

import java.io.IOException;
import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.seunome.signasafe.dto.DocumentItemDTO;
import br.com.seunome.signasafe.model.Document;
import br.com.seunome.signasafe.model.User;
import br.com.seunome.signasafe.repository.DocumentRepository;


@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private CryptoService cryptoService;

    /**
     * Processa o upload de um arquivo, calcula seu hash e salva os metadados.
     * @param file O arquivo enviado pelo usuário.
     * @param owner O usuário que é dono do documento.
     * @return O objeto Document salvo no banco.
     * @throws Exception se houver erro no cálculo do hash ou I/O.
     */
    public Document uploadDocument(MultipartFile file, User owner) throws Exception {
        if (file.isEmpty()) {
            throw new IOException("Failed to store empty file.");
        }

        // TODO: No futuro, aqui ocorreria o upload do arquivo para um storage (S3, Supabase, etc.)
        // Por enquanto, vamos focar na lógica de metadados e hash.
        String filePathInStorage = "uploads/" + owner.getId() + "/" + file.getOriginalFilename();

        // Calcula o hash do conteúdo do arquivo
        byte[] fileBytes = file.getBytes();
        byte[] documentHash = cryptoService.hashDocument(fileBytes);

        // Converte o hash para Base64 para ser armazenado como string
        String documentHashBase64 = Base64.getEncoder().encodeToString(documentHash);

        Document newDocument = new Document();
        newDocument.setFileName(file.getOriginalFilename());
        newDocument.setDocumentPath(filePathInStorage);
        newDocument.setDocumentHash(documentHashBase64);
        newDocument.setOwner(owner);
        newDocument.setCreatedAt(Instant.now());

        return documentRepository.save(newDocument);
    }

    // NOVO MÉTODO
    public List<DocumentItemDTO> getDocumentsByUser(UUID ownerId) {
    return documentRepository.findAllByOwnerId(ownerId) // <-- CHAMADA ATUALIZADA
            .stream()
            .map(doc -> new DocumentItemDTO(
                doc.getId(), 
                doc.getFileName(), 
                doc.getCreatedAt()
            ))
            .collect(Collectors.toList());
}
}