package br.com.seunome.signasafe.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

class CryptoServiceTest {

    private CryptoService cryptoService;

    // O método anotado com @BeforeEach é executado antes de cada @Test.
    // Isso garante que temos uma instância "limpa" do serviço para cada teste.
    @BeforeEach
    void setUp() {
        cryptoService = new CryptoService();
    }

    @Test
    @DisplayName("Deve gerar um par de chaves, assinar e verificar com sucesso")
    void testSignAndVerify_Success() throws Exception {
        // 1. Arrange (Preparação)
        KeyPair keyPair = cryptoService.generateKeyPair();
        PublicKey publicKey = keyPair.getPublic();
        PrivateKey privateKey = keyPair.getPrivate();

        String originalDataString = "Este é um teste de assinatura digital!";
        byte[] originalDataBytes = originalDataString.getBytes(StandardCharsets.UTF_8);

        // Calcula o hash do dado original
        byte[] dataHash = cryptoService.hashDocument(originalDataBytes);

        // 2. Act (Ação)
        // Assina o hash com a chave privada
        byte[] signature = cryptoService.sign(dataHash, privateKey);

        // Verifica a assinatura com a chave pública
        boolean isSignatureValid = cryptoService.verify(dataHash, signature, publicKey);

        // 3. Assert (Verificação)
        Assertions.assertNotNull(keyPair);
        Assertions.assertNotNull(signature);
        Assertions.assertTrue(isSignatureValid, "A assinatura deveria ser válida.");
    }

    @Test
    @DisplayName("Deve falhar na verificação se o dado for alterado")
    void testSignAndVerify_FailureOnDataTamper() throws Exception {
        // 1. Arrange
        KeyPair keyPair = cryptoService.generateKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();

        String originalDataString = "Dado original.";
        byte[] dataHash = cryptoService.hashDocument(originalDataString.getBytes(StandardCharsets.UTF_8));
        byte[] signature = cryptoService.sign(dataHash, privateKey);

        // Cria um dado adulterado
        String tamperedDataString = "Dado adulterado!";
        byte[] tamperedDataHash = cryptoService.hashDocument(tamperedDataString.getBytes(StandardCharsets.UTF_8));

        // 2. Act
        // Tenta verificar a assinatura original com o hash adulterado
        boolean isSignatureValid = cryptoService.verify(tamperedDataHash, signature, publicKey);

        // 3. Assert
        Assertions.assertFalse(isSignatureValid, "A assinatura deveria ser inválida para dados adulterados.");
    }
}