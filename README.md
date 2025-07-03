# EXAM_JUNE_2025 - Application Cocktail Bar

## Prérequis
- Docker et Docker Compose installés
- Java 17+ et Maven (pour build manuel en local si besoin)
- Node.js 20+ (pour build manuel en local si besoin)

---

## Mode Développement (hot reload)

1. **Lancer l'environnement de développement complet** :
```bash
docker-compose up -d db backend-dev frontend-dev
```

2. **Accéder aux applications** :
   - Frontend : [http://localhost:5173](http://localhost:5173)
   - API Backend : [http://localhost:8080](http://localhost:8080)
   - Base de données : localhost:5433

3. **Arrêter l'environnement** :
```bash
docker-compose down
```

---

## Mode Production

1. **Lancer l'environnement de production** :
```bash
docker-compose up -d db backend frontend
```

2. **Accéder aux applications** :
   - Frontend : [http://localhost:80](http://localhost:80)
   - API Backend : [http://localhost:8080](http://localhost:8080)
   - Base de données : localhost:5433

---

## Ports utilisés

- **80** : frontend (production)
- **5173** : frontend (développement)
- **8080** : backend (développement et production)
- **5433** : base de données PostgreSQL

---

## Commandes utiles

### Reconstruction des images

- **Reconstruire une image spécifique** :
```bash
docker-compose build frontend  # Pour l'image de production du frontend
docker-compose build frontend-dev  # Pour l'image de développement du frontend
docker-compose build backend  # Pour l'image de production du backend
docker-compose build backend-dev  # Pour l'image de développement du backend
```

### Logs et Debug

- **Voir les logs** :
```bash
docker-compose logs -f frontend-dev  # Logs du frontend en dev
docker-compose logs -f backend-dev   # Logs du backend en dev
```

### Nettoyage

- **Arrêter et nettoyer tout** :
```bash
docker-compose down -v --rmi all
docker system prune -f
```

---

## Tests
Pour lancer les tests du backend :
```bash
docker compose exec backend-dev ./mvnw clean verify
```

Pour lancer les tests du frontend :
```bash
docker compose exec frontend-dev npm test
```