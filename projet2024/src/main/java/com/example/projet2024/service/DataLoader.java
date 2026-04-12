package com.example.projet2024.service;

import com.example.projet2024.entite.Produit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Cette classe initialise les produits par défaut au démarrage de l'application
 */
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ProduitServiceImpl produitService;

    @Override
    public void run(String... args) throws Exception {
        // Initialiser les produits par défaut (si la table est vide)
        produitService.initializeDefaultProduits();
    }
}
