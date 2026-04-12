package com.example.projet2024.repository;

import com.example.projet2024.entite.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long> {
    Optional<Produit> findByCode(String code);
    List<Produit> findByActifTrue();
}
