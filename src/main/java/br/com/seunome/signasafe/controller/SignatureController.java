package br.com.seunome.signasafe.controller;

import br.com.seunome.signasafe.dto.SignatureRequestDTO;
import br.com.seunome.signasafe.dto.SignatureResponseDTO;
import br.com.seunome.signasafe.model.Signature;
import br.com.seunome.signasafe.model.User;
import br.com.seunome.signasafe.service.SignatureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signatures")
public class SignatureController {

    @Autowired
    private SignatureService signatureService;

    @PostMapping("/sign")
    public ResponseEntity<SignatureResponseDTO> signDocument(
            @RequestBody SignatureRequestDTO requestDTO,
            Authentication authentication
    ) {
        try {
            User authenticatedUser = (User) authentication.getPrincipal();
            Signature newSignature = signatureService.createSignature(authenticatedUser, requestDTO.documentId());

            SignatureResponseDTO response = new SignatureResponseDTO(
                    newSignature.getId(),
                    newSignature.getDocument().getId(),
                    newSignature.getSigner().getEmail(),
                    newSignature.getSignedAt()
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null); // Em um projeto real, retornaríamos um DTO de erro.
        }
    }
}