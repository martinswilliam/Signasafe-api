package br.com.seunome.signasafe.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.seunome.signasafe.model.Signature;

@Repository
public interface SignatureRepository extends JpaRepository<Signature, UUID> {
}