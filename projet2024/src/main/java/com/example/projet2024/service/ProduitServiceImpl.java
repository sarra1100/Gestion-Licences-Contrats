package com.example.projet2024.service;

import com.example.projet2024.entite.Produit;
import com.example.projet2024.repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProduitServiceImpl {

    @Autowired
    private ProduitRepository produitRepository;

    /**
     * Récupère tous les produits actifs
     */
    public List<Produit> getAllProduitsActifs() {
        return produitRepository.findByActifTrue();
    }

    /**
     * Récupère tous les produits (actifs et inactifs)
     */
    public List<Produit> getAllProduits() {
        return produitRepository.findAll();
    }

    /**
     * Récupère un produit par son ID
     */
    public Optional<Produit> getProduitById(Long id) {
        return produitRepository.findById(id);
    }

    /**
     * Récupère un produit par son code
     */
    public Optional<Produit> getProduitByCode(String code) {
        return produitRepository.findByCode(code);
    }

    /**
     * Ajoute un nouveau produit
     */
    public Produit addProduit(Produit produit) {
        return produitRepository.save(produit);
    }

    /**
     * Met à jour un produit existant
     */
    public Produit updateProduit(Produit produit) {
        return produitRepository.save(produit);
    }

    /**
     * Désactive un produit
     */
    public void desactiverProduit(Long id) {
        Optional<Produit> produit = produitRepository.findById(id);
        if (produit.isPresent()) {
            Produit p = produit.get();
            p.setActif(false);
            produitRepository.save(p);
        }
    }

    /**
     * Supprime un produit
     */
    public void deleteProduit(Long id) {
        produitRepository.deleteById(id);
    }

    /**
     * Initialise les produits par défaut
     */
    public void initializeDefaultProduits() {
        // Vérifier si les produits existent déjà
        if (produitRepository.count() > 0) {
            return;
        }

        // Ajouter les produits ESET par défaut
        produitRepository.saveAll(List.of(
                new Produit("eset_protect_entry", "ESET PROTECT Entry"),
                new Produit("eset_protect_entry_on_prem", "ESET PROTECT Entry On-Prem"),
                new Produit("eset_protect_advanced", "ESET PROTECT Advanced"),
                new Produit("eset_protect_advanced_on_prem", "ESET PROTECT Advanced On-Prem"),
                new Produit("eset_protect_essential", "ESET PROTECT Essential"),
                new Produit("eset_protect_essential_on_prem", "ESET PROTECT Essential On-Prem"),
                new Produit("eset_protect_essential_plus_on_prem", "ESET PROTECT Essential Plus On-Prem"),
                new Produit("eset_protect_enterprise_on_prem", "ESET PROTECT Enterprise On-Prem"),
                new Produit("eset_home_security_essential", "Eset Home Security Essential"),
                new Produit("eset_protect_enterprise", "Eset Protect Enterprise"),
                new Produit("eset_endpoint_encryption", "Eset Endpoint Encryption"),
                new Produit("eset_endpoint_encryption_pro", "Eset Endpoint Encryption Pro"),
                new Produit("eset_mail_security", "Eset Mail Security"),
                new Produit("eset_smart_security_premium", "Eset Smart Security Premium"),
                new Produit("eset_secure_authentication", "Eset Secure Authentication"),
                new Produit("eset_internet_security", "Eset Internet Security"),
                new Produit("eset_server_security", "Eset Server Security"),
                new Produit("eset_protect_mail_plus", "Eset Protect Mail Plus"),
                new Produit("eset_protect_complete", "Eset Protect Complete")
        ));
    }
}
