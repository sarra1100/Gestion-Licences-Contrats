# Guide d'Implémentation des Permissions - État actuel

## Composants modifiés 

### 1. **Eset - Affichage Component** ✅
   - **Fichier** : `src/app/Eset/affichage/affichage.component.ts`
   - **Permissions appliquées** :
     - Bouton "Ajouter une nouvelle ESET" : `canAdd('licenses')`
     - Bouton "Ajouter un nouveau produit" : `canAdd('products')`
     - Barre de recherche : `canView('licenses')`
     - Bouton "Voir détails" : `canView('licenses')`
     - Bouton "Modifier" : `canEdit('licenses')`
     - Bouton "Supprimer" : `canDelete('licenses')`
     - Bouton "Expirer" : `canEdit('licenses')`

### 2. **Products - Formulaire d'ajout d'ESET** ✅
   - **Fichier** : `src/app/products/products.component.ts` et `.html`
   - **Permissions appliquées** :
     - Bouton poubelle (supprimer produit) : `canDelete('products')`

---

## Composants à modifier prochainement

### 3. **Contrats (à faire)**
   - Ajouter vérifications pour `view_contracts`, `add_contracts`, `edit_contracts`, `delete_contracts`

### 4. **Clients (à faire)**
   - Ajouter vérifications pour `view_clients`, `add_clients`, `edit_clients`, `delete_clients`

### 5. **Interventions Préventives (à faire)**
   - Ajouter vérifications pour interventions préventives

### 6. **Interventions Curatives (à faire)**
   - Ajouter vérifications pour interventions curatives

### 7. **Utilisateurs (à faire)**
   - Restriction : admins commerciaux ne voient PAS les utilisateurs
   - Super admin peut gérer tous les utilisateurs

---

## Comment utiliser le service de permissions

### Dans un composant TypeScript :
```typescript
import { PermissionService } from 'app/Services/permission.service';

export class MonComposant implements OnInit {
  constructor(public permissionService: PermissionService) {}

  ngOnInit() {
    // Vérifier une permission directe
    if (this.permissionService.hasPermission('add_licenses')) {
      // Afficher le bouton d'ajout
    }

    // Vérifier avec la méthode rapide
    if (this.permissionService.canEdit('contracts')) {
      // Afficher les boutons de modification
    }
  }

  canPerformAction() {
    return this.permissionService.canDelete('clients');
  }
}
```

### Dans le template HTML :
```html
<!-- Afficher un bouton seulement si l'utilisateur a la permission -->
<button *ngIf="permissionService.canAdd('licenses')" (click)="addLicense()">
  Ajouter une licence
</button>

<!-- Masquer les actions basées sur les permissions -->
<div *ngIf="permissionService.canView('contracts')" class="contracts-list">
  <!-- Afficher la liste des contrats -->
</div>

<!-- Boutons contextuels -->
<button *ngIf="permissionService.canEdit('clients')" (click)="editClient(client)">
  Modifier
</button>
<button *ngIf="permissionService.canDelete('clients')" (click)="deleteClient(client)">
  Supprimer
</button>
```

---

## Sécurité - Points importants

1. **Public vs Private** : Le permissionService doit être `public` pour être accessible dans le template
2. **Backend validation** : Les permissions côté frontend sont pour l'UX. Le backend DOIT aussi valider les permissions
3. **Caching** : Les permissions sont chargées au démarrage. Si le rôle change, appeler `permissionService.refreshUserRole()`

---

## Récapitulatif des permissions par rôle

| Rôle | Licences | Contrats | Clients | Produits | Interventions | Utilisateurs |
|------|----------|----------|---------|----------|----------------|--------------|
| Commercial | View | ❌ | View | ❌ | ❌ | ❌ |
| Technique | View | View | View | ❌ | View+Add+Edit | ❌ |
| Admin Commercial | View | View | View | View | View | ❌ |
| Admin Technique | View | CUD | CUD | ❌ | CUD | ❌ |
| Super Admin | CUD | CUD | CUD | CUD | CUD | CUD |

*CUD = Create, Update, Delete*
