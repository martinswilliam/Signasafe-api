package br.com.seunome.signasafe.controller;

import br.com.seunome.signasafe.dto.DocumentDetailsDTO;
import br.com.seunome.signasafe.dto.DocumentItemDTO;
import br.com.seunome.signasafe.model.Document;
import br.com.seunome.signasafe.model.User;
import br.com.seunome.signasafe.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal; // <-- 1. IMPORTAÇÃO ADICIONADA
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PostMapping("/upload")
    public ResponseEntity<DocumentDetailsDTO> uploadDocument(
            @RequestParam("file") MultipartFile file,
            @AuthenticationPrincipal User authenticatedUser // Usando o atalho @AuthenticationPrincipal
    ) {
        try {
            Document savedDocument = documentService.uploadDocument(file, authenticatedUser);

            DocumentDetailsDTO response = new DocumentDetailsDTO(
                    savedDocument.getId(),
                    savedDocument.getFileName(),
                    savedDocument.getOwner().getEmail(),
                    savedDocument.getCreatedAt()
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/my-documents")
    // O tipo de retorno aqui agora corresponde ao que o serviço retorna
    public ResponseEntity<List<DocumentItemDTO>> getMyDocuments(@AuthenticationPrincipal User user) {
        // O @AuthenticationPrincipal já nos dá o usuário logado
        var documents = documentService.getDocumentsByUser(user.getId());
        return ResponseEntity.ok(documents);
    }
}