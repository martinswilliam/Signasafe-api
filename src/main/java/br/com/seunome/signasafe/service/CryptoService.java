package br.com.seunome.signasafe.service;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Service;

import java.security.*;

@Service
public class CryptoService {

    static {
        // Adiciona o Bouncy Castle como um provedor de segurança Java.
        // Isso permite que o Java encontre os algoritmos implementados pelo Bouncy Castle.
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * Gera um par de chaves (pública e privada) usando o algoritmo RSA.
     * @return O KeyPair gerado.
     */
    public KeyPair generateKeyPair() throws NoSuchProviderException, NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA", "BC"); // "BC" = Bouncy Castle
        generator.initialize(2048); // Tamanho da chave. 2048 bits é um padrão seguro.
        return generator.generateKeyPair();
    }

    /**
     * Calcula o hash SHA-256 de um array de bytes (o conteúdo de um documento).
     * @param data O conteúdo do arquivo.
     * @return O hash de 256 bits.
     */
    public byte[] hashDocument(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        return digest.digest(data);
    }

    /**
     * Assina um dado (geralmente um hash) usando uma chave privada.
     * @param dataToSign O dado a ser assinado.
     * @param privateKey A chave privada do signatário.
     * @return A assinatura digital como um array de bytes.
     */
    public byte[] sign(byte[] dataToSign, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA", "BC");
        signature.initSign(privateKey);
        signature.update(dataToSign);
        return signature.sign();
    }

    /**
     * Verifica se uma assinatura digital é válida para um determinado dado e chave pública.
     * @param originalData O dado original que foi assinado (o hash).
     * @param signatureBytes A assinatura a ser verificada.
     * @param publicKey A chave pública correspondente à chave privada que assinou.
     * @return true se a assinatura for válida, false caso contrário.
     */
    public boolean verify(byte[] originalData, byte[] signatureBytes, PublicKey publicKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA", "BC");
        signature.initVerify(publicKey);
        signature.update(originalData);
        return signature.verify(signatureBytes);
    }
}