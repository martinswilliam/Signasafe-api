package br.com.seunome.signasafe.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.seunome.signasafe.model.Document;

@Repository
public interface DocumentRepository extends JpaRepository<Document, UUID> {

    // VERIFIQUE SE O MÉTODO AQUI ESTÁ EXATAMENTE ASSIM:
    // O nome deve ser findAllByOwnerId e o parâmetro ownerId
    List<Document> findAllByOwnerId(UUID ownerId);

}