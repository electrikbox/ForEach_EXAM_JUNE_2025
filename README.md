# EXAM_JUNE_2025 - Backend Spring Boot

## Prérequis
- Docker et Docker Compose installés
- Java 17+ et Maven (pour build manuel en local si besoin)

---

## Lancement en mode **développement** (hot reload, code monté)

1. **Construire l'image dev** (si modif du Dockerfile.dev) :
```bash
docker-compose build backend-dev
```
2. **Lancer la base de données et le backend dev** :
```bash
docker-compose up db backend-dev
```
3. **Accéder à l'API** :
   - [http://localhost:8000](http://localhost:8000)

---

## Lancement en mode **production** (image figée, JAR)

1. **Compiler le projet et générer le JAR** :
```bash
cd backend
mvn clean install
```
2. **Lancer la base de données et le backend prod** :
```bash
docker-compose up db backend
```
3. **Accéder à l'API** :
   - [http://localhost:8081](http://localhost:8081)

---

## Ports utilisés
- **8000** : backend-dev (développement)
- **8081** : backend (production)
- **5433** : PostgreSQL (accès local à la BDD)

---

## Commandes utiles
- **Arrêter et nettoyer tout** :
```bash
docker-compose down -v --rmi all
docker system prune -f
```
- **Vérifier le port 8000/8081** :
```bash
sudo lsof -i :8000
sudo lsof -i :8081
```

---

## Tests
Pour lancer les tests, utilisez la commande suivante depuis le dossier où se trouve le docker-compose.yml :
```bash
docker compose exec backend-dev ./mvnw clean verify
```

