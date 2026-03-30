# secure-api-hateoas


## Stack technique

- Java 17
- Spring Boot 3.2.x (Web, Data JPA, Security, HATEOAS)
- Springdoc OpenAPI (swagger UI)
- H2 (base en mémoire pour développement)
- JJWT (JWT handling)
- Lombok (génération de code)



## Fonctionnalités exposées (endpoints clés)

- POST /api/auth/register
	- Enregistre un nouvel utilisateur
	- Corps JSON attendu (exemple):

```json
{
	"name": "Alice",
	"email": "alice@example.com",
	"password": "secret",
	"role": "USER"
}
```

- POST /api/auth/login
	- Authentifie un utilisateur et renvoie un token JWT
	- Corps JSON attendu:

```json
{
	"email": "alice@example.com",
	"password": "secret"
}
```

- GET /api/dashboard
	- Retourne les agrégations globales (utilisateurs, événements, réservations, notes, top événements, etc.)
	- Requiert un token JWT (Authorization: Bearer <token>) selon la documentation dans les contrôleurs

- GET /api/dashboard/events/{id}
	- Statistiques détaillées pour un événement particulier
	- Requiert un token JWT

Autres points d'accès utiles :
- H2 Console : `/h2-console` (activée en dev)
- Swagger / OpenAPI UI : `/swagger-ui.html` ou `/swagger-ui/index.html`


## Fonctionnalités détaillées de l'application

### 1. Gestion des utilisateurs et authentification
- **Enregistrement** : création de compte avec email, mot de passe (hashé via BCrypt), nom et rôle
- **Connexion** : génération d'un token JWT valide 30 jours (configurable) ; le token doit être inclus dans les requêtes futures
- **Sécurisation des mots de passe** : BCryptPasswordEncoder appliqué automatiquement
- **Stateless** : aucune session serveur (SessionCreationPolicy.STATELESS) ; toute l'auth passe par JWT

### 2. Gestion des événements
Modèle : `Event` (titre, lieu, date de début/fin, capacité, catégorie)
- **Créer/consulter** des événements
- **Statuts de réservation** : CONFIRMED, CANCELLED, PENDING
- **Catégories** d'événements (multiples par événement)
- **Calcul du taux de remplissage** (places occupées / capacité)

### 3. Système de réservations
- **Créer une réservation** : utilisateur reserve une place à un événement
- **Annuler une réservation** : marquer comme CANCELLED
- **Statuts** : CONFIRMED (réservée), PENDING (en attente), CANCELLED (annulée)
- **Agrégation** : répartition des réservations par statut, taux de remplissage par événement

### 4. Système d'avis (Reviews)
- **Laisser un avis** : note (1-5 étoiles) + commentaire texte sur un événement
- **Calcul** : note moyenne par événement, taux d'avis positifs (note ≥ 4), distribution des notes
- **Métrique** : nombre total d'avis par événement

### 5. Dashboard & Statistiques
#### Dashboard global (`GET /api/dashboard`)
Agrégations de la plateforme entière :
- Nombre total : utilisateurs, événements, réservations, avis, catégories
- Taux de remplissage moyen (moyenne des taux par événement)
- Répartition des réservations : nombre de CONFIRMED, CANCELLED, PENDING
- Nombre d'événements par catégorie
- Note globale moyenne
- **Top 5 événements** :
  - Par taux de remplissage (plus pleins)
  - Par note moyenne (mieux notés)

#### Dashboard par événement (`GET /api/dashboard/events/{id}`)
Statistiques détaillées pour un seul événement :
- Informations : titre, lieu, dates, catégories
- Capacité et places disponibles
- Taux de remplissage (en %)
- Répartition des réservations (CONFIRMED / CANCELLED / PENDING)
- Note moyenne, distribution complète des avis (1, 2, 3, 4, 5 étoiles)
- Pourcentage d'avis positifs (≥ 4 étoiles)

### 6. Principes HATEOAS
Les réponses des contrôleurs incluent des liens HTTP (`_links`) :
- Exemple dans `AuthController` : les réponses login/register incluent un lien `self`
- Permet aux clients de découvrir les actions possibles sans mémoriser les URLs
- Format : `{ "rel": "self", "href": "/api/auth/login" }`

### 7. Documentation interactive (OpenAPI/Swagger)
- Tous les endpoints documentés avec descriptions, paramètres, exemples de réponse
- `DashboardController` : annotations `@Operation`, `@ApiResponse`, `@Parameter` détaillées
- Accessibles sur `/swagger-ui.html` ou `/v3/api-docs` (JSON brut)

### 8. Sécurité et contrôle d'accès
- **JWT (JJWT)** : tokens signés, validation du timestamp (expiration)
- **Filtre personnalisé** : `JwtAuthenticationFilter` intercepte toutes les requêtes
- **Matrice de contrôle** (SecurityConfig) :
  - `/api/auth/**` : public (register, login)
  - `/api/dashboard/**` : public (statistiques sans auth)
  - `/h2-console/**` : public (dev uniquement)
  - `/v3/api-docs/**`, `/swagger-ui/**` : public (documentation)
  - Autres routes : authentification requise (Authorization header)
- **Password encoding** : BCrypt (force 10)

## Prérequis du système

### Configuration minimale requise
- **JDK** : Java 17 ou supérieur (recommandé : OpenJDK 17+, Eclipse Temurin, ou Oracle JDK 17+)
- **Maven** : 3.8.1 ou supérieur (inclus via `mvnw`/`mvnw.cmd`)
- **RAM** : 2 GB minimum pour la compilation et l'exécution
- **Disque** : 500 MB pour le projet compilé + dépendances Maven

### Versions testées
- **Java** : OpenJDK 17 LTS (primary), compatible avec Java 18-21
- **Spring Boot** : 3.2.4
- **Maven** : 3.9.x (via Maven wrapper)
- **Systèmes d'exploitation** : Windows 10/11, macOS 12+, Linux (Ubuntu 20.04+)



### IDE recommandés
- **IntelliJ IDEA** (Community ou Ultimate) — version 2022.2+
- **VS Code** + Extensions Java
- **Eclipse IDE** avec plugins Spring Tools

## Build depuis IntelliJ IDEA



## Swagger / OpenAPI

La doc interactive est fournie par Springdoc et disponible (après démarrage) sur :

- `http://localhost:8080/swagger-ui.html` ou
- `http://localhost:8080/swagger-ui/index.html`
version enligne 
- `https://secure-api-hateoas.onrender.com/swagger-ui/index.html#`

Les routes `/v3/api-docs/**` et `/swagger-ui/**` sont exposées sans authentification dans la configuration actuelle.


