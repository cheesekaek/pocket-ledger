# Pocket Ledger

A Java + Spring Boot API that creates, saves and retrieves all expenses made by a user. The expenses can be filtered
by description, category, date (specific) and dates (range).

---

## Project Structure
```
pocket-ledger/
│
├── .github/workflows/ # CI workflow
├── .mvn/wrapper/ # Contains maven-wrapper.properties
├── src/ 
    ├── main/java/com/kaek/pl/ # Main application + controller, domain, exception, repository, service
    └── test/
        ├── java/com/kaek/pl # Application test + tests for controller, mapper and service
        └── resources # Contains application.properties file for test database
├── .gitattributes
├── .gitignore
├── Dockerfile # Build Docker image
├── docker-compose.yml # Run containers for backend and database
├── main.py # API entrypoint
├── mvnw
├── mvnvw.cmd
├── pom.xml
└── README.md
```
---



