package br.com.seunome.signasafe.service;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.seunome.signasafe.model.Document;
import br.com.seunome.signasafe.model.Signature;
import br.com.seunome.signasafe.model.User;
import br.com.seunome.signasafe.repository.DocumentRepository;
import br.com.seunome.signasafe.repository.SignatureRepository;

@Service
public class SignatureService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private SignatureRepository signatureRepository;

    @Autowired
    private CryptoService cryptoService;

    public Signature createSignature(User signer, UUID documentId) throws Exception {
        // 1. Encontra o documento que será assinado. Se não existir, lança um erro.
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found with id: " + documentId));

        // 2. Prepara os dados para a assinatura
        // Decodifica o hash do documento de Base64 para bytes
        byte[] documentHash = Base64.getDecoder().decode(document.getDocumentHash());

        // Decodifica a chave privada do usuário de Base64 para bytes
        byte[] privateKeyBytes = Base64.getDecoder().decode(signer.getPrivateKey());

        // Recria o objeto PrivateKey a partir dos bytes. Este é um passo técnico da criptografia Java.
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));

        // 3. Usa o CryptoService para assinar o hash com a chave privada
        byte[] signatureBytes = cryptoService.sign(documentHash, privateKey);
        String signatureBase64 = Base64.getEncoder().encodeToString(signatureBytes);

        // 4. Cria e salva a nova entidade de assinatura no banco de dados
        Signature newSignature = new Signature();
        newSignature.setDocument(document);
        newSignature.setSigner(signer);
        newSignature.setSignatureData(signatureBase64);
        newSignature.setSignedAt(Instant.now());

        return signatureRepository.save(newSignature);
    }
}