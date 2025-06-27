package br.com.seunome.signasafe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.com.seunome.signasafe.dto.DocumentDetailsDTO;
import br.com.seunome.signasafe.model.Document;
import br.com.seunome.signasafe.model.User;
import br.com.seunome.signasafe.service.DocumentService;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    @Autowired
    private DocumentService documentService;

    @PostMapping("/upload")
    public ResponseEntity<DocumentDetailsDTO> uploadDocument(
            @RequestParam("file") MultipartFile file,
            Authentication authentication // Spring Security injeta o usuário autenticado aqui
    ) {
        try {
            // O objeto 'Authentication' contém os detalhes do usuário que foi autenticado pelo nosso SecurityFilter.
            User authenticatedUser = (User) authentication.getPrincipal();

            Document savedDocument = documentService.uploadDocument(file, authenticatedUser);

            // Criamos e retornamos um DTO, nunca a entidade diretamente.
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
}