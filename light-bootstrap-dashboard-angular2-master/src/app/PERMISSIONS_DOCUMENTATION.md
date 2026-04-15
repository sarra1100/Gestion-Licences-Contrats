# Système de Rôles et Permissions

## Rôles définis

### 1. Commercial (ROLE_COMMERCIAL)
- ✅ Consulter les licences
- ✅ Consulter les clients
- ❌ Aucune action de modification/suppression

### 2. Technique (ROLE_TECHNIQUE)
- ✅ Consulter les licences
- ✅ Consulter les contrats
- ✅ Consulter les interventions préventives
- ✅ Modifier les interventions préventives
- ✅ Consulter les interventions curatives
- ✅ Ajouter les interventions curatives
- ✅ Modifier les interventions curatives

### 3. Admin Commercial (ROLE_ADMIN_COMMERCIAL)
- ✅ Consulter les licences
- ✅ Consulter les contrats
- ✅ Consulter les clients
- ✅ Consulter les produits
- ✅ Consulter les interventions préventives
- ✅ Consulter les interventions curatives
- ❌ Interdit : Consulter les utilisateurs
- ❌ Interdit : Ajouter, modifier, supprimer

### 4. Admin Technique (ROLE_ADMIN_TECHNIQUE)
- ✅ Consulter les licences
- ✅ Consulter les contrats
- ✅ Ajouter les contrats
- ✅ Modifier les contrats
- ✅ Supprimer les contrats
- ✅ Consulter les clients
- ✅ Ajouter les clients
- ✅ Modifier les clients
- ✅ Supprimer les clients
- ✅ Consulter les interventions préventives
- ✅ Ajouter les interventions préventives
- ✅ Modifier les interventions préventives
- ✅ Supprimer les interventions préventives
- ✅ Consulter les interventions curatives
- ✅ Ajouter les interventions curatives
- ✅ Modifier les interventions curatives
- ✅ Supprimer les interventions curatives

### 5. Super Admin (ROLE_SUPER_ADMIN)
- ✅ Accès total à toutes les ressources
- ✅ Peut ajouter, modifier, supprimer toutes les ressources
- ✅ Gestion des utilisateurs
- ✅ Gestion des produits
- ✅ Gestion des licences, contrats, interventions

### 6. Administrateur (ROLE_ADMINISTRATEUR) - Ancien rôle pour compatibilité
- ✅ Même accès que Super Admin

## Utilisation du Service de Permissions

### Dans les composants TypeScript :

```typescript
import { PermissionService } from 'app/Services/permission.service';

export class MonComposant implements OnInit {
  constructor(private permissionService: PermissionService) {}

  ngOnInit() {
    // Vérifier une permission spécifique
    if (this.permissionService.hasPermission('add_licenses')) {
      // Afficher le bouton d'ajout
    }

    // Vérifier si l'utilisateur peut consulter une ressource
    if (this.permissionService.canView('contracts')) {
      // Charger les contrats
    }

    // Vérifier si l'utilisateur peut modifier une ressource
    if (this.permissionService.canEdit('clients')) {
      // Afficher les boutons de modification
    }
  }
}
```

### Dans les templates (HTML) :

```html
<!-- Afficher un bouton seulement si l'utilisateur a la permission -->
<button *ngIf="permissionService.hasPermission('add_licenses')">
  Ajouter une licence
</button>

<!-- Afficher en fonction de la ressource -->
<button *ngIf="permissionService.canDelete('licenses')">
  Supprimer
</button>
```

## Permissions par ressource

### Licences (licenses)
- `view_licenses` : Consulter
- `add_licenses` : Ajouter
- `edit_licenses` : Modifier
- `delete_licenses` : Supprimer

### Contrats (contracts)
- `view_contracts` : Consulter
- `add_contracts` : Ajouter
- `edit_contracts` : Modifier
- `delete_contracts` : Supprimer

### Clients (clients)
- `view_clients` : Consulter
- `add_clients` : Ajouter
- `edit_clients` : Modifier
- `delete_clients` : Supprimer

### Utilisateurs (users)
- `view_users` : Consulter
- `add_users` : Ajouter
- `edit_users` : Modifier
- `delete_users` : Supprimer

### Produits (products)
- `view_products` : Consulter
- `add_products` : Ajouter
- `edit_products` : Modifier
- `delete_products` : Supprimer

### Interventions préventives (preventive_interventions)
- `view_preventive_interventions` : Consulter
- `add_preventive_interventions` : Ajouter
- `edit_preventive_interventions` : Modifier
- `delete_preventive_interventions` : Supprimer

### Interventions curatives (curative_interventions)
- `view_curative_interventions` : Consulter
- `add_curative_interventions` : Ajouter
- `edit_curative_interventions` : Modifier
- `delete_curative_interventions` : Supprimer
