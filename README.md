# 🛒 Shop-2026 - Backend E-Commerce

Bienvenue sur le backend de **Shop-2026**, une application e-commerce modulaire construite avec **Spring Boot**, suivant les principes de la **Clean Architecture** (Hexagonale) et utilisant **Apache Kafka** pour la gestion des événements asynchrones.

## 🚀 Technologies Utilisées

* **Langage :** Java 22
* **Framework :** Spring Boot 3.2
* **Build Tool :** Maven
* **Base de données :** H2 (In-Memory)
* **Messaging :** Apache Kafka (Mode KRaft - sans Zookeeper)
* **Conteneurisation :** Docker & Docker Compose
* **Documentation API :** Swagger UI (OpenAPI)
* **Outils :** Lombok, Spring Data JPA

---

## 🏗 Architecture

Le projet respecte une séparation stricte des responsabilités :

1.  **Domain** : Le cœur du métier (Entités, Exceptions, Interfaces des Ports). Aucune dépendance externe (Framework agnostique).
2.  **UseCase** : La logique applicative (Services, DTOs). Orchestre le domaine.
3.  **Adapters** :
    * **Infrastructure** : Implémentation des ports (Repository JPA, Kafka Producers/Listeners).
    * **Rest** : Contrôleurs API (Points d'entrée HTTP).

---

## 🐳 Démarrage Rapide (Docker) - Recommandé

L'application est entièrement "dockerisée" avec une compilation multi-stage. **Pas besoin d'installer Java ou Maven sur votre machine.**

### Pré-requis
* Docker Desktop ou Docker Engine installé.

### Lancer le projet
À la racine du projet, exécutez la commande suivante pour construire et lancer tous les services :

```bash
docker compose up --build -d
```

### Accès aux services
Une fois les conteneurs démarrés :

* **Backend API (Swagger) :** [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
* **Kafka UI (Visualisation) :** [http://localhost:8081](http://localhost:8081)

### Arrêter le projet
Pour tout arrêter et nettoyer les volumes (données Kafka) :
```bash
docker compose down -v
```

---

## 🛠 Démarrage Local (Développement)

Si vous souhaitez lancer l'application via IntelliJ ou un terminal sans Dockeriser le backend (mais avec Kafka en Docker) :

1.  **Démarrer Kafka uniquement :**
    Lancez uniquement les services d'infrastructure :
    ```bash
    docker compose up kafka kafka-ui -d
    ```

2.  **Lancer l'application Spring Boot avec  IntelliJ**


---

## 📨 Gestion des Événements Kafka

Le système utilise Kafka pour découpler les services et réagir aux changements d'état métier.

### 1. Inscription Utilisateur (`user-created`)
* **Déclencheur :** `POST /users` (Création d'un compte)
* **Producteur :** `KafkaUserEventAdapter`
* **Consommateur :** `UserEventListener`
* **Action :** Le service de notification reçoit l'événement et simule l'envoi d'un email de bienvenue.

### 2. Validation de Commande (`order-created`)
* **Déclencheur :** `PATCH /orders` 
* **Producteur :** `KafkaOrderEventAdapter`
* **Consommateur :** `OrderEventListener`
* **Action :** Le service logistique reçoit l'événement, génère une étiquette d'expédition et prépare le colis.

---

## 👥 Auteurs

Projet réalisé dans le cadre du cours d'Architecture Logicielle (ESIEA - 4A).

* **Abdelhadi BAGHDADLI**
* **Julian DOLOIR**